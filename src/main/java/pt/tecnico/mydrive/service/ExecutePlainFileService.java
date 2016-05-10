//package pt.tecnico.mydrive.service;
//
//import pt.tecnico.mydrive.domain.App;
//import pt.tecnico.mydrive.domain.Directory;
//import pt.tecnico.mydrive.domain.File;
//import pt.tecnico.mydrive.domain.Link;
//import pt.tecnico.mydrive.domain.MyDrive;
//import pt.tecnico.mydrive.domain.PlainFile;
//import pt.tecnico.mydrive.exception.InvalidTokenException;
//import pt.tecnico.mydrive.exception.NoSuchFileException;
//import pt.tecnico.mydrive.exception.PermissionDeniedException;
//
//
//
//public class ExecutePlainFileService extends MyDriveService {
//	private long token;
//	private String path;
//	private String argList;
//	
//	
//	public ExecutePlainFileService(long token, String path, String argList) {
//		this.token = token;
//		this.path = path;
//		this.argList = argList;
//	}
//
//	
//
//	@Override
//	protected void dispatch() throws PermissionDeniedException, NoSuchFileException, InvalidTokenException {
//		MyDrive md = getMyDrive();
//        Directory currentDir = md.getCurrentDirByToken(token);
//        File f =  md.getFileByPath(path, currentDir);
//        //md.executable(f);
//		  f.execute();    
//
//        //será que em f tenho já OBJECTO do tipo que quero??
//        //Se assim for, basta fazer f.execute() e ele sabe se chama o execute() do PlainFile(), Link ou App
//        
//        if(f instanceof PlainFile) {
//        	PlainFile pf = (PlainFile) f;
//            String fileName = pf.getName();
//            md.checkPermissions(token, fileName, "read-write-execute", "execute");
//            pf.execute();
//        }
//        
//        else if(f instanceof Link) {
//        	Link l = (Link) f;
//            String fileName = l.getName();
//            md.checkPermissions(token, fileName, "read-write-execute", "execute");
//            l.execute();
//        }
//        
//        else if(f instanceof App) {
//        	App a = (App) f;
//            String fileName = a.getName();
//            md.checkPermissions(token, fileName, "read-write-execute", "execute");
//            a.execute();
//        }
//	}
//
//
//}

