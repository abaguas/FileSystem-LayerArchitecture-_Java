package pt.tecnico.mydrive.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;
import org.joda.time.DateTime;


import pt.tecnico.mydrive.exception.FileIsNotExecuteAbleException;
import pt.tecnico.mydrive.exception.FileNotAppException;
import pt.tecnico.mydrive.exception.ExpiredSessionException;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.FileIsNotReadAbleException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;

import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidExceptionCodeException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;


import pt.tecnico.mydrive.exception.InvalidPathException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public abstract class File extends File_Base {

	public File() {
	}

	public File(String name, int id, User owner, Directory father) throws InvalidFileNameException {
		init(name, id, owner, father);
	}

	protected void init(String name, int id, User owner, Directory father) throws InvalidFileNameException, FileAlreadyExistsException, MaximumPathException {
		if (name.contains("/") || name.contains("\0")) {
			throw new InvalidFileNameException(name);
		}		
		try {
			father.search(name);
			throw new FileAlreadyExistsException(name, id);
		}
		catch (NoSuchFileException e) {
			DateTime dt = new DateTime();
			setOwner(owner);
			setName(name);
			setId(id);
			setUserPermission(owner.getOwnPermission().copy());
			setOthersPermission(owner.getOthersPermission().copy());
			setLastChange(dt);
			setDirectory(father);
			checkPermissions(owner, father, "write");
		}
		validateFile(name);
	}

	
	public void validateFile(String name) throws MaximumPathException{
		if ((getAbsolutePath().length() + name.length()) > 1024){
			throw new MaximumPathException(name);
		}
	}

	protected void initRoot(String name, int id, User owner) throws InvalidFileNameException {
		DateTime dt = new DateTime();
		setOwner(owner);
		setName(name);
		setId(id);
		setUserPermission(owner.getOwnPermission().copy());
		setOthersPermission(owner.getOthersPermission().copy());
		setLastChange(dt);
	}

	public int dimension() {
		return 0;
	}

	public abstract void remove(User user) throws PermissionDeniedException;

	public abstract String read(User user, MyDrive md) throws FileIsNotReadAbleException;
	
	public abstract String read(User user, MyDrive md, Set<String> cycleDetector) throws FileIsNotReadAbleException;
	
	public abstract void write(User user, String content, MyDrive md)  throws FileIsNotWriteAbleException;
	
	public abstract void write(User user, String content, MyDrive md, Set<String> cycleDetector)  throws FileIsNotWriteAbleException;

	
	public abstract void execute(User caller, String[] args, MyDrive md);

	
	public String toString() {
		return print();
	}

	protected String print() {
		String s = " ";
		s += getUserPermission().toString();
		s += getOthersPermission().toString() + " ";
		s += dimension() + " ";
		s += getOwner().getUsername() + " ";
		s += getId() + " ";
		s += getLastChange() + " ";
		s += getName();
		return s;
	}

	public String getAbsolutePath() {
		if(getName().equals("/")){
			return "";
		}
		return getDirectory().getAbsolutePath() + "/" + getName();
	}

	public void accept(Visitor v) {
		v.execute(this);
	}
	
	//code: read, write, execute
	public void checkPermissions(User user, File file, String code) throws InvalidExceptionCodeException, PermissionDeniedException {
		//o root faz tudo
		if (user.getUsername().equals("root")){ }
		else {
			Permission permission = null;
			//se for o owner aplica as permissoes de owner 
			if (user.getUsername().equals(file.getOwner().getUsername())) {
				permission = file.getUserPermission();
			}
			//se for other aplica as permissoes de other
			else permission = file.getOthersPermission();

			//depois de saber que permissoes aplicar vê o code
			if (code.equals("read")) {
				if (!permission.getRead()) {
					throw new PermissionDeniedException(file.getName());
				}
			}
			else if (code.equals("write")) {
				if (!permission.getWrite()) {
					throw new PermissionDeniedException(file.getName());
				}
			}
			else if (code.equals("execute")) {
				if (!permission.getExecute()) {
					throw new PermissionDeniedException(file.getName());
				}
			}
			else throw new InvalidExceptionCodeException(code);
		}
	}
	//A operaçao de remocao é a única que precisa de autorização do diretorio no qual o ficheiro está inserido
	public void checkPermissionsRemove(User user, Directory directory, File file) throws PermissionDeniedException {
		if (user.getUsername().equals("root")){
			return;
		}
		else {
			Permission permissionDir = null;
			Permission permissionFile = null;
			//aplico as permissoes de other ou owner consoante o user para o file
			if (user.getUsername().equals(file.getOwner().getUsername())) {
				permissionFile = file.getUserPermission();
			}
			else permissionFile = file.getOthersPermission();
			
			//e o mesmo para o diretorio			
			if (user.getUsername().equals(directory.getOwner().getUsername())) {
				permissionDir = directory.getUserPermission();
			}
			else permissionDir = directory.getOthersPermission();
			
			//verifico as permissoes para os dois
			if (!permissionDir.getEliminate()) {
				throw new PermissionDeniedException(directory.getName());
			}
			if (!permissionFile.getEliminate()) {
				throw new PermissionDeniedException(file.getName());
			}
		}
	}
		
    public String pwd(){
    	Directory d= getDirectory();
        String output="";
        if(d.getName().equals("/")){
            output="/";
        }
        else{
            while(!d.getName().equals("/")){
                output = "/" + d.getName() + output;
                d = d.getFatherDirectory();
            }
        }
        return output;
    }
    
    public File getFileByPath(User user, String path, Directory dir, MyDrive md) throws PermissionDeniedException, InvalidPathException, FileNotCdAbleException {
    	List<String> elements = new ArrayList<String> (Arrays.asList(path.split("/")));	
        elements.remove("");
        String[] parts = elements.toArray(new String[0]);

        File aux;
        int i = 0;
        int numOfParts = parts.length;
        
        if (path.charAt(0)=='/') {
            dir = md.getRootDirectory();
        }
        else if(numOfParts == 0){
            throw new InvalidPathException(path);
        }
        while(i < numOfParts-1){
            aux = dir.get(parts[i]);
            cdable(aux);
            checkPermissions(user, aux, "execute");
            dir = (Directory)aux;
            i++;
        }
        return dir.get(parts[numOfParts-1]);        
    }
    
    public Directory getDirectoryByPath(User user, String path, Directory dir, MyDrive md){
    	File file = getFileByPath(user, path, dir, md);
    	cdable(file);
    	checkPermissions(user, file, "execute");
    	return (Directory) file;
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

    public String ls(){
		return null;
    }
    

//////////////////////////////////////////////////////////////////////////////////////
//                                   XML                               //
//////////////////////////////////////////////////////////////////////////////////////

    public void xmlExport(Element element){
    	Element path_element = new Element ("path");
        path_element.setText(getAbsolutePath());
        element.addContent(path_element);

        Element name_element = new Element ("name");
        name_element.setText(getName());
        element.addContent(name_element);

        Element owner_element = new Element ("owner");
        owner_element.setText(getOwner().getUsername());
        element.addContent(owner_element);

        Element permission_element = new Element ("perm");
        permission_element.setText(getUserPermission().toString() + getOthersPermission().toString());
        element.addContent(permission_element);

        Element lastChange_element = new Element ("lastChange");
    	lastChange_element.setText(getLastChange().toString());
    	element.addContent(lastChange_element);
    }

    public void xmlImport(Element element, User user, Directory father){
		int id= Integer.parseInt(element.getAttribute("id").getValue());
        String name = element.getChildText("name");
        String perm= element.getChildText("perm");
        if(perm == null){
            perm = "rwxd--x-";
        }
        Permission ownpermission = new Permission(perm.substring(0,4));
        Permission otherspermission = new Permission(perm.substring(4));
        init(name, MyDrive.getInstance().generateId(), user, father);
	}

}
