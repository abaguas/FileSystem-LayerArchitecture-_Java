package pt.tecnico.mydrive.exception;

public class InvalidFileNameException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _name;
    
    public InvalidFileNameException(String name){
        _name=name;
    }
    
	
    public String getName() {
        return _name;
    }
    
    @Override
    public String getMessage(){
        return getName() + " : Invalid File Name.";
    }
}