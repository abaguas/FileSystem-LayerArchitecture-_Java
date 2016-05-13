/*package pt.tecnico.mydrive.system;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import pt.tecnico.mydrive.service.AbstractServiceTest;
import pt.tecnico.mydrive.service.AddVariableService;
import pt.tecnico.mydrive.service.ChangeDirectoryService;
import pt.tecnico.mydrive.service.CreateFileService;
import pt.tecnico.mydrive.service.DeleteFileService;
import pt.tecnico.mydrive.service.ExecuteAssociationService;
import pt.tecnico.mydrive.service.ExecuteFileService;
import pt.tecnico.mydrive.service.ListDirectoryService;
import pt.tecnico.mydrive.service.LoginService;
import pt.tecnico.mydrive.service.ReadFileService;
import pt.tecnico.mydrive.service.WriteFileService;
import pt.tecnico.mydrive.service.XMLImportService;
import pt.tecnico.mydrive.service.dto.FileDto;
import pt.tecnico.mydrive.service.dto.VariableDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

	private ByteArrayOutputStream testOut = new ByteArrayOutputStream();;
	private final PrintStream standard = System.out;
	private long tokenAbaguas;
	private long tokenElGorila;
	private static final String importFile = "info/MD.xml";
	private final String usernameAbaguas = "abaguas";
	private final String nameAbaguas = "Andre";
	private final String passwordAbaguas = "ins3cur3";
	private final String usernameElGorila = "elGorila";
	private final String nameElGorila = "King";
	private final String passwordElGorila = "ultras3cur3";
	
	
	private final String dirCarne = "carne";
	private final String dirBananas = "bananas";
	
	private final String appElGorila = "appElGorila";
	private final String appElGorilaContent = "pt.tecnico.mydrive.presentation.Hello.greet";
	private final String appAbaguas = "appAbaguas";
	private final String appAbaguasContent = "pt.tecnico.mydrive.presentation.Hello";
	
	private final String plainFileAbaguas = "plainFileAbaguas";
	private final String plainFileAbaguasContent = "las Palmas";
	private final String plainFileAbaguasNewContent = "ls";
	private final String plainFileAbaguasGor = "plainFileAbaguas.gor";
	private final String plainFileAbaguasGorContent = "cd\\n/usr/bin/ls";
	
	private final String linkAbaguas = "linkAbaguas";
	private final String linkAbaguasContent = "/home/abaguas/plainFileAbaguas";
	private final String linkElGorila = "linkElGorila";
	private final String linkElGorilaContent = "../../$EL/appElGorila";

	//FIXME fill contents
	
	
	@Override
	protected void populate() {
	}
		
	@Test
	public void success() throws JDOMException, IOException {
		
		File file = new File(importFile);
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document)builder.build(file);

		new XMLImportService(document).execute();
		
		//servicos de mudanca de diretoria
		ChangeDirectoryService change = null;
		
		//servicos de login para iniciar os tokens
		LoginService loginService = new LoginService(usernameAbaguas, passwordAbaguas);
		loginService.execute();
		tokenAbaguas = loginService.result();
		
		loginService = new LoginService(usernameElGorila, passwordElGorila);
		loginService.execute();
		tokenElGorila = loginService.result();
		
		//servicos de listagem de diretoria
		ListDirectoryService listAbaguas = new ListDirectoryService(tokenAbaguas);
		ListDirectoryService listElGorila = new ListDirectoryService(tokenElGorila);
		
		//Construções no lado do abaguas
		listAbaguas.execute();
		assertEquals("A diretoria /home/abaguas não tem tamanho 2", listAbaguas.result().size(), 2);
		
		new CreateFileService(tokenAbaguas, dirCarne, "Dir").execute();
		new CreateFileService(tokenAbaguas, plainFileAbaguas, plainFileAbaguasContent, "PlainFile").execute();
		listAbaguas.execute();
		assertEquals("A diretoria carne ou(inclusivo) o plainFileAbaguas não foi corretamente criado", listAbaguas.result().size(), 4);
		
		change = new ChangeDirectoryService(tokenAbaguas, dirCarne);
		change.execute();
		
		listAbaguas.execute();
		assertEquals("A diretoria nao foi corretamente alterada para carne", listAbaguas.result().size(), 2);
		
		new CreateFileService(tokenAbaguas, appAbaguas, appAbaguasContent, "App").execute();;
		listAbaguas.execute();
		assertEquals("A appAbaguas não foi corretamente criado", listAbaguas.result().size(), 3);
		

		//Construções do lado do ElGorila
		    //pelo ElGorila
		listElGorila.execute();
		assertEquals("A diretoria /home/ElGorila não tem tamanho 2", listElGorila.result().size(), 2);
		
		new CreateFileService(tokenElGorila, dirBananas, "Dir").execute();
		listElGorila.execute();
		assertEquals("A diretoria não foi corretamente criada para bananas", listElGorila.result().size(), 3);
		
		new CreateFileService(tokenElGorila, appElGorila, appElGorilaContent, "App").execute(); 
		listElGorila.execute();
		assertEquals("A appElGorila não foi corretamente criada", listElGorila.result().size(), 4);
		
		change = new ChangeDirectoryService(tokenElGorila, dirBananas);
		change.execute();
		listElGorila.execute();
		assertEquals("A diretoria nao foi corretamente alterada para bananas", listElGorila.result().size(), 2);
		
		new CreateFileService(tokenElGorila, linkElGorila, linkElGorilaContent, "Link").execute(); 
		listElGorila.execute();
		assertEquals("O linkElGorila não foi corretamente criado", listElGorila.result().size(), 3);
		
		
		   //pelo Abaguas
		change = new ChangeDirectoryService(tokenAbaguas, "../../elGorila/bananas");
		change.execute();
		listAbaguas.execute();
		assertEquals("A diretoria nao foi corretamente alterada para bananas", listAbaguas.result().size(), 3);
		
		new CreateFileService(tokenAbaguas, plainFileAbaguasGor, plainFileAbaguasGorContent, "PlainFile").execute(); 
		listAbaguas.execute();
		assertEquals("O plainFileAbaguasGor não foi corretamente criado", listAbaguas.result().size(), 4);
		
		new CreateFileService(tokenAbaguas, linkAbaguas, linkAbaguasContent, "Link").execute(); 
		listAbaguas.execute();
		assertEquals("O plainFileAbaguasGor não foi corretamente criado", listAbaguas.result().size(), 5);
		
		
		
		//escrita e leitura de ficheiros
		ReadFileService readFileService = new ReadFileService(tokenAbaguas, plainFileAbaguasGor);
		readFileService.execute();
		assertEquals("O conteudo lido de plainFileAbaguas.gor é incorreto", readFileService.result(), plainFileAbaguasGorContent);
		
		change = new ChangeDirectoryService(tokenAbaguas, "/home/abaguas");
		change.execute();
		new WriteFileService(plainFileAbaguas, plainFileAbaguasNewContent, tokenAbaguas).execute();
		
		readFileService = new ReadFileService(tokenAbaguas, plainFileAbaguas);
		readFileService.execute();
		assertEquals("O conteudo lido de plainFileAbaguas é incorreto", readFileService.result(), plainFileAbaguasNewContent);
	
		change = new ChangeDirectoryService(tokenAbaguas, "/home/elGorila/bananas");
		change.execute();
		
		readFileService = new ReadFileService(tokenAbaguas, linkAbaguas);
		readFileService.execute();
		assertEquals("O conteudo lido de linkAbaguas é incorreto", readFileService.result(), plainFileAbaguasNewContent);
		
		//criacao de variáveis
		AddVariableService addVariableService = new AddVariableService(tokenAbaguas, "VAR", "cataratas");
		addVariableService.execute();
		assertEquals("Variáveis de ambiente criadas incorretamente", addVariableService.result().size(), 1);
		
		
		//leitura de ficheiros com env links
		new MockUp<ReadFileService>() {
			@Mock
			void execute() { }
		};
		new MockUp<ReadFileService>() {
			@Mock
			String result() { return appElGorilaContent; }
		};
		readFileService = new ReadFileService(tokenElGorila, linkElGorila);
		readFileService.execute();
		assertEquals("O conteudo lido de linkElGorila é incorreto", readFileService.result(), appElGorilaContent);
		
		
		//execução de ficheiros
		String[] s = {"aquele", "b"};
		
		

//		System.setOut(new PrintStream(testOut));
//		new ExecuteFileService(tokenAbaguas, "/home/abaguas/"+ plainFileAbaguas, s).execute();
//		assertNotNull("A execução do plainFileAbaguas não imprimiu nada", testOut.toString());
//		

//		
//		testOut.reset();
//		new ExecuteFileService(tokenAbaguas, "/home/abaguas/carne"+ appAbaguas, s).execute();
//		assertEquals("A execução da AppAbaguas não é Hello phonebook!", testOut.toString(), "Hello phonebook!");
//		
//		testOut.reset();
//		new ExecuteFileService(tokenAbaguas, "../"+ appElGorila, s);
//		assertEquals("A execução da AppElGorila não é Hello aquele!", testOut.toString(), "Hello aquele!");
//		
//		//execucao com extensao
//		testOut.reset();
//		new MockUp<ExecuteAssociationService>(){
//			@Mock
//			void execute() { System.out.println("Execute "+plainFileAbaguasGor+"?"); }
//		};
//		new ExecuteFileService(tokenAbaguas, plainFileAbaguasGor, s).execute();
//		assertEquals("A execucao da app não é Execute " + plainFileAbaguasGor + "?" , "Execute "+plainFileAbaguasGor+"?", testOut.toString());	
//	
//		System.setOut(standard);
//		
//		//eliminaçao de ficheiros
//		
//		DeleteFileService deleteFileService = new DeleteFileService(tokenAbaguas, plainFileAbaguas);
//		deleteFileService.execute();
//		
//		deleteFileService = new DeleteFileService(tokenAbaguas, linkAbaguas);
//		deleteFileService.execute();
//		
//		listElGorila.execute();
//		assertEquals("Link ou(inclusivo) plain file do abaguas incorretamente eliminados", listElGorila.result().size(), 3);
//		
//		change = new ChangeDirectoryService(tokenElGorila, "..");
//		listElGorila.execute();
//		assertEquals("Link ou(inclusivo) plain file do abaguas incorretamente eliminados", listElGorila.result().size(), 5);
//		
//		deleteFileService = new DeleteFileService(tokenElGorila, dirBananas);
//		deleteFileService.execute();
//		
//		listElGorila.execute();
//		assertEquals("Diretoria bananas incorretamente eliminada", listElGorila.result().size(), 3);
//		
//	}
//
//}

		
		//testOut.reset();
		System.setOut(new PrintStream(testOut));
		new ExecuteFileService(tokenAbaguas, "/home/abaguas/carne/"+ appAbaguas, s).execute();
		assertEquals("A execução da AppAbaguas não é Hello myDrive!", "Hello myDrive!", testOut.toString());
		
		//testOut.reset();
		new ExecuteFileService(tokenAbaguas, "../"+ appElGorila, s);
		assertEquals("A execução da AppElGorila não é Hello aquele!", testOut.toString(), "Hello aquele!");
		
		//execucao com extensao
		//testOut.reset();
		new MockUp<ExecuteAssociationService>(){
			@Mock
			void execute() { System.out.println("Execute "+plainFileAbaguasGor+"?"); }
		};
		new ExecuteFileService(tokenAbaguas, plainFileAbaguasGor, s).execute();
		assertEquals("A execucao da app não é Execute " + plainFileAbaguasGor + "?" , "Execute "+plainFileAbaguasGor+"?", testOut.toString());	
	
		System.setOut(standard);
		
		//eliminaçao de ficheiros
		
		DeleteFileService deleteFileService = new DeleteFileService(tokenAbaguas, plainFileAbaguas);
		deleteFileService.execute();
		
		deleteFileService = new DeleteFileService(tokenAbaguas, linkAbaguas);
		deleteFileService.execute();
		
		listElGorila.execute();
		assertEquals("Link ou(inclusivo) plain file do abaguas incorretamente eliminados", listElGorila.result().size(), 3);
		
		change = new ChangeDirectoryService(tokenElGorila, "..");
		listElGorila.execute();
		assertEquals("Link ou(inclusivo) plain file do abaguas incorretamente eliminados", listElGorila.result().size(), 5);
		
		deleteFileService = new DeleteFileService(tokenElGorila, dirBananas);
		deleteFileService.execute();
		
		listElGorila.execute();
		assertEquals("Diretoria bananas incorretamente eliminada", listElGorila.result().size(), 3);
		
	}

}*/

