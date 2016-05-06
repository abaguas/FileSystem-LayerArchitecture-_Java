package pt.tecnico.mydrive.domain;

public class Application extends Application_Base {
    
    public Application(String name) {
    	setName(name);
    }
    
    public void execute(File file) {
    	//Nothing to do, this will be mocked
    }
    
}
