package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerImagePreviewActivity;
import com.holike.crm.activity.customer.CustomerMultiTypeActivity;
import com.holike.crm.activity.customer.CustomerOnlineLogActivity;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.adapter.SquareImageGridAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.dialog.ReceivingCustomerDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.ICallPhoneHelper;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/7/16.
 * Copyright holike possess 2019.
 * 客户管理页面帮助类
 */
public class CustomerManagerHelper extends IHouseHelper {

    private boolean mHighSeasFlag; //是否是公海标识，从bundle带过来，如果为true，则调公海客户详情接口
    private String mPersonalId = "";
    private String mSelectedHouseId;  //选中的房屋（进来默认选中那个房屋）
    private WrapperRecyclerView mRecyclerView;
    private View mHeaderView,
            mFooterView,
            mHouseInfoView;
    private TextView mOnlineLogTextView;  //线上引流客户（线上记录）
    private FrameLayout mReceiveLayout; //如果是公海客户 底部显示“领取客户按钮”
    private List<CustomerManagerV2Bean.HouseInfoBean> mHouseInfoList;
    private CustomerManagerAdapter mAdapter;
    private boolean mFirstLoadCompleted;  //是否是第一次加载成功
    private int mSelectPosition; //当前选择的房屋位置
    private boolean mIsHighSeaHouse = false; //当前房屋是否是公海房屋

    private CustomerManagerCallback mCallback;

