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
        if(mask.substring(0,1).equals("-")){
            setRead(false);
        }
        else{
            setRead(true);
        }
        if(mask.substring(1,2).equals("-")){
            setWrite(false);
        }
        else{
            setWrite(true);
        }
        if(mask.substring(2,3).equals("-")){
            setExecute(false);
        }
        else{
            setExecute(true);
        }
        if(mask.substring(3,4).equals("-")){
            setEliminate(false);
        }
        else{
            setEliminate(true);
        }
    }

    
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
