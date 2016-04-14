package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
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

     public String pwd(long token, MyDrive md){
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

    
     public void cd(long token, String name, MyDrive md) throws NoSuchFileException, FileNotCdAbleException, PermissionDeniedException {
     	File f = null;
         if(name.charAt(0)=='/') {
             f = md.getDirectoryByAbsolutePath(token, name); //getDirectoryByAbsolutePath chama o último caso desta função, que chama o checkPermissions
             md.cdable(f);
             md.setCurrentDirByToken(token, (Directory) f);
         }
         else if(name.contains("/")) {
         	String result = pwd(token, md);
         	result = result + "/" + name;
             f = md.getDirectoryByAbsolutePath(token, result); //getDirectoryByAbsolutePath chama o último caso desta função, que chama o checkPermissions
             md.cdable(f);
             md.setCurrentDirByToken(token, (Directory) f);
         }
         else {
             f = md.getCurrentDirByToken(token).get(name);
         	md.cdable(f);
         	md.checkPermissions(token, name, "cd", "");
        	md.setCurrentDirByToken(token, (Directory) f);
         }
     }     
     

    
    public final void dispatch() throws FileNotCdAbleException, FileNotDirectoryException, InvalidFileNameException, NoSuchFileException, PermissionDeniedException {
       MyDrive md = getMyDrive();     
       cd(token, path, md);
       
    }

}
