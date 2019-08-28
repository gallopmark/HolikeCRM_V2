package com.holike.crm.activity.customer.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerImagePreviewActivity;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/7/30.
 * Copyright holike possess 2019.
 */
public class HighSeasHistoryHelper extends IHighSeasMultipleHelper {

    class HistoryListAdapter extends CommonAdapter<CustomerManagerV2Bean.HouseInfoBean> {

        int mSelectPosition;

        HistoryListAdapter(Context context, List<CustomerManagerV2Bean.HouseInfoBean> mDatas) {
            super(context, mDatas);
        }

        void setSelectPosition(int selectPosition) {
            this.mSelectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, CustomerManagerV2Bean.HouseInfoBean bean, final int position) {
            TextView tvName = holder.obtainView(R.id.tv_name);
            String text = mContext.getString(R.string.record) + (position + 1);
            tvName.setText(text);
            if (mSelectPosition == position) {
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.color_while));
                tvName.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
            } else {
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                tvName.setBackgroundResource(R.drawable.bg_corners5dp_top_bg);
            }
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_customer_historyrecord;
        }
    }

    private RecyclerView mHistoryRecyclerView;
    private RecyclerView mMultipleRecyclerView;
    private List<CustomerManagerV2Bean.HouseInfoBean> mHistoryList;
    private HistoryListAdapter mListAdapter;
    private List<MultiItem> mMultipleItems;
    private MultipleInfoAdapter mMultipleAdapter;
    private int mOldIndex = -1, mCurrentIndex;
    private CustomerManagerV2Bean mCustomerManagerBean;

    public HighSeasHistoryHelper(BaseActivity<?, ?> activity) {
        super(activity);
        mHistoryList = new ArrayList<>();
        mListAdapter = new HistoryListAdapter(mActivity, mHistoryList);
        mMultipleItems = new ArrayList<>();
        mMultipleAdapter = new MultipleInfoAdapter(mActivity, mMultipleItems);
        mHistoryRecyclerView = mActivity.findViewById(R.id.rv_history);
        mMultipleRecyclerView = mActivity.findViewById(R.id.rv_multiple);
        setup();
    }

    private void setup() {
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        mHistoryRecyclerView.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            mHistoryRecyclerView.smoothScrollToPosition(position);
            mListAdapter.setSelectPosition(position);
            updateHistoryRecord(position);
        });
        mMultipleRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        mMultipleRecyclerView.setAdapter(mMultipleAdapter);
    }

    public void onHttpResponse(CustomerManagerV2Bean bean) {
        mCustomerManagerBean = bean;
        updateHouseList();
        updateHistoryRecord(mCurrentIndex);
    }

    /*历史记录列表*/
    private void updateHouseList() {
        mHistoryList.clear();
        mHistoryList.addAll(mCustomerManagerBean.getHouseInfoList());
        mListAdapter.notifyDataSetChanged();
    }

    private void updateHistoryRecord(int position) {
        this.mCurrentIndex = position;
        if (this.mCurrentIndex != mOldIndex) { //点击的房屋位置不是上次点击的位置才更新
            updateMultiHouseInfo(position);
            this.mOldIndex = mCurrentIndex;
        }
    }

    private void updateMultiHouseInfo(int position) {
        mMultipleItems.clear();
        List<CustomerManagerV2Bean.HouseInfoBean> houseItems = mCustomerManagerBean.getHouseInfoList();
        if (houseItems.isEmpty() || position < 0 || position >= houseItems.size()) {
            mMultipleAdapter.notifyDataSetChanged();
            return;
        }
        mCurrentHouseInfoBean = houseItems.get(position);
        mCurrentHistoryList = new ArrayList<>(mCurrentHouseInfoBean.getHistoryList());
        mCurrentHouseDetailBean = mCurrentHouseInfoBean.getHouseDetail();
        wrapperMultipleItems();
    }

    private void wrapperMultipleItems() {
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
        mMultipleAdapter.notifyDataSetChanged();
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
        if (!list.isEmpty()) {
            for (CustomerManagerV2Bean.PhoneRecordBean bean : list) {
                mMultipleItems.add(new PhoneRecordItem(bean));
            }
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

    class MultipleInfoAdapter extends CommonAdapter<MultiItem> {

        MultipleInfoAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == MultiHouseItem.TYPE_GUIDE) {
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
            if (itemType == MultiHouseItem.TYPE_GUIDE) {
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

        private void setGuide(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.house_manage_divide_guide));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_store, transform(R.string.tips_customer_store_belong2, mCurrentHouseDetailBean.shopName));  //所属门店
            holder.setText(R.id.tv_guide, transform(R.string.customer_guide_tips, mCurrentHouseDetailBean.salesName)); //导购
//            holder.itemView.setOnClickListener(view -> openMultipleActivity(CustomerValue.TYPE_ASSIGN_GUIDE, assignGuide()));
        }

        private void setCallLogs(RecyclerHolder holder, PhoneRecordItem item) {
            holder.setVisibility(R.id.tv_more, View.GONE);  //隐藏更多按钮
            holder.setText(R.id.tv_call_time, transform2(R.string.tips_call_time, item.recordBean.getCreateDate())); //通话时间
            holder.setText(R.id.tv_call_duration, transform2(R.string.tips_call_duration, item.recordBean.talkTime)); //通话时长
            holder.setText(R.id.tv_dialer, transform2(R.string.tips_call_dialer, item.recordBean.dailPerson)); //拨号人
        }

        /*预约量尺*/
        private void setUnMeasured(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.customer_appointment_ruler_tips2));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_reservation_time, transform(R.string.followup_reservation_time_tips, mCurrentHouseDetailBean.getAppointmentTime())); //预约时间
            holder.setText(R.id.tv_ruler, transform(R.string.customer_ruler_tips, mCurrentHouseDetailBean.appointMeasureByName)); //量尺人员
            holder.setText(R.id.tv_measure_store, transform(0, mCurrentHouseDetailBean.appointShopName, holder.obtainView(R.id.tv_measure_store_tips))); //量尺门店
            holder.setText(R.id.tv_measure_space, transform(0, mCurrentHouseDetailBean.getAppointMeasureSpace(), holder.obtainView(R.id.tv_measure_space_tips)));
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips))); //备注信息
            holder.setVisibility(R.id.tv_edit_unmeasured, View.GONE);
        }

        /*分配设计师*/
        private void setDesigner(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.followup_distribution_designer2));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_store, transform(R.string.tips_customer_store_belong2, mCurrentHouseDetailBean.designerShopName)); //所属门店
            holder.setText(R.id.tv_designer, transform(R.string.followup_designer, mCurrentHouseDetailBean.designerName));
