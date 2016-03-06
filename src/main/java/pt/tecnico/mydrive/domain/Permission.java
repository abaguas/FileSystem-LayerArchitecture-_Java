package pt.tecnico.mydrive.domain;

public class Permission extends Permission_Base {
    
    public Permission(boolean read, boolean write, boolean execute, boolean eliminate) {
        super();
        setRead(read);
        setWrite(write);
        setExecute(execute);
        setEliminate(eliminate);
    }
    
}
