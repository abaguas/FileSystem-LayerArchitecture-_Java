package pt.tecnico.mydrive.presentation;
import pt.tecnico.mydrive.service.XMLImportService;
import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class Import extends MyDriveCommand {

    public Import(MyDriveShell sh) { super(sh, "import", "import mydrive files and users. (use ./locaFile or resourceFile)"); }
    public void execute(String[] args) {
		if (args.length < 1) {
		    throw new RuntimeException("USAGE: "+name()+" filename");
		}
		
		File file = new File(args[0]);
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		try {
			document = (Document)builder.build(file);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new XMLImportService(document).execute();
    }
}