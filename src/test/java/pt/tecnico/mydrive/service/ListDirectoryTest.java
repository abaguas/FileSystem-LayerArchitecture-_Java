package pt.tecnico.mydrive.service;

import java.util.List;
import java.util.Random;
import java.math.BigInteger;
import java.util.ArrayList;

import pt.tecnico.mydrive.exception.InvalidTokenException;
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

//FIXME faltam testes para a permissao de leitura
//FIXME falta teste invalid token

public class ListDirectoryTest extends AbstractServiceTest{
	private	long token1;
	private long token2;
		
	protected void populate(){
		MyDrive md = MyDriveService.getMyDrive();
		SessionManager sm = md.getSessionManager();
		Directory rootdir = MyDrive.getInstance().getRootDirectory();

		Directory home = (Directory)rootdir.get("home");
		home.setOthersPermission(new Permission("rwx-"));

		User u1 = new User(md,"EusebioSilva","grandepass1", "Eusebio");
		Directory home_user= u1.getMainDirectory();

		new PlainFile("CasoBruma", 124, u1, "conteudo1", home_user);
	    new PlainFile("Exemplo", 125, u1, "conteudo3", home_user);
	    new App("Application", 123, u1, "conteudo1", home_user);
	   	new Link("Ligacao", 126, u1, "conteudo1", home_user);
	   	
	   	Session s1 = new Session("EusebioSilva", "grandepass1", sm);
	   	s1.setCurrentDir(home_user);
	   	token1 = s1.getToken();
	   	
	   	Session s2 = new Session("EusebioSilva", "grandepass1", sm);
	   	token2=s2.getToken();
	   	s2.setCurrentDir(md.getRootDirectory());
	   	
	   	home.setOthersPermission(new Permission("r-xd"));
	}

