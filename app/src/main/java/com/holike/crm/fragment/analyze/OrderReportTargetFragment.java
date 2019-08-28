package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OrderReportBean;
import com.holike.crm.bean.OrderReportTargetBean;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.presenter.fragment.OrderReportTargetPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.OrderReportTargetView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/5/30.
 * 订单交易报表-填写目标
 */

public class OrderReportTargetFragment extends MyFragment<OrderReportTargetPresenter, OrderReportTargetView> implements OrderReportTargetView {
    @BindView(R.id.tab_order_report_target_type)
    CommonTabLayout tabType;
    @BindView(R.id.tv_order_report_target_date)
    TextView tvDate;
    @BindView(R.id.rv_order_report_target)
    RecyclerView rv;
    private OrderReportTargetBean bean;
    private String time = "1";
    private ArrayList<CustomTabEntity> mTabEntities;
    private OrderReportTargetPresenter.TxtWatcher txtWatcher = new OrderReportTargetPresenter.TxtWatcher();

    @Override
    public boolean onBackPressed() {
        finishFragment(0, Constants.RESULT_CODE_REFRESH_ORDER_REPORT, null);
        return true;
    }

    @Override
    protected void back() {
        finishFragment(0, Constants.RESULT_CODE_REFRESH_ORDER_REPORT, null);
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item2_write_target_title));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            time = mPresenter.getTime(bundle.getString(Constants.TIME));
        }
        getData();
    }

    private void getData() {
        showLoading();
        mPresenter.getData(time);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_order_report_target;
    }

    @Override
    protected OrderReportTargetPresenter attachPresenter() {
        return new OrderReportTargetPresenter();
    }

    @Override
    public void getDataSuccess(OrderReportTargetBean orderReportTargetBean) {
        dismissLoading();
        txtWatcher = new OrderReportTargetPresenter.TxtWatcher();
        bean = orderReportTargetBean;
        initTab(bean.getSelectData());
        setTitle((TextUtils.isEmpty(bean.getTitle()) ? "" : bean.getTitle() + "—") + getString(R.string.report_item2_write_target_title));
        showDate(bean.getTimeData().getStartTime() + "", bean.getTimeData().getEndTime() + "");
        rv.setAdapter(new CommonAdapter<OrderReportTargetBean.DepositListBean>(mContext, this.bean.getDepositList()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_order_report_target;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, OrderReportTargetBean.DepositListBean depositListBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_order_report_target);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_order_report_target_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_order_report_target_name);
                final EditText tvTarget = holder.obtainView(R.id.tv_item_rv_order_report_target_target);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvName.setTextColor(ContextCompat.getColor(mContext, depositListBean.getIsChange() == 1 ? R.color.textColor17 : R.color.textColor8));
                tvArea.setText(depositListBean.getArea());
                tvName.setText(depositListBean.getName());
                tvTarget.setText(depositListBean.getTarget());
                tvTarget.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        txtWatcher.setDepositListBean(bean.getDepositList().get(position));
                        tvTarget.addTextChangedListener(txtWatcher);
                    } else {
                        tvTarget.removeTextChangedListener(txtWatcher);
                        tvTarget.post(() -> notifyItemChanged(position));
                    }
                });
            }
        });
    }

    private void initTab(final List<OrderReportBean.SelectDataBean> selectDataBeans) {
        if (mTabEntities == null) {
            mTabEntities = new ArrayList<>();
            for (OrderReportBean.SelectDataBean bean : selectDataBeans) {
                mTabEntities.add(new TabEntity(bean.getName(), 0, 0));
            }
            tabType.setTabData(mTabEntities);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    time = selectDataBeans.get(position).getTime();
                    getData();
                }

                @Override
                public void onTabReselect(int position) {
                }
            });
            tabType.setCurrentTab(OrderReportPresenter.getSelectPosition(time, selectDataBeans));
        }
    }

    private void showDate(String startTime, String endTime) {
        String dateTime = TimeUtil.stampToString(startTime, "yyyy.MM.dd") + "—" + TimeUtil.stampToString(endTime, "yyyy.MM.dd");
        tvDate.setText(dateTime);
    }

    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void saveTargetSuccess(String success) {
        dismissLoading();
        txtWatcher.clear();
        showShortToast("保存目标成功");
        back();
    }

    @Override
    public void saveTargetFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }


    @OnClick({R.id.btn_translate_report_save})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_translate_report_save) {
            hideSoftInput(tvDate);
            mPresenter.saveTarget(txtWatcher, time);
        }
    }
}
