package pt.tecnico.mydrive.exception;

public class LinkWithCycleException extends MyDriveException {
    
    private static final long serialVersionUID = 1L;
    
    private String linkName;
    
    public LinkWithCycleException(String name){
        linkName = name;
    }
    
    public String getLinkName() {
        return linkName;
    }
    
    @Override
    public String getMessage(){
        return getLinkName() + " : Completes a cycle";
    }
}
