package pt.tecnico.mydrive.domain;
import java.util.Set;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import pt.tecnico.mydrive.exception.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class Login extends Login_Base {
    
    public Login() {
        super();
    }

    public  long createSession(String username, String password) throws InvalidUsernameOrPasswordException, NoSuchUserException {
    	User user= getMyDrive().getUserByUsername(username);
    	if(user.getPassword().equals(password)){
    		long token= new BigInteger(64, new Random()).longValue();
    		new Session(user,token,this);
    		return token;
    	}
    	else{
    		throw new InvalidUsernameOrPasswordException(username);
    	}
    }
    public Session getSessionByToken(long token) throws ExpiredSessionException {
    	Set<Session> sessions = getSessionSet();
    	DateTime actual = new DateTime();
        DateTime twohoursbefore = actual.minusHours(2);
    	for(Session session : sessions){
    		int result = DateTimeComparator.getInstance().compare(twohoursbefore, session.getTimestamp());
    		if(session.getToken()== token){
    			if(result == -1 || result == 0 ){
    				session.setTimestamp(actual);
    				return session;
    			}
    			else{
    				//log.warm(); fix me
    				removeSession(session);

    			} 
    		}
    		else{
    			if(result == 1){
    				removeSession(session);
    			}
    		}
    	}
    	throw new ExpiredSessionException();
    }

    public Directory getCurrentDirByToken(long token) throws ExpiredSessionException {
    	Session session = getSessionByToken(token);
    	return session.getCurrentDir();
    }

    public User getCurrentUserByToken(long token) throws ExpiredSessionException{
    	Session session = getSessionByToken(token);
    	return session.getCurrentUser();
    }

    public void setCurrentDirByToken(long token, Directory dir) throws ExpiredSessionException{
    	Session session = getSessionByToken(token);
    	session.setCurrentDir(dir);

    }
    
}
