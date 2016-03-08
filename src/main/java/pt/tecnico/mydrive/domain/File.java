package pt.tecnico.mydrive.domain;

import org.jdom2.Element;

import pt.tecnico.mydrive.exception.InvalidFileNameException;

import org.jdom2.Document;

public class File extends File_Base {
	
	public File(){}
    
	public File(String name, int id, User owner) throws InvalidFileNameException{
		super();
		try{
	        setOwner(owner);
	        setName(name);
	        setId(id);
	        setUserPermission(owner.getOwnPermission());
	        setOthersPermission(owner.getOthersPermission());
	        //lastChange updated (joda time)
	    }
		catch(InvalidFileNameException e)
		{
			//O nome do ficheiro n ´ ao pode conter os carateres ’/’ (barra) e ’\0’ (null)
		}
	}

	public File(String name, int id) throws InvalidFileNameException{
	    super();
	    //File(name, id, "root"); FIXME nao sei
	}
	
    public int dimension(){
    	return 0;
    }
    
    public void remove(){
    	//Just for directory
    }
    
    public void accept(Visitor v){
    	//TODO
    }
    
    public String toString(){
    	String s=getClass().getName();
    	return s;
    }
    
    public void xmlExport(Element element_mydrive){}
    

    public String ls(){
    	//Just for directory
		return null;
	}
    
}
