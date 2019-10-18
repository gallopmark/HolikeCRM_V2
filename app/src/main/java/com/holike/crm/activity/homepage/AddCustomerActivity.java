package com.holike.crm.activity.homepage;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gallopmark.imagepicker.model.ImagePicker;
import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.helper.HouseMutilSelectHelper;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.AddCustomerView;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/6/12.
 * 添加客户
 */
@Deprecated
public class AddCustomerActivity extends MyFragmentActivity<AddCustomerPresenter, AddCustomerView> implements AddCustomerView, HouseMutilSelectHelper.HouseMutilSelectListener {
    protected final int TYPE_CUSTOMER_GENDER = 1;
    protected final int TYPE_CUSTOMER_SOURCE = 2;
    protected final int TYPE_SPECIAL_CUSTOMER = 3;
    protected final int TYPE_SHOP = 5;
    protected final int TYPE_ASSOCIATES = 6;
    protected final int TYPE_HOUSE_STYLE = 7;
    protected final int TYPE_DECORATE_STYLE = 8;
    protected final int TYPE_DECORATE_PROPERTY = 9;
    protected final int TYPE_MATERIAL = 10;
    protected final int TYPE_COLOR = 11;
    protected final int TYPE_CUSTOM_CATEGORY = 12;
    protected final int TYPE_CUSTOM_DECORATE_STATE = 13;
    protected final int TYPE_CUSTOM_WHETHER_FLOOR = 14;
    protected final int TYPE_CUSTOM_FLOOR_TIME = 15;
    protected final int TYPE_CUSTOM_DECORATE_TIME = 16;

    @BindView(R.id.et_baseCustomer_customerName)
    EditText etCustomerName;
    @BindView(R.id.et_baseCustomer_customerPhone)
    EditText etCustomerPhone;
    @BindView(R.id.tv_baseCustomer_customerGender)
    TextView tvCustomerGender;
    @BindView(R.id.tv_baseCustomer_customerSource)
    TextView tvCustomerSource;
    @BindView(R.id.tv_baseCustomer_specialSource)
    TextView tvSpecialSource;
    @BindView(R.id.et_baseCustomer_residence)
    EditText etResidence;
    @BindView(R.id.tv_baseCustomer_associates)
    TextView tvAssociates;
    @BindView(R.id.tv_baseCustomer_shop)
    TextView tvShop;
    @BindView(R.id.et_baseCustomer_houseNum)
    EditText etHouseNum;
    @BindView(R.id.et_baseCustomer_houseArea)
    EditText etHouseArea;
    @BindView(R.id.tv_baseCustomer_houseStyle)
    TextView tvHouseStyle;
    @BindView(R.id.tv_baseCustomer_decorateStyle)
    TextView tvDecorateStyle;
    @BindView(R.id.tv_baseCustomer_decorateProperty)
    TextView tvDecorateProperty;
    @BindView(R.id.et_baseCustomer_depositAmount)
    TextView etDepositAmount;
    @BindView(R.id.rv_add_customer)
    RecyclerView rv;
    @BindView(R.id.ll_add_customer_collect_deposit)
    LinearLayout llCollectDeposit;
    @BindView(R.id.et_baseCustomer_budget)
    EditText etBudget;
    @BindView(R.id.et_baseCustomer_phone)
    EditText etPhone;
    @BindView(R.id.tv_baseCustomer_material)
    TextView tvMaterial;
    @BindView(R.id.tv_baseCustomer_color)
    TextView tvColor;
    @BindView(R.id.et_baseCustomer_note)
    EditText etNote;
    @BindView(R.id.btn_baseCustomer_save)
    TextView btnSave;
    @BindView(R.id.et_baseCustomer_name)
    EditText etBaseCustomerName;
    @BindView(R.id.tv_baseCustomer_custom_category)
    TextView tvCustomCategory;

