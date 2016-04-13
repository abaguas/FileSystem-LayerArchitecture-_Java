package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.NoSuchFileException;


public class ChangeDirectoryService extends MyDriveService
{
  private long token;
	private String path;
  private String result;


    public ChangeDirectoryService(long token, String path) {
    	this.token = token;
    	this.path = path;
    }

     public String pwd(long token,MyDrive md){
        Directory current = md.getCurrentDirByToken(token);
        String output="";
        if(md.getCurrentDirByToken(token).getName().equals("/")){
            output="/";
        }
        else{
            while(!current.getName().equals("/")){
                output = "/" + current.getName() + output;
                current= current.getFatherDirectory();
            }
        }
        return output;
    }

    
       
    
    public final void dispatch() throws FileNotCdAbleException, FileNotDirectoryException, InvalidFileNameException, NoSuchFileException, PermissionDeniedException {
       MyDrive md = getMyDrive();
       md.cd(token, path);
       result= pwd(token,md);

    }
    public final String result(){
      return result();
    }
}
