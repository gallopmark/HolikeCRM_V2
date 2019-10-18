package com.holike.crm.fragment.bank;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.BillListBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.presenter.fragment.BillListPresenter;
import com.holike.crm.presenter.fragment.OrderReportPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.CopyUtil;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.BillListView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class BillListFragment extends MyFragment<BillListPresenter, BillListView> implements BillListView {


    //    @BindView(R.id.tv_day_sum)
//    TextView tvDaySum;
    @BindView(R.id.rv_bill_list)
    RecyclerView rv;
    @BindView(R.id.srl_bill_list)
    SmartRefreshLayout srlBillList;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.sv_total_page)
    ScrollView svTotalPage;
    @BindView(R.id.tv_select_date)
    TextView tvSelectDate;
    @BindView(R.id.tv_this_month)
    TextView mThisMonth;
    @BindView(R.id.iv_select_date)
    ImageView ivSelectDate;
    @BindView(R.id.ll_select_date)
    LinearLayout llSelectDate;
    @BindView(R.id.rl_select_date)
    RelativeLayout rlSelectDate;
    @BindView(R.id.tv_bill_list_xsrw_1)
    TextView tvBillListXsrw1;
    @BindView(R.id.tv_bill_list_dqrdd_2)
    TextView tvBillListDqrdd2;
    @BindView(R.id.tv_bill_list_sxed_3)
    TextView tvBillListSxed3;
    @BindView(R.id.tv_bill_list_sxye4)
    TextView tvBillListSxye4;
    @BindView(R.id.tv_bill_list_qcye5)
    TextView tvBillListQcye5;
    @BindView(R.id.tv_bill_list_crmhk6)
    TextView tvBillListCrmhk6;
    @BindView(R.id.tv_bill_list_crmflje7)
    TextView tvBillListCrmflje7;
    @BindView(R.id.tv_bill_list_ccjzq8)
    TextView tvBillListCcjzq8;
    @BindView(R.id.tv_bill_list_hdzkje9)
    TextView tvBillListHdzkje9;
    @BindView(R.id.tv_bill_list_ccjzh10)
    TextView tvBillListCcjzh10;
    @BindView(R.id.tv_bill_list_kf11)
    TextView tvBillListKf11;
    @BindView(R.id.tv_bill_list_kyje12)
    TextView tvBillListKyje12;
    @BindView(R.id.tv_bill_list_kscddje13)
    TextView tvBillListKscddje13;
    @BindView(R.id.tv_bill_list_cwflqcye14)
    TextView tvBillListCwflqcye14;
    @BindView(R.id.tv_bill_list_cwfldqzj15)
    TextView tvBillListCwfldqzj15;
    @BindView(R.id.tv_bill_list_cwfldqjs16)
    TextView tvBillListCwfldqjs16;
    @BindView(R.id.tv_bill_list_cwflqmye17)
    TextView tvBillListCwflqmye17;
    @BindView(R.id.tv_bill_list_ysqcye18)
    TextView tvBillListYsqcye18;
    @BindView(R.id.tv_bill_list_ysfsye19)
    TextView tvBillListYsfsye19;
    @BindView(R.id.tv_bill_list_ysqmye20)
    TextView tvBillListYsqmye20;

    private boolean showAll;
    private int pageNo = 1;
    private int loadType;
    private static final int LOAD_TYPE_REFRESH = 0;
    private static final int LOAD_TYPE_LOAD_MORE = 1;
    private BillListBean billListBean;
    private String startTime = "";
    private String endTime = "";
    private String orderId;

    @Override
    protected BillListPresenter attachPresenter() {
        return new BillListPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.bill_list));
        mThisMonth.setVisibility(View.INVISIBLE);
