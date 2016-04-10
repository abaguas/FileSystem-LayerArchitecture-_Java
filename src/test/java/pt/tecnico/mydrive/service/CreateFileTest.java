/*

package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;



public class CreateFileTest extends AbstractServiceTest {

	protected void populate() {
		
		MyDrive md = MyDrive.getInstance();
		
		
		User u0 = md.getRootUser(); //User u0 = md.getUserByUsername("root");
		User u1 = new User("ana", "pass1", "Ana");
		User u2 = new User("maria", "pass2", "Maria");
		User u3 = new User("filipa", "pass3", "Filipa");
		
		//este getMainDirectory faz mesmo o que quero?
		Directory dir0 = u0.getMainDirectory();
		Directory dir1 = u1.getMainDirectory();
		Directory dir2 = u2.getMainDirectory();
		Directory dir3 = u3.getMainDirectory();
		
		Session s0 = md.getRootUser().getSession();
		s0.setCurrentDir(dir0);
	    
		Session s1 = new Session(u1, 1); // ana - token=1
	    s1.setCurrentDir(dir1);
	    
	    Session s2 = new Session(u2, 2); // maria - token=2
	    s2.setCurrentDir(dir2);
	    
	    Session s3 = new Session(u3, 3); // filipa - token=3
	    s3.setCurrentDir(dir3);

	    
	    md.createPlainFile(1, "agenda-Ana", "cozinhar para o Rui");
	    //PlainFile pf1 = new PlainFile("agenda-Ana", 31, u1, "cozinhar para o Rui", dir1); // id=31
		
	    
	    long roottoken = s0.getToken();
		md.setCurrentDir(roottoken, md.getRootDirectory());
        md.cd(roottoken, "home");
        md.cd(roottoken, "maria");
		md.createDir(roottoken, "forbiddenFolder");
        Directory forbidden = (Directory) s0.getCurrentDir().get("forbiddenFolder");
		Permission rootP = new Permission(true, true, true, true);
		Permission othersP = new Permission(false, false, false, false);
		forbidden.setUserPermission(rootP);
		forbidden.setOthersPermission(othersP);
		md.cd(roottoken, "forbiddenFolder");
		s2.setCurrentDir(forbidden); // s2.setCurrentDir(s0.getCurrentDir())   or    md.cd(maria-token, "forbiddenFolder")
		s0.setCurrentDir(dir0);
	
		
		String hugeDirName = "";
		for (int i = 0; i<1011; i++) { // "/home/filipa" length=12, mais descontar /, logo 1024-13=1011
			hugeDirName += "a"; // "a" ou 'a' ??
		}
		
		md.createDir(3, hugeDirName);
		Directory hugeDir = (Directory) s3.getCurrentDir().get("hugeDirName");
		md.setCurrentDir(3, hugeDir);
		
	}


	// SHORTCUTS
	
	private User getUser(long token) {
		User u = MyDriveService.getMyDrive().getCurrentUser(token);
		return u;
	}
	
	private Directory getDirectory(long token) {
		Directory d = MyDriveService.getMyDrive().getCurrentDir(token);
		return d;
	}
	

		
	// TESTS
	
    @Test
    public void successPlainFile() {
        CreateFileService service = new CreateFileService(1, "calendar", "day 1 - nothing to do", "PlainFile");
        service.execute();
       
        User owner = getUser(1);
        Directory currentDirectory = getDirectory(1);
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);

        PlainFile pf = (PlainFile) currentDirectory.get("calendar");
        //check plain file was created correctly
        assertNotNull("plain file was not created", pf);
        assertEquals("plain file name not correct", "calendar", pf.getName());
        assertEquals("plain file content not correct", "day 1 - nothing to do", pf.getContent());
        assertEquals("plain file owner not correct", owner, pf.getOwner());
        assertEquals("plain file directory not correct", currentDirectory, pf.getDirectory());
        assertEquals("plain file user permission not correct", userPermission, pf.getUserPermission());
        assertEquals("plain file others permission not correct", othersPermission, pf.getOthersPermission());
        //assertEquals("plain file id not correct", 2, pf.getId());
    }


    @Test
    public void successLink() {
    	CreateFileService service = new CreateFileService(1, "agenda-Ana_link", "/home/ana/agenda-Ana", "Link"); // home/ana ou /home/ana
    	service.execute();
    	
    	User owner = getUser(1);
    	Directory currentDirectory = getDirectory(1);
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);
    	
    	Link l = (Link) currentDirectory.get("agenda-Ana_link");
        //check link was created correctly
        assertNotNull("link was not created", l);
        assertEquals("link name not correct", "agenda-Ana_link", l.getName());
        assertEquals("link content not correct", "/home/ana/agenda-Ana", l.getContent());
        assertEquals("link owner not correct", owner, l.getOwner());
        assertEquals("link directory not correct", currentDirectory, l.getDirectory());
        assertEquals("link user permission not correct", userPermission, l.getUserPermission());
        assertEquals("link others permission not correct", othersPermission, l.getOthersPermission());
        //assertEquals("link id not correct", 2, l.getId());
    }
    
    
    @Test
    public void successApp() {
    	CreateFileService service = new CreateFileService(1, "MyDrive Application", "pt.tecnico.mydrive.MyDriveApplication.main", "App"); // content = "pt.tecnico.mydrive.domain.MyDrive.pwd 1", onde 1 é o argumento (token, neste caso)
    	service.execute();
    	
    	User owner = getUser(1);
    	Directory currentDirectory = getDirectory(1);
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);
    	
    	App a = (App) currentDirectory.get("MyDrive Application");
        //check app was created correctly
        assertNotNull("app was not created", a);
        assertEquals("app name not correct", "MyDrive Application", a.getName());
        assertEquals("app content not correct", "pt.tecnico.mydrive.MyDriveApplication.main", a.getContent());
        assertEquals("app owner not correct", owner, a.getOwner());
        assertEquals("app directory not correct", currentDirectory, a.getDirectory());
        assertEquals("app user permission not correct", userPermission, a.getUserPermission());
        assertEquals("app others permission not correct", othersPermission, a.getOthersPermission());
        //assertEquals("app id not correct", 2, a.getId());
    }
    
    
    @Test
    public void successDirectory() {
    	CreateFileService service = new CreateFileService(1, "picsFolder", "Dir");
    	service.execute();
    	
    	User owner = getUser(1);
    	Directory currentDirectory = getDirectory(1);
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);
    	
    	Directory d = (Directory) currentDirectory.get("picsFolder");
        //check directory was created correctly
        assertNotNull("directory was not created", d);
        assertEquals("directory name not correct", "MyDrive Application", d.getName());
        assertEquals("directory owner not correct", owner, d.getOwner());
        assertEquals("directory directory not correct", currentDirectory, d.getDirectory());
        assertEquals("directory user permission not correct", userPermission, d.getUserPermission());
        assertEquals("directory others permission not correct", othersPermission, d.getOthersPermission());
        //assertEquals("directory id not correct", 2, a.getId());
    }
    
    
    @Test(expected = PermissionDeniedException.class)
    public void notPermittedFileCreation() {
    	 CreateFileService service = new CreateFileService(2, "attemptFile", "attempt", "PlainFile");
    	 service.execute();
    }
    
    
	@Test (expected = FileAlreadyExistsException.class)
    public void duplicatedFileCreation() {
    	 CreateFileService service = new CreateFileService(1, "agenda-Ana", "receitas", "PlainFile");
    	 service.execute();
    }

	
	@Test (expected = InvalidFileNameException.class)
    public void invalidFileNameCreation1() {
    	CreateFileService service = new CreateFileService(1, "ab/cd", "attempt", "PlainFile"); 
    	service.execute();
    }

	@Test (expected = InvalidFileNameException.class)
    public void invalidFileNameCreation2() {
    	CreateFileService service = new CreateFileService(1, "ab\0cd", "attempt", "PlainFile"); 
    	service.execute();
    }
	
	
	@Test (expected = MaximumPathException.class)
	public void maxPathExceededFileCreation() {
		String name = ""; //caso limite
		CreateFileService service = new CreateFileService(1, name, "attempt", "PlainFile");
		service.execute();
	}
    

}	


*/









