package pt.tecnico.mydrive.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import pt.tecnico.mydrive.exception.InvalidOperationException;
import pt.tecnico.mydrive.exception.NoSuchUserException;
import pt.tecnico.mydrive.exception.OutDatedUserException;

public class Session extends Session_Base {

	public Session(String username, String password, SessionManager sm) throws NoSuchUserException,OutDatedUserException {
		super.setSessionManager(sm);
		//sm.addSession(this);
		User user = sm.validateUser(username, password);
		setPrivateCurrentUser(user);
		setCurrentDir(user.getMainDirectory());
		setPrivateToken();
		setPrivateTimestamp();
		sm.removeExpiredSessions();
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
	
	public boolean expiration(){
		DateTime actual = new DateTime();
		DateTime twohoursbefore = actual.minusHours(2);
		int result = DateTimeComparator.getInstance().compare(twohoursbefore, getTimestamp());
		
		if (result > 0) {
			return true;
		}
		else{
			setPrivateTimestamp();
			return false;
		}
	}
	
	@Override
	public void setUser(User u) {
		throw new InvalidOperationException("setCurrentUser");
	}
	
	@Override
	public void setCurrentDir(Directory dir) {
		super.setCurrentDir(dir);
	}
	
	private void setPrivateCurrentUser(User user){
		super.setUser(user);
	}

	@Override
	public void addEnv(Env e) {
		for(Env env : getEnvSet())
			if(env.getName().equals(e.getName())) {
				env.setValue(e.getValue());
				return;
			}
		super.addEnv(e);
	}
	
}
