package com.thm.hoangminh.multimediamarket.views.UpdateProductViews;

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
import android.view.MenuInflater;
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
import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenters.ModifyProductPresenters.ModifyProductPresenter;
import com.thm.hoangminh.multimediamarket.presenters.UpdateProductPresenters.UpdateProductPresenter;
import com.thm.hoangminh.multimediamarket.presenters.UpdateProductPresenters.UpdateProductPresenterImpl;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity implements UpdateProductView {
    private ImageView[] imgArr;
    private EditText edtTitle, edtPrice, edtIntro, edtDes, edtVideo;
    private ArrayList<Integer> mSelectedItems;
    private Map<String, String> categoryList;
    private TextView txtCategory, txtAgeLimit, txtFile;
    private Toolbar toolbar;
    private LinearLayout videoLayout;
    private String cate_id;
    private com.thm.hoangminh.multimediamarket.models.File pickedFile;
    private ProgressDialog progressDialog;

    private int imgPosition;

    private final int REQUEST_CODE_TAKEPHOTO = 1;
    private final int REQUEST_CODE_PICKPHOTO = 2;
    private final int REQUEST_CODE_PICKFILE = 3;

    private final int TAG_CLICKEDIMAGE = 0;
    private final int TAG_UNCLICKIMAGE = 1;

    private final String YOUTUBE_LINK = "https://www.youtube.com/watch?v=";

    private String product_id;
    private ArrayList<String> productSections;
    private UpdateProductPresenterImpl presenter;
    private HashMap<String, String> imageList;
    private Product product;
    private ProductDetail pDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_modify_product);
        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);/*
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_upload) + "");*/
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product_id = bundle.getString("product_id");
            cate_id = bundle.getString("cate_id");

            if (cate_id.equals(MainActivity.categories.get(1).getCate_id()) || cate_id.equals(MainActivity.categories.get(3).getCate_id())) {
                hideEdtYoutube();
            }

            presenter = new UpdateProductPresenterImpl(this);
            presenter.LoadProductById(cate_id, product_id);
            presenter.LoadSectionById(cate_id);

            setEvents();
        }
    }

    private void hideEdtYoutube() {
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
                SaveContent();
                return true;
        }
        return false;
    }

    public void SaveContent() {
        if (User.getInstance().getId() != pDetail.getOwner_id() && User.getInstance().getRole()!= User.ADMIN) {
            Toast.makeText(this, R.string.info_fail_role, Toast.LENGTH_SHORT).show();
            return;
        }
        String title = edtTitle.getText().toString().trim();
        if (edtTitle.getVisibility() == View.VISIBLE && title.length() == 0) {
            edtTitle.setError(this.getResources().getString(R.string.err_empty));
            return;
        }

        String price = edtPrice.getText().toString().trim();
        if (edtPrice.getVisibility() == View.VISIBLE && price.length() == 0) {
            edtPrice.setError(this.getResources().getString(R.string.err_empty));
            return;
        }

        String ageLimit = txtAgeLimit.getText().toString().trim();
        String intro = edtIntro.getText().toString().trim();
        if (edtIntro.getVisibility() == View.VISIBLE && intro.length() == 0) {
            edtIntro.setError(this.getResources().getString(R.string.err_empty));
            return;
        }

        String desc = edtDes.getText().toString().trim();
        if (edtDes.getVisibility() == View.VISIBLE && desc.length() == 0) {
            edtDes.setError(this.getResources().getString(R.string.err_empty));
            return;
        }

        String video = edtVideo.getText().toString().trim();
        if (videoLayout.getVisibility() == View.VISIBLE && video.length() == 0) {
            edtVideo.setError(this.getResources().getString(R.string.err_empty));
            return;
        }

        Bitmap productImage = null;
        if (((int[]) imgArr[0].getTag())[1] == TAG_CLICKEDIMAGE) {
            productImage = ((BitmapDrawable) imgArr[0].getDrawable()).getBitmap();
        }

        Map<Integer, Bitmap> productDetailBitmaps = new HashMap();
        for (int i = 1; i < imgArr.length; i++) {
            if (((int[]) imgArr[i].getTag())[1] == TAG_CLICKEDIMAGE) {
                productDetailBitmaps.put(i - 1, ((BitmapDrawable) imgArr[i].getDrawable()).getBitmap());
            }
        }

        progressDialog = new ProgressDialog(this);

        ArrayList<String> newSections = null;
        if (mSelectedItems.size() == 0) {
            progressDialog.setMessage("Creating product...");
            progressDialog.setMax(2 + (productImage != null ? 1 : 0) + productDetailBitmaps.size() + (pickedFile != null ? 1 : 0));
        } else {
            progressDialog.setMessage("Updating sections...");
            progressDialog.setMax(4 + (productImage != null ? 1 : 0) + productDetailBitmaps.size() + (pickedFile != null ? 1 : 0));
            newSections = new ArrayList<>();
            for (int i : mSelectedItems) {
                newSections.add((String) categoryList.keySet().toArray()[i]);
            }

        }

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Update");
        progressDialog.setProgress(0);
        progressDialog.show();

        product.setTitle(title);
        product.setPrice(Double.valueOf(price));

        if (pDetail.getImageList() == null) pDetail.setImageList(new HashMap<String, String>());
        pDetail.setIntro(intro);
        pDetail.setDescription(desc);
        pDetail.setAgeLimit(Integer.parseInt(ageLimit.replace("+", "")));
        pDetail.setVideo(video);

        presenter.UpdateProduct(productSections, newSections, product, pDetail, productImage, productDetailBitmaps, pickedFile);
    }

    @Override
    public void UpdateProductUI(Product product) {
        this.product = product;
        edtTitle.setText(product.getTitle());
        edtPrice.setText(product.getPrice() + "");
        product.setBitmapImage(imgArr[0], this);
    }

    @Override
    public void UpdateProductDetailUI(ProductDetail pDetail) {
        this.pDetail = pDetail;
        edtIntro.setText(pDetail.getIntro());
        edtDes.setText(pDetail.getDescription());
        edtVideo.setText(pDetail.getVideo());
        if (pDetail.getImageList() != null) {
            imageList = pDetail.getImageList();
            int minSize = imageList.size() < imgArr.length ? imageList.size() : imgArr.length;
            for (int i = 1; i < minSize + 1; i++) {
                pDetail.setBitmapImage(imgArr[i], String.valueOf(imageList.values().toArray()[i - 1]), this);
            }
        }
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
                showFilePicker();
            }
        });
    }

    @Override
    public void UpdateSectionProductUI(Map<String, String> sections) {
        this.productSections = new ArrayList<>(sections.keySet());
        if (sections.size() > 0) {
            String[] stArr = sections.values().toArray(new String[sections.size()]);
            String st = stArr[0];
            for (int i = 1; i < sections.size() - 1; i++)
                st += ", " + stArr[i];
            txtCategory.setText(st);
        }
    }

    @Override
    public void UpdateSectionList(Map<String, String> sections) {
        this.categoryList = sections;
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
                                    Toast.makeText(UpdateProductActivity.this, R.string.info_successUpdateProduct, Toast.LENGTH_SHORT).show();
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
        if (((int[]) imgArr[imgPosition].getTag())[1] == TAG_UNCLICKIMAGE)
            menu.findItem(R.id.menuRemove).setVisible(false);
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
                imgArr[imgPosition].setTag(new int[]{imgPosition, TAG_UNCLICKIMAGE});
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
                    imgArr[imgPosition].setTag(new int[]{imgPosition, TAG_CLICKEDIMAGE});
                }
                break;
            case REQUEST_CODE_PICKPHOTO:
                if (resultCode == this.RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    imgArr[imgPosition].setImageURI(selectedImage);
                    imgArr[imgPosition].setTag(new int[]{imgPosition, TAG_CLICKEDIMAGE});
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
                .setMultiChoiceItems(categoryList.values().toArray(new String[categoryList.size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
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
                            String[] stArr = categoryList.values().toArray(new String[categoryList.size()]);
                            String st = stArr[mSelectedItems.get(0)];
                            for (int i = 1; i < mSelectedItems.size(); i++) {
                                st += ", " + stArr[mSelectedItems.get(i)];
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
        if (cate_id.equals(MainActivity.categories.get(0).getCate_id())) {
            intent.setType("application/vnd.android.package-archive");
        } else if (cate_id.equals(MainActivity.categories.get(1).getCate_id())) {
            intent.setType("image/*");
        } else if (cate_id.equals(MainActivity.categories.get(2).getCate_id())) {
            intent.setType("video/*");
        } else if (cate_id.equals(MainActivity.categories.get(3).getCate_id())) {
            intent.setType("audio/*");
        }
        startActivityForResult(intent, REQUEST_CODE_PICKFILE);
    }

    private void setEvents() {
        int pos = 0;
        for (final ImageView img : imgArr) {
            registerForContextMenu(img);
            img.setLongClickable(false);
            img.setTag(new int[]{pos++, TAG_UNCLICKIMAGE});
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
        mSelectedItems = new ArrayList();          // Where we track the selected items
        videoLayout = findViewById(R.id.videoLayout);
    }
}
