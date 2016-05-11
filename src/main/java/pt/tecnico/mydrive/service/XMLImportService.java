package pt.tecnico.mydrive.service;

import java.io.File;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.MyDriveException;

public class XMLImportService extends MyDriveService {
    private final Document doc;

    public XMLImportService(Document doc) {
        this.doc = doc;
    }

    @Override
    protected void dispatch() throws MyDriveException { //Excepção??
        MyDrive.getInstance().xmlImport(doc.getRootElement());
    }
}
