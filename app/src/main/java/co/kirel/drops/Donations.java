package co.kirel.drops;

public class Donations {
    String btlsdonated,RequirementId,DonationId,DonationStatus;

    public Donations() {}

    public Donations(String btlsdonated, String requirementId, String donationId, String donationStatus) {
        this.btlsdonated = btlsdonated;
        RequirementId = requirementId;
        DonationId = donationId;
        DonationStatus = donationStatus;
    }

    public String getRequirementId() {
        return RequirementId;
    }

    public void setRequirementId(String requirementId) {
        RequirementId = requirementId;
    }

    public String getBtlsdonated() {
        return btlsdonated;
    }

    public void setBtlsdonated(String btlsdonated) {
        this.btlsdonated = btlsdonated;
    }

    public String getDonationId() {
        return DonationId;
    }

    public void setDonationId(String donationId) {
        DonationId = donationId;
    }

    public String getDonationStatus() {
        return DonationStatus;
    }

    public void setDonationStatus(String donationStatus) {
        DonationStatus = donationStatus;
    }
}
