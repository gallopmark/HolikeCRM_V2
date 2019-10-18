package com.holike.crm.fragment.customer;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.presenter.activity.CustomerDetailPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.activity.CustomerDetailView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/20.
 * 客户管理 客户详情重构（scrollView 嵌套多重recyclerView 没有复用机制，容易造成栈堆溢出异常）
 */
@Deprecated
public class CustomerDetailFragment extends MyFragment<CustomerDetailPresenter, CustomerDetailView> implements CustomerDetailView {
    @BindView(R.id.tv_customer_detail_name)
    TextView tvName;
    @BindView(R.id.tv_customer_detail_phone)
    TextView tvPhone;
    @BindView(R.id.tv_customer_detail_customer_type)
    TextView tvCustomerType;
    @BindView(R.id.tv_customer_detail_time)
    TextView tvTime;
    @BindView(R.id.rv_customer_detail_house)
    RecyclerView rvHouse;
    @BindView(R.id.mContainer)
    View mContentLayout;

    private FragmentManager fragmentManager;
    private HouseManageFragment mFragment;
    private Map<String, HouseManageFragment> fragments;
    private CustomerDetailBean customerDetailBean;
    private boolean isAddHouse;
    private String currentHouseId;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_detail;
    }

    @Override
    protected CustomerDetailPresenter attachPresenter() {
        return new CustomerDetailPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(mContext.getString(R.string.customer_manage_title));
        setRightMenu(mContext.getString(R.string.message_title));
        fragmentManager = getChildFragmentManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHouse.setLayoutManager(layoutManager);
        initData();
    }

    private void initData() {
        String personalId = "";
        if (getArguments() != null) {
            currentHouseId = getArguments().getString(Constants.HOUSE_ID);
            personalId = getArguments().getString(Constants.PERSONAL_ID);
        }
        showLoading();
        mPresenter.getData(personalId);
    }

    @Override
    protected void clickRightMenu(CharSequence text, View actionView) {
        super.clickRightMenu(text, actionView);
        startActivity(MessageV2Activity.class);
    }

    /**
     * 刷新页面
     */
    public void refresh(boolean isAddHouse) {
        this.isAddHouse = isAddHouse;
        fragments.clear();
        getChildFragmentManager().getFragments().clear();
        mPresenter.getData(getArguments().getString(Constants.PERSONAL_ID));
    }

    /**
     * 获取客户详情成功
     */
    @Override
    public void getCustomerSuccess(CustomerDetailBean bean) {
        dismissLoading();
        if (mContentLayout.getVisibility() != View.VISIBLE) {
            mContentLayout.setVisibility(View.VISIBLE);
        }
        customerDetailBean = bean;
        CustomerDetailBean.PersonalBean personalBean = bean.getPersonal();
        if (personalBean != null) {
            tvName.setText(personalBean.getUserName());
            tvPhone.setText(personalBean.getPhoneNumber());
            tvCustomerType.setText(personalBean.getSourceName());
            String entryTime = mContext.getString(R.string.entry_time_tips);
            if (!TextUtils.isEmpty(personalBean.getCreateDate())) {
                entryTime += TimeUtil.stampToString(String.valueOf(personalBean.getCreateDate()), "yyyy-MM-dd");
            }
            tvTime.setText(entryTime);
            copy(tvPhone, personalBean.getPhoneNumber());
        }
        showHouseList(bean.getCustomerDetailInfoList(), bean.getPersonal(), bean.getUrl(), bean.getAppurl());
        int currentHousePosition = getCurrentHousePosition(isAddHouse, bean.getCustomerDetailInfoList(), currentHouseId);
        if (bean.getCustomerDetailInfoList().size() > 0) {
            mFragment = changeFragment(bean.getCustomerDetailInfoList().get(currentHousePosition).getListHouseInfo().getHouseId(), R.id.fl_customer_detail_house, mFragment, bean.getCustomerDetailInfoList().get(currentHousePosition), bean.getPersonal(), bean.getUrl(), bean.getAppurl());
        }
    }

    /**
     * 获取当前的房屋的索引
     */
    private int getCurrentHousePosition(boolean isAddHouse, List<CustomerDetailBean.CustomerDetailInfoListBean> listBeans, String currentHouseId) {
        if (isAddHouse) {//添加房屋则跳到新添加的房屋页
            return listBeans.size() - 1;
        } else {
            for (int i = 0, size = listBeans.size(); i < size; i++) {
                if (listBeans.get(i).getListHouseInfo().getHouseId().equals(currentHouseId)) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * 获取客户详情失败
     */
    @Override
    public void getCustomerFailed(String failed) {
        dismissLoading();
        dealWithFailed(failed, true);
    }


    @OnClick({R.id.tv_customer_detail_edit, R.id.btn_customer_detail_add_house, R.id.tv_customer_detail_phone})
    public void onViewClicked(View view) {
        if (customerDetailBean == null) return;
        switch (view.getId()) {
            case R.id.tv_customer_detail_edit:
                MobclickAgent.onEvent(mContext, "customerdetails_edit_customer");
                Map<String, Serializable> paramsPersonal = new HashMap<>();
                paramsPersonal.put(Constants.PERSONAL_BEAN, customerDetailBean.getPersonal());
                startFragment(paramsPersonal, new EditCustomerFragment());
                break;
            case R.id.btn_customer_detail_add_house:
                MobclickAgent.onEvent(mContext, "customerdetails_add_house");
                Map<String, Serializable> paramsHouse = new HashMap<>();
                paramsHouse.put(Constants.PERSONAL_BEAN, customerDetailBean.getPersonal());
                paramsHouse.put(Constants.HOUSE_TYPE, Constants.ADD_HOUSE);
                startFragment(paramsHouse, new EditHouseFragment());
                break;
            case R.id.tv_customer_detail_phone:
                if (mContext != null && customerDetailBean.getPersonal() != null) {
                    ((BaseActivity) mContext).call(customerDetailBean.getPersonal().getPhoneNumber());
                }
                break;
        }
    }

    /**
     * 显示房屋列表
     */
    public void showHouseList(final List<CustomerDetailBean.CustomerDetailInfoListBean> listBeans, final CustomerDetailBean.PersonalBean personal, final String url, final String appurl) {
        if (listBeans != null && listBeans.size() > 0) {
            listBeans.get(getCurrentHousePosition(isAddHouse, listBeans, currentHouseId)).setSelect(true);
            rvHouse.setAdapter(new CommonAdapter<CustomerDetailBean.CustomerDetailInfoListBean>(mContext, listBeans) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_house_manage_house;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, CustomerDetailBean.CustomerDetailInfoListBean bean, int position) {
                    TextView tvName = holder.obtainView(R.id.tv_item_rv_house_manage_house_name);
                    String text = mContext.getString(R.string.house) + (position + 1);
                    tvName.setText(text);
                    if (bean.isSelect()) {
                        tvName.setTextColor(ContextCompat.getColor(mContext, R.color.color_while));
                        tvName.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
                    } else {
                        tvName.setTextColor(ContextCompat.getColor(mContext, R.color.textColor5));
                        tvName.setBackgroundResource(R.color.bg_transparent);
                    }
                    holder.itemView.setOnClickListener(v -> {
                        currentHouseId = bean.getListHouseInfo().getHouseId();
                        for (CustomerDetailBean.CustomerDetailInfoListBean detailInfoListBean : listBeans) {
                            detailInfoListBean.setSelect(false);
                        }
                        bean.setSelect(true);
                        notifyDataSetChanged();
                        mFragment = changeFragment(bean.getListHouseInfo().getHouseId(), R.id.fl_customer_detail_house, mFragment, bean, personal, url, appurl);
                    });
                }
            });
        }
    }

    /**
     * 切换房屋
     */
    public HouseManageFragment changeFragment(String houseId, int layoutId, HouseManageFragment mFragment, CustomerDetailBean.CustomerDetailInfoListBean bean, CustomerDetailBean.PersonalBean personal, String url, String appurl) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragments == null) {
            fragments = new HashMap<>();
        }
        if (mFragment == null) {
            mFragment = new HouseManageFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.HOUSE_BEAN, bean);
            bundle.putSerializable(Constants.PERSONAL_BEAN, personal);
            bundle.putSerializable(Constants.HOUSE_BEAN_URL, url);
            bundle.putSerializable(Constants.HOUSE_BEAN_APP_URL, appurl);
            mFragment.setArguments(bundle);
            transaction.add(layoutId, mFragment, houseId);
            transaction.show(mFragment).commitAllowingStateLoss();
            fragments.put(houseId, mFragment);
        } else {
            HouseManageFragment changeFragment;
            if (fragments.get(houseId) == null) {
                changeFragment = new HouseManageFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HOUSE_BEAN, bean);
                bundle.putSerializable(Constants.PERSONAL_BEAN, personal);
                bundle.putSerializable(Constants.HOUSE_BEAN_URL, url);
                bundle.putSerializable(Constants.HOUSE_BEAN_APP_URL, appurl);
                changeFragment.setArguments(bundle);
                transaction.add(layoutId, changeFragment, houseId);
                fragments.put(houseId, changeFragment);
            } else {
                changeFragment = fragments.get(houseId);
            }
            assert changeFragment != null;
            transaction.hide(mFragment).show(changeFragment).commitAllowingStateLoss();
            mFragment = changeFragment;
        }
        return mFragment;
    }

    @Override
    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
        super.onFinishResult(requestCode, resultCode, result);
        if (resultCode == Constants.RESULT_CODE_OPERATE_SUCCESS) {
            refresh(false);
        } else if (resultCode == Constants.RESULT_CODE_ADD_HOUSE_SUCCESS) {
            refresh(true);
        }
    }

    @Override
    protected void reload() {
        initData();
    }
}
