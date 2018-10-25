package com.thm.hoangminh.multimediamarket.view.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.presenter.AddProductPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.AddProductPresenterImpl;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.ModifyProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddProductFragment extends Fragment implements ModifyProductView {
    private Toolbar toolbar;
    private ImageView[] imgArr;
    private LinearLayout videoLayout;
    private ProgressDialog progressDialog;
    private TextView txtSection, txtAgeLimit, txtFile;
    private EditText edtTitle, edtPrice, edtIntro, edtDes, edtVideo;

    private String cateId;
    private File pickedFile;
    private AddProductPresenter presenter;
    private Map<String, String> sections;
    private ArrayList<Integer> mSelectedItems;

    private int imgPosition;

    private final int REQUESTCODE_TAKEPHOTO = 1;
    private final int REQUESTCODE_PICKPHOTO = 2;
    private final int REQUESTCODE_PICKFILE = 3;


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            presenter.extractBundle(bundle);
            setEvents();
        }
    }

    private void initPresenter() {
        presenter = new AddProductPresenterImpl(this);
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
                saveContent();
                return true;
        }
        return false;
    }

    @Override
    public void showSectionList(Map<String, String> sections) {
        this.sections = sections;
        txtSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSectionDialog();
            }
        });
    }

    public void showSectionDialog() {
        mSelectedItems.clear();
        txtSection.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.hint_category)
                .setMultiChoiceItems(sections.values().toArray(new String[sections.size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mSelectedItems.size() != 0) {
                            String[] stArr = sections.values().toArray(new String[sections.size()]);
                            String st = stArr[mSelectedItems.get(0)];
                            for (int i = 1; i < mSelectedItems.size(); i++) {
                                st += ", " + stArr[mSelectedItems.get(i)];
                            }
                            txtSection.setText(st);
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
        numberPicker.setMinValue(Constants.MinAgeLimit);
        numberPicker.setMaxValue(Constants.MaxAgeLimit);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.img_dialog_menu, menu);
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
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imgArr[imgPosition].setImageBitmap(bitmap);
                    imgArr[imgPosition].setTag(new int[]{imgPosition, Constants.HasImageTag});
                }
                break;
            case REQUESTCODE_PICKPHOTO:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    imgArr[imgPosition].setImageURI(selectedImage);
                    imgArr[imgPosition].setTag(new int[]{imgPosition, Constants.HasImageTag});
                }
                break;
            case REQUESTCODE_PICKFILE:
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

    public void saveContent() {
        int productImgStatus = ((int[]) imgArr[0].getTag())[1];
        if (productImgStatus == Constants.NotHasImageTag) {
            AnimationSupport.shake(getContext(), imgArr[0]);
            return;
        }
        boolean validate = Validate.validateEditTextsToString(getContext(), edtTitle, edtPrice)
                & Validate.validateTextViewsToString(getContext(), txtSection, txtAgeLimit);
        String title = edtTitle.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String ageLimit = txtAgeLimit.getText().toString().trim();
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
            if (((int[]) img.getTag())[1] == Constants.HasImageTag) {
                bitmaps.add(((BitmapDrawable) img.getDrawable()).getBitmap());
            }
        }

        progressDialog.setProgress(0);
        progressDialog.setMessage("Creating product...");
        progressDialog.setMax(3 + bitmaps.size() + mSelectedItems.size());
        progressDialog.show();
        Map<String, String> sections = new HashMap<>();
        for (int i : mSelectedItems) {
            sections.put((String) this.sections.keySet().toArray()[i], (String) this.sections.values().toArray()[i]);
        }
        presenter.addProduct(title, cateId, bitmaps, Double.valueOf(price),
                intro, desc, Integer.valueOf(ageLimit.replace("+", "")), video, pickedFile, sections);
    }

    @Override
    public void hideEdtYoutube() {
        videoLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(int messageId) {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
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
                                    getActivity().onBackPressed();
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
            img.setTag(new int[]{pos++, Constants.NotHasImageTag});
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
        txtSection = view.findViewById(R.id.textViewSection);
        txtAgeLimit = view.findViewById(R.id.textViewAgeLimit);
        txtFile = view.findViewById(R.id.textViewFile);
        toolbar = view.findViewById(R.id.toolbar);
        mSelectedItems = new ArrayList();
        videoLayout = view.findViewById(R.id.videoLayout);
        progressDialog = new ProgressDialog(getContext());
    }

}
