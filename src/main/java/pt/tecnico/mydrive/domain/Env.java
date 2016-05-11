package pt.tecnico.mydrive.domain;

public class Env extends Env_Base {
    
    public Env(String name, String value) {
        super.setName(name);
        super.setValue(value);
    }

    @Override
    public void setSession(Session s){
    	s.addEnv(this);
    }

    @Override
    public String toString(){
    	return getName() + " " + getValue() + " ";
    }
    
}
