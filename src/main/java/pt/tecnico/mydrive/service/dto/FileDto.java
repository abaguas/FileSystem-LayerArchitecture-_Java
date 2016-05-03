package pt.tecnico.mydrive.service.dto;

public class FileDto implements Comparable<FileDto> {

    private String type;
    private String userPermission;
    private String othersPermission;
    private String dimension;
    private String usernameOwner;
    private String id;
    private String lastChange;
    private String name;

    public FileDto(String descriptionString) {
    	String[] parts = descriptionString.split(" ");
    	type = parts[0];
    	userPermission = parts[1];
    	othersPermission = parts[2];
    	dimension = parts[3];
    	usernameOwner = parts[4];
    	id = parts[5];
    	lastChange = parts[6];
    	name = parts[7];
    }

    public final String getType() {
        return this.type;
    }
    
    public final String getUserPermission() {
        return this.userPermission;
    }
    
    public final String getOthersPermission() {
        return this.othersPermission;
    }
    
    public final String getDimension() {
        return this.dimension;
    }
    
    public final String getUsernameOwner() {
        return this.usernameOwner;
    }
    
    public final String getId() {
        return this.id;
    }
    
    public final String lastChange() {
        return this.lastChange;
    }
    
    public final String getName() {
        return this.name;
    }
    
    @Override
    public int compareTo(FileDto other) {
    	return getName().compareTo(other.getName());
    }
}