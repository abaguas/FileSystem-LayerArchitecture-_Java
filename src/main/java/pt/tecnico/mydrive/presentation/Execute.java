package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ExecuteFileService;

public class Execute extends MyDriveCommand {
	public Execute(MyDriveShell sh){
		super(sh, "do", "Execute a Plain File");
	}

	public void execute(String[] args){
		if(args.length > 1){
		  long token = getShell().getActiveToken();
		  String path = args[0];
		  String[] arguments = new String[300];
		  for(int i=1; i < args.length ; i++){
			  System.out.println(args[i]);
		    arguments[i-1] = args[i];
		  }
		  ExecuteFileService eps = new ExecuteFileService(token, path, arguments);
			eps.execute();
		}
		else if (args.length == 1) {
			 long token = getShell().getActiveToken();
			  String path = args[0];
			  String[] arguments = new String[0];
			  ExecuteFileService eps = new ExecuteFileService(token, path, arguments);
				eps.execute();
		}
		else{
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}
	}
}
