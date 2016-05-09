package pt.tecnico.mydrive.domain;

public class FileExtension extends FileExtension_Base {
    
    public FileExtension(User user, File file, App app) {
    	setExtensionOwner(user);
    	setFile(file);
    	setApp(app);
    }
    
}
