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
import com.holike.crm.bean.BuildStoreBean;
import com.holike.crm.presenter.fragment.BuildStorePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.BuildStoreView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
/**
 * Created by wqj on 2018/5/9.
 * 建店数据报表
 */

public class BuildStoreFragment extends MyFragment<BuildStorePresenter, BuildStoreView> implements BuildStoreView {
    @BindView(R.id.tab_build_stort_type)
    CommonTabLayout tabType;
    @BindView(R.id.tv_build_stort_table_time)
    TextView tvTime;
    @BindView(R.id.rv_build_stort)
    RecyclerView rv;

    ArrayList<CustomTabEntity> mTabEntities;
    private String cityCode;
    private String type;
    private String time;
    private String title;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_build_store;
    }

    @Override
    protected BuildStorePresenter attachPresenter() {
        return new BuildStorePresenter();
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
        } else {
            setLeft(getString(R.string.report_title));
        }
        if (title != null) {
            setTitle(getString(R.string.report_item6_title) + "—" + title);
        } else {
            setTitle(getString(R.string.report_item6_title));
        }
        getData();
    }

    @Override
    public void getData() {
        mPresenter.getData(cityCode, time, type);
        showLoading();
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
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(BuildStoreBean buildStoreBean) {
        dismissLoading();
        initTab(buildStoreBean.getQuarter());
        tvTime.setText(buildStoreBean.getTimeData());
        rv.setAdapter(new CommonAdapter<BuildStoreBean.PercentDataBean>(mContext, buildStoreBean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_build_store_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, BuildStoreBean.PercentDataBean percentDataBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_build_store_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_build_store_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_build_store_report_name);
                TextView tvTarget = holder.obtainView(R.id.tv_item_rv_build_store_report_target);
                TextView tvComplete = holder.obtainView(R.id.tv_item_rv_build_store_report_complete);
                TextView tvCompletePercen = holder.obtainView(R.id.tv_item_rv_build_store_report_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_build_store_report_rank);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvArea.setText(percentDataBean.getArea());
                tvName.setText(percentDataBean.getName());
                tvTarget.setText(percentDataBean.getCountsTarget());
                tvCompletePercen.setText(percentDataBean.getPercent());
                tvComplete.setText(String.valueOf(percentDataBean.getCountsComplete()));
                tvRank.setText(percentDataBean.getRank());
                tvArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (percentDataBean.getIsClick() == 1) {
                            Map<String, Serializable> params = new HashMap<>();
                            params.put(Constants.CITY_CODE, percentDataBean.getCityCode());
                            params.put(Constants.TIME, time);
                            params.put(Constants.TYPE, String.valueOf(percentDataBean.getType()));
                            params.put(Constants.TITLE, percentDataBean.getArea());
                            startFragment(params, new BuildStoreFragment());
                        }
                    }
                });
            }
        });
    }

    /**
     * 月份切换
     *
     * @param item1
     */
    private void initTab(String item1) {
        if (mTabEntities == null) {
            mTabEntities = new ArrayList<>();
            mTabEntities.add(new TabEntity(item1, 0, 0));
            mTabEntities.add(new TabEntity("全年目标", 0, 0));
            tabType.setTabData(mTabEntities);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    time = String.valueOf(position + 1);
                    getData();
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            tabType.setCurrentTab(TextUtils.isEmpty(time) ? 0 : Integer.parseInt(time) - 1);
        }
    }

}
