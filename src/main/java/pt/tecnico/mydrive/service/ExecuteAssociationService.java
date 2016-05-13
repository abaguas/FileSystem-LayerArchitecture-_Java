package pt.tecnico.mydrive.service;


public class ExecuteAssociationService extends MyDriveService{
	
	private long token; 
	private String filename;

	public ExecuteAssociationService(long token, String filename) {
		this.token = token;
    	this.filename = filename;
    }

    public final void dispatch() { // throws ContactDoesNotExistException
	// TODO: mockup example
    }
}