package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.FileOrDirectoryAlreadyExistsException;
import pt.tecnico.mydrive.exception.NoSuchFileOrDirectoryException;

public class MyDrive extends MyDrive_Base {
    
    public MyDrive() throws MyDriveException{
        super();
	setRootUser(new RootUser ());
	setCurrentUser(getRootUser());
	setRootDirectory(new Directory("/", getRootUser()));
	getRootDirectory().create("home", getRootUser());
	cd("home");
	getRootDirectory().create("root", getRootUser());
	cd("root");
	getRootUser.setMainDirectory(getCurrentDir());
    }

    public void createDirectory(String name) throws FileOrDirectoryAlreadyExistsException{
	getCurrentDir().createDir(name, getCurrentUser());
    }

    public void cd(String name) throws NoSuchFileOrDirectoryException {
	setCurrentDir(getCurrentDir().get(name));
    }

    public void ls(String name) throws NoSuchFileOrDirectoryException{
	getCurrentDir().ls(name);
    }
    
    public void ls(){
	getCurrentDir().ls();
    }
}
