package sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import database.AModelDAO;

public class SQLUsersDAO extends AModelDAO{

	private SQLPlugin db;
	
	public SQLUsersDAO(SQLPlugin sqlPlugin) {
		db = sqlPlugin;
	}
	
	/**
	 * Saves the list of users(serialize it first) into the db-only one blob/row
	 */
	@Override
	public void save(Object model){
		XStream xStream = new XStream(new DomDriver());

	    try {
//			PreparedStatement pstmt = db.getConnection().prepareStatement("insert into Users (users) values (?)");
//		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		    ObjectOutputStream oos = new ObjectOutputStream(baos);
//		    oos.writeObject(model);
//		    byte[] modelAsBytes = baos.toByteArray();
//			ByteArrayInputStream bais = new ByteArrayInputStream(modelAsBytes);
//		    pstmt.setBinaryStream(1, bais, modelAsBytes.length);
			String xml = xStream.toXML(model);
			PreparedStatement pstmt = db.getConnection().prepareStatement("insert into Users (users) values (?)");
			pstmt.setString(1, xml);	
	    	pstmt.executeUpdate();
		    db.getConnection().commit();
		    pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the blob representing the list of users
	 */
	@Override
	public Object load(){
		Object model=null;
		Statement stmt=null;
		XStream xStream = new XStream(new DomDriver());
		try {
			stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT users FROM Users");
		    db.getConnection().commit();
			while (rs.next()) {
//				byte[] st = (byte[]) rs.getObject(1);
//				ByteArrayInputStream baip = new ByteArrayInputStream(st);
//				ObjectInputStream ois = new ObjectInputStream(baip);
				String xml = (String)rs.getString(1);
				model = (Object) xStream.fromXML(xml);
//				model = ois.readObject();
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return model;
	}
	
	/**
	 * Drop table, create empty table
	 */
	@Override
	public void clear(){

		String dropUsers="DROP TABLE Users";
		String makeUsers="CREATE TABLE Users " +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + 
				"users BLOB NOT NULL)";
		Statement stmt = null;
		try {		
			stmt = db.getConnection().createStatement();
			stmt.addBatch(dropUsers);
			stmt.addBatch(makeUsers);
			stmt.executeBatch();
		    db.getConnection().commit();
		}
		catch (SQLException e) {
			System.out.println("Failed clearing user table:");
			e.printStackTrace();
		}		
		finally {
			SQLPlugin.safeClose(stmt);
		}
	}
}
