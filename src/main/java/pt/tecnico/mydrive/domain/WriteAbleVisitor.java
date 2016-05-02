package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;
import pt.tecnico.mydrive.exception.FileNotCdAbleException;

public class WriteAbleVisitor implements Visitor {

	@Override
	public void execute(File f) throws FileIsNotWriteAbleException {
	}
	
	@Override
	public void execute(PlainFile f) {		
	}

	@Override
	public void execute(Directory d) throws FileIsNotWriteAbleException {
		throw new FileIsNotWriteAbleException(d.getName());
	}

	@Override
	public void execute(Link l) {
	}

	@Override
	public void execute(App a) {
	}


}
