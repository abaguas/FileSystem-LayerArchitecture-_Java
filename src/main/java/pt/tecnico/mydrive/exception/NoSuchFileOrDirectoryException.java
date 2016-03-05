package pt.tecnico.mydrive.exception;

public class NoSuchFileOrDirectoryException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _name;

	public NoSuchFileOrDirectoryException(String name){
		_name=name;
	}
	
	//_name em vez de contactName
    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return getName() + " : No Such File or Directory.";
    }
}
