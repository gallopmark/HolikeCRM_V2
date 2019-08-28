package com.holike.crm.presenter.activity;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.fragment.customer.workflow.AddRecordFragment;
import com.holike.crm.fragment.customer.workflow.BookingInstallFragment;
import com.holike.crm.fragment.customer.workflow.BookingMeasureFragment;
import com.holike.crm.fragment.customer.workflow.CollectDepositFragment;
import com.holike.crm.fragment.customer.workflow.CollectMoneyFragment;
import com.holike.crm.fragment.customer.workflow.DivideDesignerFragment;
import com.holike.crm.fragment.customer.workflow.DividedGuideFragment;
import com.holike.crm.fragment.customer.workflow.InstalledFragment;
import com.holike.crm.fragment.customer.workflow.LossedFragment;
import com.holike.crm.fragment.customer.workflow.ShoperCheckFragment;
import com.holike.crm.fragment.customer.workflow.SignedFragment;
import com.holike.crm.fragment.customer.workflow.UploadMeasureFragment;
import com.holike.crm.fragment.customer.workflow.UploadPlanFragment;
import com.holike.crm.fragment.customer.workflow.UploadReMeasureFragment;
import com.holike.crm.model.activity.CustomerDetailModel;
import com.holike.crm.util.NoDoubleClickUtil;
import com.holike.crm.util.NumberUtil;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.activity.CustomerDetailView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/8/17.
 * 客户管理
 */

public class CustomerDetailPresenter extends BasePresenter<CustomerDetailView, CustomerDetailModel> {
    private CustomerDetailBean mDetailBean;
    private CustomerDetailBean.CustomerDetailInfoListBean mInfoListBean;
    private CustomerDetailBean.PersonalBean mPersonalBean;
    private int mSelectPosition = 0;

    private List<MultiItem> mMultiItems = new ArrayList<>();
    private CustomerDetailAdapter mDetailAdapter;