/*
DRAFTS   ---------------------------------------------------------------------------------------


// IMPORTANTE PARA EVITAR OS COMBOIOS 


	private User getUser(long token) {
		User u = MyDriveService.getInstance().getCurrentUser(token);
		return u;
	}
	
	private Directory getDirectory(long token) {
		Directory d = MyDriveService.getInstance().getCurrentDir(token);
		return d;
	}




    @Test
    public void successPlainFile() {
        CreateFileService service = new CreateFileService(1, "calendar", "day 1 - nothing to do", "PlainFile");
        service.execute();
        
        //check plain file was created
        //User owner = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentUser();
        //Directory currentDirectory = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentDirectory();
        
        Directory currentDirectory = MyDriveService.getMyDrive().getCurrentDir(1);
        
        PlainFile pf = (PlainFile) currentDirectory.get("calendar");
        assertNotNull("plain file was not created", pf);
        assertEquals("plain file name not correct", "calendar", pf.getName());
    	
        //assertEquals("plain file owner not correct", pf.getOwner(), owner);
    }

    
    @Test
    public void successDirectory() {
        CreateFileService service = new CreateFileService("folder1", "", "Dir", 10); //token=10, content é vazio
        service.execute();
        
        //check directory was created
        //User owner = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentUser();
        //Directory currentDirectory = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentDirectory;
        
        Directory d = (Directory) getDirectory().get("folder1");
        assertNotNull("directory was not created", d);
        assertEquals("directory name not correct", "folder1", d.getName());
    	//assertEquals("directory owner not correct", d.getOwner(), owner);
        
        //alternativa: assertTrue("directory was not created", currentDirectory.hasFile("folder1")) ; 
    }
    
    
    
    @Test (expected = PermissionDeniedException.class)
    public void notPermittedFileCreation() {
    	 CreateFileService service = new CreateFileService("dir1", "", "Dir", 2);
    }


	@Test (expected = FileAlreadyExistsException.class)
    public void duplicatedFileCreation() {
    	 CreateFileService service = new CreateFileService("folder1", "", "ana-calendario", 10); //token=10
    }


	@Test (expected = InvalidFileNameException.class)
    public void invalidFileNameCreation() {
    	 CreateFileService service = new CreateFileService("folder1", "", "\0", 10); //token=10
    }


	@Test (expected = MaximumPathException.class)
	public void maxPathExceededFileCreation() {
		CreateFileService service = new CreateFileService("folder1", "", "\0", 10);
	}
	
}
*/
