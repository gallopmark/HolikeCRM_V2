package com.holike.crm.popupwindown;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.CustomerListBean;
import com.holike.crm.bean.DistributionGuiderBean;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.helper.ShopListRequestHelper;
import com.holike.crm.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/9/18.
 * 分配门店
 */

public class DistributionStorePopupWindow extends BasePopupWindow implements ShopListRequestHelper.ShopListCallback, View.OnClickListener {

    private Context mContext;
    private RecyclerView rv;
    private ShopListRequestHelper helper;
    private CustomerListBean mCustomerListBean;
    private Button btnConfirm;
    private StateCallback stateCallback;
    private TextView tvGuiderName;
    private LinearLayout llDistributionGuide;
    private DistributionStoreBean currentStoreBean;
    private TextView tvStoreName;
    private ProgressBar mLoading;
    private List<DistributionStoreBean> dataBeans = new ArrayList<>(0);
    private DistributionGuiderBean guiderDataBeans;
    private View view;


    public DistributionStorePopupWindow(Context context, CustomerListBean bean, StateCallback stateCallback) {
        super(context);
        this.mContext = context;
        this.mCustomerListBean = bean;
        this.stateCallback = stateCallback;
        init();
    }


    private void init() {
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ((TextView) mContentView.findViewById(R.id.tv_username)).setText(mCustomerListBean.getUserName());
        //所在省市
        ((TextView) mContentView.findViewById(R.id.tv_in_provinces_cities)).setText(mCustomerListBean.getSite());
        //详细地址
        ((TextView) mContentView.findViewById(R.id.tv_detailed_address)).setText(mCustomerListBean.getAddress());
        //客服备注
        ((TextView) mContentView.findViewById(R.id.tv_server_note)).setText(mCustomerListBean.getDigitalRemark());

        mContentView.findViewById(R.id.tv_no_note).setVisibility(TextUtils.isEmpty(mCustomerListBean.getDigitalRemark()) ? View.VISIBLE : View.GONE);

        tvGuiderName = mContentView.findViewById(R.id.tv_guider_name);
        llDistributionGuide = mContentView.findViewById(R.id.ll_distribution_guide);
        btnConfirm = mContentView.findViewById(R.id.btn_confirm);
        tvStoreName = mContentView.findViewById(R.id.tv_order_numb);
        rv = mContentView.findViewById(R.id.rv_store);
        mLoading = mContentView.findViewById(R.id.loading_view);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        setGuideVisible(View.GONE);
        view = mContentView.findViewById(R.id.view_outside);
        view.setOnClickListener(this);
        helper = new ShopListRequestHelper(this);
        helper.requestStore(mCustomerListBean.getDealerId());
        setBackgroundDrawable(new ColorDrawable());
        setBackgroundAlpha(0.5f);

    }


    @Override
    int setContentView() {
        return R.layout.popup_distribution_store;
    }


