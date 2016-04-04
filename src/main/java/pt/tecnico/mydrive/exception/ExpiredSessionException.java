package pt.tecnico.mydrive.exception;

public class ExpiredSessionException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    
    public ExpiredSessionException(){
    }
    
    @Override
    public String getMessage(){
        return "Session Expired";
    }
}