package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
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
       /*User currentUser = md.getCurrentUserByToken(token);
       Directory currentDir = md.getCurrentDirByToken(token);
       
       
       File file = currentDir.get(fileName); // throws no such file exception            
       
       boolean ownerPermission = file.getOwner().getUsername().equals(currentUser.getUsername()) || currentUser.getUsername().equals("root");
       boolean writePermission = file.getOthersPermission().getWrite() && currentUser.getOthersPermission().getWrite();
       
       if(!(ownerPermission || writePermission)){
       		throw new PermissionDeniedException("Writing on " + fileName);
       }
       */
       result=writeFile(fileName, content, token, md);
       

    }

    public final String result(){
      return result;
    }
    
}
