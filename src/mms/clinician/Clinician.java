package mms.clinician;

import mms.user.user;

public class Clinician extends user {
	
    private String specialization;

    public Clinician(String clinicianId, String firstName, String lastName, String gender, String specialization, String password) {
       
    	super(clinicianId, firstName, lastName, gender, 0, "N/A", password, "N/A", "N/A", true);
    	
        this.specialization = specialization;
        
        //this.password = password;
    }

    public String getSpecialization() { 
    	
    	return specialization; 
    	
    }
    
    public String getFullName() { 
    	
    	return firstName + " " + lastName; 
    	
    }

    @Override
    public String toString() {
        return getUserId() + "," + firstName + "," + lastName + "," + gender + "," + specialization + "," + password;
    }
    
}