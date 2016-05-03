package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.NotDeleteAbleException;


public class DeleteFileService extends MyDriveService{

    private long token;
	private String fileName;
//    private Directory currentDir;
//    private User currentUser;
    
    public DeleteFileService(long token, String fileName) {
    	this.token = token;
    	this.fileName = fileName;
    }
    
    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, NotDeleteAbleException {
        //boolean isDirectory = true;
        MyDrive md = getMyDrive();
        Session session = Session.getSession(token,md);
        User currentUser = session.getCurrentUser();
        Directory currentDirectory = session.getCurrentDir();
        File file = currentDirectory.getDelete(fileName);      
    
        file.remove(currentUser, currentDirectory);
      
    }
}