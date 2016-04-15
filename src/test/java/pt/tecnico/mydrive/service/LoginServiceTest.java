package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.junit.Test;
import pt.tecnico.mydrive.exception.InvalidUsernameOrPasswordException;
import pt.tecnico.mydrive.exception.NoSuchUserException ;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.MyDrive;


public class LoginServiceTest extends AbstractServiceTest{
	protected void populate(){

		MyDrive md = MyDrive.getInstance();
		User u1= new User("user1", "1234", "Utilizador1");
		md.addUsers(u1);


	}

	@Test
	public void SuccessfulLogin(){
		LoginService login = new  LoginService("user1", "1234");
		login.execute();
		long token= login.result();
		assertNotNull (token);
		 //Sucesso
	}
	
	@Test(expected = InvalidUsernameOrPasswordException.class)
		public void UnsuccessfulLoginDueToWrongPassword() throws InvalidUsernameOrPasswordException{
		LoginService login= new LoginService("user1", "1235");
		login.execute();
 
		}
	
	@Test(expected = InvalidUsernameOrPasswordException.class)
		public void UnsuccessfulLoginDueToNonExistentUser() throws InvalidUsernameOrPasswordException {
		LoginService login = new LoginService ("NonExistent", "1243");
		login.execute();

		}
	
	
}	
