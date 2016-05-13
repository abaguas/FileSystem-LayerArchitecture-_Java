package pt.tecnico.mydrive.exception;

public class NoArgumentsForAppExecutionException extends MyDriveException {


    private static final long serialVersionUID = 1L;

    private String _name;

    public NoArgumentsForAppExecutionException(String name){
        _name=name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return getName() + " : At least one argument is needed to execute an App.";
    }
}