    /*设置客户详情适配器*/
    public void setDetailAdapter(RecyclerView recyclerView, OnMultiItemClickListener onMultiItemClickListener) {
        mDetailAdapter = new CustomerDetailAdapter(recyclerView.getContext(), mMultiItems);
        recyclerView.setAdapter(mDetailAdapter);
        mDetailAdapter.setOnMultiItemClickListener(onMultiItemClickListener);
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public CustomerDetailBean getDetailBean() {
        return mDetailBean;
    }

    /*更新客户详情信息，默认显示第一个房屋信息*/
    public void update(Context context, CustomerDetailBean detailBean, int defaultIndex) {
        this.mDetailBean = detailBean;
        this.mPersonalBean = detailBean.getPersonal();
        this.mSelectPosition = defaultIndex;
        if (this.mDetailBean.getCustomerDetailInfoList() == null || this.mDetailBean.getCustomerDetailInfoList().isEmpty()) {
            return;
        }
        if (defaultIndex < 0 || defaultIndex >= this.mDetailBean.getCustomerDetailInfoList().size()) {
            return;
        }
        this.mMultiItems.clear();
        List<CustomerDetailBean.CustomerDetailInfoListBean> houseList = new ArrayList<>();
        if (detailBean.getCustomerDetailInfoList() != null && !detailBean.getCustomerDetailInfoList().isEmpty()) {
            this.mInfoListBean = detailBean.getCustomerDetailInfoList().get(defaultIndex);
            houseList.addAll(detailBean.getCustomerDetailInfoList());
        }
        String address = "";
        CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean infoBean = mInfoListBean.getListHouseInfo();
        if (infoBean != null) {
            if (!TextUtils.isEmpty(infoBean.getBuildingName())) {
                address += infoBean.getBuildingName();
            }
            if (!TextUtils.isEmpty(infoBean.getBuildingNumber())) {
                address += infoBean.getBuildingNumber();
            }
        }
        String currentState = context.getString(R.string.current_state_tips);
        if (infoBean != null && !TextUtils.isEmpty(infoBean.getStatusMove())) {
            currentState += infoBean.getStatusMove();
        }
        ItemTopBean topBean = new ItemTopBean(address, currentState);
        topBean.setHouseList(houseList);
        this.mMultiItems.add(topBean);
        if (this.mInfoListBean != null) {
            if (this.mInfoListBean.getListHouseSpace() != null && !this.mInfoListBean.getListHouseSpace().isEmpty()) {
                this.mMultiItems.addAll(this.mInfoListBean.getListHouseSpace());
            }
            if (this.mInfoListBean.getListStatus() != null && !this.mInfoListBean.getListStatus().isEmpty()) {
                this.mMultiItems.add(new OperateItem(this.mInfoListBean.getListStatus()));
            }
            if (this.mInfoListBean.getListHistory() != null && !this.mInfoListBean.getListHistory().isEmpty()) {
                this.mMultiItems.add(new FollowupItem());
                this.mMultiItems.addAll(this.mInfoListBean.getListHistory());
            }
        }
        mDetailAdapter.notifyDataSetChanged();
    }

    class ItemTopBean implements MultiItem {
        @Override
        public int getItemType() {
            return 0;
        }

        String address;
        String state;
        List<CustomerDetailBean.CustomerDetailInfoListBean> mHouseList;

        ItemTopBean(String address, String state) {
            this.address = address;
            this.state = state;
        }

        List<CustomerDetailBean.CustomerDetailInfoListBean> getHouseList() {
            if (mHouseList == null) return new ArrayList<>();
            return mHouseList;
        }

        void setHouseList(List<CustomerDetailBean.CustomerDetailInfoListBean> houseList) {
            this.mHouseList = houseList;
        }
    }

    class OperateItem implements MultiItem {

        private List<CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean> mListStatusBeans;

        List<CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean> getListStatusBeans() {
            return mListStatusBeans;
        }

        OperateItem(List<CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean> listStatusBeans) {
            this.mListStatusBeans = listStatusBeans;
        }

        @Override
        public int getItemType() {
            return 2;
        }
    }

    class FollowupItem implements MultiItem {

        @Override
        public int getItemType() {
            return 3;
        }
    }

    public interface OnMultiItemClickListener {
        void onHouseSelected(CustomerDetailBean.CustomerDetailInfoListBean bean, String houseId, int position);

        void onEditClick(CustomerDetailBean.CustomerDetailInfoListBean infoListBean, CustomerDetailBean.PersonalBean personalBean);

        void onOrderClick(String orderId);

        void onOperateClick(boolean isShowed, CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean listStatusBean,
                            CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean,
                            CustomerDetailBean.PersonalBean personalBean, int position);

        void onCanceled(CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean,
                        CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean);
    }

    public class CustomerDetailAdapter extends CommonAdapter<MultiItem> {

        public OnMultiItemClickListener mListener;
        private int mScrollPosition;

        void setOnMultiItemClickListener(OnMultiItemClickListener mListener) {
            this.mListener = mListener;
        }

        CustomerDetailAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem item, int position) {
            if (position == 0) {
                holder.setBackgroundResource(R.id.itemContainer, R.drawable.bg_shadow_layer_radius6dp_top);
            } else if (position == getItemCount() - 1) {
                holder.setBackgroundResource(R.id.itemContainer, R.drawable.bg_shadow_layer_radius6dp_bottom);
            } else {
                holder.setBackgroundResource(R.id.itemContainer, R.drawable.bg_shadow_layer_radius6dp_center);
            }
            int itemViewType = holder.getItemViewType();
            if (itemViewType == 0) {
                ItemTopBean topBean = (ItemTopBean) item;
                holder.setText(R.id.tv_house_manage_address, topBean.address);
                holder.setText(R.id.tv_house_manage_state, topBean.state);
                holder.setOnClickListener(R.id.tv_customer_detail_edit, view -> {
                    if (mListener != null) {
                        mListener.onEditClick(mInfoListBean, mPersonalBean);
                    }
                });
                RecyclerView recyclerView = holder.obtainView(R.id.mHouseRv);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new HouseListAdapter(mContext, topBean.getHouseList()));
                recyclerView.clearOnScrollListeners();
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        mScrollPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    }
                });
                recyclerView.smoothScrollToPosition(mScrollPosition);
            } else if (itemViewType == 1) {
                CustomerDetailBean.CustomerDetailInfoListBean.ListHouseSpaceBean orderBean = (CustomerDetailBean.CustomerDetailInfoListBean.ListHouseSpaceBean) item;
                TextView tvNum = holder.obtainView(R.id.tv_item_rv_house_manage_order_num);
                TextView tvState = holder.obtainView(R.id.tv_item_rv_house_manage_order_state);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_house_manage_order_name);
                TextView tvTime = holder.obtainView(R.id.tv_item_rv_house_manage_order_time);
                String orderId = mContext.getString(R.string.order_number_tips) + (TextUtils.isEmpty(orderBean.getOrderId()) ? "" : orderBean.getOrderId());
                tvNum.setText(orderId);
                String state = mContext.getString(R.string.order_status_tips) + (TextUtils.isEmpty(orderBean.getHouseName()) ? "" : orderBean.getHouseName());
                tvState.setText(state);
                String creator = mContext.getString(R.string.order_player_tips) + (TextUtils.isEmpty(orderBean.getCreater()) ? "" : orderBean.getCreater());
                tvName.setText(creator);
                String time = mContext.getString(R.string.order_time_tips) + (TextUtils.isEmpty(orderBean.getCreateDate()) ? "" : TimeUtil.stampToString(String.valueOf(orderBean.getCreateDate()), "yyyy.MM.dd hh:mm"));
                tvTime.setText(time);
                holder.itemView.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(orderBean.getOrderId()) && mListener != null) {
                        mListener.onOrderClick(orderBean.getOrderId());
                    }
                });
            } else if (itemViewType == 2) {
                OperateItem operateItem = (OperateItem) item;
                RecyclerView recyclerView = holder.obtainView(R.id.mOperateRv);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
                recyclerView.setAdapter(new OperateAdapter(mContext, operateItem.getListStatusBeans()));
            } else if (itemViewType == 3) {
                holder.setText(R.id.tv_follow_records_title, mContext.getString(R.string.followup_record));
            } else {
                final CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean listHistoryBean = (CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean) item;
                CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean = listHistoryBean.getHistory();
                CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean = mInfoListBean.getListHouseInfo();
                TextView tvFollowTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_follow_time);
                TextView tvFollower = holder.obtainView(R.id.tv_item_rv_house_manage_record_follower);
                TextView tvType = holder.obtainView(R.id.tv_item_rv_house_manage_record_type);
                TextView tvCancel = holder.obtainView(R.id.btn_item_rv_house_manage_record_cancel);
                View lineTop = holder.obtainView(R.id.view_item_rv_house_manage_record_top);
                View lineBottom = holder.obtainView(R.id.view_item_rv_house_manage_record_bottom);
                lineTop.setBackgroundResource(position == 0 ? R.color.bg_transparent : R.drawable.line_dash_vertical);
                lineBottom.setBackgroundResource(position == mDatas.size() - 1 ? R.color.bg_transparent : R.drawable.line_dash_vertical);
                tvFollowTime.setText(historyBean.getFileType());
                tvFollower.setText(getString(mContext.getString(R.string.followup_person_tips), historyBean.getUserId()));
                tvType.setText(getString(mContext.getString(R.string.followup_type_tips), historyBean.getFromStatusCode()));
                tvCancel.setOnClickListener(view -> {
                    if (mListener != null) {
                        mListener.onCanceled(historyBean, houseInfoBean);
                    }
                });
                TextView tvNote = null;
                TextView tvImgTime;
                TextView tvInstallerName;
                TextView tvInstaller;
                TextView tvInstallTime;
                TextView tvResult;
                TextView tvActualMeasurer;
                RecyclerView rvImg;
                switch (itemViewType) {
                    case 5:
                        TextView tvGuide = holder.obtainView(R.id.tv_item_rv_house_manage_record_designer);
                        tvGuide.setText(getString(mContext.getString(R.string.followup_distribution_guide), historyBean.getSalesId()));
                        tvCancel.setVisibility(View.GONE);
                        break;
                    case 6:
                        TextView tvWay = holder.obtainView(R.id.tv_item_rv_house_manage_record_add_record_way);
                        tvResult = holder.obtainView(R.id.tv_item_rv_house_manage_record_add_record_result);
                        TextView tvNextTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_add_record_next_time);
                        tvWay.setText(getString(mContext.getString(R.string.followup_linkup_tips), historyBean.getDesignerId()));
                        tvResult.setText(getString(mContext.getString(R.string.followup_linkup_result_tips), historyBean.getRemark()));
                        tvCancel.setVisibility(View.GONE);
                        if (TextUtils.isEmpty(historyBean.getNextFollowUpDate()))
                            tvNextTime.setVisibility(View.GONE);
                        else {
                            tvNextTime.setVisibility(View.VISIBLE);
                            tvNextTime.setText(getString(mContext.getString(R.string.followup_nextDate_tips), TimeUtil.stampToString(historyBean.getNextFollowUpDate(), "yyyy.MM.dd")));
                        }
                        break;
                    case 7:
                        TextView tvAmount = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_amount);
                        tvAmount.setText(getNumberString(mContext.getString(R.string.followup_deposit_collection_tips), historyBean.getDepositAmount()));
                        TextView tvTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_time);
                        tvTime.setText(getString(mContext.getString(R.string.followup_deposit_collectionTime_tips), TimeUtil.stampToString(historyBean.getNextFollowUpDate(), "yyyy.MM.dd")));
                        TextView tvPeople = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_people);
                        tvPeople.setText(getString(mContext.getString(R.string.followup_deposit_collectionPerson_tips), historyBean.getDesignerId()));
                        TextView tvRemark = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_remark);
                        tvRemark.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        tvRemark.setVisibility(TextUtils.isEmpty(historyBean.getRemark()) ? View.GONE : View.VISIBLE);
                        TextView tvCategory = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_custom_category);
                        tvCategory.setText(getString(mContext.getString(R.string.followup_custom_products), houseInfoBean.getEarnestHouseTypeName()));
                        rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                        showImg(mContext, rvImg, listHistoryBean.getUrl());
                        break;
                    case 8:
                        tvInstallTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_booking_measure_time);
                        TextView tvMeasurer = holder.obtainView(R.id.tv_item_rv_house_manage_record_booking_measure_measurer);
                        TextView tvRoom = holder.obtainView(R.id.tv_item_rv_house_manage_record_booking_measure_room);
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        tvInstallTime.setText(getString(mContext.getString(R.string.followup_appointment_time), TextUtils.isEmpty(houseInfoBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentTime(), "yyyy.MM.dd")));
                        tvMeasurer.setText(getString(mContext.getString(R.string.followup_room_surveyor), historyBean.getSalesId()));
                        tvRoom.setText(getString(mContext.getString(R.string.followup_room_space), historyBean.getDesignerId()));
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        break;
                    case 9:
                        TextView tvDesigner = holder.obtainView(R.id.tv_item_rv_house_manage_record_designer);
                        tvDesigner.setText(getString(mContext.getString(R.string.followup_distribution_designer), historyBean.getDesignerId()));
                        tvCancel.setVisibility(View.GONE);
                        break;
                    case 10:
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        TextView tvMeasureTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_measure_time);
                        tvImgTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_measure_img_time);
                        tvActualMeasurer = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_measurer);
                        rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                        tvMeasureTime.setVisibility(TextUtils.isEmpty(historyBean.getMeasureTime()) ? View.GONE : View.VISIBLE);
                        tvMeasureTime.setText(getString(mContext.getString(R.string.followup_real_measure_time), TextUtils.isEmpty(historyBean.getMeasureTime()) ? "" : TimeUtil.stampToString(historyBean.getMeasureTime(), "yyyy.MM.dd")));
                        tvActualMeasurer.setText(getString(mContext.getString(R.string.followup_actual_surveyor), historyBean.getDesignerId()));
                        if (TextUtils.isEmpty(historyBean.getAppointmentTime()))
                            tvImgTime.setVisibility(View.GONE);
                        tvImgTime.setText(getString(mContext.getString(R.string.followup_reservation_date), TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        showImg(mContext, rvImg, listHistoryBean.getUrl());
                        break;
                    case 11:
                        TextView tvUploadTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_plan_time);
                        TextView tvUploadName = holder.obtainView(R.id.tv_item_rv_house_manage_upload_name);
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        tvImgTime = holder.obtainView(R.id.tv_item_rv_house_manage_img_time);
                        tvUploadTime.setText(getString(mContext.getString(R.string.followup_upload_scheme_time), TextUtils.isEmpty(historyBean.getMeasureTime()) ? "" : TimeUtil.stampToString(historyBean.getMeasureTime(), "yyyy.MM.dd")));
                        rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        tvUploadName.setText(getString(mContext.getString(R.string.followup_upload_scheme_person), historyBean.getDesignerId()));
                        if (TextUtils.isEmpty(historyBean.getAppointmentTime()))
                            tvImgTime.setVisibility(View.GONE);
                        tvImgTime.setText(getString(mContext.getString(R.string.followup_reservation_date), TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
                        showImg(mContext, rvImg, listHistoryBean.getUrl());
                        break;
                    case 12:
                        tvResult = holder.obtainView(R.id.tv_item_rv_house_manage_record_shoper_check_result);
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        tvResult.setText(getString(mContext.getString(R.string.followup_house_result), (TextUtils.isEmpty(historyBean.getDesignerId()) ? mContext.getString(R.string.no_pass)
                                : TextUtils.equals(historyBean.getDesignerId(), "02") ? mContext.getString(R.string.pass) : mContext.getString(R.string.no_pass))));
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        break;
                    case 13:
                        TextView tvOrderMoney = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_order_amount);
                        TextView tvDeposit = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_deposit);
                        TextView tvCollect = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_collect);
                        TextView tvUncollect = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_uncollect);
                        TextView tvRemeasure = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_remeasure);
                        TextView tvBookingTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_booking_time);
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                        tvOrderMoney.setText(getNumberString(mContext.getString(R.string.followup_order_amount_tips), historyBean.getSalesAmount()));
                        tvDeposit.setText(getNumberString(mContext.getString(R.string.followup_deposit_received), historyBean.getSalesId()));
                        tvCollect.setText(getNumberString(mContext.getString(R.string.followup_payments_received), historyBean.getDepositAmount()));
