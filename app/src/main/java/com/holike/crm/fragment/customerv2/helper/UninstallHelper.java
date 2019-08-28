package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.DealerInfoBean;
import com.holike.crm.bean.internal.Installer;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.TextSpanHelper;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/7/29.
 * Copyright holike possess 2019.
 * 预约安装帮助类
 */
public class UninstallHelper {
    private BaseActivity<?, ?> mContext;
    private Callback mCallback;
    private WrapperRecyclerView mRecyclerView;
    private TextView mSelectDateTextView;
    private TextView mSelectTimeTextView;
    private EditText mAreaEditText, mRemarkEditText;
    private List<Installer> mList;
    private List<Installer> mSelectedInstallers; //从详情进来，已被选择的安装师傅集合
    private List<Installer> mDeleteInstallers;  //被删除的安装师傅id集合
    private InstallerAdapter mAdapter;
    private String mPersonalId,
            mHouseId, mPhone,
            mAddress, mName,
            mInstallDate,
            mInstallTime;
    private List<DealerInfoBean.UserBean> mInstallUsers; //经销商下所有安装工
    private int mCurrentClickIndex = -1; //当前点击的位置
    private boolean mIsTrigger; //是否是点击触发

    public UninstallHelper(BaseFragment<?, ?> fragment, Callback callback) {
        mContext = (BaseActivity<?, ?>) fragment.getActivity();
        mCallback = callback;
        initView(fragment.getContentView());
        Bundle bundle = fragment.getArguments();
        mList = new ArrayList<>();
        if (bundle == null) {
            mList.add(Installer.newAdded());
        } else {
            obtainBundleValue(bundle);
        }
        mAdapter = new InstallerAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.recyclerView);
        initRecyclerView();
        TextView tvSave = contentView.findViewById(R.id.tvSave);
        tvSave.setOnClickListener(view -> onSaved());
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_customer_uninstall, mRecyclerView, false);
        initHeader(headerView);
        mRecyclerView.addHeaderView(headerView);
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_customer_uninstall, mRecyclerView, false);
        initFooter(footerView);
        mRecyclerView.addFooterView(footerView);
    }

    private void initHeader(View view) {
        mSelectDateTextView = view.findViewById(R.id.tv_reserved_install_date);
        mSelectTimeTextView = view.findViewById(R.id.tv_reserved_time);
        mSelectDateTextView.setOnClickListener(v -> onSelectDate());
        mSelectTimeTextView.setOnClickListener(v -> onSelectTime());
    }

    /*选择预约安装日期*/
    private void onSelectDate() {
        PickerHelper.showTimePicker(mContext, (date, v) -> {
            mInstallDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
            mSelectDateTextView.setText(TimeUtil.timeMillsFormat(date));
        });
    }

    /*选择预约时间*/
    private void onSelectTime() {
        PickerHelper.showTimeHMPicker(mContext, (date, v) -> {
            mInstallTime = TimeUtil.timeMillsFormat(date, "HH:mm:ss");
            mSelectTimeTextView.setText(TimeUtil.timeMillsFormat(date, "HH:mm"));
        });
    }

    private void initFooter(View view) {
        mAreaEditText = view.findViewById(R.id.et_total_area);
        final TextView tvSquareMeter = view.findViewById(R.id.tv_square_meter);
        tvSquareMeter.setText(TextSpanHelper.getSquareMeter());
        mAreaEditText.setFilters(new InputFilter[]{new CashierInputFilter()});
        mAreaEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(cs.toString())) {
                    tvSquareMeter.setVisibility(View.VISIBLE);
                } else {
                    tvSquareMeter.setVisibility(View.GONE);
                }
            }
        });
        mRemarkEditText = view.findViewById(R.id.et_remark);
    }

    private void obtainBundleValue(Bundle bundle) {
        mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID);
        mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
        mName = bundle.getString("name");
        mPhone = bundle.getString("phone");
        mAddress = bundle.getString("address");
        mInstallDate = bundle.getString("installDate");
        String installDate = bundle.getString("installDate");
        mInstallDate = TimeUtil.timeMillsFormat(installDate, "yyyy-MM-dd");
        mSelectDateTextView.setText(TimeUtil.timeMillsFormat(installDate));
        mInstallTime = bundle.getString("installTime");
        mSelectTimeTextView.setText(TimeUtil.parse(mInstallTime, "HH:mm:ss", "HH:mm"));
        mAreaEditText.setText(bundle.getString("installSquare"));
        mRemarkEditText.setText(bundle.getString("remark"));
        ArrayList<Installer> installers = bundle.getParcelableArrayList("installers");
        if (installers != null && !installers.isEmpty()) {
            for (int i = 0; i < 20; i++) {  //最多20个安装师傅
                if (i < installers.size()) {
                    mList.add(installers.get(i));
                }
            }
            mSelectedInstallers = new ArrayList<>(mList);
        }
        if (mList.size() <= 0) {
            mList.add(Installer.newAdded());
        }
    }

    public void setInstaller(List<DealerInfoBean.UserBean> list) {
        if (list != null && !list.isEmpty()) {
            mInstallUsers = new ArrayList<>(list);
            if (mIsTrigger) {
                onSelectInstaller();
            }
        }
    }

    class InstallerAdapter extends CommonAdapter<Installer> {
        String mTipsInstaller;

        InstallerAdapter(Context context, List<Installer> mDatas) {
            super(context, mDatas);
            mTipsInstaller = mContext.getString(R.string.followup_installation_master2);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_customer_selectinstaller;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, Installer installer, int position) {
            holder.setText(R.id.tv_installer, mTipsInstaller + (position + 1));
            holder.setText(R.id.tv_select, installer.getInstallUserName());
            if (position < 19 && position == getItemCount() - 1) {
                holder.setVisibility(R.id.iv_add, View.VISIBLE);
            } else {
                holder.setVisibility(R.id.iv_add, View.INVISIBLE);
            }
            if (position == 0 && getItemCount() == 1) {
                holder.setVisibility(R.id.iv_remove, View.INVISIBLE);
            } else {
                holder.setVisibility(R.id.iv_remove, View.VISIBLE);
            }
            holder.setOnClickListener(R.id.tv_select, view -> {
                mCurrentClickIndex = position;
                onSelectInstaller();
            });
            holder.setOnClickListener(R.id.iv_add, view -> addNewTab());
            holder.setOnClickListener(R.id.iv_remove, view -> removeTab(position, installer));
        }
    }

    private void onSelectInstaller() {
        if (mCurrentClickIndex < 0 || mCurrentClickIndex > mList.size()) {
            return;
        }
        if (mInstallUsers == null) {
            mIsTrigger = true;
            mCallback.onQueryInstallers(true);
        } else {
            selectInstaller();
        }
    }

    private void selectInstaller() {
        List<String> optionItems = new ArrayList<>();
        for (DealerInfoBean.UserBean bean : mInstallUsers) {
            optionItems.add(bean.userName);
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (options1, options2, options3, v) -> {
            String id = mInstallUsers.get(options1).userId;
            String name = mInstallUsers.get(options1).userName;
            Installer installer = new Installer(id, name);
            if (mList.contains(installer) && mList.indexOf(installer) != mCurrentClickIndex) {  //选择安装师傅重复提示
                mCallback.onRequired(mContext.getString(R.string.followup_installation_master_select_error));
            } else {
                Installer oldInstaller = mList.get(mCurrentClickIndex);  //旧的安装工被替换 则添加进删除的集合中
                if (mSelectedInstallers != null && mSelectedInstallers.contains(oldInstaller)) {
                    addRemove(oldInstaller);
                }
                mList.set(mCurrentClickIndex, installer);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addNewTab() {
        if (mList.size() >= 20) {
            return;
        }
        mList.add(Installer.newAdded());
        mAdapter.notifyDataSetChanged();
    }

    private void removeTab(int position, Installer installer) {
        if (mSelectedInstallers != null && mSelectedInstallers.contains(installer)) {
            //从详情进来，已被选的安装师傅被删除时，往被删除的集合中添加元素
            addRemove(installer);
        }
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    private void addRemove(Installer installer) {
        if (mDeleteInstallers == null) {
            mDeleteInstallers = new ArrayList<>();
        }
        if (!mDeleteInstallers.contains(installer)) {
            mDeleteInstallers.add(installer);
        }
        mSelectedInstallers.remove(installer);
    }

    private void onSaved() {
        if (TextUtils.isEmpty(mInstallDate)) {
            mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_reserved_install_date2));
        } else {
            if (TextUtils.isEmpty(mInstallTime)) {
                mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_reservation_time_tips2));
            } else {
                if (!isAllSelected()) {
                    mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_installation_master2));
                } else {
                    String installSquare = mAreaEditText.getText().toString();
                    if (TextUtils.isEmpty(installSquare)) {
                        mCallback.onRequired(mAreaEditText.getHint());
                    } else {
                        String appointmentRemark = mRemarkEditText.getText().toString();
                        Param param = new Param();
                        String dealerId = SharedPreferencesUtils.getDealerId();
                        param.setInstallInfo(new Param.InstallInfo(dealerId, mPersonalId, mHouseId,
                                mAddress, mPhone, mName, mInstallDate, mInstallTime, installSquare, appointmentRemark));
                        if (mDeleteInstallers != null && !mDeleteInstallers.isEmpty()) {
                            List<String> deleteInstallUser = new ArrayList<>();
                            for (Installer installer : mDeleteInstallers) {
                                deleteInstallUser.add(installer.getId());
                            }
                            param.setDeleteInstallUser(deleteInstallUser);
                        }
                        List<Param.User> userInfoList = new ArrayList<>();
                        for (Installer installer : mList) {
                            if (mSelectedInstallers == null || !mSelectedInstallers.contains(installer)) {
                                userInfoList.add(new Param.User(dealerId, mHouseId, installer.getInstallUserId(), installer.getInstallUserName()));
                            }
                        }
                        param.setUserInfoList(userInfoList);
                        String body = MyJsonParser.fromBeanToJson(param);
                        mCallback.onSaved(body);
                    }
                }
            }
        }
    }

    private boolean isAllSelected() {  //选择安装师傅时，是否全部填写完毕
        for (Installer installer : mList) {
            if (TextUtils.isEmpty(installer.getInstallUserId())) {
                return false;
            }
        }
        return true;
    }

    static class Param {
        InstallInfo installInfo;
        List<User> userInfoList = new ArrayList<>();
        List<String> deleteInstallUser = new ArrayList<>();

        void setInstallInfo(InstallInfo installInfo) {
            this.installInfo = installInfo;
        }

        void setUserInfoList(List<User> userInfoList) {
            this.userInfoList = userInfoList;
        }

        void setDeleteInstallUser(List<String> deleteInstallUser) {
            this.deleteInstallUser = deleteInstallUser;
        }

        static class InstallInfo {
            String dealerId;
            String personalId;
            String houseId;
            String address;
            String phone;
            String name;
            String installDate;
            String installTime;
            String installSquare;
            String appointmentRemark;

            InstallInfo(String dealerId, String personalId, String houseId, String address,
                        String phone, String name, String installDate,
                        String installTime, String installSquare, String appointmentRemark) {
                this.dealerId = dealerId;
                this.personalId = personalId;
                this.houseId = houseId;
                this.address = address;
                this.phone = phone;
                this.name = name;
                this.installDate = installDate;
                this.installTime = installTime;
                this.installSquare = installSquare;
                this.appointmentRemark = appointmentRemark;
            }
        }

        static class User {
            String dealerId;
            String houseId;
            String installUserId;
            String installUserName;

            User(String dealerId, String houseId, String installUserId, String installUserName) {
                this.dealerId = dealerId;
                this.houseId = houseId;
                this.installUserId = installUserId;
                this.installUserName = installUserName;
            }
        }
    }

    public interface Callback {
        void onQueryInstallers(boolean showLoading);

        void onRequired(CharSequence text);

        void onSaved(String body);
    }
}
