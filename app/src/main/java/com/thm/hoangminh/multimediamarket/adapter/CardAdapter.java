package com.thm.hoangminh.multimediamarket.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.view.activity.CardActivity;
import com.thm.hoangminh.multimediamarket.view.activity.ModifyCardActivity;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.SingleItemRowHolder>  {
    private ArrayList<Card> itemsList;
    private Context mContext;

    public final static int ACTIVE_MENU_ID = 2222;
    public final static int INACTIVE_MENU_ID = 3333;

    public CardAdapter(Context context, ArrayList<Card> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_layout, null);
        CardAdapter.SingleItemRowHolder mh = new CardAdapter.SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
        Card card = itemsList.get(position);
        holder.card = card;
        card.LoadCardImageView(holder.imgAvatar);
        card.LoadCardStatus(holder.imgStatus);
        holder.txtCardSeri.setText(card.getSeri());
        card.LoadCardValue(holder.txtValue);
    }

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txtCardSeri, txtValue;
        private ImageView imgAvatar, imgStatus;
        private Card card;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            txtCardSeri = itemView.findViewById(R.id.textViewCardSeri);
            txtValue = itemView.findViewById(R.id.textViewValue);
            imgAvatar = itemView.findViewById(R.id.imageViewAvatar);
            imgStatus = itemView.findViewById(R.id.imageViewStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ModifyCardActivity.class);
                    intent.putExtra(CardActivity.requestCode, card);
                    ((Activity) mContext).startActivityForResult(intent, CardActivity.cardRequestCode);
                }
            });
            itemView.setLongClickable(true);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (card.getStatus() == 0)
                contextMenu.add(0, ACTIVE_MENU_ID, getAdapterPosition(), mContext.getResources().getString(R.string.menu_active));
            else
                contextMenu.add(0, INACTIVE_MENU_ID, getAdapterPosition(), mContext.getResources().getString(R.string.menu_inactive));
        }
    }
}
