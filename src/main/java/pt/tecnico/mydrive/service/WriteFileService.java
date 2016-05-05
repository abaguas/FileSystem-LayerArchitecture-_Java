package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;



public class WriteFileService extends MyDriveService
{
	
    private String fileName;
    private String content;
    private long token;
    private String result;

    public WriteFileService(String fileName, String content, long token)
    {        
        this.fileName=fileName;
        this.content=content;
        this.token=token;

    }
    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
       MyDrive md = getMyDrive();
       Session session = md.getSessionManager().getSession(token);
       User currentUser = session.getCurrentUser();
       Directory currentDirectory = session.getCurrentDir();
       File file = currentDirectory.get(fileName);
       file.writeContent(currentUser,currentDirectory, content);
       result = file.read(currentUser,md);
    }
    
    public String result(){
    	return result;
    }

}
