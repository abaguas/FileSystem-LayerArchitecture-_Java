package pt.tecnico.mydrive.exception;

public class InvalidPasswordSizeException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordSizeException(){
    }

    @Override
    public String getMessage(){
        return "Password with size less than eight characters";
    }
}