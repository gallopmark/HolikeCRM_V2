package com.holike.crm.fragment.customerv2.helper;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.MultipleChoiceAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.helper.FlexboxManagerHelper;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by pony on 2019/8/12.
 * Copyright holike possess 2019.
 * 预约量尺帮助类
 */
public class UnmeasuredHelper extends GeneralHelper implements View.OnClickListener {

    private Activity mActivity;
    private Callback mCallback;
    private CurrentUserBean mCurrentUser;
    private String mDate, mTime; //mTime-接口需要传HH:mm:ss格式
    private List<DictionaryBean> mSelectedItems;
    private MultipleChoiceAdapter mMeasureSpaceAdapter;
    private String mAppointShopId, mAppointMeasureBy;
    private int mShopIndex = 0, mRulerIndex = 0;
    private TextView mReservationDateTextView, mReservationTimeTextView,
            mMeasureShopTextView, mMeasureByTextView;
    private EditText mRemarkEditText;

    public UnmeasuredHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        mActivity = fragment.getActivity();
        mCallback = callback;
        mCurrentUser = IntentValue.getInstance().getCurrentUser();
        initView(fragment.getContentView());
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            setDataFromBundle(bundle);
        }
    }

    private void initView(View contentView) {
        mReservationDateTextView = contentView.findViewById(R.id.tv_reservation_date);
        mReservationTimeTextView = contentView.findViewById(R.id.tv_reservation_time);
        mMeasureShopTextView = contentView.findViewById(R.id.tv_measure_store);
        mMeasureByTextView = contentView.findViewById(R.id.tv_measure_ruler);
        mRemarkEditText = contentView.findViewById(R.id.et_remark);
        TextView tvSave = contentView.findViewById(R.id.tvSave);
        mReservationDateTextView.setOnClickListener(this);
        mReservationTimeTextView.setOnClickListener(this);
        mMeasureShopTextView.setOnClickListener(this);
        mMeasureByTextView.setOnClickListener(this);
        tvSave.setOnClickListener(view -> onSave());
    }

    private void setDataFromBundle(Bundle bundle) {
        String appointmentTime = bundle.getString("appointmentTime");
        mDate = TimeUtil.timeMillsFormat(appointmentTime, "yyyy-MM-dd");
        mReservationDateTextView.setText(mDate);
        mTime = TimeUtil.timeMillsFormat(appointmentTime, "HH:mm:ss");
        mReservationTimeTextView.setText(TimeUtil.timeMillsFormat(appointmentTime, "HH:mm"));
        mAppointShopId = bundle.getString("appointShopId");
        mMeasureShopTextView.setText(bundle.getString("appointShopName"));
        mAppointMeasureBy = bundle.getString("appointMeasureBy");
        mMeasureByTextView.setText(bundle.getString("appointMeasureByName"));
        mRemarkEditText.setText(bundle.getString("remark"));
        mSelectedItems = new ArrayList<>(getSelectedMeasureSpaceCode(bundle.getString("appointMeasureSpace")));
    }

    private List<String> getAppointMeasureSpaceCode(String appointMeasureSpace) {
        if (TextUtils.isEmpty(appointMeasureSpace)) return new ArrayList<>();
        try {
            String[] array = appointMeasureSpace.split(",");
            return Arrays.asList(array);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /*已选的量尺空间*/
    private List<DictionaryBean> getSelectedMeasureSpaceCode(String appointMeasureSpace) {
        List<String> list = getAppointMeasureSpaceCode(appointMeasureSpace);
        if (list.isEmpty()) return new ArrayList<>();
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) return new ArrayList<>();
        List<DictionaryBean> beanList = new ArrayList<>();
        Map<String, String> map = bean.getCustomerMeasureSpace();
        for (String code : list) {
            if (map.containsKey(code)) {
                beanList.add(new DictionaryBean(code, map.get(code)));
            }
        }
        return beanList;
    }

    public void setMeasureSpace(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(mContext));
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean != null) {
            List<DictionaryBean> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : bean.getCustomerMeasureSpace().entrySet()) {
                list.add(new DictionaryBean(entry.getKey(), entry.getValue()));
            }
            mMeasureSpaceAdapter = new MultipleChoiceAdapter(mContext, list, mSelectedItems);
            recyclerView.setAdapter(mMeasureSpaceAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput(mActivity);
        switch (view.getId()) {
            case R.id.tv_reservation_date:
                onSelectDate();
                break;
            case R.id.tv_reservation_time:
                onSelectTime();
                break;
            case R.id.tv_measure_store:
                if (mCurrentUser == null) {
                    mCallback.onQueryUserInfo();
                } else {
                    onSelectShop();
                }
                break;
            case R.id.tv_measure_ruler:
                if (TextUtils.isEmpty(mAppointShopId)) {
                    showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_measure_store_tips2));
                } else {
                    mCallback.onQueryMeasurer(mAppointShopId);
                }
                break;
            case R.id.tvSave:
                onSave();
                break;
        }
    }

    private void onSelectDate() {
        PickerHelper.showTimePicker(mContext, date -> {
            mDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
            mReservationDateTextView.setText(TimeUtil.timeMillsFormat(date));
        });
    }

    private void onSelectTime() {
        PickerHelper.showTimeHMPicker(mContext, date -> {
            mTime = TimeUtil.timeMillsFormat(date, "HH:mm:ss");
            mReservationTimeTextView.setText(TimeUtil.timeMillsFormat(date, "HH:mm"));
        });
    }

    public void setUserInfo(CurrentUserBean bean) {
        this.mCurrentUser = bean;
        onSelectShop();
    }

    private void onSelectShop() {
        List<DictionaryBean> optionItems = new ArrayList<>();
        final List<CurrentUserBean.ShopInfo> shopInfoList = mCurrentUser.getShopInfo();
        for (CurrentUserBean.ShopInfo bean : shopInfoList) {
            optionItems.add(new DictionaryBean(bean.shopId, bean.shopName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, mShopIndex, ((position, bean) -> {
            mShopIndex = position;
            mAppointShopId = bean.id;
            mMeasureShopTextView.setText(bean.name);
            mAppointMeasureBy = null;
            mMeasureByTextView.setText(null);
        }));
    }

    /*选择设计师*/
    public void onSelectRuler(final List<ShopRoleUserBean.UserBean> list) {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : list) {
            optionItems.add(new DictionaryBean(bean.userId, bean.userName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, mRulerIndex, (position, bean) -> {
            mRulerIndex = position;
            mAppointMeasureBy = bean.id;
            mMeasureByTextView.setText(bean.name);
        });
    }

    private void onSave() {
        if (TextUtils.isEmpty(mDate)) {
            showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_reservation_date_tips2));
        } else {
            if (TextUtils.isEmpty(mTime)) {
                showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_reservation_time_tips2));
            } else {
                if (mMeasureSpaceAdapter == null || mMeasureSpaceAdapter.getSelectedItems().isEmpty()) {
                    showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_measure_space_tips2));
                } else {
                    String appointTime = mDate + " " + mTime;
                    List<DictionaryBean> list = mMeasureSpaceAdapter.getSelectedItems();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        sb.append(list.get(i).id);
                        if (i < list.size() - 1) {
                            sb.append(",");
                        }
                    }
                    String remark = mRemarkEditText.getText().toString();
                    mCallback.onSave(ParamHelper.Customer.appointMeasure(mHouseId, appointTime, sb.toString(), mAppointShopId, mAppointMeasureBy, remark));
                }
            }
        }
    }

    public interface Callback {
        void onQueryUserInfo();

        void onQueryMeasurer(String shopId);

        void onSave(String body);
    }
}
