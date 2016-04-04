package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;

public class WriteAbleVisitor implements Visitor {

	@Override
	public void execute(FileWithContent f) {		
	}

	@Override
	public void execute(Directory d) throws FileIsNotWriteAbleException {
		throw new FileIsNotWriteAbleException(d.getName());
	}
}
