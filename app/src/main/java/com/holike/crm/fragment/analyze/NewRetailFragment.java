package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.NewRetailBean;
import com.holike.crm.presenter.fragment.NewRetailPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.NewRetailView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/5/31.
 * 新零售
 */

public class NewRetailFragment extends MyFragment<NewRetailPresenter, NewRetailView> implements NewRetailView {

    protected ArrayList<CustomTabEntity> mTabEntities;
    protected String cityCode;
    protected String type;
    protected String time;
    protected String title;
    @BindView(R.id.tab_new_retail_type)
    CommonTabLayout tabType;
    @BindView(R.id.tv_new_retail_table_time)
    TextView tvTableTime;
    @BindView(R.id.tv_new_retail_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.rv_new_retail)
    RecyclerView rv;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_new_retail;
    }

    @Override
    protected NewRetailPresenter attachPresenter() {
        return new NewRetailPresenter();
    }

    @Override
    protected void init() {
        super.init();
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            time = bundle.getString(Constants.TIME);
            type = bundle.getString(Constants.TYPE);
            title = bundle.getString(Constants.TITLE);
        }
        setTitle(getString(R.string.report_item13_title) + (title == null ? "" : "—" + title));
        getData();
    }

    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(cityCode, time, type);
    }

    /**
     * 获取数据成功
     *
     * @param bean
     */
    @Override
    public void getDataSuccess(NewRetailBean bean) {
        dismissLoading();
        initTab(bean.getSelectData());
        tvTableTime.setText(bean.getTimeData());
        tvUpdateTime.setText(bean.getData());
        tvUpdateTime.setVisibility(TextUtils.isEmpty(bean.getData()) ? View.GONE : View.VISIBLE);
        rv.setAdapter(new CommonAdapter<NewRetailBean.DepositListBean>(mContext, bean.getDepositList()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_new_retail_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, NewRetailBean.DepositListBean depositListBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_new_retail_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_new_retail_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_new_retail_report_name);
                TextView tvTarget = holder.obtainView(R.id.tv_item_rv_new_retail_report_target);
                TextView tvComplete = holder.obtainView(R.id.tv_item_rv_new_retail_report_complete);
                TextView tvCompletePercen = holder.obtainView(R.id.tv_item_rv_new_retail_report_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_new_retail_report_rank);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, depositListBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvName.setTextColor(ContextCompat.getColor(mContext, depositListBean.getIsChange() == 1 ? R.color.textColor17 : R.color.textColor8));
                tvArea.setText(depositListBean.getArea());
                tvName.setText(depositListBean.getName());
                tvTarget.setText(depositListBean.getTarget());
                tvCompletePercen.setText(depositListBean.getPercent());
                tvComplete.setText(String.valueOf(depositListBean.getCountsComplete()));
                tvRank.setText(depositListBean.getRank());
                tvArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (depositListBean.getIsClick() == 1) {
                            Map<String, Serializable> params = new HashMap<>();
                            params.put(Constants.CITY_CODE, depositListBean.getCityCode());
                            params.put(Constants.TIME, time);
                            params.put(Constants.TYPE, String.valueOf(depositListBean.getType()));
                            params.put(Constants.TITLE, depositListBean.getArea());
                            startFragment(params, new NewRetailFragment());
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 切换导航
     */
    private void initTab(final List<NewRetailBean.SelectDataBean> selectDataBeans) {
        if (mTabEntities == null) {
            mTabEntities = new ArrayList<>();
            for (NewRetailBean.SelectDataBean bean : selectDataBeans) {
                mTabEntities.add(new TabEntity(bean.getName(), 0, 0));
            }
            tabType.setTabData(mTabEntities);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    time = String.valueOf(selectDataBeans.get(position).getTime());
                    getData();
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            tabType.setCurrentTab(mPresenter.getSelectPosition(time, selectDataBeans));
        }
    }
}
