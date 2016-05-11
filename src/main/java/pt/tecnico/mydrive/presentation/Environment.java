package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.AddVariableService;
import pt.tecnico.mydrive.service.LoginService;

public class Environment extends MyDriveCommand {
	
	public Environment(MyDriveShell sh) {
		super(sh, "env", "create or change environment variable");
	}

	@Override
	void execute(String[] args) {
		int numArgs = args.length;
		if (numArgs < 0 || numArgs > 2) {
		    throw new RuntimeException("USAGE: " + name() +" [<name> [<value>]]");
		}
		else {
			long token = getShell().getToken();
			AddVariableService avs = null;
			if (numArgs == 0) {
				new AddVariableService(token, null, null);
				avs.execute();
			}
			else if (numArgs == 1){
				new AddVariableService(token, args[0], null);
				avs.execute();
			}
			else {
				avs = new AddVariableService(token, args[0], args[1]);
				avs.execute();
			}
		}
/*		for (VariableDto v: avs.result()) {
			System.out.println(avs.getName()+" = "+avs.getValue());
		}*/
	}

}
