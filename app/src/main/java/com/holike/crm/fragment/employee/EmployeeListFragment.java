package com.holike.crm.fragment.employee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.employee.EmployeeDetailsActivity;
import com.holike.crm.activity.employee.EmployeeEditActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeBean;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.local.MainDataSource;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.presenter.fragment.EmployeeListPresenter;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.fragment.EmployeeListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class EmployeeListFragment extends MyFragment<EmployeeListPresenter, EmployeeListView>
        implements View.OnClickListener, EmployeeListView, EmployeeListPresenter.OnItemClickListener, EmployeeModel.OnGetStoreCallback {

    private EditText mSearchEditText;
    @BindView(R.id.mLine)
    View mLine;
    @BindView(R.id.mStoreTextView)
    TextView mStoreTextView;
    @BindView(R.id.mBillTextView)
    TextView mBillTextView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mCreateTv)
    TextView mCreateTv;

    private String store = "";
    private String bill = "";

    private List<DistributionStoreBean> dictionaryBeans;
    private int clickedId;
    private boolean isRefresh;

    @Override
    protected EmployeeListPresenter attachPresenter() {
        return new EmployeeListPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employeelist;
    }

    @Override
    protected void init() {
        super.init();
        initViewData();
        getEmployeeList();
    }

    private void initViewData() {
        mSearchEditText = setSearchBar(R.string.employee_list_search_hint);
        mSearchEditText.setGravity(Gravity.CENTER | Gravity.START);
        mPresenter.setAdapter(mContext, mRecyclerView, this);
        mStoreTextView.setOnClickListener(this);
        mBillTextView.setOnClickListener(this);
        mCreateTv.setOnClickListener(this);
        dictionaryBeans = MainDataSource.getShopData(mContext);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> onRefresh());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mCreateTv) {
            startActivityForResult(EmployeeEditActivity.class, 996);
        } else {
            KeyBoardUtil.hideKeyboard(mSearchEditText);
            clickedId = v.getId();
            if (dictionaryBeans == null) {
                mPresenter.getStoreList(this);
            } else {
                showPopupWindow();
            }
        }
    }

    private void showPopupWindow() {
        if (clickedId == R.id.mStoreTextView) {
            mPresenter.showPopupWindow(mContext, mLine, dictionaryBeans, (bean, isFirst) -> {
                if (isFirst) resetStore();
                else setTextParams(bean, mStoreTextView);
                mSearchEditText.setText("");
                getEmployeeList();
            }, mStoreTextView, 1);
        } else if (clickedId == R.id.mBillTextView) {
            mPresenter.showPopupWindow(mContext, mLine, getStatusList(), (bean, isFirst) -> {
                if (isFirst) resetBill();
                else setTextParams(bean, mBillTextView);
                mSearchEditText.setText("");
                getEmployeeList();
            }, mBillTextView, 2);
        }
    }

    private void setTextParams(DistributionStoreBean bean, TextView mTextView) {
        if (mTextView.getId() == R.id.mStoreTextView) {
            store = bean.getShopId();
        } else {
            bill = bean.getShopId();
        }
        mTextView.setText(bean.getShopName());
        mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
    }

    private void resetStore() {
        store = "";
        mStoreTextView.setText(mContext.getString(R.string.employee_list_select_store));
        mStoreTextView.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
    }

    private void resetBill() {
        bill = "";
        mBillTextView.setText(mContext.getString(R.string.employee_list_select_bill));
        mBillTextView.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
    }

    private List<DistributionStoreBean> getStatusList() {
        List<DistributionStoreBean> list = new ArrayList<>();
        list.add(new DistributionStoreBean("1", mContext.getString(R.string.valid)));
        list.add(new DistributionStoreBean("0", mContext.getString(R.string.invalid)));
        return list;
    }

    private void onRefresh() {
        isRefresh = true;
        getEmployeeList();
    }

    @Override
    protected void doSearch() {
        KeyBoardUtil.hideKeyboard(mSearchEditText);
        resetParams();
        getEmployeeList();
    }

    /*点击搜索不会对筛选结果页面进行搜索，而是对所有员工数据进行搜索*/
    private void resetParams() {
        resetStore();
        resetBill();
    }

    @Override
    public void onItemClick(EmployeeBean bean, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("employeeId", bean.getUserId());
        startActivityForResult(EmployeeDetailsActivity.class, bundle, 10086);
    }

    private void getEmployeeList() {
        String searchContent = mSearchEditText.getText().toString();
        mPresenter.getEmployeeList(searchContent, store, bill);
    }

    @Override
    public void onShowLoading() {
        if (!isRefresh) showLoading();
    }

    @Override
    public void onHideLoading() {
        dismissLoading();
    }

    @Override
    public void getEmployeeList(List<EmployeeBean> mData) {
        smartRefreshLayout.finishRefresh();
        if (mData.size() > 0) {
            hasData();
            smartRefreshLayout.setVisibility(View.VISIBLE);
            mPresenter.addAndNotifyDataSetChanged(mData);
        } else {
            smartRefreshLayout.setVisibility(View.GONE);
            noResult();
        }
    }

    @Override
    public void getEmployeeListError(String errorMessage) {
        smartRefreshLayout.finishRefresh();
        if (isNoAuth(errorMessage)) {
            mPresenter.clearData();
            noAuthority();
        } else {
            if (isRefresh) {
                showShortToast(errorMessage);
                isRefresh = false;
            } else {
                smartRefreshLayout.setVisibility(View.GONE);
                noNetwork();
            }
        }
    }

    @Override
    protected void reload() {
        isRefresh = false;
        getEmployeeList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 996 && resultCode == Activity.RESULT_OK) {           //新增员工返回
            getEmployeeList();  //刷新列表
        } else if (requestCode == 10086 && resultCode == Activity.RESULT_OK) {
            getEmployeeList();    //刷新列表
        }
    }

    @Override
    public void onGetStoreStart() {
        showLoading();
    }

    @Override
    public void onGetStoreList(List<DistributionStoreBean> beans) {
        this.dictionaryBeans = beans;
        MainDataSource.saveShopData(mContext, MyJsonParser.fromBeanToJson(beans));
        showPopupWindow();
    }

    @Override
    public void onGetStoreFalure(String message) {
        showShortToast(message);
    }

    @Override
    public void onGetStoreComplete() {
        dismissLoading();
    }
}
