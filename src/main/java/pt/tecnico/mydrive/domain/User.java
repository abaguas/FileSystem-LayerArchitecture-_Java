package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.*;
import org.jdom2.Element;
import org.jdom2.Document;

public class User extends User_Base
{
	public User(){}
	
	public User(String username, String password, String name) throws InvalidUsernameException{
		init(username, password, name);
	}
	
	public User(String username, String password, String name, Directory home) throws InvalidUsernameException{
		init(username, password, name, home);
	}

	public User(String username, String password, String name, Directory home, Permission mask) throws InvalidUsernameException{
		init(username, password, name, home, mask);
	}
	
    protected void init(String username, String password, String name) throws InvalidUsernameException{
    	if (validUsername(username)){
	        setUsername(username);
	        setPassword(password);
	        setName(name);
			Permission ownP = new Permission(true, true, true, true);
			Permission othP = new Permission(false, false, false, false);
			setOwnPermission(ownP);
			setOthersPermission(othP);
    	}
    	else
    		throw new InvalidUsernameException(username); 
    }
    	
	
    protected void init(String username, String password, String name, Directory home) throws InvalidUsernameException{
    	init(username,password,name);
    	setMainDirectory(home);
    }
    
    protected void init(String username, String password, String name, Directory home, Permission mask) throws InvalidUsernameException{
    	init(username,password,name,home);
    	setOwnPermission(mask);
    }
    
	public User(Element user_element, Directory home){
        setMainDirectory(home);
        xmlImport(user_element);
    }

    protected void init(){}
    
    	
    
    public boolean validUsername(String username){
    	String pattern = "^[a-zA-Z0-9]*$"; // String pattern = "[A-Za-z0-9]+";
        if(username.matches(pattern) && (username.length()>2) ) {
            return true;
        }
        else {
        	return false;
        }
    }
    
    
    public String toString(){
    	return "Username: " + getUsername() + "Name: "  + getName();
    }
    public void xmlImport(Element user_element){
        String username= user_element.getAttribute("username").getValue();
        String password= user_element.getChildText("password");
        String name= user_element.getChildText("name");
        String home_xml= user_element.getChildText("home");
        String mask_xml= user_element.getChildText("mask");
        if(password==null){
            password=username;
        }
        if(mask_xml==null){
            mask_xml="rwxd----";       
        }
        if (validUsername(username)){
            setUsername(username);
            setPassword(password);
            setName(name);
            Permission ownpermission = new Permission(mask_xml.substring(0,4));
            Permission otherspermission = new Permission(mask_xml.substring(4));
            setOwnPermission(ownpermission);
            setOthersPermission(otherspermission);
        }
        else
            throw new InvalidUsernameException(username); 
    }
    
    public void xmlExport(Element element_mydrive){
        
        if(!getUsername().equals("root")){

            Element user_element = new Element ("user");
            user_element.setAttribute("username", getUsername());

            Element pass_element = new Element ("password");
            pass_element.setText(getPassword());
            user_element.addContent(pass_element);

            Element name_element = new Element ("name");
            name_element.setText(getName());
            user_element.addContent(name_element);        
            
            Element home_element = new Element ("home");
            home_element.setText(getMainDirectory().getAbsolutePath() + "/" + getMainDirectory().getName());
            user_element.addContent(home_element);
            
            Element mask_element = new Element ("mask");
            mask_element.setText(getOwnPermission().toString() + getOthersPermission().toString());
            user_element.addContent(mask_element);

            element_mydrive.addContent(user_element);

        }
    }	
    
}
