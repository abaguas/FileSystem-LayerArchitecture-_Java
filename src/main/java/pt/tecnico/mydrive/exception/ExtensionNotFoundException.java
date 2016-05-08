package pt.tecnico.mydrive.exception;

public class ExtensionNotFoundException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String fileName = null;
	
	public ExtensionNotFoundException(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	@Override
    public String getMessage(){
        return "No extension found to open the file: " + getFileName();
    }
}