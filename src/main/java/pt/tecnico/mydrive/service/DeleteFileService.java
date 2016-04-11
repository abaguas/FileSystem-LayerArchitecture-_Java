package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;


public class DeleteFileService extends MyDriveService{

    private long token;
	private String fileName;
    private Directory currentDir;
    private User currentUser;
    
    public DeleteFileService(long token, String fileName) {
    	this.token = token;
    	this.fileName = fileName;
    }
    
    //FIXME avisar sá couto para nao usar o invalid file name exception
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException {
        MyDrive md = MyDrive.getInstance();
        currentUser = md.getSessionByToken(token).getCurrentUser();
        currentDir = md.getSessionByToken(token).getCurrentDir();
        
        if (fileName != null){  //FIXME todos têm de verificar isto
            File file = currentDir.get(fileName); // throws no such file exception            
            checkPermissions(file, currentUser); //  FIXME permissoes ainda nao concluidas, 
            /* a funcao check permissions deve enviar uma excepcao*/
        }        
        else {
        	throw new NoSuchFileException("Invalid file name: null");
        }
        //FIXME quando removo uma dir tenho de ver as permissoes de todas as outras dirs?
        //e cancelar a operacao se uma nao dá? ou é responsabilidade de cada utilizador construir
        //em locais de confianca
        currentDir.remove(fileName);
    }
    
    //FIXME devo passar um file ou o nome dele
    void checkPermissions(File file, User user) {
    	
    }
}
