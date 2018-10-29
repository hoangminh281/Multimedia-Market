package com.thm.hoangminh.multimediamarket.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
    private String cardId;
    private String number, seri;
    private int category;
    private int value;
    private int status;

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

    public Card(int category,int value, String number, String seri, int status) {
        this.category = category;
        this.value = value;
        this.number = number;
        this.seri = seri;
        this.status = status;
    }

    public Card(String cardId, int category,int value, String number, String seri, int status) {
        this.cardId = cardId;
        this.category = category;
        this.value = value;
        this.number = number;
        this.seri = seri;
        this.status = status;
    }

    protected Card(Parcel in) {
        cardId = in.readString();
        number = in.readString();
        seri = in.readString();
        category = in.readInt();
        value = in.readInt();
        status = in.readInt();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cardId);
        parcel.writeString(number);
        parcel.writeString(seri);
        parcel.writeInt(category);
        parcel.writeInt(value);
        parcel.writeInt(status);
    }
}
