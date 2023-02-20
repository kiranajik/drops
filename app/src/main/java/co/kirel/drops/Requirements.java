package co.kirel.drops;

public class Requirements {

    String honame,BloodGroup;

    public Requirements(){}

    public Requirements(String honame, String bloodGroup) {
        this.honame = honame;
        BloodGroup = bloodGroup;
    }

    public String getHoname() {
        return honame;
    }

    public void setHoname(String honame) {
        this.honame = honame;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }
}
