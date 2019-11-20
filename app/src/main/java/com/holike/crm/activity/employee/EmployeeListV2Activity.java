package com.holike.crm.activity.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.EmployeeBeanV2;
import com.holike.crm.presenter.activity.EmployeeListV2Presenter;
import com.holike.crm.view.activity.EmployeeListV2View;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pony on 2019/8/6.
 * Copyright holike possess 2019.
 * （员工管理）员工列表 v2.0
 */
public class EmployeeListV2Activity extends MyFragmentActivity<EmployeeListV2Presenter, EmployeeListV2View>
        implements EmployeeListHelper.Callback, EmployeeListV2View {

    private EditText mSearchEditText;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_create)
    TextView mCreateTextView;
    private EmployeeListHelper mHelper;

    @Override
    protected EmployeeListV2Presenter attachPresenter() {
        return new EmployeeListV2Presenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employeelistv2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSearchEditText = setSearchBar(R.string.employee_list_search_hint2);
        mSearchEditText.setGravity(Gravity.CENTER | Gravity.START);
        mHelper = new EmployeeListHelper(this, this);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> mHelper.onRefresh());
    }

    @OnClick({R.id.tv_select_shop, R.id.tv_select_role, R.id.tv_create})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_create) {
            startActivity(EmployeeEditV2Activity.class);
        } else {
            mHelper.onViewClicked(view);
        }
    }

    @Override
    protected void doSearch() {
        mHelper.doSearch();
    }

    @Override
    public void onQuery(String shopId, String roleCode, boolean isRefresh) {
        String content = mSearchEditText.getText().toString();
        if (!isRefresh) {
            showLoading();
        }
        mPresenter.getEmployeeList(content, shopId, "", roleCode);
    }

    @Override
    public void onSuccess(List<EmployeeBeanV2> list) {
        dismissLoading();
        mHelper.onSuccess(list, mRefreshLayout);
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mHelper.onFailure(failReason, mRefreshLayout);
    }

    @Override
    public void reload() {
        mHelper.onQuery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mHelper.onQuery();
        }
    }
}
