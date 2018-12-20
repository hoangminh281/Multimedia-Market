package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.fomular.MoneyFormular;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;
import com.thm.hoangminh.multimediamarket.presenter.RechargeHistoryPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.RechargeHistoryPresenterImpl;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeHistoryView;

public class RechargeHistoryActivity extends AppCompatActivity implements RechargeHistoryView {
    private TextView txtValueHeader, txtTransCode, txtTime, txtCardCategory, txtValue,
            txtSeriNum, txtStatus, txtStatusHeader;

    private RechargeHistoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_history_layout);
        setControls();

        initPresenter();
        Bundle bundle = getIntent().getExtras();
        presenter.extractBundle(bundle);
    }

    private void initPresenter() {
        presenter = new RechargeHistoryPresenterImpl(this);
    }

    public void NewRecharge(View view) {
        Intent intent = new Intent(RechargeHistoryActivity.this, RechargeActivity.class);
        startActivity(intent);
    }

    @Override
    public void showRechargeHistory(RechargedHistory rechargedHistory) {
        int cardValue = Constants.CardValueList[rechargedHistory.getCardValue()];
        String value = MoneyFormular.format(cardValue);
        txtValueHeader.setText(value + "");
        txtValue.setText(value + "");
        txtCardCategory.setText(Constants.CardCategoryList[rechargedHistory.getCardCategory()]);
        txtTransCode.setText(rechargedHistory.getId());
        txtTime.setText(rechargedHistory.getTime());

    }

    @Override
    public void showCard(Card card) {
        txtSeriNum.setText(card.getSeri());
        txtStatus.setText(card.getStatus() == 1 ? getResources().getString(R.string.txt_unRecharge) : getResources().getString(R.string.txt_recharged));
        txtStatusHeader.setText(card.getStatus() == 1 ? getResources().getString(R.string.txt_failure) : getResources().getString(R.string.txt_success));
    }

    private void setControls() {
        txtValueHeader = findViewById(R.id.textViewValueHeader);
        txtTransCode = findViewById(R.id.textViewTransCode);
        txtTime = findViewById(R.id.textViewTime);
        txtCardCategory = findViewById(R.id.textViewCardCategory);
        txtValue = findViewById(R.id.textViewValue);
        txtSeriNum = findViewById(R.id.textViewSeriNum);
        txtStatus = findViewById(R.id.textViewStatus);
        txtStatusHeader = findViewById(R.id.textViewStatusHeader);
    }

}
