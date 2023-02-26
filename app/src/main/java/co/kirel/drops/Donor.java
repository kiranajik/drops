package co.kirel.drops;

public class Donor {
    String Name;
    String bloodgroup;
    String phonenumber;

    public Donor(String name, String bloodgroup, String phonenumber) {
        Name = name;
        this.bloodgroup = bloodgroup;
        this.phonenumber = phonenumber;
    }
    public Donor(){
        Name = "Name";
        this.bloodgroup = "BG";
        this.phonenumber = "Phone Number";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
