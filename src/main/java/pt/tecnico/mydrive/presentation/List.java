package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ListDirectoryService;
import pt.tecnico.mydrive.service.dto.FileDto;


public class List extends MyDriveCommand {

	public List(MyDriveShell sh) {
		super(sh, "ls", "list entries of given directory (default: current directory)");
	}

	
	@Override
	public void execute(String[] args) {

		if(args.length == 0) {
		    
			ListDirectoryService lds = new ListDirectoryService(shell().getActiveToken());
			lds.execute();
			
		    for(FileDto file:lds.result()) {
		    	String s = file.getType() + " " + file.getUserPermission() + " " + file.getOthersPermission() + " " + file.getDimension() + " " + file.getUsernameOwner() + " " + file.getId() + " " + file.getLastChange() + " " + file.getName();
		    	System.out.println(s);
		    }    
		}
		
		else if(args.length == 1) {

			ListDirectoryService lds = new ListDirectoryService(shell().getActiveToken(), args[0]);
		    lds.execute();
		    for(FileDto file:lds.result()) {
		    	String s = file.getType() + " " + file.getUserPermission() + " " + file.getOthersPermission() + " " + file.getDimension() + " " + file.getUsernameOwner() + " " + file.getId() + " " + file.getLastChange() + " " + file.getName();
		    	System.out.println(s);
		    }
		    
		}
		
		else {
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}

	}
}