    @BindView(R.id.tv_baseCustomer_decorate_state)
    TextView tvCustomDecorateState;
    @BindView(R.id.tv_baseCustomer_whether_floor)
    TextView tvCustomWhetherFloor;
    @BindView(R.id.tv_baseCustomer_floor_time)
    TextView tvCustomFloorTime;
    @BindView(R.id.tv_baseCustomer_decorate_time)
    TextView tvCustomDecorateTime;

    @BindView(R.id.ll_custom_category)
    LinearLayout llCustomCategory;
    @BindView(R.id.rv_custom_space)
    RecyclerView rvCustomSpace;
    @BindView(R.id.rv_furniture_demand)
    RecyclerView rvFurnitureDemand;
    @BindView(R.id.ll_furniture_demand)
    LinearLayout llFurnitureDemand;
    @BindView(R.id.ll_custom_space)
    LinearLayout llCustomSpace;

    protected OptionsPickerView mPickerView;
    protected int type;
    protected TypeIdBean typeIdBean;
    protected TypeIdBean.TypeIdItem gender, customerSource, shop, houseStyle, decorateStyle, decorateProperty, customCategory, decorateState, whetherFloor, floorTime, decorateTime;
    protected TypeIdBean.TypeIdItem specialCustomer = new TypeIdBean.TypeIdItem("", ""), material = new TypeIdBean.TypeIdItem("", ""), color = new TypeIdBean.TypeIdItem("", "");
    protected List<? extends TypeIdBean.TypeIdItem> genders, customerSources, specialCustomers, shops, houseStyles, decorateStyles, decoratePropertys, materials, colors, customCategorys, customSpace, furnitureDemand, decorateStates, whetherFloors, floorTimes, decorateTimes;
    protected AssociateBean.GuideBean associate;
    protected List<AssociateBean.GuideBean> associates;

