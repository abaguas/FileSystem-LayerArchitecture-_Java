package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

import java.util.Set;

public class PlainFile extends PlainFile_Base {
	
    public PlainFile(String name, int id, User owner, String content, Directory father) {
    	init(name,id,owner,content, father);
    }
    public PlainFile(String name, int id, String content) {
    }
    public PlainFile(Element plain_element, User user, Directory father){
        xmlImport(plain_element, user, father);
    }

    public PlainFile() {};
    protected void init(String name, int id, User owner, String content, Directory father){
    	init(name,id,owner, father);
    	setContent(content);
    }
    
    public void addContent(String content){
    	DateTime lt = new DateTime();
    	setLastChange(lt);
    	String t = getContent();
    	t+="\n"+content; 
    	setContent(t);
    }
    
    public void execute() {
    }
    
    public int dimension(){
    	return getContent().length();
    }

    @Override
	public void remove(User user, Directory directory) throws PermissionDeniedException {
    	checkPermissions(user, directory, getName(), "delete"); 
    	setOwner(null);
		setUserPermission(null);
		setOthersPermission(null);
		setDirectory(null);
		deleteDomainObject();
	}
    
    @Override
	public void accept(Visitor v) {
		v.execute(this);
	}
 
    public String toString(){
    	String t = "PlainFile";
    	t+=print();
    	return t;
    }

	public String ls(){
		return getContent();
	}
	
	@Override
	public String read(User user, MyDrive md) throws PermissionDeniedException {
		checkPermissions(user, getDirectory(), getName(), "read");
		return this.getContent();
	}
	
	@Override
	public String read(User user, MyDrive md, Set<String> set){
		checkPermissions(user, getDirectory(), getName(), "read");
		return read(user, md);
	}
	
	@Override
	public void write(User user, String content, MyDrive md) throws FileIsNotWriteAbleException {
		checkPermissions(user, getDirectory(), getName(), "write");
		setContent(content);
	}
	
	@Override
	public void write(User user, String content, MyDrive md, Set<String> cycleDetector)
			throws FileIsNotWriteAbleException {
		checkPermissions(user, getDirectory(), getName(), "write");
		setContent(content);	
	}

//////////////////////////////////////////////////////////////////////////////////////
//                                   XML                               //
//////////////////////////////////////////////////////////////////////////////////////
    
    public void xmlImport(Element plain_element, User user, Directory father){
        int id= Integer.parseInt(plain_element.getAttribute("id").getValue());
        String name = plain_element.getChildText("name");
        String perm= plain_element.getChildText("perm");
        String contents= plain_element.getChildText("contents");
        if(perm == null){
            perm = "rwxd--x-";
        }
        Permission ownpermission = new Permission(perm.substring(0,4));
        Permission otherspermission = new Permission(perm.substring(4));
        init(name,id,user,contents,father);
    }
    
    @Override
    public void xmlExport(Element element_mydrive ){
        Element element = new Element ("plain");
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

        Element permission_element = new Element ("perm");
        permission_element.setText(getUserPermission().toString() + getOthersPermission().toString());
        element.addContent(permission_element);

        Element value_element = new Element ("contents");
        value_element.setText(getContent());
        element.addContent(value_element);

        Element lastChange_element = new Element ("lastChange");
    	lastChange_element.setText(getLastChange().toString());
    	element.addContent(lastChange_element);

    	element_mydrive.addContent(element);
    }
 
}
