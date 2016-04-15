package pt.tecnico.mydrive.service;

import java.util.List;
import java.util.ArrayList;


import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.MyDrive;

import static org.junit.Assert.*;

import org.junit.Test;


public class ListDirectoryTest extends AbstractServiceTest{
		ArrayList<String> list;
		String home_string;
		String fatherDir_root;
		String selfDirectory_root;
		String home_father;
		
	protected void populate(){
		MyDrive md = MyDrive.getInstance();
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
		User u1 = new User("EusebioSilva","pass1", "Eusebio");
		Directory home_user= new Directory("EusebioSilva", md.generateId(), u1, home);
		name = home_user.getFatherDirectory().getName();
		home_user.getFatherDirectory().setName("..");
		home_father=home_user.getFatherDirectory().toString();
		home_user.getFatherDirectory().setName(name);

		u1.setMainDirectory(home_user);
		PlainFile p1 = new PlainFile("CasoBruma", 124, u1, "conteudo1", home_user);
	    PlainFile p2 = new PlainFile("Exemplo", 125, u1, "conteudo3", home_user);
	    App a1 = new App("Application", 123, u1, "conteudo1", home_user);
	   	Link l1 = new Link("Ligacao", 126, u1, "conteudo1", home_user);
	   	Session s1 = new Session(u1,1,md);
	   	s1.setCurrentDir(home_user);
	   	Session s2 = new Session(u1,2,md);
	   	list = new ArrayList<String>();
	   	s2.setCurrentDir(md.getRootDirectory());
	   	list.add(a1.toString());
	   	list.add(p1.toString());
	   	list.add(p2.toString());
	   	list.add(l1.toString());
	   	u1.getMainDirectory().getSelfDirectory().setName(".");
	   	list.add(u1.getMainDirectory().toString());

	   	home_string= md.getRootDirectory().get("home").toString();


	}

	@Test
	public void PermitedListDirectory() throws NoSuchFileException{
		final long token = 1;
        ListDirectoryService service = new ListDirectoryService(token);
        service.execute();
        List<String> cs = service.result();


		assertEquals("List with 6 Contacts", 6, cs.size());
		assertEquals("First File is FatherDir", home_father, cs.get(0));
		assertEquals("Second File is SelfDir",list.get(4) , cs.get(1));
		assertEquals("Third File is application", list.get(0), cs.get(2));
		assertEquals("Fourth File is Caso Bruma", list.get(1), cs.get(3));
		assertEquals("Fifth File is Exemplo", list.get(2), cs.get(4));
		assertEquals("Sixth File is ligacao ligacao", list.get(3), cs.get(5));


	}
	@Test
	public void ListRootDirectory() throws NoSuchFileException{
		final long token = 2;
		ListDirectoryService service= new ListDirectoryService(token);
		service.execute();
		List<String> cs = service.result();
		assertEquals("List with 3 Contacts", 3, cs.size());
		assertEquals("First File is FatherDir", fatherDir_root.toString(), cs.get(0));
		assertEquals("Second File is SelfDir",selfDirectory_root.toString(), cs.get(1));
		assertEquals("Third File is Home", home_string, cs.get(2));

	}



	

}