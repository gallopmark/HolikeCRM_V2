package com.holike.crm.fragment.report.target;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.BusinessObjectivesBean;

import java.util.List;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 * 经营目标
 */
class BusinessTargetHelper extends FragmentHelper {

    BusinessTargetHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
        init();
    }

    private void init() {
        BusinessObjectivesBean bean = (BusinessObjectivesBean) IntentValue.getInstance().removeBy("business-objectives-bean");
        RecyclerView recyclerView = obtainView(R.id.recyclerView);
        recyclerView.setAdapter(new AbsAdapter(mContext, bean.getShopData()));
    }

    private final class AbsAdapter extends AbsFormAdapter<BusinessObjectivesBean.ShopDataBean> {


        AbsAdapter(Context context, List<BusinessObjectivesBean.ShopDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, BusinessObjectivesBean.ShopDataBean bean, int position) {
            holder.setText(R.id.tv_store, bean.shopName);
            holder.setText(R.id.tv_money, bean.money);
            holder.setText(R.id.tv_count, bean.count);
            holder.setText(R.id.tv_percent, bean.percent);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_business_target_form_content;
        }
    }
}
