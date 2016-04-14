package pt.tecnico.mydrive.service;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.exception.NoSuchFileException;

import java.util.List;

public class ListDirectoryService extends MyDriveService{

	private long token;
	private List<String> _result;
	
	public ListDirectoryService(long token) throws NoSuchFileException{
    		this.token = token;
   	}

	public final void dispatch() {
			MyDrive md = MyDrive.getInstance();
    		_result = md.getCurrentDirByToken(token).lsList();
	}
	
	public final List<String> result(){
		return _result;
	}    

}

