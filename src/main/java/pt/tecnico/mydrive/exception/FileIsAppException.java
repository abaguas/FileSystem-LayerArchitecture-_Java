package pt.tecnico.mydrive.exception;

public class FileIsAppException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _name;

    public FileIsAppException(String name){
        _name=name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return getName() + " : Operation only valid for plain files.";
    }
}
