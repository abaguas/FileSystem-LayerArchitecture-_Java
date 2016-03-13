package pt.tecnico.mydrive.domain;

public class Permission extends Permission_Base {
    
    public Permission(boolean read, boolean write, boolean execute, boolean eliminate) {
        super();
        setRead(read);
        setWrite(write);
        setExecute(execute);
        setEliminate(eliminate);
    }
    public Permission(String mask){}//Fix me

    
    public String toString(){
    	String s ="";
    	if (getRead()){
    		s+="r";
    	}
    	else {
    		s+="-";
    	}
    	if (getWrite()){
    		s+="w";
    	}
    	else {
    		s+="-";
    	}
    	if (getExecute()){
    		s+="x";
    	}
    	else {
    		s+="-";
    	}
    	if (getEliminate()){
    		s+="d";
    	}
    	else {
    		s+="-";
    	}
    	return s;
    } 
}
