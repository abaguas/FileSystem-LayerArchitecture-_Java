package pt.tecnico.mydrive.domain;
import org.joda.time.DateTime;
import pt.tecnico.mydrive.exception.ExpiredSessionException;
public class Session extends Session_Base {
    
    public Session(User u, long token, MyDrive mydrive) {   
        super();
        setCurrentUser(u);
        setToken(token);
        setMd(mydrive);
        setCurrentDir(u.getMainDirectory());
        DateTime actual = new DateTime();
        setTimestamp(actual);
    } 

    @Override
    public void setMd(MyDrive md){
        if(md == null){
            super.setMd(null);
        }
        else{
            md.addSession(this);
        }
    }
}
