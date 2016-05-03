package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.InvalidFileNameException;
import pt.tecnico.mydrive.exception.PermissionDeniedException;

public abstract class File extends File_Base {

	public File() {
	}

	public File(String name, int id, User owner, Directory father) throws InvalidFileNameException {
		init(name, id, owner, father);
	}

	protected void init(String name, int id, User owner, Directory father) throws InvalidFileNameException {
		if (name.contains("/") || name.contains("\0")) {
			throw new InvalidFileNameException(name);
		}

		DateTime dt = new DateTime();
		setOwner(owner);
		setName(name);
		setId(id);
		setUserPermission(owner.getOwnPermission());
		setOthersPermission(owner.getOthersPermission());
		setLastChange(dt);
		setDirectory(father);
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
}
