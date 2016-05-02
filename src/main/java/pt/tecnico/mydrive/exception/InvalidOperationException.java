package pt.tecnico.mydrive.exception;

public class InvalidOperationException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidOperationException(){
    }
    
    @Override
    public String getMessage(){
        return "Invalid Operation";
    }
}