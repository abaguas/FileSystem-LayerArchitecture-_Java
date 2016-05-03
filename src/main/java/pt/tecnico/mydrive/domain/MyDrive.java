package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

import org.jdom2.Element;
import org.jdom2.Document;
import pt.tecnico.mydrive.exception.MyDriveException;

import java.util.Set;
import java.util.ArrayList;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.FileIsNotExecuteAbleException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidUsernameException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.UserAlreadyExistsException;
import pt.tecnico.mydrive.exception.NoSuchUserException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.InvalidIdException;
import pt.tecnico.mydrive.exception.InvalidOperationException;
import pt.tecnico.mydrive.exception.ExpiredSessionException;
import pt.tecnico.mydrive.exception.InvalidTokenException;

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
        setRootUser(r);
        setSessionManager(SessionManager.getInstance()); 
        addUsers(r);
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

    public void cdable(File f) throws FileNotCdAbleException{
		Visitor v = new CdableVisitor();
   	 	f.accept(v);
   	}

    public void writeable(File f) throws FileIsNotWriteAbleException{
        Visitor v = new WriteAbleVisitor();
        f.accept(v);
    }
    

    public void executable(File f) throws FileIsNotExecuteAbleException{
        Visitor v = new ExecuteAbleVisitor();
        f.accept(v);
    }
    
    public String ls(long token, String name) throws NoSuchFileException{
    	return getCurrentDirByToken(token).get(name).ls();
    }
    
    public String ls(long token){
		return getCurrentDirByToken(token).ls();
    }
    
    public void createUser_xml(Element user_element) throws InvalidUsernameException, UserAlreadyExistsException, FileAlreadyExistsException{
    	String default_home="/home";
    	String home = user_element.getChildText("home");
        String username = user_element.getAttribute("username").getValue();
    	if(home==null){
            home=default_home.concat("/" + username);
        }
		Directory home_user = (Directory)getFileByPathXml(home, getRootDirectory());
		User user = new User(user_element,home_user);
		home_user.setOwner(user);
        user.setMainDirectory(home_user);
		addUsers(user);
    }

    
    public boolean userExists(String username){
		Set<User> users = getUsersSet();
		for (User u : users)
	 		if (u.getName().equals(u.getUsername()))
	 			return true;
		return false;
    }

    @Override 
    public void removeUsers(User u){
        if(!u.getUsername().equals("root")){
            u.remove();
        }
    }

    @Override
    public void setRootUser(RootUser r){
        
    }
    
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
    	d.addFiles(file);
    }

    public User getUserByUsername(String username) throws NoSuchUserException {
        Set<User> users = getUsersSet();
        for(User u : users){
            if(u.getUsername().equals(username))
                return u;
        }
        throw new NoSuchUserException(username);
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

    public File getFileByPath(String path, Directory dir) throws  NoSuchFileException, FileNotDirectoryException {
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
                throw new NoSuchFileException(parts[i]);
            }
            i++;
        }
        return dir.get(parts[numOfParts-1]);        
    }

    public boolean fileIdExists(int id){
        if(id > getCounter()){
            return false;
        }
        else{
            throw new InvalidIdException(id);
        }
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
	
	   for (File f: getRootDirectory().getFilesSet()){
            f.xmlExport(element);
        }
	   return doc;
    }


    
    
}
