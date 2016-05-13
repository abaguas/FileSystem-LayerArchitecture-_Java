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
import pt.tecnico.mydrive.exception.InvalidTokenException;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;



public class WriteFileTest extends AbstractServiceTest{
	private long token;
	
	protected void populate() {
		MyDrive md = MyDriveService.getMyDrive();
		SessionManager sm = md.getSessionManager();
		
		md.getRootDirectory().get("home").setOthersPermission(new Permission("rwx-"));
	    User u1 = new User(md,"Catio", "grandepass1", "CatioBalde");
	    	    
	    User u2 = md.getRootUser();
	    Directory d1 = new Directory("folder", md.generateId(), u1, u1.getMainDirectory());
	    PlainFile p3 = new PlainFile("rel", md.generateId(), u1, "content", d1);
	    
	    PlainFile p1 = new PlainFile("CasoBruma", md.generateId(), u1, "conteudo1", u1.getMainDirectory());
	    PlainFile p2 = new PlainFile("Exemplo", md.generateId(), u2, "conteudo3", u1.getMainDirectory());
	    App a1 = new 	App("application", md.generateId(), u1, "conte.udo1", u1.getMainDirectory());
	    Link l1 = new Link("ligacao", md.generateId(), u1, "CasoBruma", u1.getMainDirectory());
	    Link l2 = new Link("relative", md.generateId(), u1, "folder/rel", u1.getMainDirectory());

	    Session s1 = new Session("Catio", "grandepass1", sm);
		s1.setCurrentDir(u1.getMainDirectory());

	    
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
	    WriteFileService wfs = new WriteFileService("application", "a.b.c", token); // token = 1
	    wfs.execute();
	    wfs.execute();
	    String result= wfs.result();
	    assertEquals("Wrong Content", "a.b.c", result);
	}

	@Test
	public void writeOnLinkRelativePath() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
	    WriteFileService wfs = new WriteFileService("relative", "cont", token); // token = 1
	    wfs.execute();
	    String result= wfs.result();
	    assertEquals("Wrong Content", "cont", result);

	}
	@Test
	public void writeOnLink() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
	    WriteFileService wfs = new WriteFileService("ligacao", "abc", token); // token = 1
	    wfs.execute();
	    String result= wfs.result();
	    assertEquals("Wrong Content", "abc", result);

	}
	@Test (expected = InvalidTokenException.class)
    public void invalidToken() {
		long token1 = new BigInteger(64, new Random()).longValue();
		while (token1 == token){
			token1 = new BigInteger(64, new Random()).longValue();
		}
		WriteFileService wfs = new WriteFileService("ligacao", "abc", token1); // token = 1
	    wfs.execute();
	    String result= wfs.result();
	    assertEquals("Wrong Content", "abc", result);
    }

}	
