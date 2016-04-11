package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class ChangeDirectoryTest extends AbstractServiceTest {

	protected void populate() {
		
		MyDrive md = MyDrive.getInstance();
		User u1 = new User("Catio Balde", "pass1", "Catio");
	    User u2 = md.getUserByUsername("root");
	    Directory home1 = u1.getMainDirectory();
		Directory home2 = u2.getMainDirectory();
	    
	    Directory d1 = new Directory("folder", 125, u1, home1);
	    Directory d2 = new Directory("folder2", 126, u2, home2);        
	    PlainFile p1 = new PlainFile("Caso Bruma", 123, u1, "conteudo1", home1);
		PlainFile p2 = new PlainFile("Exemplo", 124, u2, "conteudo3", home2);

		Directory rootdir = MyDrive.getInstance().getRootDirectory();
	    
//	    Session s1 = new Session(u1, 1);
//	    s1.setCurrentDirectory(home1);
//
//	    Session s2 = new Session(u1, 2);
//	    s2.setCurrentDirectory(home2);
//
//	    Session s3 = new Session(u1, 3);
//	    s3.setCurrentDirectory(rootdir);
//
//	    Session s4 = new Session(u2, 4);
//	    s3.setCurrentDirectory(home2);
//
//	    Session s5 = new Session(u1, 5);
//	    s5.setCurrentDirectory(rootdir.get("home"));
//
//	    Session s6 = new Session(u1, 6);
//	    s6.setCurrentDirectory(rootdir.get("home"));
//		
	}


// IMPORTANTE PARA EVITAR OS COMBOIOS ----------------------------------------------------------------


	private User getUser(long token) {
		User u = MyDriveService.getLogin().getSessionByToken(token).getCurrentUser();
		return u;
	}
	
	private Directory getDirectory(long token) {
		Directory d = MyDriveService.getLogin().getSessionByToken(token).getCurrentDirectory();
		return d;
	}

// ----------------------------------------------------------------------------------------------------


    @SuppressWarnings("deprecation")
	@Test
    public void successCDRelativePath() { //Testing CD with a valid token, permitted and existing relative path
    	MyDrive md = MyDrive.getInstance();
    	long token = 0; //FIXME create a valid token
    	Directory dir = md.getDirectoryByAbsolutePath(token, "/home");
    	User root = md.getUserByUsername("root");
    	
    	md.getSessionByToken(token).setCurrentUser(root);
    
    	md.setCurrentDir(token, dir);
    	
        ChangeDirectoryService service = new ChangeDirectoryService("root", false, token); 
        
        service.execute();
        
        
        md.getCurrentDir(token);
//        try{
//        	assertEquals("/home/root", md.pwd(token), "");
//        	Assert.fail("Changed Directory with success");
//        }
//        catch(Exception e){
//        	String expectedMessage = "this is the message I expect to get";
//            Assert.assertEquals( "Exception message must be correct", expectedMessage, e.getMessage() );
//        }
        assertEquals("Changed Directory with a relative path with success", "/home/root", md.pwd(token));
        
    	//assertEquals("plain file owner not correct", pf.getOwner(), owner);
    }

    
    @Test
    public void successCDAbsolutePath() { //Testing CD with a valid token, permitted and existing absolute path
    	MyDrive md = MyDrive.getInstance();
    	long token = 0; //FIXME create a valid token

    	User root = md.getUserByUsername("root");
    	
    	md.getSessionByToken(token).setCurrentUser(root);
   
        ChangeDirectoryService service = new ChangeDirectoryService("/home/root", true, token); 
        
        service.execute();
        
        md.getCurrentDir(token);
        
        assertEquals("Changed Directory with an absolute path with success", "/home/root", md.pwd(token));
    }
    
    
    @Test (expected = PermissionDeniedException.class)
    public void notPermittedRelativePath() { //Testing CD with relative path with not permitted directory (no read permissions)
    	 
    }


	@Test (expected = PermissionDeniedException.class)
    public void notPermittedAbsolutePath() { //Testing CD with absolute path with not permitted directory (no read permissions)
    	 
    }
	
	@Test (expected = NoSuchFileException.class)
    public void nonExistentRelativePath() { //Testing CD with non-existent relative path
    	 
    }
	
	@Test (expected = NoSuchFileException.class)
    public void nonExistentAbsolutePath() { //Testing CD with non-existent absolute path
    	 
    }
	
	@Test 	
    public void messageToOwnDirectory() { //Testing CD to "." and verify the returned message
    	 
    }

	

	@Test
	public void deletePermittedFileAbsolutePath(){

		DeleteService dfs = DeleteService("/home/Catio Balde/Caso Bruma", 1);
		dfs.execute();
	
		assertFalse("file was not removed", home1.hasFile("Caso Bruma"));	
	
	}

}

//PROF 
//	@Test
//	public void success() {
//		final String personName = "João";
//		RemoveContactService service = new RemoveContactService(personName, "António");
//		service.execute();
//
//		// check contact was removed
//		Contact c = getContact(personName, "António");
//		assertNull("contact was not removed", c);
//		assertEquals("Invalid number of contacts", 0, PhoneBookService.getPerson(personName).getContactSet().size());
//	}
//	
//	
//
//	@Test(expected = ContactDoesNotExistException.class)
//	public void removeInvalidContact() {
//		final String personName = "João";
//		RemoveContactService service = new RemoveContactService(personName, "Ant");
//		service.execute();
//	}
//
//	@Test(expected = PersonDoesNotExistException.class)
//	public void invalidPerson() {
//		final String personName = "José";
//		RemoveContactService service = new RemoveContactService(personName, "António");
//		service.execute();
//	}

