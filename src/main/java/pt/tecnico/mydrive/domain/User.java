package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.*;


public class User extends User_Base
{
        
    public User(String username, String password, String name, Directory home, Permission mask) throws InvalidUsernameException
    {
    	super();
    	if (validUsername(username))
    	{
	        setUsername(username);
	        setPassword(password);
	        setName(name);
	        setMainDirectory(home);
	        setOwnPermission(mask);
	        //Permission othersPermission = new Permission(...);
	        //setOthersPermission(othersPermission);
    	}
    	else
    		throw new InvalidUsernameException(username);
    }
    
    
    public boolean validUsername(String username)
    {
    	String pattern = "^[a-zA-Z0-9]*$"; // String pattern = "[A-Za-z0-9]+";
        if(username.matches(pattern) && !username.equals(""))
            return true;
        else
        	return false;		
    }
    
    
    public String toString()
    {
    	return "Username: " + getUsername() + "Name: "  + getName();
    }
    
}
