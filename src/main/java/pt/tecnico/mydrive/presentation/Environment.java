package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.AddVariableService;
import pt.tecnico.mydrive.service.LoginService;
import pt.tecnico.mydrive.service.dto.VariableDto;

public class Environment extends MyDriveCommand {
	
	public Environment(MyDriveShell sh) {
		super(sh, "env", "create or change environment variable");
	}

	@Override
	public void execute(String[] args) {
		AddVariableService avs = null;
		int numArgs = args.length;
		if (numArgs < 0 || numArgs > 2) {
		    throw new RuntimeException("USAGE: " + name() +" [<name> [<value>]]");
		}
		else {
			long token = getShell().getActiveToken();
			if (numArgs == 0) {
				avs = new AddVariableService(token, null, null);
				avs.execute();
			}
			else if (numArgs == 1){
				avs = new AddVariableService(token, args[0], null);
				avs.execute();
			}
			else {
				avs = new AddVariableService(token, args[0], args[1]);
				avs.execute();
			}
		}
		avs.result();
		
		for (VariableDto v: avs.result()) {
			System.out.println(v.getName()+" = "+v.getValue());
		}
	}

}
