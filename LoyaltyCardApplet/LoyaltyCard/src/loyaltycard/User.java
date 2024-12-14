package loyaltycard;
public class User {
	private byte[] firstName;
    private byte[] lastName;
    private byte[] phone;
    private byte[] identification;
    private byte[] birthday;
    private byte gender;
    
    
    
		
	 public User() {
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.identification = null;
        this.birthday = null;
        this.gender = 0;
    }
		
    public void setFirstName(byte[] firstName) { this.firstName = firstName; }
    public void setLastName(byte[] lastName) { this.lastName = lastName; }
    public void setPhone(byte[] phone) { this.phone = phone; }
    public void setIdentification(byte[] identification) { this.identification = identification; }
    public void setBirthday(byte[] birthday) { this.birthday = birthday; }
    public void setGender(byte gender) { this.gender = gender; }
   

    public byte[] getFirstName() { return  this.firstName; }
    public byte[] getLastName() { return this.lastName; }
    public byte[] getPhone() { return this.phone; }
    public byte[] getIdentification() { return this.identification; }
    public byte[] getBirthday() { return this.birthday; }
    public byte getGender() { return this.gender; }
   
}
