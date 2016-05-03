package pt.tecnico.mydrive.domain;

import org.joda.time.DateTime;
import pt.tecnico.mydrive.exception.InvalidOperationException;
import pt.tecnico.mydrive.exception.NoSuchUserException;

public class Session extends Session_Base {

	public Session(String username, String password, MyDrive myDrive) throws NoSuchUserException {
		SessionManager sm = myDrive.getSessionManager();
		setSessionManager(sm);
		sm.addSession(this);
		sm.removeExpiredSessions();
		User user = sm.validateUser(username, password);
		setPrivateCurrentUser(user);
		setCurrentDir(user.getMainDirectory());
		setPrivateToken();
		setPrivateTimestamp();
	}

	@Override
	public void setToken(long token) throws InvalidOperationException {
		throw new InvalidOperationException("setToken()");
	}
	
	private void setPrivateToken() {
		long token = getSessionManager().generateToken();
		super.setToken(token);
	}
	
	@Override
	public void setTimestamp(DateTime timestamp) throws InvalidOperationException {
		throw new InvalidOperationException("setTimestamp");
	}
	
	private void setPrivateTimestamp(){
		super.setTimestamp(new DateTime());
	}
	
	@Override
	public void setCurrentUser(User u) {
		throw new InvalidOperationException("setCurrentUser");
	}
	
	private void setPrivateCurrentUser(User user){
		super.setCurrentUser(user);
	}
	
}
