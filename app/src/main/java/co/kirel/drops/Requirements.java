package co.kirel.drops;

public class Requirements {

    String Purpose,BloodGroup;

    public Requirements(String purpose, String bloodGroup) {
        Purpose = purpose;
        BloodGroup = bloodGroup;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }
}
