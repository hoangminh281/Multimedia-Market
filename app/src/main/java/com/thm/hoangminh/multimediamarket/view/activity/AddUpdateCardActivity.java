package com.thm.hoangminh.multimediamarket.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.presenter.AddUpdateCardPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.AddUpdateCardPresenterImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.AddUpdateCardView;

import java.util.ArrayList;

public class AddUpdateCardActivity extends AppCompatActivity implements AddUpdateCardView {
    private Toolbar toolbar;
    private LinearLayout walletLayout;
    private RadioGroup rgCardCategory;
    private EditText edtCardNumber, edtCardSeri;

    private AddUpdateCardPresenter presenter;
    private int checkPositionCardCategory = -1;
    private int checkedPositionCardValue = -1;
    private ArrayList<RadioButton> rbCardValueList;
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

        initPresenter();
        Bundle bundle = getIntent().getExtras();
        presenter.extractBundle(bundle);
    }

    private void initPresenter() {
        presenter = new AddUpdateCardPresenterImpl(this);
    }

    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }

    @Override
    public void showCard(Card card) {
        rbCardCategoryList.get(card.getCategory()).setChecked(true);
        rbCardValueList.get(card.getValue()).setChecked(true);
        edtCardSeri.setText(card.getSeri());
    }

    @Override
    public void initCardUI() {
        rbCardCategoryList.get(0).setChecked(true);
        rbCardValueList.get(0).setChecked(true);
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
                boolean validate = Validate.validateEditTextsToString(this, edtCardSeri);
                if (!validate) return true;
                String number = edtCardNumber.getText().toString().trim();
                String seri = edtCardSeri.getText().toString().trim();
                presenter.addOrUpdateCard(new Card(checkPositionCardCategory, checkedPositionCardValue, number, seri, 1));
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

    @Override
    public void showMessage(int messageId) {
        Toast.makeText(this, getResources().getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backToScreen() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onBackScreenWithResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.Result, 1);
        setResult(RESULT_OK, intent);
        finish();
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
        walletLayout = findViewById(R.id.WalletLayout);
        walletLayout.setVisibility(View.GONE);
        toolbar = findViewById(R.id.toolbar);
    }
}
