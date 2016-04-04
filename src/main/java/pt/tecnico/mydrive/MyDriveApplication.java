package pt.tecnico.mydrive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        /*System.out.println("*** Welcome to the MyDrive application! ***");
        try{
        System.out.println("--------------------------------");
        delivery11();
        xmlPrint();
        delivery12();
        }catch(NoSuchFileException e){
            System.out.println("NoSuchFileException...");
        }catch(FileNotDirectoryException e){
            System.out.println("FileNotDirectoryException...");
        } finally { FenixFramework.shutdown(); }*/
    }

    /*@Atomic
    public static void delivery11() { 
        MyDrive md = MyDrive.getInstance();
        md.cd("..");
        md.createPlainFile("README", "lista de Utilizadores");
        md.cd("..");
        md.createDir("usr");
        md.cd("usr");
        md.createDir("local");
        md.cd("local");
        md.createDir("bin");
        md.cd("bin");
        md.cd("..");
        md.cd("..");
        md.cd("..");
        md.cd("home");
        md.ls("README");
        md.cd("..");
        md.cd("usr");
        md.cd("local");
        md.removeFile("bin");
    }
    

    @Atomic
    public static void delivery12() { 
        MyDrive md = MyDrive.getInstance();
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("--------------------------------");
        md.cd("home");
        System.out.println(md.ls());
        System.out.println("--------------------------------");
        md.removeFile("README");System.out.println(md.ls());
        System.out.println("--------------------------------");
        System.out.println(md.ls());
        System.out.println("--------------------------------");
    }       

    @Atomic
    public static void xmlPrint() {
        Document doc = MyDrive.getInstance().xmlExport();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
        } catch (IOException e) { System.out.println(e); }
    }

    @Atomic
    public static void xmlScan(File file) {
        //log.trace("xmlScan: " + FenixFramework.getDomainRoot());
        MyDrive md = MyDrive.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(file);
            md.xmlImport(document.getRootElement());
        } catch (JDOMException | IOException e) {
        e.printStackTrace();
        }
    }

    //a few tests

    @Atomic
    public static void test2() { 
        MyDrive md = MyDrive.getInstance();
        System.out.println("USERS");
        for (User u: md.getUsers()){
            System.out.println(u);
        }
        System.out.println("-------------------------------");
        System.out.println(md.pwd());
        md.cd("..");
        System.out.println(md.pwd());
        md.cd("..");
        System.out.println(md.pwd());
        md.cd("home");
        System.out.println(md.pwd());        
        System.out.println(md.ls());
        md.cd("jtb");
        System.out.println(md.ls("profile"));
        System.out.println(md.ls("documents"));
        System.out.println(md.ls("doc"));
        /*md.cd("bin");
        System.out.println(md.ls());
        System.out.println(md.ls("length"));
    }

    @Atomic
    public static void testDelivery11() { 
        MyDrive md = MyDrive.getInstance();
        System.out.println(md.ls());
        System.out.println("1--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("2--------------------------------");
        md.createPlainFile("README", "lista de Utilizadores");
        md.createLink("example", "isto e um link");
        System.out.println(md.ls());
        System.out.println("3--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("4--------------------------------");
        md.createDir("usr");
        System.out.println(md.ls());
        System.out.println("5--------------------------------");
        md.cd("usr");
        System.out.println(md.ls());
        System.out.println("6--------------------------------");
        md.createDir("local");
        System.out.println(md.ls());
        System.out.println("7--------------------------------");
        md.cd("local");
        System.out.println(md.ls());
        System.out.println("8--------------------------------");
        md.createDir("bin");
        System.out.println(md.ls());
        System.out.println("9--------------------------------");
        md.cd("bin");
        System.out.println(md.ls());
        System.out.println("10--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("11--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("12--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("13--------------------------------");
        md.cd("home");
        System.out.println(md.ls());
        System.out.println("14--------------------------------");
        System.out.println(md.ls("README"));
        System.out.println("15--------------------------------");
        md.cd("..");
        System.out.println(md.ls());
        System.out.println("16--------------------------------");
        md.cd("usr");
        System.out.println(md.ls());
        System.out.println("17--------------------------------");
        md.cd("local");
        System.out.println(md.ls());
        System.out.println("18--------------------------------");
        md.removeFile("bin");
        System.out.println(md.ls());
        System.out.println("19--------------------------------");
        try{
            md.cd("bin");
        }catch (Exception e){
            System.out.println("Excecao apanhada");
        }
    }*/

}
