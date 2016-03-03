package pt.tecnico.mydrive.exception;

public class FileOrDirectoryAlreadyExistsException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _name;
    
    public FileOrDirectoryAlreadyExistsException(String name){
        _name=name;
    }
    
    public String getName() {
        return _name;
    }
    
    @Override
    public String getMessage(){
        return getName() + " : File or Directory already exists.";
    }
}