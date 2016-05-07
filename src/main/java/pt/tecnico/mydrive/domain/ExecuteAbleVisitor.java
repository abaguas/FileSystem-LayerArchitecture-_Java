package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileIsNotExecuteAbleException;

public class ExecuteAbleVisitor implements Visitor {

	@Override
	public void execute(File f) throws FileIsNotExecuteAbleException {
		//Será que aqui lanço excepção ??
	}
	
	@Override
	public void execute(PlainFile f) {		
	}

	@Override
	public void execute(Directory d) throws FileIsNotExecuteAbleException {
		throw new FileIsNotExecuteAbleException(d.getName());
	}

	@Override
	public void execute(Link l) {
	}

	@Override
	public void execute(App a) {
	}
	
}