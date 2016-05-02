package pt.tecnico.mydrive.exception;

public class InvalidOperationException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String method;
    
    public InvalidOperationException(String method){
    	this.method=method;
    }
    
    public String getMethod(){
    	return method;
    }
    @Override
    public String getMessage(){
        return "Invalid Operation";
    }
}