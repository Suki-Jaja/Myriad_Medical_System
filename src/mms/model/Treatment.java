package mms.model;

import java.time.LocalDate; 
import java.util.ArrayList;

public class Treatment {
    private String treatmentId;
    private String patientId;
    private String clinicianId;
    private LocalDate date;           
    private TreatmentStatus status;   
    private ArrayList<Procedure> procedures;

    public Treatment(String treatmentId, String patientId, LocalDate date) {
        this.treatmentId = treatmentId;
        this.patientId = patientId;
        this.date = date;
        this.status = TreatmentStatus.NEW; 
        this.clinicianId = "Unassigned";
        this.procedures = new ArrayList<>();
    }
    
    public void setClinicianId(String clinicianId) {
        
    	this.clinicianId = clinicianId;
    	
    	if (this.status == TreatmentStatus.NEW) {
    		
            this.status = TreatmentStatus.ASSESSED;
            
    	}   
    }

    public String getClinicianId() {
		return clinicianId;
	}

	public void addProcedure(Procedure p) {
		
        this.procedures.add(p);
        this.status = TreatmentStatus.ASSESSED;
       
    }
	
	public ArrayList<Procedure> getProcedures() {
		
        return procedures;
    }

    public String getTreatmentId() { 
    	
    	return treatmentId; 
    	
    }
    public String getPatientId() { 
    	
    	return patientId; 
    	
    }
    public TreatmentStatus getStatus() { 
    	
    	return status; 
    	
    }
    
    public void setStatus(TreatmentStatus s) { 
    	
    	this.status = s; 
    
    }
    
    public LocalDate getDate() { 
    	
    	return date; 
    	
    }
    

    @Override
    public String toString() {
        return String.format("ID: %s | Patient: %s | Dr: %s | Date: %s | Status: %s",
                treatmentId, patientId, clinicianId, date, status);
    }
}