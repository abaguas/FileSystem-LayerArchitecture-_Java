package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.MyDriveException;
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
       User currentUser = session.getCurrentUser();
       Directory currentDirectory = session.getCurrentDir();
       
       File f = null;
       Directory d = null;
       if (path.equals(".")) {
       
       } 
       else if (path.charAt(0) == '/') {
    	   d = getDirectoryByAbsolutePath(token, path, md);
    	   md.setCurrentDirByToken(token, d);
       } 
       else if (path.contains("/")) {
    	   String result = pwd();
    	   result = result + "/" + name;
    	   d = getDirectoryByAbsolutePath(token, result, md);
    	   md.setCurrentDirByToken(token, d);
       }
       else {
    	   f = md.getCurrentDirByToken(token).get(path);
    	   md.cdable(f);
    	   md.checkPermissions(token, path, "cd", "");
    	   md.setCurrentDirByToken(token, (Directory) f);
       }
       
    }
    
    public String getResult(){
    	return result;
    }


}
