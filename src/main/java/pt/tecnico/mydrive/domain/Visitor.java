package pt.tecnico.mydrive.domain;

public interface Visitor {
	void execute(Directory d);
	void execute(FileWithContent f);
}
