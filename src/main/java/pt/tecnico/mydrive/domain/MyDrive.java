package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;

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
    
    public void createUser(String username, String password, String name, Directory home, Permission mask){

    }

    public void createPlainFile(String name, User owner, Permission permits, String content, String absolutepath){
    	Directory d = getDirectoryByAbsolutePath(absolutepath);
    	//d.addFiles((File) new PlainFile(name, owner, content, d));
    }

    public void createApp(String name, User owner, Permission permits, String content, String absolutepath){
    	Directory d = getDirectoryByAbsolutePath(absolutepath);
    	//d.addFiles((File) new App(name, owner, content, d));
    }
  
    public void createLink(String name, User owner, Permission permits, String content, String absolutepath){
    	Directory d = getDirectoryByAbsolutePath(absolutepath);
    	//d.addFiles((File) new Link(name, owner, content, d));
    }
    
    public void createDirectory(String name, User owner, Permission permits, String content, String absolutepath){
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
