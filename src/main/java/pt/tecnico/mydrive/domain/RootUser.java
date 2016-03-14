package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

public class RootUser extends RootUser_Base{
    
    public static RootUser getInstance(){
        RootUser rootUser = FenixFramework.getDomainRoot().getMyDrive().getRootUser();
        if (rootUser != null)
            return rootUser;
        return new RootUser();
    }
    
    private RootUser(){
    	super();
    	setUsername("root");
        setPassword("***");
        setName("Super User");
	Permission ownP = new Permission(true, true, true, true);
	Permission othP = new Permission(true, false, true, false);
	setOwnPermission(ownP);
        setOthersPermission(othP);
    }
    
}
