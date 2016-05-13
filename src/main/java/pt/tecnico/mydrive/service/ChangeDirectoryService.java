package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.NoSuchFileException;


public class ChangeDirectoryService extends MyDriveService
{
	private long token;
	private String path;
	private String result;


    public ChangeDirectoryService(long token, String path) {
    	this.token = token;
    	this.path = path;
    }

    
    public final void dispatch() throws FileNotCdAbleException, NoSuchFileException, PermissionDeniedException, InvalidTokenException { 
       MyDrive md = getMyDrive();
       Session session = md.getSessionManager().getSession(token);
       User currentUser = session.getUser();
       Directory currentDirectory = session.getCurrentDir();
       
       if (path.equals(".")) {
    	   result = currentDirectory.pwd();
       } 
       else{
    	   Directory directory = currentDirectory.getDirectoryByPath(currentUser, path, currentDirectory, md);
    	   session.setCurrentDir(directory);
    	   result = directory.pwd();
       }
       
    }
    
    public String getResult(){
    	return result;
    }


}
