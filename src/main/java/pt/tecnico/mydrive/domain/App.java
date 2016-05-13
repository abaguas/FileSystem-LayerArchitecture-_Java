package pt.tecnico.mydrive.domain;

import org.jdom2.Element;

import pt.tecnico.mydrive.exception.InvalidAppContentException;
import pt.tecnico.mydrive.exception.NoArgumentsForAppExecutionException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

import java.lang.reflect.InvocationTargetException;
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
    
    
    public static void run(String name, String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class<?> cls;
        Method meth;
        try { // name is a class: call main()
          cls = Class.forName(name);
          meth = cls.getMethod("main", String[].class);
        } catch (ClassNotFoundException cnfe) { // name is a method
          int pos;
          if ((pos = name.lastIndexOf('.')) < 0) throw cnfe;
          cls = Class.forName(name.substring(0, pos));
          meth = cls.getMethod(name.substring(pos+1), String[].class);
        }
        meth.invoke(null, (Object)args); // static method (ignore return)
      }
    
    
    @Override
    public void execute(User caller, String[] args, MyDrive md) throws PermissionDeniedException {
    	
    	checkPermissions(caller, this, "execute");
    	
    	String name = this.getContent();
    	
    	if (args.length <= 0)
    		throw new NoArgumentsForAppExecutionException(getName());
	    		
    	try {
    		    run(name, args);
    	}	 
    	catch (Exception e) { throw new RuntimeException("" + e); }
    }
    	
    	
    @Override
    public String toString(){
    	String t = "App";
    	t+=print();
    	return t;
    }
    
    
    
//////////////////////////////////////////////////////////////////////////////////////
//                                          XML                               //
//////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void xmlImport(Element element, User owner, Directory father){
        super.xmlImport(element,owner,father);
        String contents= element.getChildText("method");
        setContent(contents);
    }

    public void xmlExport(Element element_mydrive){
        Element element = new Element ("app");
        element.setAttribute("id", Integer.toString(getId()));
        
        super.xmlExport(element);

        Element method_element = new Element ("method");
        method_element.setText(getContent());
        element.addContent(method_element);
        

        element_mydrive.addContent(element);
    }



    
}