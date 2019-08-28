package com.holike.crm.presenter.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.OrderReportTargetBean;
import com.holike.crm.model.fragment.OrderReportTargetModel;
import com.holike.crm.view.fragment.OrderReportTargetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wqj on 2018/5/30.
 * 订单交易报表-填写目标
 */

public class OrderReportTargetPresenter extends BasePresenter<OrderReportTargetView, OrderReportTargetModel> {
    /**
     * 获取填写目标数据
     *
     */
    public void getData(String time) {
        model.getData(time == null ? "" : time, new OrderReportTargetModel.GetDataListener() {
            @Override
            public void success(OrderReportTargetBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }

    /**
     * 保存填写目标
     */
    public void saveTarget(TxtWatcher txtWatcher, String time) {
        if (txtWatcher == null || txtWatcher.getDepositListBeanlist().size() == 0 || TextUtils.isEmpty(time)) {
            if (getView() != null)
                    getView().saveTargetFailed("保存失败！");
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (OrderReportTargetBean.DepositListBean bean : txtWatcher.getDepositListBeanlist()) {
            JSONObject object = new JSONObject();
            try {
                object.put("money", TextUtils.isEmpty(bean.getTarget()) ? 0 : Integer.valueOf(bean.getTarget()));
                object.put("param1", bean.getName());
                object.put("city", bean.getCityCode());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        model.saveTarget(URLEncoder.encode(jsonArray.toString()), time, new OrderReportTargetModel.SaveTargetListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().saveTargetSuccess(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().saveTargetFailed(failed);
            }
        });
    }

    public String getTime(String time) {
        if (time == null || time.equals("4")) {
            return "3";
        } else {
            return time;
        }
    }

    public static class TxtWatcher implements TextWatcher {
        private Set<OrderReportTargetBean.DepositListBean> depositListBeanlist = new HashSet<>();
        private OrderReportTargetBean.DepositListBean depositListBean;

        public Set<OrderReportTargetBean.DepositListBean> getDepositListBeanlist() {
            return depositListBeanlist;
        }

        public void clear() {
            depositListBeanlist.clear();
        }

        public void setDepositListBean(OrderReportTargetBean.DepositListBean depositListBean) {
            this.depositListBean = depositListBean;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            depositListBean.setTarget(s.toString());
            depositListBeanlist.add(depositListBean);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
