package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException; 

import static org.junit.Assert.*;

import org.junit.Test;



public class WriteFileTest extends AbstractServiceTest{
	private long token;
	
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		SessionManager sm = md.getSessionManager();
		
		md.getRootDirectory().get("home").setOthersPermission(new Permission("rwx-"));
	    User u1 = new User(md,"Catio", "pass1", "CatioBalde");
	    
	    Directory user_home = new Directory("Catio", md.generateId(),u1, (Directory)md.getRootDirectory().get("home"));
	    
	    u1.setMainDirectory(user_home);
	    Session s1 = new Session("Catio", "pass1", sm);

	    User u2 = md.getRootUser();
	    Directory d1 = new Directory("folder", md.generateId(), u1, user_home);    
	    
	    PlainFile p1 = new PlainFile("CasoBruma", md.generateId(), u1, "conteudo1", user_home);
	    PlainFile p2 = new PlainFile("Exemplo", md.generateId(), u2, "conteudo3", user_home);
	    App a1 = new 	App("application", md.generateId(), u1, "conteudo1", user_home);
	    Link l1 = new Link("ligacao", md.generateId(), u1, "CasoBruma", user_home);
	  
	    
	    token=s1.getToken();
		md.getRootDirectory().get("home").setOthersPermission(new Permission("r-x-"));
	}
	    
	
	@Test
	public void successPermittedFile() {
	    MyDrive md = MyDrive.getInstance();
	    WriteFileService wfs = new WriteFileService("CasoBruma", "abc", token); 
	    wfs.execute();
	    String result= wfs.result();
	    assertEquals("Wrong Content", "abc", result);

	}


	@Test(expected = PermissionDeniedException.class)
	public void notPermittedFile() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
	    WriteFileService wfs = new WriteFileService("Exemplo", "abc", token); // token = 1
	    wfs.execute();
	}

	@Test(expected = NoSuchFileException.class)
	public void noSuchFile() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
	    WriteFileService wfs = new WriteFileService("blabla", "abc", token); // token = 1
	    wfs.execute();
	}

	@Test(expected = FileIsNotWriteAbleException.class)
	public void writeOnDirectory()throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
	    WriteFileService wfs = new WriteFileService("folder", "abc", token); // token = 1
	    wfs.execute();
	}


	@Test
		public void writeOnApp() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException{
	    WriteFileService wfs = new WriteFileService("application", "abc", token); // token = 1
	    wfs.execute();
	    wfs.execute();
	    String result= wfs.result();
	    assertEquals("Wrong Content", "abc", result);
	}

	@Test
	public void writeOnLink() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
	    WriteFileService wfs = new WriteFileService("ligacao", "abc", token); // token = 1
	    wfs.execute();
	    String result= wfs.result();
	    assertEquals("Wrong Content", "abc", result);

	}

}	