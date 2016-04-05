package pt.tecnico.mydrive.domain;

public class Session extends Session_Base {
    
    public Session(User u, long token) {   //Login //FIXME retirei o argumento login que era recebido
        super();
        setCurrentUser(u);
        setToken(token);
    }
    
}
