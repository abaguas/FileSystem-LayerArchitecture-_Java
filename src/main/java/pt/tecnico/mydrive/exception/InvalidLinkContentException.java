package pt.tecnico.mydrive.exception;

public class InvalidLinkContentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String path;

    public InvalidLinkContentException(String path){
        this.path=path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String getMessage(){
        return "The path in the link doesnt exist: "+ getPath();
    }
}
