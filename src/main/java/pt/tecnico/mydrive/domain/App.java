package pt.tecnico.mydrive.domain;

import org.jdom2.Element;

import pt.tecnico.mydrive.exception.InvalidAppContentException;

import java.lang.reflect.Method;

import org.jdom2.Document;

public class App extends App_Base {
    public App(String name, int id, User owner, String content, Directory father) {
    	init(name,id,owner,content, father);
    }
    
    public App(String name, int id, String content) {

    }
    public App(Element app_element, User owner, Directory father){
        xmlImport(app_element, owner, father);

    }
    

    public void execute() {
    	String fullMethod = getContent();
    	String[] methodParts=fullMethod.split(".");
    	
    	if(methodParts.length==3) {
    		String className = methodParts[0] + "." + methodParts[1];
    		//Class<?> c = Class.forName(className);
    		
    		
    	}
    	else if(methodParts.length==2) {
    		String className = methodParts[0] + "." + methodParts[1];
    		
    		
    	}
    	else {
    		throw new InvalidAppContentException(fullMethod);
    	}
    	
    	

    	
    	
    }

    /*
    public void execute(User u, String fileName) {
    	checkPermissions(u, this, "execute");
    	String fullMethod = getContent();
    	String[] methodParts=fullMethod.split(".");
    	
    	String fullClass;
    	for(int i = 0; i < methodParts.length - 2 ; i++) {
    		fullClass += methodParts[i] + ".";
    	}

    	
    	Class<?> cls;
    	Method meth;
    	
    	try { // name is a class: call main()
    		cls = Class.forName(name);
    		meth = cls.getMethod("main", String[].class);
    	}
    	catch (ClassNotFoundException cnfe) { // name is a method
    		int pos;
    		if ((pos = name.lastIndexOf('.')) < 0) throw cnfe;
    		cls = Class.forName(name.substring(0, pos));
    		meth = cls.getMethod(name.substring(pos+1), String[].class);
    	}
    	meth.invoke(null, (Object)args); // static method (ignore return)
    	
    }
    */
    
    @Override
    public String toString(){
    	String t = "App";
    	t+=print();
    	return t;
    }
    
//////////////////////////////////////////////////////////////////////////////////////
//                                          XML                               //
//////////////////////////////////////////////////////////////////////////////////////


    public void xmlImport(Element app_element, User owner, Directory father){
        int id= Integer.parseInt(app_element.getAttribute("id").getValue());
        String name = app_element.getChildText("name");
        String perm= app_element.getChildText("perm");
        if(perm == null){
            perm = "rwxd--x-";
        }
        String contents= app_element.getChildText("method");
        Permission ownpermission = new Permission(perm.substring(0,4));
        Permission otherspermission = new Permission(perm.substring(4));
        init(name, id, owner, contents, father);
        setUserPermission(ownpermission);
        setOthersPermission(otherspermission);
    }

    public void xmlExport(Element element_mydrive){
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
        
        Element lastChange_element = new Element ("lastChange");
    	lastChange_element.setText(getLastChange().toString());
    	element.addContent(lastChange_element);

        element_mydrive.addContent(element);
    }



    
}