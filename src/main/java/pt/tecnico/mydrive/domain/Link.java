package pt.tecnico.mydrive.domain;

import org.jdom2.Element;

import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;
import pt.tecnico.mydrive.exception.InvalidLinkContentException;
import pt.tecnico.mydrive.exception.InvalidPathException;
import pt.tecnico.mydrive.exception.LinkWithCycleException;
import pt.tecnico.mydrive.exception.LinkWithoutContentException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

import java.util.Set;
import java.util.TreeSet;

public class Link extends Link_Base {

    public Link(String name, int id, User owner, String content, Directory father) {
    	initLink(name, content);
    	init(name,id,owner,content, father);
    }

    public Link(String name, User owner, String content, Directory father) {
        initLink(name,content);
        init(name,MyDrive.getInstance().generateId(),owner,content, father);
    }
    
    public Link(Element link_element, User owner, Directory father){
        xmlImport(link_element, owner, father);
    }
    

    public void initLink(String name, String content){
        if(content.equals("")){
            throw new LinkWithoutContentException(name);
        }
        else if(content.length()>1024){
            throw new MaximumPathException(name);
        }
    }




    
    @Override
    public String toString(){
    	String t = "Link";
    	t+=print()+"->"+getContent();
    	return t;
    }
    
    public String ls(){
		return getContent();
	}
    
 	public String read(User user, MyDrive md)  throws LinkWithCycleException {
    	Set<String> cycleDetector = new TreeSet<String>();
    	checkPermissions(user, this, "read");
    	File f = getFileByPathWithLinkException(user, md);
    	if (!cycleDetector.add(f.pwd())){
    		throw new LinkWithCycleException(f.getName());
    	}
    	return f.read(user, md, cycleDetector);
    }
   	
    @Override
   	public String read(User user, MyDrive md, Set<String> cycleDetector) throws LinkWithCycleException {
    	checkPermissions(user, this, "read");
    	File f = getFileByPathWithLinkException(user, md);
    	if (!cycleDetector.add(f.pwd())){
    		throw new LinkWithCycleException(f.getName());
    	}
    	return f.read(user, md);
    }
   	
   	@Override
	public void write(User user, String content, MyDrive md) throws InvalidLinkContentException, FileIsNotWriteAbleException, LinkWithCycleException {
   		Set<String> cycleDetector = new TreeSet<String>();
    	checkPermissions(user, this, "write");
    	File f = getFileByPathWithLinkException(user, md);
    	if (!cycleDetector.add(f.pwd())){
    		throw new LinkWithCycleException(f.getName());
    	}
    	f.write(user, content, md, cycleDetector);
	}
	
	@Override
	public void write(User user, String content, MyDrive md, Set<String> cycleDetector) throws FileIsNotWriteAbleException, LinkWithCycleException {
		checkPermissions(user, this, "write");
		File f = getFileByPathWithLinkException(user, md);
    	if (!cycleDetector.add(f.pwd())){
    		throw new LinkWithCycleException(f.getName());
    	}
    	f.write(user, content, md, cycleDetector);
	}
	
    @Override
    public void execute(User caller, String[] args, MyDrive md) throws  NoSuchFileException, FileNotDirectoryException, PermissionDeniedException {
    	Set<String> cycleDetector = new TreeSet<String>();
    	checkPermissions(caller, this, "execute");
    	File f = getFileByPathWithLinkException(caller, md);
    	if (!cycleDetector.add(f.pwd())){
    		throw new LinkWithCycleException(f.getName());
    	}
    	f.execute(caller, args, md, cycleDetector); 
    }
    
	@Override
	public void execute(User caller, String[] args, MyDrive md, Set<String> cycleDetector) throws  NoSuchFileException, FileNotDirectoryException, PermissionDeniedException, LinkWithCycleException {
		checkPermissions(caller, this, "execute");
    	File f = getFileByPathWithLinkException(caller, md);
    	if (!cycleDetector.add(f.pwd())){
    		throw new LinkWithCycleException(f.getName());
    	}
    	f.execute(caller, args, md, cycleDetector); 
	}
    
	
	public File getFileByPathWithLinkException (User user, MyDrive md) throws PermissionDeniedException, InvalidLinkContentException{
		File f = null;
		try {
    		f = getFileByPath(user, getContent(), getDirectory(), md);
    	} catch (PermissionDeniedException pde) {
    		throw pde;
    	} catch (Exception e) {
    		throw new InvalidLinkContentException(getName());
    	}
		return f;
	}

       
//////////////////////////////////////////////////////////////////////////////////////
//                                   XML                               //
//////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void xmlImport(Element element, User owner, Directory father){
        super.xmlImport(element,owner,father);
        String contents= element.getChildText("value");
        setContent(contents);
    }
    
    @Override
    public void xmlExport(Element element_mydrive){
        Element element = new Element ("link");
        element.setAttribute("id",Integer.toString(getId()));
        
        super.xmlExport(element);

        Element value_element = new Element ("value");
        value_element.setText(getContent());
        element.addContent(value_element);

        element_mydrive.addContent(element);
    }
    
}