package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.implement.ProfilePresenter;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.view.callback.ProfileView;

public class ProfileActivity extends AppCompatActivity implements ProfileView {
    private Toolbar toolbar;
    private ImageView img, imgGender, imgEmail, imgPass;
    private TextView txtName, txtAge, txtRole, txtBalance, txtGame, txtImage, txtVideo, txtMusic, txtUsername, txtEmail, txtBirthday, txtGender;
    private ProfilePresenter presenter;
    private User currentUser;
    private AlertDialog dialog;
    private LinearLayout layoutEdit;
    private ProgressBar pgbDialog;
    private ImageView imgBalance;
    private String user_id;

    private final int REQUEST_CODE_TAKEPHOTO = 1;
    private final int REQUEST_CODE_PICKPHOTO = 2;
    private boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setControls();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
            initPresenter(user_id);
            imgBalance.setVisibility(View.VISIBLE); // allow admin edit balance user
        } else {
            initPresenter("");
        }

        presenter.LoadCurrentUserInformation();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        registerForContextMenu(img);
        img.setLongClickable(false);
    }

    private void initPresenter(String user_id) {
        presenter = new ProfilePresenter(this, user_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.img_dialog_menu, menu);
    }

    public void ShowImageChooseDialog(View view) {
        img.showContextMenu();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTakepic:
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhoto, REQUEST_CODE_TAKEPHOTO);
                return true;
            case R.id.menuChoosepic:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_CODE_PICKPHOTO);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKEPHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(bitmap);
                    currentUser.UpdateImageCurrentUser(this, bitmap);
                }
                break;
            case REQUEST_CODE_PICKPHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    img.setImageURI(selectedImage);
                    currentUser.UpdateImageCurrentUser(this, ((BitmapDrawable) img.getDrawable()).getBitmap());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void ShowCurrentUserInformation(User user) {
        if (user.getRole() == User.ADMIN) {
            imgBalance.setVisibility(View.VISIBLE); // allow admin edit balance user
        }
        this.currentUser = user;
        user.LoadUserImageView(img, this);
        user.LoadUserRole(txtRole);
        user.LoadUserImageGender(imgGender);
        txtName.setText(user.getName());
        if (!user.getBirthday().equals("")) {
            String[] birthdate = user.getBirthday().split("/");
            txtAge.setText(Tools.getAge(Integer.parseInt(birthdate[0]), Integer.parseInt(birthdate[1])
                    , Integer.parseInt(birthdate[2])) + "");
        }
        txtBalance.setText(Tools.FormatMoney(user.getBalance()));

        txtUsername.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtBirthday.setText(user.getBirthday());
        txtGender.setText(user.getSex() == 0 ? R.string.info_male : user.getSex() == 1 ? R.string.info_female : R.string.info_others);
    }

    @Override
    public void showGameNumber(int size) {
        txtGame.setText(size + "");
    }

    @Override
    public void showImageNumber(int size) {
        txtImage.setText(size + "");
    }

    @Override
    public void showVideoNumber(int size) {
        txtVideo.setText(size + "");
    }

    @Override
    public void showMusicNumber(int size) {
        txtMusic.setText(size + "");
    }

    @Override
    public void EnableChangeCurrentUserEmail() {
        ImageViewCompat.setImageTintList(imgEmail, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.theme_app)));
        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditEmail();
            }
        });
    }

    @Override
    public void EnableChangeCurrentUserPassword() {
        ImageViewCompat.setImageTintList(imgPass, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.theme_app)));
        imgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPassword();
            }
        });
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
    public void dismissDialog() {
        if (dialog != null)
            if (dialog.isShowing())
                dialog.dismiss();
    }

    @Override
    public void showProgresbarDialog() {
        pgbDialog.setVisibility(View.VISIBLE);
        layoutEdit.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgresbarDialog() {
        pgbDialog.setVisibility(View.INVISIBLE);
        layoutEdit.setVisibility(View.VISIBLE);
    }

    @Override
    public void editable() {
        editable = true;
    }

    public void EditBalance(View view) {
        View viewDialog = getLayoutInflater().inflate(R.layout.edit_dialog, null);

        TextView txtTitle = viewDialog.findViewById(R.id.textViewTitle);
        txtTitle.setText(R.string.menu_balance);

        final EditText edt = viewDialog.findViewById(R.id.editText);
        edt.setHint(R.string.hint_balance);
        if (currentUser.getBalance() != 0)
            edt.setText(currentUser.getBalance() + "");

        edt.setInputType(InputType.TYPE_CLASS_NUMBER);

        dialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .setPositiveButton(R.string.button_save, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String balance = edt.getText().toString().trim();
                        if (balance.length() == 0) {
                            edt.setError(getResources().getString(R.string.err_empty));
                            return;
                        }
                        presenter.setBalance(Double.valueOf(balance));
                    }
                });
            }
        });
        dialog.show();
    }

    public void EditUsername(final View view) {
        View viewDialog = getLayoutInflater().inflate(R.layout.edit_dialog, null);

        TextView txtTitle = viewDialog.findViewById(R.id.textViewTitle);
        txtTitle.setText(R.string.hint_username);

        final EditText edt = viewDialog.findViewById(R.id.editText);
        edt.setHint(R.string.hint_enter_username);
        if (currentUser.getName() != null)
            edt.setText(currentUser.getName());
        edt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        layoutEdit = viewDialog.findViewById(R.id.layoutEdit);
        pgbDialog = viewDialog.findViewById(R.id.progressBar);

        dialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .setPositiveButton(R.string.button_save, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String st = edt.getText().toString().trim();
                        if (st.length() == 0) {
                            edt.setError(getResources().getString(R.string.err_empty));
                            return;
                        }

                        presenter.EditUsername(st);
                    }
                });
            }
        });
        dialog.show();
    }

    private void EditEmail() {
        View viewDialog = getLayoutInflater().inflate(R.layout.edit_dialog, null);

        TextView txtTitle = viewDialog.findViewById(R.id.textViewTitle);
        txtTitle.setText(R.string.hint_email);

        final EditText edt = viewDialog.findViewById(R.id.editText);
        edt.setHint(R.string.hint_enter_email);
        if (currentUser.getEmail() != null)
            edt.setText(currentUser.getEmail());
        edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        layoutEdit = viewDialog.findViewById(R.id.layoutEdit);
        pgbDialog = viewDialog.findViewById(R.id.progressBar);

        dialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .setPositiveButton(R.string.button_save, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String st = edt.getText().toString().trim();
                        if (st.length() == 0) {
                            edt.setError(getResources().getString(R.string.err_empty));
                            return;
                        }

                        presenter.EditEmail(st);
                    }
                });
            }
        });
        dialog.show();
    }

    private void EditPassword() {
        View viewDialog = getLayoutInflater().inflate(R.layout.password_edit_dialog, null);

        TextView txtTitle = viewDialog.findViewById(R.id.textViewTitle);
        txtTitle.setText(R.string.txt_password);

        final EditText edt = viewDialog.findViewById(R.id.editText);
        edt.setHint(R.string.hint_password);

        CheckBox cbEye = viewDialog.findViewById(R.id.checkBoxEye);
        cbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edt.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    edt.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        final EditText edtRe = viewDialog.findViewById(R.id.editTextRe);
        edtRe.setHint(R.string.hint_repassword);

        CheckBox cbEyeRe = viewDialog.findViewById(R.id.checkBoxEyeRe);
        cbEyeRe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edtRe.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    edtRe.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        layoutEdit = viewDialog.findViewById(R.id.layoutEdit);
        pgbDialog = viewDialog.findViewById(R.id.progressBar);

        dialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .setPositiveButton(R.string.button_save, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String st = edt.getText().toString().trim();
                        String stRe = edtRe.getText().toString().trim();
                        if (st.length() == 0) {
                            edt.setError(getResources().getString(R.string.err_empty));
                            return;
                        } else if (st.length() < 6) {
                            edt.setError(getResources().getString(R.string.err_notlength));
                            return;
                        } else if (stRe.length() == 0) {
                            edtRe.setError(getResources().getString(R.string.err_empty));
                            return;
                        } else if (stRe.length() < 6) {
                            edtRe.setError(getResources().getString(R.string.err_notlength));
                            return;
                        } else if (!st.equals(stRe)) {
                            edtRe.setError(getResources().getString(R.string.err_passnotsame));
                            return;
                        }

                        presenter.EditPassword(st);
                    }
                });
            }
        });
        dialog.show();
    }

    public void EditBirthday(View view) {
        View viewDialog = getLayoutInflater().inflate(R.layout.birthday_edit_dialog, null);

        TextView txtTitle = viewDialog.findViewById(R.id.textViewTitle);
        txtTitle.setText(R.string.hint_birthday);

        final DatePicker datePicker = viewDialog.findViewById(R.id.datePicker);
        if (currentUser.getBirthday() != null && !currentUser.getBirthday().equals("")) {
            String[] dateArr = currentUser.getBirthday().split("/");
            datePicker.updateDate(Integer.valueOf(dateArr[2]), Integer.valueOf(dateArr[1]) - 1, Integer.valueOf(dateArr[0]));
        }

        layoutEdit = viewDialog.findViewById(R.id.layoutEdit);
        pgbDialog = viewDialog.findViewById(R.id.progressBar);

        dialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .setPositiveButton(R.string.button_save, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                            Tools.getAge(datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
                        } catch (IllegalArgumentException e) {
                            Toast.makeText(ProfileActivity.this, R.string.err_ageExceed, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        presenter.EditBirthday(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                    }
                });
            }
        });
        dialog.show();
    }

    public void EditGender(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle(R.string.hint_gender)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setItems(R.array.gender, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.EditGender(i);
                    }
                });
        builder.show();
    }

    public void ShowGamesPurchased(View view) {
        Intent intent = new Intent(ProfileActivity.this, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", currentUser.getId());
        bundle.putString("cateProduct", Category.getInstance().get(0).getCateId());
        bundle.putString("cateTitle", Category.getInstance().get(0).getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void ShowImagesPurchased(View view) {
        Intent intent = new Intent(ProfileActivity.this, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", currentUser.getId());
        bundle.putString("cateProduct", Category.getInstance().get(1).getCateId());
        bundle.putString("cateTitle", Category.getInstance().get(1).getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void ShowVideosPurchased(View view) {
        Intent intent = new Intent(ProfileActivity.this, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", currentUser.getId());
        bundle.putString("cateProduct", Category.getInstance().get(2).getCateId());
        bundle.putString("cateTitle", Category.getInstance().get(2).getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void ShowMusicsPurchased(View view) {
        Intent intent = new Intent(ProfileActivity.this, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", currentUser.getId());
        bundle.putString("cateProduct", Category.getInstance().get(3).getCateId());
        bundle.putString("cateTitle", Category.getInstance().get(3).getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void Null(View view) {

    }

    private void setControls() {
        toolbar = findViewById(R.id.toolbar);
        img = findViewById(R.id.imageViewUser);
        imgGender = findViewById(R.id.imageViewGender);
        txtName = findViewById(R.id.textviewName);
        txtAge = findViewById(R.id.textViewAge);
        txtRole = findViewById(R.id.textViewRole);
        txtBalance = findViewById(R.id.textviewBalance);
        txtUsername = findViewById(R.id.textViewUsername);
        txtEmail = findViewById(R.id.textViewEmail);
        txtBirthday = findViewById(R.id.textViewBirthday);
        txtGender = findViewById(R.id.textViewGender);
        txtGame = findViewById(R.id.textViewGame);
        txtImage = findViewById(R.id.textViewImage);
        txtVideo = findViewById(R.id.textViewVideo);
        txtMusic = findViewById(R.id.textViewMusic);
        imgEmail = findViewById(R.id.imageViewEmail);
        imgPass = findViewById(R.id.imageViewPassword);
        imgBalance = findViewById(R.id.imageViewBalance);
    }

    @Override
    public void onBackPressed() {
        if (editable) {
            Intent intent = new Intent();
            intent.putExtra("result", 1);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}