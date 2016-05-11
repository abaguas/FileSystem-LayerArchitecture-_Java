package pt.tecnico.mydrive.service.dto;

public class VariableDto implements Comparable<VariableDto> {

    private String name;  
    private String value;  

    public VariableDto(String descriptionString) {
    	String[] parts = descriptionString.split(" ");
    	name = parts[0];
    	value = parts[1];
    }

    public final String getName() {
        return this.name;
    }
    
    public final String getValue() {
        return this.value;
    }
    
    @Override
    public int compareTo(VariableDto other) {
    	return getName().compareTo(other.getName());
    }
}