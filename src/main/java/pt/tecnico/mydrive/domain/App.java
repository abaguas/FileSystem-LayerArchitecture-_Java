package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class App extends App_Base {
    //o conteudo representa o nome completo de um metodo de uma classe Java
    public App(String name, int id, User owner, String content, Directory father) {
    	super();
    	init(name,id,owner,content, father);
    }
    
    public App(String name, int id, String content) {
    	super();
    	//FIXME (root)

    }
    public App(Element app_element, User owner, Directory father){
        super();
        setOwner(owner);
        setDirectory(father);
        XMLImport(app_element);

    }
    
    public void execute(){
    	//FIXME?
    }
  
    public String toString(){
    	String t = getClass().getSimpleName();
    	t+=print();
    	return t;
    }

    public void XMLImport(Element app_element){
        int id= Integer.parseInt(app_element.getAttribute("id").getValue());
        String name = app_element.getChildText("name");
        String perm= app_element.getChildText("perm");
        String contents= app_element.getChildText("methods");
        Permission ownpermission = new Permission(app_element.getChildText("perm").substring(0,4));
        Permission otherspermission = new Permission(app_element.getChildText("perm").substring(4));
        setName(name);
        setId(id);
        setUserPermission(ownpermission);
        setOthersPermission(otherspermission);
        setContent(contents);

    }

    public void XMLExport(Element element_mydrive){
        Element element = new Element ("app");
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

        Element method_element = new Element ("method");
        method_element.setText(getContent());
        element.addContent(method_element);

        element_mydrive.addContent(element);
    }

    
}