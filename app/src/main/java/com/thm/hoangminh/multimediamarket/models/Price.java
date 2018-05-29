package com.thm.hoangminh.multimediamarket.models;

public class Price {
    private int money;
    private String currency;

    public Price() {
    }

    public Price(int money, String currency) {
        this.money = money;
        this.currency = currency;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String toString() {
        return money + currency;
    }
}
