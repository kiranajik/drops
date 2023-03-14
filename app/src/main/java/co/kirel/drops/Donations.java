package co.kirel.drops;

public class Donations {
    String btlsgot,RequirementID,DonationId;

    public Donations(String btlsgot, String requirementID, String donationId) {
        this.btlsgot = btlsgot;
        RequirementID = requirementID;
        DonationId = donationId;
    }

    public String getRequirementID() {
        return RequirementID;
    }

    public void setRequirementID(String requirementID) {
        RequirementID = requirementID;
    }

    public String getBtlsgot() {
        return btlsgot;
    }

    public void setBtlsgot(String btlsgot) {
        this.btlsgot = btlsgot;
    }


    public String getDonationId() {
        return DonationId;
    }

    public void setDonationId(String donationId) {
        DonationId = donationId;
    }
}
