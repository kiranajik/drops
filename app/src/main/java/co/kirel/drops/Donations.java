package co.kirel.drops;

public class Donations {
    String btlsdonated,RequirementId,DonationId,DonationStatus,honame;

    public Donations() {}

    public Donations(String btlsdonated, String requirementId, String donationId, String donationStatus) {
        this.btlsdonated = btlsdonated;
        RequirementId = requirementId;
        DonationId = donationId;
        DonationStatus = donationStatus;
        this.honame = honame;
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

    public String getHoname() { return honame; }

    public void setHoname(String honame) { this.honame = honame; }
}
