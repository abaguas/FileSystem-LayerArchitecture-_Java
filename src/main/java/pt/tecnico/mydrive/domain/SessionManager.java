package pt.tecnico.mydrive.domain;

import java.math.BigInteger;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.mydrive.exception.InvalidOperationException;
import pt.tecnico.mydrive.exception.InvalidPasswordException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.NoSuchUserException;
import pt.tecnico.mydrive.exception.OutDatedUserException;

public class SessionManager extends SessionManager_Base {
    
	public static SessionManager getInstance(){
        SessionManager sessionManager = FenixFramework.getDomainRoot().getMyDrive().getSessionManager();
        if (sessionManager != null){
            return sessionManager;
        }
        return new SessionManager();
    }
    
	@Override
	public Set<Session> getSessionSet()  throws InvalidOperationException {
		throw new InvalidOperationException("getSessionSet");
	}
	
	@Override
	public void removeSession(Session session) throws InvalidOperationException {
		throw new InvalidOperationException("getSessionSet");
	}
	
	public void removeSessionByToken(long token) {
		Session session = getSession(token);
		super.removeSession(session);
	}
	
	
	public void removeExpiredSessions() {
		Set<Session> sessions = super.getSessionSet();
		DateTime actual = new DateTime();
		DateTime twohoursbefore = actual.minusHours(2);

		for (Session session : sessions) {
			int result = DateTimeComparator.getInstance().compare(twohoursbefore, session.getTimestamp());
			
			if (!(session.getUser().equals( getMd().getGuestUser() ))) {
				if (result > 0) {
					super.removeSession(session);
				}
			}
		}
	}
	
	public Session getSession(long token) throws InvalidTokenException{
		Session session = null;
		Set<Session> sessions = super.getSessionSet();
		
		for (Session s : sessions) {
			if(s.getToken() == token){
				session=s;
				break;
			}
		}
		
		if(session==null){
			throw new InvalidTokenException();
		}
		
		boolean expire = session.expiration();
		
		if(expire){
			Session newSession = new Session(session.getUser().getUsername(), session.getUser().getPassword(), this);
			removeSession(session);
			session = newSession;
		}
		else{
			//Session sets its Timestamp
		}
		
		return session;
	}
	
	public User validateUser(String username, String password) throws NoSuchUserException,OutDatedUserException {
		User user = getMd().getUserByUsername(username);

		if (!user.getPassword().equals(password)) {
			throw new InvalidPasswordException();
		}
		else if((user.getPassword().length()<8) && !((user.getUsername().equals("root")|| user.getUsername().equals("nobody")))){
			throw new OutDatedUserException(user.getUsername());
		}
		return user;
	}
	
	public long generateToken() {
		long token = new BigInteger(64, new Random()).longValue();
		while (super.getSessionSet().contains(token)) {
			token = new BigInteger(64, new Random()).longValue();
		}
		return token;
	}
	
}

