package com.holike.crm.presenter.activity;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CustomerStateListBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.model.activity.CustomerStateListModel;
import com.holike.crm.view.activity.CustomerStateListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/8/22.
 * 客户状态列表
 */

public class CustomerStateListPresenter extends BasePresenter<CustomerStateListView, CustomerStateListModel> {

    private CustomerListAdapter mAdapter;
    private List<MultiItem> mListBeans = new ArrayList<>();
    private NoMoreBean mNoMoreBean = new NoMoreBean();

    private int mItemType = -1;
    private int mLayoutResId;

    public class CustomerListAdapter extends CommonAdapter<MultiItem> {

        CustomerListAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == 1) {
                return mLayoutResId;
            }
            return R.layout.item_nomore_data;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == 1) {
                final CustomerStateListBean.DateBean dateBean = (CustomerStateListBean.DateBean) multiItem;
                TextView tvName = holder.obtainView(R.id.tv_item_rv_customer_state_list_name);
                TextView tvPhone = holder.obtainView(R.id.tv_item_rv_customer_state_list_phone);
                TextView tvAddress = holder.obtainView(R.id.tv_item_rv_customer_state_list_address);
                TextView tvCustomerType = holder.obtainView(R.id.tv_item_rv_customer_state_list_customer_type);
                tvName.setText(dateBean.getUserName());
                tvPhone.setText(dateBean.getPhone());
                tvAddress.setText(dateBean.getAdress());
                tvCustomerType.setText(dateBean.getSourceName());
                LinearLayout llGuide;
                TextView tvDesigner;
                TextView tvGuide;
                TextView tvSigneTime;
                TextView tvState;
                TextView tvSigneMoney;
                TextView tvUnCollect;
                TextView tvBookingInstall;
                TextView tvOrderTime;
                if (mItemType == 1) { //意向客户跟进
                    LinearLayout llNextTime = holder.obtainView(R.id.ll_item_rv_customer_state_list_intention_next_time);
                    TextView tvCreateTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_intention_create_time);
                    TextView tvFollowTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_intention_follow_time);
                    tvGuide = holder.obtainView(R.id.tv_item_rv_customer_state_list_intention_guide);
                    TextView tvNextTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_intention_next_time);
                    tvGuide.setVisibility(dateBean.getShow().equals("1") ? View.VISIBLE : View.GONE);
                    tvNextTime.setVisibility(TextUtils.isEmpty(dateBean.getNextDate()) ? View.GONE : View.VISIBLE);
                    String createDate = "创建时间：";
                    if (!TextUtils.isEmpty(dateBean.getCreateDate()))
                        createDate += dateBean.getCreateDate();
                    tvCreateTime.setText(createDate);
                    String updateDate = "最新跟进：";
                    if (!TextUtils.isEmpty(dateBean.getUpdateDate()))
                        updateDate += dateBean.getUpdateDate();
                    tvFollowTime.setText(updateDate);
                    String nextDate = "下次跟进：";
                    if (!TextUtils.isEmpty(dateBean.getNextDate()))
                        nextDate += dateBean.getNextDate();
                    tvNextTime.setText(nextDate);
                    String houseScale = "导购：";
                    if (!TextUtils.isEmpty(dateBean.getHouseSaleId()))
                        houseScale += dateBean.getHouseSaleId();
                    tvGuide.setText(houseScale);
                    llNextTime.setVisibility((TextUtils.isEmpty(dateBean.getNextDate()) && !dateBean.getShow().equals("1")) ? View.GONE : View.VISIBLE);
                } else if (mItemType == 2) { //待量房客户
                    TextView tvTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_measure_time);
                    TextView tvMan = holder.obtainView(R.id.tv_item_rv_customer_state_list_measure_man);
                    String appointmentTime = "预约量房日期：";
                    if (!TextUtils.isEmpty(dateBean.getAppointmentTime()))
                        appointmentTime += dateBean.getAppointmentTime();
                    tvTime.setText(appointmentTime);
                    String scale = "量房人员：";
                    if (!TextUtils.isEmpty(dateBean.getSaleId())) scale += dateBean.getSaleId();
                    tvMan.setText(scale);
                } else if (mItemType == 3) { //待出图客户
                    LinearLayout layout = holder.obtainView(R.id.ll_tip_red);
                    TextView tvMeasureTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_out_diagram_measure_time);
                    TextView tvImgTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_out_diagram_img_time);
                    TextView tvMeasurer = holder.obtainView(R.id.tv_item_rv_customer_state_list_out_diagram_measurer);
                    tvImgTime.setVisibility(TextUtils.isEmpty(dateBean.getBookOrderDate()) ? View.GONE : View.VISIBLE);
                    String bookOrderDate = "预约确图：";
                    if (!TextUtils.isEmpty(dateBean.getBookOrderDate()))
                        bookOrderDate += dateBean.getBookOrderDate();
                    tvImgTime.setText(bookOrderDate);
                    layout.setBackground(dateBean.getIsShow().equals("1") ? ContextCompat.getDrawable(mContext, R.drawable.bg_tip_red) : null);
                    String measureTime = "量房日期：";
                    if (!TextUtils.isEmpty(dateBean.getMeasureTime()))
                        measureTime += dateBean.getMeasureTime();
                    tvMeasureTime.setText(measureTime);
                    String user = "量房人员：";
                    if (!TextUtils.isEmpty(dateBean.getUserId()))
                        user += dateBean.getUserId();
                    tvMeasurer.setText(user);
                } else if (mItemType == 4) { // 待签约客户
                    tvDesigner = holder.obtainView(R.id.tv_item_rv_customer_state_list_unsigne_designer);
                    TextView tvUploadPlan = holder.obtainView(R.id.tv_item_rv_customer_state_list_unsigne_upload_plan);
                    TextView tvImg = holder.obtainView(R.id.tv_item_rv_customer_state_list_unsigne_img);
                    tvImg.setVisibility(TextUtils.isEmpty(dateBean.getBookOrderDate()) ? View.GONE : View.VISIBLE);
                    String designer = "设计师：";
                    if (!TextUtils.isEmpty(dateBean.getDesignerId()))
                        designer += dateBean.getDesignerId();
                    tvDesigner.setText(designer);
                    String optTime = "上传方案：";
                    if (!TextUtils.isEmpty(dateBean.getOptTime()))
                        optTime += dateBean.getOptTime();
                    tvUploadPlan.setText(optTime);
                    String bookOrderDate = "预约确图：";
                    if (!TextUtils.isEmpty(bookOrderDate))
                        bookOrderDate += dateBean.getBookOrderDate();
                    tvImg.setText(bookOrderDate);
                } else if (mItemType == 5) { //待收全款客户
                    llGuide = holder.obtainView(R.id.ll_item_rv_customer_state_list_collect_money_guide);
                    tvSigneTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_collect_money_signe_time);
                    tvState = holder.obtainView(R.id.tv_item_rv_customer_state_list_collect_money_state);
                    tvSigneMoney = holder.obtainView(R.id.tv_item_rv_customer_state_list_collect_money_signe_money);
                    tvUnCollect = holder.obtainView(R.id.tv_item_rv_customer_state_list_collect_money_uncollect);
                    tvDesigner = holder.obtainView(R.id.tv_item_rv_customer_state_list_collect_money_designer);
                    tvGuide = holder.obtainView(R.id.tv_item_rv_customer_state_list_collect_money_guide);
                    llGuide.setVisibility(dateBean.getShow().equals("1") ? View.VISIBLE : View.GONE);
                    String optTime = "签约时间：";
                    if (!TextUtils.isEmpty(dateBean.getOptTime()))
                        optTime += dateBean.getOptTime();
                    tvSigneTime.setText(optTime);
                    String statusMove = "客户状态：";
                    if (!TextUtils.isEmpty(dateBean.getStatuMove()))
                        statusMove += dateBean.getStatuMove();
                    tvState.setText(statusMove);
                    String singeMoney = "签约金额：";
                    if (!TextUtils.isEmpty(dateBean.getHouseSaleAmount())) {
                        singeMoney += dateBean.getHouseSaleAmount().replace(".00", "");
                    }
                    tvSigneMoney.setText(singeMoney);
                    String lastRarin = "未收尾款：";
                    if (!TextUtils.isEmpty(dateBean.getLastRarin())) {
                        lastRarin += dateBean.getLastRarin().replace(".00", "");
                    }
                    tvUnCollect.setText(lastRarin);
                    String designer = "设计师：";
                    if (!TextUtils.isEmpty(dateBean.getDesignerId()))
                        designer += dateBean.getDesignerId();
                    tvDesigner.setText(designer);
                    String houseScale = "导购：";
                    if (!TextUtils.isEmpty(dateBean.getHouseSaleId()))
                        houseScale += dateBean.getHouseSaleId();
                    tvGuide.setText(houseScale);
                } else if (mItemType == 6) { //待复尺客户
                    llGuide = holder.obtainView(R.id.ll_item_rv_customer_state_list_remeasure_guide);
                    tvSigneTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_remeasure_signe_time);
                    tvBookingInstall = holder.obtainView(R.id.tv_item_rv_customer_state_list_remeasure_booking_sintall);
                    tvGuide = holder.obtainView(R.id.tv_item_rv_customer_state_list_remeasure_guide);
                    tvDesigner = holder.obtainView(R.id.tv_item_rv_customer_state_list_remeasure_designer);
                    llGuide.setVisibility(dateBean.getShow().equals("1") ? View.VISIBLE : View.GONE);
                    String signTime = "签约日期：";
                    if (!TextUtils.isEmpty(dateBean.getOptTime()))
                        signTime += dateBean.getOptTime();
                    tvSigneTime.setText(signTime);
                    String appointment = "预约安装：";
                    if (!TextUtils.isEmpty(dateBean.getAppointmentToInstallDate()))
                        appointment += dateBean.getAppointmentToInstallDate();
                    tvBookingInstall.setText(appointment);
                    String designer = "设计师：";
                    if (!TextUtils.isEmpty(dateBean.getDesignerId()))
                        designer += dateBean.getDesignerId();
                    tvDesigner.setText(designer);
                    String scale = "导购：";
                    if (!TextUtils.isEmpty(dateBean.getSaleId()))
                        scale += dateBean.getSaleId();
                    tvGuide.setText(scale);
                } else if (mItemType == 7) { //待下单客户
                    llGuide = holder.obtainView(R.id.ll_item_rv_customer_state_list_order_guide);
                    tvSigneTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_order_signe_time);
                    tvBookingInstall = holder.obtainView(R.id.tv_item_rv_customer_state_list_order_booking_install);
                    tvGuide = holder.obtainView(R.id.tv_item_rv_customer_state_list_order_guide);
                    tvDesigner = holder.obtainView(R.id.tv_item_rv_customer_state_list_order_designer);
                    llGuide.setVisibility(dateBean.getShow().equals("1") ? View.VISIBLE : View.GONE);
                    String signTime = "签约日期：";
                    if (!TextUtils.isEmpty(dateBean.getOptTime()))
                        signTime += dateBean.getOptTime();
                    tvSigneTime.setText(signTime);
                    String appointment = "预约安装：";
                    if (!TextUtils.isEmpty(dateBean.getAppointmentToInstallDate()))
                        appointment += dateBean.getAppointmentToInstallDate();
                    tvBookingInstall.setText(appointment);
                    String designer = "设计师：";
                    if (!TextUtils.isEmpty(dateBean.getDesignerId()))
                        designer += dateBean.getDesignerId();
                    tvDesigner.setText(designer);
                    String saleId = "导购：";
                    if (!TextUtils.isEmpty(dateBean.getSaleId()))
                        saleId += dateBean.getSaleId();
                    tvGuide.setText(saleId);
                } else if (mItemType == 8) { //待安装客户
                    LinearLayout llLayout = holder.obtainView(R.id.ll_tip_red_uninstall);
                    tvSigneTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_uninstall_signe_time);
                    tvOrderTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_uninstall_order_time);
                    tvGuide = holder.obtainView(R.id.tv_item_rv_customer_state_list_uninstall_guide);
                    tvDesigner = holder.obtainView(R.id.tv_item_rv_customer_state_list_uninstall_designer);
                    TextView tvBook = holder.obtainView(R.id.tv_item_rv_customer_state_list_uninstall_booking);
                    TextView tvInstaller = holder.obtainView(R.id.tv_item_rv_customer_state_list_uninstall_installer);
                    String signTime = "确图签约：";
                    if (!TextUtils.isEmpty(dateBean.getOptTime()))
                        signTime += dateBean.getOptTime();
                    tvSigneTime.setText(signTime);
                    String orderDate = "下单日期：";
                    if (!TextUtils.isEmpty(dateBean.getOrderDate()))
                        orderDate += dateBean.getOrderDate();
                    tvOrderTime.setText(orderDate);
                    String designer = "设计师：";
                    if (!TextUtils.isEmpty(dateBean.getDesignerId()))
                        designer += dateBean.getDesignerId();
                    tvDesigner.setText(designer);
                    String saleId = "导购：";
                    if (!TextUtils.isEmpty(dateBean.getSaleId()))
                        saleId += dateBean.getSaleId();
                    tvGuide.setText(saleId);
                    String appointment = "预约安装：";
                    if (!TextUtils.isEmpty(dateBean.getAppointmentToInstallDate()))
                        appointment += dateBean.getAppointmentToInstallDate();
                    tvBook.setText(appointment);
                    llLayout.setBackground(dateBean.getIsShow().equals("1") ? ContextCompat.getDrawable(mContext, R.drawable.bg_tip_red) : null);
                    tvInstaller.setText(TextUtils.isEmpty(dateBean.getUserId()) ? "安装师:未派单" : "安装师：" + dateBean.getUserId());
                } else if (mItemType == 9) { //已安装客户
                    tvSigneTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_installed_signe_time);
                    tvOrderTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_installed_order_time);
                    TextView tvInstallTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_installed_install_time);
                    TextView tvInstaller = holder.obtainView(R.id.tv_item_rv_customer_state_list_installed_installer);
                    String optTime = "确图签约：";
                    if (!TextUtils.isEmpty(dateBean.getOptTime()))
                        optTime += dateBean.getOptTime();
                    tvSigneTime.setText(optTime);
                    String orderDate = "下单日期：";
                    if (!TextUtils.isEmpty(orderDate))
                        orderDate += dateBean.getOrderDate();
                    tvOrderTime.setText(orderDate);
                    String endingInstall = "安装日期：";
                    if (!TextUtils.isEmpty(dateBean.getEndingInstallDate()))
                        endingInstall += dateBean.getEndingInstallDate();
                    tvInstallTime.setText(endingInstall);
                    String user = "安装师傅：";
                    if (!TextUtils.isEmpty(dateBean.getUserId()))
                        user += dateBean.getUserId();
                    tvInstaller.setText(user);
                } else if (mItemType == 10) { //已流失客户
                    TextView tvLossTime = holder.obtainView(R.id.tv_item_rv_customer_state_list_lossed_time);
                    TextView tvReason = holder.obtainView(R.id.tv_item_rv_customer_state_list_lossed_reason);
                    String optTime = "确认流失：";
                    if (!TextUtils.isEmpty(dateBean.getOptTime()))
                        optTime += dateBean.getOptTime();
                    tvLossTime.setText(optTime);
                    String leaveResone = "流失原因：";
                    if (!TextUtils.isEmpty(dateBean.getLeaveReason()))
                        leaveResone += dateBean.getLeaveReason();
                    tvReason.setText(leaveResone);
                }
                tvPhone.setOnClickListener(v -> ((BaseActivity) mContext).call(dateBean.getPhone()));
                holder.itemView.setOnClickListener(v -> CustomerDetailV2Activity.open((BaseActivity) mContext, dateBean.getPersonalId(), dateBean.getHouseId()));
            } else {
                holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
            }
        }
    }

    public void setAdapter(RecyclerView recyclerView, String stateName) {
        if (TextUtils.equals(stateName, "意向客户跟进")) {
            mItemType = 1;
            mLayoutResId = R.layout.item_rv_customer_state_list_intention;
        } else if (TextUtils.equals(stateName, "待量房客户")) {
            mItemType = 2;
            mLayoutResId = R.layout.item_rv_customer_state_list_measure;
        } else if (TextUtils.equals(stateName, "待出图客户")) {
            mItemType = 3;
            mLayoutResId = R.layout.item_rv_customer_state_list_out_diagram;
        } else if (TextUtils.equals(stateName, "待签约客户")) {
            mItemType = 4;
            mLayoutResId = R.layout.item_rv_customer_state_list_unsigne;
        } else if (TextUtils.equals(stateName, "待收全款客户")) {
            mItemType = 5;
            mLayoutResId = R.layout.item_rv_customer_state_list_collect_money;
        } else if (TextUtils.equals(stateName, "待复尺客户")) {
            mItemType = 6;
            mLayoutResId = R.layout.item_rv_customer_state_list_remeasure;
        } else if (TextUtils.equals(stateName, "待下单客户")) {
            mItemType = 7;
            mLayoutResId = R.layout.item_rv_customer_state_list_order;
        } else if (TextUtils.equals(stateName, "待安装客户")) {
            mItemType = 8;
            mLayoutResId = R.layout.item_rv_customer_state_list_uninstall;
        } else if (TextUtils.equals(stateName, "已安装客户")) {
            mItemType = 9;
            mLayoutResId = R.layout.item_rv_customer_state_list_installed;
        } else if (TextUtils.equals(stateName, "已流失客户")) {
            mItemType = 10;
            mLayoutResId = R.layout.item_rv_customer_state_list_lossed;
        }
        if (mItemType != -1) {
            mAdapter = new CustomerListAdapter(recyclerView.getContext(), mListBeans);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void onRefreshCompleted(List<CustomerStateListBean.DateBean> beans) {
        this.mListBeans.clear();
        onLoadMoreCompleted(beans);
    }

    public void onLoadMoreCompleted(List<CustomerStateListBean.DateBean> beans) {
        this.mListBeans.addAll(beans);
        notifyDataSetChanged();
    }

    public void noMoreData() {
        this.mListBeans.remove(mNoMoreBean);
        mListBeans.add(mNoMoreBean);
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void getDate(String stateName, int pageNo, int pageSize) {
        String statusMove = "";
        switch (stateName) {
            case "意向客户跟进":
                statusMove = "01";
                break;
            case "待量房客户":
                statusMove = "02";
                break;
            case "待出图客户":
                statusMove = "03";
                break;
            case "待签约客户":
                statusMove = "04";
                break;
            case "待收全款客户":
                statusMove = "09";
                break;
            case "待复尺客户":
                statusMove = "10";
                break;
            case "待下单客户":
                statusMove = "11";
                break;
            case "待安装客户":
                statusMove = "06";
                break;
            case "已安装客户":
                statusMove = "07";
                break;
            case "已流失客户":
                statusMove = "08";
                break;
        }
        pageNo = pageNo < 1 ? 1 : pageNo;
        model.getDate(statusMove, String.valueOf(pageNo), String.valueOf(pageSize), new CustomerStateListModel.GetDateListener() {
            @Override
            public void success(CustomerStateListBean bean) {
                if (getView() != null)
                    getView().success(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }
}
