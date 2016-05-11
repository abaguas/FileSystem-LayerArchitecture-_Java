package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

public class User extends User_Base {

	public User(){}
	
	public User(MyDrive md, String username, String password, String name) throws InvalidUsernameException{
        init(md, username, password, name, new Permission(true, true, true, true), new Permission(true, false, true, false));
	}

    public User(MyDrive md, String username, String password, String name, Permission maskOwn, Permission maskOther) throws InvalidUsernameException{
        init(md,username,password,name,maskOwn,maskOther);
    }

	public void init(MyDrive md, String username, String password, String name, Permission maskOwn, Permission maskOther) throws InvalidUsernameException{
		if(validUsername(username)){
            if(!md.userExists(username)){
                setUsername(username);
                super.setMyDrive(md);
                setPassword(password);
                setName(name);
                setOwnPermission(maskOwn);
                setOthersPermission(maskOther);
                Directory d = new Directory(username, md.getRootUser(), (Directory)md.getRootDirectory().get("home"));
                d.setOwner(this);
                setMainDirectory(d);
            }
        }
	}

	protected void initSpecial(String username, String password, String name) {
	   try{
            initBasic(username, password, name);
        }
       catch(InvalidPasswordSizeException e){
            super.setPassword(password);
        }
		setOwnPermission( new Permission(true, true, true, true) );
		setOthersPermission( new Permission(true, false, true, false) );
	}
    
    private void initBasic(String username, String password, String name) {
    	setUsername(username);
    	super.setPassword(password);
    	setName(name);
    }

    
	public User(Element user_element, MyDrive md){
        xmlImport(user_element, md);
    }
    
    public String toString(){
    	return "Username: " + getUsername() + "Name: "  + getName();
    }

    @Override
    public void setMyDrive(MyDrive mydrive) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void remove(){
        setMainDirectory(null);
        setOwnPermission(null);
        setOthersPermission(null);
        setMyDrive(null);
        deleteDomainObject();
    }
    
    @Override
	public void setUsername(String username) {
		String pattern = "^[a-zA-Z0-9]*$"; // String pattern = "[A-Za-z0-9]+";
        if ( !(username.matches(pattern)) || ( !(username.length() > 2)) ) {
        	throw new InvalidUsernameException(username);
        }
		super.setUsername(username);
	}
    @Override
    public void setPassword(String password) throws InvalidPasswordSizeException{
        if(password.length()<8){
            throw new InvalidPasswordSizeException();
        }
        super.setPassword(password);

    }

    @Override
    public void setOwnPermission(Permission mask){
        if(mask != null)
            super.setOwnPermission(mask);
            //super.setOwnPermission(new Permission(mask.substring(0,4)));
        super.setOwnPermission(new Permission("rwxd"));
    }

    @Override
    public void setOthersPermission(Permission mask){
        if(mask != null)
            super.setOthersPermission(mask);
        super.setOthersPermission(new Permission("----"));
        /*  super.setOthersPermission(new Permission(mask.substring(4)));
        super.setOthersPermission(new Permission("----"));*/
    }

    private boolean validUsername(String username){
        String pattern = "^[a-zA-Z0-9]*$"; // String pattern = "[A-Za-z0-9]+";
        if(username != null){ 
            if(username.matches(pattern) && (username.length()>2) ) {
                return true;
            }
        }
        return false;
    }
    
    public App getFileByExtension(String extension) throws ExtensionNotFoundException {
    	return null;
    }
    
//////////////////////////////////////////////////////////////////////////////////////
      //                                   XML                               //
//////////////////////////////////////////////////////////////////////////////////////    

    public void xmlImport(Element user_element, MyDrive md){
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
        Permission ownpermission = new Permission(mask_xml.substring(0,4));
        Permission otherspermission = new Permission(mask_xml.substring(4));
        init(md, username, password, name, ownpermission, otherspermission);
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