    public CustomerManagerHelper(BaseFragment<?, ?> fragment, CustomerManagerCallback callback) {
        super(fragment);
        this.mCallback = callback;
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            mHighSeasFlag = bundle.getBoolean(CustomerValue.HIGH_SEAS_HOUSE_FLAG, false);
            mSelectedHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID, "");
        }
        View contentView = fragment.getContentView();
        mRecyclerView = contentView.findViewById(R.id.recyclerView);
        mOnlineLogTextView = contentView.findViewById(R.id.tv_online_logs);
        mReceiveLayout = contentView.findViewById(R.id.fl_receive);
        setup();
        doRequest();
    }

    private void setup() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mHeaderView = mLayoutInflater.inflate(R.layout.header_customer_manager, new LinearLayout(mContext), false);
        mRecyclerView.addHeaderView(mHeaderView);
        mAdapter = new CustomerManagerAdapter(mContext, mMultipleItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*领取客户成功，则调用客户详情接口，将公海标识设为false*/
    public void onReceiveHouseSuccess() {
        mHighSeasFlag = false;
        doRequest();
    }

    public void doRequest() {
        mCallback.onGetCustomerDetail(mPersonalId, mHighSeasFlag);
    }

    public void setActivityPoliceEnabled(boolean isEmpty) {
        if (isEmpty) {
            mHeaderView.findViewById(R.id.ll_activity_police_layout).setVisibility(View.GONE);
        } else {
            mHeaderView.findViewById(R.id.ll_activity_police_layout).setVisibility(View.VISIBLE);
        }
    }

    public void onHttpResponse(CustomerManagerV2Bean bean) {
        this.mManagerV2Bean = bean;
        this.mCustomerInfoBean = mManagerV2Bean.getPersonalInfo();
        mHouseInfoList = new ArrayList<>(mManagerV2Bean.getHouseInfoList());
        if (!mFirstLoadCompleted) {
            for (int i = 0; i < mHouseInfoList.size(); i++) {
                CustomerManagerV2Bean.HouseDetailBean detailBean = mHouseInfoList.get(i).getHouseDetail();
                if (detailBean != null && TextUtils.equals(detailBean.houseId, mSelectedHouseId)) {
                    mSelectPosition = i;
                    break;
                }
            }
            mFirstLoadCompleted = true;
        }
        validOnlineLog();
        updateHeadView();
        updateHouseInfo(mSelectPosition);
    }

    private String getPersonalId() {
        if (this.mCustomerInfoBean == null) return "";
        return this.mCustomerInfoBean.personalId;
    }

    private void validOnlineLog() {
        if (mCustomerInfoBean != null && TextUtils.equals(mCustomerInfoBean.source, "09")) { //线上引流客户
            mOnlineLogTextView.setVisibility(View.VISIBLE);
            mOnlineLogTextView.setOnClickListener(view -> CustomerOnlineLogActivity.open(mContext, mCustomerInfoBean.personalId));
        } else {
            mOnlineLogTextView.setVisibility(View.GONE);
        }
    }

    private void updateHeadView() {
        if (mCustomerInfoBean != null) {
            TextView tvName = mHeaderView.findViewById(R.id.tv_name);
            TextView tvIntention = mHeaderView.findViewById(R.id.tv_intention);
            TextView tvSource = mHeaderView.findViewById(R.id.tv_source);
            TextView tvPhone = mHeaderView.findViewById(R.id.tv_phone);
            TextView tvWechat = mHeaderView.findViewById(R.id.tv_wechat);
            TextView tvCreateTime = mHeaderView.findViewById(R.id.tv_create_time);
            TextView tvGeneration = mHeaderView.findViewById(R.id.tv_generation);
            TextView tvNextDate = mHeaderView.findViewById(R.id.tv_next_date);
            TextView tvPolicy = mHeaderView.findViewById(R.id.tv_policy);
            tvName.setText(transform(0, mCustomerInfoBean.userName, mHeaderView.findViewById(R.id.tv_name_tips))); //姓名
            Drawable drawableLeft = null;
            if (TextUtils.equals(mCustomerInfoBean.gender, "2")) { //先生
                drawableLeft = ContextCompat.getDrawable(mContext, R.drawable.ic_sex_man);
            } else if (TextUtils.equals(mCustomerInfoBean.gender, "1")) { //女士
                drawableLeft = ContextCompat.getDrawable(mContext, R.drawable.ic_sex_lady);
            }
            if (drawableLeft != null) {
                tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            }
            tvIntention.setText(transform(R.string.tips_customer_intent2, mCustomerInfoBean.getIntentionLevel()));  //意向评级
            tvSource.setText(transform(R.string.tips_customer_source2, mCustomerInfoBean.getSource()));//客户来源
            if (!TextUtils.isEmpty(mCustomerInfoBean.phoneNumber)) { //手机号不为空，则显示手机
                tvPhone.setText(mTextHelper.obtainColorBoldStyle(R.string.customer_phone_tips, mCustomerInfoBean.phoneNumber, R.color.colorAccent));
//                tvPhoneWx.setText(transform(R.string.customer_phone_tips, mCustomerInfoBean.phoneNumber));
                tvPhone.setOnClickListener(view -> onCallPhone());
            } else {
                tvPhone.setText(transform(R.string.customer_phone_tips, ""));
            }
            tvWechat.setText(transform(R.string.customer_wechat_tips, mCustomerInfoBean.wxNumber));
            tvCreateTime.setText(transform(R.string.create_time_tips, mCustomerInfoBean.getCreateDate())); //创建时间
            tvGeneration.setText(transform(R.string.tips_customer_age2, mCustomerInfoBean.getAgeType())); //年龄段
            tvNextDate.setText(transform(R.string.followup_nextDate_tips, mCustomerInfoBean.getNextFollowTime())); //下次跟进日期
            tvPolicy.setText(transform(0, mCustomerInfoBean.activityPolicy, mHeaderView.findViewById(R.id.tv_policy_tips))); //活动优惠政策
        }
        RecyclerView recyclerView = mHeaderView.findViewById(R.id.rv_house_list); //房屋列表
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        final HouseListAdapter adapter = new HouseListAdapter(mContext, mHouseInfoList, mSelectPosition);
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(mSelectPosition);
        adapter.setOnItemClickListener((apt, holder, view, position) -> {
            adapter.setSelectPosition(position);
            updateHouseInfo(position);
        });
    }

    /*拨打电话*/
    private void onCallPhone() {
        ICallPhoneHelper.with(mContext).requestCallPhone(mCustomerInfoBean.personalId, mCurrentHouseDetailBean.houseId, mCustomerInfoBean.phoneNumber);
    }

    /*修改客户信息传值*/
    private Bundle alterCustomer() {
        Bundle bundle = new Bundle();
        bundle.putString(CustomerValue.PERSONAL_ID, mCustomerInfoBean.personalId);
        bundle.putString("recordStatus", mCustomerInfoBean.recordStatus);
        bundle.putString("versionNumber", mCustomerInfoBean.versionNumber);
        bundle.putString("userName", mCustomerInfoBean.userName);
        bundle.putBoolean("isValidCustomer", mCustomerInfoBean.isValidCustomer());
        bundle.putString("phoneNumber", mCustomerInfoBean.phoneNumber); //手机号
        bundle.putString("wxNumber", mCustomerInfoBean.wxNumber); //微信号
        bundle.putString("gender", mCustomerInfoBean.gender); //性别
        bundle.putString("genderName", mCustomerInfoBean.getGender()); //性别名称
        bundle.putString("ageType", mCustomerInfoBean.ageType);   //传年龄段
        bundle.putString("ageTypeName", mCustomerInfoBean.getAgeType());  //传年龄段字段值
        bundle.putString("source", mCustomerInfoBean.source); //来源code
        bundle.putString("sourceName", mCustomerInfoBean.getSource()); //来源名称
        bundle.putString("intentLevel", mCustomerInfoBean.intentionLevel); //意向评级
        bundle.putString("intentLevelName", mCustomerInfoBean.getIntentionLevel()); //意向评级名称
        bundle.putString("nextFollowTime", mCustomerInfoBean.nextFollowTime); //下次跟进
        bundle.putString("activityPolicy", mCustomerInfoBean.activityPolicy); //活动政策
        return bundle;
    }

    /*新建房屋或者编辑房屋传值*/
    private Bundle obtainBundle(CustomerManagerV2Bean.HouseDetailBean detailBean) {
        Bundle bundle = new Bundle();
        bundle.putString(CustomerValue.PERSONAL_ID, getPersonalId());
        if (detailBean != null) {
            bundle.putBoolean("isEdit", true);
            bundle.putString("houseId", detailBean.houseId);
            bundle.putString("recordStatus", detailBean.recordStatus);
            bundle.putString("versionNumber", detailBean.versionNumber);
            bundle.putString("provinceCode", detailBean.provinceCode);
            bundle.putString("cityCode", detailBean.cityCode);
            bundle.putString("districtCode", detailBean.districtCode);
            bundle.putString("provinceName", detailBean.provinceName);
            bundle.putString("cityName", detailBean.cityName);
            bundle.putString("districtName", detailBean.districtName);
            bundle.putString("shopId", detailBean.shopId);
            bundle.putString("shopName", detailBean.shopName);
            bundle.putString("groupId", detailBean.groupId);
            bundle.putString("groupName", detailBean.groupName);
            bundle.putString("budgetTypeCode", detailBean.budgetTypeCode);
            bundle.putString("address", detailBean.address);
            bundle.putString("spareContact", detailBean.spareContact);
            bundle.putString("spareContactPhone", detailBean.spareContactPhone);
            bundle.putString("remark", detailBean.remark);
        } else {
            bundle.putBoolean("isEdit", false);
        }
        return bundle;
    }

    /*更新房屋信息*/
    private void updateHouseInfo(int currentHouse) {
        onResetStatus();
        if (mHouseInfoList.isEmpty() || currentHouse < 0 || currentHouse >= mHouseInfoList.size()) {
            return;
        }
        this.mSelectPosition = currentHouse;
        mCurrentHouseInfoBean = mHouseInfoList.get(currentHouse);
        mCurrentHistoryList = new ArrayList<>(mCurrentHouseInfoBean.getHistoryList());
        mCurrentOperateList = new ArrayList<>(mCurrentHouseInfoBean.getMobileIcon());
        mCurrentHouseDetailBean = mCurrentHouseInfoBean.getHouseDetail();
        mMultipleItems.clear();
        if (mCurrentHouseDetailBean != null) {
            mIsHighSeaHouse = mCurrentHouseDetailBean.isHighSeasHouse();
            if (mCurrentHouseDetailBean.isExistHighSeasHistory() && !mHighSeasFlag) {  //存在公海历史记录
                mFragment.setRightMenu(R.string.history_record, view -> mCallback.onHighSeasHistory(getPersonalId(), mCurrentHouseDetailBean.houseId));
            }
            updateEditButton();
            updateSingleHouseInfo();
            updateMultiHouseInfo();
            if (mIsHighSeaHouse) {
                mReceiveLayout.setVisibility(View.VISIBLE);
                /*显示领取客户按钮*/
                mReceiveLayout.findViewById(R.id.tv_receive).setOnClickListener(view -> receivingCustomer());
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /*恢复到初始状态*/
    private void onResetStatus() {
        mHeaderView.findViewById(R.id.cl_house_info).setVisibility(View.GONE);
        mHeaderView.findViewById(R.id.tv_edit_info).setVisibility(View.GONE); //编辑客户按钮
        mHeaderView.findViewById(R.id.tv_edit_house).setVisibility(View.GONE); //编辑房屋按钮
        mHeaderView.findViewById(R.id.tv_add_house).setVisibility(View.GONE); //添加房屋按钮
        mIsHighSeaHouse = false;
        mFragment.hideRightMenu();
        mReceiveLayout.setVisibility(View.GONE);
    }

    /*公海客户点击领取客户*/
    private void receivingCustomer() {
        ReceivingCustomerDialog dialog = new ReceivingCustomerDialog(mContext);
        dialog.setOnSelectedListener((shopId, groupId) -> mCallback.doReceive(mCurrentHouseDetailBean.houseId, shopId, groupId, SharedPreferencesUtils.getUserId()));
        dialog.show();
    }

    /*判断是否是公海房屋，如果是则不能编辑客户信息*/
    private void updateEditButton() {
        TextView tvEditInfo = mHeaderView.findViewById(R.id.tv_edit_info); //编辑客户信息
        TextView tvAddHouse = mHeaderView.findViewById(R.id.tv_add_house); //添加房屋
        TextView tvEditHouse = mHeaderView.findViewById(R.id.tv_edit_house); //编辑房屋信息
        if (mIsHighSeaHouse) { //公海客户隐藏 编辑客户信息按钮、添加房屋按钮、编辑房屋信息按钮
            tvEditInfo.setVisibility(View.GONE);
            tvAddHouse.setVisibility(View.GONE);
            tvEditHouse.setVisibility(View.GONE);
        } else {
            tvEditInfo.setVisibility(View.VISIBLE);
            tvAddHouse.setVisibility(View.VISIBLE);
            tvEditHouse.setVisibility(View.VISIBLE);
            tvEditInfo.setOnClickListener((v -> mCallback.onEditInfo(alterCustomer())));
            tvAddHouse.setOnClickListener(view -> mCallback.onEditHouse(obtainBundle(null)));
            tvEditHouse.setOnClickListener(view -> mCallback.onEditHouse(obtainBundle(mCurrentHouseDetailBean)));
        }
    }

    /*更新房屋信息*/
    private void updateSingleHouseInfo() {
        mHeaderView.findViewById(R.id.cl_house_info).setVisibility(View.VISIBLE);
        mHeaderView.findViewById(R.id.cl_house_info).setBackgroundResource(R.color.color_while);
        if (mHouseInfoView == null) {
            ViewStub viewStub = mHeaderView.findViewById(R.id.vs_info);
            viewStub.setLayoutResource(R.layout.include_customer_manager_info_common);
            mHouseInfoView = viewStub.inflate();
        }
        TextView tvAddress = mHouseInfoView.findViewById(R.id.tv_address);
        TextView tvStatus = mHouseInfoView.findViewById(R.id.tv_status);
        TextView tvBudget = mHouseInfoView.findViewById(R.id.tv_budget);
        TextView tvContact = mHouseInfoView.findViewById(R.id.tv_spare_contact);
        TextView tvPhone = mHouseInfoView.findViewById(R.id.tv_spare_phone);
        TextView tvRemarkTips = mHouseInfoView.findViewById(R.id.tv_remark_tips);
        TextView tvRemark = mHouseInfoView.findViewById(R.id.tv_remark);
        tvAddress.setText(transform(R.string.customer_address_tips, mCurrentHouseDetailBean.address));    //房屋地址
        if (mIsHighSeaHouse) { //如果是公海房屋
            tvStatus.setVisibility(View.GONE);
        } else {
            tvStatus.setText(mTextHelper.obtainColorBoldStyle(R.string.current_state_tips, mCurrentHouseDetailBean.getCurrentStatus(), R.color.textColor15));  //当前状态
            tvStatus.setVisibility(View.VISIBLE);
        }
        tvBudget.setText(transform(R.string.tips_customer_custom_budget, mCurrentHouseDetailBean.getBudget()));    //定制预算
        tvContact.setText(transform(R.string.tips_customer_standby_contact2, mCurrentHouseDetailBean.spareContact)); //备用联系人
        final String sparePhone = mCurrentHouseDetailBean.spareContactPhone;
        if (TextUtils.isEmpty(sparePhone)) {
            tvPhone.setText(transform(R.string.tips_customer_standby_phone2, ""));
            tvPhone.setEnabled(false);
        } else {
            tvPhone.setEnabled(true);
            tvPhone.setText(mTextHelper.obtainColorBoldStyle(R.string.tips_customer_standby_phone2, sparePhone, R.color.colorAccent)); //备用电话
        }
        tvPhone.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(sparePhone))
                mFragment.callPhone(sparePhone);
        });
        tvRemark.setText(transform(0, mCurrentHouseDetailBean.remark, tvRemarkTips));
    }

    /*更新房屋信息 recyclerView实现复用*/
    private void updateMultiHouseInfo() {
        if (mIsHighSeaHouse) {  //公海房屋
            mMultipleItems.add(new MultiHouseItem(MultiHouseItem.TYPE_TITLE_TIPS)); //跟进记录
            if (getGuideHistory() != null) {
                wrapGuideItem(); //分配导购
            }
            wrapPhoneRecordItem();//通话记录
            if (getUnmeasuredHistory() != null) {
                wrapUnmeasured();//预约量尺
            }
            if (getDesignerHistory() != null) {
                wrapDesigner(); //分配设计师
            }
            if (getMeasuredHistory() != null) {
                mMultipleItems.add(new MeasureResultItem(mCurrentHouseInfoBean.getMeasureImgList())); //量尺结果
            }
            if (getUploadPlanHistory() != null) {
                mMultipleItems.add(new UploadPlanItem(mCurrentHouseInfoBean.getDesignRenderImgList())); //上传方案
            }
            if (getRoundsHistory() != null) {
                wrapRounds();//主管查房
            }
            if (getReceiptHistory() != null) {
                wrapReceiptItem();
            }
            if (getContractHistory() != null) {
                mMultipleItems.add(new ContractItem(mCurrentHouseInfoBean.getContractImgList())); //合同登记
            }
            if (getOrderHistory() != null) {
                wrapOrderPlaced();//生成订单
            }
            if (getUninstallHistory() != null) {
                mMultipleItems.add(new UninstallItem());  //预约安装
            }
            if (getInstallDrawingHistory() != null) { //上传安装图纸记录
                mMultipleItems.add(new InstallDrawingItem(mCurrentHouseInfoBean.getInstallImgList())); //上传安装图纸
            }
            if (getInstalledHistory() != null) { //安装完成记录
                wrapInstalledItem(); //安装完成
            }
            if (getMessageBoardHistory() != null) {  //留言板记录
                wrapMessageLogsItem(); //留言记录
            }
            if (mMultipleItems.size() < 2) {  //如果没有任何记录，则清空数据 因为添加了 “跟进记录”
                mMultipleItems.clear();
                mHeaderView.findViewById(R.id.cl_house_info).setBackgroundResource(R.drawable.bg_corners_bottom_white_5dp);
            }
            removeFooterView();
        } else {  //跟进中的房屋 才显示操作菜单
            if (!mCurrentOperateList.isEmpty()) { //操作菜单
                mMultipleItems.add(new OperateItem(mCurrentOperateList));
            }
            mMultipleItems.add(new MultiHouseItem(MultiHouseItem.TYPE_TITLE_TIPS)); //跟进记录
            wrapGuideItem(); //分配导购
            wrapPhoneRecordItem();//通话记录
            wrapUnmeasured();//预约量尺
            wrapDesigner(); //分配设计师
//        wrapMeasuredResult(new Mea);//量尺结果
            mMultipleItems.add(new MeasureResultItem(mCurrentHouseInfoBean.getMeasureImgList())); //量尺结果
            mMultipleItems.add(new UploadPlanItem(mCurrentHouseInfoBean.getDesignRenderImgList())); //上传方案
            wrapRounds();//主管查房
            wrapReceiptItem(); //收款
            mMultipleItems.add(new ContractItem(mCurrentHouseInfoBean.getContractImgList())); //合同登记
            wrapOrderPlaced();//生成订单
//        wrapUninstall(); //预约安装
            mMultipleItems.add(new UninstallItem());  //预约安装
            mMultipleItems.add(new InstallDrawingItem(mCurrentHouseInfoBean.getInstallImgList())); //上传安装图纸
            wrapInstalledItem(); //安装完成
            wrapMessageLogsItem(); //留言记录
            addFooterView();
        }
    }

    /*分配导购item*/
    private void wrapGuideItem() {
        MultiHouseItem item = new MultiHouseItem(MultiHouseItem.TYPE_GUIDE);
        item.setHistory(getGuideHistory());
        mMultipleItems.add(item);
    }

    /*通话记录*/
    private void wrapPhoneRecordItem() {
        List<CustomerManagerV2Bean.PhoneRecordBean> list = mCurrentHouseInfoBean.getPhoneRecord();
        if (list.isEmpty()) {
            if (!mIsHighSeaHouse) {  //跟进中的房屋才展示
                mMultipleItems.add(new PhoneRecordItem(new CustomerManagerV2Bean.PhoneRecordBean(), false));
            }
        } else {
            mMultipleItems.add(new PhoneRecordItem(list.get(0), list.size() > 1));
        }
    }

    /*预约量尺*/
    private void wrapUnmeasured() {
        MultiHouseItem item = new MultiHouseItem(MultiHouseItem.TYPE_UNMEASURED);
        item.setHistory(getUnmeasuredHistory());
        mMultipleItems.add(item);
    }

    /*分配设计师*/
    private void wrapDesigner() {
        MultiHouseItem item = new MultiHouseItem(MultiHouseItem.TYPE_DESIGNER);
        item.setHistory(getDesignerHistory());
        mMultipleItems.add(item);
    }

    /*主管查房*/
    private void wrapRounds() {
        MultiHouseItem item = new MultiHouseItem(MultiHouseItem.TYPE_ROUNDS);
        item.setHistory(getRoundsHistory());
        mMultipleItems.add(item);
    }

    /*收款*/
    private void wrapReceiptItem() {
        mMultipleItems.add(new MultiHouseItem(MultiHouseItem.TYPE_RECEIPT_TITLE));
        List<CustomerManagerV2Bean.PaymentBean> list = mCurrentHouseInfoBean.getPaymentList();
        if (list.isEmpty()) {
            mMultipleItems.add(new ReceiptItem(new CustomerManagerV2Bean.PaymentBean(), true));
        } else {
            for (int i = 0; i < list.size(); i++) {
                boolean isLastPosition = i == list.size() - 1;
                mMultipleItems.add(new ReceiptItem(list.get(i), isLastPosition));
            }
        }
    }

    /*生成订单*/
    private void wrapOrderPlaced() {
        mMultipleItems.add(new MultiHouseItem(MultiHouseItem.TYPE_ORDER_TITLE));
        List<CustomerManagerV2Bean.GenerateOrderBean> list = mCurrentHouseInfoBean.getOrderList();
        if (list.isEmpty()) {
            mMultipleItems.add(new OrderPlacedItem(new CustomerManagerV2Bean.GenerateOrderBean(), true));
        } else {
            for (int i = 0; i < list.size(); i++) {
                boolean isLastPosition = i == list.size() - 1;
                mMultipleItems.add(new OrderPlacedItem(list.get(i), isLastPosition));
            }
        }
    }

    /*安装完成*/
    private void wrapInstalledItem() {
        mMultipleItems.add(new MultiHouseItem(MultiHouseItem.TYPE_INSTALLED_TITLE));
        List<CustomerManagerV2Bean.InstalledInfoBean> list = mCurrentHouseInfoBean.getFinishInstallInfo();
        if (list.isEmpty()) {
            mMultipleItems.add(new InstalledItem(new CustomerManagerV2Bean.InstalledInfoBean(), true));
        } else {
            for (int i = 0; i < list.size(); i++) {
                boolean isLastPosition = i == list.size() - 1;
                mMultipleItems.add(new InstalledItem(list.get(i), isLastPosition));
            }
        }
    }

    /*留言记录*/
    private void wrapMessageLogsItem() {
        mMultipleItems.add(new MultiHouseItem(MultiHouseItem.TYPE_MESSAGE_LOGS_TITLE));
        List<CustomerManagerV2Bean.MessageBoardBean> messages = mCurrentHouseInfoBean.getMessageBoard();
        if (messages.isEmpty()) {
            mMultipleItems.add(new MessageLogsItem(true));
        } else {
            for (CustomerManagerV2Bean.MessageBoardBean bean : messages) {
                mMultipleItems.add(new MessageLogsItem(bean));
            }
        }
    }

    /*留言板底部布局 非公海房屋才能发布留言*/
    private void addFooterView() {
        if (mFooterView == null) {
            mFooterView = mLayoutInflater.inflate(R.layout.footer_customer_manager, new LinearLayout(mContext), false);
            initFooterView();
            mRecyclerView.addFooterView(mFooterView);
        }
    }

    private void initFooterView() {
        final EditText editText = mFooterView.findViewById(R.id.et_message);
        final TextView tvPublish = mFooterView.findViewById(R.id.tv_publish);
        tvPublish.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (TextUtils.isEmpty(cs.toString().trim())) {
                    tvPublish.setEnabled(false);
                } else {
                    tvPublish.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvPublish.setOnClickListener(view -> {
            String content = editText.getText().toString();
            mCallback.onPublishMessage(mCurrentHouseDetailBean.houseId, content);
            mRecyclerView.smoothScrollToPosition(mMultipleItems.size() + 1);
            editText.setText("");  //清空留言板
        });
    }

    /*公海房屋不能发布留言板*/
    private void removeFooterView() {
        if (mFooterView != null) {
            mRecyclerView.removeFooterView(mFooterView);
            mFooterView = null;
        }
    }

    class CustomerManagerAdapter extends CommonAdapter<MultiItem> {

        CustomerManagerAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == MultiHouseItem.TYPE_OPERATE) {
                return R.layout.item_customer_manager_operate;
            } else if (viewType == MultiHouseItem.TYPE_TITLE_TIPS) {
                return R.layout.item_customer_manager_followuptips;
            } else if (viewType == MultiHouseItem.TYPE_GUIDE) {
                return R.layout.item_customer_manager_guide;
            } else if (viewType == MultiHouseItem.TYPE_CALL_LOGS) {
                return R.layout.item_customer_manager_calllogs;
            } else if (viewType == MultiHouseItem.TYPE_UNMEASURED) {
                return R.layout.item_customer_manager_unmeasured;
            } else if (viewType == MultiHouseItem.TYPE_DESIGNER) {
                return R.layout.item_customer_manager_designer;
            } else if (viewType == MultiHouseItem.TYPE_MEASURE_RESULT) {
                return R.layout.item_customer_manager_measure_result;
            } else if (viewType == MultiHouseItem.TYPE_UPLOAD_PLAN) {
                return R.layout.item_customer_manager_upload_plan;
            } else if (viewType == MultiHouseItem.TYPE_ROUNDS) {
                return R.layout.item_customer_manager_rounds;
            } else if (viewType == MultiHouseItem.TYPE_RECEIPT_TITLE) {
                return R.layout.item_customer_manager_titletips;
            } else if (viewType == MultiHouseItem.TYPE_RECEIPT) {
                return R.layout.item_customer_manager_receipt;
            } else if (viewType == MultiHouseItem.TYPE_CONTRACT_REGISTER) {
                return R.layout.item_customer_manager_contractregister;
            } else if (viewType == MultiHouseItem.TYPE_ORDER_TITLE) {
                return R.layout.item_customer_manager_titletips;
            } else if (viewType == MultiHouseItem.TYPE_ORDER) {
                return R.layout.item_customer_manager_order;
            } else if (viewType == MultiHouseItem.TYPE_UNINSTALL) {
                return R.layout.item_customer_manager_uninstall;
            } else if (viewType == MultiHouseItem.TYPE_INSTALL_DRAWING) {
                return R.layout.item_customer_manager_upload_installdrawing;
            } else if (viewType == MultiHouseItem.TYPE_INSTALLED_TITLE) {
                return R.layout.item_customer_manager_titletips;
            } else if (viewType == MultiHouseItem.TYPE_INSTALLED) {
                return R.layout.item_customer_manager_installed;
            } else if (viewType == MultiHouseItem.TYPE_MESSAGE_LOGS_TITLE) {
                return R.layout.item_customer_manager_titletips;
            }
            return R.layout.item_customer_manager_message_logs;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == MultiHouseItem.TYPE_OPERATE) {
                setOperate(holder, (OperateItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_TITLE_TIPS) {
                holder.setText(R.id.tv_tips, mContext.getString(R.string.followup_record));
            } else if (itemType == MultiHouseItem.TYPE_GUIDE) {
                setGuide(holder, (MultiHouseItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_CALL_LOGS) {
                setCallLogs(holder, (PhoneRecordItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_UNMEASURED) {
                setUnMeasured(holder, (MultiHouseItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_DESIGNER) {
                setDesigner(holder, (MultiHouseItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_MEASURE_RESULT) {
                setMeasureResult(holder, (MeasureResultItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_UPLOAD_PLAN) {
                setUploadPlan(holder, (UploadPlanItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_ROUNDS) {
                setRounds(holder, (MultiHouseItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_RECEIPT_TITLE) {
                holder.setText(R.id.tv_tips, mContext.getString(R.string.followup_receipt_title));
            } else if (itemType == MultiHouseItem.TYPE_RECEIPT) {
                setReceipt(holder, (ReceiptItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_CONTRACT_REGISTER) {
                setContractRegister(holder, (ContractItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_ORDER_TITLE) {
                holder.setText(R.id.tv_tips, mContext.getString(R.string.followup_generate_orders_title));
            } else if (itemType == MultiHouseItem.TYPE_ORDER) {
                setOrder(holder, (OrderPlacedItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_UNINSTALL) {
                setUninstall(holder, (UninstallItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_INSTALL_DRAWING) {
                setInstallDrawing(holder, (InstallDrawingItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_INSTALLED_TITLE) {
                holder.setText(R.id.tv_tips, mContext.getString(R.string.house_manage_installed));
            } else if (itemType == MultiHouseItem.TYPE_INSTALLED) {
                setInstalled(holder, (InstalledItem) multiItem);
            } else if (itemType == MultiHouseItem.TYPE_MESSAGE_LOGS_TITLE) {
                holder.setText(R.id.tv_tips, mContext.getString(R.string.followup_message_logs_title));
            } else {
                MessageLogsItem item = (MessageLogsItem) multiItem;
                setMessageLogs(holder, item, position);
            }
        }

        /*菜单列表适配器*/
        class OperateGridAdapter extends CommonAdapter<CustomerManagerV2Bean.OperateItemBean> {
            int mSize;

            OperateGridAdapter(Context context, List<CustomerManagerV2Bean.OperateItemBean> mDatas) {
                super(context, mDatas);
                int width = (DensityUtil.getScreenWidth(context) - mContext.getResources().getDimensionPixelSize(R.dimen.dp_10) * 2) / 4;
                mSize = Math.round(width * 7 / 9f);
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_customer_manager_grid_operate;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, CustomerManagerV2Bean.OperateItemBean bean, int position) {
                ImageView iv = holder.obtainView(R.id.iv_operate_icon);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                params.height = mSize;
                Glide.with(mContext).load(bean.iconPath).into(iv);
                holder.itemView.setEnabled(!bean.isLock());
                holder.itemView.setOnClickListener(view -> onOperateClick(bean));
            }
        }

        /*展示操作流程*/
        private void setOperate(RecyclerHolder holder, OperateItem item) {
            RecyclerView rvOperate = holder.obtainView(R.id.rv_operate);
            final List<CustomerManagerV2Bean.OperateItemBean> beans = item.beans;
            OperateGridAdapter adapter = new OperateGridAdapter(mContext, beans);
            rvOperate.setAdapter(adapter);
//            adapter.setOnItemClickListener((a, h, view, position) -> {
//                if (position >= 0 && position < beans.size()) {
//                    CustomerManagerV2Bean.OperateItemBean bean = beans.get(position);
//                    if (!bean.isLock()) {   //没有锁上  可以编辑
//                        onOperateClick(bean);
//                    }
//                }
//            });
        }

        /*操作流程点击*/
        private void onOperateClick(CustomerManagerV2Bean.OperateItemBean bean) {
            String code = bean.iconCode;
            if (TextUtils.equals(code, OperateCode.CODE_MESSAGE_BOARD)) {  //留言板
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() + 1);  //滚动到底部
            } else if (TextUtils.equals(code, OperateCode.CODE_GUIDE)) {  //分配导购
                openMultipleActivity(CustomerValue.TYPE_ASSIGN_GUIDE, assignGuide());
            } else if (TextUtils.equals(code, OperateCode.CODE_DESIGNER)) { //分配设计师
                openMultipleActivity(CustomerValue.TYPE_ASSIGN_DESIGNER, assignDesigner());
            } else if (TextUtils.equals(code, OperateCode.CODE_RECEIPT)) { //收款
                openMultipleActivity(CustomerValue.TYPE_RECEIPT, payment());
            } else if (TextUtils.equals(code, OperateCode.CODE_LOSE)) { //已流失
                openMultipleActivity(CustomerValue.TYPE_BEEN_LOST, null);
            } else if (TextUtils.equals(code, OperateCode.CODE_UNMEASURED)) { //预约量尺
                toUnmeasured();
            } else if (TextUtils.equals(code, OperateCode.CODE_MEASURED)) { //量尺结果
                toMeasured();
            } else if (TextUtils.equals(code, OperateCode.CODE_UPLOAD_PLAN)) { //上传方案
                toUploadPlan();
            } else if (TextUtils.equals(code, OperateCode.CODE_ROUNDS)) { //主管查房
                toRounds();
            } else if (TextUtils.equals(code, OperateCode.CODE_CONTRACT)) { //合同登记（仅能登记一次）
                if (!mCurrentHouseDetailBean.isContractRegistration()) {  //没有填过合同登记
                    toContractRegister();
                }
            } else if (TextUtils.equals(code, OperateCode.CODE_INSTALL_DRAWING)) { //上传安装图纸
                toInstallDrawing();
            } else if (TextUtils.equals(code, OperateCode.CODE_UNINSTALL)) { //预约安装
                toUninstall();
            } else if (TextUtils.equals(code, OperateCode.CODE_INSTALLED)) { //安装完成
                toInstalled();
            } else if (TextUtils.equals(code, OperateCode.CODE_CONFIRM_LOSE)) { //确认流失
                toConfirmLose();
            } else if (TextUtils.equals(code, OperateCode.CODE_INVALID_RETURN)) { //无效退回
                toInvalidReturn();
            }
        }

        private void setGuide(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.house_manage_divide_guide));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            String shopTemp = mCurrentHouseDetailBean.shopName + (TextUtils.isEmpty(mCurrentHouseDetailBean.groupName) ? ""
                    : "-" + mCurrentHouseDetailBean.groupName);
            holder.setText(R.id.tv_store, transform(0, shopTemp, holder.obtainView(R.id.tv_shop_tips)));  //所属门店
            holder.setText(R.id.tv_guide, transform(0, mCurrentHouseDetailBean.salesName, holder.obtainView(R.id.tv_guide_tips))); //导购
        }

        private void setCallLogs(RecyclerHolder holder, PhoneRecordItem item) {
            holder.setText(R.id.tv_call_time, transform2(R.string.tips_call_time, item.recordBean.getCreateDate())); //通话时间
            holder.setText(R.id.tv_call_duration, transform2(R.string.tips_call_duration, item.recordBean.talkTime)); //通话时长
            holder.setText(R.id.tv_dialer, transform2(R.string.tips_call_dialer, item.recordBean.dailPerson)); //拨号人
            if (item.isDisplayMore) {
                holder.setVisibility(R.id.tv_more, View.VISIBLE);
            } else {
                holder.setVisibility(R.id.tv_more, View.GONE);
            }
            holder.setOnClickListener(R.id.tv_more, view -> {
                List<CustomerManagerV2Bean.PhoneRecordBean> list = mCurrentHouseInfoBean.getPhoneRecord();
                IntentValue.getInstance().put("phoneRecord", list);
                openMultipleActivity(CustomerValue.TYPE_CALL_LOGS, null);
            });
        }

        /*预约量尺*/
        private void setUnMeasured(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.customer_appointment_ruler_tips2));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_reservation_time, transform(R.string.followup_reservation_time_tips, mCurrentHouseDetailBean.getAppointmentTime())); //预约时间
            holder.setText(R.id.tv_ruler, transform(0, mCurrentHouseDetailBean.appointMeasureByName, holder.obtainView(R.id.tv_ruler_tips))); //量尺人员
            holder.setText(R.id.tv_measure_store, transform(0, mCurrentHouseDetailBean.appointShopName, holder.obtainView(R.id.tv_measure_store_tips))); //量尺门店
            holder.setText(R.id.tv_measure_space, transform(0, mCurrentHouseDetailBean.getAppointMeasureSpace(), holder.obtainView(R.id.tv_measure_space_tips)));
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips))); //备注信息
            holder.setVisibility(R.id.tv_edit_unmeasured, View.VISIBLE);
            holder.setVisibility(R.id.tv_edit_unmeasured, !mIsHighSeaHouse && isUnmeasuredEditVisibility());
            holder.setOnClickListener(R.id.tv_edit_unmeasured, view -> toUnmeasured());
        }

        /*分配设计师*/
        private void setDesigner(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.followup_distribution_designer2));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_store, transform(0, mCurrentHouseDetailBean.designerShopName, holder.obtainView(R.id.tv_shop_tips))); //所属门店
            holder.setText(R.id.tv_designer, transform(0, mCurrentHouseDetailBean.designerName, holder.obtainView(R.id.tv_designer_tips))); //设计师
        }

        /*展示量尺结果数据*/
        private void setMeasureResult(RecyclerHolder holder, MeasureResultItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.customer_measure_result_tips));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_house_price, transform(R.string.followup_house_price_square, mCurrentHouseDetailBean.getHousePrice())); //每平方米房价
            holder.setText(R.id.tv_house_area, transform(R.string.followup_house_area_tips, mCurrentHouseDetailBean.getHouseArea())); //房屋面积
            holder.setText(R.id.tv_house_type, transform(R.string.followup_house_type_tips, mCurrentHouseDetailBean.getHouseType())); //户型
            holder.setText(R.id.tv_family_member, transform(0, mCurrentHouseDetailBean.getFamilyMember(), holder.obtainView(R.id.tv_family_member_tips))); //家庭成员
            holder.setText(R.id.tv_custom_space, transform(0, mCurrentHouseDetailBean.getCustomizeTheSpace(), holder.obtainView(R.id.tv_custom_space_tips))); //定制空间
            holder.setText(R.id.tv_household_demand, transform(0, mCurrentHouseDetailBean.getFurnitureDemand(), holder.obtainView(R.id.tv_household_demand_tips))); //家具需求
            holder.setText(R.id.tv_budget, transform(R.string.followup_communication_budget_tips, mCurrentHouseDetailBean.getMeasureBudget())); //沟通预算
            holder.setText(R.id.tv_decoration_style, transform(R.string.followup_decoration_style_tips, mCurrentHouseDetailBean.getPreferenceStyle())); //装修风格
            holder.setText(R.id.tv_house_status, transform(R.string.followup_house_status_tips, mCurrentHouseDetailBean.getDecorateProperties())); //房屋状态
            holder.setText(R.id.tv_decoration_progress, transform(R.string.followup_decoration_progress_tips, mCurrentHouseDetailBean.getDecorateProgress())); //装修进度
            holder.setText(R.id.tv_plan_stay, transform(R.string.followup_plan_to_stay_tips, mCurrentHouseDetailBean.getPlannedStayDate())); //计划入住
            holder.setText(R.id.tv_actual_gauge, transform(R.string.followup_actual_gauge_tips, mCurrentHouseDetailBean.getAmountOfDate())); //实际量尺
            holder.setText(R.id.tv_measure_shop, transform(0, mCurrentHouseDetailBean.measureShopName, holder.obtainView(R.id.tv_measure_shop_tips)));//量尺门店
            holder.setText(R.id.tv_ruler, transform(0, mCurrentHouseDetailBean.measureByName, holder.obtainView(R.id.tv_ruler_tips)));//量尺人员
            holder.setText(R.id.tv_reservation_drawing, transform(R.string.followup_reservation, mCurrentHouseDetailBean.getMeasureAppConfirmTime())); //预约确图
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips)));
            List<String> images = new ArrayList<>(item.images);
            if (images.size() > 3) {
                holder.setVisibility(R.id.tv_more_pictures, View.VISIBLE);
                images = images.subList(0, 3);//截取前三张图片展示
            } else {
                holder.setVisibility(R.id.tv_more_pictures, View.GONE);
            }
            setImageGrid(holder.obtainView(R.id.rv_pictures), images);
            holder.setOnClickListener(R.id.tv_more_pictures, view -> CustomerImagePreviewActivity.open(mContext, mContext.getString(R.string.title_measure_pictures), item.images));
            holder.setVisibility(R.id.tv_edit_measure_result, View.VISIBLE);
            holder.setVisibility(R.id.tv_edit_measure_result, !mIsHighSeaHouse && isMeasuredEditVisibility());
            holder.setOnClickListener(R.id.tv_edit_measure_result, view -> toMeasured());
        }

        /*上传方案*/
        private void setUploadPlan(RecyclerHolder holder, UploadPlanItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.followup_upload_scheme_title));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_reservation_drawing, transform(R.string.followup_reservation, mCurrentHouseDetailBean.getBookOrderDate())); //预约确图
            holder.setText(R.id.tv_product, transform(R.string.followup_product_tips, mCurrentHouseDetailBean.getProduct())); //产品
            holder.setText(R.id.tv_series, transform(R.string.followup_series_tips, mCurrentHouseDetailBean.getSeries())); //系列
            holder.setText(R.id.tv_style, transform(R.string.followup_style_tips, mCurrentHouseDetailBean.getStyle())); //风格
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips))); //备注信息
            List<String> images = new ArrayList<>(item.images);
//            images.add("https://file.holike.com/e6be1812-cdb3-4b94-bfb1-85763946a843.jpg");
            if (images.size() > 3) {
                holder.setVisibility(R.id.tv_more_pictures, View.VISIBLE);
                images = images.subList(0, 3);//截取前三张图片展示
            } else {
                holder.setVisibility(R.id.tv_more_pictures, View.GONE);
            }
            setImageGrid(holder.obtainView(R.id.rv_pictures), images);
            holder.setOnClickListener(R.id.tv_more_pictures, view -> CustomerImagePreviewActivity.open(mContext, mContext.getString(R.string.title_scheme_pictures), item.images));
            holder.setVisibility(R.id.tv_edit_upload_plan, !mIsHighSeaHouse && isUploadPlanEditVisibility());
            holder.setOnClickListener(R.id.tv_edit_upload_plan, view -> toUploadPlan());
        }

        /*展示主管查房数据*/
        private void setRounds(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.followup_supervisor_rounds_title));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_rounds_result, transform(0, mCurrentHouseDetailBean.getMeasureResult(), holder.obtainView(R.id.tv_rounds_result_tips))); //查房结果
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips)));
            holder.setVisibility(R.id.tv_edit_rounds, !mIsHighSeaHouse && isRoundsEditVisibility());
            holder.setOnClickListener(R.id.tv_edit_rounds, view -> toRounds());
        }

        /*展示收款数据*/
        private void setReceipt(RecyclerHolder holder, ReceiptItem item) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (item.isLastPosition) {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_bottom_white_5dp);
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
            } else {
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_receipt_time, transform(R.string.followup_receipt_time_tips, item.bean.getPayTime())); //收款时间
            holder.setText(R.id.tv_receipt_person, transform(R.string.followup_receipt_person_tips, item.bean.receiver)); //收款人
            holder.setText(R.id.tv_receipt_category, transform(R.string.followup_receipt_category_tips, item.bean.type)); //收款类别
            holder.setText(R.id.tv_receipt_amount, transform(R.string.followup_receipt_amount_tips, item.bean.amount)); //收款金额
            if (TextUtils.isEmpty(item.bean.getCategory())) {//定制品类
                holder.setVisibility(R.id.tv_custom_product_tips, View.GONE);
                holder.setVisibility(R.id.tv_custom_product, View.GONE);
            } else {
                holder.setText(R.id.tv_custom_product, transform(0, item.bean.getCategory(), holder.obtainView(R.id.tv_custom_product_tips))); //定制品类
                holder.setVisibility(R.id.tv_custom_product_tips, View.VISIBLE);
                holder.setVisibility(R.id.tv_custom_product, View.VISIBLE);
            }
            holder.setText(R.id.tv_remark, transform(0, item.bean.remark, holder.obtainView(R.id.tv_remark_tips)));//备注信息
            List<String> images = new ArrayList<>(item.images);
            if (images.size() > 3) {
                holder.setVisibility(R.id.tv_more_pictures, View.VISIBLE);
                images = images.subList(0, 3);//截取前三张图片展示
            } else {
                holder.setVisibility(R.id.tv_more_pictures, View.GONE);
            }
            setImageGrid(holder.obtainView(R.id.rv_pictures), images);
            if (item.images.size() > 3) {
                holder.setVisibility(R.id.tv_more_pictures, View.VISIBLE);
            } else {
                holder.setVisibility(R.id.tv_more_pictures, View.GONE);
            }
            holder.setOnClickListener(R.id.tv_more_pictures, view -> CustomerImagePreviewActivity.open(mContext, mContext.getString(R.string.title_payment_pictures), item.images));
        }

        /*展示合同登记数据*/
        private void setContractRegister(RecyclerHolder holder, final ContractItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.followup_contract_title));
            if (mCurrentHouseDetailBean.isContractRegistration()) {  //已经登记了合同
                holder.setText(R.id.tv_date, item.operateTime);
                holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
                holder.setText(R.id.tv_turnover, transform(R.string.followup_turnover_tips, mCurrentHouseDetailBean.salesAmount)); //成交金额
                holder.setText(R.id.tv_deposit_received, transform(R.string.followup_deposit_received_tips, mCurrentHouseDetailBean.earnestHouse)); //已收订金
                holder.setText(R.id.tv_payment, transform(R.string.followup_this_payment_tips, mCurrentHouseDetailBean.contractPayAmount)); //本次收款
                holder.setText(R.id.tv_remain_tail, mTextHelper.obtainColorBoldStyle(R.string.followup_remain_tail_tips, mCurrentHouseDetailBean.lastRemaining, R.color.textColor15)); //还剩尾款
                holder.setText(R.id.tv_sign_date, transform(R.string.customer_date_of_signing_tips, mCurrentHouseDetailBean.getContractDate())); //签约日期
                holder.setText(R.id.tv_contact_receipt, transform(R.string.followup_contact_receipt_tips, mCurrentHouseDetailBean.getAppDeliveryDate())); //约定收货
                holder.setText(R.id.tv_sign_person, transform(R.string.followup_contractor_tips, mCurrentHouseDetailBean.contracteByName)); //签约人
                holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips)));//备注信息
                List<String> images = new ArrayList<>(item.images);
                if (images.size() > 3) {
                    holder.setVisibility(R.id.tv_more_pictures, View.VISIBLE);
                    images = images.subList(0, 3);//截取前三张图片展示
                } else {
                    holder.setVisibility(R.id.tv_more_pictures, View.GONE);
                }
                setImageGrid(holder.obtainView(R.id.rv_pictures), images);
                holder.setOnClickListener(R.id.tv_more_pictures, view -> CustomerImagePreviewActivity.open(mContext, mContext.getString(R.string.title_contract_register_pictures), item.images));
                holder.setVisibility(R.id.tv_edit_contract, View.GONE);
            } else {
                final String empty = "";
                holder.setText(R.id.tv_date, empty);
                holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, empty)); //填写人
                holder.setText(R.id.tv_turnover, transform(R.string.followup_turnover_tips, empty)); //成交金额
                holder.setText(R.id.tv_deposit_received, transform(R.string.followup_deposit_received_tips, empty)); //已收订金
                holder.setText(R.id.tv_payment, transform(R.string.followup_this_payment_tips, empty)); //本次收款
                holder.setText(R.id.tv_remain_tail, transform(R.string.followup_remain_tail_tips, empty)); //还剩尾款
                holder.setText(R.id.tv_sign_date, transform(R.string.customer_date_of_signing_tips, empty)); //签约日期
                holder.setText(R.id.tv_contact_receipt, transform(R.string.followup_contact_receipt_tips, empty)); //约定收货
                holder.setText(R.id.tv_sign_person, transform(R.string.followup_contractor_tips, empty)); //签约人
                holder.setText(R.id.tv_remark, transform(0, empty, holder.obtainView(R.id.tv_remark_tips)));//备注信息
                holder.setVisibility(R.id.rv_pictures, View.GONE);
                holder.setVisibility(R.id.tv_more_pictures, View.GONE);
                holder.setVisibility(R.id.tv_edit_contract, !mIsHighSeaHouse && isContractEditVisibility());
            }
            holder.setOnClickListener(R.id.tv_edit_contract, view -> toContractRegister());
        }

        /*生成订单*/
        private void setOrder(RecyclerHolder holder, OrderPlacedItem item) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (item.isLastPosition) {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_bottom_white_5dp);
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
            } else {
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            String source = mContext.getString(R.string.order_number_tips);
            if (TextUtils.isEmpty(item.bean.orderId)) {
                holder.setText(R.id.tv_order_number, transform(R.string.order_number_tips, "")); //订单号
                holder.setDrawableRight(R.id.tv_order_number, null);
                holder.setEnabled(R.id.tv_order_number, false);
            } else {
                int start = source.length();
                source += item.bean.getOrderId();
                SpannableString ss = new SpannableString(source);
                ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor14)), start, source.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new StyleSpan(Typeface.BOLD), start, source.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setText(R.id.tv_order_number, ss);
                holder.setDrawableRight(R.id.tv_order_number, ContextCompat.getDrawable(mContext, R.drawable.layer_arrow_right_accent));
                holder.setEnabled(R.id.tv_order_number, true);
            }
            holder.setOnClickListener(R.id.tv_order_number, view -> OrderDetailsActivity.open(mFragment, item.bean.orderId));
            holder.setText(R.id.tv_order_person, transform(R.string.order_player_tips, item.bean.creater)); //下单人
            holder.setText(R.id.tv_order_time, transform(R.string.order_time_tips, item.bean.getCreateDate()));//下单时间、
            holder.setText(R.id.tv_space, transform(R.string.order_space_tips, item.bean.houseName)); //下单空间
            holder.setText(R.id.tv_order_status, transform(R.string.order_status_tips, item.bean.orderStatusName)); //下单状态
        }

        /*预约安装*/
        private void setUninstall(RecyclerHolder holder, UninstallItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.customer_reservation_install_tips2));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_reservation_date, transform(R.string.followup_reservation_date_tips, item.date)); //预约时间
            holder.setText(R.id.tv_install_area, item.area); //安装面积
            holder.setText(R.id.tv_installer, transform(0, item.installUser, holder.obtainView(R.id.tv_installer_tips))); //安装师傅
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips)));//备注信息
            holder.setVisibility(R.id.tv_edit_uninstall, !mIsHighSeaHouse && isUninstallEditVisibility());  //非公海客户才能编辑
            holder.setOnClickListener(R.id.tv_edit_uninstall, view -> toUninstall());
        }

        /*上传安装图纸*/
        private void setInstallDrawing(RecyclerHolder holder, InstallDrawingItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.followup_upload_install_drawing_title));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips)));//备注信息
            RecyclerView rvPictures = holder.obtainView(R.id.rv_pictures);
            List<String> images = new ArrayList<>(item.images);
            if (images.size() > 3) {
                holder.setVisibility(R.id.tv_more_pictures, View.VISIBLE);
                images = images.subList(0, 3);//截取前三张图片展示
            } else {
                holder.setVisibility(R.id.tv_more_pictures, View.GONE);
            }
            setImageGrid(rvPictures, images);
            holder.setOnClickListener(R.id.tv_more_pictures, view -> CustomerImagePreviewActivity.open(mContext, mContext.getString(R.string.title_install_drawing_pictures), item.images));
            holder.setVisibility(R.id.tv_edit_install_drawing, !mIsHighSeaHouse && isInstallDrawingEditVisibility());
            holder.setOnClickListener(R.id.tv_edit_install_drawing, view -> toInstallDrawing());
        }

        /*已安装完成*/
        private void setInstalled(RecyclerHolder holder, InstalledItem item) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (item.isLastPosition) {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_bottom_white_5dp);
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
            } else {
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            CustomerManagerV2Bean.InstallUserBean userBean = item.bean.getInstallUserBean();
            if (userBean == null) {
                holder.setText(R.id.tv_completion, transform(R.string.followup_actual_completion_tips, "")); //实际完成
                holder.setText(R.id.tv_installer, transform(R.string.followup_installation_master, "")); //安装师傅
                holder.setText(R.id.tv_install_status, transform(0, "", holder.obtainView(R.id.tv_install_status_tips))); //安装状态
                holder.setText(R.id.tv_remark, transform(0, "", holder.obtainView(R.id.tv_remark_tips)));//备注信息
            } else {
                holder.setText(R.id.tv_completion, transform(R.string.followup_actual_completion_tips, userBean.getActualInstallDate()));
                holder.setText(R.id.tv_installer, transform(R.string.followup_installation_master, userBean.installUserName)); //安装师傅
                holder.setText(R.id.tv_install_status, transform(0, userBean.getInstallState(), holder.obtainView(R.id.tv_install_status_tips))); //安装状态
                holder.setText(R.id.tv_remark, transform(0, userBean.remark, holder.obtainView(R.id.tv_remark_tips)));//备注信息
            }
            RecyclerView rvPictures = holder.obtainView(R.id.rv_pictures);
            List<String> images = new ArrayList<>(item.images);
            if (images.size() > 3) {
                holder.setVisibility(R.id.tv_more_pictures, View.VISIBLE);
                images = images.subList(0, 3);//截取前三张图片展示
            } else {
                holder.setVisibility(R.id.tv_more_pictures, View.GONE);
            }
            setImageGrid(rvPictures, images);
            holder.setOnClickListener(R.id.tv_more_pictures, view -> CustomerImagePreviewActivity.open(mContext, mContext.getString(R.string.title_install_drawing_pictures), item.images));
            //编辑按钮显示在安装完成步骤最后一个item
            holder.setVisibility(R.id.tv_edit_installed, !mIsHighSeaHouse && isInstalledEditVisibility() && item.isLastPosition);
            holder.setOnClickListener(R.id.tv_edit_installed, view -> toInstalled());
        }

        /*展示留言记录*/
        private void setMessageLogs(RecyclerHolder holder, MessageLogsItem item, int position) {
            if (position == getItemCount() - 1) {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_bottom_white_5dp);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            if (item.isEmpty) {
                holder.setVisibility(R.id.ll_top_layout, View.GONE);
                holder.setText(R.id.tv_message, transform(0, ""));
            } else {
                holder.setVisibility(R.id.ll_top_layout, View.VISIBLE);
                holder.setText(R.id.tv_date, item.bean.getCreateDate());
                String user = TextUtils.isEmpty(item.bean.createName) ? "" : item.bean.createName;
                holder.setText(R.id.tv_user, mContext.getString(R.string.tips_fill_in_person) + user);
                if (item.bean.isAssignGuide()) {
                    holder.setText(R.id.tv_user_name_tips, mContext.getString(R.string.customer_guide_tips));
                    holder.setText(R.id.tv_shop, transform(0, item.bean.shopName, holder.obtainView(R.id.tv_shop_tips)));
                    holder.setText(R.id.tv_user_name, transform(0, item.bean.userName, holder.obtainView(R.id.tv_user_name_tips)));
//                    holder.setText(R.id.tv_shop2, transform(0, mCurrentHouseDetailBean.promoterShopName, holder.obtainView(R.id.tv_shop_tips2)));
//                    holder.setText(R.id.tv_user_name2, transform(0, mCurrentHouseDetailBean.promoter, holder.obtainView(R.id.tv_user_name_tips2)));
                    holder.setVisibility(R.id.ll_assign_type, View.VISIBLE);
//                    holder.setVisibility(R.id.ll_salesman_layout, View.VISIBLE);
                    holder.setVisibility(R.id.tv_message, View.GONE);
                } else if (item.bean.isAssignDesigner()) {
                    holder.setText(R.id.tv_user_name_tips, mContext.getString(R.string.followup_designer));
                    holder.setText(R.id.tv_shop, transform(0, item.bean.shopName, holder.obtainView(R.id.tv_shop_tips)));
                    holder.setText(R.id.tv_user_name, transform(0, item.bean.userName, holder.obtainView(R.id.tv_user_name_tips)));
                    holder.setVisibility(R.id.ll_assign_type, View.VISIBLE);
//                    holder.setVisibility(R.id.ll_salesman_layout, View.GONE);
                    holder.setVisibility(R.id.tv_message, View.GONE);
                } else {
                    holder.setText(R.id.tv_message, item.bean.message);
                    holder.setVisibility(R.id.ll_assign_type, View.GONE);
//                    holder.setVisibility(R.id.ll_salesman_layout, View.GONE);
                    holder.setVisibility(R.id.tv_message, View.VISIBLE);
                }
            }
        }

        private void setImageGrid(RecyclerView rvPictures, List<String> images) {
            rvPictures.setNestedScrollingEnabled(false);
            if (images.isEmpty()) {
                rvPictures.setVisibility(View.GONE);
            } else {
                rvPictures.setVisibility(View.VISIBLE);
                if (rvPictures.getTag() == null) {
                    int space = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
                    GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(3, space, false, 0);
                    rvPictures.addItemDecoration(itemDecoration);
                    rvPictures.setTag(itemDecoration);
                }
                SquareImageGridAdapter adapter = new SquareImageGridAdapter(mContext, images);
                rvPictures.setAdapter(adapter);
            }
        }
    }

    /*房屋列表适配器*/
    private class HouseListAdapter extends CommonAdapter<CustomerManagerV2Bean.HouseInfoBean> {
        private int mSelectPosition;

        HouseListAdapter(Context context, List<CustomerManagerV2Bean.HouseInfoBean> mDatas, int selectPosition) {
            super(context, mDatas);
            mSelectPosition = selectPosition;
        }

        void setSelectPosition(int selectPosition) {
            this.mSelectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, CustomerManagerV2Bean.HouseInfoBean bean, final int position) {
            TextView tvName = holder.obtainView(R.id.tv_house_name);
            String text = mContext.getString(R.string.house) + (getItemCount() - position); //最新的排前面
            tvName.setText(text);
            if (mSelectPosition == position) {
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.color_while));
                tvName.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
            } else {
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.textColor5));
                tvName.setBackgroundResource(R.drawable.bg_corners5dp_top_bg);
            }
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_customer_manager_house;
        }
    }

    /*预约量尺*/
    private void toUnmeasured() {
        openMultipleActivity(CustomerValue.TYPE_UNMEASURED, unmeasuredBundle());
    }

    /*量尺结果*/
    private void toMeasured() {
        openMultipleActivity(CustomerValue.TYPE_MEASURE_RESULT, measureResultBundle());
    }

    /*上传方案*/
    private void toUploadPlan() {
        openMultipleActivity(CustomerValue.TYPE_UPLOAD_PLAN, uploadPlanBundle());
    }

    /*主管查房*/
    private void toRounds() {
        openMultipleActivity(CustomerValue.TYPE_ROUNDS, supervisorRounds());
    }

    /*合同登记*/
    private void toContractRegister() {
        openMultipleActivity(CustomerValue.TYPE_CONTRACT_REGISTER, contractRegister());
    }

    /*预约安装*/
    private void toUninstall() {
        openMultipleActivity(CustomerValue.TYPE_UNINSTALL, uninstall());
    }

    /*上传安装图纸*/
    private void toInstallDrawing() {
        openMultipleActivity(CustomerValue.TYPE_INSTALL_DRAWING, uploadInstallDrawing());
    }

    /*安装完成*/
    private void toInstalled() {
        openMultipleActivity(CustomerValue.TYPE_INSTALLED, finishInstall());
    }

    /*确认流失*/
    private void toConfirmLose() {
        new MaterialDialog.Builder(mContext)
                .message(R.string.dialog_message_confirm_lose_house)
                .positiveButton(R.string.confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mCallback.onConfirmLoseHouse(mCurrentHouseDetailBean.houseId);
                }).show();
    }

    /*无效退回*/
    private void toInvalidReturn() {
        openMultipleActivity(CustomerValue.TYPE_INVALID_RETURN, null);
    }

    private void openMultipleActivity(String type, @Nullable Bundle extras) {
        if (mCurrentHouseDetailBean == null) return;
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putString(CustomerValue.HOUSE_ID, mCurrentHouseDetailBean.houseId);
        Intent intent = new Intent(mContext, CustomerMultiTypeActivity.class);
        intent.setType(type);
        mFragment.openActivityForResult(intent, extras);
    }


    public interface CustomerManagerCallback {
        void onGetCustomerDetail(String personalId, boolean isHighSeasHouse);

        void onHighSeasHistory(String personalId, String houseId);

        void onEditInfo(Bundle bundle);

        void onEditHouse(Bundle bundle);

        void onPublishMessage(String houseId, String content);

        void doReceive(String houseId, String shopId, String groupId, String salesId);

        void onConfirmLoseHouse(String houseId);
    }
}
