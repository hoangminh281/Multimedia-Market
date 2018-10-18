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
import com.thm.hoangminh.multimediamarket.references.AnimationSupport;
import com.thm.hoangminh.multimediamarket.view.activity.MainActivity;

public class OptionModifyProductFragment extends Fragment {
    private RelativeLayout optionLayout;
    private ImageView imgGame, imgImage, imgVideo, imgMusic;
    private Toolbar toolbar;
    private int requestCode;

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
                createModifyProductFragment(MainActivity.categories.get(0).getCate_id(), imgGame);
                requestCode = 0;
            }
        });

        imgImage.setTag(1);
        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createModifyProductFragment(MainActivity.categories.get(1).getCate_id(), imgImage);
                requestCode = 1;
            }
        });

        imgVideo.setTag(2);
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createModifyProductFragment(MainActivity.categories.get(2).getCate_id(), imgVideo);
                requestCode = 2;
            }
        });

        imgMusic.setTag(3);
        imgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createModifyProductFragment(MainActivity.categories.get(3).getCate_id(), imgMusic);
                requestCode = 3;
            }
        });
    }

    private void createModifyProductFragment(final String cate_key, final ImageView img) {
        AnimationSupport.zoom_in(getContext(), img);
        AnimationSupport.fade_out(getContext(), optionLayout);
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
                                ModifyProductFragment fragment = new ModifyProductFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("keyCategory", cate_key);
                                fragment.setArguments(bundle);
                                transaction.add(R.id.frameLayout, fragment).addToBackStack(null);
                                transaction.commit();

                                getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                                    @Override
                                    public void onBackStackChanged() {
                                        if (((int) img.getTag()) == requestCode && getFragmentManager().getBackStackEntryCount() == 0) {
                                            AnimationSupport.fade_in(getContext(), optionLayout);
                                            AnimationSupport.zoom_out(getContext(), img);
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
