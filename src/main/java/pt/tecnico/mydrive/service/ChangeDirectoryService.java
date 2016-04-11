package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.NoSuchFileException;


public class ChangeDirectoryService extends MyDriveService
{
    private long token;
	private String path;


    public ChangeDirectoryService(long token, String path) {
    	this.token = token;
    	this.path = path;
    }

    
       
    
    public final void dispatch() throws FileNotCdAbleException, FileNotDirectoryException, InvalidFileNameException, NoSuchFileException, PermissionDeniedException {
       MyDrive md = getMyDrive();
       md.cd(token, path);
    }
}
