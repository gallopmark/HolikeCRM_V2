package com.holike.crm.fragment.customer;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.model.event.EventCurrentResult;
import com.holike.crm.model.event.EventQRCodeScanResult;
import com.holike.crm.presenter.fragment.ScanByPhonePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.fragment.ScanByPhoneView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.core.ScanBoxView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 手机直接扫描
 */
public class ScanByPhoneFragment extends MyFragment<ScanByPhonePresenter, ScanByPhoneView> implements ScanByPhoneView, QRCodeView.Delegate {
    @BindView(R.id.zxingView)
    ZXingView mZXingView;
    @BindView(R.id.tv_receiving_count)
    TextView tvReceivingCount;
    @BindView(R.id.tv_current_scan_numb)
    TextView tvCurrentScanNumb;
    @BindView(R.id.tv_scan_lighting)
    TextView tvScanLighting;

    //    Bundle bundle;
    private EventCurrentResult currentResult;
    public int scanCount;
    private boolean isLightingOpen = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int tempSize, tempHeight;

    @Override
    protected ScanByPhonePresenter attachPresenter() {
        return new ScanByPhonePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_scan_by_phone;
    }

    @Override
    protected void init() {
        super.init();
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentResult = (EventCurrentResult) bundle.getSerializable("a");
            tvReceivingCount.setText(String.format(getString(R.string.scan_by_phone_received), currentResult.getIndex()));
            tvCurrentScanNumb.setText(String.format(getString(R.string.scan_by_phone_current_numb), currentResult.getResult()));
            scanCount = currentResult.getIndex();
        } else {
            currentResult = new EventCurrentResult();
            currentResult.setResults(new ArrayList<>());
            tvReceivingCount.setText(String.format(getString(R.string.scan_by_phone_received), 0));
            tvCurrentScanNumb.setText(String.format(getString(R.string.scan_by_phone_current_numb), "无"));
            scanCount = 0;
        }
        //保持屏幕唤醒状态
        ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕唤醒
        tempSize = DensityUtil.getScreenWidth(mContext) * 2 / 3;
        tempHeight = (DensityUtil.getScreenHeight(mContext) - tempSize) / 2;
        setupLayout();
        setupScanBoxView();
        mZXingView.setDelegate(this);
    }

    /*设置头部和底部布局，达到适配各个屏幕大小的手机*/
    private void setupLayout() {
        LinearLayout topLayout = mContentView.findViewById(R.id.mTopLayout);
        FrameLayout.LayoutParams topLayoutParams = (FrameLayout.LayoutParams) topLayout.getLayoutParams();
        topLayoutParams.height = tempHeight * 3 / 4;
        LinearLayout bottomLayout = mContentView.findViewById(R.id.mBottomLayout);
        FrameLayout.LayoutParams bottomLayoutParams = (FrameLayout.LayoutParams) bottomLayout.getLayoutParams();
        bottomLayoutParams.height = tempHeight * 3 / 4;
    }

    /*设置扫码框大小  适配各种屏幕的手机*/
    private void setupScanBoxView() {
        ScanBoxView scanBoxView = mZXingView.getScanBoxView();
        scanBoxView.setTopOffset(tempHeight);
        scanBoxView.setRectWidth(tempSize);
        scanBoxView.setBarcodeRectHeight(tempSize);
    }

    @Override
    public void onResume() {
        super.onResume();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    public void onSuccess(String result, List<EventQRCodeScanResult> results) {
        tvCurrentScanNumb.setText(String.format(getString(R.string.scan_by_phone_current_numb), result));
        tvReceivingCount.setText(String.format(getString(R.string.scan_by_phone_received), results.size()));
        currentResult.setResults(results);
        EventBus.getDefault().post(results);
    }

    @Override
    public void onFail(String reason) {
        showShortToast(reason.equals(Constants.SCAN_REPEAT) ? getString(R.string.scan_by_phone_scanned) : reason, CompatToast.Gravity.LOW);
    }

    @Override
    public void resumeCamera() {
        dismissLoading();
        mHandler.postDelayed(() -> {
            mZXingView.startSpot(); // 打开后置摄像头开始预览，但是并未开始识别
            mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        }, 500);
    }

    @Override
    public void onDelayDone() {
    }

    @OnClick({R.id.tv_scan_finish, R.id.tv_scan_lighting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_scan_finish:
                finishFragment();
                break;
            case R.id.tv_scan_lighting:
                if (isLightingOpen) {
                    Drawable nav_up = getResources().getDrawable(R.drawable.scanning_light_c);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    tvScanLighting.setCompoundDrawables(null, nav_up, null, null);
                    mZXingView.closeFlashlight();
                    isLightingOpen = false;
                } else {
                    Drawable nav_up = getResources().getDrawable(R.drawable.scanning_light_o);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    tvScanLighting.setCompoundDrawables(null, nav_up, null, null);
                    isLightingOpen = true;
                    mZXingView.openFlashlight();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mZXingView.stopSpot();
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        mZXingView.stopCamera();
        super.onDestroyView();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (!TextUtils.isEmpty(result)) {
            vibrate();
            showLoading();
            mPresenter.getCodeInfo(result, currentResult.getResults());
            mHandler.postDelayed(this::resumeCamera, 500);
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showShortToast(mContext.getString(R.string.scan_open_camera_error));
    }
}
