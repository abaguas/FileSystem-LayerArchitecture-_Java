package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;



public class WriteFileService extends MyDriveService
{
    private long token;
    private String fileName;
    private String content;

    public WriteFileService(long token, String fileName, String content)
    {
        this.token=token;
        this.fileName=fileName;
        this.content=content;
    }


    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
       MyDrive md = getMyDrive();
       md.writeFile(fileName, content, token);     
    }
}
