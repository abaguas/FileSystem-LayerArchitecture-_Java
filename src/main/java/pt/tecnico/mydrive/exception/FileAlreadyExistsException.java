package pt.tecnico.mydrive.exception;

public class FileAlreadyExistsException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _name;
    
    public FileAlreadyExistsException(String name){
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