//        tvDaySum.setVisibility(View.INVISIBLE);
        rlSelectDate.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = String.valueOf(bundle.getSerializable(Constants.PAGE_TYPE));
            billListBean = (BillListBean) bundle.getSerializable(Constants.BILL_LIST_BEAN);
            showAll = bundle.getBoolean(Constants.BILL_LIST_SHOW_ALL);
        }
        if (orderId != null && orderId.equals("账单列表")) {
            setTitle(getString(R.string.bill_summary));
            setRightMenu(getString(R.string.bill_list_detail));
            showLoading();
            mPresenter.getHeadData(pageNo, "", "");
        } else {
            setTitle(getString(R.string.bill_list_detail));
            rlSelectDate.setVisibility(View.VISIBLE);
            mThisMonth.setVisibility(showAll ? View.INVISIBLE : View.VISIBLE);
            mPresenter.setAdapter(rv);
            srlBillList.setRefreshHeader(new WaterDropHeader(mContext));
            srlBillList.setRefreshFooter(new BallPulseFooter(mContext));
            srlBillList.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                    loadmore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                    refresh(false);
                }
            });
            if (billListBean != null) {
                String text = TimeUtil.stampToString(billListBean.getStartTime(), "yyyy.MM.dd") + "-"
                        + TimeUtil.stampToString(billListBean.getEndTime(), "yyyy.MM.dd");
                tvSelectDate.setText(text);
                loadList(billListBean);
            }
        }
        mThisMonth.setOnClickListener(v -> reload());
        llSelectDate.setOnClickListener(v -> {
            ivSelectDate.setRotationX(180);
            checkDate();
        });
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        if (billListBean == null) return;
        showAll = !TimeUtil.stampToString(startTime, "yyyy.MM.dd").equals(TimeUtil.stampToString(billListBean.getStartTime(), "yyyy.MM.dd")) ||
                !TimeUtil.stampToString(endTime, "yyyy.MM.dd").equals(TimeUtil.stampToString(billListBean.getEndTime(), "yyyy.MM.dd"));
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.PAGE_TYPE, "明细列表");
        params.put(Constants.BILL_LIST_BEAN, billListBean);
        params.put(Constants.BILL_LIST_SHOW_ALL, showAll);
        startFragment(params, new BillListFragment());
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_bill_list;
    }


    @Override
    public void success(BillListBean bean) {
        billListBean = bean;
        dismissLoading();
        String text = TimeUtil.stampToString(bean.getStartTime() + 1, "yyyy.MM.dd") + "-" + TimeUtil.stampToString(bean.getEndTime() + 1, "yyyy.MM.dd");
        tvSelectDate.setText(text);
        if (orderId.equals("账单列表")) {
            svTotalPage.setVisibility(View.VISIBLE);
            rlSelectDate.setVisibility(View.VISIBLE);
            tvBillListXsrw1.setText(textEmptyNumber(bean.getHeadData().getZxsrw()));
            tvBillListDqrdd2.setText(textEmptyNumber(bean.getHeadData().getDqrso()));
            tvBillListSxed3.setText(textEmptyNumber(bean.getHeadData().getLimit()));
            tvBillListSxye4.setText(textEmptyNumber(bean.getHeadData().getLimitLeft()));
            tvBillListQcye5.setText(textEmptyNumber(bean.getHeadData().getZqcye()));
            tvBillListCrmhk6.setText(textEmptyNumber(bean.getHeadData().getZhuok()));
            tvBillListCrmflje7.setText(textEmptyNumber(bean.getHeadData().getZflje()));
            tvBillListCcjzq8.setText(textEmptyNumber(bean.getHeadData().getCcjzq()));
            tvBillListHdzkje9.setText(textEmptyNumber(bean.getHeadData().getHdzkje()));
            tvBillListCcjzh10.setText(textEmptyNumber(bean.getHeadData().getCcjzh()));
            tvBillListKf11.setText(textEmptyNumber(bean.getHeadData().getKoufei()));
            tvBillListKyje12.setText(textEmptyNumber(bean.getHeadData().getZkyje()));
            tvBillListKscddje13.setText(textEmptyNumber(bean.getHeadData().getKscsoje()));
            tvBillListCwflqcye14.setText(textEmptyNumber(bean.getHeadData().getZflqc()));
            tvBillListCwfldqzj15.setText(textEmptyNumber(bean.getHeadData().getZfladd()));
            tvBillListCwfldqjs16.setText(textEmptyNumber(bean.getHeadData().getZfldow()));
            tvBillListCwflqmye17.setText(textEmptyNumber(bean.getHeadData().getZflqm()));
            tvBillListYsqcye18.setText(textEmptyNumber(bean.getHeadData().getZysqc()));
            tvBillListYsfsye19.setText(textEmptyNumber(bean.getHeadData().getZysfs()));
            tvBillListYsqmye20.setText(textEmptyNumber(bean.getHeadData().getZysqm()));
        } else {

            loadList(bean);
        }


    }

    public void loadList(BillListBean bean) {
        srlBillList.setVisibility(View.VISIBLE);
        srlBillList.finishLoadMore();
        srlBillList.finishRefresh();
        if (loadType == LOAD_TYPE_LOAD_MORE) {
            if (bean.getPageData().size() > 0) {
                loadmoreSuccess(bean.getPageData());
            } else {
                loadAllSuccess();
            }
        } else {
            refreshSuccess(bean.getPageData());
        }
    }

    @Override
    protected void reload() {
        super.reload();
        startTime = "";
        endTime = "";
        tvSelectDate.setText("");
//        tvDaySum.setVisibility(View.INVISIBLE);
//        tvDaySum.setText("");
        mThisMonth.setVisibility(View.INVISIBLE);
        srlBillList.setVisibility(View.GONE);
        showLoading();
        mPresenter.getHeadData(pageNo, startTime, endTime);
    }

    @Override
    public void fail(String errorMsg) {
        dismissLoading();
        showShortToast(errorMsg);
        noData(R.drawable.no_result, errorMsg, true);
    }

    @Override
    public void refresh(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        pageNo = 1;
        mPresenter.getHeadData(pageNo, startTime, endTime);
        loadType = LOAD_TYPE_REFRESH;
    }

    @Override
    public void refreshSuccess(List<BillListBean.PageDataBean> listBeans) {
        dismissLoading();
        srlBillList.setEnableLoadMore(true);
        if (listBeans.size() > 0) {
            hasData();
        } else {
            noResult();
        }
        mPresenter.onRefreshCompleted(listBeans);
    }


    @Override
    public void loadmore() {
        pageNo++;
        loadType = LOAD_TYPE_LOAD_MORE;
        mPresenter.getHeadData(pageNo, startTime, endTime);
    }

    @Override
    public void loadmoreSuccess(List<BillListBean.PageDataBean> listBeans) {
        srlBillList.setEnableLoadMore(true);
        mPresenter.onLoadMoreCompleted(listBeans);
    }

    @Override
    public void loadAllSuccess() {
        srlBillList.setEnableLoadMore(false);
        mPresenter.noMoreData();
    }

    @Override
    public void onItemClick(BillListBean.PageDataBean pageDataBean) {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.BILL_DETAIL, pageDataBean);
        startFragment(params, new BillDetialFragment());
    }

    @Override
    public void onItemLongClick(String orderId) {
        CopyUtil.copy(mContext, orderId);
        showShortToast("订单号已复制到粘贴板");
    }

    /**
     * 查询日期
     */
    private List<Date> mSelectedDates;

    public void checkDate() {
        OrderReportPresenter.selectDate(mContext, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
            @Override
            public void onLeftClicked(CalendarPickerDialog dialog) {
//                dialog.dismiss();
//                loadType = LOAD_TYPE_REFRESH;
//                showLoading();
//                mPresenter.getHeadData(pageNo, startTime, endTime);
            }

            @Override
            public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                dialog.dismiss();
                mSelectedDates = selectedDates;
                loadType = LOAD_TYPE_REFRESH;
                if (selectedDates.size() >= 1) {
                    startTime = TimeUtil.dateToStamp(start, false);
                    endTime = TimeUtil.dateToStamp(end, true);
//                    startTime = TimeUtil.dataToStamp(start, "yyyy年MM月dd日");
//                    endTime = TimeUtil.dataToStamp(end, "yyyy年MM月dd日");
                    String text = TimeUtil.stampToString(startTime, "yyyy.MM.dd") + "-" + TimeUtil.stampToString(endTime, "yyyy.MM.dd");
                    tvSelectDate.setText(text);
//                    tvDaySum.setVisibility(View.VISIBLE);
//                    tvDaySum.setText(((end.getTime() - start.getTime()) / 86400000) + 1 + "天");
                    mThisMonth.setVisibility(View.VISIBLE);
                } else {
                    startTime = null;
                    endTime = null;
                }
                showLoading();
                mPresenter.getHeadData(pageNo, startTime, endTime);
            }
        }, dialogInterface -> ivSelectDate.setRotationX(0));
    }
}
