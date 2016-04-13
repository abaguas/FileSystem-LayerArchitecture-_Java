package pt.tecnico.mydrive.service;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.exception.NoSuchFileException;

import java.util.List;

public class ListDirectoryService extends MyDriveService{

	private long token;
	private String name;
	
	public ListDirectoryService(long token) throws NoSuchFileException{
    		//return getMyDrive.getCurrentDir(token).get(name).ls();

   	}

	public final void dispatch() {

	}

	public final List<File> result(){
		return null;
	}    
    	/*public String ListDirectoryService(long token){
		return getMyDrive.getCurrentDir(token).ls();
    	}
*/

}

