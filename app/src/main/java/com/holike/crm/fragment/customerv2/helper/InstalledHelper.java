package com.holike.crm.fragment.customerv2.helper;


import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.SingleChoiceAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.bean.internal.Installer;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.FlexboxManagerHelper;
import com.holike.crm.helper.IImageSelectHelper;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.TextSpanHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/7/25.
 * Copyright holike possess 2019.
 */
public class InstalledHelper extends IImageSelectHelper implements View.OnClickListener {
    private TextView mInstallTimeTextView;
    private TextView mInstallMasterTextView;
    private EditText mInstallAreaEditText;
    private TextView mSquareMeterTextView;
    private RecyclerView mStatusRecyclerView;
    private EditText mRemarkEditText;
    private RecyclerView mPicturesRecyclerView;

    private Callback mCallback;
    private String mHouseId, //房屋id
            mInstallId, //安装id(必填)
            mActualInstallDate, // 实际安装时间(必填)
            mInstallUserId,  //安装用户id(必填)
            mInstallUserName, //安装工姓名(必填)
            mInstallState; //安装状态(必填)
    private SysCodeItemBean mSystemCode;
    private List<Installer> mInstallerList;

    public InstalledHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        mCallback = callback;
        initView(fragment.getContentView());
        setArea(mInstallAreaEditText, mSquareMeterTextView);
        setBundleValue(fragment.getArguments());
        setupSelectImages(mPicturesRecyclerView, R.string.tips_add_installed_image);
        mSystemCode = IntentValue.getInstance().getSystemCode();
        if (mSystemCode == null) {
            mCallback.onQuerySystemCode();
        } else {
            setInstallStatus();
        }
    }

    private void initView(View contentView) {
        mInstallTimeTextView = contentView.findViewById(R.id.tv_install_time);
        mInstallMasterTextView = contentView.findViewById(R.id.tv_install_master);
        mInstallAreaEditText = contentView.findViewById(R.id.et_install_area);
        mSquareMeterTextView = contentView.findViewById(R.id.tv_square_meter);
        mStatusRecyclerView = contentView.findViewById(R.id.rv_install_status);
        mRemarkEditText = contentView.findViewById(R.id.et_remark);
        mPicturesRecyclerView = contentView.findViewById(R.id.rv_pictures);
        TextView tvSave = contentView.findViewById(R.id.tvSave);
        mInstallAreaEditText.setFilters(new InputFilter[]{new CashierInputFilter()});
        mInstallTimeTextView.setOnClickListener(this);
        mInstallMasterTextView.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    private void setBundleValue(Bundle bundle) {
        if (bundle != null) {
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mInstallId = bundle.getString("installId");
            ArrayList<Installer> installers = bundle.getParcelableArrayList("installers");
            if (installers != null && !installers.isEmpty()) {
                mInstallerList = new ArrayList<>(installers);
            }
        }
    }

    private void setArea(EditText editText, final TextView tvSquareMeter) {
        editText.setFilters(new InputFilter[]{new CashierInputFilter()});
        tvSquareMeter.setText(TextSpanHelper.getSquareMeter());
        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(cs.toString())) {
                    tvSquareMeter.setVisibility(View.VISIBLE);
                } else {
                    tvSquareMeter.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setInstallStatus() {
        mStatusRecyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(mContext));
        List<DictionaryBean> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : mSystemCode.getInstallFeedbackState().entrySet()) {
            list.add(new DictionaryBean(entry.getKey(), entry.getValue()));
        }
        SingleChoiceAdapter adapter = new SingleChoiceAdapter(mContext, list);
        adapter.setSingleChoiceListener(bean -> mInstallState = bean.id);
        mStatusRecyclerView.setAdapter(adapter);
    }

    public void setSystemCode(SysCodeItemBean bean) {
        this.mSystemCode = bean;
        setInstallStatus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_install_time:
                onPickerTime();
                break;
            case R.id.tv_install_master:
                if (mInstallerList != null) {
                    onSelectInstaller();
                } else {
                    mCallback.onRequired(mContext.getString(R.string.followup_installation_uninstall_error));
                }
//                if (mInstallerList == null) {
//                    mCallback.onQueryInstaller();
//                } else {
//                    onSelectInstaller();
//                }
                break;
            case R.id.tvSave:
                onSaved();
                break;
        }
    }

    /*选择实际安装时间 不能选未来时间*/
    private void onPickerTime() {
        PickerHelper.showTimePicker(mContext, null, new Date(), date -> {
            if (date.after(new Date())) {
                mActualInstallDate = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd");
                mInstallTimeTextView.setText(TimeUtil.timeMillsFormat(new Date()));
            } else {
                mActualInstallDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                mInstallTimeTextView.setText(TimeUtil.timeMillsFormat(date));
            }
        });
    }

    /*选择安装师傅*/
    private void onSelectInstaller() {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (Installer installer : mInstallerList) {
            optionItems.add(new DictionaryBean(installer.getInstallUserId(), installer.getInstallUserName()));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (position,bean) -> {
            mInstallUserId = bean.id;
            mInstallUserName = bean.name;
            mInstallMasterTextView.setText(mInstallUserName);
        });
    }

    private void onSaved() {
        if (TextUtils.isEmpty(mActualInstallDate)) {
            mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.house_manage_install_really_time));
        } else {
            if (TextUtils.isEmpty(mInstallUserId) || TextUtils.isEmpty(mInstallUserName)) {
                mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_installation_master2));
            } else {
                String installArea = mInstallAreaEditText.getText().toString();
                if (TextUtils.isEmpty(installArea)) {
                    mCallback.onRequired(mInstallAreaEditText.getHint());
                } else {
                    if (TextUtils.isEmpty(mInstallState)) {
                        mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_install_status_tips2));
                    } else {
                        mCallback.onSaved(ParamHelper.Customer.finishInstall(mHouseId, mInstallId, mActualInstallDate,
                                mInstallUserId, mInstallUserName, installArea,
                                mInstallState, mRemarkEditText.getText().toString()), mImageHelper.getSelectedImages());
                    }
                }
            }
        }
    }

    public interface Callback {
        void onQuerySystemCode();

        void onQueryInstaller();

        void onRequired(CharSequence text);

        void onSaved(Map<String, Object> params, List<String> imagePaths);
    }
}
