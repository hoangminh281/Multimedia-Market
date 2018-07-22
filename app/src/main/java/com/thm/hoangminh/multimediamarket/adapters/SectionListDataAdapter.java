package com.thm.hoangminh.multimediamarket.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.views.ProductDetailViews.ProductDetailActivity;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<Product> itemsList;
    private Context mContext;
    private StorageReference mStorageRef;

    public SectionListDataAdapter(Context context, ArrayList<Product> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_view_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {
        Product product = itemsList.get(i);
        product.setBitmapImage(holder.img, mContext);
        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText(Tools.FormatMoney(product.getPrice()));
        holder.tvRate.setText(product.getRating() + "");
        holder.product = product;
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvRate, tvPrice;
        private ImageView img;
        private Product product;

        private SingleItemRowHolder(View view) {
            super(view);
            this.tvTitle = view.findViewById(R.id.textViewTitle);
            this.img = view.findViewById(R.id.imageViewPhoto);
            this.tvRate = view.findViewById(R.id.textViewRate);
            this.tvPrice = view.findViewById(R.id.textViewPrice);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("cate_id", product.getCate_id());
                    bundle.putString("product_id", product.getProduct_id());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}