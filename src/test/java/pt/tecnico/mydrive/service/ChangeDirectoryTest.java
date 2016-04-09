//package pt.tecnico.mydrive.service;
//
//import pt.tecnico.mydrive.domain.Directory;
//import pt.tecnico.mydrive.domain.MyDrive;
//import pt.tecnico.mydrive.domain.Permission;
//import pt.tecnico.mydrive.domain.PlainFile;
//import pt.tecnico.mydrive.domain.Session;
//import pt.tecnico.mydrive.domain.User;
//
//public class ChangeDirectoryTest extends AbstractServiceTest {
//
//	protected void populate() {
//		
//		MyDrive md = MyDrive.getInstance();
//		
//	}
//
//
//// IMPORTANTE PARA EVITAR OS COMBOIOS ----------------------------------------------------------------
//
//
//	private User getUser(long token) {
//		User u = MyDriveService.getLogin().getSessionByToken(token).getCurrentUser();
//		return u;
//	}
//	
//	private Directory getDirectory(long token) {
//		Directory d = MyDriveService.getLogin().getSessionByToken(token).getCurrentDirectory();
//		return d;
//	}
//
//// ----------------------------------------------------------------------------------------------------
//
//
//    @Test
//    public void successCDRelativePath() {
//        ChangeDirectoryService service = new ChangeDirectoryService(); //token=10
//        service.execute();
//        
//        
//        PlainFile pf = (PlainFile) getDirectory().get("joao-calendario");
//        assertNotNull("plain file was not created", pf);
//        assertEquals("plain file name not correct", "joao-calendario", pf.getName());
//    	//assertEquals("plain file owner not correct", pf.getOwner(), owner);
//    }
//
//    
//    @Test
//    public void successCDAbsolutePath() {
//
//
//    }
//    
//    
//    
//    @Test 
//    public void notPermittedFileCreation() {
//    	 
//    }
//
//
//	@Test 
//    public void duplicatedFileCreation() {
//    	 
//    }
//
//
//	@Test (expected = InvalidFileNameException.class)
//    public void invalidFileNameCreation() {
//    	 CreateFileService service = new CreateFileService("folder1", "", "\0", 10); //token=10
//    }
//
//}
//
//
