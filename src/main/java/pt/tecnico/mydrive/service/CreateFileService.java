package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.LinkWithoutContentException;
import pt.tecnico.mydrive.exception.MaximumPathException;

public class CreateFileService extends MyDriveService{
	
	private long token; //FIXME valid token?
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
	
	@Override
	protected void dispatch() throws PermissionDeniedException, FileAlreadyExistsException, InvalidFileNameException, LinkWithoutContentException, MaximumPathException {
		//Invalid token exception
		MyDrive md = MyDrive.getInstance();
        User currentUser = md.getSessionByToken(token).getCurrentUser();
        Directory currentDir = md.getSessionByToken(token).getCurrentDir();
        
        md.checkPermissions(token, name, "create-delete", "create");
        
        if(code.equals("Link")){
        	if(content.equals("")){
        		throw new LinkWithoutContentException(name);
        	}
        	else if(content.length()>1024){
        		throw new MaximumPathException(name);
        	}
        }
        
        int id = md.generateId();
        currentDir.createFile(name,content,id,currentUser,code);
    }
	
}


