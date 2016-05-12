package pt.tecnico.mydrive.system;

import java.io.File;
import java.io.IOException;

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
import pt.tecnico.mydrive.service.ListDirectoryService;
import pt.tecnico.mydrive.service.LoginService;
import pt.tecnico.mydrive.service.ReadFileService;
import pt.tecnico.mydrive.service.WriteFileService;
import pt.tecnico.mydrive.service.XMLImportService;

import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

	private long tokenAbaguas;
	private long tokenElGorila;
	private static final String importFile = "src/MD.xml";
	private final String usernameAbaguas = "abaguas";
	private final String nameAbaguas = "Andre";
	private final String passwordAbaguas = "ins3cur3";
	private final String usernameElGorila = "elGorila";
	private final String nameElGorila = "King";
	private final String passwordElGorila = "s3cur3";
	
	
	private final String dirCarne = "carne";
	private final String dirBananas = "bananas";
	
	private final String appElGorila = "appElGorila";
	private final String appElGorilaContent = "Umas asinhas";  //FIXME
	private final String appAbaguas = "appAbaguas";
	private final String appAbaguasContent = "/home/ls";  //FIXME
	
	private final String plainFileAbaguas = "plainFileAbaguas";
	private final String plainFileAbaguasContent = "las Palmas"; //FIXME
	private final String plainFileAbaguasNewContent = "Guardei a roupa no frigorifico"; //FIXME
	private final String plainFileAbaguasGor = "plainFileAbaguas.gor";
	private final String plainFileAbaguasGorContent = "Umas batatas fritas";  //FIXME
	
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
		
		ClassLoader loader = getClass().getClassLoader();
		File file = new File(loader.getResource(importFile).getFile());
		Document doc = (Document)new SAXBuilder().build(file);
		new XMLImportService(doc).execute();
		
		//servicos de listagem de diretoria
		ListDirectoryService listAbaguas = new ListDirectoryService(tokenAbaguas);
		ListDirectoryService listElGorila = new ListDirectoryService(tokenElGorila);
		
		//servicos de mudanca de diretoria
		ChangeDirectoryService change;
		
		//servicos de login para iniciar os tokens
		LoginService loginService = new LoginService(usernameAbaguas, passwordAbaguas);
		loginService.execute();
		tokenAbaguas = loginService.result();
		
		loginService = new LoginService(usernameElGorila, passwordElGorila);
		loginService.execute();
		tokenElGorila = loginService.result();
		
		//Construções no lado do abaguas
		listAbaguas.execute();
		assertEquals("A diretoria /home/abaguas não tem tamanho 2", listAbaguas.result().size(), 2);
		
		new CreateFileService(tokenAbaguas, dirCarne, "Dir");
		new CreateFileService(tokenAbaguas, plainFileAbaguas, plainFileAbaguasContent, "PlainFile");
		listAbaguas.execute();
		assertEquals("A diretoria carne ou(inclusivo) o plainFileAbaguas não foi corretamente criado", listAbaguas.result().size(), 4);
		
		change = new ChangeDirectoryService(tokenAbaguas, dirCarne);
		change.execute();
		listAbaguas.execute();
		assertEquals("A diretoria nao foi corretamente alterada para carne", listAbaguas.result().size(), 2);
		
		new CreateFileService(tokenAbaguas, appAbaguas, appAbaguasContent, "App");
		listAbaguas.execute();
		assertEquals("A appAbaguas não foi corretamente criado", listAbaguas.result().size(), 3);
		

		//Construções do lado do ElGorila
		    //pelo ElGorila
		listElGorila.execute();
		assertEquals("A diretoria /home/ElGorila não tem tamanho 2", listElGorila.result().size(), 2);
		
		new CreateFileService(tokenElGorila, dirBananas, "Dir");
		listElGorila.execute();
		assertEquals("A diretoria não foi corretamente criada para bananas", listElGorila.result().size(), 3);
		
		new CreateFileService(tokenElGorila, appElGorila, appElGorilaContent, "App"); 
		listElGorila.execute();
		assertEquals("A appElGorila não foi corretamente criada", listElGorila.result().size(), 4);
		
		change = new ChangeDirectoryService(tokenElGorila, dirBananas);
		change.execute();
		listElGorila.execute();
		assertEquals("A diretoria nao foi corretamente alterada para bananas", listElGorila.result().size(), 2);
		
		new CreateFileService(tokenElGorila, linkElGorila, linkElGorilaContent, "Link"); 
		listElGorila.execute();
		assertEquals("O linkElGorila não foi corretamente criado", listElGorila.result().size(), 3);
		
		
		   //pelo Abaguas
		change = new ChangeDirectoryService(tokenAbaguas, "../../elGorila/bananas");
		change.execute();
		listElGorila.execute();
		assertEquals("A diretoria nao foi corretamente alterada para bananas", listElGorila.result().size(), 3);
		
		new CreateFileService(tokenAbaguas, plainFileAbaguasGor, plainFileAbaguasGorContent, "PlainFile"); 
		listElGorila.execute();
		assertEquals("O plainFileAbaguasGor não foi corretamente criado", listAbaguas.result().size(), 4);
		
		new CreateFileService(tokenAbaguas, linkAbaguas, linkAbaguasContent, "Link"); 
		listElGorila.execute();
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
		new AddVariableService(tokenAbaguas, "VAR", "cataratas").execute();
		AddVariableService addVariableService = new AddVariableService(tokenAbaguas, "EL", "elGorila");
		addVariableService.execute();
		assertEquals("Variáveis de ambiente criadas incorretamente", addVariableService.result().size(), 2);
		
		
		//leitura de ficheiros com env links
		new MockUp<ReadFileService>() {
			@Mock
			String result() { return appElGorilaContent; }
		};
		readFileService = new ReadFileService(tokenElGorila, linkElGorila);
		readFileService.execute();
		assertEquals("O conteudo lido de linkElGorila é incorreto", readFileService.result(), appElGorilaContent);
		
		
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
		
		
		
		//FIXME apenas falta testar a execucao de ficheiros
		//XXX compila mas nao testei
		
		/*
		String[] str = null;
		new ExecutePlainFileService(token, "path", str );*/
		//FIXME: A execucao é ou não sobre ficheiros da aplicação
	}
	

}
