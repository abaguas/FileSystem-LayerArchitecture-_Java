package pt.tecnico.mydrive.service;


public class ExecuteAssociationService extends MyDriveService{
	
	private long token; 
	private String filename;
	String[] argList;

	public ExecuteAssociationService(long token, String filename, String[] argList) {
		this.token = token;
    	this.filename = filename;
    	this.argList = argList;
    }

    public final void dispatch() {
	// TODO: mockup example
    }
}