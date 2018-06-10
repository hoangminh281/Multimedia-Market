package com.thm.hoangminh.multimediamarket.models;

public class Card {
    private String number, seri;/*
    private int category; *//*0: viettel, 1: mobiphone, 2: vinaphone, 3: vietnamobile, 4: garena*//*
    private int value; *//*0: 10000, 1: 20000, 2: 50000, 3: 100000, 4: 200000, 5: 500000*/
    private int status; /*0: available, 1: unavailable */
    public static int[] cardValueList = {10000, 20000, 50000, 100000, 200000, 500000};
    public static String[] cardCategoryList = {"Viettel", "Mobiphone", "Vinaphone", "Vietnamobile", "Garena"};

    public Card() {
    }

    public Card(String number, String seri) {
        this.number = number;
        this.seri = seri;
    }

    public Card(String number, String seri, int status) {
        this.number = number;
        this.seri = seri;
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean compareTo(Card card) {
        if (this.seri.equals(card.seri)) {
            if (this.number.equals(card.number)) {
                return true;
            }
        }
        return false;
    }
}
