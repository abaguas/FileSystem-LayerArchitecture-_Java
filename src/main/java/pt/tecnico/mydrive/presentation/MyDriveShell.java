package pt.tecnico.mydrive.presentation;

import java.util.TreeMap;

import java.io.ObjectInputStream.GetField;

public class MyDriveShell extends Shell {

	private long guestToken;
	private String activeUser;
  	private long activeToken;

  	private TreeMap<String, Long> loggedUsers = new TreeMap<String, Long>();

	
	public static void main(String[] args) throws Exception {
		MyDriveShell sh = new MyDriveShell();
		sh.execute();
	}

	public MyDriveShell() { // add commands here
		    super("MyDrive");
		    new Login(this);
		    new ChangeWorkingDirectory(this);
		    new List(this);
//		    new Execute(this);
		    new Write(this);
		    new Environment(this);
		    new Key(this);
		    String[] str = {"nobody"};
		    Command login = get("login");
		    login.execute(str);
		    new Quit(this);
	}

	public void addUser(String username, Long token){
	    loggedUsers.put(username, token);
	}

	public Long getToken(String username){return loggedUsers.get(username);}

	public void removeUser(String username){
		loggedUsers.remove(loggedUsers.get(username));
	}

	public void removeUser(Long token){
		loggedUsers.remove(token);
	}

	public void setActiveToken(Long token){activeToken=token;}
	public void setActiveUser(String username){activeUser=username;}

	public String getActiveUser(){return activeUser;}
	public long getActiveToken(){return activeToken;}
	
	public long getGuestToken() {
		return guestToken;
	}
	
	public void setGuestToken(long guestToken) {
		this.guestToken = guestToken;
	}
}
