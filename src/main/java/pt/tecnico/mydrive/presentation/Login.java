package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LoginService;

public class Login extends MyDriveCommand {
	public Login(MyDriveShell sh){
		super(sh, "login", "Login user");
	}

	public void execute(String[] args){
		String username;
		String password;
		if(args.length == 1){
			username = args[0];
			password = "";
		}
		else if(args.length == 2){
			username = args[0];
			password = args[1];
		}
		else{
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}

		LoginService ls = new LoginService(username, password);
		ls.execute();
		getShell().addUser(username, ls.result());
		getShell().setActiveToken(ls.result());
		getShell().setActiveUser(username);
	}
}
