package com.thm.hoangminh.multimediamarket.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.fomular.MoneyFormular;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.repository.CardRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CardRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.activity.AddUpdateCardActivity;
import com.thm.hoangminh.multimediamarket.view.activity.CardActivity;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.SingleItemRowHolder>  {
    private Context context;
    private ArrayList<Card> itemsList;
    private CardRepository cardRepository;

    public final static int ACTIVE_MENU_ID = 2222;
    public final static int INACTIVE_MENU_ID = 3333;

    public CardAdapter(Context context, ArrayList<Card> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
        cardRepository = new CardRepositoryImpl();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_layout, null);
        CardAdapter.SingleItemRowHolder mh = new CardAdapter.SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int position) {
        Card card = itemsList.get(position);
        holder.card = card;
        holder.imgAvatar.setImageResource(Validate.validateCardCategoryToResource(card.getCategory()));
        holder.txtSeri.setText(card.getSeri());
        holder.txtValue.setText(MoneyFormular.format(Constants.CardValueList[card.getValue()]));
        cardRepository.findAndWatchStatus(card, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int status = dataSnapshot.getValue(int.class);
                    if (status == Constants.CardActive) {
                        holder.imgStatus.setColorFilter(R.color.theme_app);
                    }
                    else {
                        holder.imgStatus.clearColorFilter();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txtSeri, txtValue;
        private ImageView imgAvatar, imgStatus;
        private Card card;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            txtSeri = itemView.findViewById(R.id.textViewCardSeri);
            txtValue = itemView.findViewById(R.id.textViewValue);
            imgAvatar = itemView.findViewById(R.id.imageViewAvatar);
            imgStatus = itemView.findViewById(R.id.imageViewStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddUpdateCardActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.CardObjectKey, card);
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, Constants.CardRequestCode);
                }
            });
            itemView.setLongClickable(true);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (card.getStatus() == 0)
                contextMenu.add(0, ACTIVE_MENU_ID, getAdapterPosition(), context.getResources().getString(R.string.menu_active));
            else
                contextMenu.add(0, INACTIVE_MENU_ID, getAdapterPosition(), context.getResources().getString(R.string.menu_inactive));
        }
    }
}
