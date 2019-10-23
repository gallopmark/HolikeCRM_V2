package com.holike.crm.fragment.employee;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.fragment.OnFragmentDataChangedListener;
import com.holike.crm.presenter.fragment.EmployeeStorePresenter;
import com.holike.crm.view.fragment.StoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*新增员工步骤二*/
//v2.0版本以前
@Deprecated
public class EmployeeEditStepTwoFragment extends MyFragment<EmployeeStorePresenter, StoreListView> implements StoreListView, EmployeeStorePresenter.StoreListAdapter.OnItemSelectedListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private boolean isDataUpdated = false;

    private static List<DistributionStoreBean> storeBeans;
    private List<DistributionStoreBean> resultBeans = new ArrayList<>();
    private OnShopSelectListener onShopSelectListener;
    private OnFragmentDataChangedListener onFragmentDataChangedListener;

    public void setOnShopSelectListener(OnShopSelectListener onShopSelectListener) {
        this.onShopSelectListener = onShopSelectListener;
    }

    public void setOnFragmentDataChangedListener(OnFragmentDataChangedListener onFragmentDataChangedListener) {
        this.onFragmentDataChangedListener = onFragmentDataChangedListener;
    }

    public static EmployeeEditStepTwoFragment newInstance(List<DistributionStoreBean> beans) {
        storeBeans = beans;
        return new EmployeeEditStepTwoFragment();
    }

    @Override
    protected EmployeeStorePresenter attachPresenter() {
        return new EmployeeStorePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_edit_step_two;
    }

    @Override
    protected void init() {
        super.init();
        mPresenter.setAdapter(mRecyclerView, this);
        boolean isDetail = false;
        if (getArguments() != null) {
            isDetail = getArguments().getBoolean("isDetail", false);
            boolean isBoss = getArguments().getBoolean("isBoss", false);
            if (storeBeans != null) {
                update(storeBeans, isBoss);
                storeBeans.clear();
                storeBeans = null;
            }
        }
        if (!isDetail) {
            mPresenter.getStoreList(mContext);
        }
    }

    @Override
    public void onShowLoading() {
        showLoading();
    }

    @Override
    public void getStoreList(List<DistributionStoreBean> beans) {
        mPresenter.addAndNotifyDataSetChanged(beans, false);
    }

    @Override
    public void getStoreFailure(String errorMessage) {
        showShortToast(errorMessage);
    }

    @Override
    public void onHideLoading() {
        dismissLoading();
    }

    public boolean isDataUpdated() {
        return isDataUpdated;
    }

    public void update(List<DistributionStoreBean> storeBeans, boolean isBoss) {
        for (DistributionStoreBean bean : storeBeans) {
            if (bean.getIsSelect() == 1) {
                resultBeans.add(bean);
            }
        }
        mPresenter.addAndNotifyDataSetChanged(storeBeans, isBoss);
        isDataUpdated = true;
    }

    @Override
    public void onItemSelected(List<DistributionStoreBean> mSelected) {
        if (onShopSelectListener != null) onShopSelectListener.onShopSelected(mSelected);
        if (onFragmentDataChangedListener != null) {
            onFragmentDataChangedListener.onFragmentDataChanged(resultBeans.size() != mSelected.size() || !resultBeans.containsAll(mSelected));
        }
    }

    /*选中了哪些门店 activity回调*/
    public String getShopIds() {
        List<DistributionStoreBean> mSelected = mPresenter.getSelectedBeans();
        if (mSelected == null || mSelected.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (DistributionStoreBean bean : mSelected) {
            stringBuilder.append(bean.getShopId());
            stringBuilder.append(",");
        }
        if (stringBuilder.lastIndexOf(",") >= 0) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        }
        return stringBuilder.toString();
    }

    @Override
    public void onDestroyView() {
        if (storeBeans != null) {
            storeBeans.clear();
            storeBeans = null;
        }
        super.onDestroyView();
    }

    public interface OnShopSelectListener {
        void onShopSelected(List<DistributionStoreBean> mSelected);
    }
}
