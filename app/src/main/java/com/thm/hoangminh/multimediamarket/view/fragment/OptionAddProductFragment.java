package com.thm.hoangminh.multimediamarket.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;

public class OptionAddProductFragment extends Fragment {
    private Toolbar toolbar;
    private ImageView imgGame, imgImage, imgVideo, imgMusic;

    private int requestCode;
    private RelativeLayout optionLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.option_modify_product, container, false);
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu_upload);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        setEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return false;
    }

    private void setEvents() {
        imgGame.setTag(0);
        imgGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddProductFragment(Category.getInstance().get(1).getCateId(), imgGame);
                requestCode = 0;
            }
        });

        imgImage.setTag(1);
        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddProductFragment(Category.getInstance().get(2).getCateId(), imgImage);
                requestCode = 1;
            }
        });

        imgVideo.setTag(2);
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddProductFragment(Category.getInstance().get(3).getCateId(), imgVideo);
                requestCode = 2;
            }
        });

        imgMusic.setTag(3);
        imgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddProductFragment(Category.getInstance().get(4).getCateId(), imgMusic);
                requestCode = 3;
            }
        });
    }

    private void createAddProductFragment(final String cateId, final ImageView img) {
        AnimationSupport.zoomIn(getContext(), img);
        AnimationSupport.fadeOut(getContext(), optionLayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        Thread.sleep(400);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                optionLayout.clearAnimation();
                                optionLayout.setVisibility(View.GONE);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                AddProductFragment fragment = new AddProductFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.CateIdKey, cateId);
                                fragment.setArguments(bundle);
                                transaction.add(R.id.frameLayout, fragment).addToBackStack(null);
                                transaction.commit();

                                getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                                    @Override
                                    public void onBackStackChanged() {
                                        if (((int) img.getTag()) == requestCode && getFragmentManager().getBackStackEntryCount() == 0) {
                                            AnimationSupport.fadeIn(getContext(), optionLayout);
                                            AnimationSupport.zoomOut(getContext(), img);
                                        }
                                    }
                                });
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setControls(View view) {
        optionLayout = view.findViewById(R.id.optionLayout);
        imgGame = view.findViewById(R.id.imageViewGame);
        imgImage = view.findViewById(R.id.imageViewImage);
        imgVideo = view.findViewById(R.id.imageViewVideo);
        imgMusic = view.findViewById(R.id.imageViewMusic);
        toolbar = view.findViewById(R.id.toolbar);
    }

}
