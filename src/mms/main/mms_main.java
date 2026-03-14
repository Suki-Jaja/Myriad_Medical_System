package mms.main;

import mms.storage.MmsStorage;


import mms.user.Patient;
import mms.user.user;
import mms.admin.CentreAdministrator;
import mms.clinician.Clinician;
import mms.model.Treatment;
import mms.model.TreatmentStatus;
import mms.util.IdGenerator;
import exceptions.PaymentPendingException;
import mms.model.Procedure;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class mms_main {
	
	private static ArrayList<CentreAdministrator> admins = new ArrayList<>();
    private static ArrayList<Patient> patients = new ArrayList<>();
    private static ArrayList<Clinician> clinicians = new ArrayList<>();
    private static ArrayList<Treatment> treatments = new ArrayList<>();
    
    private static MmsStorage storage = MmsStorage.getInstance();
    private static Scanner scanner = new Scanner(System.in);

    private static CentreAdministrator mainAdmin = new CentreAdministrator("ADM-000", "System", "Admin", "root");
    
    public static void main(String[] args) {
    	
        //    --- LOAD DATA ---
    	
    	System.out.println("Loading system data...");
        patients = storage.loadPatientsCSV();
        clinicians = storage.loadCliniciansCSV();
        treatments = storage.loadTreatmentsCSV();
        
        
       //4 team so 4 admin
        admins.add(new CentreAdministrator("ADM-001", "Rabeya", "Ferdousi", "pass1"));
        admins.add(new CentreAdministrator("ADM-002", "Aeysha", "Siddique", "pass2"));
        admins.add(new CentreAdministrator("ADM-003", "Olasunkanmi", "Lanlehin", "pass3"));
        admins.add(new CentreAdministrator("ADM-004", "Thinesh", "Vijayakumar", "pass4"));

        // TEsttt!!
        if (clinicians.isEmpty()) {
            clinicians.add(new Clinician("C-001","Rabeya","Ferdousi","F","Cardiology","pass1"));
            clinicians.add(new Clinician("C-002","Ayesha","Siddique","F","Physiotherapy","pass2"));
            
            // Saves to file immediately
            storage.saveCliniciansToCSV(clinicians); 
        }

        while (true) {
            System.out.println("\n--- Welcome to Myriad Medical System ---");
            System.out.println("1. Login");
            System.out.println("2. Register (Patient Only)");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": findUserLogin(); break;
                case "2": registerPatient(); break;
                case "3":
                    System.out.println("Saving before exit...");
                    storage.savePatientsToCSV(patients);
                    storage.saveCliniciansToCSV(clinicians);
                    storage.saveTreatmentsToCSV(treatments);
                    System.out.println("Saved..");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }

    }
 // --- LOGIN ---
    
    private static void findUserLogin() {
        System.out.print("Enter your ID(ADM-xxx / C-xxx / P-xxx): ");
        String id = scanner.nextLine().trim();

        if (id.startsWith("ADM-")) {
        	loginAdmin(id);
        } 
        else if (id.startsWith("C-")) {
        	loginClinician(id);
        } 
        else if (id.startsWith("P-")) {
        	loginPatient(id);
        } 
        else {
            System.out.println("Invalid ID format.");
        }
    }
    
    
    
    // 		---	ADMIN ---
    
    private static void loginAdmin(String id) {
    	
        CentreAdministrator admin = null;
        for (CentreAdministrator a : admins) {
        	
            if (a.getUserId().equals(id)) { admin = a; break; }
        }

        if (admin == null) { System.out.println("Admin not found."); return; }

        System.out.print("Enter Password: ");
        if (admin.login(scanner.nextLine())) {
            System.out.println("Welcome Admin " + admin.getLastName());
            adminMenu(admin);
        } else {
            System.out.println("Incorrect Password.");
        }
    }

    private static void adminMenu(CentreAdministrator admin) {
        boolean sessionActive = true;
        while (sessionActive) {
            System.out.println("\n--- Administrator Panel ---");
            System.out.println("1. Register New User (Patient/Clinician)");
            System.out.println("2. Flag a Patient (Payment Issue)");
            System.out.println("3. Allocate Clinician to Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Generate Patient Bill");
            System.out.println("6. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine()) {
                case "1":
                    handleAdminRegistration(admin); 
                    break;
                case "2":
                    System.out.print("Enter Patient ID to Flag: ");
                    String pid = scanner.nextLine();
                    admin.flagNonPaying(patients, pid);
                    break;
                case "3": 
                    allocateClinician(admin); 
                    break;
                case "4": handleCancellation(admin); 
                	break;
                case "5":
                	System.out.print("Enter Patient ID for Invoice: ");
                    String billPid = scanner.nextLine();
                    admin.generateBill(treatments, billPid);
                    break;
                case "6":
                    sessionActive = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    // Assigning Clinician 
    
    private static void allocateClinician(CentreAdministrator admin) {
    	
        System.out.println("\n--- Allocate Clinician ---");
        
        // To Show Unassigned Treatments
        System.out.println("Unassigned Appointment/Treatment:");
        boolean found = false;
        for (Treatment t : treatments) {
        	
            if (t.getClinicianId().equals("Unassigned")) {
            	
                System.out.println(t.toString());
                found = true;
            }
        }
        
        if (!found) {
        	
            System.out.println("No unassigned appointment found.");
            return;
        }

        System.out.print("Enter Treatment ID to assign: ");
        String tId = scanner.nextLine();
        Treatment selectedT = null;
        for (Treatment t : treatments) {
        	
            if (t.getTreatmentId().equals(tId)) { 
            	
            	selectedT = t; 
            	break; 
            	
            }
        }
        
        if (selectedT == null) { 
        	
        	System.out.println("Invalid Treatment ID."); 
        	return; 
        	
        }

        System.out.print("Enter Clinician ID to assign (C-): ");
        String cId = scanner.nextLine();
        
        boolean clinicianExists = false;
        for (Clinician c : clinicians) {
        	
            if (c.getUserId().equals(cId)) { 
            	
            	clinicianExists = true; 
            	break; 
            	
            }
        }
        
        if (!clinicianExists) { 
        	
        	System.out.println("Clinician not found."); 
        	return; 
        	
        }

        selectedT.setClinicianId(cId);
        System.out.println("Success! Treatment " + tId + " is now assigned to " + cId);
    }
    
    // -- IN-ADMIN REGISTRATION MENU -- 
    
    private static void handleAdminRegistration(CentreAdministrator admin) {
    	
        System.out.println("\n--- Register User ---");
        System.out.println("1. Capture Walk-In Patient");
        System.out.println("2. Onboard New Clinician");
        System.out.println("3. Back");
        System.out.print("Select User Type: ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                registerWalkInPatient(admin); 
                break;
            case "2":
                registerClinician();     
                break;
            case "3":
                return;
            default:
                System.out.println("Invalid selection.");
        }
    }

 //  -- REGISTER CLINICIANS --
    private static void registerClinician() {
        System.out.println("\n--- Onboard Clinician ---");
        System.out.print("First Name: "); String fn = scanner.nextLine();
        System.out.print("Last Name: "); String ln = scanner.nextLine();
        System.out.print("Gender: "); String gender = scanner.nextLine();
        System.out.print("Specialization (e.g. Dermatology): "); String spec = scanner.nextLine();
        System.out.print("Set Password: "); String pass = scanner.nextLine();

        // Generate ID 
        String cId = IdGenerator.generate("C");

        Clinician newClinician = new Clinician(cId, fn, ln, gender ,spec, pass);
        
        // Add to List & Save
        clinicians.add(newClinician);
        storage.saveCliniciansToCSV(clinicians); 
        
        System.out.println("Success! Clinician " + fn + " " + ln + " registered with ID: " + cId);
    }
    
 //  -- REGISTER WALK-INS --
    
    private static void registerWalkInPatient(CentreAdministrator admin) {
    	
        System.out.println("\n--- Capture Walk-In Patient ---");
        registerPatient(); 
        
        System.out.println("(Note: Patient capture logged by Admin " + admin.getUserId() + ")");
    }
    
    
    
    //          --- PATIENTS ---
    
    private static void loginPatient(String id) {
    	
        Patient p = findPatient(id);
        if (p == null) { System.out.println("Error: Patient ID not found."); return; }

        System.out.print("Enter Password: ");
        if (p.login(scanner.nextLine())) {
        	
            System.out.println("\nLogin Successful! Welcome " + p.getFirstName());
            patientMenu(p);
        } 
        
        else { System.out.println("Error: Incorrect Password."); 
        
        }
    }

    private static void patientMenu(Patient p) {
    	
        boolean sessionActive = true;
        while (sessionActive) {
            System.out.println("\n--- Patient Dashboard (" + p.getFirstName() + ") ---");
            System.out.println("1. View My Profile");
            System.out.println("2. Book an Appointment");
            System.out.println("3. Cancel an Appointment");
            System.out.println("4. Toggle Notifications (Currently: " + (p.wantsNotifications() ? "ON" : "OFF") + ")");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            
            switch (scanner.nextLine()) {
                case "1":
                    System.out.println(p.toString());
                    break;
                case "2":
                    try {
                        bookTreatment(p);
                    } 
                    catch (PaymentPendingException e) {
                    	
                        System.out.println("\n!!! BOOKING BLOCKED !!!");
                        System.out.println("Reason: " + e.getMessage());
                        System.out.println("Please contact the front desk.");
                    } 
                    catch (DateTimeParseException e) {
                    	
                        System.out.println("Error: Invalid date format. Use YYYY-MM-DD.");
                    }
                    break;
                case "3": handleCancellation(p); 
                	break;
                case "4": 
                    boolean currentStatus = p.wantsNotifications();
                    p.setWantsNotifications(!currentStatus);
                    System.out.println("Success! Notifications turned " + (p.wantsNotifications() ? "ON" : "OFF"));
                    storage.savePatientsToCSV(patients);
                    break;
                case "5": sessionActive = false; 
                	break;
                default: System.out.println("Invalid choice.");
            }
        }
    }
// -- BOOKING APPOINTMENTS --
    
    private static void bookTreatment(Patient p) throws PaymentPendingException {
    	
        if (p.hasOutstandingBill()) {
            throw new PaymentPendingException("You have an unpaid invoice on your account.");
        }
        
        System.out.println("--- New Booking ---");
        
        System.out.print("Enter Preferred Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);
        String tId = IdGenerator.generate("T");
        Treatment t = new Treatment(tId, p.getUserId(), date);
        treatments.add(t); 
        System.out.println("Success! Treatment " + tId + " booked for " + date);
    }
    
    //   -- CANCLLATIONS -- 
    
    private static void handleCancellation(user u) {
    	
        System.out.println("\n--- Cancel an Appointment ---");
       
        boolean found = false;
        System.out.println("Your Active Bookings:");
        
        for (Treatment t : treatments) {
            
            boolean isMyBooking = (u instanceof Patient) && t.getPatientId().equals(u.getUserId());
            boolean isAdmin     = (u instanceof CentreAdministrator);
            
            if ((isMyBooking || isAdmin) && t.getStatus() != mms.model.TreatmentStatus.CANCELLED) {
            	
                System.out.println(t.toString());
                found = true;
            }
        }

        if (!found) {
        	
            System.out.println("No active appointments found to cancel.");
            return;
        }

        System.out.print("Enter Treatment ID to Cancel: ");
        String tId = scanner.nextLine();
        
        Treatment toCancel = null;
        for (Treatment t : treatments) {
        	
            if (t.getTreatmentId().equals(tId)) {
            	
                toCancel = t;
                break;
            }
        }

        if (toCancel == null) {
        	
            System.out.println("Error: Treatment ID not found.");
            return;
        }

        if (u instanceof Patient && !toCancel.getPatientId().equals(u.getUserId())) {
            System.out.println("Error: You can only cancel your own appointments.");
            return;
        }

        toCancel.setStatus(mms.model.TreatmentStatus.CANCELLED);
        storage.saveTreatmentsToCSV(treatments); // Save immediately
        System.out.println("Success! Appointment " + tId + " has been CANCELLED.");
    }
    

    //          --- CLINICIANS ---
    
    private static void loginClinician(String id) {
        Clinician clinician = null;
        for (Clinician c : clinicians) {
            if (c.getUserId().equals(id)) { 
            	
            	clinician = c; 
            	break; 
            	
            }
        }

        if (clinician == null) { 
        	
        	System.out.println("Clinician not found."); 
        	return; 
        	
        }

        System.out.print("Enter Password: ");
        
     // --- DEBUG LINES (Delete these later) ---
       String inputPass = scanner.nextLine().trim();
       // System.out.println("[DEBUG] ID: " + clinician.getUserId());
      //  System.out.println("[DEBUG] Stored Password in Memory: '" + clinician.getPassword() + "'");
       // System.out.println("[DEBUG] You Typed: '" + inputPass + "'");
        
        if (clinician.login(inputPass)) {
        	
        	System.out.println("Welcome Dr. " + clinician.getLastName());
        	clinicianMenu(clinician);
        	
        } else {
        	
           System.out.println("Incorrect Password.");
           
        }
        
    }
      
        private static void clinicianMenu(Clinician c) {
            boolean session = true;
            while (session) {
                System.out.println("\n--- Clinician Dashboard (" + c.getLastName() + ") ---");
                System.out.println("1. View My Appointments");
                System.out.println("2. Record Procedure (Consultation)");
                System.out.println("3. Logout");
                System.out.print("Choose: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": 
                        viewAppointments(c); 
                        break;
                    case "2": 
                        recordProcedure(c); 
                        break;
                    case "3": 
                        session = false; 
                        break;
                    default: 
                        System.out.println("Invalid option.");
                
            }
                
           }
        }
            
//          CLINICIAN ACTIONS
                
            private static void viewAppointments(Clinician c) {
            	
                System.out.println("\n--- Appointments for Dr. " + c.getLastName() + " ---");
                boolean found = false;
                
                for (Treatment t : treatments) {
                	
                    // Checking if the treatment is assigned to this Clinician
                    if (t.getClinicianId().equals(c.getUserId())) {
                        System.out.println(t.toString());
                        found = true;
                    }
                }
                
                if (!found) {
                    System.out.println("You have no assigned appointments yet.");
                }
            }

            private static void recordProcedure(Clinician c) {
                System.out.println("\n--- Record Procedure ---");
                System.out.print("Enter Treatment ID to process: ");
                String tId = scanner.nextLine();

                // to find the treatment
                Treatment selectedTreatment = null;
                for (Treatment t : treatments) {
                	
                    // it must match Treatment ID and be assigned to this clinician
                    if (t.getTreatmentId().equals(tId) && t.getClinicianId().equals(c.getUserId())) {
                    	
                        selectedTreatment = t;
                        break;
                    }
                }

                if (selectedTreatment == null) {
                    System.out.println("Error: Treatment not found or not assigned to you.");
                    return;
                }

                // Procedure Details
                //System.out.println("Processing Patient: " + selectedTreatment.getPatientId());
                
                Patient p = findPatient(selectedTreatment.getPatientId());
                if (p != null) {
                    System.out.println("  Treating Patient: " + p.getFirstName() + " " + p.getLastName());
                    System.out.println("  (ID: " + p.getUserId() + ")");
                    System.out.println("-----------------------------------------");
                }
                
                System.out.print("Procedure Name (e.g. Filling): ");
                String name = scanner.nextLine();
                
                System.out.print("Cost($): ");
                double cost = 0;
                try {
                    cost = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid cost. cancelled.");
                    return;
                }

                System.out.print("Notes/Description: ");
                String desc = scanner.nextLine();

                // Creating and Adding Procedures
                Procedure pr = new Procedure(selectedTreatment.getPatientId(), name, cost, desc);
                selectedTreatment.addProcedure(pr);
                
                System.out.println("Success! Procedure added. Status updated to: " + selectedTreatment.getStatus());
      
    }
            

    //         --- HELPER METHODS ---
    private static Patient findPatient(String id) {
        for (Patient p : patients) if (p.getUserId().equals(id)) return p;
        return null;
    }

    private static void registerPatient() {
        System.out.println("\n--- Patient Registration Form ---");
        System.out.print("First Name: "); String fn = scanner.nextLine();
        System.out.print("Last Name: "); String ln = scanner.nextLine();
        System.out.print("Gender (M/F/§): "); String gender = scanner.nextLine();
        System.out.print("Age: "); int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Phone: "); String phone = scanner.nextLine();
        System.out.print("Address: "); String addr = scanner.nextLine();
        System.out.print("Create Password: "); String pass = scanner.nextLine();

        // Checked for duplicates (by email)
        for (Patient p : patients) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Error: A patient with this email already exists.");
                return;
            }
        }

        String newId = IdGenerator.generate("P");
       
        Patient newP = new Patient(newId, fn, ln, gender, age, email, phone, addr, true);
        newP.register(pass);
        
        patients.add(newP);
        storage.savePatientsToCSV(patients); 
        
        System.out.println("Registration Complete! Your Patient ID is: " + newId);
    }


    //  Storage Menu .Only admin can access the storage
    private static void storageMenu() {
        System.out.println("\n-- Storage --");
        System.out.println("1. Save All Data");
        System.out.println("2. Load All Data");
        System.out.println("3. Back");
        System.out.print("Choose: ");
        
        String c = scanner.nextLine();
        switch (c) {
            case "1":
                storage.savePatientsToCSV(patients);
                storage.saveCliniciansToCSV(clinicians);
                System.out.println("Saved.");
                break;
            case "2":
                patients = storage.loadPatientsCSV();
                System.out.println("Loaded.");
                break;
            default: break;
        }
    }
}
