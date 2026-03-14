package mms.storage;

import mms.clinician.Clinician;
import mms.user.Patient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MmsStorage {
    private static MmsStorage instance = null;
    
   
    private final String PATIENT_FILE = "patients.csv";
    private final String CLINICIAN_FILE = "clinicians.csv";

    // Singleton Pattern
    private MmsStorage() {}

    public static MmsStorage getInstance() {
        if (instance == null) instance = new MmsStorage();
        return instance;
    }

        //       --- PATIENT STORAGE ---
    
    public void savePatientsToCSV(ArrayList<Patient> patients) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PATIENT_FILE))) {
            
        	writer.println("patientId,firstName,lastName,gender,age,email,phone,address,isRegistered,hasOutstandingBill,password,notifications");
            
            for (Patient p : patients) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%b,%b,%s,%b",
                        p.getUserId(), 
                        p.getFirstName(), 
                        p.getLastName(), 
                        p.getGender(),
                        p.getAge(),
                        p.getEmail(), 
                        p.getPhoneNumber(), 
                        p.getAddress(), 
                        p.isRegistered(),
                        p.hasOutstandingBill(), 
                        p.getPassword(),
                        p.wantsNotifications()
                );
                writer.println(line);
            }
        } catch (IOException e) { 
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }

    public ArrayList<Patient> loadPatientsCSV() {
    	
        ArrayList<Patient> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PATIENT_FILE))) {
        	
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
            	
                String[] s = line.split(",");
                if (s.length < 11) continue; 
                
                Patient p = new Patient(s[0], s[1], s[2], s[3], Integer.parseInt(s[4]), 
                                      s[5], s[6], s[7], Boolean.parseBoolean(s[8]));
                
                p.setOutstandingBill(Boolean.parseBoolean(s[9]));
                p.register(s[10]);   
                p.setWantsNotifications(Boolean.parseBoolean(s[11]));
                list.add(p);
                
            }
            
        } 
        
        catch (Exception e) {
        	
            System.out.println("No patient file found (or error reading it). Starting fresh.");
            
        }
        return list;
    }

        //          --- CLINICIAN STORAGE ---

    public void saveCliniciansToCSV(List<Clinician> list) {
    	
    	try (PrintWriter pw = new PrintWriter(new FileWriter(CLINICIAN_FILE))) {
   
            pw.println("id,firstName,lastName, gender, specialization, password");
            for (Clinician c : list) {
            	
            	String line = String.format("%s,%s,%s,%s,%s,%s",
            	
            			c.getUserId(),
            			c.getFirstName(),
            			c.getLastName(),
            			c.getGender(),
            			c.getSpecialization(),
            			c.getPassword()
                           
            			);
            	 pw.println(line);
            }
        } 
        
        catch (IOException e) { 
        	
        	System.out.println("Error saving clinicians: " + e.getMessage()); 
        	
        }
    }

    public ArrayList<Clinician> loadCliniciansCSV() {
    	
        ArrayList<Clinician> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CLINICIAN_FILE))) {
           
            String line = br.readLine(); 
            
            
            while ((line = br.readLine()) != null) {
                String[] s = line.split(",");
                
                if (s.length < 6) continue;

                String id = s[0].trim();
                String fName = s[1].trim();
                String lName = s[2].trim();
                String gender = s[3].trim(); 
                String specialization = s[4].trim();
                String password = s[5].trim();  

                Clinician c = new Clinician(id, fName, lName, gender , specialization, password);
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println("No clinicians file found. One will be created.");
        }
        return list;
    }
    
    	//   	--- TREATMENT STORAGE ---
    
    private final String TREATMENT_FILE = "treatments.csv";

    public void saveTreatmentsToCSV(ArrayList<mms.model.Treatment> list) {
    	
        try (PrintWriter pw = new PrintWriter(new FileWriter(TREATMENT_FILE))) {
        	
            pw.println("treatmentId,patientId,clinicianId,date,status");
            for (mms.model.Treatment t : list) {
                
                String line = String.format("%s,%s,%s,%s,%s",
                        t.getTreatmentId(),
                        t.getPatientId(),
                        t.getClinicianId(),
                        t.getDate().toString(), 
                        t.getStatus().name()
                );
                pw.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving treatments: " + e.getMessage());
        }
    }

    public ArrayList<mms.model.Treatment> loadTreatmentsCSV() {
        ArrayList<mms.model.Treatment> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TREATMENT_FILE))) {
            String line = br.readLine(); // Skip Header
            while ((line = br.readLine()) != null) {
                String[] s = line.split(",");
                if (s.length < 5) continue;

                String tId = s[0].trim();
                String pId = s[1].trim();
                String cId = s[2].trim();
                String dateStr = s[3].trim();
                String statusStr = s[4].trim();

                // Converting the String back to LocalDate and Enum
                java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
                
                mms.model.Treatment t = new mms.model.Treatment(tId, pId, date);
                t.setClinicianId(cId);
                
                list.add(t);
            }
        } catch (Exception e) {
            System.out.println("No treatments file found. One will be created.");
        }
        return list;
    }
}