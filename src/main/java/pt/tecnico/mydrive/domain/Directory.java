package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

import java.util.Set;
import org.jdom2.Element;
import org.jdom2.Document;
import java.util.Set;
import pt.tecnico.mydrive.exception.*;

public class Directory extends Directory_Base {
    
    public static Directory newRootDir(RootUser user){
        Directory rootDir = FenixFramework.getDomainRoot().getMyDrive().getRootDirectory();
        if (rootDir != null)
            return rootDir;
        return new Directory((User)user);
    }
    
    private Directory(User user) {
        initRoot("/", 0, user, this);
        setSelfDirectory(this);
        setFatherDirectory(this);
    }
    
	public Directory(String name, int id, User user, Directory father) {
		super();
        init(name, id, user, father);
		init(father);
    }
	
	public void init(Directory father){
		setFatherDirectory(father);
        setSelfDirectory(this);
	}
	
	public Directory(String name, int id, User user) {
		super();
		init(name, id, user, this);
        init(this);
    }
	
    public Directory(Element directory_element, User owner, Directory father){
    	super();
    	XMLImport(directory_element, owner, father);
    }

	public void createFile(String name, String content, int id, User user, String code) throws FileAlreadyExistsException {
		try {
			search(name);
			throw new FileAlreadyExistsException(name, id);
		} 
		catch (NoSuchFileException e) {
			File f = fileFactory(name, content, id, user, code);
			addFiles(f);
		}
	}	

	public File fileFactory(String name, String content, int id, User user, String code){
        if(code.equals("PlainFile")){
            return new PlainFile(name, id, user, content, this);
        }
        else if(code.equals("App")){
            return new App(name, id, user, content, this);
        }
        else if(code.equals("Dir")){
        	return new Directory(name, id, user, this);
        }
        else{
            return new Link(name, id, user, content, this);
        }
    }

	public void remove(String name) throws NoSuchFileException{
		File f = search(name);
		System.out.println(f);
		removeFiles(f);
		f.remove();
	}
	
	public void remove() {
		Set<File> files = getFiles();
		for (File f: files) {
   	 		f.remove();
   	 		removeFiles(f);
   	 	}
	}
	
	public File get(String name) throws NoSuchFileException, FileNotDirectoryException{
		if (name.equals("..")) {
			return getFatherDirectory();
		}
		else if (name.equals(".")) {
			return getSelfDirectory();
		}
		else {
			File f = search(name);
   	 		return f;
		}
	}
	
	public File search(String name) throws NoSuchFileException{	
		Set<File> files = getFiles();
		
		for (File f: files) {
   	 		if (f.getName().equals(name)) {
   	 			return f;
   	 		}
		}
		throw new NoSuchFileException(name);
	}
	
	public String ls(){
		String output="";
		Set<File> files = getFiles();
	
		String name = getFatherDirectory().getName();
		getFatherDirectory().setName("..");
		output+=getFatherDirectory().toString()+"\n";
		getFatherDirectory().setName(name);
   	 	
		name = getSelfDirectory().getName();
		getSelfDirectory().setName(".");
		output+=getSelfDirectory().toString();
	 	getSelfDirectory().setName(name);
	 	
   	 	for (File f: files){
   	 		output+= "\n"+f.toString();
   	 	}
		return output;
	}
	
	public String toString(){
		String t = getClass().getSimpleName();
		t+=print();
    	return t;
	}
	
	public int dimension(){
		return 2 + getFiles().size();
	}
	
	public void accept(Visitor v) throws FileNotDirectoryException{
		v.execute(this);
	}
	
	public void XMLImport(Element directory_element,User user, Directory father){
		int id= Integer.parseInt(directory_element.getAttribute("id").getValue());
        String name = directory_element.getChildText("name");
        String perm= directory_element.getChildText("perm");
        if(perm == null){
            perm = "rwxd--x-";
        }
        Permission ownpermission = new Permission(perm.substring(0,4));
        Permission otherspermission = new Permission(perm.substring(4));
        init(name, id, user, father);
		init(father);
	}
	public boolean isHome(){
		if(getId() == getOwner().getMainDirectory().getId())
			return true;
		return false;
	} 

	public void XMLExport(Element element_mydrive){
        if(getId() > 2 && !isHome()){

        	Element element = new Element ("dir");
        	element.setAttribute("id", Integer.toString(getId()));
        
        	Element path_element = new Element ("path");
        	path_element.setText(getAbsolutePath());
        	element.addContent(path_element);

        	Element name_element = new Element ("name");
        	name_element.setText(getName());
        	element.addContent(name_element);

        	Element owner_element = new Element ("owner");
        	owner_element.setText(getOwner().getUsername());
        	element.addContent(owner_element);

        	Element perm_element = new Element ("perm");
        	perm_element.setText(getUserPermission().toString() + getOthersPermission().toString());
        	element.addContent(perm_element);
        	
        	Element lastChange_element = new Element ("lastChange");
        	lastChange_element.setText(getLastChange().toString());
        	element.addContent(lastChange_element);

        	element_mydrive.addContent(element);
        }

        for (File f: getFiles()){
            f.XMLExport(element_mydrive);
        }
    }

}
