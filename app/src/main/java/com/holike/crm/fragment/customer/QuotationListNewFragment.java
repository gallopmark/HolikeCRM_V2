package com.holike.crm.fragment.customer;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.QuotationBean;
import com.holike.crm.customView.ObservableHorizontalScrollView;
import com.holike.crm.presenter.fragment.QuotationListPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.fragment.QuotationListView;

import butterknife.BindView;

/*报价清单*/
public class QuotationListNewFragment extends MyFragment<QuotationListPresenter, QuotationListView> implements QuotationListView {

    @BindView(R.id.mContentLl)
    LinearLayout mContentLl;
    @BindView(R.id.mHsvTopTab)
    ObservableHorizontalScrollView mHsvTopTab;
    @BindView(R.id.mQuotationContent2)
    LinearLayout mQuotationContent2;
    @BindView(R.id.mQuotationContent1)
    LinearLayout mQuotationContent1;
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.mShadowView)
    View mShadowView;
    @BindView(R.id.tv_quotation_info_product_number)
    TextView llProductNumber;
    @BindView(R.id.v_top_shadow)
    View vTop;
    @BindView(R.id.v_down_shadow)
    View vDown;
    private String orderId;

    @Override
    protected QuotationListPresenter attachPresenter() {
        return new QuotationListPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_quotationlist;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.quotation_info));
        llProductNumber.setVisibility(View.GONE);
        initTitleLayout();
        mPresenter.initContent(mHsvTopTab, rvContent);
        if (getArguments() != null) {
            orderId = (String) getArguments().getSerializable(Constants.ORDER_ID);
        }
        initData();
    }

    private void initTitleLayout() {
        mPresenter.initTitleLayout(mContext, mQuotationContent1, mQuotationContent2);
        mQuotationContent1.setVisibility(View.GONE);
        mQuotationContent2.setVisibility(View.GONE);
    }

    private void initData() {
        showLoading();
        mPresenter.getData(orderId);
    }

    @Override
    public void onSuccess(QuotationBean bean) {
        dismissLoading();
        mQuotationContent1.setVisibility(View.GONE);
        mQuotationContent2.setVisibility(View.GONE);
        if (bean.getDataList() == null || bean.getDataList().isEmpty()) {
            onFail(mContext.getString(R.string.no_value));
        } else {
            if (mContentLl.getVisibility() != View.VISIBLE)
                mContentLl.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vTop.getLayoutParams();
            FrameLayout.LayoutParams paramsDown = (FrameLayout.LayoutParams) vDown.getLayoutParams();
            if (mShadowView.getVisibility() != View.VISIBLE) {
                mShadowView.setVisibility(View.VISIBLE);
            }
            FrameLayout.LayoutParams mShadowParams = (FrameLayout.LayoutParams) mShadowView.getLayoutParams();
            int dp94 = DensityUtil.dp2px(94);
            int dp134 = DensityUtil.dp2px(134);
            boolean isProduct = TextUtils.equals(bean.isProduct, "1");
            if (!isProduct) {
                llProductNumber.setVisibility(View.VISIBLE);
                mQuotationContent1.setVisibility(View.VISIBLE);
                params.width = dp134;
                paramsDown.width = dp134;
            } else {
                mQuotationContent2.setVisibility(View.VISIBLE);
                params.width = dp94;
                paramsDown.width = dp94;
            }
            mShadowParams.leftMargin = params.width;
            vTop.setLayoutParams(params);
            vDown.setLayoutParams(paramsDown);
            mPresenter.update(bean.getDataList(), isProduct);
        }
    }

    @Override
    public void onFail(String errorMsg) {
        dismissLoading();
        if (TextUtils.equals(errorMsg, mContext.getString(R.string.no_value)))
            noResult(getString(R.string.tips_no_corresponding_quotation_list));
        else noNetwork();
    }

    @Override
    protected void reload() {
        initData();
    }

}
