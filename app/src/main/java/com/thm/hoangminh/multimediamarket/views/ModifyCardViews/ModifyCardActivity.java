package com.thm.hoangminh.multimediamarket.views.ModifyCardViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.presenters.ModifyCardPresenters.ModifyCardPresenter;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.views.CardViews.CardActivity;

import java.util.ArrayList;

public class ModifyCardActivity extends AppCompatActivity implements ModifyCardView {
    private Toolbar toolbar;
    private Card card;
    private RadioGroup rgCardCategory;
    private ArrayList<RadioButton> rbCardValueList;
    private int checkedPositionCardValue = -1;
    private int checkPositionCardCategory = -1;
    private EditText edtCardNumber, edtCardSeri;
    private Button btnNext;
    private boolean flag_num, flag_seri;
    private LinearLayout WalletLayout;
    private int mode;
    private ModifyCardPresenter presenter;
    private ArrayList<RadioButton> rbCardCategoryList;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_procedure_layout);
        setControls();
        setEvents();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        WalletLayout.setVisibility(View.GONE);

        initPresenter();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_editCard) + "");
            card = getIntent().getParcelableExtra(CardActivity.requestCode);
            showCardInfomation(card);
            mode = 0;
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_addCard) + "");
            rbCardCategoryList.get(0).setChecked(true);
            rbCardValueList.get(0).setChecked(true);
            mode = 1;
        }
    }

    private void initPresenter() {
        presenter = new ModifyCardPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToScreen();
                return true;
            case R.id.menu_save:
                String number = edtCardNumber.getText().toString().trim();
                if (mode == 1) {
                    if (number.length() == 0) {
                        edtCardNumber.setError(getResources().getString(R.string.err_empty));
                        return true;
                    }
                }
                String seri = edtCardSeri.getText().toString().trim();
                if (seri.length() == 0) {
                    edtCardSeri.setError(getResources().getString(R.string.err_empty));
                    return true;
                }
                if (mode == 1) {
                    presenter.createNewCard(new Card(checkPositionCardCategory, checkedPositionCardValue, Tools.md5(number), seri, 1));
                } else if (mode == 0) {
                    if (number == null || number.equals("")) number = card.getNumber();
                    else number =  Tools.md5(number);
                    presenter.editCard(new Card(card.getId(), checkPositionCardCategory, checkedPositionCardValue, number, seri, 1), card);
                }
                return true;
        }
        return false;
    }

    private void setEvents() {
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
                    }
                }
            });
        }
    }

    private void showCardInfomation(Card card) {
        rbCardCategoryList.get(card.getCategory()).setChecked(true);
        rbCardValueList.get(card.getValue()).setChecked(true);
        edtCardSeri.setText(card.getSeri());
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
        WalletLayout = findViewById(R.id.WalletLayout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void showMessage(int message_id) {
        Toast.makeText(this, getResources().getString(message_id), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backToScreen() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onBackScreenWithOkCode() {
        Intent intent = new Intent();
        intent.putExtra("result", 1);
        setResult(RESULT_OK, intent);
        finish();
    }
}
