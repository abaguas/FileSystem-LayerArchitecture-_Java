package pt.tecnico.mydrive.service;

import pt.ist.fenixframework.Atomic;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.MyDriveException;


public abstract class MyDriveService {

    @Atomic
    public final void execute() throws MyDriveException {
        dispatch();
    }

    static MyDrive getMyDrive() {
        return MyDrive.getInstance();
    }
    
    protected abstract void dispatch() throws MyDriveException;
}