package pt.tecnico.mydrive.service;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.MyDriveException;

public class XMLExportService extends MyDriveService {
    private Document doc;

    public XMLExportService() {
        
    }

    @Override
    protected void dispatch() throws MyDriveException {
		doc = MyDrive.getInstance().xmlExport();
    }

    public final Document result() {
        return doc;
    }
}