package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;


public class ReadFileService extends MyDriveService{

    private long token;
	private String fileName;
    private String result;
    

    public ReadFileService(long token, String fileName) {
    	this.token = token;
    	this.fileName = fileName;
    }
    
    public final void dispatch() throws PermissionDeniedException, InvalidFileNameException, NoSuchFileException{
        MyDrive md = MyDrive.getInstance();
        Session session = md.getSessionManager().getSession(token);
        User currentUser = session.getUser();
        Directory currentDir = session.getCurrentDir();
        
        File file = currentDir.get(fileName);
        
        result = file.read(currentUser, md);
    }

    public final String result() {
        return result;
    }

    
}