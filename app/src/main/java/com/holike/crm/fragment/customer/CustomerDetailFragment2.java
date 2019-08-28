package com.holike.crm.fragment.customer;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.dialog.CancelDialog;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.presenter.activity.CustomerDetailPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.activity.CustomerDetailView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/*客户详情*/
public class CustomerDetailFragment2 extends MyFragment<CustomerDetailPresenter, CustomerDetailView>
        implements View.OnClickListener, CustomerDetailView, CustomerDetailPresenter.OnMultiItemClickListener, CustomerDetailPresenter.RevokeCallback {
    @BindView(R.id.mRecyclerView)
    WrapperRecyclerView mRecyclerView;
    private TextView mNameTextView, mPhoneTextView,
            mTypeTextView, mTimeTextView;
    //    private RecyclerView mHouseRv;
//    private String currentHouseId;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_detail2;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(mContext.getString(R.string.customer_manage_title));
        setRightMenu(mContext.getString(R.string.message_title));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_customer_detail, new LinearLayout(mContext), false);
        TextView editTextView = headerView.findViewById(R.id.tv_customer_detail_edit);
        TextView addHouseTextView = headerView.findViewById(R.id.btn_customer_detail_add_house);
        editTextView.setOnClickListener(this);
        addHouseTextView.setOnClickListener(this);
        mNameTextView = headerView.findViewById(R.id.tv_customer_detail_name);
        mPhoneTextView = headerView.findViewById(R.id.tv_customer_detail_phone);
        mTypeTextView = headerView.findViewById(R.id.tv_customer_detail_customer_type);
        mTimeTextView = headerView.findViewById(R.id.tv_customer_detail_time);
        mPhoneTextView.setOnClickListener(this);
        mRecyclerView.addHeaderView(headerView);
        mPresenter.setDetailAdapter(mRecyclerView, this);
        initData();
    }

    @Override
    protected void clickRightMenu(String text, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    private void initData() {
        String personalId = "";
        if (getArguments() != null) {
//            currentHouseId = getArguments().getString("houseId");
            personalId = getArguments().getString("personalId");
        }
        showLoading();
        mPresenter.getData(personalId);
    }

    @Override
    protected CustomerDetailPresenter attachPresenter() {
        return new CustomerDetailPresenter();
    }

    @Override
    public void getCustomerSuccess(CustomerDetailBean bean) {
        dismissLoading();
        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        CustomerDetailBean.PersonalBean personalBean = bean.getPersonal();
        if (personalBean != null) {
            mNameTextView.setText(personalBean.getUserName());
            mPhoneTextView.setText(personalBean.getPhoneNumber());
            mTypeTextView.setText(personalBean.getSourceName());
            String entryTime = mContext.getString(R.string.entry_time_tips);
            if (!TextUtils.isEmpty(personalBean.getCreateDate())) {
                entryTime += TimeUtil.stampToString(String.valueOf(personalBean.getCreateDate()), "yyyy-MM-dd");
            }
            mTimeTextView.setText(entryTime);
            copy(mPhoneTextView, personalBean.getPhoneNumber());
        }
        mPresenter.update(mContext, bean, mPresenter.getSelectPosition());
    }

    @Override
    public void getCustomerFailed(String failed) {
        dismissLoading();
        dealWithFailed(failed, true);
    }

    @Override
    public void onClick(View view) {
        CustomerDetailBean detailBean = mPresenter.getDetailBean();
        if (detailBean == null) return;
        switch (view.getId()) {
            case R.id.tv_customer_detail_edit:
                MobclickAgent.onEvent(mContext, "customerdetails_edit_customer");
                Map<String, Serializable> paramsPersonal = new HashMap<>();
                paramsPersonal.put(Constants.PERSONAL_BEAN, detailBean.getPersonal());
                startFragment(paramsPersonal, new EditCustomerFragment());
                break;
            case R.id.btn_customer_detail_add_house:
                MobclickAgent.onEvent(mContext, "customerdetails_add_house");
                Map<String, Serializable> paramsHouse = new HashMap<>();
                paramsHouse.put(Constants.PERSONAL_BEAN, detailBean.getPersonal());
                paramsHouse.put(Constants.HOUSE_TYPE, Constants.ADD_HOUSE);
                startFragment(paramsHouse, new EditHouseFragment());
                break;
            case R.id.tv_customer_detail_phone:
                if (mContext != null && detailBean.getPersonal() != null) {
                    ((BaseActivity) mContext).call(detailBean.getPersonal().getPhoneNumber());
                }
                break;
        }
    }

    @Override
    public void onHouseSelected(CustomerDetailBean.CustomerDetailInfoListBean bean, String houseId, int position) {
//        currentHouseId = houseId;
    }

    @Override
    public void onEditClick(CustomerDetailBean.CustomerDetailInfoListBean infoListBean, CustomerDetailBean.PersonalBean personalBean) {
        Map<String, Serializable> paramsHouse = new HashMap<>();
        paramsHouse.put(Constants.PERSONAL_BEAN, personalBean);
        paramsHouse.put(Constants.HOUSE_INFO, infoListBean.getListHouseInfo());
        paramsHouse.put(Constants.HOUSE_TYPE, Constants.EDIT_HOUSE);
        if (infoListBean.getListDetails() != null && !infoListBean.getListDetails().isEmpty()) {
            paramsHouse.put(Constants.LIST_DETAILS_BEAN, infoListBean.getListDetails().get(0));
        }
        startFragment(paramsHouse, new EditHouseFragment());
    }

    @Override
    public void onOrderClick(String orderId) {
        OrderDetailsActivity.open(this, orderId);
    }

    @Override
    public void onOperateClick(boolean isShowed, CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean listStatusBean,
                               CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean, CustomerDetailBean.PersonalBean personalBean, int position) {
        if (isShowed) {
            showShortToast(mContext.getString(R.string.customer_operate_repeat_tips));
        } else {
            Map<String, Serializable> params = new HashMap<>();
            params.put(Constants.CUSTOMER_STATUS, listStatusBean);
            params.put(Constants.HOUSE_INFO, houseInfoBean);
            params.put(Constants.PERSONAL_BEAN, personalBean);
            Fragment fragment = mPresenter.operate(mContext, listStatusBean.getCustomerStatus());
            if (fragment != null) {
                startFragment(params, fragment);
            }
        }
    }

    @Override
    public void onCanceled(CustomerDetailBean.CustomerDetailInfoListBean.ListHistoryBean.HistoryBean historyBean, CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean) {
        if (TextUtils.equals(historyBean.getCanceled(), "1")) {
            showShortToast(R.string.tips_cannot_cancel);
        } else {
            CancelDialog dialog = new CancelDialog(mContext).setListener(cancelReason -> {
                if (TextUtils.isEmpty(cancelReason)) {
                    showShortToast(R.string.tips_input_cancel_reason);
                } else {
                    mPresenter.onEvent(mContext, historyBean.getOperateCode());
                    revoke(cancelReason, historyBean.getOperateCode(), houseInfoBean.getHouseId(), historyBean.getToStatusCode());
                }
            });
            dialog.show();
        }
    }

    /*执行撤销请求*/
    private void revoke(String cancelReason, String operateCode, String houseId, String optCode) {
        showLoading();
        mPresenter.revoke(cancelReason, operateCode, houseId, optCode, this);
    }

    @Override
    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
        super.onFinishResult(requestCode, resultCode, result);
        if (resultCode == Constants.RESULT_CODE_OPERATE_SUCCESS) {
            initData();
        } else if (resultCode == Constants.RESULT_CODE_ADD_HOUSE_SUCCESS) {
            initData();
        }
    }

    @Override
    protected void reload() {
        initData();
    }

    /*撤销成功*/
    @Override
    public void onRevokeSuccess(String success) {
        dismissLoading();
        showShortToast(MyJsonParser.getShowMessage(success));
        initData();
    }

    /*撤销失败*/
    @Override
    public void onRevokeFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
