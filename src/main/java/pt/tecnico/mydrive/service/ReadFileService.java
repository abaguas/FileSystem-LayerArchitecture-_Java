package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.FileIsNotReadAbleException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.TooManyLevelsOfSymbolicLinksException;


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
        int linkHops=0;
        if(fileName != null){
            File file = currentDir.get(fileName); 
            if(file instanceof Directory){
                throw new FileIsNotReadAbleException("Cant read a Directory");
            }
            else{
                while(file instanceof Link){
                    md.checkPermissions(token, fileName, "read-write-execute", "read");
                    md.checkPermissions(token, fileName, "read-write-execute", "execute");
                    String link = file.ls();
                    file = md.getFileByPath(link, file.getDirectory());
                    if(file instanceof Directory){
                        throw new FileIsNotReadAbleException(file.getName());
                    }
                    md.setCurrentDirByToken(token , file.getDirectory());
                    md.checkPermissions(token, file.getName(), "read-write-execute", "read"); 
                    md.setCurrentDirByToken(token , currentDir);
                    linkHops++;
                    if(linkHops > 20){
                        throw new TooManyLevelsOfSymbolicLinksException(fileName);
                    }         
                }
                md.checkPermissions(token, fileName, "read-write-execute", "read");            
                _result = file.ls();
                return;
            }
        }
        throw new InvalidFileNameException("Invalid file name null ");
    }

    public final String result() {
        return _result;
    }

    
}