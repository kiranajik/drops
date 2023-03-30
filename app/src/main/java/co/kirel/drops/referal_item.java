package co.kirel.drops;

public class referal_item {

    String ref_mail,status,coins;

    public referal_item(){}

    public referal_item(String ref_mail,String status,String coins)
    {
            this.ref_mail = ref_mail;
            this.status = coins;
            this.coins = coins;

    }

    public String getref_mail() {
        return ref_mail;
    }
    public String getstatus() {
        return status;
    }
    public String getcoins() {
        return coins;
    }
}
