package pt.tecnico.mydrive.presentation;

public class Key extends MyDriveCommand {
	public Key(MyDriveShell sh){
		super(sh, "token", "Exchange Session");
	}

	public void execute(String[] args){
		if(args.length == 0){
			System.out.println("Current token: " + getShell().getActiveUser());
			System.out.println("Current user: " + getShell().getActiveToken());
		}
		else if(args.length == 1){
			String user = args[0];
			getShell().setActiveUser(user);
			getShell().setActiveToken(getShell().getToken(user));
			System.out.println("Current token: " + getShell().getActiveToken());
			System.out.println("Current user: " + getShell().getActiveUser());

		}
		else{
			throw new RuntimeException("USAGE: " + name() + " [<path>]");
		}
	}
}