    private String isEarnest = "0";
    protected UploadImgHelper.OnClickListener clickImgListener;
    private List<String> imgs = new ArrayList<>();
    private String spaceIds, needIds;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_add_customer;
    }

    @Override
    protected AddCustomerPresenter attachPresenter() {
        return new AddCustomerPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(Constants.IS_EARNEST) != null) {
            initImgList();
        }
        typeIdBean = Parcels.unwrap(getIntent().getParcelableExtra(Constants.TYPE_ID));
        setTypeId();
    }

    /**
     * 显示上传图片控件
     */
    private void initImgList() {
        isEarnest = getIntent().getStringExtra(Constants.IS_EARNEST);
        if (isEarnest.equals("1")) {
            llCollectDeposit.setVisibility(View.VISIBLE);
            llCustomCategory.setVisibility(View.VISIBLE);
            setTitle(getString(R.string.receive_deposit_add_customer));
            rv.setNestedScrollingEnabled(false);
            rv.setLayoutManager(new GridLayoutManager(this, 3));
            clickImgListener = new UploadImgHelper.OnClickListener() {
                @Override
                public void addImg() {
                    ImagePicker.builder().maxSelectCount(9 - imgs.size()).start(AddCustomerActivity.this);
                }

                @Override
                public void delImg(String img) {
                    imgs.remove(img);
                    UploadImgHelper.setImgList(getActivity(), rv, imgs, getString(R.string.feedback_add_deposit_receipt), 9, clickImgListener);
                }
            };
            UploadImgHelper.setImgList(getActivity(), rv, imgs, getString(R.string.feedback_add_deposit_receipt), 9, clickImgListener);
        } else {
            setTitle(getString(R.string.customer_manage_add_customer));
        }
        mPresenter.getTypeId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.DEFAULT_REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImagePicker.SELECT_RESULT);
            imgs.addAll(images);
            UploadImgHelper.setImgList(getActivity(), rv, imgs, getString(R.string.feedback_problem_upload_img), 9, clickImgListener);
        }
    }

    /**
     * 设置类型id数据
     */
    private void setTypeId() {
        if (typeIdBean != null) {
            genders = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_GENDER_CODE());
            customerSources = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_SOURCE_CODE());
            specialCustomers = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_ESPECIALLY_TYPE());
            shops = AddCustomerPresenter.getTypeIdItems(typeIdBean.getShopId());
            houseStyles = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_HOUSE_TYPE());
            decorateStyles = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_DECORATE_TYPE());
            decoratePropertys = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_DECORATE_PROPERTIES());
            materials = AddCustomerPresenter.getTypeIdItems(typeIdBean.getORDER_MATERIAL_ZE_CZ());
            colors = AddCustomerPresenter.getTypeIdItems(typeIdBean.getSTYLE_SERIES_ZE_FG());
            customCategorys = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_EARNESTHOUSE_TYPE());
            customSpace = AddCustomerPresenter.getTypeIdItems(typeIdBean.getDIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE());
            furnitureDemand = AddCustomerPresenter.getTypeIdItems(typeIdBean.getDIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND());
            decorateStates = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_CHECKBULIDING_CODE());
            whetherFloors = AddCustomerPresenter.getTypeIdItems(typeIdBean.getCUSTOMER_DECORATION_PROGRESS());

            switch (type) {
                case TYPE_CUSTOMER_GENDER:
                    showPickerView(genders, getText(tvCustomerGender));
                    break;
                case TYPE_CUSTOMER_SOURCE:
                    showPickerView(genders, getText(tvCustomerSource));
                    break;
                case TYPE_SPECIAL_CUSTOMER:
                    showPickerView(specialCustomers, getText(tvSpecialSource));
                    break;
                case TYPE_SHOP:
                    showPickerView(shops, getText(tvShop));
                    break;
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
                case TYPE_CUSTOM_CATEGORY:
                    showPickerView(customCategorys, getText(tvCustomCategory));
                    break;
                case TYPE_CUSTOM_DECORATE_STATE:
                    showPickerView(decorateStates, getText(tvCustomDecorateState));
                    break;
                case TYPE_CUSTOM_WHETHER_FLOOR:
                    showPickerView(whetherFloors, getText(tvCustomWhetherFloor));
                    break;
            }
        }
    }

    /**
     * 条件选择回调
     */
    @Override
    public void optionsSelect(int position) {
        switch (type) {
            case TYPE_CUSTOMER_GENDER:
                gender = genders.get(position);
                tvCustomerGender.setText(gender.getName());
                break;
            case TYPE_CUSTOMER_SOURCE:
                customerSource = customerSources.get(position);
                tvCustomerSource.setText(customerSource.getName());
                break;
            case TYPE_SPECIAL_CUSTOMER:
                specialCustomer = specialCustomers.get(position);
                tvSpecialSource.setText(specialCustomer.getName());
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
            case TYPE_CUSTOM_CATEGORY:
                customCategory = customCategorys.get(position);
                tvCustomCategory.setText(customCategory.getName());
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
        }
    }

    @OnClick({R.id.tv_baseCustomer_whether_floor, R.id.tv_baseCustomer_floor_time, R.id.tv_baseCustomer_decorate_time, R.id.tv_baseCustomer_decorate_state, R.id.tv_baseCustomer_custom_category, R.id.tv_baseCustomer_customerGender, R.id.tv_baseCustomer_customerSource, R.id.tv_baseCustomer_specialSource, R.id.tv_baseCustomer_houseStyle, R.id.tv_baseCustomer_shop, R.id.tv_baseCustomer_associates, R.id.tv_baseCustomer_decorateStyle, R.id.tv_baseCustomer_decorateProperty, R.id.tv_baseCustomer_material, R.id.tv_baseCustomer_color, R.id.btn_baseCustomer_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_baseCustomer_customerGender:
                type = TYPE_CUSTOMER_GENDER;
                showPickerView(genders, tvCustomerGender.getText().toString());
                break;
            case R.id.tv_baseCustomer_customerSource:
                type = TYPE_CUSTOMER_SOURCE;
                showPickerView(customerSources, tvCustomerSource.getText().toString());
                break;
            case R.id.tv_baseCustomer_specialSource:
                type = TYPE_SPECIAL_CUSTOMER;
                showPickerView(specialCustomers, tvSpecialSource.getText().toString());
                break;
            case R.id.tv_baseCustomer_shop:
                type = TYPE_SHOP;
                showPickerView(shops, tvShop.getText().toString());
                break;
            case R.id.tv_baseCustomer_associates:
                type = TYPE_ASSOCIATES;
                mPresenter.getAssociateData(shop);
                break;
            case R.id.tv_baseCustomer_houseStyle:
                type = TYPE_HOUSE_STYLE;
                showPickerView(houseStyles, tvHouseStyle.getText().toString());
                break;
            case R.id.tv_baseCustomer_decorateStyle:
                type = TYPE_DECORATE_STYLE;
                showPickerView(decorateStyles, tvDecorateStyle.getText().toString());
                break;
            case R.id.tv_baseCustomer_decorateProperty:
                type = TYPE_DECORATE_PROPERTY;
                showPickerView(decoratePropertys, tvDecorateProperty.getText().toString());
                break;
            case R.id.tv_baseCustomer_material:
                type = TYPE_MATERIAL;
                showPickerView(materials, tvMaterial.getText().toString());
                break;
            case R.id.tv_baseCustomer_color:
                type = TYPE_COLOR;
                showPickerView(colors, tvColor.getText().toString());
                break;
            case R.id.tv_baseCustomer_custom_category:
                type = TYPE_CUSTOM_CATEGORY;
                showPickerView(customCategorys, tvCustomCategory.getText().toString());
                break;
            case R.id.tv_baseCustomer_decorate_state:
                type = TYPE_CUSTOM_DECORATE_STATE;
                showPickerView(decorateStates, tvCustomDecorateState.getText().toString());
                break;
            case R.id.tv_baseCustomer_decorate_time:
                type = TYPE_CUSTOM_DECORATE_TIME;
                mPresenter.showTimePickerView(getActivity(), getText(tvCustomDecorateTime), tvCustomDecorateTime);
                break;
            case R.id.tv_baseCustomer_whether_floor:
                type = TYPE_CUSTOM_WHETHER_FLOOR;
                showPickerView(whetherFloors, tvCustomWhetherFloor.getText().toString());
                break;
            case R.id.tv_baseCustomer_floor_time:
                type = TYPE_CUSTOM_FLOOR_TIME;
                mPresenter.showTimePickerView(getActivity(), getText(tvCustomFloorTime), tvCustomFloorTime);
                break;
            case R.id.btn_baseCustomer_save:
                save();
                break;
        }
    }


    /**
     * 保存
     */
    protected void save() {
        if ((isEarnest.equals("1") && (etDepositAmount.getText().toString().length() == 0
                || imgs.size() == 0))
                || etCustomerPhone.getText().toString().length() == 0
                || shop == null
                || customerSource == null
                || etCustomerName.getText().toString().length() == 0) {
            showShortToast(R.string.tips_complete_information);
        } else if (etCustomerPhone.getText().length() != 11) {
            showShortToast(R.string.tips_complete_phone);
        } else if (isEarnest.equals("1") && customCategory == null) {
            showShortToast(R.string.tips_complete_information);
        } else if (etPhone.getText().length() != 11 && etPhone.getText().length() != 0) {
            showShortToast(R.string.tips_complete_phone_contact);
        } else {
//            showLoading();
            mPresenter.addCustomer(this, etHouseArea.getText().toString(),
                    associate == null ? "" : associate.getUserId(),
                    etBudget.getText().toString(),
                    etResidence.getText().toString(),
                    etHouseNum.getText().toString(),
                    decorateProperty == null ? "" : decorateProperty.getId(),
                    decorateStyle == null ? "" : decorateStyle.getId(),
                    etDepositAmount.getText().toString(),
                    color == null ? "" : color.getId(),
                    material == null ? "" : material.getId(),
                    gender == null ? "" : gender.getId(),
                    houseStyle == null ? "" : houseStyle.getId(),
                    isEarnest,
                    etPhone.getText().toString(),
                    etCustomerPhone.getText().toString(),
                    etNote.getText().toString(),
                    shop == null ? "" : shop.getId(),
                    customerSource.getId() == null ? "" : customerSource.getId(),
                    specialCustomer.getId() == null ? "" : specialCustomer.getId(),
                    etCustomerName.getText().toString(),
                    imgs,
                    etBaseCustomerName.getText().toString(),
                    (customCategory == null ? "" : customCategory.getId()),
                    tvCustomDecorateTime.getText().toString(),
                    tvCustomFloorTime.getText().toString(), decorateState == null ? "" : decorateState.getId(), whetherFloor == null ? "" : whetherFloor.getId()
                    , spaceIds, needIds);
        }
    }


    /**
     * 条件选择弹窗
     */
    public void showPickerView(List<? extends TypeIdBean.TypeIdItem> dates, String select) {
        if (mPickerView == null) {
            mPickerView = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> optionsSelect(options1)).build();
//            mPickerView = new OptionsPickerBuilder(this, new OptionsPickerView.OnOptionsSelectListener() {
//                @Override
//                public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                    optionsSelect(options1);
//                }
//            }).build();
        }
        if (!mPickerView.isShowing() && dates != null && dates.size() > 0) {
            hideSoftInput(etCustomerName);
            mPickerView.setPicker(dates);
            mPickerView.setSelectOptions(mPresenter.getSelectOption(dates, select));
            mPickerView.show();
        } else if (dates == null) {
            showLoading();
            mPresenter.getTypeId();
        } else if (dates.size() == 0) {
            showShortToast("没有选择数据！");
        }
    }

    /**
     * 获取类型id数据成功
     */
    @Override
    public void getTypeIdSuccess(TypeIdBean typeIdBean) {
        dismissLoading();
        this.typeIdBean = typeIdBean;
        setTypeId();
        HouseMutilSelectHelper.getInstance().setList(this, rvCustomSpace, (List<TypeIdBean.TypeIdItem>) customSpace, null, 0, this);
        HouseMutilSelectHelper.getInstance().setList(this, rvFurnitureDemand, (List<TypeIdBean.TypeIdItem>) furnitureDemand, null, 1, this);

    }

    /**
     * 获取类型id数据失败
     */
    @Override
    public void getTypeIdFailed(String failed) {
        showShortToast(failed);
    }

    @Override
    public void addCustomerSuccess() {
        dismissLoading();
        showShortToast(R.string.tips_add_success);
        setResult(Constants.RESULT_CODE_ADD_SUCCESS);
        finish();
    }

    /**
     * 添加客户鼠标
     */
    @Override
    public void addCustomerFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 获取所属人成功
     */
    @Override
    public void getAssociateSuccess(List<? extends TypeIdBean.TypeIdItem> associates) {
        dismissLoading();
        this.associates = (List<AssociateBean.GuideBean>) associates;
        showPickerView(associates, tvAssociates.getText().toString());
    }

    /**
     * 获取所属人失败
     */
    @Override
    public void getAssociateFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void selectTime(Date date) {
        switch (type) {
            case TYPE_CUSTOM_DECORATE_TIME:
                tvCustomDecorateTime.setText(new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date));
                break;
            case TYPE_CUSTOM_FLOOR_TIME:
                tvCustomFloorTime.setText(new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date));
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


    /**
     * 显示loading
     */
    @Override
    public void loading() {
        showLoading();
    }

    @OnClick(R.id.et_baseCustomer_name)
    public void onViewClicked() {
    }

}
