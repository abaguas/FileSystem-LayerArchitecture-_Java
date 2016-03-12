package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.LocalTime;

import pt.tecnico.mydrive.exception.InvalidFileNameException;

import org.jdom2.Document;

public class File extends File_Base {
	
	public File(){}
    
	public File(String name, int id, User owner) throws InvalidFileNameException{
		super();
		init(name,id,owner);
	}

	public File(String name, int id) throws InvalidFileNameException{
	    super();
	    //FIXME (root)
	}
	
	//Enables inheritance
	protected void init(String name, int id, User owner) throws InvalidFileNameException{

		if(name.contains("/") || name.contains("0")){
			throw new InvalidFileNameException(name);
		}
		
		LocalTime dt=new LocalTime();
        setOwner(owner);
        setName(name);
        setId(id);
        setUserPermission(owner.getOwnPermission());
        setOthersPermission(owner.getOthersPermission());
        setLastChange(dt);
	}
	
    public int dimension(){
    	return 0;
    }

    protected void remove() {
    	setOwner(null);
        setUserPermission(null);
        setOthersPermission(null);
        setLastChange(null);
    	deleteDomainObject();
    }
    
    public void accept(Visitor v){
    	//TODO
    }
    
    public String toString(){	
    	return print();
    }
    
    protected String print(){
    	String s=" ";
    	s+=getUserPermission().toString()+" ";
    	s+=getOthersPermission().toString()+" ";
    	s+=dimension()+" ";
    	//s+=getOwner().getUsername()+" ";
    	s+=getId()+" ";
    	s+=getLastChange()+" ";
    	s+=getName();
    	return s;
    }
    
    public void xmlExport(Element element_mydrive){}
    

    public String ls(){
		return null;
	}
    
}
