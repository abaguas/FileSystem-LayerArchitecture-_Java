package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LogoutService;

public class Quit extends MyDriveCommand {

	public Quit(MyDriveShell sh){
		super(sh, "quit", "Quit the command interpreter");
	}
	
	@Override
	void execute(String[] args) {
		new LogoutService(getShell().getGuestToken());
		System.out.println(" quit"); //como sei o nome da shell agora?
        System.exit(0);
	}

}
