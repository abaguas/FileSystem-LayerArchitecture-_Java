package pt.tecnico.mydrive.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.FileExtension;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;


import pt.tecnico.mydrive.exception.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.*;



@RunWith(JMockit.class)
public class ExecuteAssociationTest extends AbstractServiceTest {

	private long token0;
	private long token1;
	private long token2;
	private long token3;
	MyDrive md;
	private User u1;
	private User u2;
	private User u3;
	private App gedit;
	private App adobeReader;
	private App helloAPP;
	
	private ByteArrayOutputStream baos;
	private final PrintStream stdout = System.out;

	
	@Override
	protected void populate() {
		md = MyDriveService.getMyDrive();
		u1 = new User(md, "oscar", "grandepass1", "Cardozo", new Permission(true, true, true, true), new Permission(true, true, true, true));
		u2 = new User(md, "talisca", "grandepass1", "goleiro", new Permission(true, true, true, true), new Permission(true, true, true, true));
		u3 = new User(md, "Manel", "jaPercebiPorqueEQueEGrandePasse", "Manulas", new Permission(false, false, false, false), new Permission(false, false, false, false));
		
		Directory rootdir = md.getRootDirectory();
		Directory home = (Directory)rootdir.get("home");
		
		Session s = new Session("oscar","grandepass1",md.getSessionManager());
		token0 = s.getToken();
		Session s1 = new Session("oscar","grandepass1",md.getSessionManager());
		token1 = s1.getToken();
		Session s2 = new Session("talisca","grandepass1",md.getSessionManager());
		token2 = s2.getToken();
		s2.setCurrentDir(u1.getMainDirectory());
		Session s3 = new Session("Manel", "jaPercebiPorqueEQueEGrandePasse", md.getSessionManager());
		token3 = s3.getToken();
		s3.setCurrentDir(u1.getMainDirectory());
		
		PlainFile p1 = new PlainFile("example.txt", 144, u1, "tacuara", u1.getMainDirectory());
		Permission valeTudo = new Permission(true, true, true, true);
		p1.setOthersPermission(valeTudo);
		p1.setOthersPermission(valeTudo);
		PlainFile p2 = new PlainFile("irina.png", 146, u1, "censored", u1.getMainDirectory());
		PlainFile p3 = new PlainFile("relatorio.pdf", 253, u1, "querias", u1.getMainDirectory());
		PlainFile p4 = new PlainFile("domingo.txt", 412, u2, "35", u1.getMainDirectory());
		PlainFile p5 = new PlainFile("tryHard.ext", 837, u1, "doesNotMatter", u1.getMainDirectory());
		
		gedit = new App("gedit", 145, u1, "pt.tecnico.mydrive.presentation.Hello", u1.getMainDirectory()); 
		adobeReader = new App("adobeReader", 312, u1, "pt.tecnico.mydrive.presentation.Hello.sum", u1.getMainDirectory());
		helloAPP = new App("helloAPP", 948, u1, "pt.tecnico.mydrive.presentation.Hello.greet", u1.getMainDirectory());
		
		FileExtension fe1 = new FileExtension(u1, "txt", gedit);
		FileExtension fe2 = new FileExtension(u1, "pdf", adobeReader);
		FileExtension fe3 = new FileExtension(u1, "ext", helloAPP);
		
		baos  = new ByteArrayOutputStream();
	}

	
	
	@Test
    public void successTXT() {
		
		System.setOut(new PrintStream(baos));
		
        new MockUp<ExecuteAssociationService>() {
        	@Mock
		  	void execute() {
		  		String[] args = {};
        		gedit.execute(u1, args, md);
		  	}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "example.txt", args);
		service.execute();
		assertNotNull("Incorrect execution", baos.toString());
		assertEquals("Incorrect execution", "Hello myDrive!\n", baos.toString());

		System.setOut(stdout);
	}

	
	@Test
    public void successPDF() {
		
		System.setOut(new PrintStream(baos));
		
        new MockUp<ExecuteAssociationService>() {
        	@Mock
		  	void execute() {
		  		String[] args = {"1", "2", "3"};
        		adobeReader.execute(u1, args, md);
		  	}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "relatorio.pdf", args);
		service.execute();
		assertNotNull("Incorrect execution", baos.toString());
		assertEquals("Incorrect execution", "sum=6\n", baos.toString());

		System.setOut(stdout);
	}
	
	
	@Test
    public void successEXT() {
		
		System.setOut(new PrintStream(baos));
		
        new MockUp<ExecuteAssociationService>() {
        	@Mock
		  	void execute() {
        		String[] args = {"joao"};
        		helloAPP.execute(u1, args, md);
		  	}
		};
		
		String[] args = {"joao"};
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "tryHard.ext", args);
		service.execute();
		assertNotNull("Incorrect execution", baos.toString());
		assertEquals("Incorrect execution", "Hello joao\n", baos.toString());
		
