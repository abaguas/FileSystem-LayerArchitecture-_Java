//package pt.tecnico.mydrive.service;
//
//public class WriteFileTest extends AbstractServiceTest{
//	protected void populate() {
//
//	    User u1 = new User("Catio Balde", "pass1", "Catio");
//	    User u2 = md.getUserByUsername("root");
//	    Directory home = u2.getMainDirectory();
//	    Directory d1 = new Directory("folder", 125, u1, home);    
//	    
//	    PlainFile p1 = new PlainFile("Caso Bruma", 123, u1, "conteudo1", home);
//	    PlainFile p2 = new PlainFile("Exemplo", 124, u2, "conteudo3", home);
//	    App a1 = new PlainFile("application", 125, u1, "conteudo1", home);
//	    Link l1 = new PlainFile("ligacao", 126, u1, "conteudo1", home);
//	    
//	    Session s1 = new Session(u1, md.getLogin());
//	    s1.setCurrentDirectory(home);
//	    s1.setToken(1);
//	    
//	}
//	    
//	
//	@Test
//	public void successPermittedFile() {
//	    PlainFile file;
//	    MyDrive md = MyDrive.getInstance();
//	    for(File f: md.getUserByUsername("root").getMainDirectory().getFiles()){
//	        if(f.getId() == 123){
//	            file = f;
//	        }
//	    }
//	    WriteFileService wfs = new WriteFileService(Caso Bruma", "abc", 1); // token = 1
//	    wfs.execute();
//	    assertEquals("Wrong Content", "abc", file.getContent());
//
//	}
//
//
//	@Test(expected = PermissionDeniedException.class)
//	public void notPermittedFile() {
//	    WriteFileService wfs = new WriteFileService("Exemplo", "abc", 1); // token = 1
//	    wfs.execute();
//	}
//
//	@Test(expected = NoSuchFileException.class)
//	public void noSuchFile() {
//	    WriteFileService wfs = new WriteFileService("blabla", "abc", 1); // token = 1
//	    wfs.execute();
//	}
//
//	@Test(expected = FileIsDirectoryException.class)
//	public void writeOnDirectory() {
//	    WriteFileService wfs = new WriteFileService("folder", "abc", 1); // token = 1
//	    wfs.execute();
//	}
//
//
//	@Test(expected = FileIsAppException.class)
//	public void writeOnApp() {
//	    WriteFileService wfs = new WriteFileService("application", "abc", 1); // token = 1
//	    wfs.execute();
//	}
//
//	@Test(expected = FileIsLinkException.class)
//	public void writeOnLink() {
//	    WriteFileService wfs = new WriteFileService("ligacao", "abc", 1); // token = 1
//	    wfs.execute();
//	}

//}