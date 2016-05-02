package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileNotCdAbleException;

public class CdableVisitor implements Visitor {

	@Override
	public void execute(File f) {
	}
	
	@Override
	public void execute(PlainFile f) throws FileNotCdAbleException  {
		throw new FileNotCdAbleException(f.getName());
	}

	@Override
	public void execute(Directory d) {		
	}

	@Override
	public void execute(Link l) throws FileNotCdAbleException {
		throw new FileNotCdAbleException(l.getName());
	}

	@Override
	public void execute(App a) throws FileNotCdAbleException {
		throw new FileNotCdAbleException(a.getName());
	}

}