//            holder.itemView.setOnClickListener(view -> openMultipleActivity(CustomerValue.TYPE_ASSIGN_DESIGNER, assignDesigner()));
        }

        private void setMeasureResult(RecyclerHolder holder, MeasureResultItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.customer_measure_result_tips));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_house_price, transform(R.string.followup_house_price_square, mCurrentHouseDetailBean.getHousePrice())); //每平方米房价
            holder.setText(R.id.tv_house_area, transform(R.string.followup_house_area_tips, mCurrentHouseDetailBean.getAreaType())); //房屋面积
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
            holder.setText(R.id.tv_measure_shop, transform(R.string.followup_measure_store_tips, mCurrentHouseDetailBean.measureShopName));//量尺门店
            holder.setText(R.id.tv_ruler, transform(R.string.customer_ruler_tips, mCurrentHouseDetailBean.measureByName));//量尺人员
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
            holder.setVisibility(R.id.tv_edit_measure_result, View.GONE);
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
            holder.setVisibility(R.id.tv_edit_upload_plan, View.GONE);
        }

        private void setRounds(RecyclerHolder holder, MultiHouseItem item) {
            holder.setText(R.id.tv_title, mContext.getString(R.string.followup_supervisor_rounds_title));
            holder.setText(R.id.tv_date, item.operateTime);
            holder.setText(R.id.tv_user, transform(R.string.tips_fill_in_person, item.operator)); //填写人
            holder.setText(R.id.tv_rounds_result, transform(0, mCurrentHouseDetailBean.getMeasureResult(), holder.obtainView(R.id.tv_rounds_result_tips))); //查房结果
            holder.setText(R.id.tv_remark, transform(0, item.remark, holder.obtainView(R.id.tv_remark_tips)));
            holder.setVisibility(R.id.tv_edit_rounds, View.GONE);
        }

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
            if (item.bean.isTailType()) {      //收尾款
                holder.setVisibility(R.id.tv_custom_product_tips, View.GONE);
                holder.setVisibility(R.id.tv_custom_product, View.GONE);
            } else {
                holder.setText(R.id.tv_custom_product, transform(0, item.customProduct, holder.obtainView(R.id.tv_custom_product_tips))); //定制品类
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
//            holder.itemView.setOnClickListener(view -> openMultipleActivity(CustomerValue.TYPE_RECEIPT, payment()));
        }

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
            }
