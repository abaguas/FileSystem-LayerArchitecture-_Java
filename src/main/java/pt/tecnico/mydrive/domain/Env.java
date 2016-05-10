package pt.tecnico.mydrive.domain;

public class Env extends Env_Base {
    
    public Env(String name, String value) {
        super();
    }

    @Override
    public void setSession(Session s){
    	s.addEnv(this);
    }
    
}
