package pt.tecnico.mydrive.exception;

public class TokenAlreadyExistsException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private long token;
    
    public TokenAlreadyExistsException(long token){
    	this.token=token;
    }
    
    public long getToken(){
    	return token;
    }
    
    @Override
    public String getMessage(){
        return "Token already exists: " + getToken();
    }
}