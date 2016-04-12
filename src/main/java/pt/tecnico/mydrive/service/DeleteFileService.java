package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;
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
        /*MyDrive md = MyDrive.getInstance();
        currentUser = md.getSessionByToken(token).getCurrentUser();
        currentDir = md.getSessionByToken(token).getCurrentDir();
        
        if (fileName != null){  //FIXME todos têm de verificar isto
            File file = currentDir.get(fileName); // throws no such file exception 
            try {
            	//check execute permissions (current directory
            	//check read permissions (file)
	            md.cdable(file);
	            Directory d = (Directory) file;
	            for (File f: d.getFiles()){
	            	try{
	            		this.dispatch();
	            	} catch (PermissionDeniedException pde){
	            		//nothing to do
	            	}
	            }
            } catch (FileNotCdAbleException fnde){
            	currentDir.remove(fileName);
            }
        }        
        else {
        	throw new NoSuchFileException("Invalid file name: null");
        }*/
    }
    //FIXME
   //ver as permissoes de execucao e leitura
    //pensar em como voltar a chamar o servico (acho que está bem
    // a funcao check permissions deve enviar uma excepcao
    
    
    //FIXME devo passar um file ou o nome dele? Depende da implementacao do Rui
    void checkPermissions(File file, User user) {
    	
    }
}
