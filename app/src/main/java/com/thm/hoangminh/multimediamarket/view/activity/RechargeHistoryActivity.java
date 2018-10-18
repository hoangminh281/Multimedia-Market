package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargeHistory;
import com.thm.hoangminh.multimediamarket.presenter.implement.RechargeHistoryPresenter;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeHistoryView;

public class RechargeHistoryActivity extends AppCompatActivity implements RechargeHistoryView {
    private String trans_code;
    private RechargeHistoryPresenter presenter;
    private TextView txtValueHeader, txtTransCode, txtTime, txtCardCategory, txtValue, txtSeriNum, txtStatus, txtStatusHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_history_layout);

        setControls();

        initPresenter();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            trans_code = bundle.getString("trans_code");
            presenter.LoadRechargeHistory(trans_code);
        }

        setEvents();
    }

    private void initPresenter() {
        presenter = new RechargeHistoryPresenter(this);
    }

    public void NewRecharge(View view) {
        Intent intent = new Intent(RechargeHistoryActivity.this, RechargeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoadRechargeHistorySuccess(RechargeHistory rechargeHistory) {
        int cardValue = Card.cardValueList[rechargeHistory.getCardValue()];
        String value = Tools.FormatMoney(cardValue);

        txtValueHeader.setText(value + "");
        txtValue.setText(value + "");
        txtTransCode.setText(trans_code);
        txtTime.setText(rechargeHistory.getTime());
        txtCardCategory.setText(Card.cardCategoryList[rechargeHistory.getCardCategory()]);

    }

    @Override
    public void onLoadCardDetailSuccess(Card card) {
        txtSeriNum.setText(card.getSeri());
        txtStatus.setText(card.getStatus() == 1 ? getResources().getString(R.string.txt_unRecharge) : getResources().getString(R.string.txt_recharged));
        txtStatusHeader.setText(card.getStatus() == 1 ? getResources().getString(R.string.txt_failure) : getResources().getString(R.string.txt_success));
    }

    private void setEvents() {

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
