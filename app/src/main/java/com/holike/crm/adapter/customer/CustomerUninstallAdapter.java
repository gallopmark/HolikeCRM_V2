package com.holike.crm.adapter.customer;

import android.content.Context;

import com.holike.crm.R;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 * 待安装客户
 */
public class CustomerUninstallAdapter extends CustomerStatusListAdapter {
    private String mTipsReservationInstall, mTipsSignDate, mTipsSource,
            mTipsOrderDate, mTipsGuide, mTipsDesigner, mTipsInstaller;

    public CustomerUninstallAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsReservationInstall = context.getString(R.string.customer_reservation_install_tips);
        mTipsSignDate = context.getString(R.string.customer_date_of_signing_tips);
        mTipsSource = context.getString(R.string.customer_source_tips);
        mTipsOrderDate = context.getString(R.string.order_date_tips);
        mTipsGuide = context.getString(R.string.customer_guide_tips);
        mTipsDesigner = context.getString(R.string.followup_designer);
        mTipsInstaller = context.getString(R.string.customer_installer_tips);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_install_date, obtain2(mTipsReservationInstall, wrap(bean.appointmentInstallDate))); //预约安装日期
        holder.setText(R.id.tv_signDate, obtain(mTipsSignDate, wrap(bean.contractDate), false)); //签约日期
        holder.setText(R.id.tv_source, obtain(mTipsSource, wrap(bean.source), false)); //来源
        holder.setText(R.id.tv_order_date, obtain(mTipsOrderDate, wrap(bean.orderDate), false)); //下单日期
        holder.setText(R.id.tv_guide, obtain(mTipsGuide, wrap(bean.salesName), false)); //导购
        holder.setText(R.id.tv_designer, obtain(mTipsDesigner, wrap(bean.designer), false)); //设计师
        holder.setText(R.id.tv_installer, obtain(mTipsInstaller, wrap(bean.installUserName), false)); //安装工
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_uninstall_v2;
    }
}
