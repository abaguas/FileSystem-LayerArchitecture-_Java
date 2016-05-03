package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.NotDeleteAbleException;


public class DeleteFileService extends MyDriveService{

    private long token;
	private String fileName;
	
    
    public DeleteFileService(long token, String fileName) {
    	this.token = token;
    	this.fileName = fileName;
    }
    
    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, NotDeleteAbleException {

        MyDrive md = getMyDrive();
        Session session = Session.getSession(token,md);
        User currentUser = session.getCurrentUser();
        Directory currentDirectory = session.getCurrentDir();
        currentDirectory.removeFile(currentUser, currentDirectory, fileName);
      
    }
}