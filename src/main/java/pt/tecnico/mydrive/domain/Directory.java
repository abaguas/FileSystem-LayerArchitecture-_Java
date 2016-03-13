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
    
    private Directory(User user){
        init("a", 0, user, this);
        setName("/");
        setSelfDirectory(this);
        setFatherDirectory(this);
    }
    
	public Directory(String name, int id, User user, Directory father) {
        init(name, id, user, father);
		setFatherDirectory(father);
        setSelfDirectory(this);
        setFatherDirectory(father);
        setSelfDirectory(this);
	}
	
    public Directory(Element directory_element, User owner, Directory father){
    	super();
    	setFatherDirectory(father);
    	setOwner(owner);
    	XMLImport(directory_element);
    }

	public void createDir(String name, int id, User user) throws FileAlreadyExistsException {
		try {
			search(name);
			throw new FileAlreadyExistsException(name);
		} 
		catch (NoSuchFileException e) {
			Directory d = new Directory(name, id, user, this);
			addFiles(d);
		}
		
	}

	public void remove(String name) throws NoSuchFileException{
		File f = search(name);
		f.remove();
		removeFiles(f);
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
	
	//lista o conteudo da diretoria
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
	
	//devolve a descricao da diretoria
	public String toString(){
		String t = getClass().getSimpleName();
		t+=print();
    	return t;
	}
	
	//devolve a dimensao da diretoria
	public int dimension(){
		return 2 + getFiles().size();
	}
	
	public void accept(Visitor v) throws FileNotDirectoryException{
		v.execute(this);
	}
	
	public void XMLImport(Element directory_element){
		int id= Integer.parseInt(directory_element.getAttribute("id").getValue());
        String name = directory_element.getChildText("name");
        String perm= directory_element.getChildText("perm");
        Permission ownpermission = new Permission(directory_element.getChildText("perm").substring(0,4));
        Permission otherspermission = new Permission(directory_element.getChildText("perm").substring(4));
        setName(name);
        setId(id);
        setUserPermission(ownpermission);
        setOthersPermission(otherspermission);
	}

	public void XMLExport(Element element_mydrive){
        if(getId() != 3){

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

        	element_mydrive.addContent(element);
        }

        for (File f: getFiles()){
            f.XMLExport(element_mydrive);
        }
    }

}
