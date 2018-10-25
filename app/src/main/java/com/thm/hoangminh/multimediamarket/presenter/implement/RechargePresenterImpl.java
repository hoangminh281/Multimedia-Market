package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;
import com.thm.hoangminh.multimediamarket.presenter.RechargePresenter;
import com.thm.hoangminh.multimediamarket.repository.CardRepository;
import com.thm.hoangminh.multimediamarket.repository.RechargeHistoryRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CardRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RechargeHistoryRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Tools;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.activity.RechargeHistoryActivity;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RechargePresenterImpl implements RechargePresenter {
    private RechargeView listener;
    private FirebaseUser currentUser;
    private UserRepository userRepository;
    private CardRepository cardRepository;
    private RechargeHistoryRepository rechargeHistoryRepository;

    public RechargePresenterImpl(RechargeView listener) {
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        cardRepository = new CardRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        rechargeHistoryRepository = new RechargeHistoryRepositoryImpl();
    }

    @Override
    public void loadUserWallet() {
        userRepository.findBalance(currentUser.getUid(),
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                            listener.showTotal(dataSnapshot.getValue(double.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void rechargeCard(final Card card) {
        card.setNumber(Tools.md5(card.getNumber()));
        cardRepository.findAll(card.getCategory(), card.getValue(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        final Card baseCard = item.getValue(Card.class);
                        boolean validate = Validate.validateSameCard(baseCard, card);
                        if (validate) {
                            cardRepository.setStatus(baseCard, Constants.CardInactive,
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            userRepository.findBalance(currentUser.getUid(),
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                double currentBalance = dataSnapshot.getValue(double.class);
                                                                userRepository.setBalance(currentUser.getUid(), currentBalance + Constants.CardValueList[baseCard.getValue()],
                                                                        new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                SimpleDateFormat dateFormatter
                                                                                        = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                                                                RechargedHistory rechargeHistory = new RechargedHistory(baseCard.getCardId()
                                                                                        , baseCard.getCategory(), baseCard.getValue(), dateFormatter.format(Calendar.getInstance().getTime()));
                                                                                DatabaseReference mRef = rechargeHistoryRepository.createDataRef(currentUser.getUid());
                                                                                final String transactionId = mRef.getKey();
                                                                                rechargeHistory.setId(transactionId);
                                                                                rechargeHistoryRepository.pushByDataRef(mRef, rechargeHistory,
                                                                                        new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                redirectToRechargeHistoryActivity(transactionId);
                                                                                            }
                                                                                        },
                                                                                        new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                rechargeCardFailure();
                                                                                            }
                                                                                        });

                                                                            }
                                                                        },
                                                                        new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                rechargeCardFailure();
                                                                            }
                                                                        });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            rechargeCardFailure();
                                                        }
                                                    });
                                        }
                                    },
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            rechargeCardFailure();
                                        }
                                    });
                            return;
                        }
                    }
                    rechargeCardWrong();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                rechargeCardWrong();
            }
        });
    }

    private void redirectToRechargeHistoryActivity(String transactionId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TransactionKey, transactionId);
        listener.startActivity(RechargeHistoryActivity.class, bundle);
    }

    private void rechargeCardFailure() {
        listener.showMessage(R.string.info_failure);
    }

    private void rechargeCardWrong() {
        listener.showMessage(R.string.info_wrong);
    }
}
