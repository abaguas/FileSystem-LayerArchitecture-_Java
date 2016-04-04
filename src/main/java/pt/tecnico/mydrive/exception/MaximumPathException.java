package pt.tecnico.mydrive.exception;

public class MaximumPathException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _name;

    public MaximumPathException(String name){
        _name=name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return getName() + " : Exceeded maximum absolute path (1024 characters).";
    }
}