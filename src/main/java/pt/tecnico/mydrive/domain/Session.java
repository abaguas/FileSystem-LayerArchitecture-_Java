package pt.tecnico.mydrive.domain;
import org.joda.time.DateTime;
public class Session extends Session_Base {
    
    public Session(User u, long token, MyDrive mydrive) {   //Login //FIXME retirei o argumento login que era recebido
        super();
        setCurrentUser(u);
        setToken(token);
        setMd(mydrive);
        setCurrentDir(u.getMainDirectory());
        getMd().addSession(this);
        DateTime actual = new DateTime();
        setTimestamp(actual);
    } 
}
