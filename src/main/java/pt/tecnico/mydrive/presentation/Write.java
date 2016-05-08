package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.WriteFileService;

public class Write extends MyDriveCommand {

	public Write(MyDriveShell sh) {
		super(sh, "update", "change the content of a file");
	}

	@Override
	void execute(String[] args) {
		long token = getShell().getToken();
		if (args.length < 2 || args.length > 2)
		    throw new RuntimeException("USAGE: "+name()+" <path> <text>");
		else
		    new WriteFileService(args[0], args[1], 1).execute(); //FIXME token is third parameter
	}

}
