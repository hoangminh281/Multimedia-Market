package com.thm.hoangminh.multimediamarket.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenter.UpdateProductPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.UpdateProductPresenterImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.UpdateProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity implements UpdateProductView {
    private Toolbar toolbar;
    private ImageView[] imgArr;
    private LinearLayout videoLayout;
    private ProgressDialog progressDialog;
    private TextView txtCategory, txtAgeLimit, txtFile;
    private EditText edtTitle, edtPrice, edtIntro, edtDes, edtVideo;

    private final int REQUEST_CODE_TAKEPHOTO = 1;
    private final int REQUEST_CODE_PICKPHOTO = 2;
    private final int REQUEST_CODE_PICKFILE = 3;

    private final int TAG_CLICKEDIMAGE = 0;
    private final int TAG_UNCLICKIMAGE = 1;

    private final String YOUTUBE_LINK = "https://www.youtube.com/watch?v=";

    private Product product;
    private File pickedFile;
    private int imgPosition;
    private ProductDetail pDetail;
    private ArrayList<Integer> selectedProductSections;
    private UpdateProductPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_modify_product);
        setControls();
        initPresenter();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.menu_upload);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            presenter.extractBundle(bundle);

            setEvents();
        }
    }

    private void initPresenter() {
        presenter = new UpdateProductPresenterImpl(this);
    }

    @Override
    public void hideVideoEditText() {
        videoLayout.setVisibility(View.GONE);
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
            getFragmentManager().popBackStack();
            return true;
        case R.id.menu_save:
            saveAction();
            return true;
        }
        return false;
    }

    public void saveAction() {
        if (User.getInstance().getId() != pDetail.getOwnerId() && User.getInstance().getRole() != User.ADMIN) {
            Toast.makeText(this, R.string.info_fail_role, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean edtValidate = Validate.validateEditTexts(this, edtTitle, edtPrice, edtIntro, edtDes, edtVideo);
        boolean txtValidate = Validate.validateTextViews(this, txtAgeLimit);

        if (edtValidate && txtValidate) {
            if (edtTitle.getVisibility() == View.VISIBLE && title.length() == 0) {
                edtTitle.setError(this.getResources().getString(R.string.err_empty));
                return;
            }

            if (edtPrice.getVisibility() == View.VISIBLE && price.length() == 0) {
                edtPrice.setError(this.getResources().getString(R.string.err_empty));
                return;
            }

            if (edtIntro.getVisibility() == View.VISIBLE && intro.length() == 0) {
                edtIntro.setError(this.getResources().getString(R.string.err_empty));
                return;
            }

            if (edtDes.getVisibility() == View.VISIBLE && desc.length() == 0) {
                edtDes.setError(this.getResources().getString(R.string.err_empty));
                return;
            }

            boolean edtValidate = Validate.validateEditTextsToString(this, edtTitle, edtPrice, edtIntro, edtDesc,
                    edtVideo);
            boolean txtValidate = Validate.validateTextViewsToString(this, txtAgeLimit, txtFile, txtCategory);
            if (!edtValidate || !txtValidate) {
                return;
            }

            Bitmap productImage = null;
            if (((int[]) imgArr[0].getTag())[1] == Constants.HasImageTag) {
                productImage = ((BitmapDrawable) imgArr[0].getDrawable()).getBitmap();
            }

            Map<Integer, Bitmap> productDetailBitmaps = new HashMap();
            for (int i = 1; i < imgArr.length; i++) {
                if (((int[]) imgArr[i].getTag())[1] == Constants.HasImageTag) {
                    productDetailBitmaps.put(i - 1, ((BitmapDrawable) imgArr[i].getDrawable()).getBitmap());
                }
            }

            progressDialog = new ProgressDialog(this);

            ArrayList<String> newProductSections;
            if (mSelectedItems.size() == 0) {
                progressDialog.setMessage("Creating product...");
                progressDialog.setMax(2 + (productImage != null ? 1 : 0) + productDetailBitmaps.size()
                        + (pickedFile != null ? 1 : 0));
            } else {
                progressDialog.setMessage("Updating sections...");
                progressDialog.setMax(4 + (productImage != null ? 1 : 0) + productDetailBitmaps.size()
                        + (pickedFile != null ? 1 : 0));
                newProductSections = new ArrayList<>();
                for (int i : mSelectedItems) {
                    newProductSections.add((String) sectionCategories.keySet().toArray()[i]);
                }

            }

            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("Update");
            progressDialog.setProgress(0);
            progressDialog.show();

            product.setTitle(title);
            product.setPrice(Double.valueOf(price));

            if (pDetail.getImageList() == null)
                pDetail.setImageList(new HashMap<String, String>());
            pDetail.setIntro(intro);
            pDetail.setDescription(desc);
            pDetail.setAgeLimit(Integer.parseInt(ageLimit.replace("+", "")));
            pDetail.setVideo(video);

            presenter.UpdateProduct(productSections, newProductSections, product, pDetail, productImage,
                    productDetailBitmaps, pickedFile);
        }
    }

    @Override
    public void UpdateProductUI(Product product) {
        this.product = product;
        edtTitle.setText(product.getTitle());
        edtPrice.setText(product.getPrice() + "");
        Validate.validateImageProduct(imgArr[0], product.getStatus());
    }

    @Override
    public void loadProductImageByUri(Uri uri) {
        Picasso.with(this).load(uri).error(R.mipmap.icon_app_2).into(imgArr[0]);
    }

    @Override
    public void setTagImageView(int i, int hasOrNotHasImageTag) {
        imgArr[i].setTag(new int[] { i, hasOrNotHasImageTag });
    }

    @Override
    public void UpdateProductDetailUI(ProductDetail pDetail, final String cateId) {
        this.pDetail = pDetail;
        edtIntro.setText(pDetail.getIntro());
        edtDes.setText(pDetail.getDescription());
        edtVideo.setText(pDetail.getVideo());
        presenter.loadProductDetailImages(this, imgArr);
        txtAgeLimit.setText(pDetail.getAgeLimit() + "+");
        txtAgeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAgePickerDialog();
            }
        });
        txtFile.setText(pDetail.getDownloadLink());
        txtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilePicker(cateId);
            }
        });
    }

    @Override
    public void showProductCateText(String productCate) {
        txtCategory.setText(productCate);
    }

    @Override
    public void setEventSectionCategories() {
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog();
            }
        });
    }

    @Override
    public synchronized void UpdateProgressDialog(int message) {
        progressDialog.setMessage(getResources().getString(message));
        int i = progressDialog.getProgress() + 1;
        progressDialog.setProgress(i);
        if (i == progressDialog.getMax()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            Thread.sleep(400);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProductActivity.this, R.string.info_successUpdateProduct,
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void showMessage(int e) {
        progressDialog.dismiss();
        Toast.makeText(this, getResources().getString(e), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.img_dialog_menu, menu);
        if (imgArr[imgPosition] != null)
            if (((int[]) imgArr[imgPosition].getTag())[1] == Constants.HasImageTag)
                menu.findItem(R.id.menuRemove).setVisible(true);
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
        case R.id.menuRemove:
            imgArr[imgPosition].setImageResource(R.mipmap.add_new_image);
            setTagImageView(imgPosition, Constants.NotHasImageTag);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQUEST_CODE_TAKEPHOTO:
            if (resultCode == this.RESULT_OK && data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgArr[imgPosition].setImageBitmap(bitmap);
                setTagImageView(imgPosition, Constants.HasImageTag);
            }
            break;
        case REQUEST_CODE_PICKPHOTO:
            if (resultCode == this.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                imgArr[imgPosition].setImageURI(selectedImage);
                setTagImageView(imgPosition, Constants.HasImageTag);
            }
            break;
        case REQUEST_CODE_PICKFILE:
            if (resultCode == this.RESULT_OK && data != null) {
                Uri selectedFile = data.getData();
                if (selectedFile != null) {
                    pickedFile = new File(this, selectedFile);
                    File.Size size = pickedFile.getAbsoluteSize();
                    txtFile.setText(pickedFile.getName() + " [" + size.getValue() + size.getUnit() + "]");
                }
                break;
            }
        }
    }

    public void showCategoryDialog() {
        mSelectedItems.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title
        builder.setTitle(R.string.hint_category)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(sectionCategories.values().toArray(new String[sectionCategories.size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mSelectedItems.size() != 0) {
                            String[] stArr = sectionCategories.values().toArray(new String[sectionCategories.size()]);
                            String st = stArr[mSelectedItems.get(0)];
                            for (int i = 1; i < mSelectedItems.size(); i++) {
                                st += ", " + stArr[mSelectedItems.get(i)];
                            }
                            txtCategory.setText(st);
                        }
                    }
                }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }

    public void showAgePickerDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.number_edit_dialog, null);

        TextView txtTitle = viewDialog.findViewById(R.id.textViewTitle);
        txtTitle.setText(R.string.txt_ageLimit);

        final NumberPicker numberPicker = viewDialog.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        if (txtAgeLimit.getText().toString() != "")
            numberPicker.setValue(Integer.valueOf(txtAgeLimit.getText().toString().replace("+", "")));
        numberPicker.setWrapSelectorWheel(false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(viewDialog)
                .setPositiveButton(R.string.button_save, null).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        txtAgeLimit.setText(numberPicker.getValue() + "+");
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public void showFilePicker(String cateId) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (cateId.equals(MainActivity.categories.get(0).getCateId())) {
            intent.setType("application/vnd.android.package-archive");
        } else if (cateId.equals(MainActivity.categories.get(1).getCateId())) {
            intent.setType("image/*");
        } else if (cateId.equals(MainActivity.categories.get(2).getCateId())) {
            intent.setType("video/*");
        } else if (cateId.equals(MainActivity.categories.get(3).getCateId())) {
            intent.setType("audio/*");
        }
        startActivityForResult(intent, REQUEST_CODE_PICKFILE);
    }

    private void setEvents() {
        int pos = 0;
        for (final ImageView img : imgArr) {
            registerForContextMenu(img);
            img.setLongClickable(false);
            setTagImageView(pos++, Constants.NotHasImageTag);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgPosition = ((int[]) img.getTag())[0];
                    img.showContextMenu();
                }
            });
        }
    }

    private void setControls() {
        imgArr = new ImageView[5];
        imgArr[0] = findViewById(R.id.imageView1);
        imgArr[1] = findViewById(R.id.imageView2);
        imgArr[2] = findViewById(R.id.imageView3);
        imgArr[3] = findViewById(R.id.imageView4);
        imgArr[4] = findViewById(R.id.imageView5);
        edtTitle = findViewById(R.id.editTextTitle);
        edtPrice = findViewById(R.id.editTextPrice);
        edtIntro = findViewById(R.id.editTextIntro);
        edtDes = findViewById(R.id.editTextDescription);
        edtVideo = findViewById(R.id.editTextVideo);
        txtCategory = findViewById(R.id.textViewCategory);
        txtAgeLimit = findViewById(R.id.textViewAgeLimit);
        txtFile = findViewById(R.id.textViewFile);
        toolbar = findViewById(R.id.toolbar);
        mSelectedItems = new ArrayList(); // Where we track the selected items
        videoLayout = findViewById(R.id.videoLayout);
    }
}
