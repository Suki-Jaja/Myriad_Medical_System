package mms.model;

import java.io.Serializable;

public class Procedure {
	
	private String pId; // Patient Id
    private String name;
    private double cost;
    private String description;

	public Procedure(String pId, String name, double cost, String description) {
		
		this.pId = pId;
        this.name = name;
        this.cost = cost;
        this.description = description;
	}

	public String getPId() { 
		
		return pId; 
		
	}
    public String getName() { 
    	
    	return name; 
    	
    }
    public double getCost() {
    	
    	return cost; 
    	
    }
    public String getDescription() { 
    	
    	return description; 
    	
    }

    // 4. toString() for easy printing
    @Override
    public String toString() {
        return pId + ": " + name + " - " + description + " ($" + cost + ")";
    }

}
