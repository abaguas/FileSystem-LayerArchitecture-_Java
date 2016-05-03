package pt.tecnico.mydrive.exception;

public class InvalidPathException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String path;

    public InvalidPathException(String path){
        this.path=path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String getMessage(){
        return "The path doesnt exist: "+ getPath();
    }
}
