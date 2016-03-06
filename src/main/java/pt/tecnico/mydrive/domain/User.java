package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.*;
import org.jdom2.Element;
import org.jdom2.Document;

public class User extends User_Base
{
    protected User(String username, String password, String name)
    {
    	super();
    	if (validUsername(username))
    	{
	        setUsername(username);
	        setPassword(password);
	        setName(name);
			Permission ownP = new Permission(true, true, true, true);
			Permission othP = new Permission(false, false, false, false);
			setOwnPermission(ownP);
			setOthersPermission(othP);
    	}
    	else
    		throw new InvalidUsernameException(username); //no myDrive vai ter de se apanhar esta excepção
    }
    	
	
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
	        Permission othersPermission = new Permission(false, false, false, false);
	        setOthersPermission(othersPermission);
    	}
    	else
    		throw new InvalidUsernameException(username); //no myDrive vai ter de se apanhar esta excepção
    }
    
    public User(){}	
    
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
    
    public void XMLExport(Element element_mydrive){
        Element element = new Element ("user");
        element.setAttribute("username", getUsername());
        
        element = new Element ("password");
        element.setText(getPassword());
        
        element = new Element ("name");
        element.setText(getName());
        
        element = new Element ("home");
        //element.setText(getMainDirectory());
        
        element = new Element ("mask");
        //element.setText(getOwnPermission());
    }	
    
}
