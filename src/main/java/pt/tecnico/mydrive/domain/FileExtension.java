package pt.tecnico.mydrive.domain;

public class FileExtension extends FileExtension_Base {
    
    public FileExtension(User user, File file, Application application) {
    	setExtensionOwner(user);
    	setFile(file);
    	setApplication(application);
    }
    
}
