package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;



public class WriteFileService extends MyDriveService {
	
    private String fileName;
    private String content;
    private long token;
    private String result;

    public WriteFileService(String fileName, String content, long token) {        
        this.fileName=fileName;
        this.content=content;
        this.token=token;
    }
    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
       MyDrive md = getMyDrive();
       Session session = md.getSessionManager().getSession(token);
       User currentUser = session.getUser();
       Directory currentDirectory = session.getCurrentDir();
       String path = "";
       String[] parts = null;
       File file = null;
       Directory dir = null;
      if(fileName.contains("/")  && path.startsWith("/")) {
        parts = fileName.split("/");
        int i = 0;
        for(i= 1; i < parts.length - 2; i++){
          path = path + parts[i] + "/";
        }
        path = path + parts[i];
        fileName = parts[i+1];
        dir =  md.getRootDirectory().getDirectory(path); 
        file = dir.get(fileName);
      } 
      else if(fileName.contains("/")) {
        parts = fileName.split("/");
        int i = 1;
        for(i= 0; i < parts.length - 2; i++){
          path = path + parts[i] + "/";
        }
        path = path + parts[i];
        fileName = parts[i+1];
        dir =  currentDirectory.getDirectory(path); 
        file = dir.get(fileName);
      }
      else{
        path = fileName;
        file = currentDirectory.get(path);
      } 
        file.write(currentUser, content, md);
        result = file.read(currentUser, md);
    }
    
    public String result(){
    	return result;
    }

}
