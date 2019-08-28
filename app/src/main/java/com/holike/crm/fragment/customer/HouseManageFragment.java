package com.holike.crm.fragment.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.dialog.CancelDialog;
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
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.util.Constants;
import com.holike.crm.util.NoDoubleClickUtil;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WorkflowView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/7/30.
 * 单个房屋信息
 */
@Deprecated
public class HouseManageFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_house_manage_address)
    TextView tvAddress;
    @BindView(R.id.tv_house_manage_state)
    TextView tvState;
    @BindView(R.id.rv_house_manage_order)
    RecyclerView rvOrder;
    @BindView(R.id.rv_house_manage_operate)
    RecyclerView rvOperate;
    @BindView(R.id.rv_house_manage_record)
    RecyclerView rvRecord;
    @BindView(R.id.tv_follow_records_title)
    TextView tvHistoryTitle;
    @BindView(R.id.v_follow_line)
    View vFollowLine;

    private CustomerDetailBean.CustomerDetailInfoListBean infoListBean;
    private CustomerDetailBean.PersonalBean personal;
    private String url, appurl;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_house_manage;
    }

    @Override
    protected void init() {
        super.init();
        rvOrder.setLayoutManager(new LinearLayoutManager(mContext));
        rvOperate.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvRecord.setLayoutManager(new LinearLayoutManager(mContext));
        rvOrder.setNestedScrollingEnabled(false);
        rvOperate.setNestedScrollingEnabled(false);
        rvRecord.setNestedScrollingEnabled(false);
        Bundle bundle = getArguments();
        infoListBean = (CustomerDetailBean.CustomerDetailInfoListBean) bundle.getSerializable(Constants.HOUSE_BEAN);
        personal = (CustomerDetailBean.PersonalBean) bundle.getSerializable(Constants.PERSONAL_BEAN);
        url = bundle.getString(Constants.HOUSE_BEAN_URL, "");
        appurl = bundle.getString(Constants.HOUSE_BEAN_APP_URL, "");
        showText();
        showOrderList(infoListBean);
        showOperateList(infoListBean);
        showHistory(infoListBean.getListHistory());
    }

    private void showText() {
        String address = "";
        CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean infoBean = infoListBean.getListHouseInfo();
        if (infoBean != null) {
            if (!TextUtils.isEmpty(infoBean.getBuildingName())) {
                address += infoBean.getBuildingName();
            }
            if (!TextUtils.isEmpty(infoBean.getBuildingNumber())) {
                address += infoBean.getBuildingNumber();
            }
        }
        tvAddress.setText(address);
        String currentState = mContext.getString(R.string.current_state_tips);
        if (infoBean != null && !TextUtils.isEmpty(infoBean.getStatusMove())) {
            currentState += infoBean.getStatusMove();
        }
        tvState.setText(currentState);
    }

    /**
     * 显示订单列表
     *
     * @param bean
     */
    private void showOrderList(final CustomerDetailBean.CustomerDetailInfoListBean bean) {
        if (bean != null && bean.getListHouseSpace() != null && bean.getListHouseSpace().size() > 0) {
            rvOrder.setAdapter(new CommonAdapter<CustomerDetailBean.CustomerDetailInfoListBean.ListHouseSpaceBean>(mContext, bean.getListHouseSpace()) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_house_manage_order;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, CustomerDetailBean.CustomerDetailInfoListBean.ListHouseSpaceBean listHouseSpaceBean, int position) {
                    TextView tvNum = holder.obtainView(R.id.tv_item_rv_house_manage_order_num);
                    TextView tvState = holder.obtainView(R.id.tv_item_rv_house_manage_order_state);
                    TextView tvName = holder.obtainView(R.id.tv_item_rv_house_manage_order_name);
                    TextView tvTime = holder.obtainView(R.id.tv_item_rv_house_manage_order_time);
                    String orderId = "订单号：" + (TextUtils.isEmpty(listHouseSpaceBean.getOrderId()) ? "" : listHouseSpaceBean.getOrderId());
                    tvNum.setText(orderId);
                    String state = "订单状态：" + (TextUtils.isEmpty(listHouseSpaceBean.getHouseName()) ? "" : listHouseSpaceBean.getHouseName());
                    tvState.setText(state);
                    String creator = "下单人：" + (TextUtils.isEmpty(listHouseSpaceBean.getCreater()) ? "" : listHouseSpaceBean.getCreater());
                    tvName.setText(creator);
                    String time = "下单时间：" + (TextUtils.isEmpty(listHouseSpaceBean.getCreateDate()) ? "" : TimeUtil.stampToString(String.valueOf(listHouseSpaceBean.getCreateDate()), "yyyy.MM.dd hh:mm"));
                    tvTime.setText(time);
                    holder.itemView.setOnClickListener(v -> {
                        if (!TextUtils.isEmpty(listHouseSpaceBean.getOrderId())) {
                            OrderDetailsActivity.open(getFragment(),listHouseSpaceBean.getOrderId());
//                            NotifyFragment.startOrderDetails(getFragment(), listHouseSpaceBean.getOrderId(), null, REQUEST_CODE);
                        }
                    });
                }
            });
        }
    }

    /**
     * 显示操作列表
     */
    public void showOperateList(final CustomerDetailBean.CustomerDetailInfoListBean bean) {
        if (bean != null && bean.getListStatus() != null && bean.getListStatus().size() > 0) {
            rvOperate.setAdapter(new CommonAdapter<CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean>(mContext, bean.getListStatus()) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_house_manage_operate;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean listStatusBean, int position) {
                    ImageView ivIcon = holder.obtainView(R.id.iv_item_rv_house_manage_operate_icon);
                    TextView tvName = holder.obtainView(R.id.tv_item_rv_house_manage_operate_name);
                    Glide.with(mContext).load(appurl + listStatusBean.getImg()).into(ivIcon);
                    tvName.setText(listStatusBean.getCustomerStatusName());
                    holder.itemView.setOnClickListener(v -> {
                        if (!NoDoubleClickUtil.isFastDoubleClick())
                            if (listStatusBean.getIsShow().equals("0")) {
                                Map<String, Serializable> params = new HashMap<>();
                                params.put(Constants.CUSTOMER_STATUS, listStatusBean);
                                params.put(Constants.HOUSE_INFO, bean.getListHouseInfo());
                                params.put(Constants.PERSONAL_BEAN, personal);
                                switch (listStatusBean.getCustomerStatus()) {
                                    case "01":
                                        MobclickAgent.onEvent(mContext, "customerdetails_divide_guide");
                                        startFragment(params, new DividedGuideFragment());
                                        break;
                                    case "02":
                                        MobclickAgent.onEvent(mContext, "customerdetails_add_record");
                                        startFragment(params, new AddRecordFragment());
                                        break;
                                    case "03":
                                        MobclickAgent.onEvent(mContext, "customerdetails_collect_deposit");
                                        startFragment(params, new CollectDepositFragment());
                                        break;
                                    case "04":
                                        MobclickAgent.onEvent(mContext, "customerdetails_booking_measure");
                                        startFragment(params, new BookingMeasureFragment());
                                        break;
                                    case "05":
                                        MobclickAgent.onEvent(mContext, "customerdetails_divide_designer");
                                        startFragment(params, new DivideDesignerFragment());
                                        break;
                                    case "06":
                                        MobclickAgent.onEvent(mContext, "customerdetails_upload_measure");
                                        startFragment(params, new UploadMeasureFragment());
                                        break;
                                    case "07":
                                        MobclickAgent.onEvent(mContext, "customerdetails_upload_plan");
                                        startFragment(params, new UploadPlanFragment());
                                        break;
                                    case "08":
                                        MobclickAgent.onEvent(mContext, "customerdetails_shoper_check");
                                        startFragment(params, new ShoperCheckFragment());
                                        break;
                                    case "09":
                                        MobclickAgent.onEvent(mContext, "customerdetails_sign");
                                        startFragment(params, new SignedFragment());
                                        break;
                                    case "10":
                                        MobclickAgent.onEvent(mContext, "customerdetails_upload_remeasure");
                                        startFragment(params, new UploadReMeasureFragment());
                                        break;
                                    case "11":
                                        break;
                                    case "12":
                                        MobclickAgent.onEvent(mContext, "customerdetails_loosed");
                                        startFragment(params, new LossedFragment());
                                        break;
                                    case "13":
                                        break;
                                    case "14":
                                        MobclickAgent.onEvent(mContext, "customerdetails_collect_money");
                                        startFragment(params, new CollectMoneyFragment());
                                        break;
                                    case "15":
                                        MobclickAgent.onEvent(mContext, "customerdetails_booking_install");
                                        startFragment(params, new BookingInstallFragment());
                                        break;
                                    case "16":
                                        MobclickAgent.onEvent(mContext, "customerdetails_installed");
                                        startFragment(params, new InstalledFragment());
                                        break;
                                }
                            } else {
                                showShortToast("您已填写过，需要撤销后才能重新填写");
                            }
                    });
                }

            });
        }
    }

    /**
     * 显示历史列表
     */
    private void showHistory(final List<CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean> listHistoryBeans) {
        if (listHistoryBeans != null && listHistoryBeans.size() > 0) {
            final CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean = infoListBean.getListHouseInfo();
            rvRecord.setAdapter(new CommonAdapter<CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean>(mContext,listHistoryBeans){

                @Override
                public int getItemViewType(int position) {
                    if(mDatas.get(position).getHistory() == null){
                        return 0;
                    }
                    try {
                        return Integer.parseInt(mDatas.get(position).getHistory().getOperateCode());
                    } catch (Exception e) {
                        return 0;
                    }
                }

                @Override
                protected int bindView(int viewType) {
                    return HouseManageFragment.this.getItemLayoutId(viewType);
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean listHistoryBean, int position) {
                    final CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean = listHistoryBean.getHistory();
                    TextView tvFollowTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_follow_time);
                    TextView tvFollower = holder.obtainView(R.id.tv_item_rv_house_manage_record_follower);
                    TextView tvType = holder.obtainView(R.id.tv_item_rv_house_manage_record_type);
                    TextView tvCancel = holder.obtainView(R.id.btn_item_rv_house_manage_record_cancel);
                    View lineTop = holder.obtainView(R.id.view_item_rv_house_manage_record_top);
                    View lineBottom = holder.obtainView(R.id.view_item_rv_house_manage_record_bottom);
                    lineTop.setBackgroundResource(position == 0 ? R.color.bg_transparent : R.drawable.line_dash_vertical);
                    lineBottom.setBackgroundResource(position == listHistoryBeans.size() - 1 ? R.color.bg_transparent : R.drawable.line_dash_vertical);
                    tvFollowTime.setText(historyBean.getFileType());
                    tvFollower.setText("跟进人：" + historyBean.getUserId());
                    tvType.setText("类型：" + historyBean.getFromStatusCode());
                    tvCancel.setOnClickListener(v -> cancel(historyBean, houseInfoBean));
                    TextView tvNote = null;
                    TextView tvImgTime;
                    TextView tvInstallerName;
                    TextView tvInstaller;
                    TextView tvInstallTime;
                    TextView tvResult;
                    TextView tvActualMeasurer;
                    RecyclerView rvImg;
                    switch (historyBean.getOperateCode()) {
                        case "01":
                            TextView tvGuide = holder.obtainView(R.id.tv_item_rv_house_manage_record_designer);
                            tvGuide.setText("分配导购：" + historyBean.getSalesId());
                            tvCancel.setVisibility(View.GONE);
                            break;
                        case "02":
                            TextView tvWay = holder.obtainView(R.id.tv_item_rv_house_manage_record_add_record_way);
                            tvResult = holder.obtainView(R.id.tv_item_rv_house_manage_record_add_record_result);
                            TextView tvNextTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_add_record_next_time);
                            tvWay.setText("沟通方式：" + historyBean.getDesignerId());
                            tvResult.setText("沟通结果：" + historyBean.getRemark());
                            tvCancel.setVisibility(View.GONE);
                            if (TextUtils.isEmpty(historyBean.getNextFollowUpDate()))
                                tvNextTime.setVisibility(View.GONE);
                            else
                                tvNextTime.setText("下次跟进日期：" + TimeUtil.stampToString(historyBean.getNextFollowUpDate(), "yyyy.MM.dd"));

                            break;
                        case "03":
                            TextView tvAmount = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_amount);
                            tvAmount.setText("收取订金：￥" + textEmptyNumber(String.valueOf(historyBean.getDepositAmount())));
                            TextView tvTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_time);
                            tvTime.setText("收取时间：" + TimeUtil.stampToString(historyBean.getNextFollowUpDate(), "yyyy.MM.dd"));
                            TextView tvPeople = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_people);
                            tvPeople.setText("收取人：" + String.valueOf(historyBean.getDesignerId()));
                            TextView tvRemark = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_remark);
                            tvRemark.setText("备注：" + String.valueOf(historyBean.getRemark()));
                            tvRemark.setVisibility(TextUtils.isEmpty(historyBean.getRemark()) ? View.GONE : View.VISIBLE);
                            TextView tvCategory = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_deposit_custom_category);
                            tvCategory.setText("定制品类：" + (TextUtils.isEmpty(houseInfoBean.getEarnestHouseTypeName()) ? "" : houseInfoBean.getEarnestHouseTypeName()));

                            rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                            showImg(mContext, rvImg, listHistoryBean.getUrl());
                            break;
                        case "04":
                            tvInstallTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_booking_measure_time);
                            TextView tvMeasurer = holder.obtainView(R.id.tv_item_rv_house_manage_record_booking_measure_measurer);
                            TextView tvRoom = holder.obtainView(R.id.tv_item_rv_house_manage_record_booking_measure_room);
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            tvInstallTime.setText("预约量房日期：" + (TextUtils.isEmpty(houseInfoBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentTime(), "yyyy.MM.dd")));
                            tvMeasurer.setText("量房人员：" + historyBean.getSalesId());
                            tvRoom.setText("量房空间：" + historyBean.getDesignerId());
                            tvNote.setText("备注：" + historyBean.getRemark());
                            break;
                        case "05":
                            TextView tvDesigner = holder.obtainView(R.id.tv_item_rv_house_manage_record_designer);
                            tvDesigner.setText("分配设计师：" + historyBean.getDesignerId());
                            tvCancel.setVisibility(View.GONE);
                            break;
                        case "06":
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            TextView tvMeasureTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_measure_time);
                            tvImgTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_measure_img_time);
                            tvActualMeasurer = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_measurer);
                            rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                            tvMeasureTime.setVisibility(TextUtils.isEmpty(historyBean.getMeasureTime()) ? View.GONE : View.VISIBLE);
                            tvMeasureTime.setText("实际量房日期：" + (TextUtils.isEmpty(historyBean.getMeasureTime()) ? "" : TimeUtil.stampToString(historyBean.getMeasureTime(), "yyyy.MM.dd")));
                            tvActualMeasurer.setText("实际量房人员：" + historyBean.getDesignerId());
                            if (TextUtils.isEmpty(historyBean.getAppointmentTime()))
                                tvImgTime.setVisibility(View.GONE);
                            tvImgTime.setText("预约确图日期：" + (TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
                            tvNote.setText("备注：" + historyBean.getRemark());
                            showImg(mContext, rvImg, listHistoryBean.getUrl());
                            break;
                        case "07":
                            TextView tvUplaodTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_plan_time);
                            TextView tvUplaodName = holder.obtainView(R.id.tv_item_rv_house_manage_upload_name);
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            tvImgTime = holder.obtainView(R.id.tv_item_rv_house_manage_img_time);
                            tvUplaodTime.setText("上传方案时间：" + (TextUtils.isEmpty(historyBean.getMeasureTime()) ? "" : TimeUtil.stampToString(historyBean.getMeasureTime(), "yyyy.MM.dd")));
                            rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                            tvNote.setText("备注：" + historyBean.getRemark());
                            tvUplaodName.setText("上传方案人员：" + historyBean.getDesignerId());
                            if (TextUtils.isEmpty(historyBean.getAppointmentTime()))
                                tvImgTime.setVisibility(View.GONE);
                            tvImgTime.setText("预约确图日期：" + (TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
                            showImg(mContext, rvImg, listHistoryBean.getUrl());
                            break;
                        case "08":
                            tvResult = holder.obtainView(R.id.tv_item_rv_house_manage_record_shoper_check_result);
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            tvResult.setText("查房结果：" + (TextUtils.isEmpty(historyBean.getDesignerId()) ? "不通过" : historyBean.getDesignerId().equals("02") ? "通过" : "不通过"));
                            tvNote.setText("备注：" + historyBean.getRemark());
                            break;
                        case "09":
                            TextView tvOrderMoney = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_order_amount);
                            TextView tvDeposit = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_deposit);
                            TextView tvCollect = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_collect);
                            TextView tvUncollect = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_uncollect);
                            TextView tvRemeasure = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_remeasure);
                            TextView tvBookingTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_sign_booking_time);
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                            tvOrderMoney.setText("订单金额：￥" + textEmptyNumber(historyBean.getSalesAmount()));
                            tvDeposit.setText("已收订金：￥" + textEmptyNumber(historyBean.getSalesId()));
                            tvCollect.setText("已收款：￥" + textEmptyNumber(historyBean.getDepositAmount()));
