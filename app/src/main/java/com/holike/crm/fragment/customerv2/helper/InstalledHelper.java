package com.holike.crm.fragment.customerv2.helper;


import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.holike.crm.R;
import com.holike.crm.adapter.SingleChoiceAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.DealerInfoBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.TextSpanHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.manager.FlowLayoutManager;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gallop on 2019/7/25.
 * Copyright holike possess 2019.
 */
public class InstalledHelper extends IBaseHelper implements View.OnClickListener {
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
    private List<DealerInfoBean.UserBean> mInstallerList;

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
            String actualInstallDate = bundle.getString("actualInstallDate"); //long
            mActualInstallDate = TimeUtil.timeMillsFormat(actualInstallDate, "yyyy-MM-dd");
            mInstallTimeTextView.setText(TimeUtil.timeMillsFormat(actualInstallDate));
            mInstallUserId = bundle.getString("installUserId");
            mInstallUserName = bundle.getString("installUserName");
            mInstallMasterTextView.setText(mInstallUserName);
            mInstallAreaEditText.setText(bundle.getString("installArea"));
            mInstallState = bundle.getString("installState");
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
        mStatusRecyclerView.setLayoutManager(new FlowLayoutManager());
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
                if (mInstallerList == null) {
                    mCallback.onQueryInstaller();
                } else {
                    onSelectInstaller();
                }
                break;
            case R.id.tvSave:
                onSaved();
                break;
        }
    }

    /*选择实际安装时间*/
    private void onPickerTime() {
        PickerHelper.showTimePicker(mContext, (date, v) -> { //不能选未来时间
            if (date.after(new Date())) { //选择了未来时间
                mActualInstallDate = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd");
                mInstallTimeTextView.setText(TimeUtil.timeMillsFormat(new Date()));
            } else {
                mActualInstallDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                mInstallTimeTextView.setText(TimeUtil.timeMillsFormat(date));
            }
        });
    }

    public void setInstaller(List<DealerInfoBean.UserBean> list) {
        mInstallerList = new ArrayList<>(list);
        onSelectInstaller();
    }

    /*选择安装师傅*/
    private void onSelectInstaller() {
        List<String> optionItems = new ArrayList<>();
        for (DealerInfoBean.UserBean userBean : mInstallerList) {
            optionItems.add(userBean.userName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            mInstallUserId = mInstallerList.get(options1).userId;
            mInstallUserName = mInstallerList.get(options1).userName;
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
                                mInstallState, mRemarkEditText.getText().toString()));
                    }
                }
            }
        }
    }

    public interface Callback {
        void onQuerySystemCode();

        void onQueryInstaller();

        void onRequired(CharSequence text);

        void onSaved(String body);
    }
}
