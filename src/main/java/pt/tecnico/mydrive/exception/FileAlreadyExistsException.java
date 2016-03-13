package pt.tecnico.mydrive.exception;

public class FileAlreadyExistsException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _name;
    private Integer _id;

    public FileAlreadyExistsException(String name, Integer id){
        _name=name;
        _id=id;
    }
    
    public Integer getId(){
        return _id;
    }

    public String getName() {
        return _name;
    }
    
    @Override
    public String getMessage(){
        return getName() + " : File or Directory already exists.";
    }
}