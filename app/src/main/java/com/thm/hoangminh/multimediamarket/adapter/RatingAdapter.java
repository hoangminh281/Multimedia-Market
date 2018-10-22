package com.thm.hoangminh.multimediamarket.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.repository.RatingRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.RatingRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;

import java.util.ArrayList;

public class RatingAdapter extends ArrayAdapter<ProductRating> {
    private int limit;
    private int resource;
    private Context context;
    private ArrayList<ProductRating> productRatingList;
    private UserRepository userRepository;
    private RatingRepository ratingRepository;

    public RatingAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ProductRating> productRatingList, int limit) {
        super(context, resource, productRatingList);
        this.limit = limit;
        this.context = context;
        this.resource = resource;
        userRepository = new UserRepositoryImpl();
        this.productRatingList = productRatingList;
        ratingRepository = new RatingRepositoryImpl();
    }

    private class ViewHolder {
        private CheckBox cbLike;
        private RatingBar ratingBar;
        private ImageView imgAvatar, imgOption;
        private TextView txtName, txtDate, txtComment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, null);
            holder = new ViewHolder();

            holder.imgAvatar = convertView.findViewById(R.id.imageViewAvatar);
            holder.cbLike = convertView.findViewById(R.id.imageViewLike);
            holder.imgOption = convertView.findViewById(R.id.imageViewOption);
            holder.txtName = convertView.findViewById(R.id.textViewName);
            holder.txtDate = convertView.findViewById(R.id.textViewDate);
            holder.txtComment = convertView.findViewById(R.id.textViewComment);

            if (limit != -1) {
                holder.txtComment.setMaxLines(3);
                holder.txtComment.setEllipsize(TextUtils.TruncateAt.END);
            }
            holder.ratingBar = convertView.findViewById(R.id.ratingBarUser);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //load user's information of this rating row
        final ProductRating productRating = productRatingList.get(position);
        userRepository.findById(productRating.getUserId(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    ImageLoader.loadImage(UserStorageRepository.class, context, holder.imgAvatar, user.getImage());
                    holder.txtName.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.txtDate.setText(productRating.getTime());
        String comment = productRating.getComment().trim();
        if (comment.length() == 0) {
            holder.txtComment.setVisibility(View.GONE);
        } else {
            holder.txtComment.setText(comment);
        }
        holder.ratingBar.setRating(productRating.getPoint());

        //check current user like this rating row
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ratingRepository.findLikedRatingByUserId(currentUser.getUid(), productRating, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue(int.class) == Constants.RatingLiked)
                        holder.cbLike.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //set event to like and option button
        holder.cbLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ratingRepository.setValueLikedList(currentUser.getUid(), productRating, b ? Constants.RatingLiked : Constants.RatingUnlike, null, null);
            }
        });
        holder.imgOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }
}
