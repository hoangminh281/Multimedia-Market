package com.thm.hoangminh.multimediamarket.presenters.ModifyCardPresenters;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.views.ModifyCardViews.ModifyCardView;

public class ModifyCardPresenter implements ModifyCardListener{
    private ModifyCardInteractor interactor;
    private ModifyCardView listener;

    public ModifyCardPresenter(ModifyCardView listener) {
        this.listener = listener;
        interactor = new ModifyCardInteractor(this);
    }

    public void createNewCard(Card card) {
        interactor.createNewCard(card);
    }

    @Override
    public void onCreateNewCardSuccess() {
        listener.showMessage(R.string.info_successfully_create_card);
        listener.onBackScreenWithOkCode();
    }

    @Override
    public void onCreateNewCardFailure() {
        listener.showMessage(R.string.info_failure_create_card);
    }

    @Override
    public void onEditCardSuccess() {
        listener.showMessage(R.string.info_success_edit_card);
        listener.onBackScreenWithOkCode();
    }

    @Override
    public void onEditCardFailure() {
        listener.showMessage(R.string.info_failure_edit_card);
    }

    public void editCard(Card newCard, Card oldCard) {
        interactor.editCard(newCard, oldCard);
    }
}
