package pt.tecnico.mydrive.presentation;

public abstract class MyDriveCommand extends Command {

	public MyDriveCommand(MyDriveShell sh, String n) {
		super(sh, n);
	}
	public MyDriveCommand(MyDriveShell sh, String n, String h) {
		super(sh, n, h);
	}
	
	public MyDriveShell getShell(){
		return (MyDriveShell) shell();
	}

}
