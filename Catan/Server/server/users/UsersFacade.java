package server.users;

import java.util.ArrayList;

/**
 * This Facade implements the Login and Register commands
 *
 */
public class UsersFacade extends AUsersFacade {
	
	public UsersFacade() {
		users = new ArrayList<IUser>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add(new User("Bobby", "bobby"));
		}};
	}	
}