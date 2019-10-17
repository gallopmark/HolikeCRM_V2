package com.holike.crm.fragment.customer.workflow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.CollectDepositListBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.model.event.MessageEvent;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WorkflowView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wqj on 2018/8/6.
 * 收取订金
 */
@Deprecated
public class CollectDepositFragment extends WorkflowFragment implements WorkflowView {
    private CollectDepositListBean collectDepositListBean;
    protected Unbinder unbinder;
    @BindView(R.id.tv_collect_deposit_collect_time)
    TextView tvCollectTime;
    @BindView(R.id.tv_collect_deposit_collect_man)
    TextView tvCollectMan;
    @BindView(R.id.ll_collect_deposit_operate)
    LinearLayout llOperate;
    @BindView(R.id.tv_collect_deposit_name)
    TextView tvName;
    @BindView(R.id.tv_collect_deposit_phone)
    TextView tvPhone;
    @BindView(R.id.tv_collect_deposit_address)
    TextView tvAddress;
    @BindView(R.id.ll_collect_deposit_info)
    LinearLayout llInfo;
    @BindView(R.id.et_collect_deposit_money)
    EditText etMoney;
    @BindView(R.id.rv_collect_deposit_img)
    RecyclerView rv;
    @BindView(R.id.et_collect_deposit_note)
    EditText etNote;
    @BindView(R.id.tv_collect_custom_category)
    TextView tvCollectCustomCategory;
    private List<TypeIdBean.TypeIdItem> customCategorys;
    private TypeIdBean.TypeIdItem customCategory;
    private int type;
    private final static int TYPE_PROFESSION = 0;
    private final static int TYPE_CUSTOMCATEGORY = 1;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_collect_deposit;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_collect_deposit));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initImgs(rv, getString(R.string.receive_deposit_addBills) + "*");
        tvCollectTime.setText(TimeUtil.dateToString(new Date(), "yyyy.MM.dd"));
        if (bundle != null) {
            collectDepositListBean = (CollectDepositListBean) bundle.get(Constants.COLLECT_DEPOSIT_LIST_BEAN);
        }
//        customCategorys = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_EARNESTHOUSE_TYPE());
        if (collectDepositListBean != null) {
            llOperate.setVisibility(View.GONE);
            llInfo.setVisibility(View.VISIBLE);
            tvName.setText(collectDepositListBean.getUserName());
            tvPhone.setText(collectDepositListBean.getPhoneNumber());
            tvAddress.setText(collectDepositListBean.getAddress());
            customerStatus = "03";
            operateCode = "02";
            personalId = collectDepositListBean.getPersonalId();
            houseId = collectDepositListBean.getHouseId();
            professionBean = new AssociateBean.ProfessionBean(SharedPreferencesUtils.getString(Constants.USER_ID), "");
        } else {
            if (houseInfoBean != null) {
                mPresenter.getAssociate(houseInfoBean.getShopId());
                showLoading();
            }
        }
    }

    /**
     * 获取所属人/保存成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();

        if (success instanceof AssociateBean) {
            professionBeans = ((AssociateBean) success).getProfession();
            if (isTextEmpty(tvCollectMan)) {
                professionBean = mPresenter.getCurrentProfession(professionBeans);
                tvCollectMan.setText(professionBean == null ? "" : professionBean.getUserName());
            } else {
                showPickerView(professionBeans, getText(tvCollectMan), etMoney);
            }
        } else if (success instanceof TypeIdBean) {
            typeIdBean = (TypeIdBean) success;
            customCategorys = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_EARNESTHOUSE_TYPE());
            showPickerView(customCategorys, getText(tvCollectCustomCategory), etMoney);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
            EventBus.getDefault().post(new MessageEvent(Constants.EVENT_REFRESH));
        }
    }


    /**
     * 获取所属人/保存失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @OnClick({R.id.tv_collect_deposit_collect_time, R.id.tv_collect_deposit_collect_man, R.id.tv_collect_deposit_save, R.id.tv_collect_custom_category})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_collect_deposit_collect_time:
                showTimePickerView(getActivity(), getText(tvCollectTime), etMoney);
                break;
            case R.id.tv_collect_deposit_collect_man:
                if (professionBeans == null) {
                    if (houseInfoBean != null) {
                        mPresenter.getAssociate(houseInfoBean.getShopId());
                        showLoading();
                    }
                } else {
                    type = TYPE_PROFESSION;
                    showPickerView(professionBeans, getText(tvCollectMan), etMoney);
                }
                break;
            case R.id.tv_collect_deposit_save:
                if (isTextEmpty(tvCollectTime) || professionBean == null || isTextEmpty(etMoney) || imgs.size() == 0 || customCategory == null) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.collectDeposit(mContext, getText(etNote), customerStatus, houseId,
                            professionBean.getUserId(), getText(tvCollectTime), operateCode,
                            personalId, prepositionRuleStatus, getText(etMoney),
                            imgs, customCategory.getId());
                    showLoading();
                }
                break;
            case R.id.tv_collect_custom_category:
                type = TYPE_CUSTOMCATEGORY;
                if (typeIdBean == null) {
                    mPresenter.getTypeId();
                    showLoading();
                } else {
                    showPickerView(customCategorys, getText(tvCollectCustomCategory), etMoney);
                }

                break;
        }
    }

    /**
     * 选择日期
     */
    @Override
    protected void selectTime(Date date) {
        try {
            String text = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date);
            tvCollectTime.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择收取人
     */
    @Override
    protected void optionsSelect(int options) {
        switch (type) {
            case TYPE_PROFESSION:
                if (professionBeans == null || professionBeans.isEmpty()) return;
                if (options >= 0 && options < professionBeans.size()) {
                    professionBean = professionBeans.get(options);
                    tvCollectMan.setText(professionBean.getUserName());
                }
                break;
            case TYPE_CUSTOMCATEGORY:
                if (customCategorys == null || customCategorys.isEmpty()) return;
                if (options >= 0 && options < customCategorys.size()) {
                    customCategory = customCategorys.get(options);
                    tvCollectCustomCategory.setText(customCategory.getName());
                }
                break;
        }

    }
}
