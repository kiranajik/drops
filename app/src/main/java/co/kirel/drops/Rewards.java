package co.kirel.drops;

public class
Rewards {
    String company,gift,code;
    Integer cost;

    public Rewards(){}

    public Rewards(String company, String gift, String code,Integer cost) {
        this.company = company;
        this.gift = gift;
        this.code = code;
        this.cost = cost;
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
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