		System.setOut(stdout);
	}
	
	
	@Test
    public void successSharingExtensionTablesBeetweenSessions() {
		
		System.setOut(new PrintStream(baos));

        new MockUp<ExecuteAssociationService>() {
		  	@Mock
		  	void execute() {
		  		String[] args = {};
        		gedit.execute(u1, args, md);
		  	}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(token1, "example.txt", args);
		service.execute();
		assertNotNull("Incorrect execution", baos.toString());
		assertEquals("Incorrect execution", "Hello myDrive!\n", baos.toString());

		System.setOut(stdout);
	}

	
	@Test(expected = NoSuchFileException.class)
    public void failFileNotExists() {
        new MockUp<ExecuteAssociationService>() {
		  	@Mock
		  	void dispatch() throws NoSuchFileException{ 
		  		throw new NoSuchFileException("ronaldo.txt");
		  	}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "ronaldo.txt", args);
		service.execute();
	}

	
	@Test(expected = ExtensionNotFoundException.class)
    public void failAppNotExists() {
        new MockUp<ExecuteAssociationService>() {
		  	@Mock
		  	void dispatch() throws ExtensionNotFoundException{ 
		  		throw new ExtensionNotFoundException("png");
		  	}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "irina.png", args);
		service.execute();
	}

	
	@Test(expected = ExtensionNotFoundException.class)
    public void failSharingExtensionTablesBeetweenUsers() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
		  	void dispatch() throws ExtensionNotFoundException { 
		  		throw new ExtensionNotFoundException("txt");
		  	}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(token2, "domingo.txt", args);
		service.execute();
	}
	
	
	@Test(expected = InvalidTokenException.class)
    public void failInvalidToken() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
		  	void dispatch() throws InvalidTokenException{ 
		  		throw new InvalidTokenException();
		  	}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(1131311, "example.txt", args);
		service.execute();
	}

	
	@Test(expected = PermissionDeniedException.class)
	public void executeWithoutPermission() {
		
		new MockUp<ExecuteAssociationService>() {
			@Mock
			void dispatch() {
				throw new PermissionDeniedException("example.txt");	
			}
		};
		
		String[] args = {};
		ExecuteAssociationService service = new ExecuteAssociationService(token3, "example.txt", args);
		service.execute();	
	}
	
	
}




/*


ISTO É O QUE TINHAS, FILIPE


package pt.tecnico.mydrive.service;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import pt.tecnico.mydrive.exception.*;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.FileExtension;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;

@RunWith(JMockit.class)
public class ExecuteAssociationTest extends AbstractServiceTest {

	private long token0;
	private long token1;
	private long token2;

	private ExecuteAssociationService service;

	@Override
	protected void populate() {
		MyDrive md = MyDriveService.getMyDrive();
		User u1 = new User(md, "oscar", "grandepass1", "Cardozo", new Permission(true, true, true, true), new Permission(true, true, true, true));
		User u2 = new User(md, "talisca", "grandepass1", "goleiro", new Permission(true, true, true, true), new Permission(true, true, true, true));
		Directory rootdir = md.getRootDirectory();
		Directory home = (Directory)rootdir.get("home");
		Session s = new Session("oscar","grandepass1",md.getSessionManager());
		Session s1 = new Session("oscar","grandepass1",md.getSessionManager());
		token0= s.getToken();
		token1=s1.getToken();
		Session s2 = new Session("talisca","grandepass1",md.getSessionManager());
		token2= s2.getToken();
		s2.setCurrentDir(u1.getMainDirectory());
		PlainFile p1 = new PlainFile("example.txt", 144, u1, "tacuara", u1.getMainDirectory());
		PlainFile p2 = new PlainFile("irina.png", 146, u1, "censored", u1.getMainDirectory());
		App txt= new App("txt", 145, u1, "Executa ficheiros de texto", u1.getMainDirectory()); 
		new FileExtension(u1, "txt", txt);
	}

	@Test
    public void success() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
	  	void dispatch() { }
		};
		new ExecuteAssociationService(token0,"example.txt").execute();
	}

	@Test
    public void successSharingExtensionTablesBeetweenSessions() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
	  	void dispatch() { }
		};
		new ExecuteAssociationService(token1,"example.txt").execute();
	}

	@Test(expected = NoSuchFileException.class)
    public void failFileNotExists() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
	  	void dispatch() throws NoSuchFileException{ 
	  		throw new NoSuchFileException("ronaldo");
	  		}
		};
		new ExecuteAssociationService(token0,"roanldo.txt").execute();
	}

	@Test(expected = ExtensionNotFoundException.class)
    public void failAppNotExists() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
	  	void dispatch() throws ExtensionNotFoundException{ 
	  		throw new ExtensionNotFoundException("png");
	  		}
		};
		new ExecuteAssociationService(token0,"irina.png").execute();
	}

	@Test(expected = InvalidTokenException.class)
    public void failInvalidToken() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
	  	void dispatch() throws InvalidTokenException{ 
	  		throw new InvalidTokenException();
	  		}
		};
		new ExecuteAssociationService(1131311,"example.txt").execute();
	}

	@Test(expected = ExtensionNotFoundException.class)
    public void failSharingExtensionTablesBeetweenUsers() {
        new MockUp<ExecuteAssociationService>() {
	  	@Mock
	  	void dispatch() throws ExtensionNotFoundException{ 
	  		throw new ExtensionNotFoundException("txt");
	  		}
		};
		new ExecuteAssociationService(token2,"example.txt").execute();
	}



}

*/