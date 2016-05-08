package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.LinkWithoutContentException;
import pt.tecnico.mydrive.exception.MaximumPathException;

public class CreateFileService extends MyDriveService{
	
	private long token; 
	private String name;
	private String content;
	private String code;

	public CreateFileService(long token, String name, String content, String code) {
		this.token = token;
		this.name = name;
		this.content = content;
		this.code = code;
	}
	
	public CreateFileService(long token, String name, String code) {
		this(token,name,"",code);
	}
	

	public void fileFactory(String name, String content, int id, User user, Directory directory, String code){
	    
		if(code.equals("PlainFile")){
            new PlainFile(name, id, user, content, directory);
        }
        else if(code.equals("App")){
            new App(name, id, user, content, directory);
        }
        else if(code.equals("Dir")){
        	new Directory(name, id, user, directory);
        }
        else if(code.equals("Link")){
           	new Link(name, id, user, content, directory);
        }
    }
	
	@Override
	protected void dispatch() throws PermissionDeniedException, FileAlreadyExistsException, InvalidFileNameException, LinkWithoutContentException, MaximumPathException, InvalidTokenException {
		MyDrive md = getMyDrive();

		Session session = md.getSessionManager().getSession(token);
        User currentUser = session.getUser();
        Directory currentDirectory = session.getCurrentDir();

        
        fileFactory(name, content, md.generateId(), currentUser, currentDirectory, code);
       

    }
	
}


