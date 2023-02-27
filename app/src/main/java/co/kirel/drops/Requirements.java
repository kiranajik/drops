package co.kirel.drops;

import java.util.HashMap;
import java.util.Map;

public class Requirements {

    String honame,BloodGroup,RequirementId;
    Map<String, String> Referred = new HashMap<>();

    public Requirements(){}

    public Requirements(String honame, String bloodGroup, String requirementId,Map<String,String> Referred) {
        this.honame = honame;
        BloodGroup = bloodGroup;
        RequirementId = requirementId;
        Referred = Referred;
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

    public Map<String, String> getReferred() {
        return Referred;
    }

    public void setReferred(Map<String, String> referred) {
        Referred = referred;
    }
}