//                            tvUncollect.setText("还欠尾款：￥" + houseInfoBean.getLastRemaining());
                            tvUncollect.setText("还欠尾款：￥" + textEmptyNumber(historyBean.getDesignerId()));
                            tvRemeasure.setText("是否需要复尺：" + (TextUtils.isEmpty(houseInfoBean.getIsAfter()) ? "不需要" : houseInfoBean.getIsAfter().equals("1") ? "需要" : "不需要"));
                            tvBookingTime.setText("预约安装日期：" + (TextUtils.isEmpty(houseInfoBean.getAppointmentToInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentToInstallDate(), "yyyy.MM.dd")));
                            tvNote.setText("备注：" + historyBean.getRemark());
                            showImg(mContext, rvImg, listHistoryBean.getUrl());
                            break;
                        case "10":
                            tvResult = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_remeasure_result);
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                            TextView people = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_remeasure_people);
                            TextView date = holder.obtainView(R.id.tv_item_rv_house_manage_record_upload_remeasure_date);
                            tvResult.setText("复尺结果：" + (TextUtils.isEmpty(historyBean.getDesignAvailable()) ? "复尺不可用" : historyBean.getDesignAvailable().equals("1") ? "复尺可用" : "复尺不可用"));
                            tvNote.setText("备注：" + historyBean.getRemark());
                            people.setText("复尺人员：" + historyBean.getDesignerId());
                            date.setText("复尺日期：" + (TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
                            showImg(mContext, rvImg, listHistoryBean.getUrl());
                            break;
                        case "11":
                            break;
                        case "12":
                            TextView tvReason = holder.obtainView(R.id.tv_item_rv_house_manage_record_loosed_reason);
                            TextView tvGo = holder.obtainView(R.id.tv_item_rv_house_manage_record_loosed_go);
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            tvReason.setText("流失原因：" + historyBean.getLeaveReason());
                            tvGo.setText("客户去向：" + historyBean.getLeaveToSeries());
                            tvNote.setText("备注：" + historyBean.getRemark());
                            break;
                        case "13":
                            break;
                        case "14":
                            tvInstallTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_money_collected);
                            tvInstaller = holder.obtainView(R.id.tv_item_rv_house_manage_record_collect_uncollect);
                            tvInstallTime.setText("收尾款：￥" + textEmptyNumber(historyBean.getDepositAmount()));
                            tvInstaller.setText("还欠尾款：￥" + textEmptyNumber(historyBean.getSalesAmount()));
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                            tvNote.setText("备注：" + historyBean.getRemark());
                            showImg(mContext, rvImg, listHistoryBean.getUrl());
                            break;
                        case "15":
                            tvInstallTime = holder.obtainView(R.id.tv_item_rv_house_manage_record_uninstall_time);
                            tvInstaller = holder.obtainView(R.id.tv_item_rv_house_manage_record_uninstall_installer);
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            TextView tvArea = holder.obtainView(R.id.tv_item_rv_house_manage_record_uninstall_area);
                            tvInstallTime.setText("预约安装时间：" + (TextUtils.isEmpty(houseInfoBean.getAppointmentToInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentToInstallDate(), "yyyy.MM.dd")));
                            tvInstaller.setText("安装师傅：" + historyBean.getDesignerId());
                            tvNote.setText("备注：" + historyBean.getRemark());
                            tvArea.setText("安装面积：" + historyBean.getDepositAmount());
                            break;
                        case "16":
                            TextView tvInstalled = holder.obtainView(R.id.tv_item_rv_house_manage_record_installed_time);
                            tvInstalled.setText("安装完成时间：" + (TextUtils.isEmpty(houseInfoBean.getEndingInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getEndingInstallDate(), "yyyy.MM.dd")));
                            tvNote = holder.obtainView(R.id.tv_item_rv_house_manage_record_note);
                            tvInstallerName = holder.obtainView(R.id.tv_item_rv_house_manage_record_installer_name);
                            rvImg = holder.obtainView(R.id.rv_item_rv_house_manage_record_img);
                            tvNote.setText("备注：" + historyBean.getRemark());
                            tvInstallerName.setText("安装师傅：" + historyBean.getDesignerId());
                            showImg(mContext, rvImg, listHistoryBean.getUrl());
                            break;
                    }
                    if (tvNote != null) {//备注没有，不显示
                        tvNote.setVisibility(TextUtils.isEmpty(historyBean.getRemark()) ? View.GONE : View.VISIBLE);
                    }
                }
            });
