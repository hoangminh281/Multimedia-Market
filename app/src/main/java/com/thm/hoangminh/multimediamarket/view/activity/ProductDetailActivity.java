package com.thm.hoangminh.multimediamarket.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.ViewPagerAdapter;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.fomular.MoneyFormular;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.ProductDetailPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.ProductDetailPresenterImpl;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.references.ConvertNumberToString;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.view.callback.ProductDetailView;
import com.thm.hoangminh.multimediamarket.view.fragment.RatingFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailView {
    private Menu menu;
    private Dialog checkoutDialog;
    private Toolbar toolbar;
    private EditText edtRating;
    private ViewPager viewPager;
    private ImageView imgThanks;
    private CheckBox cbBookmark;
    private ImageView imgProduct;
    private ProgressBar pgbCheckoutDialog;
    private CircleImageView imgUser;
    private RelativeLayout checkoutComponent;
    private RelativeLayout ratingLayout;
    private RatingBar ratingBarOverview, rtbUser;
    private Button btnBuy, btnRating, btnCheckout;
    private YouTubePlayerSupportFragment youtubeView;
    private LinearLayout ratingContentLayout, ratingSuccessLayout;
    private TextView txtTitle, txtOwnername, txtAgelimit, txtDownloaded, txtSubdownloaded,
            txtRating, txtDescription, txtRead, txtIntro, txtRatingSum, txtWallet, txtContent;

    private ViewPagerAdapter adapter;
    private ArrayList<Uri> imagesList;
    private ProductDetailPresenter presenter;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    public static final String googleApiKey = "AIzaSyBzvhnvsvpM2Kpy6_2ceRthi59uJx2Lyxg";
    private Product product;

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            presenter.extractBundle(bundle);
            setEvents();
        }
    }

    private void initPresenter() {
        presenter = new ProductDetailPresenterImpl(this);
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
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CateIdKey, product.getCateId());
                bundle.putString(Constants.ProductIdKey, product.getProductId());
                startActivity(UpdateProductActivity.class, bundle);
                break;
            case R.id.menu_active:
                activeOrDeactiveProduct(true);
                return true;
            case R.id.menu_deactive:
                activeOrDeactiveProduct(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void activeOrDeactiveProduct(boolean b) {
        presenter.activeOrDeactiveProduct(b);
    }

    private void initAdapter() {
        imagesList = new ArrayList<>();
        adapter = new ViewPagerAdapter(this, imagesList);
        viewPager.setAdapter(adapter);
    }

    private void initYoutubeLayout(final String videoId) {
        youtubeView.initialize(googleApiKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    youTubePlayer.cueVideo(videoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public void showProduct(Product product) {
        this.product = product;
        ImageLoader.loadImage(ProductStorageRepository.class, this, imgProduct, this.product.getPhotoId());
        txtTitle.setText(product.getTitle());
        txtRating.setText(product.getRating() + "");
        ratingBarOverview.setRating((float) product.getRating());
        btnBuy.setText(MoneyFormular.format(product.getPrice()));
    }

    @Override
    public void showProductDetail(ProductDetail productDetail) {
        initYoutubeLayout(productDetail.getVideoId());
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
        ImageLoader.loadImage(UserStorageRepository.class, this, imgUser, user.getImage());
    }

    @Override
    public void activeOrDeactiveBookmark(boolean b) {
        cbBookmark.setChecked(b);
        cbBookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.enableOrDisableBookmark(b);
            }
        });
    }

    @Override
    public void addUriToSliderLayout(Uri uri) {
        viewPager.setVisibility(View.VISIBLE);
        imagesList.add(uri);
    }

    @Override
    public void refreshSliderAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void unCollapseDescription(View view) {
        if (txtDescription.isShown()) {
            txtRead.setText(R.string.txt_readmore);
            AnimationSupport.slideUp(this, txtDescription);
            txtDescription.setVisibility(View.GONE);

        } else {
            txtDescription.setVisibility(View.VISIBLE);
            AnimationSupport.slideDown(this, txtDescription);
            txtRead.setText(R.string.txt_readless);
        }
    }

    @Override
    public void showSuccessRatingLayout() {
        if (ratingLayout.isShown()) {
            AnimationSupport.fadeOut(this, ratingContentLayout);
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
                                    AnimationSupport.fadeIn(ProductDetailActivity.this, ratingSuccessLayout);
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
    public void showMessage(int resource) {
        Toast.makeText(this, getResources().getString(resource), Toast.LENGTH_SHORT).show();
        if (checkoutDialog != null && checkoutDialog.isShowing()) checkoutDialog.dismiss();
    }

    @Override
    public void showRatingFragment(ArrayList<ProductRating> ratingList) {
        txtRatingSum.setText(ratingList.size() + "");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.RatingListKey, ratingList);
        bundle.putDouble(Constants.RatingPointKey, product.getRating());
        RatingFragment ratingFragment = new RatingFragment();
        ratingFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameRatingComment, ratingFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void showProgressbarDialog() {
        if (checkoutComponent != null) checkoutComponent.setVisibility(View.INVISIBLE);
        if (pgbCheckoutDialog != null) pgbCheckoutDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbarDialog() {
        if (checkoutComponent != null) checkoutComponent.setVisibility(View.VISIBLE);
        if (pgbCheckoutDialog != null) pgbCheckoutDialog.setVisibility(View.INVISIBLE);
    }

    @Override
    public void closeCheckoutDialog() {
        if (checkoutDialog.isShowing()) checkoutDialog.dismiss();
    }

    @Override
    public void enableOrDisableProductCheckout(double balance, boolean b) {
        if (b) {
            txtWallet.setText(MoneyFormular.format(balance));
            txtContent.setText(R.string.txt_allowCheckout);
            btnCheckout.setText(R.string.btn_checkout);
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.checkoutProduct();
                }
            });
        } else {
            txtWallet.setText(MoneyFormular.format(balance));
            txtWallet.setTextColor(getResources().getColor(R.color.red));
            txtContent.setText(R.string.txt_NotAllowCheckout);
            btnCheckout.setText(R.string.btn_recharge);
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(RechargeActivity.class);
                }
            });
        }
    }

    @Override
    public void showOrHideRatingLayout(boolean b) {
        ratingLayout.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setVisibleItemMenu(int itemId, boolean b) {
        if (menu != null) {
            menu.findItem(itemId).setVisible(b);
        }
    }

    @Override
    public void enableOrDisableDownloadButton(boolean b) {
        btnBuy.setEnabled(true);
        if (b) {
            btnBuy.setBackgroundColor(getResources().getColor(R.color.theme_app));
            btnBuy.setText(R.string.btn_install);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnBuy.setBackgroundColor(getResources().getColor(R.color.grey_50));
                    btnBuy.setText(R.string.btn_loading);
                    presenter.downLoadProduct();
                }
            });
        } else {
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkoutDialog == null) initBuyingDialog();
                    presenter.loadUserWallet();
                    checkoutDialog.show();
                }
            });
        }
    }

    private void initBuyingDialog() {
        checkoutDialog = new Dialog(ProductDetailActivity.this);
        checkoutDialog.setContentView(R.layout.checkout_dialog);
        checkoutDialog.setTitle(product.getTitle());

        ImageView imgLogo = checkoutDialog.findViewById(R.id.imageViewLogo);
        TextView txtTitle = checkoutDialog.findViewById(R.id.textViewTitle);
        TextView txtPrice = checkoutDialog.findViewById(R.id.textViewPrice);
        txtContent = checkoutDialog.findViewById(R.id.textViewContent);
        txtWallet = checkoutDialog.findViewById(R.id.textViewWallet);
        btnCheckout = checkoutDialog.findViewById(R.id.buttonCheckout);
        checkoutComponent = checkoutDialog.findViewById(R.id.relativeLayoutDialog);
        pgbCheckoutDialog = checkoutDialog.findViewById(R.id.progressBarDialog);
        // khai báo control trong checkoutDialog để bắt sự kiện
        imgLogo.setImageBitmap(((BitmapDrawable) imgProduct.getDrawable()).getBitmap());
        txtTitle.setText(product.getTitle());
        txtPrice.setText(MoneyFormular.format(product.getPrice()));
    }

    private void setEvents() {
        rtbUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                    ratingBar.setRating((int) v);
                    btnRating.setEnabled(true);
                }
            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductRating productRating = new ProductRating();
                productRating.setProductId(product.getProductId());
                productRating.setPoint((int) rtbUser.getRating());
                productRating.setComment(edtRating.getText().toString());
                presenter.addRating(productRating);
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
        ratingBarOverview = findViewById(R.id.ratingBar);
        btnBuy = findViewById(R.id.buttonBuy);
        youtubeView = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_layout);
        txtIntro = findViewById(R.id.textViewIntro);
        txtDescription = findViewById(R.id.textViewDescription);
        txtDescription.setVisibility(View.GONE);
        txtRead = findViewById(R.id.textViewRead);
        viewPager = findViewById(R.id.viewPager);
        rtbUser = findViewById(R.id.ratingBarUser);
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

    @Override
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
