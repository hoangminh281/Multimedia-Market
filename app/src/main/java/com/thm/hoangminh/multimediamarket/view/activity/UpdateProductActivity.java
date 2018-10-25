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

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.presenter.UpdateProductPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.UpdateProductPresenterImpl;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.UpdateProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity implements UpdateProductView {
    private Toolbar toolbar;
    private ImageView[] imgArr;
    private LinearLayout videoLayout;
    private ProgressDialog progressDialog;
    private TextView txtCategory, txtAgeLimit, txtFile;
    private EditText edtTitle, edtPrice, edtIntro, edtDesc, edtVideo;

    private String cateId;
    private File pickedFile;
    private int imgPosition;
    private UpdateProductPresenter presenter;
    private Map<String, String> sectionCategories;
    private ArrayList<Integer> selectedProductSections;

    private final int REQUESTCODE_TAKEPHOTO = 1;
    private final int REQUESTCODE_PICKPHOTO = 2;
    private final int REQUESTCODE_PICKFILE = 3;

    private final String YOUTUBE_LINK = "https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_modify_product);
        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        Bundle bundle = getIntent().getExtras();
        initPresenter();
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
        boolean validate = Validate.validateEditTextsToString(this, edtTitle, edtPrice, edtIntro, edtDesc, edtVideo)
                & Validate.validateTextViewsToString(this, txtAgeLimit);
        if (!validate) return;

        String title = edtTitle.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        Product product = new Product();
        product.setTitle(title);
        product.setPrice(Double.valueOf(price));

        String intro = edtIntro.getText().toString().trim();
        String description = edtDesc.getText().toString().trim();
        String ageLimit = txtAgeLimit.getText().toString().trim();
        String video = edtVideo.getText().toString().trim();
        ProductDetail productDetail = new ProductDetail();
        productDetail.setIntro(intro);
        productDetail.setDescription(description);
        productDetail.setAgeLimit(Integer.parseInt(ageLimit.replace("+", "")));
        productDetail.setVideoId(video);

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

        presenter.updateProduct(selectedProductSections, product, productDetail, productImage, productDetailBitmaps, pickedFile);
    }

    @Override
    public void updateProductUI(Product product) {
        edtTitle.setText(product.getTitle());
        edtPrice.setText(product.getPrice() + "");
    }

    @Override
    public void loadProductImageByUri(Uri uri) {
        ImageLoader.loadImageByUri(this, imgArr[0], uri);
    }

    @Override
    public void updateProductDetailUI(ProductDetail productDetail, String cateId) {
        this.cateId = cateId;
        presenter.loadProductDetailImages(this, imgArr);
        edtIntro.setText(productDetail.getIntro());
        edtDesc.setText(productDetail.getDescription());
        edtVideo.setText(productDetail.getVideoId());
        txtAgeLimit.setText(productDetail.getAgeLimit() + "+");
        txtAgeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAgePickerDialog();
            }
        });
        txtFile.setText(productDetail.getFileId());
        txtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilePicker();
            }
        });
    }

    @Override
    public void setupProgressDialog(int pgdMax, String title, String pgdMessage) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(pgdMax);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(pgdMessage);
        progressDialog.setProgress(0);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void setTagImageView(int i, int statusTag) {
        imgArr[i].setTag(new int[]{i, statusTag});
    }

    @Override
    public synchronized void updateProgressDialog(int message) {
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
                                    showMessage(R.string.info_successUpdateProduct);
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
    public void showProductCateText(String productCate) {
        if (txtCategory != null) {
            txtCategory.setText(productCate);
        }
    }

    @Override
    public void setEventSectionCategories(Map<String, String> sectionCategories) {
        this.sectionCategories = sectionCategories;
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.img_dialog_menu, menu);
        if (((int[]) imgArr[imgPosition].getTag())[1] == Constants.NotHasImageTag)
            menu.findItem(R.id.menuRemove).setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTakepic:
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhoto, REQUESTCODE_TAKEPHOTO);
                return true;
            case R.id.menuChoosepic:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUESTCODE_PICKPHOTO);
                return true;
            case R.id.menuRemove:
                imgArr[imgPosition].setImageResource(R.mipmap.add_new_image);
                imgArr[imgPosition].setTag(new int[]{imgPosition, Constants.NotHasImageTag});
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_TAKEPHOTO:
                if (resultCode == this.RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imgArr[imgPosition].setImageBitmap(bitmap);
                    imgArr[imgPosition].setTag(new int[]{imgPosition, Constants.HasImageTag});
                }
                break;
            case REQUESTCODE_PICKPHOTO:
                if (resultCode == this.RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    imgArr[imgPosition].setImageURI(selectedImage);
                    imgArr[imgPosition].setTag(new int[]{imgPosition, Constants.HasImageTag});
                }
                break;
            case REQUESTCODE_PICKFILE:
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
        selectedProductSections.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.hint_category)
                .setMultiChoiceItems(sectionCategories.values().toArray(new String[sectionCategories.size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    selectedProductSections.add(which);
                                } else if (selectedProductSections.contains(which)) {
                                    selectedProductSections.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (selectedProductSections.size() != 0) {
                            String[] stArr = sectionCategories.values().toArray(new String[sectionCategories.size()]);
                            String st = stArr[selectedProductSections.get(0)];
                            for (int i = 1; i < selectedProductSections.size(); i++) {
                                st += ", " + stArr[selectedProductSections.get(i)];
                            }
                            txtCategory.setText(st);
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
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

        final AlertDialog dialog = new AlertDialog.Builder(this)
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
                        txtAgeLimit.setText(numberPicker.getValue() + "+");
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public void showFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (cateId.equals(Category.getInstance().get(1).getCateId())) {
            intent.setType("application/vnd.android.package-archive");
        } else if (cateId.equals(Category.getInstance().get(2).getCateId())) {
            intent.setType("image/*");
        } else if (cateId.equals(Category.getInstance().get(3).getCateId())) {
            intent.setType("video/*");
        } else if (cateId.equals(Category.getInstance().get(4).getCateId())) {
            intent.setType("audio/*");
        }
        startActivityForResult(intent, REQUESTCODE_PICKFILE);
    }

    private void setEvents() {
        int pos = 0;
        for (final ImageView img : imgArr) {
            registerForContextMenu(img);
            img.setLongClickable(false);
            img.setTag(new int[]{pos++, Constants.NotHasImageTag});
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
        edtDesc = findViewById(R.id.editTextDescription);
        edtVideo = findViewById(R.id.editTextVideo);
        txtCategory = findViewById(R.id.textViewSection);
        txtAgeLimit = findViewById(R.id.textViewAgeLimit);
        txtFile = findViewById(R.id.textViewFile);
        toolbar = findViewById(R.id.toolbar);
        selectedProductSections = new ArrayList();
        videoLayout = findViewById(R.id.videoLayout);
    }
}