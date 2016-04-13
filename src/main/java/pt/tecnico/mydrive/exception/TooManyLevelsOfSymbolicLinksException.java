package pt.tecnico.mydrive.exception;

public class TooManyLevelsOfSymbolicLinksException extends MyDriveException {
	private static final long serialVersionUID = 1L;
    
    private String _name;
    
    public TooManyLevelsOfSymbolicLinksException(String name){
        _name=name;
    }
    
    public String getName() {
        return _name;
    }
    
    @Override
    public String getMessage(){
        return getName() + " : has too many indirections (links) in it's path";
    }
}
