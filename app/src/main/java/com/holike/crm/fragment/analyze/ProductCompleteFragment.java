package com.holike.crm.fragment.analyze;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.bean.OrderRankingBean;
import com.holike.crm.bean.ProductCompleteBean;
import com.holike.crm.dialog.SelectAreaDialog;
import com.holike.crm.presenter.fragment.ProductCompletePresenter;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.view.fragment.ProductCompleteView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pony.xcode.chart.BarChartView;
import pony.xcode.chart.data.BarChartData;

/**
 * Created by wqj on 2018/5/9.
 * 成品目标各月完成率
 */

public class ProductCompleteFragment extends MyFragment<ProductCompletePresenter, ProductCompleteView> implements ProductCompleteView {
    @BindView(R.id.tv_product_complete_area_select)
    TextView tvAreaSelect;
    @BindView(R.id.tv_product_complete_area)
    TextView tvArea;
//    @BindView(R.id.monthCompleteView_product)
//    MonthCompleteChartView monthCompleteView;

    @BindView(R.id.barChartView)
    BarChartView mBarChartView;
    private ProductCompleteBean productCompleteBean;
    private String cityCode;
    private String type;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_product_complete;
    }

    @Override
    protected ProductCompletePresenter attachPresenter() {
        return new ProductCompletePresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        setTitle(getString(R.string.report_product_complete));
        getData();
    }

    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(cityCode, type);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(ProductCompleteBean bean) {
        dismissLoading();
        this.productCompleteBean = bean;
        String tempText = (bean.getSelect() == null || TextUtils.isEmpty(bean.getSelect().getName())) ? "" : bean.getSelect().getName();
        tvAreaSelect.setText(tempText);
        tempText += mContext.getString(R.string.translate_report_month_complete);
        tvArea.setText(tempText);
        setBarData(bean.getPercentList());
//        monthCompleteView.setBeans(productCompleteBean.getPercentList());
        if (cityCode == null) {
            cityCode = bean.getSelect().getCityCode();
        }
    }

    private void setBarData(List<ProductCompleteBean.PercentListBean> dataList) {
        List<BarChartData> chartDataList = new ArrayList<>();
        String[] xAxisTextArray = new String[dataList.size()];
        String[] subXAxisTextArray = new String[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            MonthCompleteBean bean = dataList.get(i);
            float value = ParseUtils.parseFloat(bean.getDepositPercent().replace("%", ""));
            chartDataList.add(new BarChartData("", value, value > 0 ? value + "%" : " "));
            xAxisTextArray[i] = TextUtils.isEmpty(bean.getMonth()) ? "" : bean.getMonth();
            subXAxisTextArray[i] = TextUtils.isEmpty(bean.getDay()) ? "" : bean.getDay().replace("-", "\n至\n");
        }
        mBarChartView.withData(chartDataList, xAxisTextArray, "%")
                .withSubXAxisTextArray(subXAxisTextArray)
                .barValueUnitEdge(false)
                .start();
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @OnClick({R.id.btn_product_complete_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_product_complete_area:
                if (productCompleteBean != null && productCompleteBean.getSelectData() != null) {
                    new SelectAreaDialog(mContext, getString(R.string.dialog_title_select_area), cityCode, productCompleteBean.getSelectData(), new SelectAreaDialog.SelectAreaListener() {
                        @Override
                        public void select(OrderRankingBean.SelectDataBean selectDataBean) {
                            cityCode = selectDataBean.getCityCode();
                            type = selectDataBean.getType();
                            getData();
                        }
                    }).show();
                }
                break;
        }
    }

}
