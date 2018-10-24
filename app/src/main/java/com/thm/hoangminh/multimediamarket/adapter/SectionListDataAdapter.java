package com.thm.hoangminh.multimediamarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.fomular.MoneyFormular;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.activity.ProductDetailActivity;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {
    private Context context;
    private ArrayList<Product> products;

    public SectionListDataAdapter(Context context, ArrayList<Product> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_view_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {
        Product product = products.get(i);
        ImageLoader.loadImage(ProductStorageRepository.class, context, holder.img, product.getPhotoId());
        Validate.validateImageProduct(holder.img, product.getStatus());
        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText(MoneyFormular.format(product.getPrice()));
        holder.tvRate.setText(product.getRating() + "");
        holder.product = product;
    }

    @Override
    public int getItemCount() {
        return (null != products ? products.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private Product product;
        private TextView tvTitle, tvRate, tvPrice;

        private SingleItemRowHolder(View view) {
            super(view);
            this.tvTitle = view.findViewById(R.id.textViewTitle);
            this.img = view.findViewById(R.id.imageViewPhoto);
            this.tvRate = view.findViewById(R.id.textViewRate);
            this.tvPrice = view.findViewById(R.id.textViewPrice);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CateIdKey, product.getCateId());
                    bundle.putString(Constants.ProductIdKey, product.getProductId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}