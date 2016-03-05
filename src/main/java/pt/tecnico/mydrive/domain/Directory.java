package pt.tecnico.mydrive.domain;

import java.util.Set;
import pt.tecnico.mydrive.exception.*;

public class Directory extends Directory_Base {
    
	//construtor
    //NETO: o file preenche os campos das permissoes pois tem o user
	public Directory(int id, String name, User user, Directory father) {//o mydrive tem de fazer controlo dos ids
        //super(id, name, user); superclasse nao construida
        setFatherDirectory(father);
        setSelfDirectory(this);
    }
	
    //cria uma diretoria
    //as permissoes serao verificadas no mydrive
    
	//LUIS: preciso de id, no catch atualizas o counter se der exceçao
	
	public void createDir(int id, String name, User user) throws FileOrDirectoryAlreadyExistsException { //Sugiro FileAlreadyExistsException pois uma directory é um file
		try {
			search(name);
		} catch (NoSuchFileOrDirectoryException e) {} //verifico se nao existe nenhum ficherio com esse nome
		Directory d = new Directory(id, name, user, this);
    	addFiles((File) d);
    }
	
	public void remove(String name) throws NoSuchFileOrDirectoryException{
		File f = search(name);
		f.remove();
		removeFiles(f);
	}
	
	public void remove() {
		Set<File> files = getFiles();
		for (File f: files) {
   	 		f.remove();
   	 		removeFiles(f);
   	 	}
	}
	
	public File search(String name) throws NoSuchFileOrDirectoryException{
		Set<File> files = getFiles();
		for (File f: files) {
   	 		if (f.getName().equals(name)) {
   	 			return f;
   	 		}
		}
		throw new NoSuchFileOrDirectoryException(name);
	}
	
	public File cdable(File f) throws FileNotDirectoryException{
		Visitor v = new CdableVisitor();
   	 	f.accept(v);
   	 	return f;
   	}
	
	//lista o conteudo da diretoria
	public String ls(){
		String output="";
		Set<File> files = getFiles();
   	 	output+=getFatherDirectory().toString()+getSelfDirectory().toString();
   	 	for (File f: files){
   	 		output+= f.toString();
   	 	}
		return output;
	}
	
	//devolve a descricao da diretoria
	public String toString(){
		String s = "Directory ";
		s+="Dimension: "+dimension();
		s+=super.toString();
		return s;
	}
	
	//devolve a dimensao da diretoria
	public int dimension(){
		return 2 + getFiles().size();
	}
	
	public void accept(Visitor v) throws FileNotDirectoryException{
		v.execute(this);
	}
	
	public Directory get(String name) throws NoSuchFileOrDirectoryException, FileNotDirectoryException{
		File f = search(name);
		Visitor v = new CdableVisitor();
   	 	f.accept(v);
   	 	return (Directory)f;
	}
}
