package com.thm.hoangminh.multimediamarket.presenter.service.implement;

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
import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.models.RechargeHistory;
import com.thm.hoangminh.multimediamarket.presenter.RechargePresenter;
import com.thm.hoangminh.multimediamarket.repository.CardRepository;
import com.thm.hoangminh.multimediamarket.repository.RechargeHistoryRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CardRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RechargeHistoryRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Tools;
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

    public void rechargeCard(final Card card, final int category, final int value) {
        card.setNumber(Tools.md5(card.getNumber()));
        cardRepository.findByCard(card, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final Card baseCard = dataSnapshot.getValue(Card.class);
                    if (card.compareTo(baseCard)) {
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
                                                            userRepository.setBalance(currentUser.getUid(), currentBalance + Card.cardValueList[value],
                                                                    new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            SimpleDateFormat dateFormatter
                                                                                    = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                                                            RechargeHistory rechargeHistory = new RechargeHistory(baseCard.getId()
                                                                                    , category, value, dateFormatter.format(Calendar.getInstance().getTime()));
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
                    } else {
                        rechargeCardWrong();
                    }
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
