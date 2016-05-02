package pt.tecnico.mydrive.presentation;

public class MyDriveShell extends Shell {

	public static void main(String[] args) throws Exception {
		MyDriveShell sh = new MyDriveShell();
		sh.execute();
	}

	public MyDriveShell() { // add commands here
		    super("MyDrive");
		    new Login(this);
//		    new ChangeWorkingDirectory(this);
		    new List(this);
//		    new Execute(this);
		    new Write(this);
//		    new Environment(this);
//		    new Key(this);
	}

}
