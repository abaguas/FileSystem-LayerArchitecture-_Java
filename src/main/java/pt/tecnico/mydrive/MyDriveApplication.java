package pt.tecnico.mydrive;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;


public class MyDriveApplication {

    public static void main(String[] args) {
        System.out.println("*** Welcome to the MyDrive application! ***");
        try{
         setup();
        }catch(NoSuchFileException e){
            System.out.println("NoSuchFileException...");
        }catch(FileNotDirectoryException e){
            System.out.println("FileNotDirectoryException...");
        } finally { FenixFramework.shutdown(); }
    }
    
    @Atomic
    public static void init() { // empty phonebook
        //log.trace("Init: " + FenixFramework.getDomainRoot());
        //MyDrive.getInstance().cleanup();
    }
    
    @Atomic
    public static void setup() throws MyDriveException{
        MyDrive md = MyDrive.getInstance();
        System.out.println("*** Welcome to the MyDrive application! ***");
        System.out.println("*** Welcome to the MyDrive application! ***");
        System.out.println("*** Welcome to the MyDrive application! ***");
        System.out.println("*** Welcome to the MyDrive application! ***");
        md.cd("..");
        md.createPlainFile("README", "lista de Utilizadores. ");
        System.out.println(md.getCurrentDir().ls());
        System.out.println(md.ls("README"));
        System.out.println(md.pwd());
        md.cd("..");
        System.out.println(md.pwd());
        md.createDir("usr");
        md.cd("usr");
        System.out.println(md.pwd());
        md.createDir("local");
        md.cd("local");
        System.out.println(md.pwd());
        md.createDir("bin");
        md.cd("bin");
        System.out.println(md.pwd());
        System.out.println("*** Welcome to the MyDrive application! ***");
        //md.createUser("luissacouto", "pass", "Luis");
        //md.createDir("ist");
    }
}
