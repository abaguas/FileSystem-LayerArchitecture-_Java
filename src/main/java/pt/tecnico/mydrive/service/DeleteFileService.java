package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.NotDeleteAbleException;


public class DeleteFileService extends MyDriveService{

    private long token;
	private String fileName;
    private Directory currentDir;
    private User currentUser;
    
    public DeleteFileService(long token, String fileName) {
    	this.token = token;
    	this.fileName = fileName;
    }
    
    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException {
        boolean isDirectory = true;
        MyDrive md = MyDrive.getInstance();
        currentUser = md.getCurrentUserByToken(token);
        currentDir = md.getCurrentDirByToken(token);
        
        if (fileName != null){  
            if(fileName.equals(".") || fileName.equals("..")){
                throw new NotDeleteAbleException("Cant delete special directories . and ..");
            }
            else if(fileName.equals("/")){
                throw new NotDeleteAbleException("Cant delete File System Root");
            }
            File file = currentDir.get(fileName); 
            if(file.getOwner().getMainDirectory().getId() == file.getId()){
                throw new NotDeleteAbleException("Cant delete User's home despite permissions");
            }
            file.remove(md, token);
        }        
        else {
        	throw new NoSuchFileException("Invalid file name: null");
        }
    }
}