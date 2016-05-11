package pt.tecnico.mydrive.presentation;
import pt.tecnico.mydrive.service.XMLExportService;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import java.io.PrintStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Export extends MyDriveCommand {

    public Export(Shell sh) { super(sh, "export", "xml export the content of MyDrive.\n\tUSAGE: export [filename [name1 name2 ...]]"); }
    public void execute(String[] args) {
	XMLExportService e = new XMLExportService();
	if (args.length != 0) {
	    throw new RuntimeException("USAGE: " + name() + " [<path>]");
	}
	e.execute();
	try {
	    Format form = Format.getPrettyFormat();
	    form.setEncoding("UTF-8");
	    XMLOutputter xmlOutput = new XMLOutputter(form);
	    if (args.length == 0)
		System.out.println(xmlOutput.outputString(e.result()));
	    else
		xmlOutput.output(e.result(), new FileWriter(args[0]));
	} catch (IOException ex) { throw new RuntimeException(ex); }
    }
}