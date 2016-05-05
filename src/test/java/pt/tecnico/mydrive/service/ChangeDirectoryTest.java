//package pt.tecnico.mydrive.service;
//
//import static org.junit.Assert.*;
//
//import org.apache.commons.lang.StringUtils;
//import org.junit.Test;
//
//import pt.tecnico.mydrive.domain.App;
//import pt.tecnico.mydrive.domain.Directory;
//import pt.tecnico.mydrive.domain.Link;
//import pt.tecnico.mydrive.domain.MyDrive;
//import pt.tecnico.mydrive.domain.Permission;
//import pt.tecnico.mydrive.domain.PlainFile;
//import pt.tecnico.mydrive.domain.Session;
//import pt.tecnico.mydrive.domain.User;
//import pt.tecnico.mydrive.exception.FileIsNotReadAbleException;
//import pt.tecnico.mydrive.exception.FileNotCdAbleException;
//import pt.tecnico.mydrive.exception.FileNotDirectoryException;
//import pt.tecnico.mydrive.exception.InvalidTokenException;
//import pt.tecnico.mydrive.exception.MaximumPathException;
//import pt.tecnico.mydrive.exception.NoSuchFileException;
//import pt.tecnico.mydrive.exception.PermissionDeniedException;
//
//public class ChangeDirectoryTest extends AbstractServiceTest {
//
//	private String hugeDirName = "";
//	
//	
//	protected void populate() {
//		
//		MyDrive md = MyDrive.getInstance();
//    	
//		User root = md.getRootUser();
//    	User u1 = new User("neto", "***", "netjinho");
//    	md.addUsers(u1);
//	    User u2 = new User("zecarlos", "***", "zecarlos");
//    	md.addUsers(u2);
//	    
//    	Directory home = (Directory) md.getRootDirectory().get("home");    	
//	    
//    	Directory homeRoot = root.getMainDirectory();
//    	Directory home1 = new Directory("neto", 425, u1, home); //id=425
//	    Directory home2 = new Directory("zecarlos", 645, u2, home); //id=645
//	    
//	    
//	    PlainFile p1 = new PlainFile("example.txt", 144, u1, "", home1); //id=144
//	    
//	    Permission userPerm = new Permission(true, true, false, true); //caso limite: não pode executar, que corresponde a fazer cd
//	    Permission othersPerm = new Permission(false, false, false, false);
//	    
//	    
//	    Directory music = new Directory("music", 983, u1, home1); //id=983;
//	    Directory emanuel = new Directory("emanuel", 988, u1, music); //id=988
//	    Directory bestOf = new Directory("bestOf", 992, u1, emanuel); //id=992
//	    
//	    
//	    Directory videos = new Directory("videos", 312, u1, home1);
//	    Directory videoclips = new Directory("videoclips", 419, u1, videos);
//	    Directory youtube = new Directory("youtube", 653, u1, videoclips);
//	    videoclips.setUserPermission(userPerm);
//	    videoclips.setOthersPermission(othersPerm);
//	    
//	    
//	    Directory images = new Directory("images", 765, u2, home2); //id=765
//	    home2.setUserPermission(userPerm);
//	    home2.setOthersPermission(othersPerm);
//	     
//		hugeDirName = "";
//		for (int i = 0; i<1013; i++) { // "/home/neto" length=10, mais descontar /, logo 1024-11=1013
//			hugeDirName += "a"; 
//		}
//
//		Directory hugeDir = new Directory(hugeDirName, 239, u1, home1); // id=239
//
//	    
//	 	//String name = StringUtils.rightPad("/home/root", 1024, "/zezacarias");
//	    //md.getDirectoryByAbsolutePath(3, name); //Creates big directory path
//	    
//	    
//	    Session s0 = new Session(root, 0, md);
//	    s0.setCurrentDir(homeRoot);
//	    
//	    Session s1 = new Session(u1, 1, md);
//	 	s1.setCurrentDir(home1);
//	 
//	 	Session s2 = new Session(u2, 2, md);
//	 	s2.setCurrentDir(home2);
//
//	 	
//
//
//	    
//	}
//	
//	
//	
//	// SHORTCUTS
//	
//	private User getUser(long token) {
//		User u = MyDriveService.getMyDrive().getCurrentUserByToken(token);
//		return u;
//	}
//	
//	private Directory getDirectory(long token) {
//		Directory d = MyDriveService.getMyDrive().getCurrentDirByToken(token);
//		return d;
//	}
//	
//	
//	
//    @Test
//    public void successCd() {	
//    	final long token = 1;
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "music");    
//        service.execute();
//        
//        Directory finalDir = getDirectory(token);
//        String expectedDir = "/home/neto/music";
//        
//        assertNotNull("null final directory", finalDir);
//        assertEquals("error changing directory", expectedDir, finalDir.getAbsolutePath());
//    }
//    
//	
//    @Test
//    public void successCdRelativePath() {
//    	final long token = 1;
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "music/emanuel/bestOf");    
//        service.execute();
//        
//        Directory finalDir = getDirectory(token);
//        String expectedDir = "/home/neto/music/emanuel/bestOf";
//        
//        assertNotNull("null final directory", finalDir);
//        assertEquals("error changing directory", expectedDir, finalDir.getAbsolutePath());
//    }
//	
//    
//    @Test
//    public void successCdAbsolutePath() {
//    	final long token = 1;
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/neto/music/emanuel/bestOf");    
//        service.execute();
//        
//        Directory finalDir = getDirectory(token);
//        String expectedDir = "/home/neto/music/emanuel/bestOf";
//        
//        assertNotNull("null final directory", finalDir);
//        assertEquals("error changing directory", expectedDir, finalDir.getAbsolutePath());
//    }
//
//    
//	@Test 	
//    public void messageToOwnDirectory() { //Testing CD to "." and verify the returned message
//		final long token = 1;
//    	final String expectedDir = "/home/neto";
//    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "."); 
//        service.execute();
//        
//        Directory selfDir = getDirectory(token);
//
//        
//		assertEquals("error changing to '.' directory", expectedDir, selfDir.getAbsolutePath());
//    }
//     
//	
//    @Test(expected = PermissionDeniedException.class)
//    public void notPermittedCd() {
//    	final long token = 2;
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "images"); 
//        service.execute();       
//    }
//
//    
//    @Test (expected = PermissionDeniedException.class)
//    public void notPermittedCd2() {
//    	final long token = 1;
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "videos/videoclips/youtube"); 
//    	service.execute();       
//    }
//    
//    
//    @Test (expected = PermissionDeniedException.class)
//    public void notPermittedCd3() {
//    	final long token = 1;
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/neto/videos/videoclips/youtube"); 
//    	service.execute();
//    }
//    
//    
//    @Test (expected = NoSuchFileException.class)
//    public void nonExistentDir() {
//    	final long token = 1;
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "casa"); 
//        service.execute();        
//    }
//    
//    
//    @Test (expected = NoSuchFileException.class)
//    public void nonExistentRelativeDir() {
//    	final long token = 1;
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "music/emanuel/abc"); 
//        service.execute();
//    }
//    
//    
//    @Test (expected = NoSuchFileException.class)
//    public void nonExistentAbsoluteDir() {
//    	final long token = 1;
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/neto/music/abc/emanuel"); 
//        service.execute();
//    }
//    
//    
//    @Test (expected = FileNotCdAbleException.class)
//    public void notADirectory() {
//    	final long token = 1;
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, "example.txt"); 
//        service.execute();
//    }
//        
//    
//	@Test (expected = InvalidTokenException.class)
//    public void invalidToken() { //Testing CD with an invalid token
//		final long token = 1234567890;
//    	final String targetDir = "/home/neto/music";
//    	    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, targetDir);    
//        service.execute();
//    }
//    
//    
//	@Test 
//    public void successCdWithMaxPath() { //Testing CD with 1024 character in an absolute path
//    	final long token = 1;
//    	
//        ChangeDirectoryService service = new ChangeDirectoryService(token, hugeDirName); 
//        service.execute();
//        
//        Directory finalDir = getDirectory(token);
//        String expected = "/home/neto/" + hugeDirName;
//        
//        assertEquals("error changing to directory with maximum path size",  expected, finalDir.getAbsolutePath());
//    }
//
//
//}
