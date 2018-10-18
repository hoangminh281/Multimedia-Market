package com.thm.hoangminh.multimediamarket.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.ViewPagerAdapter;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.view.callback.ProductDetailView;
import com.thm.hoangminh.multimediamarket.view.fragment.RatingFragment;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.references.ConvertNumberToString;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.implement.ProductDetailPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailView {
    private ImageView imgProduct;
    private TextView txtTitle, txtOwnername, txtAgelimit, txtDownloaded, txtSubdownloaded,
            txtRating, txtDescription, txtRead, txtIntro, txtRatingSum, txtWallet, txtContent;
    private RatingBar ratingBar, rtb_User;
    private Button btnBuy, btnRating, btnCheckout;
    private ProductDetailPresenter presenter;
    private Product product;
    private YouTubePlayerSupportFragment youtubeView;
    private String video_id;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private ViewPagerAdapter adapter;
    private ArrayList<String> imagesList;
    private ViewPager viewPager;
    private EditText edtRating;
    private RelativeLayout ratingLayout;
    private LinearLayout ratingContentLayout, ratingSuccessLayout;
    private CircleImageView imgUser;
    private RelativeLayout rlDialog;
    private ProgressBar pgbDialog;
    private Dialog dialog;
    private CheckBox cbBookmark;
    private ImageView imgThanks;
    private Toolbar toolbar;

    public static final String googleApiKey = "AIzaSyBzvhnvsvpM2Kpy6_2ceRthi59uJx2Lyxg";
    private String owner_id;
    private Menu menu;
    private String fileName;
    private String user_id;
    private int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_layout);

        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        initPresenter();

        initAdapter();

        initYoutubeLayout();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String cate_id = bundle.getString("cate_id");
            String product_id = bundle.getString("product_id");

            presenter.LoadProductById(product_id);
            presenter.LoadProductDetailById(product_id);
            presenter.LoadBookmarkContent(cate_id, product_id);
            presenter.LoadProductTransactionHistory(cate_id, product_id);
            presenter.LoadRating(product_id);

        }
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_detail_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
                break;
            case R.id.menu_update:
                Intent intent = new Intent(this, UpdateProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("product_id", product.getProduct_id());
                bundle.putString("cate_id", product.getCate_id());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.menu_active:
                ActiveContent(1);
                return true;
            case R.id.menu_inactive:
                ActiveContent(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ActiveContent(final int status) {
        presenter.ActiveProduct(product.getProduct_id(), status);
    }

    private void initPresenter() {
        presenter = new ProductDetailPresenter(this);
    }

    private void initAdapter() {
        imagesList = new ArrayList<>();
        adapter = new ViewPagerAdapter(this, imagesList);
        viewPager.setAdapter(adapter);
    }

    private void initYoutubeLayout() {
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    youTubePlayer.cueVideo(video_id);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
    }

    @Override
    public void showProductDetail(ProductDetail productDetail) {
        fileName = productDetail.getDownloadLink();
        owner_id = productDetail.getOwner_id();
        presenter.LoadCurrentUser();
        video_id = productDetail.getVideo();
        youtubeView.initialize(googleApiKey, onInitializedListener);
        txtAgelimit.setText(productDetail.getAgeLimit() + "+");
        ConvertNumberToString convert = new ConvertNumberToString(productDetail.getDownloaded(), getResources().getStringArray(R.array.unit));
        txtDownloaded.setText(convert.getNumber() + "");
        txtSubdownloaded.setText(convert.getUnit());
        txtIntro.setText(productDetail.getIntro());
        txtDescription.setText(productDetail.getDescription());
    }

    @Override
    public void showOwner(User user) {
        txtOwnername.setText(user.getName());
    }

    @Override
    public void checkBookmark(boolean b) {
        cbBookmark.setChecked(b);
        cbBookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    presenter.SavedProductBookmark(product.getCate_id(), product.getProduct_id());
                } else {
                    presenter.UnSavedProductBookmark(product.getCate_id(), product.getProduct_id());
                }
            }
        });
    }

    @Override
    public void addLinkIntoSlider(String link) {
        viewPager.setVisibility(View.VISIBLE);
        imagesList.add(link);
    }

    @Override
    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void UnCollapse_Description(View view) {
        if (txtDescription.isShown()) {
            txtRead.setText(R.string.txt_readmore);
            AnimationSupport.slide_up(this, txtDescription);
            txtDescription.setVisibility(View.GONE);

        } else {
            txtDescription.setVisibility(View.VISIBLE);
            AnimationSupport.slide_down(this, txtDescription);
            txtRead.setText(R.string.txt_readless);
        }
    }

    @Override
    public void showBottomProgressbar() {

    }

    @Override
    public void hideBottomProgressbar() {

    }

    @Override
    public void showRatingLayout() {
        ratingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRatingSuccessLayout() {
        if (ratingLayout.isShown()) {
            AnimationSupport.fade_out(this, ratingContentLayout);
            new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(400);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ratingContentLayout.setVisibility(View.GONE);
                                    String[] tickets = getResources().getStringArray(R.array.thanksTickers);
                                    ArrayList<String> ticketList = new ArrayList<>(Arrays.asList(tickets));
                                    Collections.shuffle(ticketList);
                                    imgThanks.setImageResource(getResources().getIdentifier(ticketList.get(0), "mipmap", getPackageName()));
                                    AnimationSupport.fade_in(ProductDetailActivity.this, ratingSuccessLayout);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Override
    public void showImageUser(String user_image_link) {
        Picasso.with(this)
                .load(user_image_link)
                .into(imgUser);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void showMessageFromResource(int resource) {
        Toast.makeText(this, getResources().getString(resource), Toast.LENGTH_SHORT).show();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void showRatingFragment(ArrayList<ProductRating> ratingList) {
        txtRatingSum.setText(ratingList.size() + "");

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ratingList", ratingList);
        bundle.putDouble("rating_point", product.getRating());
        bundle.putInt("limit", 3);
        RatingFragment ratingFragment = new RatingFragment();
        ratingFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameRatingComment, ratingFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void showDialogProgressbar() {
        if (rlDialog != null) rlDialog.setVisibility(View.INVISIBLE);
        if (pgbDialog != null) pgbDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialogProgressbar() {
        if (rlDialog != null) rlDialog.setVisibility(View.VISIBLE);
        if (pgbDialog != null) pgbDialog.setVisibility(View.INVISIBLE);
    }

    @Override
    public void closeDialogProgressbar() {
        dialog.dismiss();
    }

    @Override
    public void AllowCheckout(double balance) {
        txtWallet.setText(Tools.FormatMoney(balance));
        txtContent.setText(R.string.txt_allowCheckout);
        btnCheckout.setText(R.string.btn_checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.CheckoutProduct(product.getCate_id(), product.getProduct_id(), owner_id);
            }
        });
    }

    @Override
    public void NotAllowCheckout(double balance) {
        txtWallet.setText(Tools.FormatMoney(balance));
        txtWallet.setTextColor(getResources().getColor(R.color.red));
        txtContent.setText(R.string.txt_NotAllowCheckout);
        btnCheckout.setText(R.string.btn_recharge);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, RechargeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void EnableInstall() {
        btnBuy.setEnabled(true);
        btnBuy.setBackgroundColor(getResources().getColor(R.color.theme_app));
        btnBuy.setText(R.string.btn_install);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBuy.setEnabled(false);
                btnBuy.setBackgroundColor(getResources().getColor(R.color.grey_50));
                btnBuy.setText(R.string.btn_loading);
                presenter.downLoadProduct(ProductDetailActivity.this, fileName);
            }
        });
    }

    @Override
    public void onLoadCurrentUserSuccess(int role, String user_id) {
        this.role = role;
        this.user_id = user_id;
        if (owner_id.equals(user_id) || role == User.ADMIN) {
            menu.findItem(R.id.menu_update).setVisible(true);
        } else {
            menu.findItem(R.id.menu_update).setVisible(false);
        }
        EnableModifyProduct();
    }

    @Override
    public void showMessage(int messageId) {
        Toast.makeText(this, getResources().getString(messageId), Toast.LENGTH_SHORT).show();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void EnableButtonBuy() {
        btnBuy.setEnabled(true);
    }

    @Override
    public void showProduct(Product value) {
        this.product = value;
        product.setBitmapImage(imgProduct, this);
        txtTitle.setText(product.getTitle());
        txtRating.setText(product.getRating() + "");
        ratingBar.setRating((float) product.getRating());
        btnBuy.setText(Tools.FormatMoney(product.getPrice()));
        EnableModifyProduct();
    }

    private void EnableModifyProduct() {
        if (product == null || owner_id == null) return;
        if (owner_id.equals(user_id) || role == User.ADMIN) {
            if (product.getStatus() == 0) {
                menu.findItem(R.id.menu_inactive).setVisible(false);
                menu.findItem(R.id.menu_active).setVisible(true);
            } else {
                menu.findItem(R.id.menu_inactive).setVisible(true);
                menu.findItem(R.id.menu_active).setVisible(false);
            }
        } else {
            menu.findItem(R.id.menu_inactive).setVisible(false);
            menu.findItem(R.id.menu_active).setVisible(false);
        }
    }

    private void setEvents() {
        rtb_User.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                    ratingBar.setRating((float) Math.floor(v));
                    btnRating.setEnabled(true);
                }
            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewRating(product.getProduct_id(), rtb_User.getRating(), edtRating.getText().toString());
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProductDetailActivity.this);
                // khởi tạo dialog
                dialog.setContentView(R.layout.checkout_dialog);
                // xét layout cho dialog
                dialog.setTitle(product.getTitle());
                // xét tiêu đề cho dialog

                ImageView imgLogo = dialog.findViewById(R.id.imageViewLogo);
                TextView txtTitle = dialog.findViewById(R.id.textViewTitle);
                TextView txtPrice = dialog.findViewById(R.id.textViewPrice);
                txtContent = dialog.findViewById(R.id.textViewContent);
                txtWallet = dialog.findViewById(R.id.textViewWallet);
                btnCheckout = dialog.findViewById(R.id.buttonCheckout);
                rlDialog = dialog.findViewById(R.id.relativeLayoutDialog);
                pgbDialog = dialog.findViewById(R.id.progressBarDialog);
                // khai báo control trong dialog để bắt sự kiện
                presenter.LoadUserWallet(product.getPrice());
                product.setBitmapImage(imgLogo, ProductDetailActivity.this);
                txtTitle.setText(product.getTitle());
                txtPrice.setText(Tools.FormatMoney(product.getPrice()));

                // bắt sự kiện cho nút đăng kí
                dialog.show();

            }
        });
    }

    private void setControls() {
        imgProduct = findViewById(R.id.imageViewPhoto);
        txtTitle = findViewById(R.id.textViewTitle);
        txtOwnername = findViewById(R.id.textViewOwner);
        txtAgelimit = findViewById(R.id.textViewAgeLimit);
        cbBookmark = findViewById(R.id.checkBoxSave);
        txtDownloaded = findViewById(R.id.textViewDownloaded);
        txtSubdownloaded = findViewById(R.id.textViewSubDownloaded);
        txtRating = findViewById(R.id.textViewRating);
        ratingBar = findViewById(R.id.ratingBar);
        btnBuy = findViewById(R.id.buttonBuy);
        youtubeView = (YouTubePlayerSupportFragment) this.getSupportFragmentManager().findFragmentById(R.id.youtube_layout);
        txtIntro = findViewById(R.id.textViewIntro);
        txtDescription = findViewById(R.id.textViewDescription);
        txtDescription.setVisibility(View.GONE);
        txtRead = findViewById(R.id.textViewRead);
        viewPager = findViewById(R.id.viewPager);
        rtb_User = findViewById(R.id.ratingBarUser);
        imgUser = findViewById(R.id.imageViewUser);
        edtRating = findViewById(R.id.editTextRating);
        txtRatingSum = findViewById(R.id.textViewRatingSum);
        btnRating = findViewById(R.id.buttonRating);
        ratingLayout = findViewById(R.id.RatingLayout);
        ratingContentLayout = findViewById(R.id.ratingContentLayout);
        ratingSuccessLayout = findViewById(R.id.ratingSuccessLayout);
        imgThanks = findViewById(R.id.imageViewThanks);
        toolbar = findViewById(R.id.toolbar);
    }
}
