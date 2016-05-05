package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.exception.NotDeleteAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeleteFileServiceTest extends AbstractServiceTest{
	
	private Directory home1;
	private Directory home2;
	private Directory rootdir;
	private long token1;
	private long token2;
	private long token3;
	private long token4;
	private long token5;
	private long token6;
	private SessionManager sm;
	
	protected void populate() {

		MyDrive md = MyDrive.getInstance();
		sm = md.getSessionManager();
		
		rootdir = MyDrive.getInstance().getRootDirectory();

		home1 = (Directory)rootdir.get("home");
		home1.setOthersPermission(new Permission("-w--"));

	    User u1 = new User(md, "CatioBalde", "pass1", "Catio");
	    Directory home3 = new Directory("CatioBalde", 127, u1, home1);
	    u1.setMainDirectory(home3);

	    User u2 = md.getUserByUsername("root");
		home2 = u2.getMainDirectory();
		Directory home2 = u2.getMainDirectory();
	    
	    Directory d1 = new Directory("folder", 125, u1, home3);
	    Directory d2 = new Directory("folder2", 126, u2, home2);        
	    PlainFile p1 = new PlainFile("CasoBruma", 123, u1, "conteudo1", home3);
		PlainFile p2 = new PlainFile("Exemplo", 124, u2, "conteudo3", home2);
	    
		Session s1 = new Session("CatioBalde", "pass1", sm);
	    s1.setCurrentDir(home3);
	    token1=s1.getToken();
	    
	    Session s2 = new Session("CatioBalde", "pass1", sm);
	    s2.setCurrentDir(home2);
	    token2=s2.getToken();

	    Session s3 = new Session("CatioBalde", "pass1", sm);
	    s3.setCurrentDir(rootdir);
	    token3=s3.getToken();
	    
	    Session s4 = new Session("root", "***", sm);
	    s4.setCurrentDir(home2);
	    token4=s4.getToken();
	    
	    Session s5 = new Session("CatioBalde", "pass1", sm);
	    s5.setCurrentDir((Directory)rootdir.get("home"));
	    token5=s5.getToken();
	    
	    Session s6 = new Session("CatioBalde", "pass1", sm);
	    s6.setCurrentDir((Directory)rootdir.get("home"));
	    token6=s6.getToken();
	    home1.setOthersPermission(new Permission("----"));

	}

	@Test
	public void deletePermittedFile(){

		DeleteFileService dfs = new DeleteFileService(token1, "CasoBruma");
		dfs.execute();

 		assertFalse("file was not removed", sm.getSession(token1).getCurrentDir().hasFile("CasoBruma"));	
 	}

	@Test(expected=PermissionDeniedException.class)
	public void deleteNotPermittedFile(){

		DeleteFileService dfs = new DeleteFileService(token2, "Exemplo");
		dfs.execute();

	}

	@Test(expected=NoSuchFileException.class)
	public void deleteNonExistingFile(){

		DeleteFileService dfs = new DeleteFileService(token1, "naoexisto");
		dfs.execute();

	}

	@Test(expected=NoSuchFileException.class)
	public void deleteWithNullName(){

		DeleteFileService dfs = new DeleteFileService(token1, null);
		dfs.execute();

	}

	@Test
	public void deletePermittedDirectory(){

		DeleteFileService dfs = new DeleteFileService(token1, "folder");
		dfs.execute();

 		assertFalse("Directory was not removed", sm.getSession(token1).getCurrentDir().hasFile("folder"));	
 	}
	

	@Test(expected=PermissionDeniedException.class)
	public void deleteNotPermittedDirectory(){

		DeleteFileService dfs = new DeleteFileService(token2, "folder2");
		dfs.execute();

	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteFileSystemRoot(){

		DeleteFileService dfs = new DeleteFileService(token3, "/");
		dfs.execute();

	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteSpecialDotDirectory(){
		DeleteFileService dfs = new DeleteFileService(token3, ".");
		dfs.execute();
	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteSpecialDoubleDotDirectory(){
		DeleteFileService dfs = new DeleteFileService(token3, "..");
		dfs.execute();
	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteUserHomeDirectory(){
		DeleteFileService dfs = new DeleteFileService(token5, "CatioBalde");
		dfs.execute();
	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteUserHomeDirectoryByRoot(){
		DeleteFileService dfs = new DeleteFileService(token6, "CatioBalde");
		dfs.execute();
	}

}