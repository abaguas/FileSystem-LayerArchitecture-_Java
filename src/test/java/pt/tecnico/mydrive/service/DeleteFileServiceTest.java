package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.exception.NotDeleteAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.MyDrive;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeleteFileServiceTest extends AbstractServiceTest{
	
	private Directory home1;
	private Directory home2;
	private Directory rootdir;

	protected void populate() {

		MyDrive md = MyDrive.getInstance();

		rootdir = MyDrive.getInstance().getRootDirectory();

		home1 = (Directory)rootdir.get("home");

	    User u1 = new User("CatioBalde", "pass1", "Catio");
	    u1.setMainDirectory(home1);

	    User u2 = md.getUserByUsername("root");
		home2 = u2.getMainDirectory();
		Directory home2 = u2.getMainDirectory();
	    
	    Directory d1 = new Directory("folder", 125, u1, home1);
	    Directory d2 = new Directory("folder2", 126, u2, home2);        
	    PlainFile p1 = new PlainFile("CasoBruma", 123, u1, "conteudo1", home1);
		PlainFile p2 = new PlainFile("Exemplo", 124, u2, "conteudo3", home2);
	    
	    Session s1 = new Session(u1, 1, md);
	    s1.setCurrentDir(home1);

	    Session s2 = new Session(u1, 2, md);
	    s2.setCurrentDir(home2);

	    Session s3 = new Session(u1, 3, md);
	    s3.setCurrentDir(rootdir);

	    Session s4 = new Session(u2, 4, md);
	    s3.setCurrentDir(home2);

	    Session s5 = new Session(u1, 5, md);
	    s5.setCurrentDir((Directory)rootdir.get("home"));

	    Session s6 = new Session(u1, 6, md);
	    s6.setCurrentDir((Directory)rootdir.get("home"));
	    
	}

	@Test
	public void deletePermittedFile(){

		DeleteFileService dfs = new DeleteFileService(1, "CasoBruma");
		dfs.execute();

 		assertFalse("file was not removed", MyDrive.getInstance().getCurrentDirByToken(1).hasFile("CasoBruma"));	
 	}

	@Test(expected=PermissionDeniedException.class)
	public void deleteNotPermittedFile(){

		DeleteFileService dfs = new DeleteFileService(2, "Exemplo");
		dfs.execute();

	}

	@Test(expected=NoSuchFileException.class)
	public void deleteNonExistingFile(){

		DeleteFileService dfs = new DeleteFileService(1, "naoexisto");
		dfs.execute();

	}

	@Test(expected=InvalidFileNameException.class)
	public void deleteWithNullName(){

		DeleteFileService dfs = new DeleteFileService(1, null);
		dfs.execute();

	}

	@Test
	public void deletePermittedDirectory(){

		DeleteFileService dfs = new DeleteFileService(1, "folder");
		dfs.execute();

 		assertFalse("Directory was not removed", MyDrive.getInstance().getCurrentDirByToken(1).hasFile("folder"));	
 	}

	@Test(expected=PermissionDeniedException.class)
	public void deleteNotPermittedDirectory(){

		DeleteFileService dfs = new DeleteFileService(2, "folder2");
		dfs.execute();

	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteFileSystemRoot(){

		DeleteFileService dfs = new DeleteFileService(3, "/");
		dfs.execute();

	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteSpecialDotDirectory(){
		DeleteFileService dfs = new DeleteFileService(3, ".");
		dfs.execute();
	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteSpecialDoubleDotDirectory(){
		DeleteFileService dfs = new DeleteFileService(3, "..");
		dfs.execute();
	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteUserHomeDirectory(){
		DeleteFileService dfs = new DeleteFileService(5, "CatioBalde");
		dfs.execute();
	}

	@Test(expected=NotDeleteAbleException.class)
	public void deleteUserHomeDirectoryByRoot(){
		DeleteFileService dfs = new DeleteFileService(6, "CatioBalde");
		dfs.execute();
	}

}