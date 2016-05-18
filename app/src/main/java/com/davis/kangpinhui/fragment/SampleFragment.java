package com.davis.kangpinhui.fragment;

import android.animation.Animator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.fragment.base.BaseFragment;

/**
 * Created by davis on 16/5/16.
 */
public class SampleFragment extends BaseFragment {
    @Override
    protected void initVariable() {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mian;
    }

    @Override
    protected void findViews(View view) {

        TextView aaa=$(view,R.id.aaa);
        TextView aaaa=$(view,R.id.aaaa);
        ImageView aaaaa=$(view,R.id.aaaaa);

        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "aaa", Snackbar.LENGTH_SHORT).show();
            }
        });
        aaaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "aaaa", Snackbar.LENGTH_SHORT).show();
            }
        });
        aaaaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "aaaaa", Snackbar.LENGTH_SHORT).show();
            }
        });



        final View oval = $(view,R.id.oval);
        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        oval,
                        oval.getWidth()/2,
                        oval.getHeight()/2,
                        oval.getWidth(),
                        0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });

        final View rect = $(view, R.id.rect);

        rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        rect,
                        0,
                        0,
                        0,
                        (float) Math.hypot(rect.getWidth(), rect.getHeight()));
                animator.setInterpolator(new AccelerateInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }

    @Override
    protected void initData() {



    }

    @Override
    protected void setListener() {

    }

    public static Fragment newInstance(int i) {
        return null;
    }
}
