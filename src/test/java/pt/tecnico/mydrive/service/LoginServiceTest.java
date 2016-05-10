package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.mydrive.exception.InvalidPasswordException;
import pt.tecnico.mydrive.exception.NoSuchUserException;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.MyDrive;


public class LoginServiceTest extends AbstractServiceTest{
	protected void populate(){

		MyDrive md = MyDriveService.getMyDrive();
		User u1= new User(md,"user1", "1234", "Utilizador1");
		md.addUsers(u1);


	}

	@Test
	public void SuccessfulLogin(){
		LoginService login = new  LoginService("user1", "grande1234");
		login.execute();
		long token= login.result();
		assertNotNull (token);

	}
	
	@Test(expected = InvalidPasswordException.class)
		public void UnsuccessfulLoginDueToWrongPassword() throws InvalidPasswordException{
		LoginService login= new LoginService("user1", "grande1235");
		login.execute();
 
		}
	
	@Test(expected = NoSuchUserException.class)
		public void UnsuccessfulLoginDueToNonExistentUser() throws InvalidPasswordException {
		LoginService login = new LoginService ("NonExistent", "grande1243");
		login.execute();

		}
	
	
}	
