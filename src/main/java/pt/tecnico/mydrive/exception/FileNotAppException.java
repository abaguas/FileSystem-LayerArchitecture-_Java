package pt.tecnico.mydrive.exception;

public class FileNotAppException extends MyDriveException {
    
	private static final long serialVersionUID = 1L;

    private String name;

    public FileNotAppException(String name){
        this.name=name;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage(){
        return getName() + ": File is not an App";
    }
    
}
