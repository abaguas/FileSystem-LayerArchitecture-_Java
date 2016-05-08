package pt.tecnico.mydrive.exception;

public class InvalidExceptionCodeException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String code;

	 public InvalidExceptionCodeException(String code){
		 this.code = code;
	 }

	    public String getCode() {
	        return code;
	    }

	    @Override
	    public String getMessage(){
	        return "This exception code is not valid: "+ getCode();
	    }
}