//            rvRecord.setAdapter(new MultiItemCommonAdapter<CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean>(mContext, listHistoryBeans, multiItemSupport) {
//                @SuppressLint("SetTextI18n")
//                @Override
//                protected void convert(ViewHolder holder, CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean listHistoryBean, final int position) {
//                    final CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean = listHistoryBean.getHistory();
//                    TextView tvFollowTime = holder.getView(R.id.tv_item_rv_house_manage_record_follow_time);
//                    TextView tvFollower = holder.getView(R.id.tv_item_rv_house_manage_record_follower);
//                    TextView tvType = holder.getView(R.id.tv_item_rv_house_manage_record_type);
//                    TextView tvCancel = holder.getView(R.id.btn_item_rv_house_manage_record_cancel);
//                    View lineTop = holder.getView(R.id.view_item_rv_house_manage_record_top);
//                    View lineBottom = holder.getView(R.id.view_item_rv_house_manage_record_bottom);
//
//                    lineTop.setBackgroundResource(position == 0 ? R.color.bg_transparent : R.drawable.line_dash_vertical);
//                    lineBottom.setBackgroundResource(position == listHistoryBeans.size() - 1 ? R.color.bg_transparent : R.drawable.line_dash_vertical);
//                    tvFollowTime.setText(historyBean.getFileType());
//                    tvFollower.setText("跟进人：" + historyBean.getUserId());
//                    tvType.setText("类型：" + historyBean.getFromStatusCode());
//                    tvCancel.setOnClickListener(v -> cancel(historyBean, houseInfoBean));
//
//                    TextView tvNote = null;
//                    TextView tvImgTime;
//                    TextView tvInstallerName;
//                    TextView tvInstaller;
//                    TextView tvInstallTime;
//                    TextView tvResult;
//                    TextView tvActualMeasurer;
//                    RecyclerView rvImg;
//                    switch (historyBean.getOperateCode()) {
//                        case "01":
//                            TextView tvGuide = holder.getView(R.id.tv_item_rv_house_manage_record_designer);
//                            tvGuide.setText("分配导购：" + historyBean.getSalesId());
//                            tvCancel.setVisibility(View.GONE);
//                            break;
//                        case "02":
//                            TextView tvWay = holder.getView(R.id.tv_item_rv_house_manage_record_add_record_way);
//                            tvResult = holder.getView(R.id.tv_item_rv_house_manage_record_add_record_result);
//                            TextView tvNextTime = holder.getView(R.id.tv_item_rv_house_manage_record_add_record_next_time);
//                            tvWay.setText("沟通方式：" + historyBean.getDesignerId());
//                            tvResult.setText("沟通结果：" + historyBean.getRemark());
//                            tvCancel.setVisibility(View.GONE);
//                            if (TextUtils.isEmpty(historyBean.getNextFollowUpDate()))
//                                tvNextTime.setVisibility(View.GONE);
//                            else
//                                tvNextTime.setText("下次跟进日期：" + TimeUtil.stampToString(historyBean.getNextFollowUpDate(), "yyyy.MM.dd"));
//
//                            break;
//                        case "03":
//                            TextView tvAmount = holder.getView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_amount);
//                            tvAmount.setText("收取订金：￥" + textEmptyNumber(String.valueOf(historyBean.getDepositAmount())));
//                            TextView tvTime = holder.getView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_time);
//                            tvTime.setText("收取时间：" + TimeUtil.stampToString(historyBean.getNextFollowUpDate(), "yyyy.MM.dd"));
//                            TextView tvPeople = holder.getView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_people);
//                            tvPeople.setText("收取人：" + String.valueOf(historyBean.getDesignerId()));
//                            TextView tvRemark = holder.getView(R.id.tv_item_rv_house_manage_record_collect_deposit_order_remark);
//                            tvRemark.setText("备注：" + String.valueOf(historyBean.getRemark()));
//                            tvRemark.setVisibility(TextUtils.isEmpty(historyBean.getRemark()) ? View.GONE : View.VISIBLE);
//                            TextView tvCategory = holder.getView(R.id.tv_item_rv_house_manage_record_collect_deposit_custom_category);
//                            tvCategory.setText("定制品类：" + (TextUtils.isEmpty(houseInfoBean.getEarnestHouseTypeName()) ? "" : houseInfoBean.getEarnestHouseTypeName()));
//
//                            rvImg = holder.getView(R.id.rv_item_rv_house_manage_record_img);
//                            showImg(mContext, rvImg, listHistoryBean.getUrl());
//                            break;
//                        case "04":
//                            tvInstallTime = holder.getView(R.id.tv_item_rv_house_manage_record_booking_measure_time);
//                            TextView tvMeasurer = holder.getView(R.id.tv_item_rv_house_manage_record_booking_measure_measurer);
//                            TextView tvRoom = holder.getView(R.id.tv_item_rv_house_manage_record_booking_measure_room);
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            tvInstallTime.setText("预约量房日期：" + (TextUtils.isEmpty(houseInfoBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentTime(), "yyyy.MM.dd")));
//                            tvMeasurer.setText("量房人员：" + historyBean.getSalesId());
//                            tvRoom.setText("量房空间：" + historyBean.getDesignerId());
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            break;
//                        case "05":
//                            TextView tvDesigner = holder.getView(R.id.tv_item_rv_house_manage_record_designer);
//                            tvDesigner.setText("分配设计师：" + historyBean.getDesignerId());
//                            tvCancel.setVisibility(View.GONE);
//                            break;
//                        case "06":
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            TextView tvMeasureTime = holder.getView(R.id.tv_item_rv_house_manage_record_upload_measure_time);
//                            tvImgTime = holder.getView(R.id.tv_item_rv_house_manage_record_upload_measure_img_time);
//                            tvActualMeasurer = holder.getView(R.id.tv_item_rv_house_manage_record_upload_measurer);
//                            rvImg = holder.getView(R.id.rv_item_rv_house_manage_record_img);
//                            tvMeasureTime.setVisibility(TextUtils.isEmpty(historyBean.getMeasureTime()) ? View.GONE : View.VISIBLE);
//                            tvMeasureTime.setText("实际量房日期：" + (TextUtils.isEmpty(historyBean.getMeasureTime()) ? "" : TimeUtil.stampToString(historyBean.getMeasureTime(), "yyyy.MM.dd")));
//                            tvActualMeasurer.setText("实际量房人员：" + historyBean.getDesignerId());
//                            if (TextUtils.isEmpty(historyBean.getAppointmentTime()))
//                                tvImgTime.setVisibility(View.GONE);
//                            tvImgTime.setText("预约确图日期：" + (TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            showImg(mContext, rvImg, listHistoryBean.getUrl());
//                            break;
//                        case "07":
//                            TextView tvUplaodTime = holder.getView(R.id.tv_item_rv_house_manage_record_upload_plan_time);
//                            TextView tvUplaodName = holder.getView(R.id.tv_item_rv_house_manage_upload_name);
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            tvImgTime = holder.getView(R.id.tv_item_rv_house_manage_img_time);
//                            tvUplaodTime.setText("上传方案时间：" + (TextUtils.isEmpty(historyBean.getMeasureTime()) ? "" : TimeUtil.stampToString(historyBean.getMeasureTime(), "yyyy.MM.dd")));
//                            rvImg = holder.getView(R.id.rv_item_rv_house_manage_record_img);
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            tvUplaodName.setText("上传方案人员：" + historyBean.getDesignerId());
//                            if (TextUtils.isEmpty(historyBean.getAppointmentTime()))
//                                tvImgTime.setVisibility(View.GONE);
//                            tvImgTime.setText("预约确图日期：" + (TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
//                            showImg(mContext, rvImg, listHistoryBean.getUrl());
//                            break;
//                        case "08":
//                            tvResult = holder.getView(R.id.tv_item_rv_house_manage_record_shoper_check_result);
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            tvResult.setText("查房结果：" + (TextUtils.isEmpty(historyBean.getDesignerId()) ? "不通过" : historyBean.getDesignerId().equals("02") ? "通过" : "不通过"));
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            break;
//                        case "09":
//                            TextView tvOrderMoney = holder.getView(R.id.tv_item_rv_house_manage_record_sign_order_amount);
//                            TextView tvDeposit = holder.getView(R.id.tv_item_rv_house_manage_record_sign_deposit);
//                            TextView tvCollect = holder.getView(R.id.tv_item_rv_house_manage_record_sign_collect);
//                            TextView tvUncollect = holder.getView(R.id.tv_item_rv_house_manage_record_sign_uncollect);
//                            TextView tvRemeasure = holder.getView(R.id.tv_item_rv_house_manage_record_sign_remeasure);
//                            TextView tvBookingTime = holder.getView(R.id.tv_item_rv_house_manage_record_sign_booking_time);
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            rvImg = holder.getView(R.id.rv_item_rv_house_manage_record_img);
//                            tvOrderMoney.setText("订单金额：￥" + textEmptyNumber(historyBean.getSalesAmount()));
//                            tvDeposit.setText("已收订金：￥" + textEmptyNumber(historyBean.getSalesId()));
//                            tvCollect.setText("已收款：￥" + textEmptyNumber(historyBean.getDepositAmount()));
////                            tvUncollect.setText("还欠尾款：￥" + houseInfoBean.getLastRemaining());
//                            tvUncollect.setText("还欠尾款：￥" + textEmptyNumber(historyBean.getDesignerId()));
//                            tvRemeasure.setText("是否需要复尺：" + (TextUtils.isEmpty(houseInfoBean.getIsAfter()) ? "不需要" : houseInfoBean.getIsAfter().equals("1") ? "需要" : "不需要"));
//                            tvBookingTime.setText("预约安装日期：" + (TextUtils.isEmpty(houseInfoBean.getAppointmentToInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentToInstallDate(), "yyyy.MM.dd")));
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            showImg(mContext, rvImg, listHistoryBean.getUrl());
//                            break;
//                        case "10":
//                            tvResult = holder.getView(R.id.tv_item_rv_house_manage_record_upload_remeasure_result);
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            rvImg = holder.getView(R.id.rv_item_rv_house_manage_record_img);
//                            TextView people = holder.getView(R.id.tv_item_rv_house_manage_record_upload_remeasure_people);
//                            TextView date = holder.getView(R.id.tv_item_rv_house_manage_record_upload_remeasure_date);
//                            tvResult.setText("复尺结果：" + (TextUtils.isEmpty(historyBean.getDesignAvailable()) ? "复尺不可用" : historyBean.getDesignAvailable().equals("1") ? "复尺可用" : "复尺不可用"));
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            people.setText("复尺人员：" + historyBean.getDesignerId());
//                            date.setText("复尺日期：" + (TextUtils.isEmpty(historyBean.getAppointmentTime()) ? "" : TimeUtil.stampToString(historyBean.getAppointmentTime(), "yyyy.MM.dd")));
//                            showImg(mContext, rvImg, listHistoryBean.getUrl());
//                            break;
//                        case "11":
//                            break;
//                        case "12":
//                            TextView tvReason = holder.getView(R.id.tv_item_rv_house_manage_record_loosed_reason);
//                            TextView tvGo = holder.getView(R.id.tv_item_rv_house_manage_record_loosed_go);
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            tvReason.setText("流失原因：" + historyBean.getLeaveReason());
//                            tvGo.setText("客户去向：" + historyBean.getLeaveToSeries());
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            break;
//                        case "13":
//                            break;
//                        case "14":
//                            tvInstallTime = holder.getView(R.id.tv_item_rv_house_manage_record_collect_money_collected);
//                            tvInstaller = holder.getView(R.id.tv_item_rv_house_manage_record_collect_uncollect);
//                            tvInstallTime.setText("收尾款：￥" + textEmptyNumber(historyBean.getDepositAmount()));
//                            tvInstaller.setText("还欠尾款：￥" + textEmptyNumber(historyBean.getSalesAmount()));
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            rvImg = holder.getView(R.id.rv_item_rv_house_manage_record_img);
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            showImg(mContext, rvImg, listHistoryBean.getUrl());
//                            break;
//                        case "15":
//                            tvInstallTime = holder.getView(R.id.tv_item_rv_house_manage_record_uninstall_time);
//                            tvInstaller = holder.getView(R.id.tv_item_rv_house_manage_record_uninstall_installer);
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            TextView tvArea = holder.getView(R.id.tv_item_rv_house_manage_record_uninstall_area);
//                            tvInstallTime.setText("预约安装时间：" + (TextUtils.isEmpty(houseInfoBean.getAppointmentToInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getAppointmentToInstallDate(), "yyyy.MM.dd")));
//                            tvInstaller.setText("安装师傅：" + historyBean.getDesignerId());
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            tvArea.setText("安装面积：" + historyBean.getDepositAmount());
//                            break;
//                        case "16":
//                            TextView tvInstalled = holder.getView(R.id.tv_item_rv_house_manage_record_installed_time);
//                            tvInstalled.setText("安装完成时间：" + (TextUtils.isEmpty(houseInfoBean.getEndingInstallDate()) ? "" : TimeUtil.stampToString(houseInfoBean.getEndingInstallDate(), "yyyy.MM.dd")));
//                            tvNote = holder.getView(R.id.tv_item_rv_house_manage_record_note);
//                            tvInstallerName = holder.getView(R.id.tv_item_rv_house_manage_record_installer_name);
//                            rvImg = holder.getView(R.id.rv_item_rv_house_manage_record_img);
//                            tvNote.setText("备注：" + historyBean.getRemark());
//                            tvInstallerName.setText("安装师傅：" + historyBean.getDesignerId());
//                            showImg(mContext, rvImg, listHistoryBean.getUrl());
//                            break;
//                    }
//                    if (tvNote != null) {//备注没有，不显示
//                        tvNote.setVisibility(TextUtils.isEmpty(historyBean.getRemark()) ? View.GONE : View.VISIBLE);
//                    }
//                }
//            });
        } else {
            tvHistoryTitle.setVisibility(View.GONE);
            vFollowLine.setVisibility(View.GONE);
        }
    }

    /**
     * 撤销
     */
    protected void cancel(final CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean, final CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean) {
        if (TextUtils.equals(historyBean.getCanceled(), "1")) {
            showShortToast(R.string.tips_cannot_cancel);
        } else {
            CancelDialog dialog = new CancelDialog(mContext).setListener(new CancelDialog.ClickListener() {
                @Override
                public void onClick(String string) {
                    if (TextUtils.isEmpty(string)) {
                        showShortToast("请输入撤销原因");
                    } else {
                        if (!TextUtils.isEmpty(historyBean.getOperateCode())) {
                            switch (historyBean.getOperateCode()) {
                                case "04":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_booking_measure");
                                    break;
                                case "06":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_measure");
                                    break;
                                case "07":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_upload_plan");
                                    break;
                                case "09":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_sign");
                                    break;
                                case "10":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_remeasure_result");
                                    break;
                                case "15":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_booking_install");
                                    break;
                                case "16":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_installed");
                                    break;
                                case "12":
                                    MobclickAgent.onEvent(mContext, "customerdetails_cancel_loosed");
                                    break;
                            }
                        }
                        mPresenter.revoke(string, historyBean.getOperateCode(), houseInfoBean.getHouseId(), historyBean.getToStatusCode());
                        showLoading();
                    }
                }
            });
            dialog.show();
        }
    }

    /**
     * 根据类型获取跟进记录样式
     *
     * @param itemType
     * @return
     */
    protected int getItemLayoutId(int itemType) {
        int layoutId = 0;
        switch (itemType) {
            case 1:
                layoutId = R.layout.item_rv_house_manage_record_divied_designer;
                break;
            case 2:
                layoutId = R.layout.item_rv_house_manage_record_add_record;
                break;
            case 3:
                layoutId = R.layout.item_rv_house_manage_record_collect_deposit;
                break;
            case 4:
                layoutId = R.layout.item_rv_house_manage_record_booking_measure;
                break;
            case 5:
                layoutId = R.layout.item_rv_house_manage_record_divied_designer;
                break;
            case 6:
                layoutId = R.layout.item_rv_house_manage_record_upload_measure;
                break;
            case 7:
                layoutId = R.layout.item_rv_house_manage_record_upload_plan;
                break;
            case 8:
                layoutId = R.layout.item_rv_house_manage_record_shoper_check;
                break;
            case 9:
                layoutId = R.layout.item_rv_house_manage_record_sign;
                break;
            case 10:
                layoutId = R.layout.item_rv_house_manage_record_upload_remeasure;
                break;
            case 11:
                layoutId = R.layout.item_rv_undefine;
                break;
            case 12:
                layoutId = R.layout.item_rv_house_manage_record_lossed;
                break;
            case 13:
                layoutId = R.layout.item_rv_undefine;
                break;
            case 14:
                layoutId = R.layout.item_rv_house_manage_record_collect_money;
                break;
            case 15:
                layoutId = R.layout.item_rv_house_manage_record_uninstall;
                break;
            case 16:
                layoutId = R.layout.item_rv_house_manage_record_installed;
                break;
        }
        return layoutId;
    }

    /**
     * 显示图片列表
     *
     * @param context
     * @param rv
     * @param list
     */
    public void showImg(final Context context, RecyclerView rv, final List<String> list) {
        if (list != null && list.size() > 0) {
            final List<String> imgUrls = new ArrayList<>();
            for (String imgUrl : list) {

                imgUrls.add(url + imgUrl);
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
                    Glide.with(context).load(url + imgUrl).apply(new RequestOptions().placeholder(R.drawable.loading_photo)).into(iv);
//                    Glide.with(context).load(url + imgUrl).placeholder(R.drawable.loading_photo).into(iv);
                    iv.setOnClickListener(v -> PhotoViewActivity.openImage((Activity) context, position, imgUrls));
                }
            });
        }
    }

    @OnClick(R.id.tv_customer_detail_edit)
    public void onViewClicked() {
        Map<String, Serializable> paramsHouse = new HashMap<>();
        paramsHouse.put(Constants.PERSONAL_BEAN, personal);
        paramsHouse.put(Constants.HOUSE_INFO, infoListBean.getListHouseInfo());
        paramsHouse.put(Constants.HOUSE_TYPE, Constants.EDIT_HOUSE);
        if (infoListBean != null && infoListBean.getListDetails() != null && infoListBean.getListDetails().size() > 0) {
            paramsHouse.put(Constants.LIST_DETAILS_BEAN, infoListBean.getListDetails().get(0));
        }
        startFragment(paramsHouse, new EditHouseFragment());
    }

    @Override
    public void success(Object success) {
        dismissLoading();
        showShortToast(MyJsonParser.getShowMessage((String) success));
        ((CustomerDetailFragment) getParentFragment()).refresh(false);
    }

    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
