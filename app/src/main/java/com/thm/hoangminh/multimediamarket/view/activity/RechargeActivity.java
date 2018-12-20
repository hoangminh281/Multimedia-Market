package com.thm.hoangminh.multimediamarket.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.fomular.MoneyFormular;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.presenter.RechargePresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.RechargePresenterImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeView;

import java.util.ArrayList;

public class RechargeActivity extends AppCompatActivity implements RechargeView {
    private Button btnNext;
    private Toolbar toolbar;
    private TextView txtTotal;
    private RadioGroup rgCardCategory;
    private EditText edtCardNumber, edtCardSeri;

    private double balance = 0;
    private RechargePresenter presenter;
    private int checkedPositionCardValue = -1;
    private int checkPositionCardCategory = -1;
    private ArrayList<RadioButton> rbCardValueList;
    private ArrayList<RadioButton> rbCardCategoryList;
    private boolean cardNumberAvailable, cardSeriAvailable;

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
        presenter.loadUserWallet();
        setEvents();
        rbCardCategoryList.get(0).setChecked(true);
        rbCardValueList.get(0).setChecked(true);
    }

    private void initPresenter() {
        presenter = new RechargePresenterImpl(this);
    }

    @Override
    public void showTotal(double balance) {
        this.balance = balance;
        double total = balance + (checkedPositionCardValue == -1 ? 0 : Constants.CardValueList[checkedPositionCardValue]);
        txtTotal.setText(MoneyFormular.format(total));
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

    public void handleRechargeCard(View view) {
        Card card = new Card();
        card.setCategory(checkPositionCardCategory);
        card.setValue(checkedPositionCardValue);
        card.setNumber(edtCardNumber.getText().toString());
        card.setSeri(edtCardSeri.getText().toString());
        presenter.rechargeCard(card);
    }

    private void validate() {
        if (cardNumberAvailable && cardSeriAvailable) {
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
                boolean validated = Validate.validateEditTextsToNumber(RechargeActivity.this, edtCardNumber);
                cardNumberAvailable = validated ? true : false;
                validate();
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
                boolean validated = Validate.validateEditTextsToString(RechargeActivity.this, edtCardSeri);
                cardSeriAvailable = validated ? true : false;
                validate();
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
                        txtTotal.setText(MoneyFormular.format(balance + Constants.CardValueList[checkedPositionCardValue]));

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
    public void showMessage(int messageId) {
        Toast.makeText(this, getResources().getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
