package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class Link extends Link_Base {
    //o conteudo representa o caminho (absoluto ou relativo) para outro ficheiro
    public Link(String name, int id, User owner, String content) {
    	super();
    	init(name,id,owner,content);
    }
    public Link(String name, int id, String content) {
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