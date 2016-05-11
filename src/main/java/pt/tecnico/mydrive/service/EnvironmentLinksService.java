package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.SessionManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.Env;
import pt.tecnico.mydrive.domain.Link;

import java.util.List;
import java.util.ArrayList;

public class EnvironmentLinksService extends MyDriveService {

	private long token;
	private Link link;
	private String result;

	public EnvironmentLinksService(long token, Link link){
		this.token = token;
		this.link = link;
	}

	@Override
	public final void dispatch(){
		//MOCKUP
	}

	public final String result() {
		return result;
	} 

}