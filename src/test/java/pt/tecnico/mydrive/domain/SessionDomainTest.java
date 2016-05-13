package pt.tecnico.mydrive.domain;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.joda.time.DateTime;


import pt.tecnico.mydrive.service.AbstractServiceTest;
import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.FileExtension;
import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.Permission;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.GuestUser;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.RootUser;



import pt.tecnico.mydrive.exception.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.*;

public class SessionDomainTest extends AbstractServiceTest {
	MyDrive md;
	User u1;
	User h1;
	SessionManager sm;
	GuestUser guest;
	RootUser root;

	@Override
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		sm = md.getSessionManager();
		User u1 = new User(md, "oscar", "grandepass1", "Cardozo", new Permission(true, true, true, true), new Permission(true, true, true, true));
		User h1= new User(md, "Hacker", "grandepass1", "Pequeno", new Permission(true, true, true, true), new Permission(true, true, true, true));
		guest = GuestUser.getInstance();
		root=RootUser.getInstance();

	}

	@Test
	public void success(){
		Session s = new Session("oscar", "grandepass1", sm);
		assertNotNull(s);
		assertEquals("oscar", s.getUsername());
	}

	@Test
	public void success1(){
		Session s = new Session("oscar", "grandepass1", sm);
		
	}

	@Test
	public void checkExpirationGuest(){
		Session s = new Session("nobody", "", sm);
		boolean v = s.expiration("nobody");
		assertEquals(false, v);

	}

	@Test
	public void checkExpirationRoot(){
		Session s = new Session("root", "***", sm);
		boolean v = s.expiration("root");
		assertEquals(false, v);

	}

	@Test(expected = NoSuchUserException.class)
	public void fail1(){
		Session s = new Session("Maia", "grandepass1", sm);
	}

	@Test(expected = InvalidPasswordException.class)
	public void fail2(){
		Session s = new Session("oscar", "grandepass2", sm);
	}

	@Test(expected = InvalidOperationException.class)
	public void fail3(){
		Session s = new Session("oscar", "grandepass1", sm);
		s.setToken(21312);
	}

	@Test(expected = InvalidOperationException.class)
	public void fail4(){
		Session s = new Session("oscar", "grandepass1", sm);
		s.setTimestamp(new DateTime());
	}


	@Test(expected = InvalidOperationException.class)
	public void fail5(){
		Session s = new Session("oscar", "grandepass1", sm);
		s.setUser(h1);
	}

}