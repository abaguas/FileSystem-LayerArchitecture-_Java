package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class Link extends Link_Base {
    
    public Link() {
        super();
    }
    
    @Override
    public void XMLExport(Element element_mydrive){
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
