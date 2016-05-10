package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.RootUser;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.LinkWithoutContentException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;



public class CreateFileTest extends AbstractServiceTest {

	private long token0;
	private long token1;
	private long token2;
	private long token3;
	private SessionManager sm;
	
	protected void populate() {
		
		MyDrive md = MyDriveService.getMyDrive();
		sm = md.getSessionManager();
				
		//User u0 = md.getUserByUsername("root");
		//RootUser u0 = RootUser.getInstance();
		User u0 = md.getRootUser();
		User u1 = new User(md, "ana", "grandepass1", "Ana");
		md.addUsers(u1);
		User u2 = new User(md, "maria", "grandepass2", "Maria");
		md.addUsers(u2);
		User u3 = new User(md, "filipa", "grandepass3", "Filipa");
		md.addUsers(u3);
		
		Directory rootdir = md.getRootDirectory();
		Directory home = (Directory)rootdir.get("home");

		//Directory roothome = u0.getMainDirectory();
		home.setOthersPermission(new Permission("rwx-"));


//		Directory dir0 = u0.getMainDirectory();
//		Directory dir1 = u1.getMainDirectory();
//		Directory dir2 = u2.getMainDirectory();
//		Directory dir3 = u3.getMainDirectory();
		
		Directory dir0 = u0.getMainDirectory();
		Directory dir1 = new Directory("ana", 10, u1, home); //id=10
		Directory dir2 = new Directory("maria", 20, u2, home); //id=20
		Directory dir3 = new Directory("filipa", 30, u3, home); //id=30
		
//		u1.setMainDirectory(dir1);
//		u2.setMainDirectory(dir2);
//		u3.setMainDirectory(dir3);
		
		
		Session s0 = new Session("root", "***", sm);
		s0.setCurrentDir(dir0);
	    token0 = s0.getToken();
		
		Session s1 = new Session("ana", "grandepass1", sm); // ana - token=1
	    s1.setCurrentDir(dir1);
	    token1 = s1.getToken();
	    
	    Session s2 = new Session("maria", "grandepass2", sm); // maria - token=2
	    s2.setCurrentDir(dir2);
	    token2 = s2.getToken();
	    
	    Session s3 = new Session("filipa", "grandepass3", sm); // filipa - token=3
	    s3.setCurrentDir(dir3);
	    token3 = s3.getToken();
	    
	    PlainFile pf1 = new PlainFile("agenda-Ana", 31, u1, "cozinhar para o Rui", dir1); // id=31
		
	    
		s0.setCurrentDir(dir2);
		Directory forbidden = new Directory("forbiddenFolder", 50, u0, dir2); // id=50
		Permission rootP = new Permission(true, true, true, true);
		Permission othersP = new Permission(true, false, true, true); //caso limite: pode-se fazer tudo menos criar/remover ficheiros dessa pasta
		forbidden.setUserPermission(rootP);
		forbidden.setOthersPermission(othersP);
		s2.setCurrentDir(forbidden);
		s0.setCurrentDir(dir0);
		home.setOthersPermission(new Permission("r-x-"));

	}
	
	// TESTS
	
    @Test
    public void successPlainFile() {
        CreateFileService service = new CreateFileService(token1, "calendar", "day 1 - nothing to do", "PlainFile");
        service.execute();
       
        User owner = sm.getSession(token1).getUser();
        Directory currentDirectory = sm.getSession(token1).getCurrentDir();
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);

