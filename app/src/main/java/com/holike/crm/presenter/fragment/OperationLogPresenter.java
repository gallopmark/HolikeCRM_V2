package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.OpreationLogBean;
import com.holike.crm.customView.WrapContentLinearLayoutManager;
import com.holike.crm.model.fragment.OperationLogModel;
import com.holike.crm.view.fragment.OperationLogView;

import java.util.List;

/**
 * 产品信息
 */
public class OperationLogPresenter extends BasePresenter<OperationLogView, OperationLogModel> {


    public void getData(String orderId) {
        model.getData(orderId, new OperationLogModel.OperationLogListener() {
            @Override
            public void success(List<OpreationLogBean> beans) {
                if (getView() != null)
                    getView().onSuccess(beans);
            }

            @Override
            public void fail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }


    public void setContentAdapter(final Context ctx, final RecyclerView rv, final List<OpreationLogBean> datas) {
        rv.setLayoutManager(new WrapContentLinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<OpreationLogBean>(ctx, datas) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_operation_info_content;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, OpreationLogBean bean, int position) {
                holder.setText(R.id.tv_operation_log_log, TextUtils.isEmpty(bean.getOperationLogContent()) ? "-" : bean.getOperationLogContent());
                holder.setText(R.id.tv_operation_log_operator, TextUtils.isEmpty(bean.getUserName()) ? "-" : bean.getUserName());
                holder.setText(R.id.tv_operation_log_space_nick, TextUtils.isEmpty(bean.getSpaceName()) ? "-" : bean.getSpaceName());
                holder.setText(R.id.tv_operation_log_time, TextUtils.isEmpty(bean.getYear()) && TextUtils.isEmpty(bean.getHour()) ? "-" : bean.getYear() + "\n" + bean.getHour());

                holder.setBackgroundColor(R.id.ll_product_info_content_parent, ContextCompat.getColor(ctx, position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
                holder.setVisibility(R.id.v_footer, datas.size() - 1 == position);
            }
        });
    }

}
