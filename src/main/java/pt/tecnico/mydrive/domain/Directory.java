package pt.tecnico.mydrive.domain;

import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.jdom2.Element;
import org.jdom2.Document;
import java.util.Set;
import pt.tecnico.mydrive.exception.*;

public class Directory extends Directory_Base {

    public static Directory newRootDir(RootUser user){
  //FIXME     Directory rootDir = FenixFramework.getDomainRoot().getMyDrive().getRootDirectory();
  /*      if (rootDir != null)
            return rootDir; */
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
	public void remove(MyDrive md, long token) throws PermissionDeniedException {
		boolean allDeleted = true;
		md.checkPermissions(token, getName(), "create-delete", "delete"); //verify delete permission
		Set<File> files = getFiles();
		while (!files.isEmpty()){
			for (File f: files) {
				try {
					files.remove(f);
					md.cdable(f);
					md.checkPermissions(token, getName(), "read-write-execute", "execute"); //verify execute permission for other files
		   	 		f.remove(md, token);
		   	 	}catch (PermissionDeniedException pde){
		   	 		allDeleted = false;
		   	 	} catch (FileNotCdAbleException fncde){
		   	 		try{
		   	 			f.remove(md, token);
		   	 		}catch (PermissionDeniedException pde2){
		   	 			allDeleted = false;
		   	 		}
		   	 	}
			}
		}
		if (allDeleted) {
			setOwner(null);
	    	setUserPermission(null);
	        setOthersPermission(null);
	        setDirectory(null);
	        deleteDomainObject();
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

	public File search(String name) throws NoSuchFileException{
		Set<File> files = getFiles();

		for (File f: files) {
   	 		if (f.getName().equals(name)) {
   	 			return f;
   	 		}
		}
		throw new NoSuchFileException(name);
	}

	public String ls() {
		String output="";
		Set<File> files = getFiles();
		List<File> list = new ArrayList<File>(files);

		Collections.sort(list, new Comparator<File>() {
		    public int compare(File f1, File f2) {
		    	int compare=f1.getName().compareTo(f2.getName());
		    	return compare;
		    }
		});

		String name = getFatherDirectory().getName();
		getFatherDirectory().setName("..");
		output+=getFatherDirectory().toString()+"\n";
		getFatherDirectory().setName(name);

		name = getSelfDirectory().getName();
		getSelfDirectory().setName(".");
		output+=getSelfDirectory().toString();
	 	getSelfDirectory().setName(name);

	 	if (getName()!="/") {
	   	 	for (File f: list){
	   	 		output+= "\n"+f.toString();
	   	 	}
	 	}
		return output;
	}

	public List<String> lsList() {
		String output="";
		Set<File> files = getFiles();
		List<File> list = new ArrayList<File>(files);
		List<String> stringList = new ArrayList<String>();

		Collections.sort(list, new Comparator<File>() {
		    public int compare(File f1, File f2) {
		    	int compare=f1.getName().compareTo(f2.getName());
		    	return compare;
		    }
		});

		String name = getFatherDirectory().getName();
		getFatherDirectory().setName("..");
		stringList.add(getFatherDirectory().toString());
		getFatherDirectory().setName(name);

		name = getSelfDirectory().getName();
		getSelfDirectory().setName(".");
		stringList.add(getSelfDirectory().toString());
	 	getSelfDirectory().setName(name);

	  	for (File f: list){
	  		if (getName()!="/") {
	  			stringList.add(f.toString());
	  		}
	  	}
	 	
		return stringList;
	}

	public String toString(){
		String t = "Directory";
		t+=print();
    	return t;
	}

	public int dimension(){
		return 2 + getFiles().size();
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

        for (File f: getFiles()){
            f.xmlExport(element_mydrive);
        }
    }
}
