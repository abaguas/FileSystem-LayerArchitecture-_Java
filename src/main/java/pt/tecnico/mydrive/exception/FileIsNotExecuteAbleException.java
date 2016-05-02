package pt.tecnico.mydrive.exception;

public class FileIsNotExecuteAbleException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String name;

    public FileIsNotExecuteAbleException(String name){
        this.name=name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getMessage(){
        return getName() + " : Operation only valid for plain files.";
    }
}