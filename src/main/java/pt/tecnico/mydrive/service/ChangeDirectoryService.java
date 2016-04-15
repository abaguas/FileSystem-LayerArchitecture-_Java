package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.MyDriveException;
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

    
    
     public String pwd(long token, MyDrive md){
        Directory current = md.getCurrentDirByToken(token);
        String output="";
        if(current.getName().equals("/")){
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

     
     public Directory getDirectoryByAbsolutePath(long token, String absolutepath, MyDrive md) {
		
    	String[] parts = absolutepath.split("/");
		md.setCurrentDirByToken(token, md.getRootDirectory());
		
		for(int i=1; i < parts.length; i++){		
			cd(token, parts[i], md);
		}	
		
		return md.getCurrentDirByToken(token);
     }
     
    
     public void cd(long token, String name, MyDrive md) throws NoSuchFileException, FileNotCdAbleException, PermissionDeniedException {
    	 File f = null;
    	 Directory d = null;
    	 if(name.equals(".")) {
    		 //nothing needed to be done
    	 }
    	 else if(name.charAt(0)=='/') {
             d = getDirectoryByAbsolutePath(token, name, md); //getDirectoryByAbsolutePath chama o último caso desta função, que chama o checkPermissions
             //md.cdable(f);
             md.setCurrentDirByToken(token, d);
         }
         else if(name.contains("/")) {
        	 String result = pwd(token, md);
        	 result = result + "/" + name;
             d = getDirectoryByAbsolutePath(token, result, md); //getDirectoryByAbsolutePath chama o último caso desta função, que chama o checkPermissions
             //md.cdable(f);
             md.setCurrentDirByToken(token, d);
         }
         else {
        	 f = md.getCurrentDirByToken(token).get(name);
        	 md.cdable(f);
        	 md.checkPermissions(token, name, "cd", "");
        	 md.setCurrentDirByToken(token, (Directory) f);
         }
     }     
     

    
    public final void dispatch() throws FileNotCdAbleException, NoSuchFileException, PermissionDeniedException, InvalidTokenException { //FileNotDirectoryException, InvalidFileNameException
       MyDrive md = getMyDrive();     
       cd(token, path, md);
       
    }
    
    public String getResult(){
    	return result;
    }


}
