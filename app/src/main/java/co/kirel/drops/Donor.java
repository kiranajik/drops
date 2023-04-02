package co.kirel.drops;

public class Donor {

    String Referred_by,Referral_status,Email;

    public Donor()
    {}

    public Donor(String referred_by, String referral_status, String email) {
        Referred_by = referred_by;
        Referral_status = referral_status;
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getReferred_by() {
        return Referred_by;
    }

    public void setReferred_by(String referred_by) {
        Referred_by = referred_by;
    }

    public String getReferral_status() {
        return Referral_status;
    }

    public void setReferral_status(String referral_status) {
        Referral_status = referral_status;
    }

}