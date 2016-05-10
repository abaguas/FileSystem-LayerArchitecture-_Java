package pt.tecnico.mydrive.domain;

public class FileExtension extends FileExtension_Base {
    
    public FileExtension(User user, String extension, App app) {
    	setExtension(extension);
    	setExtensionOwner(user);
    	setApp(app);
    }
    
}
