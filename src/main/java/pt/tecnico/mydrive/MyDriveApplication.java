package pt.tecnico.mydrive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.File;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileNotDirectoryException;


public class MyDriveApplication {

    public static void main(String[] args) {
        System.out.println("*** Welcome to the MyDrive application! ***");
        try{
         setup();
         for (String s: args) XMLScan(new File(s));
        //XMLPrint();
         test();
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
    public static void test() { 
        MyDrive md = MyDrive.getInstance();
        for(User u : md.getUsers()){
            //System.out.println(u.getUsername());
        }
        /*System.out.println(md.pwd());
                System.out.println("*** Welcome to the MyDrive application! ***");
        System.out.println("*** Welcome to the MyDrive application! ***");
*/
        System.out.println(md.ls());
    }
    
    @Atomic
    public static void setup() throws MyDriveException{
        MyDrive md = MyDrive.getInstance();
        System.out.println("*** Welcome to the MyDrive application! ***");
        System.out.println("*** Welcome to the MyDrive application! ***");
        System.out.println("*** Welcome to the MyDrive application! ***");
        System.out.println("*** Welcome to the MyDrive application! ***");
        /*md.cd("..");
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
        //md.createDir("ist");*/
    }
    
    @Atomic
    public static void XMLPrint() {
        Document doc = MyDrive.getInstance().XMLExport();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }

    @Atomic
    public static void XMLScan(File file) {
        //log.trace("xmlScan: " + FenixFramework.getDomainRoot());
        MyDrive md = MyDrive.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(file);
            md.XMLImport(document.getRootElement());
        } catch (JDOMException | IOException e) {
        e.printStackTrace();
        }
    }

}
