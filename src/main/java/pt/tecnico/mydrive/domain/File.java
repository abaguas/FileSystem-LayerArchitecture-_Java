package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

public class File extends File_Base {
    
    public File() {
        super();
    }
    
    public void remove(){}
    
    public void accept(Visitor v){
    	//TODO
    }
    
    public String toString(){
    	String s="";
    	//TODO
    	return s;
    }
    
    public void XMLExport(Element element_mydrive){}
    
}
