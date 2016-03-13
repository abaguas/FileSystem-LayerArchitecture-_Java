package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

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
    	DateTime lt = new DateTime();
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