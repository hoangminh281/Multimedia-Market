package com.thm.hoangminh.multimediamarket.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.references.Tools;

import java.io.Serializable;

public class Card implements Parcelable {
    private String id;
    private String number, seri;
    private int category; /*0: viettel, 1: mobiphone, 2: vinaphone, 3: vietnamobile, 4: garena*/
    private int value; /*0: 10000, 1: 20000, 2: 50000, 3: 100000, 4: 200000, 5: 500000*/
    private int status; /*1: available, 0: unavailable */
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

    public Card(int category,int value, String number, String seri, int status) {
        this.category = category;
        this.value = value;
        this.number = number;
        this.seri = seri;
        this.status = status;
    }

    public Card(String id, int category,int value, String number, String seri, int status) {
        this.id = id;
        this.category = category;
        this.value = value;
        this.number = number;
        this.seri = seri;
        this.status = status;
    }

    protected Card(Parcel in) {
        id = in.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean compareTo(Card card) {
        if (this.seri.equals(card.seri)) {
            if (this.number.equals(card.number)) {
                return true;
            }
        }
        return false;
    }

    public void LoadCardImageView(ImageView imgAvatar) {
        switch (category) {
            case 0:
                imgAvatar.setImageResource(R.drawable.ic_viettel_checked);
                break;
            case 1:
                imgAvatar.setImageResource(R.drawable.ic_mobiphone_checked);
                break;
            case 2:
                imgAvatar.setImageResource(R.drawable.ic_vinaphone_checked);
                break;
            case 3:
                imgAvatar.setImageResource(R.drawable.ic_vietnammobi_checked);
                break;
            case 4:
                imgAvatar.setImageResource(R.drawable.ic_garena_checked);
                break;
        }
    }

    public void LoadCardStatus(final ImageView imgStatus) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("cards/" + category + "/" + value + "/" + id + "/status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    status = dataSnapshot.getValue(int.class);
                    if (status == 1)
                        imgStatus.setColorFilter(R.color.theme_app);
                    else imgStatus.clearColorFilter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadCardValue(TextView txtValue) {
        txtValue.setText(Tools.FormatMoney(cardValueList[value]) + "");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(number);
        parcel.writeString(seri);
        parcel.writeInt(category);
        parcel.writeInt(value);
        parcel.writeInt(status);
    }
}
