package pt.tecnico.mydrive.exception;

public class NoSuchUserException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _username;
    
    public NoSuchUserException(String username){
        _username=username;
    }
    
    public String getUsername() {
        return _username;
    }
    
    @Override
    public String getMessage(){
        return getUsername() + " : User already exists.";
    }
}