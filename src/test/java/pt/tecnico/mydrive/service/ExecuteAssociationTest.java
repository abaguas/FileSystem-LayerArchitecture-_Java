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



@RunWith(JMockit.class)
public class ExecuteAssociationTest extends AbstractServiceTest {

	private long token0;
	private long token1;
	private long token2;
	private long token3;
	
	private ByteArrayOutputStream testOut = new ByteArrayOutputStream();
	private final PrintStream stdout = System.out;

	
	@Override
	protected void populate() {
		MyDrive md = MyDriveService.getMyDrive();
		User u1 = new User(md, "oscar", "grandepass1", "Cardozo", new Permission(true, true, true, true), new Permission(true, true, true, true));
		User u2 = new User(md, "talisca", "grandepass1", "goleiro", new Permission(true, true, true, true), new Permission(true, true, true, true));
		User u3 = new User(md, "Manel", "jaPercebiPorqueEQueEGrandePasse", "Manulas", new Permission(false, false, false, false), new Permission(false, false, false, false));
		
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
		
		App gedit = new App("gedit", 145, u1, "Executa ficheiros de texto", u1.getMainDirectory()); 
		App adobeReader = new App("adobeReader", 312, u1, "Executa ficheiros pdf", u1.getMainDirectory());
		
		FileExtension fe1 = new FileExtension(u1, "txt", gedit);
		FileExtension fe2 = new FileExtension(u1, "pdf", adobeReader);
		
	}

	
	@Test
    public void successTXT() {
		
		System.setOut(new PrintStream(testOut));
		
        new MockUp<ExecuteAssociationService>() {
        	@Mock
		  	void dispatch() {
		  		System.out.println("Executing example.txt with gedit");
		  	}
		};
		
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "example.txt");
		service.execute();
		assertNotNull("Incorrect execution", testOut.toString());
		assertEquals("Incorrect execution", "Executing example.txt with gedit\n", testOut.toString());

		System.setOut(stdout);
	}

	
	@Test
    public void successPDF() {
		
		System.setOut(new PrintStream(testOut));
		
        new MockUp<ExecuteAssociationService>() {
        	@Mock
		  	void dispatch() {
		  		System.out.println("Executing relatorio.pdf with adobeReader");
		  	}
		};
		
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "relatorio.pdf");
		service.execute();
		assertNotNull("Incorrect execution", testOut.toString());
		assertEquals("Incorrect execution", "Executing relatorio.pdf with adobeReader\n", testOut.toString());

		System.setOut(stdout);
	}
	
	
	@Test
    public void successSharingExtensionTablesBeetweenSessions() {
		
		System.setOut(new PrintStream(testOut));

        new MockUp<ExecuteAssociationService>() {
		  	@Mock
		  	void dispatch() {
		  		System.out.println("Executing example.txt with gedit");
		  	}
		};
		
		ExecuteAssociationService service = new ExecuteAssociationService(token1, "example.txt");
		service.execute();
		assertNotNull("Incorrect execution", testOut.toString());
		assertEquals("Incorrect execution", "Executing example.txt with gedit\n", testOut.toString());

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
		
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "ronaldo.txt");
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
		
		ExecuteAssociationService service = new ExecuteAssociationService(token0, "irina.png");
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
		
		ExecuteAssociationService service = new ExecuteAssociationService(token2, "domingo.txt");
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
		
		ExecuteAssociationService service = new ExecuteAssociationService(1131311, "example.txt");
		service.execute();
	}

	
	@Test(expected = PermissionDeniedException.class)
	public void executeWithoutPermission() {
		
		new MockUp<ExecuteAssociationService>() {
			@Mock
			void execute() {
				throw new PermissionDeniedException("example.txt");	
			}
		};
		
		ExecuteAssociationService service = new ExecuteAssociationService(token3, "example.txt");
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