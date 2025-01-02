package loyaltycard;
public class User {
	private short id;  
	private byte[] firstName;
    private byte[] lastName;
    private byte[] phone;
    private byte[] identification;
    private byte[] birthday;
    private byte gender;
    private short point;    
    
    
    
		
	 public User() {
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.identification = null;
        this.birthday = null;
        this.gender = 0;
        this.point = 0;
        this.id = 0;
    }
		
    public void setFirstName(byte[] firstName) { this.firstName = firstName; }
    public void setLastName(byte[] lastName) { this.lastName = lastName; }
    public void setPhone(byte[] phone) { this.phone = phone; }
    public void setIdentification(byte[] identification) { this.identification = identification; }
    public void setBirthday(byte[] birthday) { this.birthday = birthday; }
    public void setGender(byte gender) { this.gender = gender; }
	public void addPoint(short pointsToAdd) {
        this.point += pointsToAdd;
    }
    public void subtractPoint(short pointsToSubtract) {
        this.point -= pointsToSubtract;
        if (this.point < 0) this.point = 0;
    }
    public void setPoint(short point) {
        this.point = point;
    }
    public void setID(short id) {
        this.id = id;
    }
    
	
	
    public byte[] getFirstName() { return  this.firstName; }
    public byte[] getLastName() { return this.lastName; }
    public byte[] getPhone() { return this.phone; }
    public byte[] getIdentification() { return this.identification; }
    public byte[] getBirthday() { return this.birthday; }
    public byte getGender() { return this.gender; }
    public short getPoint() {
        return point;
    }
    public short getId() {
		return id;
	}
   
}
