package pt.tecnico.mydrive.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
    }

	public Directory(String name, int id, User user, Directory father) {
        init(name, id, user, father);
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

	public void createFile(String name, String content, int id, User user, String code) throws FileAlreadyExistsException, MaximumPathException {
		try {
			search(name);
			throw new FileAlreadyExistsException(name, id);
		}
		catch (NoSuchFileException e) {
			validateFile(name);
			File f = fileFactory(name, content, id, user, code);
			addFiles(f);
		}
	}
	
	public void validateFile(String name){
		if ((getAbsolutePath().length() + name.length()) > 1024){
			throw new MaximumPathException(name);
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
	
	@Override
	public void remove(User user, Directory directory) throws PermissionDeniedException {
		
		checkPermissions(user, directory, getName(), "delete"); 
		
		Set<File> files = getFilesSet();
		
		for (File f: files) {
				checkPermissions(user, directory, f.getName(),"delete"); 
		}
		
		for (File f: files) {
				f.remove(user, directory); 
		}
		
		setOwner(null);
	    setUserPermission(null);
	    setOthersPermission(null);
	    setDirectory(null);
	    setSelfDirectory(null);
	    setFatherDirectory(null);
	    deleteDomainObject();
	}
	
	public void checkPermissions(User user, Directory directory, String fileName, String code)
			throws PermissionDeniedException {
		if (code.equals("create") || code.equals("delete")) {
			checkFileCreateDeletePermissions(user, directory, fileName, code);	
		}
		else if (code.equals("read") || code.equals("write") || code.equals("execute")) {
			checkFileReadWriteExecutePermissions(user, directory, fileName, code);
		}
		
		else if (code.equals("cd")) {
			checkChangeDirPermission(user, directory, fileName);
		}
		else if (code.equals("ls")) {
			checkListPermission(user, directory);
		}

	}

	public void checkFileCreateDeletePermissions(User user, Directory directory, String fileName, String access)
			throws PermissionDeniedException, ExpiredSessionException, InvalidTokenException {
	
		User owner = directory.getOwner();
		Permission dirOwnP = directory.getUserPermission();
		Permission dirOthP = directory.getOthersPermission();
		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (access.equals("delete")) {
				File f = directory.get(fileName);
				if (!f.getUserPermission().getEliminate()) {
					throw new PermissionDeniedException(fileName);
				}
			}
			if (!dirOwnP.getWrite()) {
				throw new PermissionDeniedException(fileName);
			}
		} else {
			if (access.equals("delete")) {
				File f = directory.get(fileName);
				if (!f.getOthersPermission().getEliminate()) {
					throw new PermissionDeniedException(fileName);
				}
			}
			if (!dirOthP.getWrite()) {
				throw new PermissionDeniedException(fileName);
			}
		}
	}

	public void checkFileReadWriteExecutePermissions(User user, Directory directory, String fileName, String access) {
		
		File f = directory.get(fileName);
		User owner = f.getOwner();
		Permission fileUserP = f.getUserPermission();
		Permission fileOthP = f.getOthersPermission();

		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (access.equals("read")) {
				if (!fileUserP.getRead()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("write")) {
				if (!fileUserP.getWrite()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("execute")) {
				if (!fileUserP.getExecute()) {
					throw new PermissionDeniedException(fileName);
				}
			}
		} else {
			if (access.equals("read")) {
				if (!fileOthP.getRead()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("write")) {
				if (!fileOthP.getWrite()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("execute")) {
				if (!fileOthP.getExecute()) {
					throw new PermissionDeniedException(fileName);
				}
			}
		}
	}

	public void checkChangeDirPermission(User user, Directory directory, String fileName) {

		User owner = directory.getOwner();
		Permission dirOwnP = directory.getUserPermission();
		Permission dirOthP = directory.getOthersPermission();

		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (!dirOwnP.getExecute()) {
				throw new PermissionDeniedException(fileName);
			}
		} else {
			if (!dirOthP.getExecute()) {
				throw new PermissionDeniedException(fileName);
			}
		}
	}

	public void checkListPermission(User user, Directory directory) {

		User owner = directory.getOwner();
		Permission dirOwnP = directory.getUserPermission();
		Permission dirOthP = directory.getOthersPermission();

		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (!dirOwnP.getRead()) {
				throw new PermissionDeniedException(directory.getName());
			}
		} else {
			if (!dirOthP.getRead()) {
				throw new PermissionDeniedException(directory.getName());
			}
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

	public boolean hasFile(String name){
		try{
			search(name);
			return true;
		}
		catch(NoSuchFileException e){
			return false;
		}
	}
	
	
	public File getDelete(String name) throws NoSuchFileException, NotDeleteAbleException{
		if (name.equals(".")){
          throw new NotDeleteAbleException("Cannot delete self directory");
		} 
		else if (name.equals(".")){
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
		Set<File> files = getFilesSet();
		
		try{
			for (File f: files) {
	   	 		if (f.getName().equals(name)) {
	   	 			return f;
	   	 		}
			}
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
		return 2 + getFilesSet().size();
	}

	public void accept(Visitor v) throws FileNotDirectoryException{
		v.execute(this);
	}

	public void xmlImport(Element directory_element,User user, Directory father){
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

	public void xmlExport(Element element_mydrive){
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

        for (File f: getFilesSet()){
            f.xmlExport(element_mydrive);
        }
    }

	@Override
	public void writeContent(String content) {
		throw new FileIsNotWriteAbleException(getName());
	}
}
