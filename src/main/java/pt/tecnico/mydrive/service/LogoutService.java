package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.exception.MyDriveException;

public class LogoutService extends MyDriveService {

	private long token;

	public LogoutService(long token) {
		this.token = token;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = MyDrive.getInstance();
		
		SessionManager sm = md.getSessionManager();
		
		sm.removeSessionByToken(token);
    }

}