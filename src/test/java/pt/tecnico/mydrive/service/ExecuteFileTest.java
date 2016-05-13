package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileIsNotExecuteAbleException;
import pt.tecnico.mydrive.exception.InvalidLinkContentException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class ExecuteFileTest extends AbstractServiceTest{
	private long token0;
	private long token1;
	private final PrintStream standard = System.out;
	
	protected void populate() {
		MyDrive md = MyDriveService.getMyDrive();
		SessionManager sm = md.getSessionManager();
		
		Directory home = (Directory) md.getRootDirectory().get("home");
		home.setOthersPermission(new Permission("rwx-"));
		
		User root = md.getRootUser();
	    User u1 = new User(md, "netjinho", "netodoavo", "netjinho");

    	Directory homeRoot = root.getMainDirectory();
    	Directory home1 = u1.getMainDirectory();
	    
	    
	    PlainFile p1 = new PlainFile("example", md.generateId(), u1, "hello", home1); 
	    PlainFile p2 = new PlainFile("private", md.generateId(), root, "this is private", homeRoot);
	    p2.setOthersPermission(new Permission("r---"));

	    PlainFile p3 = new PlainFile("writeExample", md.generateId(), u1, "ls", home1); 
	    App a1 = new App("appGreet", md.generateId(), root, "pt.tecnico.mydrive.presentation.Hello.greet", homeRoot);
	    App a2 = new App("badApp", md.generateId(), root, "should.be.a.method", homeRoot);
	    App a3 = new App("appSum", md.generateId(), root, "pt.tecnico.mydrive.presentation.Hello.sum", homeRoot);
	    App a4 = new App("appBye", md.generateId(), root, "pt.tecnico.mydrive.presentation.Hello.bye", homeRoot);
	    
	    Link l1 = new Link("invalid", md.generateId(), root, "//", homeRoot);
	    Link l2 = new Link("linkAppGreet", md.generateId(), u1, "/home/root/appGreet", home1);
	    Link l3 = new Link("linkAppSum", md.generateId(), root, "appSum", homeRoot);

	    Link l4 = new Link("linkAppBye", md.generateId(), root, "appBye", homeRoot);
//	    Link l4 = new Link("invalid", md.generateId(), root, "//", homeRoot);
	    
	    Directory d1 = new Directory("cannotEnter", md.generateId(), root, homeRoot);
	    d1.setOthersPermission(new Permission("----"));
	    
	    Session s0 = new Session("root", "***", sm);
	    s0.setCurrentDir(homeRoot);
	    token0 = s0.getToken();
	    
	    Session s1 = new Session("netjinho", "netodoavo", sm);
	 	s1.setCurrentDir(home1);
	 	token1 = s1.getToken();
	
		
		
		home.setOthersPermission(new Permission("r-x-"));
	}
	    
	
	@Test (expected=InvalidTokenException.class)
	public void plainFileWithInvalidToken() {
		long token = new BigInteger(64, new Random()).longValue();
		while (token == token0 || token == token1){
			token = new BigInteger(64, new Random()).longValue();
		}
    	    	
		String [] s = {"a", "b", "c"}; 
		
        ExecuteFileService service = new ExecuteFileService(token, "/home", s);    
        service.execute();
    }
	
	@Test (expected=NoSuchFileException.class)
	public void nonExistentPlainFile() {
		String [] s = {"a", "b", "c"}; 
		
        ExecuteFileService service = new ExecuteFileService(token0, "noFile", s);    
        service.execute();
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void nonPermittedPlainFile() {
		String [] s = {"a", "b", "c"}; 
		
        ExecuteFileService service = new ExecuteFileService(token1, "/home/root/private", s);    
        service.execute();
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void nonPermittedDirectory() {
		String [] s = {"a", "b", "c"}; 
		
		ExecuteFileService service = new ExecuteFileService(token1, "/home/root/cannotEnter/file", s);    
        service.execute();
	}
	
	@Test (expected=FileIsNotExecuteAbleException.class)
	public void notExecutableFile() {
		String [] s = {"a", "b", "c"}; 
		
		ExecuteFileService service = new ExecuteFileService(token0, "/home/root", s);    
        service.execute();
	}
	
	@Test 
	public void successNoArgumentsApp() {
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		String [] s = new String[0];
		
		ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appBye", s);    
        service.execute();
        
		String [] t = testOut.toString().split("\n");
		
        assertEquals("There should receive the file Name", "Goodbye myDrive!",t[1]);
		System.setOut(standard);

	}
	
	@Test 
	public void successNoArgumentsAppOnLink() {
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		String [] s = new String[0];
		
		ExecuteFileService service = new ExecuteFileService(token0, "linkAppBye", s);    
        service.execute();
        
		String [] t = testOut.toString().split("\n");
		
        assertEquals("There should receive the file Name", "Goodbye myDrive!",t[1]);
		System.setOut(standard);
	}
	
//	@Test 
//	public void successNoArgumentsPlainFile() {
//		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(testOut));
//		String [] s = new String[0];
//		
//		ExecuteFileService service = new ExecuteFileService(token1, "writeExample", s);    
//        service.execute();
//        
//		String [] t = testOut.toString().split("\n");
//        assertEquals("There should receive the file Name", "sum=9",t[1]);
//		System.setOut(standard);
//	}
	
//	@Test 
//	public void successNoArgumentsPlainFileOnLink() {
//		String [] s = new String[0]; 
//		
//        ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appGreet", s);    
//        service.execute();
//        
////        assertNotNull("null final directory", finalDir);
////        assertEquals("error changing directory", result, "Hello ");
//	}
//	
	@Test 
	public void successSeveralArgumentsApp() {
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		String [] s = {"2", "3", "4"};
		
		ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appSum", s);    
        service.execute();
        
		String [] t = testOut.toString().split("\n");
		
        assertEquals("There should receive the file Name", "sum=9",t[1]);
		System.setOut(standard);
	}
	
	@Test 
	public void successSeveralArgumentsAppOnLink() {
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		String [] s = {"2", "3", "4"};
		
		ExecuteFileService service = new ExecuteFileService(token0, "/home/root/linkAppSum", s);    
        service.execute();
        
		String [] t = testOut.toString().split("\n");
		
        assertEquals("There should receive the file Name", "sum=9",t[1]);
		System.setOut(standard);
	}
	
//	@Test 
//	public void successSeveralArgumentsPlainFile() {
//		String [] s = new String[0]; 
//		
//        ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appGreet", s);    
//        service.execute();
//        
////        assertNotNull("null final directory", finalDir);
////        assertEquals("error changing directory", result, "Hello ");
//	}
//	
//	@Test 
//	public void successSeveralArgumentsPlainFileOnLink() {
//		String [] s = new String[0]; 
//		
//        ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appGreet", s);    
//        service.execute();
//        
////        assertNotNull("null final directory", finalDir);
////        assertEquals("error changing directory", result, "Hello ");
//	}
//	
	@Test 
	public void successOneArgumentApp() {
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		String [] s = {"a"}; 
		
		ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appGreet", s);    
        service.execute();
        
		String [] t = testOut.toString().split("\n");
		
        assertEquals("There should receive the file Name", "Hello a",t[1]);
		System.setOut(standard);
	}
	
	@Test 
	public void successOneArgumentAppOnLink() {
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		String [] s = {"a"}; 
		
		ExecuteFileService service = new ExecuteFileService(token1, "linkAppGreet", s);    
        service.execute();
        
		String [] t = testOut.toString().split("\n");
		
        assertEquals("There should receive the file Name", "Hello a",t[1]);
		System.setOut(standard);
	}
	
//	@Test 
//	public void successOneArgumentPlainFile() {
//		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(testOut));
//		String [] s = {"a"}; 
//		
//		ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appGreet", s);    
//        service.execute();
//        
//		String [] t = testOut.toString().split("\n");
//		
//        assertEquals("There should receive the file Name", "Hello a",t[1]);
//		System.setOut(standard);
//	}
//	
//	@Test 
//	public void successOneArgumentPlainFileOnLink() {
//		String [] s = new String[0]; 
//		
//        ExecuteFileService service = new ExecuteFileService(token0, "/home/root/appGreet", s);    
//        service.execute();
//        
////        assertNotNull("null final directory", finalDir);
////        assertEquals("error changing directory", result, "Hello ");
//	}

	@Test (expected=InvalidLinkContentException.class)
	public void invalidLink() {
		String [] s = {"a", "b", "c"}; 
		
        ExecuteFileService service = new ExecuteFileService(token0, "invalid", s);    
        service.execute(); 
	}
	
//	@Test (expected=.class)
//	public void invalidPlainFile() {
//		String [] s = {"a", "b", "c"}; 
//		
//        ExecuteFileService service = new ExecuteFileService(token1, "example", s);  
//        service.execute();
//	}
	

//	@Test (expected=.class)
//	public void invalidApp() {
//		String [] s = {"a", "b", "c"}; 
//		
//        ExecuteFileService service = new ExecuteFileService(token0, "badApp", s);    
//        service.execute();
//	}
	
}
