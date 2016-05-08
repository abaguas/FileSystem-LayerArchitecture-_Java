package pt.tecnico.mydrive.service;

import org.junit.Test;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.InvalidTokenException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;


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
//		u1.setMainDirectory(dir1);
//		u2.setMainDirectory(dir2);
//		u3.setMainDirectory(dir3);		
		
		
		//Session s0 = u0.getSession();//acho que a sessão do User é sempre criada (??)
		Session s0 = new Session(u0, 0, md); // root - token = 0
		s0.setCurrentDir(dir0);
	    
		Session s1 = new Session(u1, 1, md); // ana - token=1
	    s1.setCurrentDir(dir1);
	    
	    Session s2 = new Session(u2, 2, md); // maria - token=2
	    s2.setCurrentDir(dir2);
	    
	    Session s3 = new Session(u3, 3, md); // filipa - token=3
	    s3.setCurrentDir(dir3);
			
	    //FIXME - CRIAR AQUI VARIAVEIS DE AMBIENTE
	    
//	    EnvironmentVariable v1 = new EnvironmentVariable("v1", "value1");
//	    s1.addEnvironmentVariable(v1);
//	    s2.addEnvironmentVariable(v1);
//	    
//	    EnvironmentVariable v2 = new EnvironmentVariable("v2", "value2");
//	    s2.addEnvironmentVariable(v2);
	    
	    
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

	
//	private Session getSession(long token) {
//		Session s = MyDriveService.getMyDrive().getSessionByToken(token);
//		return s;
//	}
	
		
	//TESTS
	
//	public void successAddFirstVariable() {
//		AddVariableService service = new AddVariableService(3, "newVar", "newValue");
//		service.execute();
//		List<EnvironmentVariable> varList = service.result();
//		//FIXME criar esta chave no populate() varUpdate e verificar
//		
//        assertNotNull("list of environment variables was not created", varList);
//        assertEquals("incorrect number of environment variables", 1, varList.size());
//        assertEquals("incorrect variable name", "newVar", varList.get(0).getName());
//        assertEquals("incorrect variable value", "newValue", varList.get(0).getValue());
//	}
//	
//	
//	public void successAddNewVariable() {
//		AddVariableService service = new AddVariableService(1, "home", "/home/ana");
//		service.execute();
//		List<EnvironmentVariable> varList = service.result();
//		//FIXME criar esta chave no populate() varUpdate e verificar
//		
//        assertNotNull("list of environment variables was not created", varList);
//        assertEquals("incorrect number of environment variables", 2, varList.size());
//        assertEquals("incorrect variable name", "home", varList.get(0).getName());
//        assertEquals("incorrect variable value", "/home/ana", varList.get(0).getValue());
//        assertEquals("incorrect variable name", "v1", varList.get(1).getName());
//        assertEquals("incorrect variable value", "value1", varList.get(1).getValue());
//	}
//
//	
//	public void successUpdateVariable() {
//		AddVariableService service = new AddVariableService(2, "v2", "value2-new");
//		service.execute();
//		List<EnvironmentVariable> varList = service.result();
//		//FIXME criar esta chave no populate() varUpdate e verificar
//		
//        assertNotNull("list of environment variables was not created", varList);
//        assertEquals("incorrect number of environment variables", 2, varList.size());
//        assertEquals("incorrect variable name", "v1", varList.get(0).getName());
//        assertEquals("incorrect variable value", "value1", varList.get(0).getValue());
//        assertEquals("incorrect variable name", "v2", varList.get(1).getName());
//        assertEquals("incorrect variable value", "value2-new", varList.get(1).getValue());
//	}
//	
//	
//	@Test (expected = InvalidTokenException.class)
//    public void invalidToken() { 
//		final long token = 1234567890;
//		AddVariableService service = new AddVariableService(token, "impossibleName", "impossibleValue");
//		service.execute();
//		List<EnvironmentVariable> varList = service.result();
//		//FIXME criar esta chave no populate() varUpdate e verifica
//    }
	

	
}
