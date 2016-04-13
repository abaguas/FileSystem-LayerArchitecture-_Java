package pt.tecnico.mydrive.service;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.InvalidUsernameOrPasswordException;
import pt.tecnico.mydrive.exception.NoSuchUserException;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Session;


public class LoginService extends MyDriveService{
	
	
	private String _username;
	private String _password;
	private long _returntoken;

	public LoginService(String username, String password) {
		_password= password;
		_username= username;
	}
	public  long createSession(String username, String password, MyDrive mydrive) throws InvalidUsernameOrPasswordException, NoSuchUserException {
        User user= mydrive.getUserByUsername(username);
        if(user.getPassword().equals(password)){
            long token= new BigInteger(64, new Random()).longValue();
            DateTime actual = new DateTime();
            Session s = new Session(user,token,mydrive);
            s.setTimestamp(actual);
            return token;
        }
        else{
            throw new InvalidUsernameOrPasswordException(username);
        }
    }
	
	@Override
	protected void dispatch() throws InvalidUsernameOrPasswordException, NoSuchUserException {
		MyDrive md = MyDrive.getInstance();
		_returntoken= createSession(_username,_password,md);
          
    }

    public long result(){
    	return _returntoken;

    }
	
	
}