	@Test
	public void PermitedListDirectory() throws NoSuchFileException{
        ListDirectoryService service = new ListDirectoryService(token1);
        service.execute();
        List<FileDto> cs = service.result();

		assertEquals("List has not 6 Contacts", 6, cs.size());
		
		assertTrue("Own File has not dimension = 6", cs.get(0).getDimension().equals("6"));
		assertTrue("Own File has not Id = 3", cs.get(0).getId().equals("3"));
		assertTrue("Own File has not name .", cs.get(0).getName().equals("."));
		assertTrue("Own File has not owner permission: rwxd", cs.get(0).getUserPermission().equals("rwxd"));
		assertTrue("Own File has not other permission: ----", cs.get(0).getOthersPermission().equals("----"));
		assertTrue("Own File has not type: Directory", cs.get(0).getType().equals("Directory"));
		assertTrue("Own File has not owner: EusebioSilva", cs.get(0).getUsernameOwner().equals("EusebioSilva"));
		assertNotNull("Own File has not Date", cs.get(0).getLastChange());
		
		assertTrue("Father File has not dimension: 4", cs.get(1).getDimension().equals("4"));
		assertTrue("Father File has not Id: 1", cs.get(1).getId().equals("1"));
		assertTrue("Father File has not name: ..", cs.get(1).getName().equals(".."));
		assertTrue("Father File has not owner permission: rwxd", cs.get(1).getUserPermission().equals("rwxd"));
		assertTrue("Father File has not other permission: r-xd", cs.get(1).getOthersPermission().equals("r-xd"));
		assertTrue("Father File has not type: Directory", cs.get(1).getType().equals("Directory"));
		assertTrue("Father File has not owner: root", cs.get(1).getUsernameOwner().equals("root"));
		assertNotNull("Father File has not Date", cs.get(1).getLastChange());
		
		assertTrue("Application has not dimension: 9", cs.get(2).getDimension().equals("9"));
		assertTrue("Application has not Id: 123", cs.get(2).getId().equals("123"));
		assertTrue("Application has not name: Application", cs.get(2).getName().equals("Application"));
		assertTrue("Application has not owner permission: rwxd", cs.get(2).getUserPermission().equals("rwxd"));
		assertTrue("Application has not other permission: ----", cs.get(2).getOthersPermission().equals("----"));
		assertTrue("Application has not type: App", cs.get(2).getType().equals("App"));
		assertTrue("Application has not owner: EusebioSilva", cs.get(2).getUsernameOwner().equals("EusebioSilva"));
		assertNotNull("Application has not Date", cs.get(2).getLastChange());
		
		assertTrue("CasoBruma has not dimension: 9", cs.get(3).getDimension().equals("9"));
		assertTrue("CasoBruma has not Id: 124", cs.get(3).getId().equals("124"));
		assertTrue("CasoBruma has not name: CasoBruma", cs.get(3).getName().equals("CasoBruma"));
		assertTrue("CasoBruma has not owner permission: rwxd", cs.get(3).getUserPermission().equals("rwxd"));
		assertTrue("CasoBruma has not other permission: ----", cs.get(3).getOthersPermission().equals("----"));
		assertTrue("CasoBruma has not type: PlainFile", cs.get(3).getType().equals("PlainFile"));
		assertTrue("CasoBruma has not owner: EusebioSilva", cs.get(3).getUsernameOwner().equals("EusebioSilva"));
		assertNotNull("CasoBruma has not Date", cs.get(3).getLastChange());
		
		assertTrue("Exemplo File has not dimension: 9", cs.get(4).getDimension().equals("9"));
		assertTrue("Exemplo File has not Id: 125", cs.get(4).getId().equals("125"));
		assertTrue("Exemplo File has not name: Exemplo", cs.get(4).getName().equals("Exemplo"));
		assertTrue("Exemplo File has not owner permission: rwxd", cs.get(4).getUserPermission().equals("rwxd"));
		assertTrue("Exemplo File has not other permission: ----", cs.get(4).getOthersPermission().equals("----"));
		assertTrue("Exemplo File has not type: PlainFile", cs.get(4).getType().equals("PlainFile"));
		assertTrue("Exemplo File has not owner: EusebioSilva", cs.get(4).getUsernameOwner().equals("EusebioSilva"));
		assertNotNull("Exemplo File has not Date", cs.get(4).getLastChange());
		
		assertTrue("Ligacao has not dimension: 9", cs.get(5).getDimension().equals("9"));
		assertTrue("Ligacao has not Id: 126", cs.get(5).getId().equals("126"));
		assertTrue("Ligacao has not name: Ligacao->conteudo1", cs.get(5).getName().equals("Ligacao->conteudo1"));
		assertTrue("Ligacao has not owner permission: rwxd", cs.get(5).getUserPermission().equals("rwxd"));
		assertTrue("Ligacao has not other permission: ----", cs.get(5).getOthersPermission().equals("----"));
		assertTrue("Ligacao has not type: Link", cs.get(5).getType().equals("Link"));
		assertTrue("Ligacao has not owner: EusebioSilva", cs.get(5).getUsernameOwner().equals("EusebioSilva"));
		assertNotNull("Ligacao has not Date", cs.get(5).getLastChange());


	}
	@Test
	public void ListRootDirectory() throws NoSuchFileException{
		ListDirectoryService service= new ListDirectoryService(token2);
		service.execute();
		List<FileDto> cs = service.result();
		
		assertEquals("List has not 3 Contacts", 3, cs.size());
		
		assertTrue("Own File has not dimension = 3", cs.get(0).getDimension().equals("3"));
		assertTrue("Own File has not Id = 0", cs.get(0).getId().equals("0"));
		assertTrue("Own File has not name .", cs.get(0).getName().equals("."));
		assertTrue("Own File has not owner permission: rwxd", cs.get(0).getUserPermission().equals("rwxd"));
		assertTrue("Own File has not other permission: r-x-", cs.get(0).getOthersPermission().equals("r-x-"));
		assertTrue("Own File has not type: Directory", cs.get(0).getType().equals("Directory"));
		assertTrue("Own File has not owner: root", cs.get(0).getUsernameOwner().equals("root"));
		assertNotNull("Own File has not Date", cs.get(0).getLastChange());
		
		assertTrue("Father File has not dimension: 3", cs.get(1).getDimension().equals("3"));
		assertTrue("Father File has not Id: 0", cs.get(1).getId().equals("0"));
		assertTrue("Father File has not name: ..", cs.get(1).getName().equals(".."));
		assertTrue("Father File has not owner permission: rwxd", cs.get(1).getUserPermission().equals("rwxd"));
		assertTrue("Father File has not other permission: r-x-", cs.get(1).getOthersPermission().equals("r-x-"));
		assertTrue("Father File has not type: Directory", cs.get(1).getType().equals("Directory"));
		assertTrue("Father File has not owner: root", cs.get(1).getUsernameOwner().equals("root"));
		assertNotNull("Father File has not Date", cs.get(1).getLastChange());
		
		assertTrue("home has not dimension: 4", cs.get(2).getDimension().equals("4"));
		assertTrue("home has not Id: 1", cs.get(2).getId().equals("1"));
		assertTrue("home has not name: home", cs.get(2).getName().equals("home"));
		assertTrue("home has not owner permission: rwxd", cs.get(2).getUserPermission().equals("rwxd"));
		assertTrue("home has not other permission: r-xd", cs.get(2).getOthersPermission().equals("r-xd"));
		assertTrue("home has not type: Directory", cs.get(2).getType().equals("Directory"));
		assertTrue("home has not owner: root", cs.get(2).getUsernameOwner().equals("root"));
		assertNotNull("home has not Date", cs.get(2).getLastChange());
		
	}
	
	@Test (expected = InvalidTokenException.class)
    public void invalidToken() { //Testing CD with an invalid token
		long token = new BigInteger(64, new Random()).longValue();
		while (token == token1 || token == token2){
			token = new BigInteger(64, new Random()).longValue();
		}
    	    	
    	ListDirectoryService service = new ListDirectoryService(token);    
        service.execute();
    }

}
