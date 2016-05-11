package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.PermissionDeniedException;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.mydrive.exception.InvalidOperationException;

public class GuestUser extends GuestUser_Base {
    
	public static GuestUser getInstance(){
        GuestUser guestUser = FenixFramework.getDomainRoot().getMyDrive().getGuestUser();
        if (guestUser != null)
            return guestUser;
        return new GuestUser(FenixFramework.getDomainRoot().getMyDrive());
    }
    
	private GuestUser(MyDrive md) {
		initBasic(md, "nobody","","Guest", new Permission(true, true, true, true), new Permission(true, false, true, false));	 
	}
	 
	@Override
	public void setPassword(String password) throws InvalidOperationException{
		throw new InvalidOperationException("change password of Guest User");
	}

	@Override
    public void remove(){
        throw new PermissionDeniedException("Cant Remove Special Guest User");
    }
}
