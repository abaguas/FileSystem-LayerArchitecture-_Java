package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileIsNotReadAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.TooManyLevelsOfSymbolicLinksException;

public class ReadFileTest extends AbstractServiceTest{
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		
		Directory rootdir = MyDrive.getInstance().getRootDirectory();

		Directory home = (Directory)rootdir.get("home");

		//create users
		User owner = new User("Pizza", "password", "pizz");
		md.addUsers(owner);
		User other = new User("Popcorn", "password", "corn");
		md.addUsers(other);
		User root = md.getRootUser();
		
		//create directory with permissions for all to insert the files
		Directory workingDirectory = new Directory("pizz", 954, owner, home); 
        workingDirectory.setOthersPermission(new Permission("--x-"));
        
        //create files
        //files for directories with execute permissions
		new PlainFile("Granted to owner", 1, owner, "Owner can see", home);
		new PlainFile("Granted to owner", 1, owner, "Owner can see", workingDirectory);
		PlainFile denied = new PlainFile("Denied", 2, owner, "Cannot see", workingDirectory);
		PlainFile visit = new PlainFile("To Visit", 3, owner, "Welcome Visitor", workingDirectory);
		Directory dir = new Directory("Dir", 4, owner, workingDirectory);
		new Link("Hop One", 5, owner, "../pizz/Hop Two", workingDirectory);
		new Link("Hop Two", 6, owner, "/home/pizz/Cannot Execute/Inside Cannot Execute", workingDirectory);
		new Link("Hop Two", 6, owner, "/home/pizz/Cannot Execute/Inside Cannot Execute", dir);

		new Link("Relative", 7, owner, "../Granted to owner", workingDirectory);
		new Link("AbsoluteWithPermissions", 8, owner, "/home/pizz/To Visit", workingDirectory);
		new Link("Broken", 9, owner, "/home/piz/Granted to owner", workingDirectory);
		Directory cannotExecute = new Directory("Cannot Execute", 10, owner, workingDirectory);
		PlainFile insideCannotExecute = new PlainFile("Inside Cannot Execute", 11, owner, "poor me", cannotExecute);
		Directory canExecute = new Directory("Can Execute", 12, owner, workingDirectory);
		PlainFile noYouCant = new PlainFile("No You Cant", 13, owner, "poor you", workingDirectory);
		new Link("Hop One1", 14, owner, "Hop Two", dir);
		new Link("Hop One1", 20, owner, "Dir/Hop Two", workingDirectory);
		new Link("Hop Two2", 15, owner, "/home/pizz/Can Execute/No You Cant", canExecute);
		new Link("Infinite Loop", 16, owner, "/home/pizz/loop", workingDirectory);
		new Link("loop", 17, owner, "/home/pizz/Infinite Loop", workingDirectory);
		new Link("I point to Dir", 18, owner, "/home/pizz/Can Execute", workingDirectory);
		
		
		//change permissions
		denied.setUserPermission(new Permission("-wxd"));
		visit.setOthersPermission(new Permission("r---"));
		other.setOthersPermission(new Permission("----"));//remove permissions because they dont matter
		other.setOthersPermission(new Permission("----"));//remove permissions because they dont matter
		cannotExecute.setOthersPermission(new Permission("rw-d"));
		cannotExecute.setUserPermission(new Permission("rw-d"));
		insideCannotExecute.setOthersPermission(new Permission("----"));
		insideCannotExecute.setUserPermission(new Permission("----"));
		canExecute.setOthersPermission(new Permission("--x-"));
		noYouCant.setOthersPermission(new Permission("-wxd"));
		noYouCant.setUserPermission(new Permission("-wxd"));
		
		//create session and set current directory
		Session sessionOwner = new Session(owner, 1, md);
		sessionOwner.setCurrentDir(workingDirectory);
		
		Session sessionOther = new Session(other, 2, md);
		sessionOther.setCurrentDir(workingDirectory);
		
