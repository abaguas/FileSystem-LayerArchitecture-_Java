package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

import pt.tecnico.mydrive.exception.PermissionDeniedException;

public class RootUser extends RootUser_Base{
    
    public static RootUser getInstance(){
        RootUser rootUser = FenixFramework.getDomainRoot().getMyDrive().getRootUser();
        if (rootUser != null)
            return rootUser;
        return new RootUser(FenixFramework.getDomainRoot().getMyDrive());
    }
    
    private RootUser(MyDrive md) {
        initBasic(md, "root","***","Super User", new Permission(true, true, true, true), new Permission(true, false, true, false));  
    }

    @Override
    public void remove(){
        throw new PermissionDeniedException("Cant Remove Root User");
    }
    
}
