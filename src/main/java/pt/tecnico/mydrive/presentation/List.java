package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ChangeDirectoryService;
import pt.tecnico.mydrive.service.ListDirectoryService;


public class List extends MyDriveCommand {

	public List(MyDriveShell sh) {
//VERSAO DO RUI
		super(sh, "ls", "list entries of given directory (default: current directory)");
	}

	
	//FIXME
	@Override
	public void execute(String[] args) {
		
		if (args.length > 1 || args.length < 0) {
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}
		
		else if(args.length == 0) {
			ListDirectoryService service = new ListDirectoryService(1); //FIXME qual é o token q se manda no comando?
		    service.execute();
//		    for(String s:service.result()) {
//		    	System.out.println(s);
//		    }    
		}
		
		else if(args.length == 1) {
			new ChangeDirectoryService(1, args[0]).execute(); //FIXME qual é o token q se manda no comando?

			ListDirectoryService service = new ListDirectoryService(1); //FIXME qual é o token q se manda no comando?
		    service.execute();
//		    for(String s:service.result()) {
//		    	System.out.println(s);
//		    }
		    //ACHO QUE NÃO SE DEVE CHAMAR O COMANDO CD!! ou então chamar e reverter de seguida
		}

	}
}
