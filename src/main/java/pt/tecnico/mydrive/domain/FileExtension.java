package pt.tecnico.mydrive.domain;

public class FileExtension extends FileExtension_Base {
    
    public FileExtension() {
    }
    
    public void execute() {
    	getApplication().execute(getFile());
    } 
    
}
