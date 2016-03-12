package pt.tecnico.mydrive.domain;

public class Permission extends Permission_Base {
    
    public Permission(boolean read, boolean write, boolean execute, boolean eliminate) {
        super();
        setRead(read);
        setWrite(write);
        setExecute(execute);
        setEliminate(eliminate);
    }
<<<<<<< HEAD
    public Permission(String mask){}//Fix me

    public String toString(){
    	return "" + getRead() + getWrite() + getExecute() + getEliminate(); 
    }  
=======
    
    public Permission(String mask){}//Fix me  
>>>>>>> a37da16256149ead6bb99537556664c0b437c9f9
    
    public String toString(){
    	String s ="";
    	if (getRead()){
    		s+="r";
    	}
    	if (getWrite()){
    		s+="w";
    	}
    	if (getExecute()){
    		s+="x";
    	}
    	if (getEliminate()){
    		s+="d";
    	}
    	return s;
    } 
    
}
