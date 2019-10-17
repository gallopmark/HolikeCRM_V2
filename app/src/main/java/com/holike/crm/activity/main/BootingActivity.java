package com.holike.crm.activity.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.holike.crm.R;
import com.holike.crm.activity.login.LoginActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.presenter.activity.BootingPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.activity.BootingView;


import butterknife.BindView;
import butterknife.OnClick;


/**
 * 启动页
 */
public class BootingActivity extends BaseActivity<BootingPresenter, BootingView> implements BootingView {

    @BindView(R.id.fl_booting_main)
    FrameLayout flMain;
    @BindView(R.id.iv_booting_ad)
    ImageView ivAd;
    @BindView(R.id.btn_booting_skip)
    TextView btnSkip;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isStartSkip;

    @Override
    protected BootingPresenter attachPresenter() {
        return new BootingPresenter();
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        mHandler.postDelayed(startRunnable, 3000);
        mPresenter.getAd();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_booting;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    /**
     * 跳过
     */
    public void startSkip() {
        mHandler.removeCallbacks(startRunnable);
        if (!isStartSkip) {
            isStartSkip = true;
            Intent intent;
            if (TextUtils.isEmpty(SharedPreferencesUtils.getUserId())) {
                intent = new Intent(this, LoginActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

    /**
     * 获取广告成功
     */
    @Override
    public void getAdSuccess(String url) {
        mHandler.removeCallbacks(startRunnable);
        if (TextUtils.isEmpty(url)) {
            startSkip();
        } else {
            flMain.setBackgroundColor(ContextCompat.getColor(this, R.color.color_while));
            mHandler.postDelayed(startRunnable, 5000);
            btnSkip.setVisibility(View.VISIBLE);
            Glide.with(this).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    startSkip();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    mHandler.removeCallbacks(startRunnable);
                    mHandler.postDelayed(startRunnable, 2000);
                    return false;
                }
            }).into(ivAd);
        }
    }

    /**
     * 获取广告失败
     */
    @Override
    public void getAdFailed(String failed) {
        startSkip();
    }

    @OnClick(R.id.btn_booting_skip)
    public void onViewClicked() {
        startSkip();
    }

    private Runnable startRunnable = this::startSkip;

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
