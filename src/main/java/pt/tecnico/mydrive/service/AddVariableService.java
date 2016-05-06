package pt.tecnico.mydrive.service;


public class AddVariableService extends MyDriveService {

	private long token;
	private String name;
	private String value;
	
	public AddVariableService(long token, String name, String value) {
		this.token = token;
		this.name = name;
		this.value = value;
	}
	
	@Override
	protected void dispatch() {
		
	}

}
