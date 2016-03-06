package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.MyDriveException;

import java.util.Set;

import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidUsernameException;
import pt.tecnico.mydrive.exception.UserAlreadyExistsException;

public class MyDrive extends MyDrive_Base {
    
    public MyDrive() throws MyDriveException{
        super();
	setRootUser(new RootUser ());
	setCurrentUser(getRootUser());
	setCounter(0);
	//setRootDirectory(new Directory(getCounter(),"/", getRootUser()));
	incCounter();
	setCurrentDir(getRootDirectory());
	createDir("home");
	cd("home");
	createDir("root");
	cd("root");
	getRootUser().setMainDirectory(getCurrentDir());
    }

    public void incCounter(){
	setCounter(getCounter() + 1);
    }

    public void decCounter(){
	setCounter(getCounter() - 1);
    }

    public void createDir(String name) throws FileAlreadyExistsException{
	try{
		incCounter();
		createDir(getCounter(), name, getCurrentUser());
	}
	catch(FileAlreadyExistsException e){
		decCounter();
		//System.out.println(e);
	}
    }
    
    public void createDir(int id, String name, User user) throws FileAlreadyExistsException {
	try {
		getCurrentDir().search(name);
	} 
	catch (NoSuchFileException e) {} 
	Directory d = new Directory(id, name, user, getCurrentDir());
	getCurrentDir().addFiles((File) d);
    }

    public void cd(String name) throws NoSuchFileException, FileNotDirectoryException {
	setCurrentDir(getCurrentDir().get(name));
    }

    public void ls(String name) throws NoSuchFileException{
	//getCurrentDir().get(name).ls();
    }
    
    public void ls(){
	getCurrentDir().ls();
    }
    
    public User createUser(String username, String password, String name) throws InvalidUsernameException, UserAlreadyExistsException
    {
		if (userExists(username))
			throw new UserAlreadyExistsException(username);
    	User user = null;
    	//Directory mainDirectory = null;
		try
		{
			setCurrentUser(getRootUser());
			cd("home");
			//mainDirectory = new Directory()
			//incCounter();
			createDir(username);
			cd("username");
			user = new User(username, password, name);
			user.setMainDirectory(getCurrentDir());
		}
		catch(InvalidUsernameException e)
		{
			System.out.println(e.getMessage());
		}
		catch(UserAlreadyExistsException e)
		{
			System.out.println(e.getMessage());
		}
						
		getUsersSet().add(user);
		return user;
    }

    
    public boolean userExists(String username)
    {
		Set<User> users = getUsersSet();
		for (User u : users)
	 		if (u.getName().equals(u.getUsername()))
	 			return true;
		return false;
    }
    
    
    
    public void createPlainFile(String name, String owner, String permits, String content, String absolutepath, int id){
    	//getUsers(owner);
    	setCounter(id);
    	Directory d = getDirectoryByAbsolutePath(absolutepath);
    	//d.addFiles((File) new PlainFile(id, name, owner, content, d));
    }

    public void createApp(String name, String owner, String permits, String content, String absolutepath, int id){
    	//getUsers(owner);
    	setCounter(id);
    	Directory d = getDirectoryByAbsolutePath(absolutepath);
    	//d.addFiles((File) new App(id, name, owner, content, d));
    }
  
    public void createLink(String name, String owner, String permits, String content, String absolutepath, int id){
    	//getUsers(owner);
    	setCounter(id);
    	Directory d = getDirectoryByAbsolutePath(absolutepath);
    	//d.addFiles((File) new Link(id, name, owner, content, d));
    }
    
    public void createDirectory(String name, String owner, String permits, String content, String absolutepath, int id){
	//getUsers(owner);
	setCounter(id);
	Directory d = getDirectoryByAbsolutePath(absolutepath);
	//d.addFiles((File) new Directory(id, name, owner, d));
    }

    public Directory getDirectoryByAbsolutePath(String absolutepath){
	String[] parts = absolutepath.split("/");
	setCurrentDir(getRootDirectory());
	for(int i=1; i < parts.length - 1; i++){
		try{
			setCurrentDir((Directory)getCurrentDir().get(parts[i]));
		}
		catch(NoSuchFileException e1){
			try{
				createDir(parts[i]);
				setCurrentDir(getCurrentDir().get(parts[i]));
			}
			catch(MyDriveException e2){}
		}
	}
	return getCurrentDir();
    }
}
