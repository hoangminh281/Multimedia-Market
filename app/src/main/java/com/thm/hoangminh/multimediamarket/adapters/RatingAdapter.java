package com.thm.hoangminh.multimediamarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.RatingContent;

import java.util.ArrayList;

public class RatingAdapter extends ArrayAdapter<RatingContent> {
    private Context context;
    private int resource;
    private ArrayList<RatingContent> objects;
    private int limit;

    public RatingAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RatingContent> objects, int limit) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.limit = limit;
    }

    private class ViewHolder {
        ImageView imgAvatar, imgOption;
        TextView txtName, txtDate, txtComment;
        RatingBar ratingBar;
        CheckBox cbLike;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
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

        final RatingContent ratingContent = objects.get(position);
        ratingContent.LoadImageViewUser(holder.imgAvatar, context);
        ratingContent.LoadUserName(holder.txtName);
        holder.txtDate.setText(ratingContent.getTime());
        String st = ratingContent.getContent().trim();
        if (st.length() == 0) {
            holder.txtComment.setVisibility(View.GONE);
        } else {
            holder.txtComment.setText(st);
        }
        holder.ratingBar.setRating(ratingContent.getPoint());
        ratingContent.CheckCurrentUserLike(holder.cbLike);
        holder.imgOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }
}
