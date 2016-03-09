package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class App extends App_Base {
    //o conteudo representa o nome completo de um metodo de uma classe Java
    public App(String name, int id, User owner, String content) {
    	super();
    	init(name,id,owner,content);
    }
    
    public App(String name, int id, String content) {
    	super();
    	//FIXME (root)

    }
    
    public void execute(){
    	//FIXME?
    }
  
    public String toString(){
    	String t = getClass().getName();
    	t+=print();
    	return t;
    }

    public void XMLExport(Element element_mydrive){
        Element element = new Element ("app");
        // element.setAttribute("id", getId());
        
        element = new Element ("path");
        //element.setText(getPath());
        
        element = new Element ("name");
        element.setText(getName());
        
        element = new Element ("owner");
        element.setText(getOwner().getUsername());
        
        element = new Element ("perm");
        //element.setText(getPerm());
        
        element = new Element ("method");
        //element.setText (getMethod());
        
        element_mydrive.addContent(element);
    }

    
}