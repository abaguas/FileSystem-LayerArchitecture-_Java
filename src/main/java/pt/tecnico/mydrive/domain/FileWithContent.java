package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.LocalTime;

public class FileWithContent extends FileWithContent_Base {
	
	public FileWithContent(){}
    
    public FileWithContent(String name, int id, User owner, String content, Directory father) {
    	//super(); 
    	init(name,id,owner,content, father);
    }
    
    public FileWithContent(String name, int id, String content) {
    	//super();
    	//FIXME (root)
    }
    
    //Enables inheritance
    protected void init(String name, int id, User owner, String content, Directory father){
    	init(name,id,owner, father);
    	setContent(content);
    }
    
    public void addContent(String content){
    	LocalTime lt = new LocalTime();
    	setLastChange(lt);
    	String t = getContent();
    	t+="\n"+content; //Has new line? 
    	setContent(t);
    }
    
    public void execute(){
    	//FIXME?
    }
    
    public int dimension(){
    	return getContent().length();
    }
    
    public String toString(){
    	
    	return print();
    	
    }
    
    protected String print(){
    	String s="";
    	s+=getUserPermission().toString()+" ";
    	s+=dimension()+" ";
    	s+=getOwner().getUsername()+" ";
    	s+=getId()+" ";
    	s+=getLastChange()+" ";
    	s+=getName();
    	return s;
    }

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		
	}
	
	public void XMLExport(Element element_mydrive){}
	
	public String ls(){
		return getContent();
		//LUIS: Era isto que querias?
	}
    
}