//            holder.itemView.setOnClickListener(view -> openMultipleActivity(CustomerValue.TYPE_CONTRACT_REGISTER, contractRegister()));
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
                holder.setDrawableRight(R.id.tv_order_number, 0);
                holder.setEnabled(R.id.tv_order_number, false);
            } else {
                int start = source.length();
                source += item.bean.getOrderId();
                SpannableString ss = new SpannableString(source);
                ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor14)), start, source.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new StyleSpan(Typeface.BOLD), start, source.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setText(R.id.tv_order_number, ss);
                holder.setDrawableRight(R.id.tv_order_number, R.drawable.ic_arrow_right_accent);
                holder.setEnabled(R.id.tv_order_number, true);
            }
            holder.setOnClickListener(R.id.tv_order_number, view -> {

            });
            holder.setText(R.id.tv_order_person, transform(R.string.order_player_tips, item.bean.orderBy)); //下单人
            holder.setText(R.id.tv_order_time, transform(R.string.order_time_tips, item.bean.orderTime));//下单时间、
            holder.setText(R.id.tv_space, transform(R.string.order_space_tips, item.bean.space)); //下单空间
            holder.setText(R.id.tv_order_status, transform(R.string.order_status_tips, item.bean.status)); //下单状态
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
            holder.setVisibility(R.id.tv_edit_uninstall, View.GONE);  //非公海客户才能编辑
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
            holder.setVisibility(R.id.tv_edit_install_drawing, View.GONE);
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
        }

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
                    holder.setText(R.id.tv_shop, transform(R.string.tips_customer_store_belong2, item.bean.shopName));
                    holder.setText(R.id.tv_user_name, transform(R.string.customer_guide_tips, item.bean.userName));
                    holder.setVisibility(R.id.ll_assign_type, View.VISIBLE);
                    holder.setVisibility(R.id.tv_message, View.GONE);
                } else if (item.bean.isAssignDesigner()) {
                    holder.setText(R.id.tv_shop, transform(R.string.tips_customer_store_belong2, item.bean.shopName));
                    holder.setText(R.id.tv_user_name, transform(R.string.followup_designer, item.bean.userName));
                    holder.setVisibility(R.id.ll_assign_type, View.VISIBLE);
                    holder.setVisibility(R.id.tv_message, View.GONE);
                } else {
                    holder.setText(R.id.tv_message, item.bean.message);
                    holder.setVisibility(R.id.ll_assign_type, View.GONE);
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
                ImageGridAdapter adapter = new ImageGridAdapter(mContext, images);
                rvPictures.setAdapter(adapter);
                adapter.setOnItemClickListener((adapter1, holder, view, p) -> PhotoViewActivity.openImage(mActivity, p, images));
            }
        }

        class ImageGridAdapter extends CommonAdapter<String> {

            ImageGridAdapter(Context context, List<String> mDatas) {
                super(context, mDatas);
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_square_image;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, String url, int position) {
                ImageView iv = holder.obtainView(R.id.iv_image);
                Glide.with(mContext).load(url).apply(new RequestOptions().placeholder(R.drawable.loading_photo).error(0).centerCrop()).into(iv);
            }
        }
    }
}
