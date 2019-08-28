package com.holike.crm.fragment.analyze;

import android.view.View;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OrderRankingBean;
import com.holike.crm.bean.ProductCompleteBean;
import com.holike.crm.customView.MonthCompleteChartView;
import com.holike.crm.dialog.SelectAreaDialog;
import com.holike.crm.presenter.fragment.ProductCompletePresenter;
import com.holike.crm.view.fragment.ProductCompleteView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/5/9.
 * 成品目标各月完成率
 */

public class ProductCompleteFragment extends MyFragment<ProductCompletePresenter, ProductCompleteView> implements ProductCompleteView {
    @BindView(R.id.tv_product_complete_area_select)
    TextView tvAreaSelect;
    @BindView(R.id.tv_product_complete_area)
    TextView tvArea;
    @BindView(R.id.monthCompleteView_product)
    MonthCompleteChartView monthCompleteView;

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
     *
     * @param productCompleteBean
     */
    @Override
    public void getDataSuccess(ProductCompleteBean productCompleteBean) {
        dismissLoading();
        this.productCompleteBean = productCompleteBean;
        tvAreaSelect.setText(productCompleteBean.getSelect().getName());
        tvArea.setText(productCompleteBean.getSelect().getName() + getString(R.string.translate_report_month_complete));
        monthCompleteView.setBeans(productCompleteBean.getPercentList());
        if (cityCode == null) {
            cityCode = productCompleteBean.getSelect().getCityCode();
        }
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
