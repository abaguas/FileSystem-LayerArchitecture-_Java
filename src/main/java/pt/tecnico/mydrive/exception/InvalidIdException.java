package pt.tecnico.mydrive.exception;

public class InvalidIdException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private Integer _id;

    public InvalidIdException(Integer id){
        _id=id;
    }

    public Integer getId() {
        return _id;
    }

    @Override
    public String getMessage(){
        return getId() + " : Wrong file Id given.";
    }
}