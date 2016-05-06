package pt.tecnico.mydrive.service;

import java.util.List;
import java.util.ArrayList;


import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.service.dto.FileDto;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Permission;

import static org.junit.Assert.*;

import org.junit.Test;


public class ListDirectoryTest extends AbstractServiceTest{
	private	ArrayList<String> list;
	private	String home_string;
	private	String fatherDir_root;
	private	String selfDirectory_root;
	private	Directory home_father;
	private	long token1;
	private long token2;
		
	protected void populate(){
		MyDrive md = MyDrive.getInstance();
		SessionManager sm = md.getSessionManager();
		Directory rootdir = MyDrive.getInstance().getRootDirectory();
		String name = rootdir.getFatherDirectory().getName();
		rootdir.getFatherDirectory().setName("..");
		fatherDir_root=rootdir.getFatherDirectory().toString();
		rootdir.getFatherDirectory().setName(name);
		name = rootdir.getSelfDirectory().getName();
		rootdir.getSelfDirectory().setName(".");
		selfDirectory_root=rootdir.getFatherDirectory().toString();
		rootdir.getSelfDirectory().setName(name);


		Directory home = (Directory)rootdir.get("home");
		home.setOthersPermission(new Permission("rwx-"));

		User u1 = new User(md,"EusebioSilva","pass1", "Eusebio");
		Directory home_user= new Directory("EusebioSilva", md.generateId(), u1, home);
		name = home_user.getFatherDirectory().getName();
		home_user.getFatherDirectory().setName("..");
		home_father=home_user.getFatherDirectory();
		home_user.getFatherDirectory().setName(name);

		u1.setMainDirectory(home_user);
		PlainFile p1 = new PlainFile("CasoBruma", 124, u1, "conteudo1", home_user);
	    PlainFile p2 = new PlainFile("Exemplo", 125, u1, "conteudo3", home_user);
	    App a1 = new App("Application", 123, u1, "conteudo1", home_user);
	   	Link l1 = new Link("Ligacao", 126, u1, "conteudo1", home_user);
	   	Session s1 = new Session("EusebioSilva","pass1",sm);
	   	s1.setCurrentDir(home_user);
	   	token1 = s1.getToken();
	   	Session s2 = new Session("EusebioSilva","pass1",sm);
	   	token2=s2.getToken();
	   	list = new ArrayList<String>();
	   	s2.setCurrentDir(md.getRootDirectory());
	   	list.add(a1.toString());
	   	list.add(p1.toString());
	   	list.add(p2.toString());
	   	list.add(l1.toString());
	   	u1.getMainDirectory().getSelfDirectory().setName(".");
	   	list.add(u1.getMainDirectory().toString());

	   	home_string = md.getRootDirectory().get("home").toString();

	   	home.setOthersPermission(new Permission("r-x-"));

	}

//	@Test
//	public void PermitedListDirectory() throws NoSuchFileException{
//        ListDirectoryService service = new ListDirectoryService(token1);
//        service.execute();
//        List<FileDto> cs = service.result();
//        
//        for(FileDto f: cs){
//        	System.out.println(f.getDimension());
//        	System.out.println(f.getId());  
//        	System.out.println(f.getName());
//        	System.out.println(f.getOthersPermission()); 
//        	System.out.println(f.getUserPermission());
//        	System.out.println(f.getType());
//        	System.out.println(f.getUsernameOwner()); 
//        	System.out.println(f.getLastChange());
//        	System.out.println("-------------------------------");
//        }
//
//		assertEquals("List with 6 Contacts", 6, cs.size());
//		assertTrue("First File is FatherDir", cs.get(0).getDimension().equals("6"));
//		assertTrue("First File is FatherDir", home_father.getId() == Integer.parseInt(cs.get(0).getId()));
//		assertTrue("First File is FatherDir", cs.get(0).getName().equals("."));
//		assertTrue("First File is FatherDir", cs.get(0).getUserPermission().equals("rwxd"));
//		assertTrue("First File is FatherDir", cs.get(0).getOthersPermission().equals("----"));
//		assertTrue("First File is FatherDir", cs.get(0).getType().equals("Directory"));
//		assertTrue("First File is FatherDir", cs.get(0).getUsernameOwner().equals("EusebioSilva"));
//		assertTrue("First File is FatherDir", cs.get(0).getLastChange()!=null);
//		
//		assertTrue("Second File is SelfDir", cs.get(0).getDimension().equals("4"));
//		assertTrue("Second File is SelfDir", home_father.getId() == Integer.parseInt(cs.get(0).getId()));
//		assertTrue("Second File is SelfDir", cs.get(0).getName().equals("."));
//		assertTrue("Second File is SelfDir", cs.get(0).getUserPermission().equals("rwxd"));
//		assertTrue("Second File is SelfDir", cs.get(0).getOthersPermission().equals("----"));
//		assertTrue("Second File is SelfDir", cs.get(0).getType().equals("Directory"));
//		assertTrue("Second File is SelfDir", cs.get(0).getUsernameOwner().equals("EusebioSilva"));
//		assertTrue("Second File is SelfDir", cs.get(0).getLastChange()!=null);
//		
//		assertEquals("Second File is SelfDir",list.get(4) , cs.get(1));
//		assertEquals("Third File is application", list.get(0), cs.get(2));
//		assertEquals("Fourth File is Caso Bruma", list.get(1), cs.get(3));
//		assertEquals("Fifth File is Exemplo", list.get(2), cs.get(4));
//		assertEquals("Sixth File is ligacao ligacao", list.get(3), cs.get(5));
//
//
//	}
//	@Test
//	public void ListRootDirectory() throws NoSuchFileException{
//		ListDirectoryService service= new ListDirectoryService(token2);
//		service.execute();
//		List<FileDto> cs = service.result();
//		assertEquals("List with 3 Contacts", 3, cs.size());
//		assertEquals("First File is FatherDir", fatherDir_root.toString(), cs.get(0));
//		assertEquals("Second File is SelfDir",selfDirectory_root.toString(), cs.get(1));
//		assertEquals("Third File is Home", home_string, cs.get(2));
//
//	}

}
