package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import ch.qos.logback.core.OutputStreamAppender;
import pt.tecnico.mydrive.exception.ExtensionNotFoundException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.FileNotAppException;
import pt.tecnico.mydrive.exception.InvalidAppContentException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

import java.io.*;
import java.util.*;


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
    
    @Override
    public void execute(User caller, String[] args, MyDrive md) throws PermissionDeniedException {
    	
    	checkPermissions(caller, this, "execute");
    	
		PrintWriter out = new PrintWriter(System.out, true);
		
		String input;
	    Thread master = Thread.currentThread();
	    Scanner scan = new Scanner(this.getContent());
	
	    ProcessBuilder builder;
	    
	    if (args.length == 0) {
	    	builder = new ProcessBuilder("/bin/bash");
	    }
	    
	    else {
	      java.util.List<String> l = new ArrayList<String>();
	      for (String s: args) l.add(s);
	      builder = new ProcessBuilder(l);
	    }
	    
	    builder.redirectErrorStream(true);
	    Process proc = null;
		try {
			proc = builder.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    OutputStream stdin = proc.getOutputStream();
	    InputStream stdout = proc.getInputStream();
	
	    BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
	
	    Thread throut = new Thread(new Runnable() {
	      @Override
	      public void run() {
	    	  String line;
	    	  try {
	    		  while ((line = reader.readLine ()) != null) {
	    			  out.println ("Stdout: " + line);
	    		  }
	    	  } catch (IOException e) { e.printStackTrace(); }
	    	  System.err.println ("Stdout is now closed!!!");
	      	}
	    } );
	    throut.start();
	
	    /* java 1.7 begin (must add an addition \n at the end)
	    if ((input = scan.nextLine()) != null) {
	      writer.write(input);
	      writer.newLine();
	      writer.flush();
	    }
	    java 1.7 end */
	    /* java 1.8 begin */
	    for (;;) {
	      do
		try { Thread.sleep(100);
		} catch (InterruptedException e) { }
	      while(proc.isAlive() && !scan.hasNext());
	      if (proc.isAlive()) {
			if ((input = scan.nextLine()) != null) {
			  try {
				writer.write(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  try {
				writer.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  try {
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	      } else break;
	    }
	    /* java 1.8 end */
	    try { proc.waitFor();
	    } catch (InterruptedException e) { }
	
	    System.err.println ("exit: " + proc.exitValue());
	    proc.destroy();
    }    		
    				
	@Override
	public void execute(User caller, String[] args, MyDrive md, Set<String> cycleDetector) {
		execute(caller, args, md);
		
	}

    
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
        
        super.xmlExport(element);

        Element value_element = new Element ("contents");
        value_element.setText(getContent());
        element.addContent(value_element);

    	element_mydrive.addContent(element);
    }



 
}
