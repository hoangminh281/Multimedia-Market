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

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.fomular.MoneyFormular;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.utility.Validate;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private int resource;
    private Context context;
    private List<Product> products;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> products) {
        super(context, resource, products);
        this.context = context;
        this.resource = resource;
        this.products = products;
    }

    private class ViewHolder {
        private ImageView img;
        private TextView txtTitle, txtRate, txtPrice;
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

        Product product = products.get(position);
        holder.txtTitle.setText(product.getTitle());
        holder.txtRate.setText(product.getRating() + "");
        holder.txtPrice.setText(MoneyFormular.format(product.getPrice()));
        ImageLoader.loadImage(ProductStorageRepository.class, context, holder.img, product.getPhotoId());
        Validate.validateImageProduct(holder.img, product.getStatus());
        return convertView;
    }
}
