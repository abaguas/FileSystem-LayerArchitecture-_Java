/*package pt.tecnico.mydrive.service;

import org.junit.Test;

public class DeleteFileServiceTest extends AbstractServiceTest{
	
	private Directory home1;
	private Directory home2;

	protected void populate() {

	    User u1 = new User("Catio Balde", "pass1", "Catio");
	    User u2 = md.getUserByUsername("root");
		home1 = u1.getMainDirectory();
		home2 = u2.getMainDirectory();
		Directory home2 = u2.getMainDirectory();
	    
	    Directory d1 = new Directory("folder", 125, u1, home1);
	    Directory d2 = new Directory("folder2", 126, u2, home2);        
	    PlainFile p1 = new PlainFile("Caso Bruma", 123, u1, "conteudo1", home1);
		PlainFile p2 = new PlainFile("Exemplo", 124, u2, "conteudo3", home2);
	    
	    Session s1 = new Session(u1, 1);
	    s1.setCurrentDirectory(home1);

	    Session s2 = new Session(u1, 2);
	    s2.setCurrentDirectory(home2);
	    
	}

	@test
	public void deletePermittedFile(){

		DeleteService dfs = DeleteService("Caso Bruma", 1);
		dfs.execute();

 		assertFalse("file was not removed", home1.hasFile("Caso Bruma"));	
 	}

	@test(expected=PermissionDeniedException.class)
	public void deleteNotPermittedFile(){

		DeleteService dfs = DeleteService("Exemplo", 2);
		dfs.execute();

	}

	@test(expected=NoSuchFileException.class)
	public void deleteNonExistingFile(){

		DeleteService dfs = DeleteService("naoexisto", 1);
		dfs.execute();

	}

	@test(expected=InvalidFileName.class)
	public void deleteWithNullName(){

		DeleteService dfs = DeleteService(null, 1);
		dfs.execute();

	}

	@test
	public void deletePermittedFileAbsolutePath(){

		DeleteService dfs = DeleteService("/home/Catio Balde/Caso Bruma", 1);
		dfs.execute();
	
		assertFalse("file was not removed", home1.hasFile("Caso Bruma"));	
	
	}

	@test(expected=PermissionDeniedException.class)
	public void deleteNotPermittedFileAbsolutePath(){

		DeleteService dfs = DeleteService("/home/root/Exemplo", 2);
		dfs.execute();

	}

	@test(expected=InvalidFileName.class)
	public void deleteNonExistingFileAbsolutePath(){

		DeleteService dfs = DeleteService("/home/root/naoexisto", 2);
		dfs.execute();

	}

	@test
	public void deletePermittedDirectory(){

	}

	@test
	public void deleteNotPermittedDirectory(){

	}

	@test
	public void deleteNonExistingDirectory(){

	}

	@test
	public void deletePermittedDirectoryAbsolutePath(){

	}

	@test
	public void deleteNotPermittedDirectoryAbsolutePath(){

	}

	@test
	public void deleteNonExistingDirectoryAbsolutePath(){

	}

	@test
	public void deleteFileSystemRoot(){

	}

	@test
	public void deleteSpecialDotDirectory(){

	}

	@test
	public void deleteSpecialDoubleDotDirectory(){

	}

	@test
	public void deleteUserHomeDirectory(){

	}

	@test
	public void deleteUserHomeDirectoryAbsolutePath(){
		
	}

}*/