package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileNotCdAbleException;

public class CdableVisitor implements Visitor {

	@Override
	public void execute(Directory d) {		
	}

	@Override
	public void execute(FileWithContent f) throws FileNotCdAbleException {
		throw new FileNotCdAbleException(f.getName());
	}
}
