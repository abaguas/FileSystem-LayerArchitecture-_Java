package pt.tecnico.mydrive.exception;

public class NotDeleteAbleException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _name;

    public NotDeleteAbleException(String name){
        _name=name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return getName() + " : Cant delete special directory.";
    }
}