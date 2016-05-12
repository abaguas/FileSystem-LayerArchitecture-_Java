package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

import org.jdom2.Element;
import org.jdom2.Document;
import pt.tecnico.mydrive.exception.MyDriveException;

import java.util.Set;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.FileIsNotExecuteAbleException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidUsernameException;
import pt.tecnico.mydrive.exception.UserAlreadyExistsException;
import pt.tecnico.mydrive.exception.NoSuchUserException;
import pt.tecnico.mydrive.exception.InvalidIdException;
import pt.tecnico.mydrive.exception.InvalidOperationException;

public class MyDrive extends MyDrive_Base {

    public static MyDrive getInstance(){
        MyDrive md = FenixFramework.getDomainRoot().getMyDrive();
        if (md != null)
            return md;
        return new MyDrive(); 
    }

    private MyDrive() throws MyDriveException{
        setRoot(FenixFramework.getDomainRoot());
        RootUser r = RootUser.getInstance();
        setCounter(0);
        super.setRootUser(r);
        GuestUser guestUser = GuestUser.getInstance();
        super.setGuestUser(guestUser);
        super.setSessionManager(SessionManager.getInstance()); 
        addUsers(r);
        addUsers(guestUser);
        Directory rootDir = Directory.newRootDir(getRootUser());
        rootDir.setOwner(getRootUser());
        Directory home = new Directory("home", generateId(), getRootUser(), rootDir);
        Directory root = new Directory("root", generateId(), getRootUser(), home);
        getRootUser().setMainDirectory(root);
        setRootDirectory(rootDir);
    }


    public int generateId(){
    	setCounter(getCounter()+1);
        return getCounter();
    }

    public void removeId(){
        setCounter(getCounter()-1);
    }
    
    public boolean userExists(String username){
		Set<User> users = getUsersSet();
		for (User u : users)
	 		if (username.equals(u.getUsername()))
	 			return true;
		return false;
    }

    @Override 
    public void removeUsers(User u){
        if(!u.equals(getRootUser()) || (!u.equals(getGuestUser()))){
            u.remove();
        }
        else {
        	throw new InvalidOperationException("Remove user: " + u.getName());
        }
    }

    @Override
    public void setRootUser(RootUser r) {
        throw new InvalidOperationException("set rootUser");
    }
    
    @Override
    public void setGuestUser(GuestUser guestUser) {
    	throw new InvalidOperationException("set guestUser");
    }

    public User getUserByUsername(String username) throws NoSuchUserException {
        Set<User> users = getUsersSet();
        for(User u : users){
            if(u.getUsername().equals(username))
                return u;
        }
        throw new NoSuchUserException(username);
    }
    
    public boolean fileIdExists(int id){
        if(id > getCounter()){
            return false;
        }
        else{
            throw new InvalidIdException(id);
        }
    }
    
//////////////////////////////////////////////////////////////////////////////////////
//                                       XML                               //
//////////////////////////////////////////////////////////////////////////////////////
    
    public File fileFactory(Element element, User owner, Directory father, String code){
        if(code.equals("PlainFile")){
            return new PlainFile(element, owner, father);
        }
        else if(code.equals("App")){
            return new App(element, owner, father);
        }
        else if(code.equals("Link")){
            return new Link(element, owner, father);
        }
        else{
            return new Directory(element, owner, father);
        }
    }

    public void createUser_xml(Element user_element) throws InvalidUsernameException, UserAlreadyExistsException, FileAlreadyExistsException{
    	String default_home="/home";
    	String home = user_element.getChildText("home");
        String username = user_element.getAttribute("username").getValue();
        System.out.println("creating " + username);
    	if(home==null){
            home=default_home.concat("/" + username);
        }
		User user = new User(user_element, this);
        addUsers(user);
    }
    
    
    public void createFile_xml(Element file_element, String code) throws NoSuchUserException {
    	String owner = file_element.getChildText("owner");
        User user = null;
    	if(owner ==null){
    		user = getRootUser();
    	}
    	else {
            user = getUserByUsername(owner); 
    	}
    	Directory d =  (Directory)getFileByPathXml(file_element.getChildText("path"), getRootDirectory());
        File file = fileFactory(file_element,user, d, code);
    }


    public File getFileByPathXml(String path, Directory dir) throws  NoSuchFileException, FileNotDirectoryException {
        String[] parts = path.split("/");
        int i = 0;
        int numOfParts = parts.length;
        if(numOfParts == 0){
            return dir.get(parts[i]);
        }
        else if(path.charAt(0)=='/'){
            i = 1;
            dir = getRootDirectory();
        }
        else{
            i = 0;
        }
        while(i < numOfParts-1){
            try{
                dir = (Directory)dir.get(parts[i]);
            }
            catch(Exception e){
                dir = new Directory(parts[i], generateId(), getRootUser(), dir);
            }
            i++;
        }
        File ret = null; 
        try{
            ret = dir.get(parts[numOfParts-1]);
        }
        catch(Exception e){
            ret = new Directory(parts[numOfParts-1], generateId(), getRootUser(), dir);
        }
        return ret;        
    }
    
    public void xmlImport(Element element){
        for(Element node : element.getChildren("user")){
            createUser_xml(node);
        }
        for(Element node: element.getChildren()){
            if(node.getName().equals("plain")){
                createFile_xml(node, "PlainFile");
            }
            else if(node.getName().equals("dir")){
                createFile_xml(node,"Dir");
            }
            else if(node.getName().equals("link")){  
                createFile_xml(node,"Link");
            }
            else if(node.getName().equals("app")){
                createFile_xml(node,"App");
            }
            else{}
        }
    }
    
    public Document xmlExport(){
	   Element element = new Element ("mydrive");
	   Document doc = new Document (element);

	   for (User u: getUsersSet())
            u.xmlExport(element);
	
	   getRootDirectory().xmlExport(element);
	   return doc;
    }
}
