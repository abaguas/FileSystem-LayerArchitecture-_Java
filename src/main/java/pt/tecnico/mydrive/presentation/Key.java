package pt.tecnico.mydrive.presentation;

public class Key extends MyDriveCommand {
	public Key(MyDriveShell sh){
		super(sh, "token", "Exchange Session");
	}

	public void execute(String[] args){
		if(args.length == 0){
			System.out.println("Current token: " + shell().getActiveUser());
			System.out.println("Current user: " + shell().getActiveToken());
		}
		else if(args.length == 1){
			String user = args[0];
			shell().setActiveUser(user);
			shell().setActiveToken(shell().getToken(user));
			System.out.println("Current token: " + shell().getActiveUser());
			System.out.println("Current user: " + shell().getActiveToken());

		}
		else{
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}
	}
}