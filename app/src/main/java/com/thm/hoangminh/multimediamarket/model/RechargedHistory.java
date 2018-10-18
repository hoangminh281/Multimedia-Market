package com.thm.hoangminh.multimediamarket.model;

public class RechargedHistory {
    private String id, card_id, time;
    private int cardCategory, cardValue;

    public RechargedHistory() {

    }

    public RechargedHistory(String id, String card_id, int cardCategory, int cardValue, String time) {
        this.id = id;
        this.card_id = card_id;
        this.cardCategory = cardCategory;
        this.cardValue = cardValue;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public int getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(int cardCategory) {
        this.cardCategory = cardCategory;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
