package com.holike.crm.fragment.homepage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.base.ToolbarHelper;
import com.holike.crm.bean.OrderDetailsBean;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.fragment.customer.LogisticsInfoFragment;
import com.holike.crm.fragment.customer.OperationLogFragment;
import com.holike.crm.fragment.customer.ProductInfoFragment;
import com.holike.crm.fragment.customer.QuotationListNewFragment;
import com.holike.crm.fragment.customer.ReceiptDeliveryManifestFragment;
import com.holike.crm.fragment.customer.SpaceManifestFragment;
import com.holike.crm.popupwindown.ListMenuPopupWindow;
import com.holike.crm.presenter.activity.OrderDetailsPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.activity.OrderDetailsView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/*订单详情*/
public class OrderDetailsFragment extends MyFragment<OrderDetailsPresenter, OrderDetailsView> implements ListMenuPopupWindow.ItemSelectListener, OrderDetailsView {

    @BindView(R.id.mContainer)
    View mContentLayout;
    @BindView(R.id.tv_order_details_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_details_order_type)
    TextView tvOrderType;
    @BindView(R.id.tv_order_details_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_details_order_people)
    TextView tvOrderPeople;
    @BindView(R.id.tv_order_details_order_state)
    TextView tvOrderState;
    @BindView(R.id.tv_order_details_order_isAllDelivery)
    TextView tvOrderIsAllDelivery;
    @BindView(R.id.tv_order_details_order_isUrgent)
    TextView tvOrderIsUrgent;
    @BindView(R.id.tv_order_details_order_delivery_time)
    TextView tvOrderDeliveryTime;
    @BindView(R.id.tv_order_details_order_customer_name)
    TextView tvOrderCustomerName;
    @BindView(R.id.tv_order_details_order_customer_phone)
    TextView tvOrderCustomerPhone;
    @BindView(R.id.tv_order_details_order_community_name)
    TextView tvOrderCommunityName;
    @BindView(R.id.tv_order_details_order_address)
    TextView tvOrderAddress;
    @BindView(R.id.tv_order_details_order_area)
    TextView tvOrderArea;
    @BindView(R.id.tv_order_details_order_price)
    TextView tvOrderPrice;
    @BindView(R.id.tv_order_details_order_recommended_retail_price)
    TextView tvOrderRecommendedPrice;
    @BindView(R.id.tv_order_details_order_dealer_factor)
    TextView tvOrderDealerFactor;
    @BindView(R.id.tv_order_details_order_amount_of_discount)
    TextView tvOrderAmountDiscount;
    @BindView(R.id.tv_order_details_order_total_price_before)
    TextView tvOrderBefore;
    @BindView(R.id.tv_order_details_order_total_price_after)
    TextView tvOrderAfter;
    @BindView(R.id.tv_order_details_order_district)
    TextView tvOrderDistrict;
    @BindView(R.id.ll_order_details_order_recommended_retail_price)
    LinearLayout llRecommendedPrice;
    @BindView(R.id.ll_order_details_order_dealer_factor)
    LinearLayout llDealerFactor;
    @BindView(R.id.ll_order_details_order_total_price_before)
    LinearLayout llPriceBefore;
    @BindView(R.id.ll_order_details_order_total_price_after)
    LinearLayout llPriceAfter;
    private String orderId;

    @Override
    protected OrderDetailsPresenter attachPresenter() {
        return new OrderDetailsPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_order_details;
    }

    @Override
    protected void init() {
        super.init();
        MobclickAgent.onEvent(mContext, "order_details");
        setTitle(mContext.getString(R.string.order_details_title));
        showLoading();
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = String.valueOf(bundle.getSerializable(Constants.ORDER_ID));
            mPresenter.getOrderDetails(orderId, String.valueOf(bundle.getSerializable(Constants.MESSAGE_ID)));
        }
        setOptionsMenu(R.menu.menu_more);
    }

    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        super.clickRightMenu(menuText, actionView);

        ListMenuPopupWindow popupWindow = new ListMenuPopupWindow(mContext, R.array.title_order_info, R.array.title_order_info_id, this);
