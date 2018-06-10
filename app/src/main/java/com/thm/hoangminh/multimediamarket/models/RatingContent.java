package com.thm.hoangminh.multimediamarket.models;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;

public class RatingContent implements Parcelable {
    private String user_id;
    private int point;
    private String content;
    private String time;

    public RatingContent() {
    }

    public RatingContent(int point, String content) {
        this.point = point;
        this.content = content;
    }

    public RatingContent(float point, String content) {
        this.point = (int) point;
        this.content = content;
    }

    public RatingContent(int point, String content, String time) {
        this.point = point;
        this.content = content;
        this.time = time;
    }

    protected RatingContent(Parcel in) {
        user_id = in.readString();
        point = in.readInt();
        content = in.readString();
        time = in.readString();
    }

    public static final Creator<RatingContent> CREATOR = new Creator<RatingContent>() {
        @Override
        public RatingContent createFromParcel(Parcel in) {
            return new RatingContent(in);
        }

        @Override
        public RatingContent[] newArray(int size) {
            return new RatingContent[size];
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void LoadImageViewUser(final ImageView img, final Context context) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef.child("users/" + user_id + "/image").addListenerForSingleValueEvent(new ValueEventListener() {
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
        mRef.child("users/" + user_id + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
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
        parcel.writeString(user_id);
        parcel.writeInt(point);
        parcel.writeString(content);
        parcel.writeString(time);
    }
}
