package com.holike.crm.fragment.monthdata;

import android.content.Context;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.MonthDataInstallManagerBean;
import com.holike.crm.helper.TextSpanHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/9.
 * Copyright holike possess 2019.
 * 安装经理本月数据帮助类
 */
class InstallManagerMonthDataHelper extends MonthDataHelper {

    private List<MonthDataInstallManagerBean.ArrBean> mItems;
    private ItemContentAdapter mAdapter;

    InstallManagerMonthDataHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
    }

    void initView() {
        TextView tvInstallArea = mFragmentView.findViewById(R.id.tv_install_area);
        tvInstallArea.setText(TextSpanHelper.getSquareMeter(tvInstallArea.getText().toString()));
        RecyclerView recyclerView = mFragmentView.findViewById(R.id.recyclerView);
        mItems = new ArrayList<>();
        mAdapter = new ItemContentAdapter(mContext, mItems);
        recyclerView.setAdapter(mAdapter);
    }

    void onSuccess(MonthDataInstallManagerBean bean) {
        if (bean != null && !bean.getArr().isEmpty()) {
            this.mItems.clear();
            this.mItems.addAll(bean.getArr());
            mAdapter.notifyDataSetChanged();
        }
    }

    static class ItemContentAdapter extends CommonAdapter<MonthDataInstallManagerBean.ArrBean> {

        ItemContentAdapter(Context context, List<MonthDataInstallManagerBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_installmanager_content;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataInstallManagerBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_name, bean.name);
            holder.setText(R.id.tv_install_count, bean.installed); //安装数
            holder.setText(R.id.tv_install_area, bean.area);
            holder.setText(R.id.tv_complete_rate, bean.firstSuccess);//一次安装完成率
        }
    }
}
