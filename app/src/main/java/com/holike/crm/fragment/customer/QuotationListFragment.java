//package com.holike.crm.fragment.customer;
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.holike.crm.R;
//import com.holike.crm.base.MyFragment;
//import com.holike.crm.bean.QuotationBean;
//import com.holike.crm.presenter.fragment.QuotationListPresenter;
//import com.holike.crm.util.Constants;
//import com.holike.crm.util.DensityUtil;
//import com.holike.crm.view.fragment.QuotationListView;
//
//import butterknife.BindView;
//
///**
// * 报价清单
// */
//@Deprecated
//public class QuotationListFragment extends MyFragment<QuotationListPresenter, QuotationListView> implements QuotationListView {
//    @BindView(R.id.mContentLl)
//    LinearLayout mContentLl;
//    @BindView(R.id.rv_quotation_info_side)
//    RecyclerView rvSide;
//    @BindView(R.id.rv_quotation_info_center)
//    RecyclerView rvCenter;
//    @BindView(R.id.ll_no_finish_product)
//    LinearLayout mQuotationContent2;
//    @BindView(R.id.ll_finish_product)
//    LinearLayout llFinishProduct;
//    @BindView(R.id.tv_quotation_info_product_number)
//    TextView llProductNumber;
//    @BindView(R.id.v_top_shadow)
//    View vTop;
//    @BindView(R.id.v_down_shadow)
//    View vDown;
//    private String orderId;
//
//    @Override
//    protected void initContent() {
//        super.initContent();
//        setTitle(getString(R.string.quotation_info));
//        mQuotationContent2.setVisibility(View.GONE);
//        llFinishProduct.setVisibility(View.GONE);
//        llProductNumber.setVisibility(View.GONE);
//        if (getArguments() != null) {
//            orderId = (String) getArguments().getSerializable(Constants.ORDER_ID);
//        }
//        initData();
//    }
//
//    private void initData() {
//        showLoading();
//        mPresenter.getData(orderId);
//    }
//
//    @Override
//    protected int setContentViewId() {
//        return R.layout.fragment_quoteinfo;
//    }
//
//    @Override
//    protected QuotationListPresenter attachPresenter() {
//        return new QuotationListPresenter();
//    }
//
//    @Override
//    public void onSuccess(QuotationBean bean) {
//        dismissLoading();
//        if (bean.getDataList() == null || bean.getDataList().isEmpty()) {
//            onFail(mContext.getString(R.string.no_value));
//        } else {
//            if (mContentLl.getVisibility() != View.VISIBLE)
//                mContentLl.setVisibility(View.VISIBLE);
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vTop.getLayoutParams();
//            FrameLayout.LayoutParams paramsDown = (FrameLayout.LayoutParams) vDown.getLayoutParams();
//            if (TextUtils.equals(bean.isProduct, "0")) {
//                mQuotationContent2.setVisibility(View.VISIBLE);
//                llProductNumber.setVisibility(View.VISIBLE);
//                params.width = DensityUtil.dp2px(134);
//                paramsDown.width = DensityUtil.dp2px(134);
//            } else {
//                llFinishProduct.setVisibility(View.VISIBLE);
//                params.width = DensityUtil.dp2px(94);
//                paramsDown.width = DensityUtil.dp2px(94);
//            }
//            vTop.setLayoutParams(params);
//            vDown.setLayoutParams(paramsDown);
//            mPresenter.setSideList(getActivity(), rvSide, bean);
//            mPresenter.setContentList(getActivity(), rvCenter, bean);
//        }
////        setScrollSynchronous(rvSide, rvCenter);
////        setScrollSynchronous(rvCenter, rvSide);
//    }
//
//    /**
//     * 设置2个 RecyclerView 同步滚动
//     *
//     * @param hostRv   导向者
//     * @param syncerRv 被导向者
//     */
//    @Deprecated
//    public void setScrollSynchronous(RecyclerView hostRv, final RecyclerView syncerRv) {
//        hostRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
//                    syncerRv.scrollBy(dx, dy);
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void onFail(String errorMsg) {
//        dismissLoading();
//        if (TextUtils.equals(errorMsg, mContext.getString(R.string.no_value)))
//            noResult(getString(R.string.tips_no_corresponding_quotation_list));
//        else noNetwork();
//    }
//
//    @Override
//    protected void reload() {
//        initData();
//    }
//}
