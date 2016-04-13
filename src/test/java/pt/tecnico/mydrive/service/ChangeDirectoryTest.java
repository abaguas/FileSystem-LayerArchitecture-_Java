package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import junit.framework.Assert;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileIsNotReadAbleException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class ChangeDirectoryTest extends AbstractServiceTest {
	//FIXME açways finish with assert?
	protected void populate() {
		
		MyDrive md = MyDrive.getInstance();
    	long token = 0; //FIXME create a valid token
    	
    	md.getSessionByToken(token);
    	
    	User user1 = new User("neto","***", "netjinho");
    	PlainFile pf = new PlainFile("example.txt", 1024, "");
		
	   
	    Session s1 = new Session(user1, 1);
//	    s1.setCurrentDirectory(home1);
	
	}



    @Test
    public void successCdRelativePath() { //Testing CD with a valid token, permitted and existing relative path
    	MyDrive md = MyDrive.getInstance();
    	long token = 0; //FIXME create a valid token
    	final String path = "root"; //FIXME cd to /home
    	Directory dir = md.getDirectoryByAbsolutePath(token, path);
    	User root = md.getUserByUsername("root");
    	
    	md.setCurrentUserByToken(token, root);
    
    	md.setCurrentDirByToken(token, dir);
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, "root"); 
        
        service.execute();
        
        
        md.getCurrentDirByToken(token);

        assertEquals("Changed Directory with a relative path with success", "/home/root", md.pwd(token));

    }

    
    @Test
    public void successCdAbsolutePath() { //Testing CD with a valid token, permitted and existing absolute path
    	MyDrive md = MyDrive.getInstance();
    	final long token = 0; //FIXME create a valid token
    	final String path = "/home/root";
    	User root = md.getUserByUsername("root");
    	
    	md.setCurrentUserByToken(token, root);
   
        ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        
        service.execute();
        
        md.getCurrentDirByToken(token);
        
        assertEquals("Changed Directory with an absolute path with success", "/home/root", md.pwd(token));
    }
    
    
    @Test (expected = PermissionDeniedException.class)
    public void notPermittedRelativePath() { //Testing CD with relative path with not permitted directory (no read permissions)
    	MyDrive md = MyDrive.getInstance();
    	final long token = 0; //FIXME create a valid token
    	final String path = "root"; //FIXME check permissions and cd to /home
    	User user = md.getUserByUsername("netjinho"); //FIXME create user netjinho
    	
    	md.setCurrentUserByToken(token, user);
   
        ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        
        service.execute();
        
    }


	@Test (expected = PermissionDeniedException.class)
    public void notPermittedAbsolutePath() { //Testing CD with absolute path with not permitted directory (no read permissions)
		MyDrive md = MyDrive.getInstance();
    	final long token = 0; //FIXME create a valid token
    	final String path = "/home/root"; //FIXME check permissions
    	User user = md.getUserByUsername("netjinho"); //FIXME create user netjinho
    	
    	md.setCurrentUserByToken(token, user);
   
        ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        
        service.execute();
    }
	
	@Test (expected = NoSuchFileException.class)
    public void nonExistentRelativePath() { //Testing CD with non-existent relative path
		final long token = 0; //FIXME create a valid token
		final String path = "zacarias"; //FIXME cd to /home
		ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        service.execute();
		
    }
	
	@Test (expected = NoSuchFileException.class)
    public void nonExistentAbsolutePath() { //Testing CD with non-existent absolute path
		final long token = 0; //FIXME create a valid token
		final String path = "/home/zacarias";
		ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        service.execute();
    }
	
	@Test 	
    public void messageToOwnDirectory() { //Testing CD to "." and verify the returned message
    	 //FIXME change to success?
    }
	
	@Test (expected = MaximumPathException.class)
    public void absolutePathTooLarge() { //Testing CD with more than 1024 character in an absolute path
		final long token = 0; //FIXME create a valid token
		final String path = StringUtils.leftPad("/zacarias", 1025, "/zacarias");
		ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        service.execute();
    }
	
	@Test 
    public void successCdWithMaxPath() { //Testing CD with 1024 character in an absolute path
		MyDrive md = MyDrive.getInstance();
		final long token = 0; //FIXME create a valid token
		final String path = StringUtils.leftPad("/zacarias", 1024, "/zacarias"); //FIXME create these dir
		ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        service.execute();
        
        md.getCurrentDirByToken(token);
        assertEquals("Changed Directory with maximum path size with success", path, md.pwd(token));
    }

	@Test //(expected = InvalidTokenException.class)
    public void invalidToken() { //Testing CD with an invalid token
		final long token = -1; //FIXME create an invalid token
		final String path = "/home/root";
		ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        service.execute();
    }
	
	@Test (expected = FileNotCdAbleException.class)
    public void notADirectory() { //Testing CD with more than 1024 character in an absolute path
		final long token = 0; //FIXME create a valid token
		final String path = "/home/root/example.txt"; //FIXME create example.txt file
		ChangeDirectoryService service = new ChangeDirectoryService(token,path); 
        service.execute();
    }
	

}
