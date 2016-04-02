package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

import org.jdom2.Element;
import org.jdom2.Document;
import pt.tecnico.mydrive.exception.MyDriveException;

import java.util.Set;
import java.util.ArrayList;

import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidUsernameException;
import pt.tecnico.mydrive.exception.UserAlreadyExistsException;
import pt.tecnico.mydrive.exception.NoSuchUserException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.InvalidIdException;

public class MyDrive extends MyDrive_Base {

    private ArrayList<Integer> _ids = new ArrayList(); 

    public static MyDrive getInstance(){
        MyDrive md = FenixFramework.getDomainRoot().getMyDrive();
        if (md != null)
            return md;
        return new MyDrive();
    }

    private MyDrive() throws MyDriveException{
        setRoot(FenixFramework.getDomainRoot());
        RootUser r = null;
        r = RootUser.getInstance();
        setRootUser(r);
        addUsers(r);
        setCurrentUser(getRootUser());
        _ids.add(0);
        setRootDirectory(Directory.newRootDir(getRootUser()));
        getRootDirectory().setOwner(getRootUser());
        setCurrentDir(getRootDirectory());
        createDir("home");
        cd("home");
        createDir("root");
        cd("root");
        getRootUser().setMainDirectory(getCurrentDir());
    }

    public Integer generateId(){
    	Integer i = 1;
        while(_ids.contains(i)){
            i++;
        }
        _ids.add(i);
        return i;
    }

    public void removeId(Integer id){
        _ids.remove(id);
    }

    public String pwd(){
        Directory current = getCurrentDir();
        String output="";
        if(getCurrentDir().getName().equals("/")){
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

    public void removeFile(String name) throws NoSuchFileException{
        getCurrentDir().remove(name);
    }


    public void createFile(String name, String content, String code) throws FileAlreadyExistsException{
        try{
            getCurrentDir().createFile(name, content, generateId(), getCurrentUser(), code);
        }
        catch(FileAlreadyExistsException e){
            removeId(e.getId());
            throw new FileAlreadyExistsException(name, e.getId());
        }
    }
    
    public void createDir(String name) throws FileAlreadyExistsException{
        try{
            getCurrentDir().createFile(name, "", generateId(), getCurrentUser(), "Dir");
        }
        catch(FileAlreadyExistsException e){
            removeId(e.getId());
            throw new FileAlreadyExistsException(name, e.getId());
        }
    }
    
    public void createPlainFile(String name, String content) throws FileAlreadyExistsException{
        createFile(name, content, "PlainFile");
    }
    
    public void createApp(String name, String content) throws FileAlreadyExistsException{
        createFile(name, content, "App");
    }
    
    public void createLink(String name, String content) throws FileAlreadyExistsException{
        createFile(name, content, "Link");
    }    

    public void cd(String name) throws NoSuchFileException, FileNotDirectoryException {
    	File f = getCurrentDir().get(name);
   	    cdable(f);
    	setCurrentDir((Directory) f);
    }

    public void cdable(File f) throws FileNotDirectoryException{
		Visitor v = new CdableVisitor();
   	 	f.accept(v);
   	}
    
    public String ls(String name) throws NoSuchFileException{
    	return getCurrentDir().get(name).ls();
    }
    
    public String ls(){
    	return getCurrentDir().ls();
    }
    

    public void createUser(String username, String password, String name) throws InvalidUsernameException, UserAlreadyExistsException {
	   if (userExists(username))
		throw new UserAlreadyExistsException(username);
        User user = null;
	   setCurrentUser(getRootUser());
	   setCurrentDir(getRootDirectory());
	   cd("home");
	   createDir(username);
	   cd("username");
	   //user = new User(username, password, name, getCurrentDir()); //RUI faz permissao default
	   getCurrentDir().setOwner(user);				
	   getUsersSet().add(user);
    }

    public void createUser_xml(Element user_element) throws InvalidUsernameException, UserAlreadyExistsException, FileAlreadyExistsException{
    	String default_home="/home";
    	String home = user_element.getChildText("home");
        String username = user_element.getAttribute("username").getValue();
    	if(home==null){
            home=default_home.concat("/" + username);
        }
		Directory home_user = getDirectoryByAbsolutePath(home);
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

    public void createFile_xml(Element file_element, String code) throws NoSuchUserException {
    	String owner = file_element.getChildText("owner");
        User user = null;
    	if(owner ==null){
    		user = getRootUser();
    	}
    	else {
            user = getUserByUsername(owner); 
    	}
    	Directory d =  getDirectoryByAbsolutePath(file_element.getChildText("path"));
        File file = fileFactory(file_element,user, d, code);
    	d.addFiles(file);
    }
    
    public void writeFile(String filename, String content, long token) throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
    	/*
        Session s = getLogin().getSession(token);
        Directory current = s.getDirectory();
        File file = current.get(filename);
        writeable(file);
        //checkpermissions    
        file.writeContent(content);
        */
    }

    public User getUserByUsername(String username) throws NoSuchUserException {
        Set<User> users = getUsersSet();
        for(User u : users){
            if(u.getUsername().equals(username))
                return u;
        }
        throw new NoSuchUserException(username);
    }

    public Directory getDirectoryByAbsolutePath(String absolutepath){
	String[] parts = absolutepath.split("/");
	setCurrentDir(getRootDirectory());
	for(int i=1; i < parts.length; i++){
		try{
			cd(parts[i]);
		}
		catch(NoSuchFileException e1){
			try{
				createDir(parts[i]);
				setCurrentDir((Directory) getCurrentDir().get(parts[i]));
			}
			catch(MyDriveException e2){}
		}
		catch(FileNotDirectoryException e){
			//FIXME: se houver xml errado mandar InvalidPathException
		}
	}
        return getCurrentDir();
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
        for (Element node : element.getChildren()) {
            if(!node.getName().equals("user")){
                _ids.add(Integer.parseInt(node.getAttribute("id").getValue()));  
            }
        }
    }
    
    
    public void xmlImport(Element element){
        reserveIds(element);
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
        setCurrentDir(getRootUser().getMainDirectory());
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

}
