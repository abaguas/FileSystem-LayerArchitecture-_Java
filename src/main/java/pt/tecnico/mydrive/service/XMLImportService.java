package pt.tecnico.mydrive.service;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.MyDriveException;

public class XMLImportService extends MyDriveService {

	private String fileName = null;
	
	public XMLImportService(String fileName) {        
        this.fileName = fileName;
    }
    
	
	@Override
	protected void dispatch() throws MyDriveException {
		// TODO
		MyDrive md = MyDrive.getInstance();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document)builder.build(fileName);
            md.xmlImport(document.getRootElement());
        } catch (JDOMException | IOException e) {
        	e.printStackTrace();
        }

	}

}
