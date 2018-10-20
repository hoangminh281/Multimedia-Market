package com.thm.hoangminh.multimediamarket.model;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;

public class ProductRating implements Parcelable {
    private String userId;
    private String productId;
    private int point;
    private String content;
    private String time;

    public ProductRating() {
    }

    public ProductRating(String userId, String productId, int point, String content, String time) {
        this.userId = userId;
        this.productId = productId;
        this.point = point;
        this.content = content;
        this.time = time;
    }

    protected ProductRating(Parcel in) {
        userId = in.readString();
        productId = in.readString();
        point = in.readInt();
        content = in.readString();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void LoadImageViewUser(final ImageView img, final Context context) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef.child("users/" + userId + "/image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mStorageRef.child("users/" + dataSnapshot.getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(context)
                                    .load(uri)
                                    .placeholder(R.mipmap.icon_app_2)
                                    .error(R.mipmap.icon_app_2)
                                    .into(img);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void LoadUserName(final TextView txt) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("users/" + userId + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    txt.setText(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        parcel.writeString(content);
        parcel.writeString(time);
    }

    public void CheckCurrentUserLike(final CheckBox checkBox) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("liked_rating/" + productId + "/" + userId + "/" + user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                if (dataSnapshot.exists()) {
                    i = dataSnapshot.getValue(int.class);
                }
                checkBox.setChecked(i == 0 ? false : true);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        ProductRating.this.LikeRatingContent(b);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LikeRatingContent(boolean b) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("liked_rating/" + productId + "/" + userId + "/" + user.getUid()).setValue(b ? 1 : 0);
    }
}
