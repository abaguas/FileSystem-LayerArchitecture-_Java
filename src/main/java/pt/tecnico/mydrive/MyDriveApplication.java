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
        System.out.println("*** Welcome to the MyDrive application! ***");
        try {
            if(args.length == 0){
                setup();
            }
            else{
                for (String s: args) xmlScan(new File(s));
            }
            xmlPrint();
        } finally { FenixFramework.shutdown(); }
    }

    @Atomic
    public static void setup() {
        /*MyDrive md = MyDrive.getInstance();
        Person person;

        person = new Person(pb, "Manel");
        new Contact(person, "SOS", 112);
        new Contact(person, "IST", 214315112);
        new Contact(person, "Xico", 911919191);
        new Contact(person, "Zé", 966669999);

        person = new Person(pb, "Maria");
        new Contact(person, "SOS", 112);
        new Contact(person, "IST", 214315112);
        new Contact(person, "Manel", 333333333);
        new Contact(person, "Xana", 963456789);*/
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
        MyDrive md = MyDrive.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(file);
            md.xmlImport(document.getRootElement());
        } catch (JDOMException | IOException e) {
        e.printStackTrace();
        }
    }

}
