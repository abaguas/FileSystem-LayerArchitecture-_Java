package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.MyDriveException;

public class MyDrive extends MyDrive_Base {
    
    public MyDrive() throws MyDriveException{
        super();
	setRootUser(new Root ());
	setCurrentUser(getRootUser());
	setRootDirectory(new Directory("/", getRootUser()));
	getRootDirectory().create("home", getRootUser());
	cd("home");
	getRootDirectory().create("root", getRootUser());
	cd("root");
	getRootUser.setMainDirectory(getCurrentDir());
    }

    public createDirectory(String name){
	getCurrentDir().createDir(name, getCurrentUser());
    }

    
}
