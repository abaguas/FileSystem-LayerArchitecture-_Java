
/*package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;


public class ChangeDirectoryService extends MyDriveService
{
    private String path;
    private long token;
    boolean absolute = false;

    public ChangeDirectoryService(String path, boolean absolute, long token) {
        this.path = path;
        this.token = token;
        this.absolute = absolute;
    }

    
       
    
    public final void dispatch() throws PermissionDeniedException, FileAlreadyExistsException, InvalidFileNameException {
       MyDrive md = getMyDrive();
       
       if(absolute)
    	   md.cd(path, true); //md.cd(path, true, token)
       else
    	   md.cd(path); //md.cd(path, token)
    }
}
*/

