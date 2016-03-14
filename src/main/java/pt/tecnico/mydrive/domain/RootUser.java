package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.mydrive.exception.InvalidUsernameException;

public class RootUser extends RootUser_Base{
    
    public static RootUser getInstance(){
        RootUser rootUser = FenixFramework.getDomainRoot().getMyDrive().getRootUser();
        if (rootUser != null)
            return rootUser;
        return new RootUser();
    }
    
    private RootUser() throws InvalidUsernameException{
    	init("root","***","Super User");
    	Permission ownP = new Permission(true, true, true, true);
    	Permission othP = new Permission(true, false, true, false);
    	setOwnPermission(ownP);
        setOthersPermission(othP);
    }
    
}
