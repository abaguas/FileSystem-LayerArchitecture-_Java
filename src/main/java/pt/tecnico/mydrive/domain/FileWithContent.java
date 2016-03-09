package pt.tecnico.mydrive.domain;

import org.jdom2.Element;

public class FileWithContent extends FileWithContent_Base {
	
	public FileWithContent(){}
    
    public FileWithContent(String name, int id, User owner, String content) {
    	//super();
        //super().super(name, id, owner);
    	
        setContent(content);
    }
    
    public FileWithContent(String name, int id, String content) {
    	//super();
        //super().super(name, id);
        setContent(content);
    }
    
    public void addContent(String content){ //Equivalent to setter?
    	String t = getContent();
    	t+="\n"+content; //Has new line?
    }
    
    public void execute(){
     //abstract?
    }
    
    public int dimension(){
    	return getContent().length();
    }
    
    public String toString(){
    	//String s=super().super().toString();
    	String s="";
    	s+=getUserPermission().toString()+" ";
    	s+=dimension()+" ";
    	s+=getOwner().getUsername()+" ";
    	s+=getId()+" ";
    	s+=getLastChange()+" ";
    	s+=getName();
    	return s;
    	// o tipo do ficheiro, as suas permissoes, a dimensao, o username do utilizador a quem pertence, identificador unico, data de modificacao e nome
    	
    }	

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		
	}
	
	public void xmlExport(Element element_mydrive){}
    
}