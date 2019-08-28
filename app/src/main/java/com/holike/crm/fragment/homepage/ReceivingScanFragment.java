package com.holike.crm.fragment.homepage;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.base.OnRequestPermissionsCallback;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.customView.InputEditText;
import com.holike.crm.dialog.InputDialog;
import com.holike.crm.fragment.customer.ScanByPhoneFragment;
import com.holike.crm.fragment.customer.ScanGunInstructionActivity;
import com.holike.crm.model.event.EventCurrentResult;
import com.holike.crm.model.event.EventQRCodeScanResult;
import com.holike.crm.presenter.activity.ReceivingScanPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.util.RxLoopTask;
import com.holike.crm.view.activity.ReceivingScanView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收货扫描
 */
public class ReceivingScanFragment extends MyFragment<ReceivingScanPresenter, ReceivingScanView> implements ReceivingScanView, OnRequestPermissionsCallback {
    @BindView(R.id.ll_receiving_scan_way_1)
    LinearLayout llReceivingScanWay1;
    @BindView(R.id.ll_receiving_scan_way_2)
    LinearLayout llReceivingScanWay2;
    @BindView(R.id.et_receiving_way3_input_numb)
    InputEditText etReceivingWay3InputNumb;
    @BindView(R.id.tv_way_3_input_confirm)
    TextView tvWay3InputConfirm;
    @BindView(R.id.tv_received_count)
    TextView tvReceivedCount;
    @BindView(R.id.tv_scan_result_clear)
    TextView tvScanResultClear;
    @BindView(R.id.rv_scan_result_list)
    RecyclerView rvScanResultList;
    @BindView(R.id.tv_scan_result_save)
    TextView tvScanResultSave;
    @BindView(R.id.et_scan_gun)
    EditText etScanGun;
    InputDialog inputDialog;
    private List<EventQRCodeScanResult> results = new ArrayList<>(0);
    private RxLoopTask loopTask;

    @Override
    protected ReceivingScanPresenter attachPresenter() {
        return new ReceivingScanPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_receiving_scan;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.receiving_scan));
        setLeft(getString(R.string.homepage));
        mPresenter.setEditTextWatcher(etScanGun);
        loopTask = new RxLoopTask();
        loopTask.doLoopTask(300, () -> { //循环器 循环监听文本长度
            mPresenter.textLengthListener(etScanGun);
        });
    }

    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        super.clickRightMenu(menuText, actionView);
        startActivity(MessageV2Activity.class);
    }

    @Override
    public void onDestroyView() {
        if (loopTask != null) {
            loopTask.dispose();
        }
        super.onDestroyView();
    }

    @Override
    public void onSuccess(String result) {
        results.clear();
        rvScanResultList.getAdapter().notifyDataSetChanged();
        tvReceivedCount.setText("0");
        showShortToast(R.string.tips_commit_success);
    }

    @Override
    public void onFail(String result) {
        showShortToast(result);
    }

    @Override
    public void onAddResultSuccess(List<EventQRCodeScanResult> results, boolean canClean) {
        this.results = results;
        rvScanResultList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvScanResultList.setNestedScrollingEnabled(false);
        rvScanResultList.smoothScrollToPosition(results.size() - 1);
        rvScanResultList.setAdapter(new CommonAdapter<EventQRCodeScanResult>(mContext, results) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_scan_result;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, EventQRCodeScanResult selectDataBean, int position) {
                TextView textView = holder.obtainView(R.id.tv_item_rv_scan_result);
                String pos = String.format(Locale.getDefault(), "%02d", position + 1);
                textView.setText(String.format(getString(R.string.colon), pos, selectDataBean.getResult()));
            }
        });
        if (canClean) {
            setTextColor(tvScanResultClear, R.color.textColor14);
        }
        tvReceivedCount.setText(results.size() + "");
        if (inputDialog != null)
            inputDialog.setReceivePackCount(results.size() + "");

    }

    @Override
    public void onAddResultFail(int reason) {
        switch (reason) {
            case ReceivingScanPresenter.FAIL_REASON_REPEAT:
                showShortToast(R.string.scan_by_phone_scanned, CompatToast.Gravity.CENTER);
                break;
            case ReceivingScanPresenter.FAIL_REASON_REPEAT_INPUTTED:
                showShortToast(R.string.scan_by_phone_inputted, CompatToast.Gravity.CENTER);
                break;
        }
    }

    @Override
    public void onScanGunSuccess(String result) {
        mPresenter.getCodeInfo(result, results);
        etScanGun.setText("");
        etScanGun.requestFocus();

    }


    @OnClick({R.id.tv_scan_result_save, R.id.ll_receiving_scan_way_1, R.id.ll_receiving_scan_way_2, R.id.tv_scan_result_clear, R.id.tv_way_3_input_confirm})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_scan_result_save:
                if (results.size() == 0) {
                    showShortToast(R.string.receiving_scan_result_no_data);
                    return;
                }
                mPresenter.submit(results);
                break;
            case R.id.ll_receiving_scan_way_1:
                startActivity(ScanGunInstructionActivity.class);
                break;
            case R.id.ll_receiving_scan_way_2:
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1, this);
//                ReceivingScanFragmentPermissionsDispatcher.needCameraWithPermissionCheck(this);
//                etScanGun.clearFocus();
                break;
            case R.id.tv_scan_result_clear:
                results.clear();
                if (rvScanResultList.getAdapter() != null) {
                    rvScanResultList.getAdapter().notifyDataSetChanged();
                    setTextColor(tvScanResultClear, R.color.textColor11);
                    tvReceivedCount.setText("0");
                    if (inputDialog != null)
                        inputDialog.setReceivePackCount("0");
                }

                break;
            case R.id.tv_way_3_input_confirm:
                inputDialog = new InputDialog(mContext, result -> {
                    if (TextUtils.isEmpty(result)) {
                        showShortToast(R.string.receiving_scan_result_no_data);
                        return;
                    }
                    mPresenter.getCodeInfo(result, results);
                });
                inputDialog.show();
                break;
        }
    }

    //    @NeedsPermission(Manifest.permission.CAMERA)
    void needCamera() {
        if (results.size() > 0) {
            Map<String, Serializable> params = new HashMap<>(0);
            params.put("a", new EventCurrentResult(results.get(results.size() - 1).getResult(), results.size(), results));
            startFragment(params, new ScanByPhoneFragment());
        } else
            startFragment(new ScanByPhoneFragment(), true);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        ReceivingScanFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }

    @Override
    public void onGranted(int requestCode, @NonNull String[] permissions) {
        needCamera();
    }

    @Override
    public void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit) {
        if (!isProhibit) {
            showShortToast(R.string.permission_camera_denied);
        } else {
            showShortToast(R.string.permission_camera_never_ask_again);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanSuccess(List<EventQRCodeScanResult> result) {
        mPresenter.getResult(result);
    }

    @Override
    public void onResume() {
        super.onResume();
        setRightMenuMsg(HomePagePresenter.isNewMsg());
        if (!EventBus.getDefault().isRegistered(this)) //加上判断
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

}
