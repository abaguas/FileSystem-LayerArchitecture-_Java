package pt.tecnico.mydrive.service;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.service.dto.FileDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ListDirectoryService extends MyDriveService{

	private long token;
	private List<FileDto> listedFiles;
	
	public ListDirectoryService(long token) throws NoSuchFileException{
    		this.token = token;
   	}

	public final void dispatch() {
		MyDrive md = MyDrive.getInstance();
		Session session = md.getSessionManager().getSession(token);
		Directory directory = session.getCurrentDir();
		listedFiles = new ArrayList<FileDto>();
		
		Directory father = directory.getFatherDirectory();
		String fatherName = father.getName();
		father.setName("..");
		
		String ownName = directory.getName();
		directory.setName(".");
		
		Set<File> files = directory.getFilesSet();

		listedFiles.add(new FileDto(directory.toString()));
		listedFiles.add(new FileDto(father.toString()));

	  	for (File f: files){
	  		if (f.getName()!="/") {
	  			listedFiles.add(new FileDto(f.toString()));
	  		}
	  	}
	  	
	  	Collections.sort(listedFiles);
	  	father.setName(fatherName);
	 	directory.setName(ownName);
	}

	public final List<FileDto> result(){
		return listedFiles;
	}

}

