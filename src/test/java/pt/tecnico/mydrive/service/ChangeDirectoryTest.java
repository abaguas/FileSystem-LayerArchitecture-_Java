package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileIsNotReadAbleException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class ChangeDirectoryTest extends AbstractServiceTest {

	
	
	protected void populate() {
		
		MyDrive md = MyDrive.getInstance();
    	
    	User u1 = new User("neto", "***", "netjinho");
    	User u2 = new User("zecarlos", "***", "zecarlos");
	    User root = md.getUserByUsername("root");
	    
	    Directory home1= u1.getMainDirectory();
	    Directory home2= u2.getMainDirectory();
	    Directory homeRoot = root.getMainDirectory();
	    Directory home = (Directory) md.getRootDirectory().get("home");
	    
	    
	    Directory images = new Directory("images",md.generateId(),root,homeRoot);

		Session s1 = new Session(u1,1,md);
	 	s1.setCurrentDir(home1);
	 	
	 	Session s4 = new Session(u1,4,md);
	 	s4.setCurrentDir(home);
	    
	    Session s3 = new Session(root, 3, md);
	    s3.setCurrentDir(homeRoot);
	    
	
	    String name = StringUtils.rightPad("/home/root", 1024, "/zezacarias");
	    md.getDirectoryByAbsolutePath(3, name); //Creates big directory path
	    
	    PlainFile p1 = new PlainFile("example.txt",md.generateId(), u1, "",home1);
	}



    @Test
    public void successCdRelativePath() { //Testing CD with a valid token, permitted and existing relative path
    	final long token = 3; 
    	final String targetDir = "/home/root/images";
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, "images"); 
        
        service.execute();
        
        String result = service.getResult();

		assertEquals("Error changing Directory with a relative path", result, targetDir);

    }

    
    @Test
    public void successCdAbsolutePath() { //Testing CD with a valid token, permitted and existing absolute path
        
    	final long token = 3; 
    	final String targetDir = "/home/neto";
    	    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/neto"); 
        
        service.execute();
        
        String result = service.getResult();

		assertEquals("Error changing Directory with an absolute path", result, targetDir);

    }
    
    
    @Test (expected = PermissionDeniedException.class)
    public void notPermittedRelativePath() { //Testing CD with relative path with not permitted directory (no read permissions)
        
    	final long token = 4;
    	final String targetDir = "zecarlos";
    	    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
        
    }


	@Test (expected = PermissionDeniedException.class)
    public void notPermittedAbsolutePath() { //Testing CD with absolute path with not permitted directory (no read permissions)
    	final long token = 1;
    	final String targetDir = "/home/zecarlos";
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
    }
	
	@Test (expected = NoSuchFileException.class)
    public void nonExistentRelativePath() { //Testing CD with non-existent relative path
    	final long token = 1; 
    	final String targetDir = "zacarias";
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
		
    }
	
	@Test (expected = NoSuchFileException.class)
    public void nonExistentAbsolutePath() { //Testing CD with non-existent absolute path
    	final long token = 1; 
    	final String targetDir = "/home/zacarias";
  
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
    }
	
	@Test 	
    public void messageToOwnDirectory() { //Testing CD to "." and verify the returned message
		final long token = 3;
    	final String targetDir = ("/home/root");
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, "."); 
        
        service.execute();
        
        String result = service.getResult();
		
		assertEquals("Changed to '.' Directory with success", result, targetDir);

    }
	
	@Test (expected = MaximumPathException.class)
    public void absolutePathTooLarge() { //Testing CD with more than 1024 character in an absolute path
		
    	final long token = 3; //FIXME create a valid token
		final String targetDir = StringUtils.rightPad("/home/root/", 1025, "/zezacarias");
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
		
    }
	
	@Test 
    public void successCdWithMaxPath() { //Testing CD with 1024 character in an absolute path

    	final long token = 3;
    	final String targetDir = StringUtils.rightPad("/home/root", 1024, "/zezacarias");
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
        
        String result = service.getResult();

        assertEquals("Error changing Directory with maximum path size", result, targetDir);
       
    }

	@Test (expected = InvalidTokenException.class)
    public void invalidToken() { //Testing CD with an invalid token

		MyDrive md = MyDrive.getInstance();
		final long token = -1;
    	final String targetDir = "/home/root";
    	User root = md.getUserByUsername("root");
    	md.setCurrentUserByToken(token, root);
    	
    	final String path = "/home/root"; 
    	Directory currentDir = md.getDirectoryByAbsolutePath(token, path);
    	
    	md.setCurrentDirByToken(token, currentDir);
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
       
    }
	
	@Test (expected = FileNotCdAbleException.class)
    public void notADirectory() { //Testing CD to a PlainFile
		final long token = 3; 
    	final String targetDir = "/home/root/example.txt";
    	
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir); 
        
        service.execute();
      
    }
	

}
