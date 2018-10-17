package com.thm.hoangminh.multimediamarket.adapters;

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
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.utility.Validate;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    private List<Product> objects;
    private ProductStorageRepository productStorageRepository;
    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        productStorageRepository = new ProductStorageRepositoryImpl();
    }

    private class ViewHolder {
        ImageView img;
        TextView txtTitle, txtRate, txtPrice;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
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
        holder.txtPrice.setText(MoneyFormular.format(product.getPrice()));
        Validate.validateImageProduct(holder.img, product.getStatus());
        ImageLoader.loadImageProduct(productStorageRepository, context, holder.img, product.getPhotoId());

        return convertView;
    }
}