		Session sessionRoot = new Session(root, 3, md);
		sessionRoot.setCurrentDir(workingDirectory);	}

	@Test
	public void readFileWithOwnPermissions() {
		ReadFileService rfs = new ReadFileService(1, "Granted to owner");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Owner can see'", "Owner can see", content);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readFileWithoutOwnPermissions() {
		ReadFileService rfs = new ReadFileService(2, "Denied");
		rfs.execute();
	}
	
	@Test
	public void readFileWithOthersPermissions() {
		ReadFileService rfs = new ReadFileService(3, "To Visit");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Welcome Visitor'", "Welcome Visitor", content);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readFileWithoutOthersPermissions() {
		ReadFileService rfs = new ReadFileService(2, "Denied");
		rfs.execute();
	}
	
	@Test
	public void readFileWithoutOthersPermissionsByRoot() {
		ReadFileService rfs = new ReadFileService(3, "Granted to owner");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Owner can see'", "Owner can see", content);
	}
	
	@Test
	public void readFileWithoutUsersPermissionsByRoot() {
		ReadFileService rfs = new ReadFileService(3, "Denied");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Cannot see'", "Cannot see", content);
	}
	
	@Test(expected = NoSuchFileException.class)
	public void readNonExistingFile() {
		ReadFileService rfs = new ReadFileService(3, "Broccoli");
		rfs.execute();
	}

	@Test(expected = FileIsNotReadAbleException.class)
	public void readDirectoryWithPermissions() {
		ReadFileService rfs = new ReadFileService(1, "Dir");
		rfs.execute();
	}
	
	@Test(expected = FileIsNotReadAbleException.class)
	public void readDirectoryWithoutPermissions() {
		ReadFileService rfs = new ReadFileService(1, "Cannot Execute");
		rfs.execute();
	}
	
	@Test(expected = NoSuchFileException.class)
	public void readLinkWithInvalidPath() {
		ReadFileService rfs = new ReadFileService(1, "Broken");
		rfs.execute();
	}
	
	@Test
	public void readLinkWithRelativePathWithAllPermissionsByOwner() {
		ReadFileService rfs = new ReadFileService(1, "Relative");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Owner can see'", "Owner can see", content);
	}
	
	@Test
	public void readLinkWithAbsolutePathWithAllPermissionsByOwner() {
		ReadFileService rfs = new ReadFileService(1, "AbsoluteWithPermissions");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Welcome Visitor'", "Welcome Visitor", content);
	}
	
	@Test
	public void readLinkWithAbsolutePathWithAllPermissionsByOther() { ///////////////
		ReadFileService rfs = new ReadFileService(3, "AbsoluteWithPermissions");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Welcome Visitor'", "Welcome Visitor", content);
	}
	
	@Test
	public void readLinkWithAbsolutePathWithAllPermissionsByRoot() {
		ReadFileService rfs = new ReadFileService(3, "AbsoluteWithPermissions");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'Welcome Visitor'", "Welcome Visitor", content);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readLinkWithoutExecutePermissionsByOwner() {
		ReadFileService rfs = new ReadFileService(1, "Hop One");
		rfs.execute();
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readLinkWithoutExecutePermissionsByOther() {
		ReadFileService rfs = new ReadFileService(2, "Hop One");
		rfs.execute();
	}
	
	@Test
	public void readLinkWithoutExecutePermissionsByRoot() {
		ReadFileService rfs = new ReadFileService(3, "Hop One");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'poor me'", "poor me", content);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readLinkWithoutReadPermissionsByOwner() {
		ReadFileService rfs = new ReadFileService(1, "Hop One1");
		rfs.execute();
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readLinkWithoutReadPermissionsByOther() {
		ReadFileService rfs = new ReadFileService(2, "Hop One1");
		rfs.execute();
	}
	
	@Test
	public void readLinkWithoutreadPermissionsByRoot() {
		ReadFileService rfs = new ReadFileService(3, "Hop One1");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is not 'poor me'", "poor me", content);
	}
	
	@Test(expected = TooManyLevelsOfSymbolicLinksException.class)
	public void infiniteLoopByRoot() {
		ReadFileService rfs = new ReadFileService(3, "Infinite Loop");
		rfs.execute();
	}
	
	@Test(expected = FileIsNotReadAbleException.class)
	public void readDirectoryWithPermissionsByLink() {
		ReadFileService rfs = new ReadFileService(1, "I point to Dir");
		rfs.execute();
	}
	
}

