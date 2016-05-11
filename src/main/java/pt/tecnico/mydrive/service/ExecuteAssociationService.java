package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.File;


public class ExecuteAssociationService extends MyDriveService{
	
	private long token; 
	private String file;

	public ExecuteAssociationService(long token, String file) {
		this.token = token;
    	this.file = file;
    }

    public final void dispatch() { // throws ContactDoesNotExistException
	// TODO: mockup example
    }
}