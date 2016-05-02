package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileNotCdAbleException;

public interface Visitor {
	void execute(File f) throws FileNotCdAbleException;
	void execute(PlainFile f);
	void execute(Directory d);
	void execute(Link l);
	void execute(App a);
}
