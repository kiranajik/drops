package co.kirel.drops;

public class Requirements {

    String honame,BloodGroup,RequirementId;

    public Requirements(){}

    public Requirements(String honame, String bloodGroup, String requirementId) {
        this.honame = honame;
        BloodGroup = bloodGroup;
        RequirementId = requirementId;
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

    public String getRequirementId() {
        return RequirementId;
    }

    public void setRequirementId(String requirementId) {
        RequirementId = requirementId;
    }
}
