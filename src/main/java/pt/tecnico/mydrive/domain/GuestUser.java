package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.PermissionDeniedException;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.mydrive.exception.InvalidOperationException;

public class GuestUser extends GuestUser_Base {
    
	public static GuestUser getInstance(){
        GuestUser guestUser = FenixFramework.getDomainRoot().getMyDrive().getGuestUser();
        if (guestUser != null)
            return guestUser;
        return new GuestUser();
    }
    
	 private GuestUser() {
		 initSpecial("nobody","","Guest");
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
