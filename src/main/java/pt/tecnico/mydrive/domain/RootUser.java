package pt.tecnico.mydrive.domain;

public class RootUser extends RootUser_Base
{
    
    public RootUser()
    {
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
