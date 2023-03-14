package co.kirel.drops;

public class Donations {
    String btlsdonated,RequirementID,DonationId;

    public Donations() {}

    public Donations(String btlsdonated, String requirementID, String donationId) {
        this.btlsdonated = btlsdonated;
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
        return btlsdonated;
    }

    public void setBtlsgot(String btlsdonated) {
        this.btlsdonated = btlsdonated;
    }


    public String getDonationId() {
        return DonationId;
    }

    public void setDonationId(String donationId) {
        DonationId = donationId;
    }
}