        PlainFile pf = (PlainFile) currentDirectory.get("calendar");
        //check plain file was created correctly
        assertNotNull("plain file was not created", pf);
        assertEquals("plain file name not correct", "calendar", pf.getName());
        assertEquals("plain file content not correct", "day 1 - nothing to do", pf.getContent());
        assertEquals("plain file owner not correct", owner, pf.getOwner());
        assertEquals("plain file directory not correct", currentDirectory, pf.getDirectory());
        assertEquals("plain file user read permission not correct", userPermission.getRead(), pf.getUserPermission().getRead());
        assertEquals("plain file user write permission not correct", userPermission.getWrite(), pf.getUserPermission().getWrite());
        assertEquals("plain file user execute permission not correct", userPermission.getExecute(), pf.getUserPermission().getExecute());
        assertEquals("plain file user eliminate permission not correct", userPermission.getEliminate(), pf.getUserPermission().getEliminate());
        assertEquals("plain file others read permission not correct", othersPermission.getRead(), pf.getOthersPermission().getRead());
        assertEquals("plain file others write permission not correct", othersPermission.getWrite(), pf.getOthersPermission().getWrite());
        assertEquals("plain file others execute permission not correct", othersPermission.getExecute(), pf.getOthersPermission().getExecute());
        assertEquals("plain file others eliminate permission not correct", othersPermission.getEliminate(), pf.getOthersPermission().getEliminate());
        //assertEquals("plain file id not correct", 2, pf.getId());
    }


    @Test
    public void successLink() {
    	CreateFileService service = new CreateFileService(token1, "agenda-Ana_link", "/home/ana/agenda-Ana", "Link"); // home/ana ou /home/ana
    	service.execute();
    	
    	User owner = sm.getSession(token1).getUser();
    	Directory currentDirectory = sm.getSession(token1).getCurrentDir();
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);
    	
    	Link l = (Link) currentDirectory.get("agenda-Ana_link");
        //check link was created correctly
        assertNotNull("link was not created", l);
        assertEquals("link name not correct", "agenda-Ana_link", l.getName());
        assertEquals("link content not correct", "/home/ana/agenda-Ana", l.getContent());
        assertEquals("link owner not correct", owner, l.getOwner());
        assertEquals("link directory not correct", currentDirectory, l.getDirectory());      
        assertEquals("link user read permission not correct", userPermission.getRead(), l.getUserPermission().getRead());
        assertEquals("link user write permission not correct", userPermission.getWrite(), l.getUserPermission().getWrite());
        assertEquals("link user execute permission not correct", userPermission.getExecute(), l.getUserPermission().getExecute());
        assertEquals("link user eliminate permission not correct", userPermission.getEliminate(), l.getUserPermission().getEliminate());
        assertEquals("link others read permission not correct", othersPermission.getRead(), l.getOthersPermission().getRead());
        assertEquals("link others write permission not correct", othersPermission.getWrite(), l.getOthersPermission().getWrite());
        assertEquals("link others execute permission not correct", othersPermission.getExecute(), l.getOthersPermission().getExecute());
        assertEquals("link others eliminate permission not correct", othersPermission.getEliminate(), l.getOthersPermission().getEliminate());        
        
        
        //assertEquals("link id not correct", 2, l.getId());
    }
    
    
    @Test
    public void successApp() {
    	CreateFileService service = new CreateFileService(token1, "MyDrive Application", "pt.tecnico.mydrive.MyDriveApplication.main", "App"); // content = "pt.tecnico.mydrive.domain.MyDrive.pwd 1", onde 1 é o argumento (token, neste caso)
    	service.execute();
    	
    	User owner = sm.getSession(token1).getUser();
    	Directory currentDirectory = sm.getSession(token1).getCurrentDir();
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);
    	
    	App a = (App) currentDirectory.get("MyDrive Application");
        //check app was created correctly
        assertNotNull("app was not created", a);
        assertEquals("app name not correct", "MyDrive Application", a.getName());
        assertEquals("app content not correct", "pt.tecnico.mydrive.MyDriveApplication.main", a.getContent());
        assertEquals("app owner not correct", owner, a.getOwner());
        assertEquals("app directory not correct", currentDirectory, a.getDirectory());
        assertEquals("app user read permission not correct", userPermission.getRead(), a.getUserPermission().getRead());
        assertEquals("app user write permission not correct", userPermission.getWrite(), a.getUserPermission().getWrite());
        assertEquals("app user execute permission not correct", userPermission.getExecute(), a.getUserPermission().getExecute());
        assertEquals("app user eliminate permission not correct", userPermission.getEliminate(), a.getUserPermission().getEliminate());
        assertEquals("app others read permission not correct", othersPermission.getRead(), a.getOthersPermission().getRead());
        assertEquals("app others write permission not correct", othersPermission.getWrite(), a.getOthersPermission().getWrite());
        assertEquals("app others execute permission not correct", othersPermission.getExecute(), a.getOthersPermission().getExecute());
        assertEquals("app others eliminate permission not correct", othersPermission.getEliminate(), a.getOthersPermission().getEliminate());        
        
        
        //assertEquals("app id not correct", 2, a.getId());
    }
    
    
    @Test
    public void successDirectory() {
    	CreateFileService service = new CreateFileService(token1, "picsFolder", "Dir");
    	service.execute();
    	
    	User owner = sm.getSession(token1).getUser();
    	Directory currentDirectory = sm.getSession(token1).getCurrentDir();
        Permission userPermission = new Permission(true, true, true, true);
        Permission othersPermission = new Permission(false, false, false, false);
    	
    	Directory d = (Directory) currentDirectory.get("picsFolder");
        //check directory was created correctly
        assertNotNull("directory was not created", d);
        assertEquals("directory name not correct", "picsFolder", d.getName());
        assertEquals("directory owner not correct", owner, d.getOwner());
        assertEquals("directory directory not correct", currentDirectory, d.getDirectory());
        assertEquals("directory user read permission not correct", userPermission.getRead(), d.getUserPermission().getRead());
        assertEquals("directory user write permission not correct", userPermission.getWrite(), d.getUserPermission().getWrite());
        assertEquals("directory user execute permission not correct", userPermission.getExecute(), d.getUserPermission().getExecute());
        assertEquals("directory user eliminate permission not correct", userPermission.getEliminate(), d.getUserPermission().getEliminate());
        assertEquals("directory others read permission not correct", othersPermission.getRead(), d.getOthersPermission().getRead());
        assertEquals("directory others write permission not correct", othersPermission.getWrite(), d.getOthersPermission().getWrite());
        assertEquals("directory others execute permission not correct", othersPermission.getExecute(), d.getOthersPermission().getExecute());
        assertEquals("directory others eliminate permission not correct", othersPermission.getEliminate(), d.getOthersPermission().getEliminate());        
        
        //assertEquals("directory id not correct", 2, a.getId());
    }
    
    
    @Test(expected = PermissionDeniedException.class)
    public void notPermittedFileCreation() {
    	 CreateFileService service = new CreateFileService(token2, "attemptFile", "attempt", "PlainFile");
    	 service.execute();
    }
    
    
	@Test (expected = FileAlreadyExistsException.class)
    public void duplicatedFileCreation1() {
    	 CreateFileService service = new CreateFileService(token1, "agenda-Ana", "receitas", "PlainFile"); //nome agenda-Ana é único, apesar do conteúdo do ficehiro que se queria criar ser diferente
    	 service.execute();
    }

	
	// independentemente do tipo do ficheiro, o nome tem de ser único
	@Test (expected = FileAlreadyExistsException.class)
    public void duplicatedFileCreation2() {
    	 CreateFileService service = new CreateFileService(token1, "agenda-Ana", "/home/ana/agenda-Ana", "Link");
    	 service.execute();
    }


	// independentemente do tipo do ficheiro, o nome tem de ser único
	@Test (expected = FileAlreadyExistsException.class)
    public void duplicatedFileCreation3() {
    	 CreateFileService service = new CreateFileService(token1, "agenda-Ana", "App");
    	 service.execute();
    }
	
	
	// independentemente do tipo do ficheiro, o nome tem de ser único
	@Test (expected = FileAlreadyExistsException.class)
    public void duplicatedFileCreation4() {
    	 CreateFileService service = new CreateFileService(token1, "agenda-Ana", "Dir");
    	 service.execute();
    }
    
	
	@Test (expected = InvalidFileNameException.class)
    public void invalidFileNameCreation1() {
    	CreateFileService service = new CreateFileService(token1, "ab/cd", "attempt", "PlainFile"); 
    	service.execute();
    }

	
	@Test (expected = InvalidFileNameException.class)
    public void invalidFileNameCreation2() {
    	CreateFileService service = new CreateFileService(token1, "ab\0cd", "attempt", "PlainFile"); 
    	service.execute();
    }

	
	
	@Test (expected = MaximumPathException.class)
	public void maxPathExceededFileCreation() {
		String hugeDirName = "";
		for (int i = 0; i<1011; i++) { // "/home/filipa" length=12, mais descontar /, logo 1024-13=1011
			hugeDirName += "a";
		}

		CreateFileService service = new CreateFileService(token3, hugeDirName, "attempt", "PlainFile");
		service.execute();
	}


    @Test (expected = LinkWithoutContentException.class)
	public void linkWithouthContentFileCreation() {
		CreateFileService service = new CreateFileService(token1, "invalidLink", "", "Link");
		service.execute();
	}
    
	
	@Test (expected = InvalidTokenException.class)
    public void invalidToken() { //Testing CD with an invalid token
		long token = new BigInteger(64, new Random()).longValue();
		while (token == token0 || token == token1 || token == token2 || token == token3){
			token = new BigInteger(64, new Random()).longValue();
		}
    	    	
    	CreateFileService service = new CreateFileService(token, "impossible", "content1", "PlainFile");    
        service.execute();
    }
}	