package pt.tecnico.mydrive.exception;

public class OutDatedUserException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String _name;
    
    public OutDatedUserException(String name){
        _name=name;
    }
    
    public String getName() {
        return _name;
    }
    
    @Override
    public String getMessage(){
        return getName() + " : This user is outDated because Password is less then 8 characters.";
    }
}
