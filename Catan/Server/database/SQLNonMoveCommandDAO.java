package database;

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
import java.util.ArrayList;
import java.util.List;

public class SQLNonMoveCommandDAO extends ANonMoveCommandDAO {

	private SQLPlugin db;
	
	public SQLNonMoveCommandDAO(SQLPlugin sqlPlugin) {
		db = sqlPlugin;
	}
	/**
	 * Adds a row with the type and command which was processed
	 */
	@Override
	public void add(Serializable command, String type){

	    try {
			PreparedStatement pstmt = db.getConnection().prepareStatement("insert into NonMoveCommand (type,command) values (?,?)");
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(baos);
		    oos.writeObject(command);
		    byte[] modelAsBytes = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(modelAsBytes);
			pstmt.setString(1, type);
		    pstmt.setBinaryStream(2, bais, modelAsBytes.length);
		    pstmt.executeUpdate();
		    db.getConnection().commit();
		    pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the list of commands of type
	 */
	@Override
	public List<Serializable> getAll(String type){
		List<Serializable> model=new ArrayList<Serializable>();
		Serializable temp=null;
		Statement stmt=null;
		try {
			stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT command FROM NonMoveCommand where type = '" + type+"'");
		    db.getConnection().commit();
			while (rs.next()) {
				byte[] st = (byte[]) rs.getObject(1);
				ByteArrayInputStream baip = new ByteArrayInputStream(st);
				ObjectInputStream ois = new ObjectInputStream(baip);
				temp = (Serializable) ois.readObject();
				model.add(temp);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    return model;
	}
	/**
	 * Drop table, create empty table
	 */
	@Override
	public void clear(){

		String dropNonMoveCommand="DROP TABLE NonMoveCommand";
		String makeNonMoveCommand="CREATE TABLE NonMoveCommand " +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + 
				"type CHAR(4) NOT NULL," + 
				"command BLOB NOT NULL)";
		Statement stmt = null;
		try {		
			stmt = db.getConnection().createStatement();
			stmt.addBatch(dropNonMoveCommand);
			stmt.addBatch(makeNonMoveCommand);
			stmt.executeBatch();
		    db.getConnection().commit();
		}
		catch (SQLException e) {
			System.out.println("Failed clearing NonMoveCommand table:");
			e.printStackTrace();
		}		
		finally {
			SQLPlugin.safeClose(stmt);
		}
	}
}