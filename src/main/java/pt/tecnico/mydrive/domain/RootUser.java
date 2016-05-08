package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

public class RootUser extends RootUser_Base{
    
    public static RootUser getInstance(){
        RootUser rootUser = FenixFramework.getDomainRoot().getMyDrive().getRootUser();
        if (rootUser != null)
            return rootUser;
        return new RootUser();
    }
    
    private RootUser() {
    	initSpecial("root","***","Super User");
    	//FIXME: os inits do user estão mal. Então e as permissões de other do root, vão para onde?
    }
    
}
