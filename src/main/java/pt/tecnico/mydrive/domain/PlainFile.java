package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile() {
        super();
    }
    
    @Override
    public void XMLExport(Element element_mydrive){
        Element element = new Element ("plain");
        //element.setAttribute("id",getId());
        
        element = new Element ("path");
        //element.setText(getPath());
        
        element = new Element ("name");
        element.setText(getName());
        
        element = new Element ("owner");
        element.setText(getOwner().getUsername());
        
        element = new Element ("perm");
        //element.setText(getPerm());
        
        element = new Element ("contents");
        element.setText (getContent());
        
        element_mydrive.addContent(element);
    }
    
}
