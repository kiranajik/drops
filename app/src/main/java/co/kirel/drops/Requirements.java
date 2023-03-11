package co.kirel.drops;

public class Requirements {

    String honame,BloodGroup,RequirementId,NoofBottles,btlsgot;

    public Requirements(){}

    public Requirements(String honame, String bloodGroup, String requirementId, String noofBottles, String btlsgot) {
        this.honame = honame;
        BloodGroup = bloodGroup;
        RequirementId = requirementId;
        NoofBottles = noofBottles;
        this.btlsgot = btlsgot;
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

    public String getNoofBottles() { return NoofBottles; }

    public void setNoofBottles(String noofBottles) { NoofBottles = noofBottles; }

    public String getBtlsgot() { return btlsgot; }

    public void setBtlsgot(String btlsgot) { this.btlsgot = btlsgot; }
}
