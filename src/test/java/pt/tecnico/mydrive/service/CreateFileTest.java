/*
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;


package pt.tecnico.mydrive.service;


import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;



public class CreateFileTest extends AbstractServiceTest {

	protected void populate() {
		
		MyDrive md = MyDrive.getInstance();
		
		Permission perm1 = new Permission(true, true, true, true);
		
		User u0 = md.getUserByUsername("root");
		User u1 = new User("ana", "pass1", "Ana");
		User u2 = new User("maria", "pass2", "Maria");
		
		Directory dir0 = u1.getMainDirectory();
		Directory dir1 = u1.getMainDirectory();
		Directory dir2 = u1.getMainDirectory();
		
		PlainFile pf1 = new PlainFile("ana-calendario", 1, "ana-cal");
	    
		//Session s1 = new Session(u1, md.getLogin());
	    //s1.setCurrentDirectory(home);
	    //s1.setToken(1);
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


    @Test
    public void successFileWithContent() {
        CreateFileService service = new CreateFileService("joao-calendario", "j-cal", "PlainFile", 10); //token=10
        service.execute();
        
        //check plain file was created
        //User owner = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentUser();
        //Directory currentDirectory = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentDirectory;
        
        PlainFile pf = (PlainFile) getDirectory().get("joao-calendario");
        assertNotNull("plain file was not created", pf);
        assertEquals("plain file name not correct", "joao-calendario", pf.getName());
    	//assertEquals("plain file owner not correct", pf.getOwner(), owner);
    }

    
    @Test
    public void successDirectory() {
        CreateFileService service = new CreateFileService("folder1", "", "Dir", 10); //token=10, content é vazio
        service.execute();
        
        //check directory was created
        //User owner = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentUser();
        //Directory currentDirectory = MyDriveService.getMyDrive().getLogin().getSessionByToken(1).getCurrentDirectory;
        
        Directory d = (Directory) getDirectory().get("folder1");
        assertNotNull("directory was not created", d);
        assertEquals("directory name not correct", "folder1", d.getName());
    	//assertEquals("directory owner not correct", d.getOwner(), owner);
        
        //alternativa: assertTrue("directory was not created", currentDirectory.hasFile("folder1")) ; 
    }
    
    
    
    @Test (expected = PermissionDeniedException.class)
    public void notPermittedFileCreation() {
    	 CreateFileService service = new CreateFileService("folder1", "", "Dir", 10); //token=10
    }


	@Test (expected = FileAlreadyExistsException.class)
    public void duplicatedFileCreation() {
    	 CreateFileService service = new CreateFileService("folder1", "", "ana-calendario", 10); //token=10
    }


	@Test (expected = InvalidFileNameException.class)
    public void invalidFileNameCreation() {
    	 CreateFileService service = new CreateFileService("folder1", "", "\0", 10); //token=10
    }

}
*/




/*
PROBLEMAS

- tratar do comboio todo para alcancar o User actual e a Directoria actual
- Permission Denied Exception - queria fazer cd até a uma directoria onde o User não tivesse permissões para criar o ficheiro e o tentasse criar
- verificar que directoria é criada sem content e que o content de um Link é um path


*/
