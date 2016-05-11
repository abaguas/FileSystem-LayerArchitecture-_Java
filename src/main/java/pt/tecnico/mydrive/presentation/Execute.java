package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ExecutePlainFileService;

public class Execute extends MyDriveCommand {
	public Execute(MyDriveShell sh){
		super(sh, "do", "Execute a Plain File");
	}

	public void execute(String[] args){
		if(args.length > 1){
		  long token = shell().getActiveToken();
		  String path = args[0];
		  String[] arguments = null;
		  for(int i=1; i < args.length ; i++){
		    arguments[i-1] = args[i];
		  }
		  ExecutePlainFileService eps = new ExecutePlainFileService(token, path, arguments);
			eps.execute();
		}
		else{
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}
	}
}
