package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.ExtensionNotFoundException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.FileNotAppException;
import pt.tecnico.mydrive.exception.InvalidAppContentException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

import java.util.Set;


public class PlainFile extends PlainFile_Base {
	
    public PlainFile(String name, int id, User owner, String content, Directory father) {
    	init(name,id,owner,content, father);
    }
    public PlainFile(String name, int id, String content) {
    }
    public PlainFile(Element plain_element, User user, Directory father){
        xmlImport(plain_element, user, father);
    }

    public PlainFile() {};
    protected void init(String name, int id, User owner, String content, Directory father){
    	init(name,id,owner, father);
    	setContent(content);
    }
    
    public void addContent(String content){
    	DateTime lt = new DateTime();
    	setLastChange(lt);
    	String t = getContent();
    	t+="\n"+content; 
    	setContent(t);
    }
    
    /*
    @Override
	public void execute(User caller) {
    	String fileName = this.getName();
    	if(fileName.contains(".")) {
    		String[] fileNameParts = fileName.split(".");
    		String extension = fileNameParts[fileNameParts.length - 1];
    		App a = caller.getFileByExtension(extension);
    		a.execute(caller, fileName);
    	}		
	}
    */
    
	@Override
	public void execute(User user) {
		// TODO Auto-generated method stub
		
	}
    
    //FIXME
    public void execute() throws FileNotAppException  {
    	/*
    	String fileName = this.getName();
    	if(fileName.contains(".")) {
    		String[] fileNameParts = fileName.split(".");
    		String extension = fileNameParts[fileNameParts.length - 1];
    		
    	}
    	
    	else {
    		
    	}
    	
    	String[] lines = getContent().split("\n");
    	int nLines = lines.length;
    	for(int i=0; i<nLines; i++) {
    		String[] words = lines[i].split(" ");
    		int nWords = words.length;
    		String pathToApplication = words[0];
        	File f = getFileByPath(pathToApplication, this.getDirectory());
        	if(!(f instanceof App)) {
        		throw new FileNotAppException(f.getName());
        	}
        	App a = (App) f;
        	String fullMethod = a.getContent();
        	String[] methodParts=fullMethod.split(".");
        	if(methodParts.length==3) {
        		String className = methodParts[0] + "." + methodParts[1];
        		//Class<?> c = Class.forName(className);
        		
        	}
        	else if(methodParts.length==2) {
        		
        	}
        	else {
        		throw new InvalidAppContentException(fullMethod);
        	}
    		for(int j=1; j<nWords; j++) {
    			;
    		}
    	*/
    	}
		
    
    
    
//    String input;
//    Thread master = Thread.currentThread();
//    Scanner scan = new Scanner(System.in);
//
//    ProcessBuilder builder;
//    if (args.length == 0) builder = new ProcessBuilder("/bin/bash");
//    else {
//      java.util.List<String> l = new ArrayList<String>();
//      for (String s: args) l.add(s);
//      builder = new ProcessBuilder(l);
//    }
//    builder.redirectErrorStream(true);
//    Process proc = builder.start();
//    OutputStream stdin = proc.getOutputStream ();
//    InputStream stdout = proc.getInputStream ();
//
//    BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
//    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
//
//    Thread throut = new Thread(new Runnable() {
//      @Override
//      public void run() {
//	String line;
//	try {
//	  while ((line = reader.readLine ()) != null) {
//	    out.println ("Stdout: " + line);
//	  }
//	} catch (IOException e) { e.printStackTrace(); }
//	System.err.println ("Stdout is now closed!!!");
//      }
//    } );
//    throut.start();
//
//    /* java 1.7 begin (must add an addition \n at the end)
//    if ((input = scan.nextLine()) != null) {
//      writer.write(input);
//      writer.newLine();
//      writer.flush();
//    }
//    java 1.7 end */
//    /* java 1.8 begin */
//    for (;;) {
//      do
//	try { Thread.sleep(100);
//	} catch (InterruptedException e) { }
//      while(proc.isAlive() && !scan.hasNext());
//      if (proc.isAlive()) {
//	if ((input = scan.nextLine()) != null) {
//	  writer.write(input);
//	  writer.newLine();
//	  writer.flush();
//	}
//      } else break;
//    }
//    /* java 1.8 end */
//    try { proc.waitFor();
//    } catch (InterruptedException e) { }
//
//    System.err.println ("exit: " + proc.exitValue());
//    proc.destroy();
//  }
    
    
    
    
    
    
//    }
    
    public int dimension(){
    	return getContent().length();
    }

    @Override
	public void remove(User user) throws PermissionDeniedException {
    	checkPermissionsRemove(user, getDirectory(), this);
    	setOwner(null);
		setUserPermission(null);
		setOthersPermission(null);
		setDirectory(null);
		deleteDomainObject();
	}
    
    @Override
	public void accept(Visitor v) {
		v.execute(this);
	}
 
    public String toString(){
    	String t = "PlainFile";
    	t+=print();
    	return t;
    }

	public String ls(){
		return getContent();
	}
	
	@Override
	public String read(User user, MyDrive md) throws PermissionDeniedException {
		checkPermissions(user, this, "read");
		return this.getContent();
	}
	
	@Override
	public String read(User user, MyDrive md, Set<String> set){
		checkPermissions(user, this, "read");
		return read(user, md);
	}
	
	@Override
	public void write(User user, String content, MyDrive md) throws FileIsNotWriteAbleException {
		checkPermissions(user, this, "write");
		setContent(content);
	}
	
	@Override
	public void write(User user, String content, MyDrive md, Set<String> cycleDetector)
			throws FileIsNotWriteAbleException {
		checkPermissions(user, this, "write");
		setContent(content);	
	}

//////////////////////////////////////////////////////////////////////////////////////
//                                   XML                               //
//////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void xmlImport(Element element, User owner, Directory father){
        super.xmlImport(element,owner,father);
        String contents= element.getChildText("contents");
        setContent(contents);
    }
    
    @Override
    public void xmlExport(Element element_mydrive ){
        Element element = new Element ("plain");
        element.setAttribute("id", Integer.toString(getId()));
        
        Element path_element = new Element ("path");
        path_element.setText(getAbsolutePath());
        element.addContent(path_element);

        Element name_element = new Element ("name");
        name_element.setText(getName());
        element.addContent(name_element);

        Element owner_element = new Element ("owner");
        owner_element.setText(getOwner().getUsername());
        element.addContent(owner_element);

        Element permission_element = new Element ("perm");
        permission_element.setText(getUserPermission().toString() + getOthersPermission().toString());
        element.addContent(permission_element);

        Element value_element = new Element ("contents");
        value_element.setText(getContent());
        element.addContent(value_element);

        Element lastChange_element = new Element ("lastChange");
    	lastChange_element.setText(getLastChange().toString());
    	element.addContent(lastChange_element);

    	element_mydrive.addContent(element);
    }


 
}
