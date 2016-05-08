//package pt.tecnico.mydrive.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import pt.tecnico.mydrive.domain.MyDrive;
//import pt.tecnico.mydrive.domain.Session;
//
//public class AddVariableService extends MyDriveService {
//
//	private long token;
//	private String name;
//	private String value;
//	private List<EnvironmentVariable> createdVariables;
//	
//	
//	public AddVariableService(long token, String name, String value) {
//		this.token = token;
//		this.name = name;
//		this.value = value;
//	}
//	
//	
//	//FIXME - tem de de fazer: EnvironmentVariableDto implements Comparable
//	@Override
//	protected void dispatch() {
//		MyDrive md = getMyDrive();
//		Session s = md.getSessionByToken(token);
//		
//		createdVariables = new ArrayList<EnvironmentVariable>();
//		
//		EnvironmentVariable v = new EnvironmentVariable(name, value); 
//		s.addEnvironmentVariable(v);
//		
//		for(EnvironmentVariable v : s.getEnvironmentVariables()) {
//			createdVariables.add(v);
//		}
//		
//		Collections.sort(createdVariables);
//	}
//
//	
//	public final List<EnvironmentVariable> result() {
//		return createdVariables;
//	}
//
//}
