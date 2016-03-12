package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class PlainFile extends PlainFile_Base {
	
    //a primeira palavra representa o caminho para uma aplicacao e as restantes palavras representam os seus argumentos.
    public PlainFile(String name, int id, User owner, String content) {
    	super();
    	init(name,id,owner,content);
    }
    public PlainFile(String name, int id, String content) {
    	super();
    	//FIXME (root)
    }
    public PlainFile(Element plain_element, User user){
        super();
        setOwner(user);
        xmlImport(plain_element);
    }
    
    public void execute(){
    	//FIXME?
    }
  
    public String toString(){
    	String t = getClass().getSimpleName();
    	t+=print();
    	return t;
    }
    
    public void xmlImport(Element plain_element){
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
    public void xmlExport(Element element_mydrive){
        Element element = new Element ("link");
        //element.setAttribute("id",getId());
        
        element = new Element ("path");
        //element.setText(getPath());
        
        element = new Element ("name");
        element.setText(getName());
        
        element = new Element ("owner");
        element.setText(getOwner().getUsername());
        
        element = new Element ("perm");
        //element.setText(getPerm());
        
        element = new Element ("value");
        //element.setText (getValue());
        
        element_mydrive.addContent(element);
    }    
}
