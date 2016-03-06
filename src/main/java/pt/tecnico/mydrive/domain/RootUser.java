package pt.tecnico.mydrive.domain;

public class RootUser extends RootUser_Base
{
    
    public RootUser()
    {
    	super();
    	//super("root", "***", "Super User");
    	setUsername("root");
        setPassword("***");
        setName("Super User");
        // setOwnPermission(); tenho de passar permissão
        // setOthersPermission(); tenho de passar permissão
        
    }
    
}
