package com.holike.crm.fragment.report.target;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.BusinessObjectivesBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.util.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 */
class EmployeeTargetHelper extends FragmentHelper {

    private Callback mCallback;
    private String mShopId, mShopName;
    private FrameLayout mContainer;

    EmployeeTargetHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        mContainer = obtainView(R.id.fl_container);
        mShopName = mContext.getString(R.string.all_stores);
        onSuccess(IntentValue.getInstance().removeBy("business-objectives-bean"));
    }

    void onSuccess(Object obj) {
        if (mContainer.getChildCount() > 0) {
            mContainer.removeAllViews();
        }
        if (obj == null) {
            LayoutInflater.from(mContext).inflate(R.layout.include_empty_textview, mContainer, true);
        } else {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.include_business_target, mContainer, true);
            final BusinessObjectivesBean bean = (BusinessObjectivesBean) obj;
            final TextView tvSelectShop = contentView.findViewById(R.id.tv_select_shop);
            tvSelectShop.setText(mShopName);
            tvSelectShop.setEnabled(!bean.getShopSelect().isEmpty());
            tvSelectShop.setOnClickListener(view -> {
                List<DictionaryBean> optionItems = new ArrayList<>();
                for (BusinessObjectivesBean.ShopSelectBean selectBean : bean.getShopSelect()) {
                    optionItems.add(new DictionaryBean(selectBean.shopId, selectBean.shopName));
                }
                PickerHelper.showOptionsPicker(mContext, optionItems, (position, b) -> {
                    tvSelectShop.setText(b.name);
                    mShopId = b.id;
                    mShopName = b.name;
                    doRequest();
                });
            });
            ViewStub viewStub = contentView.findViewById(R.id.viewStub);
            if (bean.getUserData().isEmpty()) {
                viewStub.setLayoutResource(R.layout.include_empty_textview);
                viewStub.inflate();
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewStub.getLayoutParams();
                params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
                viewStub.setLayoutResource(R.layout.include_form_data_content);
                View innerView = viewStub.inflate();
                FrameLayout containerLayout = innerView.findViewById(R.id.fl_form_data_container);
                View firstLayout = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_column_80dp, containerLayout, false);
                TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
                tvFirst.setText(mContext.getString(R.string.employee));
                containerLayout.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
                LinearLayout scrollableLayout = containerLayout.findViewById(R.id.ll_scrollable_content);
                View view = LayoutInflater.from(mContext).inflate(R.layout.include_business_target_tablist, scrollableLayout, false);
                scrollableLayout.addView(view, 0);
                RecyclerView rvSide = firstLayout.findViewById(R.id.rv_side);
                rvSide.setLayoutManager(new LinearLayoutManager(mContext));
                RecyclerView rvContent = containerLayout.findViewById(R.id.rv_content);
                rvContent.setLayoutManager(new LinearLayoutManager(mContext));
                RecyclerUtils.setScrollSynchronous(rvSide, rvContent);
                rvSide.setAdapter(new SideAdapter(mContext, bean.getUserData()));
                rvContent.setAdapter(new ContentAdapter(mContext, bean.getUserData()));
            }
        }
    }

    private final class SideAdapter extends AbsFormAdapter<BusinessObjectivesBean.UserDataBean> {


        SideAdapter(Context context, List<BusinessObjectivesBean.UserDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, BusinessObjectivesBean.UserDataBean bean, int position) {
            holder.setText(R.id.tv_name, bean.name);
            holder.setText(R.id.tv_newCountTarget, bean.newCountTarget);
            holder.setText(R.id.tv_newCount, bean.newCount);
            holder.setText(R.id.tv_newCountPercent, bean.newCountPercent);
            holder.setText(R.id.tv_prescaleCountTarget, bean.prescaleCountTarget);
            holder.setText(R.id.tv_prescaleCount, bean.prescaleCount);
            holder.setText(R.id.tv_prescaleCountPercent, bean.prescaleCountPercent);
            holder.setText(R.id.tv_contractTotalTarget, bean.contractTotalTarget);
            holder.setText(R.id.tv_contractTotal, bean.contractTotal);
            holder.setText(R.id.tv_contractTotalPercent, bean.contractTotalPercent);
            holder.setText(R.id.tv_receiverTarget, bean.receiverTarget);
            holder.setText(R.id.tv_receiver, bean.receiver);
            holder.setText(R.id.tv_receiverPercent, bean.receiverPercent);
            holder.setText(R.id.tv_scaleCountTarget, bean.scaleCountTarget);
            holder.setText(R.id.tv_scaleCount, bean.scaleCount);
            holder.setText(R.id.tv_scaleCountPercent, bean.scaleCountPercent);
            holder.setText(R.id.tv_picCountTarget, bean.picCountTarget);
            holder.setText(R.id.tv_picCount, bean.picCount);
            holder.setText(R.id.tv_picCountTargetPercent, bean.picCountTargetPercent);
            holder.setText(R.id.tv_orderTotalTarget, bean.orderTotalTarget);
            holder.setText(R.id.tv_orderTotal, bean.orderTotal);
            holder.setText(R.id.tv_orderTotalPercent, bean.orderTotalPercent);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_target_form_side;
        }
    }

    private final class ContentAdapter extends AbsFormAdapter<BusinessObjectivesBean.UserDataBean> {


        ContentAdapter(Context context, List<BusinessObjectivesBean.UserDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, BusinessObjectivesBean.UserDataBean bean, int position) {
            holder.setText(R.id.tv_name, bean.name);
            holder.setText(R.id.tv_newCountTarget, bean.newCountTarget);
            holder.setText(R.id.tv_newCount, bean.newCount);
            holder.setText(R.id.tv_newCountPercent, bean.newCountPercent);
            holder.setText(R.id.tv_prescaleCountTarget, bean.prescaleCountTarget);
            holder.setText(R.id.tv_prescaleCount, bean.prescaleCount);
            holder.setText(R.id.tv_prescaleCountPercent, bean.prescaleCountPercent);
            holder.setText(R.id.tv_contractTotalTarget, bean.contractTotalTarget);
            holder.setText(R.id.tv_contractTotal, bean.contractTotal);
            holder.setText(R.id.tv_contractTotalPercent, bean.contractTotalPercent);
            holder.setText(R.id.tv_receiverTarget, bean.receiverTarget);
            holder.setText(R.id.tv_receiver, bean.receiver);
            holder.setText(R.id.tv_receiverPercent, bean.receiverPercent);
            holder.setText(R.id.tv_scaleCountTarget, bean.scaleCountTarget);
            holder.setText(R.id.tv_scaleCount, bean.scaleCount);
            holder.setText(R.id.tv_scaleCountPercent, bean.scaleCountPercent);
            holder.setText(R.id.tv_picCountTarget, bean.picCountTarget);
            holder.setText(R.id.tv_picCount, bean.picCount);
            holder.setText(R.id.tv_picCountTargetPercent, bean.picCountTargetPercent);
            holder.setText(R.id.tv_orderTotalTarget, bean.orderTotalTarget);
            holder.setText(R.id.tv_orderTotal, bean.orderTotal);
            holder.setText(R.id.tv_orderTotalPercent, bean.orderTotalPercent);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_target_form_content;
        }
    }

    void doRequest() {
        mCallback.onSelectShop(mShopId);
    }

    void onFailure(String failReason) {
        if (mContainer.getChildCount() > 0) {
            mContainer.removeAllViews();
        }
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainer, true);
        mFragment.noNetwork(failReason);
    }

    interface Callback {
        void onSelectShop(String shopId);
    }
}
