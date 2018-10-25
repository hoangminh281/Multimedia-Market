package com.thm.hoangminh.multimediamarket.model;

public class RechargedHistory {
    private String id, cardId, time;
    private int cardCategory, cardValue;

    public RechargedHistory() {
    }

    public RechargedHistory(String cardId, int cardCategory, int cardValue, String time) {
        this.cardId = cardId;
        this.cardCategory = cardCategory;
        this.cardValue = cardValue;
        this.time = time;
    }

    public RechargedHistory(String id, String cardId, int cardCategory, int cardValue, String time) {
        this.id = id;
        this.cardId = cardId;
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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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
