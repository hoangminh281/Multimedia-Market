package com.thm.hoangminh.multimediamarket.view.RechargeViews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.presenters.RechargePresenters.RechargePresenter;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.view.RechargeHistoryViews.RechargeHistoryActivity;

import java.util.ArrayList;

public class RechargeActivity extends AppCompatActivity implements RechargeView {
    private RechargePresenter presenter;
    private RadioGroup rgCardCategory;
    private ArrayList<RadioButton> rbCardValueList;
    private int checkedPositionCardValue = -1;
    private int checkPositionCardCategory = -1;
    private EditText edtCardNumber, edtCardSeri;
    private Button btnNext;
    private TextView txtTotal;
    private double balance = 0;
    private boolean flag_num, flag_seri;
    private Toolbar toolbar;
    private ArrayList<RadioButton> rbCardCategoryList;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_procedure_layout);

        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.btn_recharge) + "");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        initPresenter();

        presenter.LoadUserWallet();

        setEvents();

        rbCardCategoryList.get(0).setChecked(true);
        rbCardValueList.get(0).setChecked(true);
    }

    private void initPresenter() {
        presenter = new RechargePresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    public void onClicked_btnNext(View view) {
        presenter.RechargeCard(new Card(Tools.md5(edtCardNumber.getText().toString()), edtCardSeri.getText().toString()), checkPositionCardCategory, checkedPositionCardValue);
    }

    private void isOpenBtnNext() {
        if (flag_num && flag_seri) {
            btnNext.setBackground(getResources().getDrawable(R.drawable.blue_radius_button));
            btnNext.setEnabled(true);
        }
    }

    private void setEvents() {
        edtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cardNumber = edtCardNumber.getText().toString().trim();
                if (cardNumber.length() == 0) {
                    edtCardNumber.setError(getResources().getString(R.string.err_empty));
                    flag_num = false;
                }
                flag_num = true;
                isOpenBtnNext();
            }
        });
        edtCardSeri.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cardSeri = edtCardSeri.getText().toString().trim();
                if (cardSeri.length() == 0) {
                    edtCardSeri.setError(getResources().getString(R.string.err_empty));
                    flag_seri = false;
                }
                flag_seri = true;
                isOpenBtnNext();
            }
        });

        rgCardCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for (int j = 0; j < rbCardCategoryList.size(); j++) {
                    if (rbCardCategoryList.get(j).isChecked()) {
                        checkPositionCardCategory = j;
                        break;
                    }
                }
            }
        });

        for (final RadioButton rb : rbCardValueList) {
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        rb.setTextColor(getResources().getColor(R.color.white));
                        if (checkedPositionCardValue != -1) {
                            rbCardValueList.get(checkedPositionCardValue).setChecked(false);
                            rbCardValueList.get(checkedPositionCardValue).setTextColor(getResources().getColor(R.color.black));
                        }
                        checkedPositionCardValue = rbCardValueList.indexOf(rb);
                        txtTotal.setText(Tools.FormatMoney(balance + Card.cardValueList[checkedPositionCardValue]));

                    }
                }
            });
        }
    }

    private void setControls() {
        rgCardCategory = findViewById(R.id.radioGroupCardCategory);

        rbCardCategoryList = new ArrayList<>();
        rbCardCategoryList.add((RadioButton) findViewById(R.id.radioButtonCardCategory1));
        rbCardCategoryList.add((RadioButton) findViewById(R.id.radioButtonCardCategory2));
        rbCardCategoryList.add((RadioButton) findViewById(R.id.radioButtonCardCategory3));
        rbCardCategoryList.add((RadioButton) findViewById(R.id.radioButtonCardCategory4));
        rbCardCategoryList.add((RadioButton) findViewById(R.id.radioButtonCardCategory5));

        rbCardValueList = new ArrayList<>();
        rbCardValueList.add((RadioButton) findViewById(R.id.radioButtonCardValue1));
        rbCardValueList.add((RadioButton) findViewById(R.id.radioButtonCardValue2));
        rbCardValueList.add((RadioButton) findViewById(R.id.radioButtonCardValue3));
        rbCardValueList.add((RadioButton) findViewById(R.id.radioButtonCardValue4));
        rbCardValueList.add((RadioButton) findViewById(R.id.radioButtonCardValue5));
        rbCardValueList.add((RadioButton) findViewById(R.id.radioButtonCardValue6));
        edtCardNumber = findViewById(R.id.editTextCardNumber);
        edtCardSeri = findViewById(R.id.editTextCardSeri);
        btnNext = findViewById(R.id.buttonNext);
        txtTotal = findViewById(R.id.textViewTotal);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void showTotal(double balance) {
        this.balance = balance;
        double total = balance + (checkedPositionCardValue == -1 ? 0 : Card.cardValueList[checkedPositionCardValue]);
        String value = Tools.FormatMoney(total);
        txtTotal.setText(value);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageFromResource(int resource) {
        Toast.makeText(this, getResources().getString(resource), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startRechargeHistoryActivity(Bundle bundle) {
        Intent intent = new Intent(this, RechargeHistoryActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
