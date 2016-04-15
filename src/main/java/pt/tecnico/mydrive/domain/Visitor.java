package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileNotCdAbleException;

public interface Visitor {
	void execute(Directory d);
	void execute(PlainFile f);
	void execute(File f) throws FileNotCdAbleException;
}
