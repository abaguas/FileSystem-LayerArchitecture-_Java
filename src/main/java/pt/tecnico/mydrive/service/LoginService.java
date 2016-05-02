package pt.tecnico.mydrive.service;
import pt.tecnico.mydrive.exception.InvalidPasswordException;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;


public class LoginService extends MyDriveService{
	
	
	private String _username;
	private String _password;
	private long _returntoken;

	public LoginService(String username, String password) {
		_password= password;
		_username= username;
	}



	@Override
	protected void dispatch() throws InvalidPasswordException{
		MyDrive md = MyDrive.getInstance();
		
		Session s = new Session(_username, _password, md);
		_returntoken=s.getToken();
  
    }

    public long result(){
    	return _returntoken;

    }
	
	
}


