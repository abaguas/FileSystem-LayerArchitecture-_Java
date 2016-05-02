package pt.tecnico.mydrive.domain;

import java.math.BigInteger;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import pt.tecnico.mydrive.exception.ExpiredSessionException;
import pt.tecnico.mydrive.exception.InvalidOperationException;
import pt.tecnico.mydrive.exception.InvalidPasswordException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.NoSuchUserException;

public class Session extends Session_Base {

	public Session(User u, long token, MyDrive mydrive) {
		super();
		setCurrentUser(u);
		setToken(token);
		setMd(mydrive);
		setCurrentDir(u.getMainDirectory());
		DateTime actual = new DateTime();
		setTimestamp(actual);
	}

	public Session(String username, String password, MyDrive mydrive) {
		setMd(mydrive);
		removeExpiredSessions();
		validateUser(username, password);
		long token = new BigInteger(64, new Random()).longValue();
		setPrivateToken(token);

	}

	public void validateUser(String username, String password) throws NoSuchUserException {
		User user = getMd().getUserByUsername(username);

		if (user.getPassword().equals(password)) {
			throw new InvalidPasswordException();
		}
	}

	@Override
	public void setToken(long token) throws InvalidOperationException {
		throw new InvalidOperationException("setToken()");
	}

	private void setPrivateToken(long token) {
		while (getMd().getSessions().contains(token)) {
			token = new BigInteger(64, new Random()).longValue();
		}
		super.setToken(token);
	}

	private void removeExpiredSessions() {

		Set<Session> sessions = getMd().getSessions();
		DateTime actual = new DateTime();
		DateTime twohoursbefore = actual.minusHours(2);

		for (Session session : sessions) {
			int result = DateTimeComparator.getInstance().compare(twohoursbefore, session.getTimestamp());

			if (result > 0) {
				getMd().removeSessions(session);
			}
		}
	}
	
	 public static Session getSession(long token, MyDrive md) throws InvalidTokenException{
		Session s = null;
		Set<Session> sessions = md.getSessions();
		
		for (Session session : sessions) {
			if(session.getToken() == token){
				s=session;
				break;
			}
		}
		
		if(s==null){
			throw new InvalidTokenException();
		}
		
		DateTime actual = new DateTime();
		DateTime twohoursbefore = actual.minusHours(2);
		int result = DateTimeComparator.getInstance().compare(twohoursbefore, s.getTimestamp());
		
		if (result > 0) {
			Session newSession = new Session(s.getCurrentUser().getUsername(), s.getCurrentUser().getPassword(),md);
			md.removeSessions(s);
			s=newSession;
		}
		return s;
	}

	@Override
	public void setMd(MyDrive md) {
		if (md == null) {
			super.setMd(null);
		} else {
			md.addSessions(this);
		}
	}

}
