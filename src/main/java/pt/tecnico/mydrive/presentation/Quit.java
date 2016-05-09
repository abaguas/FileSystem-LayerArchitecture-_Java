package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LogoutService;

public class Quit extends MyDriveCommand {

	public Quit(MyDriveShell sh){
		super(sh, "quit", "Quit the command interpreter");
	}
	
	@Override
	void execute(String[] args) {
		if (getShell().getToken() == getShell().getGuestToken()) {
			new LogoutService(getShell().getGuestToken());
		}
		System.out.println("MyDrive quit");
        System.exit(0);
	}

}
