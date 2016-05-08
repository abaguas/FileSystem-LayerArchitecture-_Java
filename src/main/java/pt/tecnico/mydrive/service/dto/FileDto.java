package pt.tecnico.mydrive.service.dto;

public class FileDto implements Comparable<FileDto> {

    private String type;  //CHECK
    private String userPermission;  
    private String othersPermission;
    private String dimension;
    private String usernameOwner;
    private String id;  //CHECK
    private String lastChange;  //CHECK
    private String name;  //CHECK

    public FileDto(String descriptionString) {
    	String[] parts = descriptionString.split(" ");
    	type = parts[0];
    	userPermission = parts[1].substring(0, 4);
    	othersPermission = parts[1].substring(4, 8);
    	dimension = parts[2];
    	usernameOwner = parts[3];
    	id = parts[4];
    	lastChange = parts[5];
    	name = parts[6];
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
    
    public final String getLastChange() {
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