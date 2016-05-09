package pt.tecnico.mydrive.presentation;

import java.io.ObjectInputStream.GetField;

public class MyDriveShell extends Shell {

	private long token;
	private long guestToken;
	
	public static void main(String[] args) throws Exception {
		MyDriveShell sh = new MyDriveShell();
		sh.execute();
	}

	public MyDriveShell() { // add commands here
		    super("MyDrive");
//		    new Login(this);
//		    new ChangeWorkingDirectory(this);
		    new List(this);
//		    new Execute(this);
		    new Write(this);
//		    new Environment(this);
//		    new Key(this);
//		    String[] str = {"nobody"};
//		    Command login = get("login");
//		    login.execute(str);
		    //O comando de login alterará a variável token
		    new Quit(this);
	}

	public long getToken() {
		return token;
	}

	public void setToken(long token) {
		this.token = token;
	}
	
	public long getGuestToken() {
		return guestToken;
	}
	
	public void setGuestToken(long guestToken) {
		this.guestToken = guestToken;
	}
}