//                            tvUncollect.setText("还欠尾款：￥" + houseInfoBean.getLastRemaining());
                        tvUncollect.setText(getNumberString(mContext.getString(R.string.followup_payment_arrears), historyBean.getDesignerId()));
                        String needRule = mContext.getString(R.string.followup_need_rule) + (TextUtils.isEmpty(houseInfoBean.getIsAfter()) ? mContext.getString(R.string.no_need) : TextUtils.equals(houseInfoBean.getIsAfter(), "1") ? mContext.getString(R.string.need) : mContext.getString(R.string.no_need));
                        tvRemeasure.setText(needRule);
                        String bookTime = mContext.getString(R.string.followup_reserved_install_date) + ((TextUtils.isEmpty(houseInfoBean.getAppointmentToInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentToInstallDate(), "yyyy.MM.dd")));
                        tvBookingTime.setText(bookTime);
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        showImg(mContext, rvImg, listHistoryBean.getUrl());
                        break;
                    case 14:
                        tvResult = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_remeasure_result);
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                        TextView people = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_remeasure_people);
                        TextView date = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_remeasure_date);
                        String ruleResult = mContext.getString(R.string.followup_reRule_result) + ((TextUtils.isEmpty(historyBean.getDesignAvailable()) ? mContext.getString(R.string.followup_reRule_unavailable) : TextUtils.equals(historyBean.getDesignAvailable(), "1") ? mContext.getString(R.string.followup_reRule_available) : mContext.getString(R.string.followup_reRule_unavailable)));
                        tvResult.setText(ruleResult);
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        people.setText(getString(mContext.getString(R.string.followup_reRule_person), historyBean.getDesignerId()));
                        date.setText(getString(mContext.getString(R.string.followup_reRule_date), TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
                        showImg(mContext, rvImg, listHistoryBean.getUrl());
                        break;
                    case 15:
                        break;
                    case 16:
                        TextView tvReason = holder.obtainView(R.id.tv_item_rv_house_manage_record_loosed_reason);
                        TextView tvGo = holder.obtainView(R.id.tv_item_rv_house_manage_record_loosed_go);
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        tvReason.setText(getString(mContext.getString(R.string.followup_lose_reason), historyBean.getLeaveReason()));
                        tvGo.setText(getString(mContext.getString(R.string.followup_lose_series), historyBean.getLeaveToSeries()));
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        break;
                    case 17:
                        break;
                    case 18:
                        tvInstallTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_money_collected);
                        tvInstaller = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_uncollect);
                        tvInstallTime.setText(getNumberString(mContext.getString(R.string.followup_final_payment), historyBean.getDepositAmount()));
                        tvInstaller.setText(getNumberString(mContext.getString(R.string.followup_payment_arrears), historyBean.getSalesAmount()));
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        showImg(mContext, rvImg, listHistoryBean.getUrl());
                        break;
                    case 19:
                        tvInstallTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_uninstall_time);
                        tvInstaller = holder.obtainView(R.id.tv_item_rv_house_manage_record_uninstall_installer);
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        TextView tvArea = holder.obtainView(R.id.tv_item_rv_house_manage_record_uninstall_area);
                        tvInstallTime.setText(getString(mContext.getString(R.string.followup_appointment_install_date), TextUtils.isEmpty(houseInfoBean.getAppointmentToInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentToInstallDate(), "yyyy.MM.dd")));
                        tvInstaller.setText(getString(mContext.getString(R.string.followup_installation_master), historyBean.getDesignerId()));
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        tvArea.setText(getString(mContext.getString(R.string.followup_installation_area), historyBean.getDepositAmount()));
                        break;
                    case 20:
                        TextView tvInstalled = holder.obtainView(R.id.tv_item_rv_house_manage_record_installed_time);
                        tvInstalled.setText(getString(mContext.getString(R.string.followup_installation_end_date), TextUtils.isEmpty(houseInfoBean.getEndingInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getEndingInstallDate(), "yyyy.MM.dd")));
                        tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                        tvInstallerName = holder.obtainView(R.id.tv_item_rv_house_manage_record_installer_name);
                        rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                        tvNote.setText(getString(mContext.getString(R.string.followup_remark_tips), historyBean.getRemark()));
                        tvInstallerName.setText(getString(mContext.getString(R.string.followup_installation_master), historyBean.getDesignerId()));
                        showImg(mContext, rvImg, listHistoryBean.getUrl());
                        break;
                }
                if (tvNote != null) {//备注没有，不显示
                    tvNote.setVisibility(TextUtils.isEmpty(historyBean.getRemark()) ? View.GONE : View.VISIBLE);
                }
            }
        }

        private String getString(String origin, String content) {
            return origin + (TextUtils.isEmpty(content) ? "" : content);
        }

        private String getNumberString(String origin, String content) {
            String str;
            try {
                str = TextUtils.isEmpty(content) ? "-" : NumberUtil.format(content);
            } catch (Exception e) {
                str = content;
            }
            return origin + str;
        }

        /*房屋列表适配器*/
        private class HouseListAdapter extends CommonAdapter<CustomerDetailBean.CustomerDetailInfoListBean> {

            HouseListAdapter(Context context, List<CustomerDetailBean.CustomerDetailInfoListBean> mDatas) {
                super(context, mDatas);
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, CustomerDetailBean.CustomerDetailInfoListBean bean, final int position) {
                TextView tvName = holder.obtainView(R.id.tv_item_rv_house_manage_house_name);
                String text = mContext.getString(R.string.house) + (position + 1);
                tvName.setText(text);
                if (mSelectPosition == position) {
                    tvName.setTextColor(ContextCompat.getColor(mContext, R.color.color_while));
                    tvName.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
                } else {
                    tvName.setTextColor(ContextCompat.getColor(mContext, R.color.textColor5));
                    tvName.setBackgroundResource(R.color.bg_transparent);
                }
                holder.itemView.setOnClickListener(v -> {
                    CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean info = bean.getListHouseInfo();
                    String houseId = info == null ? "" : info.getHouseId();
                    if (mListener != null) {
                        mListener.onHouseSelected(bean, houseId, position);
                    }
                    update(mContext, mDetailBean, position);
                });
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_house_manage_house;
            }

        }

        private class OperateAdapter extends CommonAdapter<CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean> {

            OperateAdapter(Context context, List<CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean> mDatas) {
                super(context, mDatas);
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, final CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean listStatusBean, int position) {
                ImageView ivIcon = holder.obtainView(R.id.iv_item_rv_house_manage_operate_icon);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_house_manage_operate_name);
                Glide.with(mContext).load(mDetailBean.getAppurl() + listStatusBean.getImg()).into(ivIcon);
                tvName.setText(listStatusBean.getCustomerStatusName());
                holder.itemView.setOnClickListener(view -> {
                    if (!NoDoubleClickUtil.isFastDoubleClick())
                        if (mListener != null) {
                            mListener.onOperateClick(TextUtils.equals(listStatusBean.getIsShow(), "1"),
                                    listStatusBean,
                                    mInfoListBean.getListHouseInfo(),
                                    mPersonalBean
                                    , position);
                        }
                });
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_house_manage_operate;
            }
        }

        private void showImg(final Context context, RecyclerView rv, final List<String> list) {
            if (list != null && list.size() > 0) {
                final List<String> imgUrls = new ArrayList<>();
                for (String imgUrl : list) {
                    imgUrls.add(mDetailBean.getUrl() + imgUrl);
                }
                rv.setLayoutManager(new GridLayoutManager(mContext, 3));
                rv.setNestedScrollingEnabled(false);
                rv.setAdapter(new CommonAdapter<String>(context, list) {
                    @Override
                    protected int bindView(int viewType) {
                        return R.layout.item_rv_house_manage_record_img;
                    }

                    @Override
                    public void onBindHolder(RecyclerHolder holder, String imgUrl, int position) {
                        ImageView iv = holder.obtainView(R.id.iv_item_rv_house_manage_record_img);
                        Glide.with(context).load(mDetailBean.getUrl() + imgUrl).apply(new RequestOptions().placeholder(R.drawable.loading_photo)).into(iv);
                        iv.setOnClickListener(v -> PhotoViewActivity.openImage((Activity) context, position, imgUrls));
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            MultiItem item = mDatas.get(position);
            int itemType = item.getItemType();
            if (itemType == 4) {
                CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean bean = (CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean) item;
                CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean = bean.getHistory();
                if (historyBean == null) {
                    return 5;
                }
                return getType(bean.getHistory());
            }
            return mDatas.get(position).getItemType();
        }

        private int getType(CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean) {
            int itemType = 99;
            if (historyBean == null || TextUtils.isEmpty(historyBean.getOperateCode()))
                return itemType;
            switch (historyBean.getOperateCode()) {
                case "01":
                    itemType = 5;
                    break;
                case "02":
                    itemType = 6;
                    break;
                case "03":
                    itemType = 7;
                    break;
                case "04":
                    itemType = 8;
                    break;
                case "05":
                    itemType = 9;
                    break;
                case "06":
                    itemType = 10;
                    break;
                case "07":
                    itemType = 11;
                    break;
                case "08":
                    itemType = 12;
                    break;
                case "09":
                    itemType = 13;
                    break;
                case "10":
                    itemType = 14;
                    break;
                case "11":
                    itemType = 99;
                    break;
                case "12":
                    itemType = 16;
                    break;
                case "13":
                    itemType = 99;
                    break;
                case "14":
                    itemType = 18;
                    break;
                case "15":
                    itemType = 19;
                    break;
                case "16":
                    itemType = 20;
                    break;
                default:
                    itemType = 99;
                    break;
            }
            return itemType;
        }

        @Override
        protected int bindView(int viewType) {
            int layoutId;
            switch (viewType) {
                case 0:
                    layoutId = R.layout.item_customer_detail_top;
                    break;
                case 1:
                    layoutId = R.layout.item_rv_house_manage_order;
                    break;
                case 2:
                    layoutId = R.layout.item_customer_detail_operate;
                    break;
                case 3:
                    layoutId = R.layout.item_customer_detail_followtips;
                    break;
                case 5:
                    layoutId = R.layout.item_rv_house_manage_record_divied_designer2;
                    break;
                case 6:
                    layoutId = R.layout.item_rv_house_manage_record_add_record2;
                    break;
                case 7:
                    layoutId = R.layout.item_rv_house_manage_record_collect_deposit2;
                    break;
                case 8:
                    layoutId = R.layout.item_rv_house_manage_record_booking_measure2;
                    break;
                case 9:
                    layoutId = R.layout.item_rv_house_manage_record_divied_designer2;
                    break;
                case 10:
                    layoutId = R.layout.item_rv_house_manage_record_upload_measure2;
                    break;
                case 11:
                    layoutId = R.layout.item_rv_house_manage_record_upload_plan2;
                    break;
                case 12:
                    layoutId = R.layout.item_rv_house_manage_record_shoper_check2;
                    break;
                case 13:
                    layoutId = R.layout.item_rv_house_manage_record_sign2;
                    break;
                case 14:
                    layoutId = R.layout.item_rv_house_manage_record_upload_remeasure2;
                    break;
                case 15:
                    layoutId = R.layout.item_rv_undefine;
                    break;
                case 16:
                    layoutId = R.layout.item_rv_house_manage_record_lossed2;
                    break;
                case 17:
                    layoutId = R.layout.item_rv_undefine;
                    break;
                case 18:
                    layoutId = R.layout.item_rv_house_manage_record_collect_money2;
                    break;
                case 19:
                    layoutId = R.layout.item_rv_house_manage_record_uninstall2;
                    break;
                case 20:
                    layoutId = R.layout.item_rv_house_manage_record_installed2;
                    break;
                default:
                    layoutId = R.layout.item_rv_undefine;
                    break;
            }
            return layoutId;
        }

    }

    /**
     * 获取客户详情
     */
    public void getData(String personalId) {
        model.getData(personalId, new CustomerDetailModel.GetDataListener() {
            @Override
            public void success(CustomerDetailBean customerDetailBean) {
                if (getView() != null)
                    getView().getCustomerSuccess(customerDetailBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getCustomerFailed(failed);
            }
        });
    }

    public Fragment operate(Context context, String customerStatus) {
        Fragment fragment = null;
        customerStatus = TextUtils.isEmpty(customerStatus) ? "" : customerStatus;
        switch (customerStatus) {
            case "01":
                MobclickAgent.onEvent(context, "customerdetails_divide_guide");
                fragment = new DividedGuideFragment();
                break;
            case "02":
                MobclickAgent.onEvent(context, "customerdetails_add_record");
                fragment = new AddRecordFragment();
                break;
            case "03":
                MobclickAgent.onEvent(context, "customerdetails_collect_deposit");
                fragment = new CollectDepositFragment();
                break;
            case "04":
                MobclickAgent.onEvent(context, "customerdetails_booking_measure");
                fragment = new BookingMeasureFragment();
                break;
            case "05":
                MobclickAgent.onEvent(context, "customerdetails_divide_designer");
                fragment = new DivideDesignerFragment();
                break;
            case "06":
                MobclickAgent.onEvent(context, "customerdetails_upload_measure");
                fragment = new UploadMeasureFragment();
                break;
            case "07":
                MobclickAgent.onEvent(context, "customerdetails_upload_plan");
                fragment = new UploadPlanFragment();
                break;
            case "08":
                MobclickAgent.onEvent(context, "customerdetails_shoper_check");
                fragment = new ShoperCheckFragment();
                break;
            case "09":
                MobclickAgent.onEvent(context, "customerdetails_sign");
                fragment = new SignedFragment();
                break;
            case "10":
                MobclickAgent.onEvent(context, "customerdetails_upload_remeasure");
                fragment = new UploadReMeasureFragment();
                break;
            case "11":
                break;
            case "12":
                MobclickAgent.onEvent(context, "customerdetails_loosed");
                fragment = new LossedFragment();
                break;
            case "13":
                break;
            case "14":
                MobclickAgent.onEvent(context, "customerdetails_collect_money");
                fragment = new CollectMoneyFragment();
                break;
            case "15":
                MobclickAgent.onEvent(context, "customerdetails_booking_install");
                fragment = new BookingInstallFragment();
                break;
            case "16":
                MobclickAgent.onEvent(context, "customerdetails_installed");
                fragment = new InstalledFragment();
                break;
        }
        return fragment;
    }

    /*统计撤销操作点击数*/
    public void onEvent(Context context, String operateCode) {
        if (TextUtils.isEmpty(operateCode)) return;
        switch (operateCode) {
            case "04":
                MobclickAgent.onEvent(context, "customerdetails_cancel_booking_measure");
                break;
            case "06":
                MobclickAgent.onEvent(context, "customerdetails_cancel_measure");
                break;
            case "07":
                MobclickAgent.onEvent(context, "customerdetails_cancel_upload_plan");
                break;
            case "09":
                MobclickAgent.onEvent(context, "customerdetails_cancel_sign");
                break;
            case "10":
                MobclickAgent.onEvent(context, "customerdetails_cancel_remeasure_result");
                break;
            case "15":
                MobclickAgent.onEvent(context, "customerdetails_cancel_booking_install");
                break;
            case "16":
                MobclickAgent.onEvent(context, "customerdetails_cancel_installed");
                break;
            case "12":
                MobclickAgent.onEvent(context, "customerdetails_cancel_loosed");
                break;
        }
    }

    /**
     * 撤销
     */
    public void revoke(String cancelReason, String customerStatus, String houseId, String optCode, final RevokeCallback callback) {
        model.revoke(cancelReason, customerStatus, houseId, optCode, new CustomerDetailModel.RevokeListener() {
            @Override
            public void success(String success) {
                callback.onRevokeSuccess(success);
            }

            @Override
            public void failed(String failed) {
                callback.onRevokeFailed(failed);
            }
        });
    }

    public interface RevokeCallback {
        void onRevokeSuccess(String success);

        void onRevokeFailed(String failed);
    }
}
