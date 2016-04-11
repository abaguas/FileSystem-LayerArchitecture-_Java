package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.LinkWithoutContentException;

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
	protected void dispatch() throws PermissionDeniedException, FileAlreadyExistsException, InvalidFileNameException, LinkWithoutContentException  {
		MyDrive md = MyDrive.getInstance();
        User currentUser = md.getSessionByToken(token).getCurrentUser();
        Directory currentDir = md.getSessionByToken(token).getCurrentDir();
        
        boolean ownerPermission = currentDir.getOwner().getUsername().equals(currentUser.getUsername()) || currentUser.getUsername().equals("root");
        boolean createPermission = currentDir.getOthersPermission().getWrite() && currentUser.getOthersPermission().getWrite();
        
        if(!(ownerPermission || createPermission)){
        	throw new PermissionDeniedException("Creating " + name);
        }
        
        if(code.equals("Link") && content.equals("")){
        	throw new LinkWithoutContentException(name);
        }
        //FIXME guarantee to receive maximumpathexception from mydrive
//        int id = md.getId();
//        currentDir.createFile(name,content,id+1,currentUser,code); //FIXME verify ID
    }
	
}


