package com.thm.hoangminh.multimediamarket.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.references.Tools;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    private List<Product> objects;
    private StorageReference mStorageRef;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
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
            holder.img = convertView.findViewById(R.id.imageViewPhoto);
            holder.txtTitle = convertView.findViewById(R.id.textViewTitle);
            holder.txtRate = convertView.findViewById(R.id.textViewRate);
            holder.txtPrice = convertView.findViewById(R.id.textViewPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = objects.get(position);
        holder.txtTitle.setText(product.getTitle());
        holder.txtRate.setText(product.getRating() + "");
        holder.txtPrice.setText(Tools.FormatMoney(product.getPrice()));
        product.setBitmapImage(holder.img, context);
        return convertView;
    }
}
