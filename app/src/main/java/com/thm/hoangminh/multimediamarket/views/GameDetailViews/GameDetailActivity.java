package com.thm.hoangminh.multimediamarket.views.GameDetailViews;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.ViewPagerAdapter;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.views.fragments.RatingFragment;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.references.ConvertNumberToString;
import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters.GameDetailPresenter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameDetailActivity extends FragmentActivity implements GameDetailView {

    private ImageView imgGame;
    private TextView txtTitle, txtOwnername, txtAgelimit, txtDownloaded, txtSubdownloaded,
            txtRating, txtDescription, txtRead, txtIntro, txtRatingSum;
    private RatingBar ratingBar,rtb_User;
    private Button btnBuy, btnRating;
    private GameDetailPresenter presenter;
    private Game game;
    private YouTubePlayerView youtubeView;
    private String video_id;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private ViewPagerAdapter adapter;
    private ArrayList<String> imagesList;
    private ViewPager viewPager;
    private EditText edtRating;
    private RelativeLayout ratingLayout;
    private CircleImageView imgUser;
    private RatingFragment ratingFragment;

    public static final String googleApiKey = "AIzaSyBzvhnvsvpM2Kpy6_2ceRthi59uJx2Lyxg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_detail_layout);

        setControls();

        initPresenter();

        initAdapter();

        initYoutubeLayout();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            game = (Game) bundle.getSerializable("game_object");

            setContents();
/*
            RatingFragment ratingFragment
                    = (RatingFragment) this.getSupportFragmentManager()
                    .findFragmentById(R.id.fragmentRatingComment);
            ratingFragment.setRatingOverview(game.getRating());*/

            presenter.LoadGameDetailById(game.getGame_id());
            presenter.LoadRating(game.getGame_id());

        }
        setEvents();
    }

    private void initPresenter() {
        presenter = new GameDetailPresenter(this, this);
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

    private void setContents() {
        game.setBitmapImage(imgGame, this);
        txtTitle.setText(game.getTitle());
        txtRating.setText(game.getRating() + "");
        ratingBar.setRating((float) game.getRating());
        btnBuy.setText(game.getPrice() + "");
    }

    @Override
    public void showGameDetail(GameDetail gameDetail) {
        video_id = gameDetail.getVideo();
        youtubeView.initialize(googleApiKey, onInitializedListener);
        txtAgelimit.setText(gameDetail.getAgeLimit() + "+");
        ConvertNumberToString convert = new ConvertNumberToString(gameDetail.getDownloaded(), getResources().getStringArray(R.array.unit));
        txtDownloaded.setText(convert.getNumber() + "");
        txtSubdownloaded.setText(convert.getUnit());
        txtIntro.setText(gameDetail.getIntro());
        txtDescription.setText(gameDetail.getDescription());
    }

    @Override
    public void showOwner(User user) {
        txtOwnername.setText(user.getName());
    }

    @Override
    public void addLinkIntoData(String link) {
        imagesList.add(link);
    }

    @Override
    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void UnCollapse_Description(View view) {
        if (txtDescription.isShown()) {
            txtRead.setText(getResources().getString(R.string.txt_readmore));
            AnimationSupport.slide_up(this, txtDescription);
            txtDescription.setVisibility(View.GONE);

        } else {
            txtDescription.setVisibility(View.VISIBLE);
            AnimationSupport.slide_down(this, txtDescription);
            txtRead.setText(getResources().getString(R.string.txt_readless));
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
    public void slideUpRatingLayout() {
        if (ratingLayout.isShown()) {
            AnimationSupport.slide_up(this, ratingLayout);
            new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ratingLayout.setVisibility(View.GONE);
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
    }

    @Override
    public void showRatingPoint(ArrayList<RatingContent> ratingList) {
        txtRatingSum.setText(ratingList.size() + "");

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ratingList", ratingList);
        bundle.putDouble("rating_point", game.getRating());
        ratingFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameRatingComment, ratingFragment);
        fragmentTransaction.commit();
    }

    private void setEvents() {
        rtb_User.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                btnRating.setEnabled(true);
            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewRating(game.getGame_id(), rtb_User.getRating(), edtRating.getText().toString());
            }
        });
    }

    private void setControls() {
        imgGame = findViewById(R.id.imageViewPhoto);
        txtTitle = findViewById(R.id.textViewTitle);
        txtOwnername = findViewById(R.id.textViewOwner);
        txtAgelimit = findViewById(R.id.textViewAgeLimit);
        txtDownloaded = findViewById(R.id.textViewDownloaded);
        txtSubdownloaded = findViewById(R.id.textViewSubDownloaded);
        txtRating = findViewById(R.id.textViewRating);
        ratingBar = findViewById(R.id.ratingBar);
        btnBuy = findViewById(R.id.buttonBuy);
        youtubeView = findViewById(R.id.youtube_layout);
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
        ratingLayout = findViewById(R.id.relativeLayoutRatingOverview);
        ratingFragment= new RatingFragment();
    }

}
