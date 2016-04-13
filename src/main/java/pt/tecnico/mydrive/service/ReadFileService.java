package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;


public class ReadFileService extends MyDriveService{

    private long token;
	private String fileName;
    private Directory currentDir;
    private User currentUser;
    private String _result;
    

    public ReadFileService(long token, String fileName) {
    	this.token = token;
    	this.fileName = fileName;
    }
    
    public final void dispatch() throws PermissionDeniedException, InvalidFileNameException, NoSuchFileException{
        MyDrive md = MyDrive.getInstance();
        currentUser = md.getCurrentUserByToken(token);
        currentDir = md.getCurrentDirByToken(token);
        if(fileName != null){
            File file = currentDir.get(fileName); // throws no such file exception
            md.checkPermissions(token, fileName, "ls", "read");            
            _result = file.ls();
            throw new PermissionDeniedException("Reading " + fileName + " ");
        }
        throw new InvalidFileNameException("Invalid file name null ");
    }

    public final String result() {
        return _result;
    }

    
}