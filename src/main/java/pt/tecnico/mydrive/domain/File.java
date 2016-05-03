package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.ExpiredSessionException;
import pt.tecnico.mydrive.exception.FileAlreadyExistsException;
import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.InvalidTokenException;
import pt.tecnico.mydrive.exception.MaximumPathException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public abstract class File extends File_Base {

	public File() {
	}

	public File(String name, int id, User owner, Directory father) throws InvalidFileNameException {
		init(name, id, owner, father);
	}

	protected void init(String name, int id, User owner, Directory father) throws InvalidFileNameException, FileAlreadyExistsException, MaximumPathException {
		if (name.contains("/") || name.contains("\0")) {
			throw new InvalidFileNameException(name);
		}
		validateFile(name);
		try {
			father.search(name);
			throw new FileAlreadyExistsException(name, id);
		}
		catch (NoSuchFileException e) {
			DateTime dt = new DateTime();
			setOwner(owner);
			setName(name);
			setId(id);
			setUserPermission(owner.getOwnPermission());
			setOthersPermission(owner.getOthersPermission());
			setLastChange(dt);
			setDirectory(father);
		}
	}

	
	public void validateFile(String name) throws MaximumPathException{
		if ((getAbsolutePath().length() + name.length()) > 1024){
			throw new MaximumPathException(name);
		}
		
	}

	protected void initRoot(String name, int id, User owner) throws InvalidFileNameException {
		DateTime dt = new DateTime();
		setOwner(owner);
		setName(name);
		setId(id);
		setUserPermission(owner.getOwnPermission());
		setOthersPermission(owner.getOthersPermission());
		setLastChange(dt);
	}

	public int dimension() {
		return 0;
	}

	public abstract void remove(User user, Directory directory) throws PermissionDeniedException;

	

	public abstract void writeContent(String content);

	public String toString() {
		return print();
	}

	protected String print() {
		String s = " ";
		s += getUserPermission().toString();
		s += getOthersPermission().toString() + " ";
		s += dimension() + " ";
		s += getOwner().getUsername() + " ";
		s += getId() + " ";
		s += getLastChange() + " ";
		s += getName();
		return s;
	}

	public String getAbsolutePath() {
		String path = "";
		Directory current = getDirectory();
		if (getName().equals("/")) {
			path = "/";
			return path;
		}
		if (current.getName().equals("/")) {
			path = "/";
		} else {
			while (!current.getName().equals("/")) {
				path = "/" + current.getName() + path;
				current = current.getFatherDirectory();
			}
		}
		path += "/" + getName();
		return path;
	}

	public void xmlExport(Element element_mydrive) {
	}

	public String ls() {
		return null;
	}

	public void accept(Visitor v) {
		v.execute(this);

	}
	
	public void checkPermissions(User user, Directory directory, String fileName, String code)
			throws PermissionDeniedException {
		if (code.equals("create") || code.equals("delete")) {
			checkFileCreateDeletePermissions(user, directory, fileName, code);	
		}
		else if (code.equals("read") || code.equals("write") || code.equals("execute")) {
			checkFileReadWriteExecutePermissions(user, directory, fileName, code);
		}
		
		else if (code.equals("cd")) {
			checkChangeDirPermission(user, directory, fileName);
		}
		else if (code.equals("ls")) {
			checkListPermission(user, directory);
		}
	}
	public void checkFileCreateDeletePermissions(User user, Directory directory, String fileName, String access)
			throws PermissionDeniedException, ExpiredSessionException, InvalidTokenException {
	
		User owner = directory.getOwner();
		Permission dirOwnP = directory.getUserPermission();
		Permission dirOthP = directory.getOthersPermission();
		
		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (access.equals("delete")) {
				File f = directory.get(fileName);
				if (!f.getUserPermission().getEliminate()) {
					throw new PermissionDeniedException(fileName);
				}
			}
			if (!dirOwnP.getWrite()) {
				throw new PermissionDeniedException(fileName);
			}
		} else {
			if (access.equals("delete")) {
				File f = directory.get(fileName);
				if (!f.getOthersPermission().getEliminate()) {
					throw new PermissionDeniedException(fileName);
				}
			}
			if (!dirOthP.getWrite()) {
				throw new PermissionDeniedException(fileName);
			}
		}
	}

	public void checkFileReadWriteExecutePermissions(User user, Directory directory, String fileName, String access) {
		
		File f = directory.get(fileName);
		User owner = f.getOwner();
		Permission fileUserP = f.getUserPermission();
		Permission fileOthP = f.getOthersPermission();

		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (access.equals("read")) {
				if (!fileUserP.getRead()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("write")) {
				if (!fileUserP.getWrite()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("execute")) {
				if (!fileUserP.getExecute()) {
					throw new PermissionDeniedException(fileName);
				}
			}
		} else {
			if (access.equals("read")) {
				if (!fileOthP.getRead()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("write")) {
				if (!fileOthP.getWrite()) {
					throw new PermissionDeniedException(fileName);
				}
			} else if (access.equals("execute")) {
				if (!fileOthP.getExecute()) {
					throw new PermissionDeniedException(fileName);
				}
			}
		}
	}

	public void checkChangeDirPermission(User user, Directory directory, String fileName) {

		User owner = directory.getOwner();
		Permission dirOwnP = directory.getUserPermission();
		Permission dirOthP = directory.getOthersPermission();

		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (!dirOwnP.getExecute()) {
				throw new PermissionDeniedException(fileName);
			}
		} else {
			if (!dirOthP.getExecute()) {
				throw new PermissionDeniedException(fileName);
			}
		}
	}

	public void checkListPermission(User user, Directory directory) {

		User owner = directory.getOwner();
		Permission dirOwnP = directory.getUserPermission();
		Permission dirOthP = directory.getOthersPermission();

		if (user.getUsername().equals("root")) {
			return;
		}

		else if (user.getUsername().equals(owner.getUsername())) {
			if (!dirOwnP.getRead()) {
				throw new PermissionDeniedException(directory.getName());
			}
		} else {
			if (!dirOthP.getRead()) {
				throw new PermissionDeniedException(directory.getName());
			}
		}
	}
}
