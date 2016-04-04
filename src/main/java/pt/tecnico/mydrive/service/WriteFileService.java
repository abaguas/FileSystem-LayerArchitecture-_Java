package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;



public class WriteFileService extends MyDriveService
{
    private String fileName;
    private String content;
    private long token;

    public WriteFileService(String fileName, String content, long token)
    {        
        this.fileName=fileName;
        this.content=content;
        this.token=token;
    }


    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
       MyDrive md = getMyDrive();
       md.writeFile(fileName, content, token);     
    }
}
