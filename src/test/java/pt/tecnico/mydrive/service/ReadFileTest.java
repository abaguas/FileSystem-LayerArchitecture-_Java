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

/*casos de teste:
 * ler ficheiro com own permissions
 * ler ficheiro sem own permissions
 * 
 * ler ficheiro com other permissions
 * ler ficheiro sem other permissions
 *
 * root: ler ficheiro sem other permissions
 * root: ler ficheiro sem own permissions
 * 
 * read non-existing file
 * 
 * diretorio sem permissoes
 * diretorio com permissoes
 * 
 * link absoluto
 * link relativo
 * link broken
 * 
 * **********************************
 * DUVIDAS:
 * vale a pena testar todos os casos na leitura de uma App?
 	@Test(expected = FileIsNotReadAbleException.class)
	  public void readAppWithPermissions() {
	}
 * 
 * TODO testar os links depois de receber o mail
 	
 */

public class ReadFileTest extends AbstractServiceTest{
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		
		//create users
		User owner = new User("Pizza", "password", "pizz");
		User other = new User("Popcorn", "password", "corn");
		User root = md.getRootUser();
		
		//create directory with permissions for all to insert the files
		Directory workingDirectory = owner.getMainDirectory();
        workingDirectory.setOthersPermission(new Permission("rwxd"));
		
        //create files
		new PlainFile("Granted to owner", 1, owner, "Owner can see", workingDirectory);
		PlainFile denied = new PlainFile("Denied", 2, owner, "Cannot see", workingDirectory);
		PlainFile visit = new PlainFile("To Visit", 3, owner, "Welcome Visitor", workingDirectory);
		Directory dir = new Directory("Dir", 4, owner, workingDirectory);
		new Directory("Dir Without Permissions", 5, owner, workingDirectory);
		new Link("Relative", 6, owner, "../Granted to owner", dir);
		new Link("Absolute", 7, owner, "/home/pizz/Granted to owner", dir);
		new Link("Broken", 8, owner, "/home/piz/Granted to owner", dir);
		
		//change permissions
		denied.setUserPermission(new Permission("-wxd"));
		visit.setOthersPermission(new Permission("r---"));
		
		
		//create session and set current directory
		Session sessionOwner = new Session(owner, 1);
		sessionOwner.setCurrentDir(workingDirectory);
		md.addSession(sessionOwner);
		
		Session sessionOther = new Session(other, 2);
		sessionOther.setCurrentDir(workingDirectory);
		md.addSession(sessionOther);
		
		Session sessionRoot = new Session(root, 3);
		sessionRoot.setCurrentDir(workingDirectory);
		md.addSession(sessionRoot);
		
	}


	@Test
	public void readFileWithOwnPermissions() {
		ReadFileService rfs = new ReadFileService(1, "Granted to Owner");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is 'Owner can see'", "Owner can see", content);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readFileWithoutOwnPermissions() {
		ReadFileService rfs = new ReadFileService(1, "Denied");
		rfs.execute();
	}
	
	@Test
	public void readFileWithOthersPermissions() {
		ReadFileService rfs = new ReadFileService(2, "To Visit");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is 'Welcome Visitor'", "Welcome Visitor", content);
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
		
		assertEquals("Content is 'Owner can see'", "Owner can see", content);
	}
	
	@Test
	public void readFileWithoutUsersPermissionsByRoot() {
		ReadFileService rfs = new ReadFileService(3, "Denied");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is 'Cannot see'", "Cannot see", content);
	}
	
	@Test(expected = NoSuchFileException.class)
	public void readNonExistingFile() {
		ReadFileService rfs = new ReadFileService(1, "Broccoli");
		rfs.execute();
	}

	@Test(expected = FileIsNotReadAbleException.class)
	public void readDirectoryWithPermissions() {
		ReadFileService rfs = new ReadFileService(1, "Dir");
		rfs.execute();
	}
	
	@Test(expected = FileIsNotReadAbleException.class)
	public void readDirectoryWithoutPermissions() {
		ReadFileService rfs = new ReadFileService(1, "Dir Without Permissions");
		rfs.execute();
	}
	
	@Test
	public void readLinkWithRelativePath() {
		ReadFileService rfs = new ReadFileService(1, "Relative");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is 'Owner can see'", "Owner can see", content);
	}
	
	@Test
	public void readLinkWithAbsolutePath() {
		ReadFileService rfs = new ReadFileService(1, "Absolute");
		rfs.execute();
		String content = rfs.result();
		
		assertEquals("Content is 'Owner can see'", "Owner can see", content);
	}
	
	@Test(expected = NoSuchFileException.class)
	public void readLinkWithInvalidPath() {
		ReadFileService rfs = new ReadFileService(1, "Broken");
		rfs.execute();
	}
}
