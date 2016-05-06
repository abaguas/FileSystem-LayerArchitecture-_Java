package pt.tecnico.mydrive.service;

import org.junit.Test;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;




public class AddVariableTest extends AbstractServiceTest {

	@Override
	protected void populate() {
		
		MyDrive md = MyDrive.getInstance();
		
		User u0 = md.getRootUser();
		User u1 = new User("ana", "pass1", "Ana");
		md.addUsers(u1);
		User u2 = new User("maria", "pass2", "Maria");
		md.addUsers(u2);
		User u3 = new User("filipa", "pass3", "Filipa");
		md.addUsers(u3);
		
		Directory rootdir = md.getRootDirectory();
		Directory home = (Directory)rootdir.get("home");
	
		Directory dir0 = u0.getMainDirectory();
		Directory dir1 = new Directory("ana", 10, u1, home); //id=10
		Directory dir2 = new Directory("maria", 20, u2, home); //id=20
		Directory dir3 = new Directory("filipa", 30, u3, home); //id=30
		u1.setMainDirectory(dir1);
		u2.setMainDirectory(dir2);
		u3.setMainDirectory(dir3);		
		
		
		//Session s0 = u0.getSession();
		Session s0 = new Session(u0, 0, md); //acho que a sessão do User é sempre criada (??)
		s0.setCurrentDir(dir0);
	    
		Session s1 = new Session(u1, 1, md); // ana - token=1
	    s1.setCurrentDir(dir1);
	    
	    Session s2 = new Session(u2, 2, md); // maria - token=2
	    s2.setCurrentDir(dir2);
	    
	    Session s3 = new Session(u3, 3, md); // filipa - token=3
	    s3.setCurrentDir(dir3);
		
	
	    
	}

	
	// SHORTCUTS
	
	private User getUser(long token) {
		User u = MyDriveService.getMyDrive().getCurrentUserByToken(token);
		return u;
	}
	
	private Directory getDirectory(long token) {
		Directory d = MyDriveService.getMyDrive().getCurrentDirByToken(token);
		return d;
	}

	
		
	//TESTS
	
	public void successAddVariable() {
		AddVariableService service = new AddVariableService(1, "var1", "ola");
		service.execute();
		
		
		
	}
	
	
	public void successUpdateVariable() {
		AddVariableService service = new AddVariableService(1, "varUpdate", "update");
		service.execute();
		//FIXME criar esta chave no populate() varUpdate e verificar
		
	}
	
	
	
	
	
}
