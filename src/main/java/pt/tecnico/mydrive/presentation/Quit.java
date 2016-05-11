package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LogoutService;

public class Quit extends MyDriveCommand {

	public Quit(MyDriveShell sh){
		super(sh, "quit", "Quit the command interpreter");
	}
	
	@Override
	public void execute(String[] args) {
		if (shell().getActiveToken() == shell().getGuestToken()) {
			new LogoutService(shell().getGuestToken());
		}
		System.out.println("MyDrive quit");
        System.exit(0);
	}

}
