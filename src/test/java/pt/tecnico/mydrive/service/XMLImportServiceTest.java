package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidPasswordSizeException;
import pt.tecnico.mydrive.exception.InvalidUsernameException;

public class XMLImportServiceTest extends AbstractServiceTest {

	private final String xmlSuccess = "info/drive.xml";
	private final String xmlSuccessUsers = "info/users.xml";
	private final String xmlSuccessNull = "info/null.xml";
	private final String xmlWithErrors = "info/errors.xml";
	private final String xmlNonExistent = "info/burro.xml";
	
	@Override
	protected void populate() {
	}

	
	@Test
    public void successXML() throws JDOMException, IOException {
		try {
            FenixFramework.getTransactionManager().begin(false);
            populate();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
		File file = new File(xmlSuccess);
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document)builder.build(file);
		XMLImportService service = new XMLImportService(document);
		service.execute();
		assertEquals(service.getMyDrive().getUsersSet().size(), 4);
		
		try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
	}
	
	@Test
    public void successXMLUsers() throws JDOMException, IOException {
		try {
            FenixFramework.getTransactionManager().begin(false);
            populate();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
		
		File file = new File(xmlSuccessUsers);
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document)builder.build(file);
		XMLImportService service = new XMLImportService(document);
		service.execute();
		
		assertEquals(service.getMyDrive().getUsersSet().size(), 9);
		try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
	}
	
	@Test
    public void successXMLNull() throws JDOMException, IOException {
		try {
            FenixFramework.getTransactionManager().begin(false);
            populate();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
		File file = new File(xmlSuccessNull);
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document)builder.build(file);
		XMLImportService service = new XMLImportService(document);
		service.execute();
		
		assertEquals(service.getMyDrive().getUsersSet().size(), 2);
		try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
	}
	
	@Test(expected = java.io.FileNotFoundException.class)
    public void invalidXMLFile() throws JDOMException, IOException {
		try {
            FenixFramework.getTransactionManager().begin(false);
            populate();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
		File file = new File(xmlNonExistent);
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document)builder.build(file);
		new XMLImportService(document).execute();
		try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
	}
	
	@Test(expected = InvalidPasswordSizeException.class)
    public void XMLFileWithErrors() throws JDOMException, IOException {
		try {
            FenixFramework.getTransactionManager().begin(false);
            populate();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
		File file = new File(xmlWithErrors);
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document)builder.build(file);
		new XMLImportService(document).execute();
		try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
	}
	
}
