package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class PlainFile extends PlainFile_Base {
	
    //a primeira palavra representa o caminho para uma aplicacao e as restantes palavras representam os seus argumentos.
    public PlainFile(String name, int id, User owner, String content, Directory father) {
    	super();
    	init(name,id,owner,content, father);
    }
    public PlainFile(String name, int id, String content) {
    	super();
    	//FIXME (root)
    }
    public PlainFile(Element plain_element, User user, Directory father){
        super();
        setOwner(user);
        setDirectory(father);
        XMLImport(plain_element);
    }
    
    public void execute(){
    	//FIXME?
    }
  
    public String toString(){
    	String t = getClass().getName();
    	t+=print();
    	return t;
    }
    
    public void XMLImport(Element plain_element){
        int id= Integer.parseInt(plain_element.getAttribute("id").getValue());
        String name = plain_element.getChildText("name");
        String perm= plain_element.getChildText("perm");
        String contents= plain_element.getChildText("contents");
        Permission ownpermission = new Permission(plain_element.getChildText("perm").substring(0,4));
        Permission otherspermission = new Permission(plain_element.getChildText("perm").substring(4));
        setName(name);
        setId(id);
        setUserPermission(ownpermission);
        setOthersPermission(otherspermission);
        setContent(contents);
    }
    
    @Override
    public void XMLExport(Element element_mydrive){
        Element element = new Element ("link");
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

        Element value_element = new Element ("value");
        value_element.setText(getContent());
        element.addContent(value_element);

        element_mydrive.addContent(element);
    }    
}
