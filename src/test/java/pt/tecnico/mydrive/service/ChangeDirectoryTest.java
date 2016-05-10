package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class ChangeDirectoryTest extends AbstractServiceTest {

	private String hugeDirName = "";
	private long token0;
	private long token1;
	private long token2;
	SessionManager sm = null;
	
	
	protected void populate() {
		
		MyDrive md = MyDriveService.getMyDrive();
    	sm = md.getSessionManager();
    	
		User root = md.getRootUser();
    	User u1 = new User(md, "neto", "grande***", "netjinho");
    	md.addUsers(u1);
	    User u2 = new User(md, "zecarlos", "grande***", "zecarlos");
    	md.addUsers(u2);
	    
    	Directory home = (Directory) md.getRootDirectory().get("home");    	
		home.setOthersPermission(new Permission("rwx-"));

    	Directory homeRoot = root.getMainDirectory();
    	Directory home1 = new Directory("neto", 425, u1, home); //id=425
	    Directory home2 = new Directory("zecarlos", 645, u2, home); //id=645
	    
	    
	    PlainFile p1 = new PlainFile("example.txt", 144, u1, "", home1); //id=144
	    
	    Permission userPerm = new Permission(true, true, false, true); //caso limite: não pode executar, que corresponde a fazer cd
	    Permission othersPerm = new Permission(false, false, false, false);
	    
	    
	    Directory music = new Directory("music", 983, u1, home1); //id=983;
	    Directory emanuel = new Directory("emanuel", 988, u1, music); //id=988
	    Directory bestOf = new Directory("bestOf", 992, u1, emanuel); //id=992
	    
	    
	    Directory videos = new Directory("videos", 312, u1, home1);
	    Directory videoclips = new Directory("videoclips", 419, u1, videos);
	    Directory youtube = new Directory("youtube", 653, u1, videoclips);
	    videoclips.setUserPermission(userPerm);
	    videoclips.setOthersPermission(othersPerm);
	    
	    
	    Directory images = new Directory("images", 765, u2, home2); //id=765
	    home2.setUserPermission(userPerm);
	    home2.setOthersPermission(othersPerm);
	    images.setUserPermission(new Permission("rw-d"));
	    
	    
	     
		hugeDirName = "";
		for (int i = 0; i<1013; i++) { // "/home/neto" length=10, mais descontar /, logo 1024-11=1013
			hugeDirName += "a"; 
		}
	    
	 	//String name = StringUtils.rightPad("/home/root", 1024, "/zezacarias");
	    //md.getDirectoryByAbsolutePath(3, name); //Creates big directory path
	    
	    
	    Session s0 = new Session("root", "***", sm);
	    s0.setCurrentDir(homeRoot);
	    token0 = s0.getToken();
	    
	    Session s1 = new Session("neto", "grande***", sm);
	 	s1.setCurrentDir(home1);
	 	token1 = s1.getToken();
	 
	 	Session s2 = new Session("zecarlos", "grande***", sm);
	 	s2.setCurrentDir(home2);
	 	token2 = s2.getToken();
		
	    
	}
	
    @Test
    public void successCd() {	
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "music");    
        service.execute();
        
        Directory finalDir = sm.getSession(token1).getCurrentDir();
        String expectedDir = "/home/neto/music";
        
        assertNotNull("null final directory", finalDir);
        assertEquals("error changing directory", expectedDir, finalDir.getAbsolutePath());
    }
    
	
    @Test
    public void successCdRelativePath() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "music/emanuel/bestOf");    
        service.execute();
        
        Directory finalDir = sm.getSession(token1).getCurrentDir();
        String expectedDir = "/home/neto/music/emanuel/bestOf";
        
        assertNotNull("null final directory", finalDir);
        assertEquals("error changing directory", expectedDir, finalDir.getAbsolutePath());
    }
	
    
    @Test
    public void successCdAbsolutePath() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "/home/neto/music/emanuel/bestOf");    
        service.execute();
        
        Directory finalDir = sm.getSession(token1).getCurrentDir();
        String expectedDir = "/home/neto/music/emanuel/bestOf";
        
        assertNotNull("null final directory", finalDir);
        assertEquals("error changing directory", expectedDir, finalDir.getAbsolutePath());
    }

    
	@Test 	
    public void messageToOwnDirectory() { //Testing CD to "." and verify the returned message
    	final String expectedDir = "/home/neto";
    	
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "."); 
        service.execute();
        
        Directory selfDir = sm.getSession(token1).getCurrentDir();

        
		assertEquals("error changing to '.' directory", expectedDir, selfDir.getAbsolutePath());
    }
     
	
    @Test(expected = PermissionDeniedException.class)
    public void notPermittedCd() {
        ChangeDirectoryService service = new ChangeDirectoryService(token2, "images"); 
        service.execute();       
    }

    
    @Test (expected = PermissionDeniedException.class)
    public void notPermittedCd2() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "videos/videoclips/youtube"); 
        service.execute();
    }
    
    
    @Test (expected = PermissionDeniedException.class)
    public void notPermittedCd3() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "/home/neto/videos/videoclips/youtube"); 
    	
        service.execute();
    }
    
    
    @Test (expected = NoSuchFileException.class)
    public void nonExistentDir() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "casa"); 
        service.execute();        
    }
    
    
    @Test (expected = NoSuchFileException.class)
    public void nonExistentRelativeDir() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "music/emanuel/abc"); 
        service.execute();
    }
    
    
    @Test (expected = NoSuchFileException.class)
    public void nonExistentAbsoluteDir() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "/home/neto/music/abc/emanuel"); 
        service.execute();
    }
    
    
    @Test (expected = FileNotCdAbleException.class)
    public void notADirectory() {
        ChangeDirectoryService service = new ChangeDirectoryService(token1, "example.txt"); 
        service.execute();
    }
        
    
	@Test (expected = InvalidTokenException.class)
    public void invalidToken() { //Testing CD with an invalid token
		long token = new BigInteger(64, new Random()).longValue();
		while (token == token0 || token == token1 || token == token2){
			token = new BigInteger(64, new Random()).longValue();
		}
    	final String targetDir = "/home/neto/music";
    	    	
        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir);    
        service.execute();
    }
	
}
