package pt.tecnico.mydrive.service;

import org.junit.Test;

public class DeleteFileServiceTest extends AbstractServiceTest{
	
	private Directory home1;

	protected void populate() {

	    User u1 = new User("Catio Balde", "pass1", "Catio");
	    User u2 = md.getUserByUsername("root");
		home1 = u1.getMainDirectory();
		Directory home2 = u2.getMainDirectory();
	    
	    Directory d1 = new Directory("folder", 125, u1, home1);    
	    PlainFile p1 = new PlainFile("Caso Bruma", 123, u1, "conteudo1", home1);

	    /*PlainFile p2 = new PlainFile("Exemplo", 124, u2, "conteudo3", home);
	    App a1 = new PlainFile("application", 125, u1, "conteudo1", home);
	    Link l1 = new PlainFile("ligacao", 126, u1, "conteudo1", home);*/
	    
	    Session s1 = new Session(u1, 1);
	    s1.setCurrentDirectory(home1);
	    
	}

	@test
	public void deletePermittedFile(){

		DeleteService dfs = DeleteService("Caso Bruma", 1);
		dfs.execute();

 		assertFalse("user was not removed", home1.hasFile("Caso Bruma"));	
 	}

	@test(expected=PermissionDeniedException.class)
	public void deleteNotPermittedFile(){

		DeleteService dfs = DeleteService("Caso Bruma", 1);
		dfs.execute();

	}

	@test(expected=NoSuchFileException.class)
	public void deleteNonExistingFile(){

	}

	@test
	public void deleteWithNullName(){

	}

	@test
	public void deletePermittedFileAbsolutePath(){

	}

	@test
	public void deleteNotPermittedFileAbsolutePath(){

	}

	@test
	public void deleteNonExistingFileAbsolutePath(){

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

}