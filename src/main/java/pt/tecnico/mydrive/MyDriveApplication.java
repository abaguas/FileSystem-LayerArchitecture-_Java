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

    public static String f(String s){
        String S[] = s.split("/");
        return S[0];
    }

    public static void main(String[] args) {
        System.out.println("*** Welcome to the MyDrive application! ***");
        try {
            /*setup();
            for (String s: args) xmlScan(new File(s));
            xmlPrint();*/
            String b = "home";
            String c = "home/root";
            String d = "home/root/cenas";
            System.out.println(f(b));
            System.out.println(f(c));
            System.out.println(f(d));
        } finally { FenixFramework.shutdown(); }
    }

    @Atomic
    public static void setup() {}

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

}
