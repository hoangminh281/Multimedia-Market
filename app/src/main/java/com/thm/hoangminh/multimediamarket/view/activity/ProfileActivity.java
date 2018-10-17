package com.thm.hoangminh.multimediamarket.view.ProfileViews;

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
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenter.ProfilePresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.ProfilePresenterImpl;
import com.thm.hoangminh.multimediamarket.utility.Tools;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.ProfileView;

public class ProfileActivity extends AppCompatActivity implements ProfileView {
    private Toolbar toolbar;
    private AlertDialog dialog;
    private ImageView imgBalance;
    private ProgressBar pgbDialog;
    private LinearLayout layoutEdit;
    private ImageView img, imgGender, imgEmail, imgPass;
    private TextView txtName, txtAge, txtRole, txtBalance, txtGame, txtImage,
            txtVideo, txtMusic, txtUsername, txtEmail, txtBirthday, txtGender;

    private boolean editable;
    private ProfilePresenter presenter;
    private final int REQUEST_CODE_TAKEPHOTO = 1;
    private final int REQUEST_CODE_PICKPHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setControls();

        Bundle bundle = getIntent().getExtras();
        initPresenter();
        presenter.extractBundle(bundle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        registerForContextMenu(img);
        img.setLongClickable(false);
    }

    private void initPresenter() {
        presenter = new ProfilePresenterImpl(this);
    }

    @Override
    public void showEditableBalance(boolean b) {
        if (b) imgBalance.setVisibility(View.VISIBLE); // allow admin edit balance user
        else imgBalance.setVisibility(View.GONE);
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
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhotoIntent, REQUEST_CODE_TAKEPHOTO);
                return true;
            case R.id.menuChoosepic:
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, REQUEST_CODE_PICKPHOTO);
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
                    presenter.updateImageCurrentUser(bitmap);
                }
                break;
            case REQUEST_CODE_PICKPHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    img.setImageURI(selectedImage);
                    presenter.updateImageCurrentUser(((BitmapDrawable) img.getDrawable()).getBitmap());
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
        user.LoadUserImageView(img, this);
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
    public void showUserRole(String role) {
        txtRole.setText(role);
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
    public void showMessage(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
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
        dialog = buildInputDialog(R.string.menu_balance, R.string.hint_balance,
                String.valueOf(presenter.getUserBalance()), InputType.TYPE_CLASS_NUMBER);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText edtBalance = dialog.findViewById(R.id.editText);
                        boolean validate = Validate.validateEditTextsToNumber(ProfileActivity.this, edtBalance);
                        if (validate) {
                            double balance = Double.parseDouble(edtBalance.getText().toString());
                            presenter.updateUserBalance(balance);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public void EditUsername(final View view) {
        dialog = buildInputDialog(R.string.hint_username, R.string.hint_enter_username, presenter.getUserName(), InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText edtUserName = dialog.findViewById(R.id.editText);
                        boolean validate = Validate.validateEditTextsToString(ProfileActivity.this, edtUserName);
                        if (validate) {
                            String userName = edtUserName.getText().toString();
                            presenter.updateUsername(userName);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void EditEmail() {
        dialog = buildInputDialog(R.string.hint_email, R.string.hint_enter_email,
                presenter.getUserEmail(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText edtEmail = dialog.findViewById(R.id.editText);
                        boolean validate = Validate.validateEditTextsToString(ProfileActivity.this, edtEmail);
                        if (validate) {
                            String email = edtEmail.getText().toString();
                            presenter.updateUserEmail(email);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void EditPassword() {
        dialog = buildInputDialog(R.string.txt_password, R.string.hint_password, "", 0);
        final EditText edtPassword = dialog.findViewById(R.id.editText);
        CheckBox cbEye = dialog.findViewById(R.id.checkBoxEye);
        cbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        final EditText edtRepassword = dialog.findViewById(R.id.editTextRe);
        edtRepassword.setHint(R.string.hint_repassword);

        CheckBox cbEyeRe = dialog.findViewById(R.id.checkBoxEyeRe);
        cbEyeRe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edtRepassword.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    edtRepassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean passwordValidate = Validate.validatePassword(ProfileActivity.this, edtPassword, edtRepassword);
                        if (!passwordValidate) return;
                        boolean samePasswordValidate = Validate.validateSamePassword(ProfileActivity.this, edtPassword, edtRepassword);
                        if (!samePasswordValidate) return;
                        String password = edtPassword.getText().toString().trim();
                        presenter.updatePassword(password);
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
        String birthday = presenter.getUserBirthday();
        if (birthday != null && !birthday.equals("")) {
            String[] dateArr = birthday.split("/");
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
                        boolean validate = Validate.validateAge(ProfileActivity.this, datePicker.getDayOfMonth()
                                , datePicker.getMonth() + 1, datePicker.getYear());
                        if (validate) {
                            presenter.updateBirthday(datePicker.getDayOfMonth(), datePicker.getMonth() + 1
                                    , datePicker.getYear());
                        }
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
                        presenter.updateGender(i);
                    }
                });
        builder.show();
    }

    private AlertDialog buildInputDialog(int title, int hint, String data, int inputType) {
        View viewDialog = getLayoutInflater().inflate(R.layout.edit_dialog, null);

        TextView txtTitle = viewDialog.findViewById(R.id.textViewTitle);
        txtTitle.setText(title);

        final EditText edt = viewDialog.findViewById(R.id.editText);
        edt.setHint(hint);
        edt.setText(data);
        edt.setInputType(inputType);

        layoutEdit = viewDialog.findViewById(R.id.layoutEdit);
        pgbDialog = viewDialog.findViewById(R.id.progressBar);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .setPositiveButton(R.string.button_save, null)
                .create();
        return dialog;
    }

    public void ShowGamesPurchased(View view) {
        presenter.redirectToProductActivity(1);
    }

    public void ShowImagesPurchased(View view) {
        presenter.redirectToProductActivity(2);

    }

    public void ShowVideosPurchased(View view) {
        presenter.redirectToProductActivity(3);

    }

    public void ShowMusicsPurchased(View view) {
        presenter.redirectToProductActivity(4);

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
            intent.putExtra(Constants.Result, Constants.UserRequestCode);
            setResult(RESULT_OK, intent);
        }
        finish();
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