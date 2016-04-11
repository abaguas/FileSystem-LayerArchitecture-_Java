package pt.tecnico.mydrive.exception;


public class LinkWithoutContentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _name;

    public LinkWithoutContentException(String name){
        _name=name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return getName() + " : Link should have a path in its content";
    }
}
