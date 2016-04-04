package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;


public class CreateFileService extends MyDriveService
{
    private String fileName;
    private String content;
    private String code;
    private long token;

    public CreateFileService(String fileName, String code, long token) {
        this.fileName = fileName;
        this.content = "";
        this.code = code;
        this.token = token;
    }

    
    public CreateFileService(String fileName, String content, String code, long token) {
        this(fileName, code, token);
        this.content = content;
    }
       
    
    public final void dispatch() throws PermissionDeniedException, FileAlreadyExistsException, InvalidFileNameException {
       MyDrive md = getMyDrive();
       if(code.equals("Dir"))
    	   md.createDir(token, fileName); //md.createDir(fileName, content, code, token);
       else
    	   md.createFile(token, fileName, content, code); //md.createFile(fileName, content, code, token);
    }
}

/*
removi atributo User u. Na classe MyDrive, vai-se fazer:
Session s=getLogin().getSession(token); Directory currentDir=s.getCurrentDirectory() e User u=s.getUser();
o content no link tem de ser um path, mas acho que isso esta assegurado no dominio
*/
