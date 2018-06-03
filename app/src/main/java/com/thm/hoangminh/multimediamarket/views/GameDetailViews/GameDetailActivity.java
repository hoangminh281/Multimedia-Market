package com.thm.hoangminh.multimediamarket.views.GameDetailViews;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.ViewPagerAdapter;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.references.ConvertDownloadedNumber;
import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters.GameDetailPresenter;

import java.util.ArrayList;

public class GameDetailActivity extends YouTubeBaseActivity implements GameDetailView {

    private ImageView img;
    private TextView txtTitle, txtOwner_name, txtAge_limit, txtDownloaded, txtSub_downloaded, txtRating, txtDescription, txtRead, txtIntro;
    private RatingBar ratingBar;
    private Button btnBuy;
    private GameDetailPresenter presenter;
    private Game game;
    private YouTubePlayerView youtubeView;
    private String video_id;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private ViewPagerAdapter adapter;
    private ArrayList<String> imagesList;
    private ViewPager viewPager;

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
            presenter.LoadGameDetailById(game.getGame_id());
        }

    }

    private void setControls() {
        img = findViewById(R.id.imageViewPhoto);
        txtTitle = findViewById(R.id.textViewTitle);
        txtOwner_name = findViewById(R.id.textViewOwner);
        txtAge_limit = findViewById(R.id.textViewAgeLimit);
        txtDownloaded = findViewById(R.id.textViewDownloaded);
        txtSub_downloaded = findViewById(R.id.textViewSubDownloaded);
        txtRating = findViewById(R.id.textViewRating);
        ratingBar = findViewById(R.id.ratingBar);
        btnBuy = findViewById(R.id.buttonBuy);
        youtubeView = findViewById(R.id.youtube_layout);
        txtIntro = findViewById(R.id.textViewIntro);
        txtDescription = findViewById(R.id.textViewDescription);
        txtDescription.setVisibility(View.GONE);
        txtRead = findViewById(R.id.textViewRead);
        viewPager = findViewById(R.id.viewPager);
    }

    private void initPresenter() {
        presenter = new GameDetailPresenter(this);
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
        game.setBitmapImage(img, this);
        txtTitle.setText(game.getTitle());
        txtRating.setText(game.getRating() + "");
        ratingBar.setRating((float) game.getRating());
        btnBuy.setText(game.getPrice() + "");
    }

    @Override
    public void showGameDetail(GameDetail gameDetail) {
        video_id = gameDetail.getVideo();
        youtubeView.initialize(googleApiKey, onInitializedListener);
        txtAge_limit.setText(gameDetail.getAgeLimit() + "+");
        ConvertDownloadedNumber convert = new ConvertDownloadedNumber(gameDetail.getDownloaded(), getResources().getStringArray(R.array.unit));
        txtDownloaded.setText(convert.getNumber() + "");
        txtSub_downloaded.setText(convert.getUnit());
        txtIntro.setText(gameDetail.getIntro());
        txtDescription.setText(gameDetail.getDescription());
    }

    @Override
    public void showOwner(User user) {
        txtOwner_name.setText(user.getName());
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
            txtRead.setText(getResources().getString(R.string.txt_Readmore));
            AnimationSupport.slide_up(this, txtDescription);
            txtDescription.setVisibility(View.GONE);

        } else {
            txtDescription.setVisibility(View.VISIBLE);
            AnimationSupport.slide_down(this, txtDescription);
            txtRead.setText(getResources().getString(R.string.txt_Readless));
        }
    }


    @Override
    public void showBottomProgressbar() {

    }

    @Override
    public void hideBottomProgressbar() {

    }
}