    /**
     * 显示门店列表
     *
     * @param context      上下文
     * @param recyclerView RecyclerView
     * @param list         获取到的门店数据
     */
    private void setCustomerList(final Context context, final RecyclerView recyclerView, final List<DistributionStoreBean> list) {

        //自适应高度
        adaptiveListHeight(recyclerView, list.size(), View.VISIBLE);
        setGuideVisible(View.GONE);
        btnConfirm.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_login_cannot_click));
        btnConfirm.setClickable(false);

        recyclerView.setAdapter(new CommonAdapter<DistributionStoreBean>(context, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_store_name;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, final DistributionStoreBean bean, int position) {
                holder.setText(R.id.tv_item_shop_name, bean.getShopName());
                holder.itemView.setOnClickListener(v -> {
                    currentStoreBean = bean;
                    helper.queryPer(bean.getShopId());
                    adaptiveListHeight(recyclerView, 0, View.INVISIBLE);
                    tvStoreName.setText(bean.getShopName());
                    tvStoreName.setTextColor(mContext.getResources().getColor(R.color.textColor4));

                    tvGuiderName.setText("");
                    tvGuiderName.setTextColor(mContext.getResources().getColor(R.color.textColor6));
                });
            }
        });


    }


    /**
     * 显示导购人员列表
     *
     * @param context      上下文
     * @param recyclerView RecyclerView
     * @param list         获取到的导购数据
     */
    private void setCustomerGuiderList(final Context context, final RecyclerView recyclerView, final DistributionGuiderBean list) {

        //自适recyclerView应高度

        adaptiveListHeight(recyclerView, list.getGuide() == null ? 0 : list.getGuide().size(), View.VISIBLE);
        recyclerView.setAdapter(new CommonAdapter<DistributionGuiderBean.GuideBean>(context, list.getGuide()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_store_name;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, DistributionGuiderBean.GuideBean bean, int position) {
                holder.setText(R.id.tv_item_shop_name, bean.getUserName());
                holder.itemView.setOnClickListener(v -> {
                    tvGuiderName.setText(bean.getUserName());
                    tvGuiderName.setTextColor(mContext.getResources().getColor(R.color.textColor4));
                    recyclerView.setVisibility(View.GONE);
                    btnConfirm.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_login_can_click));
                    btnConfirm.setEnabled(true);
                    btnConfirm.setClickable(true);
                    btnConfirm.setOnClickListener(v1 -> {
                        helper.updateStore(mCustomerListBean.getHouseId(), mCustomerListBean.getPersonalId(), currentStoreBean.getShopId(), bean.getUserId());
                        dismiss();
                    });
                });
            }

        });

    }

    /**
     * 导购列表是否可见
     *
     * @param visible GONE/VISIBLE
     */
    private void setGuideVisible(int visible) {
        llDistributionGuide.setVisibility(visible);
    }

    /**
     * 列表自适应高度 ，如果小于等于5，则高度为列表数量*48 + 1dp 如果大于5则最高为5.5*48dp
     *
     * @param rv        RecyclerView
     * @param listCount 数据条数
     */
    private void adaptiveListHeight(RecyclerView rv, int listCount, int visible) {
        ViewGroup.LayoutParams params = rv.getLayoutParams();
        if (0 == listCount) {
            params.height = 1;
        } else if (0 < listCount && listCount <= 3) {
            params.height = listCount * DensityUtil.dp2px(40 + 1);
        } else if (listCount > 3) {
            params.height = (int) (3.5f * DensityUtil.dp2px(40));
        }
        rv.setLayoutParams(params);
        rv.setVisibility(visible);
    }

    @Override
    public void onBeginRequest() {
        dataBeans.clear();
        mLoading.setVisibility(View.VISIBLE);
        tvStoreName.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStoreDataSuccess(List<DistributionStoreBean> dataBeans) {
        mLoading.setVisibility(View.INVISIBLE);
        tvStoreName.setVisibility(View.VISIBLE);
        this.dataBeans = dataBeans;
        tvStoreName.setOnClickListener(this);
    }

    @Override
    public void onGuiderDataSuccess(DistributionGuiderBean dataBeans) {
        setGuideVisible(View.VISIBLE);
        guiderDataBeans = dataBeans;
        tvGuiderName.setOnClickListener(this);
    }

    @Override
    public void onFail(String errorMsg) {
        showShortToast(errorMsg);
    }

    @Override
    public void onUpgradeSuccess() {
        showShortToast("保存门店成功。");
        stateCallback.onUpgradeSuccess();
        dataBeans.clear();
    }

    @Override
    public void onUpgradeFail(String errorMsg) {
        showShortToast(errorMsg);
    }


    private void showShortToast(CharSequence text) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_numb:
                if (rv.getVisibility() != View.VISIBLE)
                    setCustomerList(mContext, rv, dataBeans);
                else
                    adaptiveListHeight(rv, 0, View.INVISIBLE);
                break;
            case R.id.view_outside:
                view.setVisibility(View.GONE);
                if (isShowing()) dismiss();
                break;
            case R.id.tv_guider_name:
                if (rv.getVisibility() != View.VISIBLE)
                    setCustomerGuiderList(mContext, rv, guiderDataBeans);
                else
                    adaptiveListHeight(rv, 0, View.INVISIBLE);
                break;
        }
    }

    @Override
    public void dismiss() {
        stateCallback.onDismiss();
        setBackgroundAlpha(1f);
        super.dismiss();

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        view.setRotationX(180);
    }

    public interface StateCallback {
        /**
         * 这个方法是为了销毁window时滞空popupwindow的实例，popupwindow为空时，才能创建popupwindow。以达到不重复创建popupwindow
         */
        void onDismiss();

        void onUpgradeSuccess();
    }


}
