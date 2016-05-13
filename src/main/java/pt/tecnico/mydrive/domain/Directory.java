package pt.tecnico.mydrive.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.jdom2.Element;
import pt.tecnico.mydrive.exception.*;

public class Directory extends Directory_Base {

    public static Directory newRootDir(RootUser user){
        return new Directory((User)user);
    }

    private Directory(User user) {
        initRoot("/", 0, user);
        setSelfDirectory(this);
        setFatherDirectory(this);
        setOthersPermission(new Permission(true,false,true,false));
    }

	public Directory(String name, int id, User user, Directory father) {
        init(name, id, user, father);
		init(father);
    }

    public Directory(String name, User user, Directory father) {
        init(name, MyDrive.getInstance().generateId(), user, father);
		init(father);
    }

	public void init(Directory father){
		setFatherDirectory(father);
        setSelfDirectory(this);
	}

	public Directory(String name, int id, User user) {
		init(name, id, user, this);
        init(this);
    }

    public Directory(Element directory_element, User owner, Directory father){
    	xmlImport(directory_element, owner, father);
    }
    
    @Override
    public Set<File> getFilesSet() throws InvalidOperationException {
    	throw new InvalidOperationException("getFilesSet");
    }
    
    public Set<File> getFilesSet(User user) throws PermissionDeniedException {
    	checkPermissions(user, this, "read");
    	return super.getFilesSet();
    }

	
	@Override
	public void remove(User user) throws PermissionDeniedException {
		
		checkPermissionsRemove(user, getDirectory(), this); 
		
		Set<File> files = super.getFilesSet();
		
		for (File f: files) {
			try {
				f.remove(user);
			} catch (PermissionDeniedException pde) {}
		}
		
		setOwner(null);
	    setUserPermission(null);
	    setOthersPermission(null);
	    setDirectory(null);
	    setSelfDirectory(null);
	    setFatherDirectory(null);
	    deleteDomainObject();
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

	public Directory getDirectory(String path) throws NoSuchFileException, FileNotDirectoryException{
		if (isRelativeAbsolute(path)) {
			String[] parts = path.split("/");
			String rest = "";
			int i = 0;
			for(i = 1; i < parts.length - 1; i++) {
				rest = rest + parts[i] + "/";
			}
			rest = rest + parts[i];
			File next = get(parts[0]);
			if(next instanceof Directory){
				Directory d = (Directory)next;
				return d.getDirectory(rest);
			}
			throw new FileNotDirectoryException(next.getName());
		}
		else {
   	 		File f = get(path);
   	 		if(f instanceof Directory){
   	 			return (Directory)f;
   	 		}
   	 		throw new FileNotDirectoryException(f.getName());
		}
	}

	private boolean isAbsolute(String path){
		return isRelativeAbsolute(path) && path.startsWith("/");
	}

	private boolean isRelativeAbsolute(String path){
		return path.contains("/");
	}

	public boolean hasFile(String name){
		try{
			search(name);
			return true;
		}
		catch(NoSuchFileException e){
			return false;
		}
	}
	
	
	public File getDeletable(String name) throws NoSuchFileException, NotDeleteAbleException{
		if (name == null){
			throw new NoSuchFileException(name);
		}
		if (name.equals(".")){
          throw new NotDeleteAbleException("Cannot delete self directory");
		} 
		else if (name.equals("..")){
			throw new NotDeleteAbleException("Cannot delete father directory");
		} 
		else if (name.equals("/")){
			throw new NotDeleteAbleException("Cannot delete root directory");
		}

		else {
			File f = search(name);
			if(f.getOwner().getMainDirectory().getId() == f.getId()){
				throw new NotDeleteAbleException("Cannot delete User's home despite permissions");
			}
			else{
   	 		 	return f;
			}
		}
	}
	
	public File search(String name) throws NoSuchFileException{
		Set<File> files = super.getFilesSet();
		
		try{
			for (File f: files) {
	   	 		if (f.getName().equals(name)) {
	   	 			return f;
	   	 		}
			}
			if(name.equals(""))
				throw new NoSuchFileException("empty string");
			else
				throw new NoSuchFileException(name);
		}
		catch(NullPointerException e){
			throw new NoSuchFileException("Invalid File name: null");
		}
	}
	
	public String toString(){
		String t = "Directory";
		t+=print();
    	return t;
	}

	public int dimension(){
		return 2 + super.getFilesSet().size();
	}

	@Override
	public String read(User user, MyDrive md) throws FileIsNotReadAbleException{
		throw new FileIsNotReadAbleException(getName());
	}

	@Override
	public String read(User user, MyDrive md, Set<String> set) throws FileIsNotReadAbleException{
		throw new FileIsNotReadAbleException(getName());
	}

	@Override
	public void write(User user, String content, MyDrive md) throws FileIsNotWriteAbleException{
		throw new FileIsNotWriteAbleException(getName());
		
	}

	@Override
	public void write(User user, String content, MyDrive md, Set<String> cycleDetector)  throws FileIsNotWriteAbleException{
		throw new FileIsNotWriteAbleException(getName());	
	}
	
	@Override
	public void execute(User user, String[] args, MyDrive md) throws FileIsNotExecuteAbleException {
		throw new FileIsNotExecuteAbleException(getName());
	}
	
	@Override
	public void execute(User caller, String[] args, MyDrive md, Set<String> cycleDetector) {
		throw new FileIsNotExecuteAbleException(getName());
	}

	public void accept(Visitor v) throws FileNotDirectoryException{
		v.execute(this);
	}
	
	public boolean isHome(){
		if(getId() == getOwner().getMainDirectory().getId())
			return true;
		return false;
	}

	public boolean isRoot(){
		if(getName().equals("/"))
			return true;
		return false;
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////
//                                   XML                               //
//////////////////////////////////////////////////////////////////////////////////////


	public void xmlExport(Element element_mydrive){
        if(getId() > 2 && !isHome() && !isRoot()){

        	Element element = new Element ("dir");
        	element.setAttribute("id", Integer.toString(getId()));

        	super.xmlExport(element);

        	element_mydrive.addContent(element);
        }

        for (File f: super.getFilesSet()){
            f.xmlExport(element_mydrive);
        }
	}



}
