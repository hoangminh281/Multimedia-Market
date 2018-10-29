package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.presenter.CardPresenter;
import com.thm.hoangminh.multimediamarket.repository.CardRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CardRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.CardView;

import java.util.ArrayList;

public class CardPresenterImpl implements CardPresenter {
    private CardView listener;
    private FirebaseUser currentUser;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private ValueEventListener eventListener;

    public CardPresenterImpl(CardView listener) {
        this.listener = listener;
        cardRepository = new CardRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void loadCardList() {
        cardRepository.findAndWatch(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> cardCategories = dataSnapshot.getChildren();
                ArrayList<Card> cards = new ArrayList<>();
                for (DataSnapshot cardCategory : cardCategories) {
                    Iterable<DataSnapshot> cardValues = cardCategory.getChildren();
                    for (DataSnapshot cardValue : cardValues) {
                        Iterable<DataSnapshot> cardItems = cardValue.getChildren();
                        for (DataSnapshot cardItem : cardItems) {
                            cards.add(cardItem.getValue(Card.class));
                        }
                    }
                }
                listener.showCardList(cards);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void bidingCurrentUser(final Context context) {
        eventListener = userRepository.findAndWatchRole(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Validate.validateCurrentUserRole(context, dataSnapshot.getValue(int.class),
                            new int[]{Constants.AdminRole});
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void activeOrDeactiveCard(Card card, int status) {
        cardRepository.setStatus(card, status, null, null);
    }

    @Override
    public void removeListener() {
        userRepository.removeFindAndWatchRoleListener(currentUser.getUid(), eventListener);
    }
}
