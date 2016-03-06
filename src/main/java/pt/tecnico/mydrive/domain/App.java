package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class App extends App_Base {
    
    public App() {
        super();
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