//        popupWindow.showAsDropDown(actionView, -DensityUtil.dp2px(6), 0, Gravity.END);
        ToolbarHelper.showPopupWindow(popupWindow, actionView);
    }

    @Override
    public void onSelect(String title) {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.ORDER_ID, orderId);
        switch (title) {
            case "物流信息":
                startFragment(params, new LogisticsInfoFragment());
                break;
            case "空间清单":
                startFragment(params, new SpaceManifestFragment());
                break;
            case "收发货清单":
                startFragment(params, new ReceiptDeliveryManifestFragment());
                break;
            case "报价清单":
                startFragment(params, new QuotationListNewFragment());
                break;
            case "产品信息":
                startFragment(params, new ProductInfoFragment());
                break;
            case "操作日志":
                startFragment(params, new OperationLogFragment());
                break;
        }
    }

    @Override
    public void getDetailsSuccess(OrderDetailsBean orderDetailsBean) {
        dismissLoading();
        if (mContentLayout.getVisibility() != View.VISIBLE) {
            mContentLayout.setVisibility(View.VISIBLE);
        }
        tvOrderNumber.setText(TextUtils.isEmpty(orderDetailsBean.getOrderId()) ? "-" : orderDetailsBean.getOrderId());
        tvOrderType.setText(TextUtils.isEmpty(orderDetailsBean.getOrderTypeName()) ? "-" : orderDetailsBean.getOrderTypeName());
        tvOrderTime.setText(TextUtils.isEmpty(orderDetailsBean.getCreateDate()) ? "-" : orderDetailsBean.getCreateDate());
        tvOrderPeople.setText(TextUtils.isEmpty(orderDetailsBean.getBuyer()) ? "-" : orderDetailsBean.getBuyer());
        tvOrderState.setText(TextUtils.isEmpty(orderDetailsBean.getOrderStatusName()) ? "-" : orderDetailsBean.getOrderStatusName());
        tvOrderIsAllDelivery.setText(TextUtils.isEmpty(orderDetailsBean.getIsPackage()) ? "-" : orderDetailsBean.getIsPackage().equals("1") ? "是" : "否");
        tvOrderIsUrgent.setText(TextUtils.isEmpty(orderDetailsBean.getIsUrgent()) ? "-" : orderDetailsBean.getIsUrgent().equals("1") ? "是" : "否");
        tvOrderDeliveryTime.setText(TextUtils.isEmpty(orderDetailsBean.getDeliveryDate()) ? "-" : orderDetailsBean.getDeliveryDate());
        tvOrderCustomerName.setText(TextUtils.isEmpty(orderDetailsBean.getName()) ? "-" : orderDetailsBean.getName());
        tvOrderCustomerPhone.setText(TextUtils.isEmpty(orderDetailsBean.getPhone()) ? "-" : orderDetailsBean.getPhone());
        tvOrderCommunityName.setText(TextUtils.isEmpty(orderDetailsBean.getVillage()) ? "-" : orderDetailsBean.getVillage());
        tvOrderAddress.setText(TextUtils.isEmpty(orderDetailsBean.getRoomNumber()) ? "-" : orderDetailsBean.getRoomNumber());
        tvOrderPrice.setText(textEmptyNumber(orderDetailsBean.getPrice()));


        tvOrderArea.setText(TextUtils.isEmpty(orderDetailsBean.getTotalArea()) ? "-" : orderDetailsBean.getTotalArea());
        tvOrderRecommendedPrice.setText(textEmptyNumber(orderDetailsBean.getSalePrice()));
        tvOrderDealerFactor.setText(TextUtils.isEmpty(orderDetailsBean.getAccountCoefficient()) ? "-" : orderDetailsBean.getAccountCoefficient());
        tvOrderAmountDiscount.setText(TextUtils.isEmpty(orderDetailsBean.getDisAmount()) ? "-" : orderDetailsBean.getDisAmount());
        tvOrderBefore.setText(textEmptyNumber(orderDetailsBean.getFactoryPriceBeDis()));
        tvOrderAfter.setText(textEmptyNumber(orderDetailsBean.getFactoryPriceAfDis()));
        tvOrderDistrict.setText(TextUtils.isEmpty(orderDetailsBean.getDistrictName()) ? "-" : orderDetailsBean.getDistrictName());

        llRecommendedPrice.setVisibility(TextUtils.equals(orderDetailsBean.getIsSalePrice(), "0") ? View.GONE : View.VISIBLE);

        llDealerFactor.setVisibility(TextUtils.equals(orderDetailsBean.getIsFactoryPrice(), "0") ? View.GONE : View.VISIBLE);
        llPriceBefore.setVisibility(TextUtils.equals(orderDetailsBean.getIsFactoryPrice(), "0") ? View.GONE : View.VISIBLE);
        llPriceAfter.setVisibility(TextUtils.equals(orderDetailsBean.getIsFactoryPrice(), "0") ? View.GONE : View.VISIBLE);

        if (!TextUtils.isEmpty(orderDetailsBean.getOrderId()))
            copy(tvOrderNumber, orderDetailsBean.getOrderId());
        if (!TextUtils.isEmpty(orderDetailsBean.getPhone()))
            copy(tvOrderCustomerPhone, orderDetailsBean.getPhone());

    }

    @Override
    public void getDetailsFailed(String failed) {
        dismissLoading();
        dealWithFailed(failed, true, CompatToast.Gravity.CENTER);
    }
}
