package pt.tecnico.mydrive.domain;

public class Session extends Session_Base {
    
    public Session(User u, long token, Login login) {   //Login
        super();
        setCurrentUser(u);
        setToken(token);
        setLogin(login);
    }

    public Session(User u, long token, Login l){
    	//Fix meeeeeeeee
    }
    
}
