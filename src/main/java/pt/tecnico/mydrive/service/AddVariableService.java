package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.Session;
import pt.ist.fenixframework.Atomic;
import pt.tecnico.mydrive.domain.Env;

import pt.tecnico.mydrive.service.dto.VariableDto;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;

public class AddVariableService extends MyDriveService {

	private long token;
	private String name;
	private String value;
	private Session s = null;

	public AddVariableService(long token, String name, String value){
		this.token = token;
		this.name = name;
		this.value = value;
		
	}

	@Override
	public final void dispatch(){
		s = getMyDrive().getSessionManager().getSession(token);
		if (name!=null && value!=null) {
			s.addEnv(new Env(name, value));
		}
	}
	
	@Atomic
	public final List<VariableDto> result() {

		ArrayList<VariableDto> res = new ArrayList<VariableDto>();
		Set<Env> set = s.getEnvSet();
	 	for(Env e : set) {
	 		if (value == null && name == null) {
	 			res.add(new VariableDto(e.toString()));
	 		}
	 		else {
	 			if (e.getName().equals(name)) {
	 				res.add(new VariableDto(e.toString()));
	 			}
	 		}
		}
		return res;
	} 

}