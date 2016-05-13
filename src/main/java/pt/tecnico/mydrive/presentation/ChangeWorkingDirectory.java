package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ChangeDirectoryService;
import pt.tecnico.mydrive.service.ListDirectoryService;

public class ChangeWorkingDirectory extends MyDriveCommand {
	public ChangeWorkingDirectory(MyDriveShell sh){
		super(sh, "cwd", "Exchange working directory");
	}

	public void execute(String[] args) {
		String path = null;
		if(args.length == 1){
			path = args[0];
		}
		else if(args.length == 0) {
			path = ".";
		}
		else{
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}
		ChangeDirectoryService cds = new ChangeDirectoryService(getShell().getActiveToken(), path);
		cds.execute();
		System.out.println("Current directory: " + cds.getResult());
	}
}
