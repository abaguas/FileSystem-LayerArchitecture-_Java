package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.FileNotDirectoryException;

public class CdableVisitor implements Visitor {

	@Override
	public void execute(Directory d) {		
	}

	@Override
	public void execute(FileWithContent f) throws FileNotDirectoryException {
		throw new FileNotDirectoryException(f.getName());
	}
}
