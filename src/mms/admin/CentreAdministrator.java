package mms.admin;

import mms.user.user;  
import mms.user.Patient;
import mms.model.Treatment; 
import mms.model.TreatmentStatus;
import mms.model.Procedure;
import mms.clinician.Clinician;
import java.util.ArrayList;

public class CentreAdministrator extends user {

    
    public CentreAdministrator(String adminId, String firstName, String lastName, String password) {
    	
        super(adminId, firstName, lastName, "Unspecified", 0, "admin@mms.com", password, "N/A", "N/A", true);
        
    }

    public void registerPatient(ArrayList<Patient> patients, Patient p) {
    	
        patients.add(p);
        System.out.println("Admin " + this.getFirstName() + " registered patient: " + p.getUserId());
    }

    public void captureWalkin(ArrayList<Patient> patients, Patient p) {
    	
        patients.add(p);
        System.out.println("Walk-in captured by " + this.getLastName() + ". ID: " + p.getUserId());
    }

    public void flagNonPaying(ArrayList<Patient> allPatients, String patientIdToFlag) {
        
    	boolean found = false;
        for (Patient p : allPatients) {
        	
            if (p.getUserId().equals(patientIdToFlag)) {
            	
                p.setOutstandingBill(true);
                System.out.println("SUCCESS: Patient " + p.getFirstName() + " has been flagged for non-payment.");
                found = true;
                break;
            }
        }
        if (!found) {
        	
            System.out.println("Error! Patient ID not found.");
        }

    }
    
    public void generateBill(ArrayList<Treatment> allTreatments, String patientId) {
    	
        System.out.println("\n--- INVOICE FOR PATIENT: " + patientId + " ---");
        double total = 0;
        boolean foundBillableItems = false;

        for (Treatment t : allTreatments) {
        	
            // to find the treatments for this patient that are not cancelled
            if (t.getPatientId().equals(patientId) && t.getStatus() != TreatmentStatus.CANCELLED) {
                
                
                for (Procedure pr : t.getProcedures()) {
                    System.out.println("- " + pr.getName() + " (" + t.getDate() + "): $" + pr.getCost());
                    total += pr.getCost();
                    foundBillableItems = true;
                }
            }
        }

        if (!foundBillableItems) {
        	
            System.out.println("No billable procedures found for this patient.");
            
        } else {
        	
            System.out.println("TOTAL OUTSTANDING BALANCE: $" + total);
        }
    }

    public void sendNotification(String message) {
    	
        System.out.println("BROADCAST NOTIFICATION from " + this.getUserId() + ": " + message);
    }

    
    public void allocateClinician(Treatment t, Clinician c) {
    	
        t.setClinicianId(c.getUserId());
        System.out.println("Allocated clinician " + c.getLastName() + " to treatment " + t.getTreatmentId());
    }

    public double costAllTreatments(String patientId) {
        // to sum up costs
        return 0.0;
    }
    
}