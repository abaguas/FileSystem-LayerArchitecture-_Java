package pt.tecnico.mydrive.exception;

public class InvalidUsernameOrPasswordException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _username;
    private String _password;
    
    public InvalidUsernameOrPasswordException(String username){
        _username=username;
    }
    
    public String getUsername() {
        return _username;
    }
    

    @Override
    public String getMessage(){
        return "User invalid: " + getUsername() + " or Password invalid" ;
    }
}