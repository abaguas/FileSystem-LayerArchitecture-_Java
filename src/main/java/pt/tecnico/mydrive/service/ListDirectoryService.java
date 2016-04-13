package pt.tecnico.mydrive.service;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.exception.NoSuchFileException;

import java.util.List;

public class ListDirectoryService extends MyDriveService{

	private long token;
	//private String result;
	private List<File> _result;
	
	public ListDirectoryService(long token) throws NoSuchFileException{
    		this.token = token;
   	}

	public final void dispatch() {
			MyDrive md = MyDrive.getInstance();
    		_result = null ;//md.getCurrentDirByToken(token).ls();
	}

	/*public final String result(){
		return result;
	} */

	public final List<File> result(){
		return _result;
	}    

}

