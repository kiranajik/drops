package co.kirel.drops;

public class Rewards {
    String company,gift;

    public Rewards(){}

    public Rewards(String company, String gift) {
        this.company = company;
        this.gift = gift;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }
}
