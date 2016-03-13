package pt.tecnico.mydrive.domain;

public class Permission extends Permission_Base {
    
    public Permission(boolean read, boolean write, boolean execute, boolean eliminate) {
        super();
        setRead(read);
        setWrite(write);
        setExecute(execute);
        setEliminate(eliminate);
    }
    public Permission(String mask){
        super();
    }//Fix me

    
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
