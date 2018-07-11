package com.thm.hoangminh.multimediamarket.views.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.presenters.ModifyProductPresenters.ModifyProductPresenter;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainActivity;
import com.thm.hoangminh.multimediamarket.views.ModifyProductViews.ModifyProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModifyProductFragment extends Fragment implements ModifyProductView {
    private ImageView[] imgArr;
    private EditText edtTitle, edtPrice, edtIntro, edtDes, edtVideo;
    private ArrayList<Integer> mSelectedItems;
    private ModifyProductPresenter presenter;
    private Map<String, String> categoryList;
    private TextView txtCategory, txtAgeLimit, txtFile;
    private Toolbar toolbar;
    private LinearLayout videoLayout;
    private String key_category;
    private com.thm.hoangminh.multimediamarket.models.File pickedFile;
    private ProgressDialog progressDialog;

    private int imgPosition;

    private final int REQUEST_CODE_TAKEPHOTO = 1;
    private final int REQUEST_CODE_PICKPHOTO = 2;
    private final int REQUEST_CODE_PICKFILE = 3;

    private final int TAG_CLICKEDIMAGE = 0;
    private final int TAG_UNCLICKIMAGE = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_product, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControls(view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.menu_upload) + "");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Upload");

        initPresenter();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            key_category = bundle.getString("keyCategory");
            if (key_category.trim().length() != 0) {
                presenter.LoadCategoryProduct(key_category);
            }

            setEvents();
        }
    }

    private void initPresenter() {
        presenter = new ModifyProductPresenter(this);
    }

    @Override
    public void ShowCategory(Map<String, String> categoryList) {
        this.categoryList = categoryList;
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog();
            }
        });
    }

    public void showCategoryDialog() {
        mSelectedItems.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
                        if (mSelectedItems.size()!=0) {
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

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
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
        if (key_category.equals(MainActivity.categories.get(0).getCate_id())) {
            intent.setType("application/vnd.android.package-archive");
        }
        else if (key_category.equals(MainActivity.categories.get(1).getCate_id())) {
            intent.setType("image/*");
        }
        else if (key_category.equals(MainActivity.categories.get(2).getCate_id())) {
            intent.setType("video/*");
        }
        else if (key_category.equals(MainActivity.categories.get(3).getCate_id())) {
            intent.setType("audio/*");
        }
        startActivityForResult(intent, REQUEST_CODE_PICKFILE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.img_dialog_menu, menu);
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
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imgArr[imgPosition].setImageBitmap(bitmap);
                    imgArr[imgPosition].setTag(new int[]{imgPosition, TAG_CLICKEDIMAGE});
                }
                break;
            case REQUEST_CODE_PICKPHOTO:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    imgArr[imgPosition].setImageURI(selectedImage);
                    imgArr[imgPosition].setTag(new int[]{imgPosition, TAG_CLICKEDIMAGE});
                }
                break;
            case REQUEST_CODE_PICKFILE:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Uri selectedFile = data.getData();
                    if (selectedFile != null) {
                        pickedFile = new File(getActivity(), selectedFile);
                        File.Size size = pickedFile.getAbsoluteSize();
                        txtFile.setText(pickedFile.getName() + " [" + size.getValue() + size.getUnit() + "]");
                    }
                    break;
                }
        }
    }

    public void SaveContent() {
        int imgStatus = ((int[]) imgArr[0].getTag())[1];
        if (imgStatus == TAG_UNCLICKIMAGE) {
            AnimationSupport.shake(getContext(), imgArr[0]);
            return;
        }
        String title = edtTitle.getText().toString().trim();
        if (edtTitle.getVisibility() == View.VISIBLE && title.length() == 0) {
            edtTitle.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        String price = edtPrice.getText().toString().trim();
        if (edtPrice.getVisibility() == View.VISIBLE && price.length() == 0) {
            edtPrice.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        int cate = mSelectedItems.size();
        if (txtCategory.getVisibility() == View.VISIBLE && cate == 0) {
            txtCategory.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        String ageLimit = txtAgeLimit.getText().toString().trim();
        if (txtAgeLimit.getVisibility() == View.VISIBLE && ageLimit.length() == 0) {
            txtAgeLimit.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        String intro = edtIntro.getText().toString().trim();
        if (edtIntro.getVisibility() == View.VISIBLE && intro.length() == 0) {
            edtIntro.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        String desc = edtDes.getText().toString().trim();
        if (edtDes.getVisibility() == View.VISIBLE && desc.length() == 0) {
            edtDes.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        String video = edtVideo.getText().toString().trim();
        if (videoLayout.getVisibility() == View.VISIBLE && video.length() == 0) {
            edtVideo.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        String filePath = txtFile.getText().toString().trim();
        if (txtFile.getVisibility() == View.VISIBLE && filePath.length() == 0) {
            txtFile.setError(getContext().getResources().getString(R.string.err_empty));
            return;
        }
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (ImageView img : imgArr) {
            if (((int[]) img.getTag())[1] == TAG_CLICKEDIMAGE) {
                bitmaps.add(((BitmapDrawable) img.getDrawable()).getBitmap());
            }
        }

        progressDialog.setProgress(0);
        progressDialog.setMessage("Creating product...");
        progressDialog.setMax(3 + bitmaps.size() + mSelectedItems.size());
        progressDialog.show();
        Map<String, String> sections = new HashMap<>();
        for (int i : mSelectedItems) {
            sections.put((String) categoryList.keySet().toArray()[i], (String) categoryList.values().toArray()[i]);
        }
        presenter.CreateNewProduct(title, key_category, bitmaps, Double.valueOf(price),
                intro, desc, Integer.valueOf(ageLimit.replace("+", "")), video, pickedFile, sections);
    }

    @Override
    public void hideEdtYoutube() {
        videoLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public synchronized void updateProgress(String s) {
        progressDialog.setMessage(s);
        int i = progressDialog.getProgress() + 1;
        progressDialog.setProgress(i);
        if (i == progressDialog.getMax()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            Thread.sleep(400);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Congratulation! Successfully progress", Toast.LENGTH_SHORT).show();
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
        txtAgeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAgePickerDialog();
            }
        });
        txtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilePicker();
            }
        });
    }

    private void setControls(View view) {
        imgArr = new ImageView[5];
        imgArr[0] = view.findViewById(R.id.imageView1);
        imgArr[1] = view.findViewById(R.id.imageView2);
        imgArr[2] = view.findViewById(R.id.imageView3);
        imgArr[3] = view.findViewById(R.id.imageView4);
        imgArr[4] = view.findViewById(R.id.imageView5);
        edtTitle = view.findViewById(R.id.editTextTitle);
        edtPrice = view.findViewById(R.id.editTextPrice);
        edtIntro = view.findViewById(R.id.editTextIntro);
        edtDes = view.findViewById(R.id.editTextDescription);
        edtVideo = view.findViewById(R.id.editTextVideo);
        txtCategory = view.findViewById(R.id.textViewCategory);
        txtAgeLimit = view.findViewById(R.id.textViewAgeLimit);
        txtFile = view.findViewById(R.id.textViewFile);
        toolbar = view.findViewById(R.id.toolbar);
        mSelectedItems = new ArrayList();          // Where we track the selected items
        videoLayout = view.findViewById(R.id.videoLayout);
        progressDialog = new ProgressDialog(getContext());
    }

}
