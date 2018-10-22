package com.thm.hoangminh.multimediamarket.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductRating implements Parcelable {
    private String userId;
    private String productId;
    private int point;
    private String comment;
    private String time;

    public ProductRating() {
    }

    public ProductRating(String userId, String productId, int point, String comment, String time) {
        this.userId = userId;
        this.productId = productId;
        this.point = point;
        this.comment = comment;
        this.time = time;
    }

    protected ProductRating(Parcel in) {
        userId = in.readString();
        productId = in.readString();
        point = in.readInt();
        comment = in.readString();
        time = in.readString();
    }

    public static final Creator<ProductRating> CREATOR = new Creator<ProductRating>() {
        @Override
        public ProductRating createFromParcel(Parcel in) {
            return new ProductRating(in);
        }

        @Override
        public ProductRating[] newArray(int size) {
            return new ProductRating[size];
        }
    };

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        if (0 < point && point < 6)
            this.point = point;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(productId);
        parcel.writeInt(point);
        parcel.writeString(comment);
        parcel.writeString(time);
    }
}
