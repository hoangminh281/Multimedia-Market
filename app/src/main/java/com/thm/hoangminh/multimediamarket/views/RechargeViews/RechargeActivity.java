package com.thm.hoangminh.multimediamarket.views.RechargeViews;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.DecimalFormat;
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

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_procedure_layout);

        setControls();

        initPresenter();

        presenter.LoadUserWallet();

        setEvents();

        rgCardCategory.check(1);
        rbCardValueList.get(0).setChecked(true);
    }

    private void initPresenter() {
        presenter = new RechargePresenter(this, this);
    }

    public void onClicked_btnNext(View view) {
        presenter.RechargeCard(new Card(Tools.md5(edtCardNumber.getText().toString()), edtCardSeri.getText().toString()), checkPositionCardCategory - 1, checkedPositionCardValue);
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
                checkPositionCardCategory = i;
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
                        txtTotal.setText(Tools.FormatDecimal(balance + Card.cardValueList[checkedPositionCardValue]) + "đ");

                    }
                }
            });
        }
    }

    private void setControls() {
        rgCardCategory = findViewById(R.id.radioGroupCardCategory);
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
    }

    @Override
    public void showTotal(double balance) {
        this.balance = balance;
        double total = balance + (checkedPositionCardValue == -1 ? 0 : Card.cardValueList[checkedPositionCardValue]);
        String value = Tools.FormatDecimal(total) + "đ";
        txtTotal.setText(value);
    }

    @Override
    public void onBackScreen() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
