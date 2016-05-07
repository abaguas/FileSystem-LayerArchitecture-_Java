package pt.tecnico.mydrive.exception;

public class InvalidAppContentException extends MyDriveException {

	private static final long serialVersionUID = 1L;

    private String name;
    
    
    public InvalidAppContentException(String name) {
        this.name=name;
    }
	
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getMessage(){
        return getName() + " : Invalid App content.";
    }
	
}
