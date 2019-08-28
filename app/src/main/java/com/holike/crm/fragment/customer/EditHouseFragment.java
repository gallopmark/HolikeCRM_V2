package com.holike.crm.fragment.customer;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.holike.crm.R;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.customView.InputEditText;
import com.holike.crm.helper.HouseMutilSelectHelper;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WorkflowView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/8.
 * 编辑房屋信息
 */

public class EditHouseFragment extends WorkflowFragment implements WorkflowView, HouseMutilSelectHelper.HouseMutilSelectListener {
    protected final int TYPE_SHOP = 5;
    protected final int TYPE_ASSOCIATES = 6;
    protected final int TYPE_HOUSE_STYLE = 7;
    protected final int TYPE_DECORATE_STYLE = 8;
    protected final int TYPE_DECORATE_PROPERTY = 9;
    protected final int TYPE_MATERIAL = 10;
    protected final int TYPE_COLOR = 11;

    protected final int TYPE_CUSTOM_DECORATE_STATE = 13;//tv_baseCustomer_decorate_state
    protected final int TYPE_CUSTOM_WHETHER_FLOOR = 14;//tv_baseCustomer_whether_floor
    protected final int TYPE_CUSTOM_FLOOR_TIME = 15;//tv_baseCustomer_floor_time
    protected final int TYPE_CUSTOM_DECORATE_TIME = 16;//tv_baseCustomer_decorate_time

    @BindView(R.id.et_baseCustomer_residence)
    InputEditText etResidence;
    @BindView(R.id.tv_baseCustomer_houseStyle)
    TextView tvHouseStyle;
    @BindView(R.id.tv_baseCustomer_decorateStyle)
    TextView tvDecorateStyle;
    @BindView(R.id.tv_baseCustomer_decorateProperty)
    TextView tvDecorateProperty;
    @BindView(R.id.et_baseCustomer_budget)
    EditText etBudget;
    @BindView(R.id.et_baseCustomer_phone)
    EditText etPhone;
    @BindView(R.id.tv_baseCustomer_material)
    TextView tvMaterial;
    @BindView(R.id.tv_baseCustomer_color)
    TextView tvColor;
    @BindView(R.id.tv_receive_deposit_customer_service)
    TextView tvCustomerService;
    @BindView(R.id.ll_digital_remark)
    LinearLayout llDigitalRemark;
    @BindView(R.id.ll_edit_house_follow_status)
    LinearLayout llFollowStatus;
    @BindView(R.id.ll_edit_house_promotion)
    LinearLayout llPromotion;
    @BindView(R.id.ll_edit_house_registration_scheme)
    LinearLayout llRegistrationScheme;

    @BindView(R.id.ll_h_decorate_state)
    LinearLayout llDecorateState;
    @BindView(R.id.ll_h_decorate_time)
    LinearLayout llDecorateTime;
    @BindView(R.id.ll_h_whether_floor)
    LinearLayout llWhetherFloor;
    @BindView(R.id.ll_h_floor_time)
    LinearLayout llFloorTime;

    @BindView(R.id.tv_baseCustomer_follow_status)
    TextView tvFollowStatus;
    @BindView(R.id.tv_baseCustomer_promotion)
    TextView tvPromotion;
    @BindView(R.id.tv_baseCustomer_registration_scheme)
    TextView tvRegistrationScheme;


    @BindView(R.id.et_baseCustomer_note)
    EditText etNote;
    @BindView(R.id.et_baseCustomer_houseNum)
    InputEditText etHouseNum;
    @BindView(R.id.et_baseCustomer_houseArea)
    InputEditText etHouseArea;
    @BindView(R.id.et_baseCustomer_name)
    EditText etCustomerName;

    @BindView(R.id.tv_baseCustomer_decorate_state)
    TextView tvCustomDecorateState;
    @BindView(R.id.tv_baseCustomer_whether_floor)
    TextView tvCustomWhetherFloor;
    @BindView(R.id.tv_baseCustomer_floor_time)
    TextView tvCustomFloorTime;

    @BindView(R.id.tv_baseCustomer_decorate_time)
    TextView tvCustomDecorateTime;
    @BindView(R.id.tv_baseCustomer_associates)
    TextView tvAssociates;
    @BindView(R.id.tv_baseCustomer_shop)
    TextView tvShop;

    @BindView(R.id.rv_custom_space)
    RecyclerView rvCustomSpace;
    @BindView(R.id.rv_furniture_demand)
    RecyclerView rvFurnitureDemand;

