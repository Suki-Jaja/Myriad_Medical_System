package mms.user;


public class Patient extends user {
	private boolean isRegistered;
    //private boolean notification;
    private boolean wantsNotifications;
    private boolean hasOutstandingBill = false;


public Patient(String patientId, String firstName, String lastName, String gender, int age, String email, String phoneNumber, String address, boolean isRegistered) {
	
	super(patientId, firstName, lastName, gender, age, email, null, phoneNumber, address, true);
    this.isRegistered = isRegistered;
   // this.notification = false;
    this.hasOutstandingBill = false;
    this.wantsNotifications = true;
    
}

public boolean wantsNotifications() { 
	
	return wantsNotifications; 
	
}

public void setWantsNotifications(boolean w) { 
	
    this.wantsNotifications = w;
}

public void register(String password) {
	
    this.password = password;
    this.isRegistered = true;
    
}
public boolean hasOutstandingBill() { 
	
	return hasOutstandingBill; 
	
}
public void setOutstandingBill(boolean hasBill) { 
	
	this.hasOutstandingBill = hasBill; 
	
}


@Override
public String toString() {
    return "ID: " + userId + " | Name: " + firstName + " " + lastName + " | Bill Pending: " + (hasOutstandingBill ? "YES" : "No") + " | Notifications: " + (wantsNotifications ? "ON" : "OFF");
}
}