package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class Link extends Link_Base {
    //a primeira palavra representa o caminho para uma aplicacao e as restantes palavras representam os seus argumentos.
    public Link(String name, int id, User owner, String content) {
    	//super();
        super().super(name, id, owner, content);
        /*setOwner(owner);
        setName(name);
        setId(id);
        setContent(content);*/
    }
    public Link(String name, int id, String content) {
    	//super();
        super().super(name, id, content);
        /*setOwner(owner);
        setName(name);
        setId(id);
        setContent(content);*/
    }
    public int dimension(){
    	//How do I calculate?
    	return 0;
    }
    
    public String toString(){
    	String s="";
    	return s;
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