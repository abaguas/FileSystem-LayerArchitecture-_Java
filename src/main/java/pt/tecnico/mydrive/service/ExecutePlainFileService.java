package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileIsNotExecuteAbleException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;



public class ExecutePlainFileService extends MyDriveService {
	private long token;
	private String path;
	private String[] argList;
	
	
	public ExecutePlainFileService(long token, String path, String[] argList) {
		this.token = token;
		this.path = path;
		this.argList = argList;
	}

	

	@Override
	protected void dispatch() throws PermissionDeniedException, InvalidTokenException, FileIsNotExecuteAbleException, NoSuchFileException {
		MyDrive md = getMyDrive();
		Session session = md.getSessionManager().getSession(token);
        User currentUser = session.getUser();
		Directory currentDirectory = session.getCurrentDir();				
		File f = currentDirectory.getFileByPath(currentUser, path, currentDirectory, md);
		f.execute(currentUser, argList, md);
	}

}