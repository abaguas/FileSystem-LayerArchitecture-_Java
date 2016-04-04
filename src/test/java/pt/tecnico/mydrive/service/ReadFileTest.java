package pt.tecnico.mydrive.service;

import org.junit.Test;

import pt.tecnico.mydrive.exception.FileIsNotReadAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class ReadFileTest extends AbstractServiceTest{
	protected void populate() {
	    
	}
	    
	/*@Test
	public void readFileWithPermissions() {
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readFileWithoutPermissions() {
	}
	
	@Test(expected = NoSuchFileException.class)
	public void readNonExistingFile() {
	}
	
	@Test(expected = FileIsNotReadAbleException.class)
	public void readDirectoryWithPermission() {
	}
	
	@Test(expected = FileIsNotReadAbleException.class)
	public void readAppWithPermissions() {
	}
	
	@Test(expected = FileIsNotReadAbleException.class)
	public void readLinkWithPermissions() {
	}	*/
}