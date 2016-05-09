package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;

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
import pt.tecnico.mydrive.exception.InvalidPathException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class ExecuteFileTest extends AbstractServiceTest{
	private long token;
	
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		SessionManager sm = md.getSessionManager();
		
		Directory home = (Directory) md.getRootDirectory().get("home");
		home.setOthersPermission(new Permission("rwx-"));
		
		home.setOthersPermission(new Permission("r-x-"));
	}
	    
//	
//	@Test (expected=InvalidTokenException.class)
//	public void plainFileWithInvalidToken() {
//	  
//	}
//	
//	@Test (expected=NoSuchFileException.class)
//	public void nonExistentPlainFile() {
//	    
//	}
//	
//	@Test (expected=InvalidPathException.class)
//	public void invalidPath() {
//	    
//	}
//	
//	@Test (expected=PermissionDeniedException.class)
//	public void nonPermittedPlainFile() {
//	    
//	}
//	
//	@Test (expected=PermissionDeniedException.class)
//	public void nonPermittedDirectory() {
//	    
//	}
//	
//	@Test (expected=FileIsNotExecuteAbleException.class)
//	public void notExecutableFile() {
//	    
//	}
//	
//	@Test 
//	public void noArguments() {
//	    
//	}
//	
//	@Test
//	public void severalArguments() {
//	    
//	}
//	
//	@Test
//	public void oneArgument() {
//	    
//	}
//	
//	@Test (expected=InvalidLinkContentException.class)
//	public void invalidLink() {
//	    
//	}
//	
//	@Test (expected=InvalidLinkContentException.class)
//	public void invalidPlainFile() {
//	    
//	}
//	
//	@Test
//	public void validLink() {
//	    
//	}
//	
//	@Test
//	public void validPlainFile() {
//	    
//	}
}