    protected int type;
    protected TypeIdBean typeIdBean;
    protected TypeIdBean.TypeIdItem houseStyle, decorateStyle, decorateProperty, decorateState, whetherFloor, floorTime, decorateTime, specialCustomer, shop;
    protected TypeIdBean.TypeIdItem material = new TypeIdBean.TypeIdItem("", ""), color = new TypeIdBean.TypeIdItem("", "");
    protected List<? extends TypeIdBean.TypeIdItem> houseStyles, decorateStyles, decoratePropertys, materials, colors, customSpace, furnitureDemand, decorateStates, whetherFloors, floorTimes, decorateTimes, specialCustomers, shops;
    protected AssociateBean.GuideBean associate;
    protected List<AssociateBean.GuideBean> associates;


    private CustomerDetailBean.CustomerDetailInfoListBean.ListDetailsBean listDetailsBean;
    private String name, houseId;
    private String spaceIds, needIds;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_edit_house;
    }

    @Override
    protected void init() {
        super.init();
        if (TextUtils.equals(houseType, Constants.ADD_HOUSE)) {

            setTitle(getString(R.string.customer_manage_add_house));
        } else {

            setTitle(getString(R.string.customer_manage_edit_house));
        }
        if (houseInfoBean != null) {
            if (personal != null && TextUtils.equals(personal.getSource(), "09")) {
                llFollowStatus.setVisibility(View.VISIBLE);
                llPromotion.setVisibility(View.VISIBLE);
                llRegistrationScheme.setVisibility(View.VISIBLE);
                llFloorTime.setVisibility(View.VISIBLE);
                llDecorateState.setVisibility(View.VISIBLE);
                llDecorateTime.setVisibility(View.VISIBLE);
                llWhetherFloor.setVisibility(View.VISIBLE);
                llDigitalRemark.setVisibility(View.VISIBLE);
            }


            if (!TextUtils.isEmpty(houseInfoBean.getBuildingName()))
                etResidence.setText(houseInfoBean.getBuildingName());
            if (!TextUtils.isEmpty(houseInfoBean.getBuildingNumber()))
                etHouseNum.setText(houseInfoBean.getBuildingNumber());
            if (!TextUtils.isEmpty(houseInfoBean.getArea()))
                etHouseArea.setText(houseInfoBean.getArea());
            if (!TextUtils.isEmpty(houseInfoBean.getBudget()))
                etBudget.setText(houseInfoBean.getBudget());
            if (!TextUtils.isEmpty(houseInfoBean.getRemark()))
                etNote.setText(houseInfoBean.getRemark());
            if (!TextUtils.isEmpty(houseInfoBean.getDigitalRemark())) {
                tvCustomerService.setText(houseInfoBean.getDigitalRemark());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getHouseTypeName()))
                tvHouseStyle.setText(houseInfoBean.getHouseTypeName());

            if (!TextUtils.isEmpty(houseInfoBean.getDecorateTypeName()))
                tvDecorateStyle.setText(houseInfoBean.getDecorateTypeName());

            if (!TextUtils.isEmpty(houseInfoBean.getDecoratePropertiesName()))
                tvDecorateProperty.setText(houseInfoBean.getDecoratePropertiesName());

            if (!TextUtils.isEmpty(houseInfoBean.getFavTextureCodeName()))
                tvMaterial.setText(houseInfoBean.getFavTextureCodeName());

            if (!TextUtils.isEmpty(houseInfoBean.getPreferenceStyleName()))
                tvColor.setText(houseInfoBean.getPreferenceStyleName());

            if (!TextUtils.isEmpty(houseInfoBean.getDecorationProgressName()))
                tvCustomDecorateState.setText(houseInfoBean.getDecorationProgressName());

            if (!TextUtils.isEmpty(houseInfoBean.getCheckbulidingCodeName()))
                tvCustomWhetherFloor.setText(houseInfoBean.getCheckbulidingCodeName());

            if (!TextUtils.isEmpty(houseInfoBean.getPlannedHouseDeliveryDate()))
                tvCustomFloorTime.setText(TimeUtil.transDateFormat(houseInfoBean.getPlannedHouseDeliveryDate()));

            if (!TextUtils.isEmpty(houseInfoBean.getPlannedBaseDecorateDate()))
                tvCustomDecorateTime.setText(TimeUtil.transDateFormat(houseInfoBean.getPlannedBaseDecorateDate()));

            if (!TextUtils.isEmpty(houseInfoBean.getFollowCodeName()))
                tvFollowStatus.setText(houseInfoBean.getFollowCodeName());

            if (!TextUtils.isEmpty(houseInfoBean.getDeliveryChannel()))
                tvPromotion.setText(houseInfoBean.getDeliveryChannel());

            if (!TextUtils.isEmpty(houseInfoBean.getAdName())) {
                tvRegistrationScheme.setText(houseInfoBean.getAdName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getAssociatesName())) {
                tvAssociates.setText(houseInfoBean.getAssociatesName());
                associate = new AssociateBean.GuideBean(houseInfoBean.getSalesId(), houseInfoBean.getAssociatesName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getShopName())) {
                tvShop.setText(houseInfoBean.getShopName());
            }

            houseId = houseInfoBean.getHouseId();
            if (!TextUtils.isEmpty(houseInfoBean.getHouseType())) {
                houseStyle = new TypeIdBean.TypeIdItem(houseInfoBean.getHouseType(), houseInfoBean.getHouseTypeName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getDecorateProperties())) {
                decorateProperty = new TypeIdBean.TypeIdItem(houseInfoBean.getDecorateProperties(), houseInfoBean.getDecoratePropertiesName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getDecorateType())) {
                decorateStyle = new TypeIdBean.TypeIdItem(houseInfoBean.getDecorateType(), houseInfoBean.getDecorateTypeName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getFavTextureCode())) {
                material = new TypeIdBean.TypeIdItem(houseInfoBean.getFavTextureCode(), houseInfoBean.getFavTextureCodeName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getFavColorCode())) {
                color = new TypeIdBean.TypeIdItem(houseInfoBean.getFavColorCode(), houseInfoBean.getFavColorCodeName());
            }

            if (!TextUtils.isEmpty(houseInfoBean.getShopId())) {
                shop = new TypeIdBean.TypeIdItem(houseInfoBean.getShopId(), houseInfoBean.getShopName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getDecorationProgress())) {
                decorateState = new TypeIdBean.TypeIdItem(houseInfoBean.getDecorationProgress(), houseInfoBean.getDecorationProgressName());
            }
            if (!TextUtils.isEmpty(houseInfoBean.getCheckbulidingCode())) {
                whetherFloor = new TypeIdBean.TypeIdItem(houseInfoBean.getCheckbulidingCode(), houseInfoBean.getCheckbulidingCodeName());
            }


        }
        listDetailsBean = (CustomerDetailBean.CustomerDetailInfoListBean.ListDetailsBean) bundle.get(Constants.LIST_DETAILS_BEAN);
        if (listDetailsBean != null) {
            name = listDetailsBean.getName();
            etCustomerName.setText(name);
            etPhone.setText(listDetailsBean.getNumber());
        }
        mPresenter.getTypeId();
    }

    @OnClick({R.id.tv_baseCustomer_decorate_time, R.id.tv_baseCustomer_floor_time, R.id.tv_baseCustomer_whether_floor, R.id.tv_baseCustomer_decorate_state, R.id.tv_baseCustomer_shop, R.id.tv_baseCustomer_associates, R.id.tv_baseCustomer_houseStyle, R.id.tv_baseCustomer_decorateStyle, R.id.tv_baseCustomer_decorateProperty, R.id.tv_baseCustomer_material, R.id.tv_baseCustomer_color, R.id.btn_baseCustomer_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_baseCustomer_houseStyle:
                type = TYPE_HOUSE_STYLE;
                showPickerView(houseStyles, getText(tvHouseStyle));
                break;
            case R.id.tv_baseCustomer_decorateStyle:
                type = TYPE_DECORATE_STYLE;
                showPickerView(decorateStyles, getText(tvDecorateStyle));
                break;
            case R.id.tv_baseCustomer_decorateProperty:
                type = TYPE_DECORATE_PROPERTY;
                showPickerView(decoratePropertys, getText(tvDecorateProperty));
                break;
            case R.id.tv_baseCustomer_material:
                type = TYPE_MATERIAL;
                showPickerView(materials, getText(tvMaterial));
                break;
            case R.id.tv_baseCustomer_color:
                type = TYPE_COLOR;
                showPickerView(colors, getText(tvColor));
                break;
            case R.id.btn_baseCustomer_save:
                if (isTextEmpty(tvShop)) {
                    showShortToast(R.string.tips_complete_information);
                } else if (!TextUtils.isEmpty(etPhone.getText().toString().trim()) &&
                        !CheckUtils.isMobile(etPhone.getText().toString())) {
                    showShortToast(R.string.tips_complete_phone_contact);
                } else if (personal != null && TextUtils.equals(personal.getSource(), "09")) {
                    mPresenter.editHouse(getText(etHouseArea),
                            getText(etBudget),
                            getText(etResidence),
                            getText(etHouseNum),
                            decorateStyle == null ? "" : decorateStyle.getId(),
                            color == null ? "" : color.getId(),
                            material == null ? "" : material.getId(),
                            decorateProperty == null ? "" : decorateProperty.getId(),
                            houseStyle == null ? "" : houseStyle.getId(),
                            getText(etNote), houseId,
                            getText(etCustomerName),
                            getText(etPhone),
                            personalId,
                            whetherFloor == null ? "" : whetherFloor.getId(),
                            spaceIds,
                            decorateState == null ? "" : decorateState.getId(),
                            needIds,
                            TimeUtil.transDateFormatCommit(tvCustomDecorateTime.getText().toString()),
                            TimeUtil.transDateFormatCommit(tvCustomFloorTime.getText().toString()),
                            shop == null ? "" : shop.getId(),
                            associate == null ? "" : associate.getUserId());
                    showLoading();
                } else {
                    mPresenter.editHouse(getText(etHouseArea),
                            getText(etBudget),
                            getText(etResidence),
                            getText(etHouseNum),
                            decorateStyle == null ? "" : decorateStyle.getId(),
                            color == null ? "" : color.getId(),
                            material == null ? "" : material.getId(),
                            decorateProperty == null ? "" : decorateProperty.getId(),
                            houseStyle == null ? "" : houseStyle.getId(),
                            getText(etNote), houseId,
                            getText(etCustomerName),
                            getText(etPhone),
                            personalId == null ? "" : personalId,
                            "",
                            spaceIds == null ? "" : spaceIds,
                            "",
                            needIds == null ? "" : needIds,
                            "",
                            "",
                            shop == null ? "" : shop.getId(),
                            associate == null ? "" : associate.getUserId());
                    showLoading();
                }

                break;
            case R.id.tv_baseCustomer_shop:
                type = TYPE_SHOP;
                showPickerView(shops, getText(tvShop));
                break;
            case R.id.tv_baseCustomer_associates:
                type = TYPE_ASSOCIATES;
                if (shop == null) {
                    showShortToast("请先选择所属门店");
                } else if (associates == null) {
                    mPresenter.getAssociate(shop.getId());
                } else {
                    showPickerView(associates, getText(tvAssociates), tvAssociates);
                }
                break;
            case R.id.tv_baseCustomer_decorate_state:
                type = TYPE_CUSTOM_DECORATE_STATE;
                showPickerView(decorateStates, tvCustomDecorateState.getText().toString());
                break;
            case R.id.tv_baseCustomer_whether_floor:
                type = TYPE_CUSTOM_WHETHER_FLOOR;
                showPickerView(whetherFloors, tvCustomWhetherFloor.getText().toString());

                break;
            case R.id.tv_baseCustomer_floor_time:
                type = TYPE_CUSTOM_FLOOR_TIME;
                showTimePickerView(getActivity(), getText(tvCustomFloorTime), tvCustomFloorTime);

                break;
            case R.id.tv_baseCustomer_decorate_time:
                type = TYPE_CUSTOM_DECORATE_TIME;
                showTimePickerView(getActivity(), getText(tvCustomDecorateTime), tvCustomDecorateTime);
                break;
        }
    }

    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof TypeIdBean) {
            typeIdBean = (TypeIdBean) success;
            setTypeId();
        } else if (success instanceof AssociateBean) {
            associates = ((AssociateBean) success).getGuide();
            showPickerView(associates, getText(tvAssociates), tvAssociates);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            if (houseInfoBean == null) {//添加房屋成功
                finishFragment(0, Constants.RESULT_CODE_ADD_HOUSE_SUCCESS, null);
            } else {//编辑房屋成功
                operateSuccess();

            }
        }
    }

    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void selectTime(Date date) {
        switch (type) {
            case TYPE_CUSTOM_DECORATE_TIME:
                tvCustomDecorateTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
                break;
            case TYPE_CUSTOM_FLOOR_TIME:
                tvCustomFloorTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
                break;
        }
    }

    private void setTypeId() {
        if (typeIdBean != null) {
            houseStyles = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_HOUSE_TYPE());
            decorateStyles = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_DECORATE_TYPE());
            decoratePropertys = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_DECORATE_PROPERTIES());
            materials = AddCustomerPresenter.getTypeIdItems(typeIdBean.getORDER_MATERIAL_ZE_CZ());
            colors = AddCustomerPresenter.getTypeIdItems(typeIdBean.getSTYLE_SERIES_ZE_FG());

            shops = AddCustomerPresenter.getTypeIdItems(typeIdBean.getShopId());

            customSpace = AddCustomerPresenter.getTypeIdItems(typeIdBean.getDIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE());
            furnitureDemand = AddCustomerPresenter.getTypeIdItems(typeIdBean.getDIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND());
            decorateStates = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_CHECKBULIDING_CODE());
            whetherFloors = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_DECORATION_PROGRESS());

            switch (type) {
                case TYPE_HOUSE_STYLE:
                    showPickerView(houseStyles, getText(tvHouseStyle));
                    break;
                case TYPE_DECORATE_STYLE:
                    showPickerView(decorateStyles, getText(tvDecorateStyle));
                    break;
                case TYPE_DECORATE_PROPERTY:
                    showPickerView(decoratePropertys, getText(tvDecorateProperty));
                    break;
                case TYPE_MATERIAL:
                    showPickerView(materials, getText(tvMaterial));
                    break;
                case TYPE_COLOR:
                    showPickerView(colors, getText(tvColor));
                    break;
                case TYPE_SHOP:
                    showPickerView(shops, getText(tvShop));
                    break;
            }

            HouseMutilSelectHelper.getInstance().setList(getActivity(), rvCustomSpace, (List<TypeIdBean.TypeIdItem>) customSpace, houseInfoBean, 0, this);
            HouseMutilSelectHelper.getInstance().setList(getActivity(), rvFurnitureDemand, (List<TypeIdBean.TypeIdItem>) furnitureDemand, houseInfoBean, 1, this);

        }

    }

    /**
     * 条件选择弹窗
     *
     * @param dates
     * @param select
     */
    public void showPickerView(List<? extends TypeIdBean.TypeIdItem> dates, String select) {
        if (mPickerView == null) {
            mPickerView = new OptionsPickerBuilder(mContext, (options1, options2, options3, v) -> optionsSelect(options1)).build();
        }
        if (dates != null && dates.size() > 0) {
            hideSoftInput(etBudget);
            mPickerView.setPicker(dates);
            mPickerView.setSelectOptions(AddCustomerPresenter.getSelectOption(dates, select));
            mPickerView.show();
        } else if (dates == null) {
            showLoading();
            mPresenter.getTypeId();
        } else {
            showShortToast("没有选择数据！");
        }
    }

    @Override
    public void optionsSelect(int position) {
        switch (type) {
            case TYPE_HOUSE_STYLE:
                houseStyle = houseStyles.get(position);
                tvHouseStyle.setText(houseStyle.getName());
                break;
            case TYPE_DECORATE_STYLE:
                decorateStyle = decorateStyles.get(position);
                tvDecorateStyle.setText(decorateStyle.getName());
                break;
            case TYPE_DECORATE_PROPERTY:
                decorateProperty = decoratePropertys.get(position);
                tvDecorateProperty.setText(decorateProperty.getName());
                break;
            case TYPE_MATERIAL:
                material = materials.get(position);
                tvMaterial.setText(material.getName());
                break;
            case TYPE_COLOR:
                color = colors.get(position);
                tvColor.setText(color.getName());
                break;

            case TYPE_CUSTOM_DECORATE_STATE:
                decorateState = decorateStates.get(position);
                tvCustomDecorateState.setText(decorateState.getName());
                break;
            case TYPE_CUSTOM_DECORATE_TIME:
                decorateTime = decorateTimes.get(position);
                tvCustomDecorateTime.setText(decorateTime.getName());
                break;
            case TYPE_CUSTOM_WHETHER_FLOOR:
                whetherFloor = whetherFloors.get(position);
                tvCustomWhetherFloor.setText(whetherFloor.getName());
                break;
            case TYPE_CUSTOM_FLOOR_TIME:
                floorTime = floorTimes.get(position);
                tvCustomFloorTime.setText(floorTime.getName());
                break;
            case TYPE_SHOP:
                shop = shops.get(position);
                if (!tvShop.getText().toString().equals(shop.getName())) {
                    tvShop.setText(shop.getName());
                    tvAssociates.setText("");
                    associate = null;
                }
                break;
            case TYPE_ASSOCIATES:
                associate = associates.get(position);
                tvAssociates.setText(associate.getPickerViewText());
                break;

        }
    }


    @Override
    public void sustomSpace(String ids) {
        spaceIds = ids;
    }

    @Override
    public void furnitureDemand(String ids) {
        needIds = ids;
    }

}
