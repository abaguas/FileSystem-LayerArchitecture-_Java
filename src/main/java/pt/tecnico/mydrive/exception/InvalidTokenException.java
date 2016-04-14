package pt.tecnico.mydrive.exception;

public class InvalidTokenException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    
    public InvalidTokenException(){
    }
    
    @Override
    public String getMessage(){
        return "Invalid Token";
    }
}