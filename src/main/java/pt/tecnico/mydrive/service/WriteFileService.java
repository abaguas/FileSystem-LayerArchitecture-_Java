package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;



public class WriteFileService extends MyDriveService
{
	
    private String fileName;
    private String content;
    private long token;
   // private String result;

    public WriteFileService(String fileName, String content, long token)
    {        
        this.fileName=fileName;
        this.content=content;
        this.token=token;

    }

    public String writeFile(String filename, String content, long token, MyDrive md) throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
    	Directory current = md.getCurrentDirByToken(token);
        File file = current.get(filename);
        md.writeable(file);
        PlainFile f = (PlainFile)file;
        md.checkPermissions(token, filename, "read-write-execute", "write");
        f.writeContent(content);
        return f.getContent();
    }

    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
       MyDrive md = getMyDrive();
       Session session = Session.getSession(token,md);
       User currentUser = session.getCurrentUser();
       Directory currentDirectory = session.getCurrentDir();
       
      
       
       
       File file = currentDirectory.get(fileName);
       file.writeContent(content);
       
//       md.writeable(file);
//       
//       PlainFile f = (PlainFile)file;
//       
//       md.checkPermissions(token, filename, "read-write-execute", "write");
//       f.writeContent(content);
//       return f.getContent();
       
       
//       result=writeFile(fileName, content, token, md);
       

    }

//    public final String result(){
//      return result;
//    }
//		FIXME
    
}
