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
        RootUser r = RootUser.getInstance();;
        setCounter(0); 
        super.setRootUser(r);
        addUsers(r);
        Directory rootDir = Directory.newRootDir(getRootUser());
        rootDir.setOwner(getRootUser());
        Directory home = new Directory("home", generateId(), getRootUser(), rootDir);
        Directory root = new Directory("root", generateId(), getRootUser(), home);
        getRootUser().setMainDirectory(root);
        setRootDirectory(rootDir);

    }
    
    private Session getSessionByToken(long token) throws ExpiredSessionException,InvalidTokenException{


        Set<Session> sessions = getSessions();
        DateTime actual = new DateTime();
        DateTime twohoursbefore = actual.minusHours(2);
        Session s = null;
        for(Session session : sessions){
            int result = DateTimeComparator.getInstance().compare(twohoursbefore, session.getTimestamp());
            if(session.getToken() == token){
                if(result <= 0 ){
                    session.setTimestamp(actual);
                    s = session;
                }
                else{
                    removeSessions(session);
                    throw new ExpiredSessionException();

                } 
            }
            else{
                if(result > 0){
                    removeSessions(session);
                }
            }
        }
        if(s==null){
            throw new InvalidTokenException();
        }
        else{
            return s;
        }
    } 

    public Directory getCurrentDirByToken(long token) throws ExpiredSessionException,InvalidTokenException{
		Session session = getSessionByToken(token);
        return session.getCurrentDir();
    }

    public void setCurrentDirByToken(long token, Directory dir) throws ExpiredSessionException,InvalidTokenException{
    	Session session = getSessionByToken(token);
        session.setCurrentDir(dir);
    }

    public User getCurrentUserByToken(long token)throws ExpiredSessionException,InvalidTokenException{
		Session session = getSessionByToken(token);
        return session.getCurrentUser();
    }

    public void setCurrentUserByToken(long token, User u) throws ExpiredSessionException,InvalidTokenException{
        Session session = getSessionByToken(token);
        session.setCurrentUser(u);
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
	
	   for (File f: getRootDirectory().getFiles()){
            f.xmlExport(element);
        }
	   return doc;
    }

    public void checkPermissions(long token, String fileName, String code, String access) throws PermissionDeniedException {
    	if(code.equals("create-delete")) {
    		checkFileCreateDeletePermissions(token, fileName, access);
    	}
    	else if(code.equals("read-write-execute")) {
    		checkFileReadWriteExecutePermissions(token, fileName, access);
    	}
    	else if(code.equals("cd")) {
    		checkChangeDirPermission(token, fileName);
    	}
    	else if(code.equals("ls")) {
    		checkListPermission(token);
    	}
    		
    }
    
    
    public void checkFileCreateDeletePermissions(long token, String fileName, String access) throws PermissionDeniedException, ExpiredSessionException,InvalidTokenException {
    	User u = getCurrentUserByToken(token);
    	Directory d = getCurrentDirByToken(token);
    	User owner = d.getOwner();
    	Permission dirOwnP = d.getUserPermission();
    	Permission dirOthP = d.getOthersPermission();    	
    	if(u.getUsername().equals("root")) {
    		return;
    	}
    	
    	else if(u.getUsername().equals(owner.getUsername())) {
    		if(access.equals("delete")) {
    			File f = d.get(fileName);
    			if(!f.getUserPermission().getEliminate()) {
    				throw new PermissionDeniedException(fileName);
    			}
    		}
    		if(!dirOwnP.getWrite()) {
    			throw new PermissionDeniedException(fileName);
    		}		
    	}
    	else {
    		if(access.equals("delete")) {
    			File f = d.get(fileName);
    			if(!f.getOthersPermission().getEliminate()) {
    				throw new PermissionDeniedException(fileName);
    			}
    		}
    		if(!dirOthP.getWrite()) {
    			throw new PermissionDeniedException(fileName);
    		}
    	}
    }
    
    
    public void checkFileReadWriteExecutePermissions(long token, String fileName, String access) {
    	User u = getCurrentUserByToken(token);
    	Directory d = getCurrentDirByToken(token);	
    	File f = d.get(fileName);
    	User owner = f.getOwner();
    	Permission fileUserP = f.getUserPermission(); 
    	Permission fileOthP = f.getOthersPermission();
    	
    	if(u.getUsername().equals("root")) {
    		return;
    	}
    	
    	else if(u.getUsername().equals(owner.getUsername())) {
    		if(access.equals("read")) {
    			if(!fileUserP.getRead()) {
    				throw new PermissionDeniedException(fileName);
    			}
    		}
    		else if(access.equals("write")) {
    			if(!fileUserP.getWrite()) {
    				throw new PermissionDeniedException(fileName);
    			}    			
    		}
    		else if(access.equals("execute")) {
    			if(!fileUserP.getExecute()) {
    				throw new PermissionDeniedException(fileName);
    			}    			
    		}
    	}
    	else {
    		if(access.equals("read")) {
    			if(!fileOthP.getRead()) {
    				throw new PermissionDeniedException(fileName);
    			}    			
    		}
    		else if(access.equals("write")) {
    			if(!fileOthP.getWrite()) {
    				throw new PermissionDeniedException(fileName);
    			}    			
    		}
    		else if(access.equals("execute")) {
    			if(!fileOthP.getExecute()) {
    				throw new PermissionDeniedException(fileName);
    			}    			
    		}
    	}
    }
    
    
    public void checkChangeDirPermission(long token, String fileName) {
    	User u = getCurrentUserByToken(token);
    	Directory d = getCurrentDirByToken(token);
    	User owner = d.getOwner();
    	Permission dirOwnP = d.getUserPermission();
    	Permission dirOthP = d.getOthersPermission();
    	
    	if(u.getUsername().equals("root")) {
    		return;
    	}
    	
    	else if(u.getUsername().equals(owner.getUsername())) {
    		if(!dirOwnP.getExecute()) {
    			throw new PermissionDeniedException(fileName);
    		}
    	}
    	else {
    		if(!dirOthP.getExecute()) {
    			throw new PermissionDeniedException(fileName);
    		}
    	} 
    }
    
    
    public void checkListPermission(long token) {
    	User u = getCurrentUserByToken(token);
    	Directory d = getCurrentDirByToken(token);
    	User owner = d.getOwner();
    	Permission dirOwnP = d.getUserPermission();
    	Permission dirOthP = d.getOthersPermission();
    	
    	if(u.getUsername().equals("root")) {
    		return;
    	}
    	
    	else if(u.getUsername().equals(owner.getUsername())) {
    		if(!dirOwnP.getRead()) {
    			throw new PermissionDeniedException(d.getName());
    		}
    	}
    	else {
    		if(!dirOthP.getRead()) {
    			throw new PermissionDeniedException(d.getName());
    		}
    	}    	
    }
    
    
}
