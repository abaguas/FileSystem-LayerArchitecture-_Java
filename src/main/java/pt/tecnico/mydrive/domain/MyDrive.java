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
        long token = 0; //generate root token
        setRoot(FenixFramework.getDomainRoot());
        RootUser r = null;
        r = RootUser.getInstance();
        //Session rootSession = new Session(r, token, this);
        setRootUser(r);
        addUsers(r);
        setCounter(0);
        setRootDirectory(Directory.newRootDir(getRootUser()));
        getRootDirectory().setOwner(getRootUser());
        setCurrentDirByToken(token, getRootDirectory());
        createDir(token, "home");
        cd(token, "home");
        createDir(token, "root");
        cd(token, "root");
        getRootUser().setMainDirectory(getCurrentDirByToken(token));
    }

    public Session getSessionByToken(long token) throws ExpiredSessionException,InvalidTokenException{


        Set<Session> sessions = getSessionSet();
        DateTime actual = new DateTime();
        DateTime twohoursbefore = actual.minusHours(2);
        Session s = null;
        for(Session session : sessions){
            int result = DateTimeComparator.getInstance().compare(twohoursbefore, session.getTimestamp());
            if(session.getToken()== token){
                if(result == -1 || result == 0 ){
                    session.setTimestamp(actual);
                    s = session;
                }
                else{
                    removeSession(session);
                    throw new ExpiredSessionException();

                } 
            }
            else{
                if(result == 1){
                    removeSession(session);
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

    public String pwd(long token){
        Directory current = getCurrentDirByToken(token);
        String output="";
        if(getCurrentDirByToken(token).getName().equals("/")){
            output="/";
        }
        else{
            while(!current.getName().equals("/")){
                output = "/" + current.getName() + output;
                current= current.getFatherDirectory();
            }
        }
        return output;
    }


    public void createFile(long token, String name, String content, String code) throws FileAlreadyExistsException, MaximumPathException {
        try{
            getCurrentDirByToken(token).createFile(name, content, generateId(), getCurrentUserByToken(token), code);
        }
        catch(FileAlreadyExistsException e){
            removeId();
            throw new FileAlreadyExistsException(name, e.getId());
        }
    }
    
    public void createDir(long token, String name) throws FileAlreadyExistsException{
        try{
        	getCurrentDirByToken(token).createFile(name, "", generateId(), getCurrentUserByToken(token), "Dir");
        }
        catch(FileAlreadyExistsException e){
            removeId();
            throw new FileAlreadyExistsException(name, e.getId());
        }
    }
    
    public void createPlainFile(long token, String name, String content) throws FileAlreadyExistsException{
        createFile(token, name, content, "PlainFile");
    }
    
    public void createApp(long token, String name, String content) throws FileAlreadyExistsException{
        createFile(token, name, content, "App");
    }
    
    public void createLink(long token, String name, String content) throws FileAlreadyExistsException{
        createFile(token, name, content, "Link");
    }    
    
    /*
    public void cd(long token, String name) throws NoSuchFileException, FileNotCdAbleException {
    	File f = null;
        if(name.contains("/")){
            f = getDirectoryByAbsolutePath(token, name);
            cdable(f);
            setCurrentDir(token, (Directory) f);
        }
        else{
            f = getCurrentDir(token).get(name);
        	cdable(f);
       	    setCurrentDir(token, (Directory) f);
        }
    }
	*/
    
    public void cd(long token, String name) throws NoSuchFileException, FileNotCdAbleException, PermissionDeniedException {
    	File f = null;
        if(name.charAt(0)=='/') {
            f = getDirectoryByAbsolutePath(token, name); //getDirectoryByAbsolutePath chama o último caso desta função, que chama o checkPermissions
            cdable(f);
            setCurrentDirByToken(token, (Directory) f);
        }
        else if(name.contains("/")) {
        	String result = pwd(token);
        	result = result + "/" + name;
            f = getDirectoryByAbsolutePath(token, result); //getDirectoryByAbsolutePath chama o último caso desta função, que chama o checkPermissions
            cdable(f);
            setCurrentDirByToken(token, (Directory) f);
        }
        else {
            f = getCurrentDirByToken(token).get(name);
        	cdable(f);
        	checkPermissions(token, name, "cd", "");
       	    setCurrentDirByToken(token, (Directory) f);
        }
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
    

    public void createUser(long token, String username, String password, String name) throws InvalidUsernameException, UserAlreadyExistsException {
	   if (userExists(username))
        throw new UserAlreadyExistsException(username);
        User user = null;
        long roottoken = getRootUser().getSession().getToken();
        setCurrentDirByToken(roottoken, getRootDirectory());
        cd(roottoken, "home");
        createDir(roottoken, username);
        cd(roottoken, "username");
        user = new User(username, password, name, getCurrentDirByToken(token)); //RUI faz permissao default
        getCurrentDirByToken(roottoken).setOwner(user);				
        getUsersSet().add(user);
    }
    
    public void removeUser(long token, User user) throws NoSuchUserException{
    	//TODO implentation of this method
    	//FIXME throws UserIsRootException
    }
    
    public void createUser_xml(long token, Element user_element) throws InvalidUsernameException, UserAlreadyExistsException, FileAlreadyExistsException{
    	String default_home="/home";
    	String home = user_element.getChildText("home");
        String username = user_element.getAttribute("username").getValue();
    	if(home==null){
            home=default_home.concat("/" + username);
        }
		Directory home_user = getDirectoryByAbsolutePath(token, home);
		User user = new User(user_element,home_user);
		home_user.setOwner(user);
		addUsers(user);
    }

    
    public boolean userExists(String username){
		Set<User> users = getUsersSet();
		for (User u : users)
	 		if (u.getName().equals(u.getUsername()))
	 			return true;
		return false;
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

    public void createFile_xml(long token, Element file_element, String code) throws NoSuchUserException {
    	String owner = file_element.getChildText("owner");
        User user = null;
    	if(owner ==null){
    		user = getRootUser();
    	}
    	else {
            user = getUserByUsername(owner); 
    	}
    	Directory d =  getDirectoryByAbsolutePath(token, file_element.getChildText("path"));
        File file = fileFactory(file_element,user, d, code);
    	d.addFiles(file);
    }
    
    public void writeFile(String filename, String content, long token) throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
        Directory current = getCurrentDirByToken(token);
        File file = current.get(filename);
        writeable(file);
        PlainFile f = (PlainFile)file;
        checkPermissions(token, filename, "read-write-execute", "write");
        f.writeContent(content);
    }

    public User getUserByUsername(String username) throws NoSuchUserException {
        Set<User> users = getUsersSet();
        for(User u : users){
            if(u.getUsername().equals(username))
                return u;
        }
        throw new NoSuchUserException(username);
    }

    public Directory getDirectoryByAbsolutePath(long token, String absolutepath){
	String[] parts = absolutepath.split("/");
	setCurrentDirByToken(token, getRootDirectory());
	for(int i=1; i < parts.length; i++){
		try{
			cd(token, parts[i]);
		}
		catch(NoSuchFileException e1){
			try{
				createDir(token, parts[i]);
				setCurrentDirByToken(token, (Directory) getCurrentDirByToken(token).get(parts[i]));
			}
			catch(MyDriveException e2){}
		}
		catch(FileNotDirectoryException e){
			//FIXME: se houver xml errado mandar InvalidPathException
		}
	}
	   return getCurrentDirByToken(token);
    }

    public File getFileByAbsolutePath(long token, String absolutepath) throws NoSuchFileException{
        String[] parts = absolutepath.split("/");
        setCurrentDirByToken(token, getRootDirectory());
        int i;
        for(i=1; i < parts.length - 1; i++){
            try{
                cd(token, parts[i]);
            }
            catch(NoSuchFileException e1){
                try{
                    createDir(token, parts[i]);
                    setCurrentDirByToken(token, (Directory) getCurrentDirByToken(token).get(parts[i]));
                }
                catch(MyDriveException e2){}
            }
            catch(FileNotDirectoryException e){
                //FIXME: se houver xml errado mandar InvalidPathException
            }
        }
        return getCurrentDirByToken(token).get(parts[i]);
    }

    public boolean fileIdExists(int id){
        if(id > getCounter()){
            return false;
        }
        else{
            throw new InvalidIdException(id);
        }
    }
    public void reserveIds(Element element){
        /*for (Element node : element.getChildren()) {
            if(!node.getName().equals("user")){
                _ids.add(Integer.parseInt(node.getAttribute("id").getValue()));  
            }
        }*/
    }
    
    
    public void xmlImport(long token, Element element){
        reserveIds(element);
        for(Element node : element.getChildren("user")){
            createUser_xml(token, node);
        }
        for(Element node: element.getChildren()){
            if(node.getName().equals("plain")){
                createFile_xml(token, node, "PlainFile");
            }
            else if(node.getName().equals("dir")){
                createFile_xml(token, node,"Dir");
            }
            else if(node.getName().equals("link")){  
                createFile_xml(token, node,"Link");
            }
            else if(node.getName().equals("app")){
                createFile_xml(token, node,"App");
            }
            else{}
        }
        setCurrentDirByToken(token, getRootUser().getMainDirectory());
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
    
    //parameter code can be "create-delete", "read-write-execute", "cd" or "ls"
    //parameter access can be "create", "delete" (1st case) or "read", "write", "execute" (2nd case)
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
    
    
    public void checkFileCreateDeletePermissions(long token, String fileName, String access) throws PermissionDeniedException {
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
