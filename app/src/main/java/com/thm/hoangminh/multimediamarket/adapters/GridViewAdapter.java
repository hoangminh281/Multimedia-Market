package com.thm.hoangminh.multimediamarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Game;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Game> {
    private Context context;
    private int resource;
    private List<Game> objects;

    public GridViewAdapter(@NonNull Context context, int resource, @NonNull List<Game> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    private class ViewHolder {
        ImageView img;
        TextView txtTitle, txtRate, txtPrice;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, null);
            holder = new ViewHolder();
            holder.img =  convertView.findViewById(R.id.imageViewPhoto);
            holder.txtTitle =  convertView.findViewById(R.id.textViewTitle);
            holder.txtRate = convertView.findViewById(R.id.textViewRate);
            holder.txtPrice = convertView.findViewById(R.id.textViewPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Game game = objects.get(position);
        holder.txtTitle.setText(game.getTitle());
        holder.txtRate.setText(game.getRate() + "");
        holder.txtPrice.setText(game.getPrice().getMoney() + game.getPrice().getCurrency());
        game.setBitmapImage(holder.img);// not working exactly
        return convertView;
    }
}
