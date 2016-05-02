package pt.tecnico.mydrive.exception;

public class InvalidPasswordException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidPasswordException(){
    }
    
    @Override
    public String getMessage(){
        return "Invalid Password";
    }
}