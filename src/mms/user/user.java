package mms.user;

public class user {
	
	protected String userId;
    protected String firstName;
    protected String lastName;
    protected String gender;
    protected int age;
    protected String email;
    protected String password;
    protected String phoneNumber;
    protected String address;
    protected boolean isRegistered;
    

	public user(String userId, String firstName, String lastName, String gender, int age,String email, String password, String phoneNumber, String address, boolean isRegistered) {
		
		this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isRegistered = isRegistered;
		
	}
	
	public boolean login(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
        
	}
	
	public void setPassword(String password) {
		
            this.password = password;
        
	}
	
	public String getUserId() { 
		
		return userId; 
		
	}
    public String getFirstName() { 
    	
    	return firstName; 
    	
    }
    public String getLastName() {
    	
    	return lastName; 
    	
    }
    
    public String getGender() { 
    	
        return gender; 
        
    }
    
    public int getAge() { 
    	
        return age; 
        
    }
    
    public String getEmail() { 
    	
    	return email; 
    	
    }
    public String getPassword() { 
    	
    	return password; 
    	
    }
    
    public String getPhoneNumber() { 
    	
        return phoneNumber; 
        
    }

    public String getAddress() { 
    	
        return address; 
        
    }
    
    public boolean isRegistered() { 
    	
    	return isRegistered; 
    	
    }

  /*  public boolean login(String inputPassword) {
    	
    	return this.password != null && this.password.equals(inputPassword);
    }
   */
}
