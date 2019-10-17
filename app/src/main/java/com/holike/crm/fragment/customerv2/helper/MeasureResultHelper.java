package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.IImageSelectHelper;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 * 量尺结果帮助类
 */
public class MeasureResultHelper extends IImageSelectHelper {

    private MeasureResultCallback mCallback;
    private List<AdapterItem> mItems = new ArrayList<>();
    private SysCodeItemBean mSysCodeItemBean; //全局字典
    private CurrentUserBean mCurrentUserBean;
    private ItemAdapter mAdapter;
    private TextView mPlayStayTextView; //计划入住
    private TextView mMeasureDateTextView;
    private TextView mMeasureShopTextView; //选择量尺门店textView
    private TextView mMeasureByTextView;
    private TextView mAppConfirmTextView;
    private EditText mRemarkEditText;
    private String mHouseId; //房屋id（从详情bundle传值过来）
    private String mAreaType, //房屋面积
            mHouseType, //户型
            mHousePriceTypeCode,    //每平方米放假
            mMeasureBudgetTypeCode, //量尺沟通预算
            mPreferenceStyle,    //装修风格
            mDecorateProperties,    //房屋状态
            mDecorateProgress;  //装修进度
    private String mFamilyMember,  //家庭成员
            mCustomizeTheSpace, //定制空间
            mFurnitureDemand; //家具需求

    private String mPlannedStayDate, //计划入住时间
            mAmountOfDate, //量房完成时间
            mMeasureShopId, //量尺门店id
            mMeasureBy, //量尺人员id
            mMeasureAppConfirmTime; //预约确图时间
    private ArrayMap<Integer, Integer> mCacheSingle; //已经勾选的单选的数据集

    private ArrayMap<Integer, List<AdapterItem.ChoiceItem>> mCacheMultiple; //已经勾选的多选的数据集

    private List<String> mSelectedImages;  //已选择的图片 （从客户管理详情也带过来，属于网络图片）
    private List<String> mMeasureImages; //删除图片去schemeImgId，后台真是骚 搞不懂？？？？？？？？
    private List<String> mRemovedImages;  //被删除的网络图片数据
//    private List<DealerInfoBean> mDealerInfoList; //所有经销商设计师等数据
//    private List<DealerInfoBean.UserBean> mCurrentUserList; //当前选中的设计师集合

    public MeasureResultHelper(BaseFragment<?, ?> fragment, MeasureResultCallback callback) {
        super(fragment);
        this.mCallback = callback;
        mCacheSingle = new ArrayMap<>();
        mCacheMultiple = new ArrayMap<>();
        mSelectedImages = new ArrayList<>();
        mMeasureImages = new ArrayList<>();
        mRemovedImages = new ArrayList<>();
        mCurrentUserBean = IntentValue.getInstance().getCurrentUser();
        initView(fragment.getContentView());
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            setDataFromBundle(bundle);
        }
        initValues();
        setupSelectedImages();
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) {
            mCallback.onRequestSysCode();
        } else {
            mSysCodeItemBean = bean;
            mCallback.onShowContentView();
            initItems();
        }
    }

    private void initView(View contentView) {
        WrapperRecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_customer_measureresult, new FrameLayout(mContext), false);
        recyclerView.addHeaderView(headerView);
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_customer_measureresult, new LinearLayout(mContext), false);
        initFooterView(footerView);
        recyclerView.addFooterView(footerView);
        mAdapter = new ItemAdapter(mContext, mItems);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setMultipleChoiceListener(new OnMultipleChoiceListener() {
            @Override
            public void onSingleChoice(int targetPosition, AdapterItem.ChoiceItem item) {
                if (targetPosition == 0) { //房屋面积
                    mAreaType = item.id;
                } else if (targetPosition == 1) {   //户型
                    mHouseType = item.id;
                } else if (targetPosition == 3) { //每平方米房价
                    mHousePriceTypeCode = item.id;
                } else if (targetPosition == 4) {   //量尺沟通预算
                    mMeasureBudgetTypeCode = item.id;
                } else if (targetPosition == 7) { //装修风格
                    mPreferenceStyle = item.id;
                } else if (targetPosition == 8) {   //房屋状态
                    mDecorateProperties = item.id;
                } else if (targetPosition == 9) {   //装修进度
                    mDecorateProgress = item.id;
                }
            }

            @Override
            public void onMultipleChoice(int targetPosition, List<AdapterItem.ChoiceItem> selectedItems) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < selectedItems.size(); i++) {
                    sb.append(selectedItems.get(i).id);
                    if (i != selectedItems.size() - 1) {
                        sb.append(",");
                    }
                }
                if (targetPosition == 2) { //家庭成员
                    mFamilyMember = sb.toString();
                } else if (targetPosition == 5) { //定制空间
                    mCustomizeTheSpace = sb.toString();
                } else if (targetPosition == 6) { //家具需求
                    mFurnitureDemand = sb.toString();
                }
            }
        });
    }

    private void initFooterView(View view) {
        mPlayStayTextView = view.findViewById(R.id.tv_plan_stay);
//        tvPlanStay.setText(mPlannedStayDate);
        mMeasureDateTextView = view.findViewById(R.id.tv_measure_date);
//        tvMeasureDate.setText(mAmountOfDate);
        mMeasureShopTextView = view.findViewById(R.id.tv_measure_shop);
//        mMeasureShopTextView.setText(mMeasureShopName);
        mMeasureByTextView = view.findViewById(R.id.tv_surveyor);
//        tvSurveyor.setText(mMeasureByName);
        mAppConfirmTextView = view.findViewById(R.id.tv_reservation_date);
//        tvReservationDate.setText(mMeasureAppConfirmTime);
        mRemarkEditText = view.findViewById(R.id.et_remark);
//        mRemarkEditText.setText(mRemark);
        View.OnClickListener listener = v -> {
            int viewId = v.getId();
            if (viewId == R.id.tv_plan_stay) {
                onSelectPlanStayDate(mPlayStayTextView);
            } else if (viewId == R.id.tv_measure_date) {
                onSelectAmountOfDate(mMeasureDateTextView);
            } else if (viewId == R.id.tv_measure_shop) {
                if (mCurrentUserBean == null) {
                    mCallback.onQueryUserInfo();
                } else {
                    onSelectMeasureShop();
                }
            } else if (viewId == R.id.tv_surveyor) {
                if (TextUtils.isEmpty(mMeasureShopId)) {
                    String text = mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_actual_shop);
                    mCallback.onRequired(text);
                } else {
                    mCallback.onQueryMeasurer(mMeasureShopId);
                }
            } else if (viewId == R.id.tv_reservation_date) {
                onSelectMeasureConfirmTime(mAppConfirmTextView);
            }
        };
        mPlayStayTextView.setOnClickListener(listener);
        mMeasureDateTextView.setOnClickListener(listener);
        mMeasureShopTextView.setOnClickListener(listener);
        mMeasureByTextView.setOnClickListener(listener);
        mAppConfirmTextView.setOnClickListener(listener);
        RecyclerView rvPictures = view.findViewById(R.id.rv_pictures);
        rvPictures.setNestedScrollingEnabled(false);
        setupSelectImages(rvPictures, R.string.tips_add_measure_images);
    }

    /**
     * 量尺日期默认为今日，点击可以滑动选择其他时间。量尺人默认为本账号人员，点击可以滑动选择其他设计师。
     */
    private void initValues() {
        if (TextUtils.isEmpty(mAmountOfDate)) {
            Date date = new Date();
            mAmountOfDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
            mMeasureDateTextView.setText(TimeUtil.timeMillsFormat(date));
        }
//        setDefaultValue();
    }

    /*设置默认量尺人员为本账号*/
//   private void setDefaultValue() {
//        if (mCurrentUserBean == null) return;
//        if (mCurrentUserBean.getUserInfo() != null) {
//            CurrentUserBean.InfoBean infoBean = mCurrentUserBean.getUserInfo();
//            if (TextUtils.isEmpty(mMeasureBy)) {
//                mMeasureBy = infoBean.userId;
//                mMeasureByTextView.setText(infoBean.userName);
//            }
//        }
//    }

    private void setDataFromBundle(Bundle bundle) {
        mHouseId = bundle.getString(CustomerValue.HOUSE_ID); //房屋id
        mAreaType = bundle.getString("areaType"); //房屋面积
        mHouseType = bundle.getString("houseType"); //户型
        mHousePriceTypeCode = bundle.getString("housePriceCode");
        mMeasureBudgetTypeCode = bundle.getString("measureBudgetCode");
        mPreferenceStyle = bundle.getString("preferenceStyle");
        mDecorateProperties = bundle.getString("decorateProperties");
        mDecorateProgress = bundle.getString("decorateProgress");
        mFamilyMember = bundle.getString("familyMember");
        mCustomizeTheSpace = bundle.getString("customizeTheSpace");
        mFurnitureDemand = bundle.getString("furnitureDemand");
        String plannedStayDate = bundle.getString("plannedStayDate");
        mPlannedStayDate = TimeUtil.timeMillsFormat(plannedStayDate, "yyyy-MM-dd");
        mPlayStayTextView.setText(TimeUtil.timeMillsFormat(plannedStayDate));
        String amountOfDate = bundle.getString("amountOfDate");
        mAmountOfDate = TimeUtil.timeMillsFormat(amountOfDate, "yyyy-MM-dd");
        mMeasureDateTextView.setText(TimeUtil.timeMillsFormat(amountOfDate));
        mMeasureShopId = bundle.getString("shopId");
        mMeasureShopTextView.setText(bundle.getString("shopName"));
        mMeasureBy = bundle.getString("measureBy");
        mMeasureByTextView.setText(bundle.getString("measureByName"));
//        setDefaultValue();
        String measureAppConfirmTime = bundle.getString("measureAppConfirmTime");
        mMeasureAppConfirmTime = TimeUtil.timeMillsFormat(measureAppConfirmTime, "yyyy-MM-dd");
        mAppConfirmTextView.setText(TimeUtil.timeMillsFormat(measureAppConfirmTime));
        mRemarkEditText.setText(bundle.getString("remark"));
        ArrayList<String> images = bundle.getStringArrayList("images");
        ArrayList<String> schemeImages = bundle.getStringArrayList("measureImages");
        if (images != null && !images.isEmpty()) {
            mSelectedImages.addAll(images);
        }
        if (schemeImages != null && !schemeImages.isEmpty()) {
            mMeasureImages.addAll(schemeImages);
        }
    }

    private void setupSelectedImages() {
        mImageHelper.imageOptionsListener((position, path) -> {
            mImageHelper.remove(position);
            if (mSelectedImages.contains(path) && position >= 0 && position < mMeasureImages.size()) {  //删除网络图片
                mSelectedImages.remove(position);
                String imageUrl = mMeasureImages.remove(position);
                if (!mRemovedImages.contains(imageUrl)) {
                    mRemovedImages.add(imageUrl);
                }
            }
        }).setImagePaths(mSelectedImages);
    }

    /*选择计划入住时间*/
    private void onSelectPlanStayDate(final TextView tv) {
        PickerHelper.showTimePicker(mContext, date -> {
            mPlannedStayDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
            tv.setText(TimeUtil.timeMillsFormat(date));
        });
    }

    /*选择量房完成时间 不能选未来时间*/
    private void onSelectAmountOfDate(final TextView tv) {
        PickerHelper.showTimePicker(mContext, null, new Date(), date -> {
            if (date.after(new Date())) {  //选择了未来时间，则默认为当天时间
                mAmountOfDate = TimeUtil.timeMillsFormat(new Date(), "yyyy-MM-dd");
                tv.setText(TimeUtil.timeMillsFormat(new Date()));
            } else {
                mAmountOfDate = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
                tv.setText(TimeUtil.timeMillsFormat(date));
            }
        });
    }

    /*选择实际量尺门店*/
    private void onSelectMeasureShop() {
        List<DictionaryBean> optionItems = new ArrayList<>();
        final List<CurrentUserBean.ShopInfo> shopInfoList = mCurrentUserBean.getShopInfo();
        for (CurrentUserBean.ShopInfo infoBean : shopInfoList) {
            optionItems.add(new DictionaryBean(infoBean.shopId, infoBean.shopName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (position, bean) -> {
            mMeasureShopId = bean.id;
            mMeasureShopTextView.setText(bean.name);
            mMeasureBy = null; //清空已选的量尺人员
            mMeasureByTextView.setText(null);
        });
    }

    /*选择实际量尺人员*/
    public void onSelectMeasureUser(final List<ShopRoleUserBean.UserBean> userList) {
        List<DictionaryBean> optionItems = new ArrayList<>();
        for (ShopRoleUserBean.UserBean bean : userList) {
            optionItems.add(new DictionaryBean(bean.userId, bean.userName));
        }
        PickerHelper.showOptionsPicker(mContext, optionItems, (position, bean) -> {
            mMeasureBy = bean.id;
            mMeasureByTextView.setText(bean.name);
        });
    }

    /*选择预约确图时间*/
    private void onSelectMeasureConfirmTime(final TextView tv) {
        PickerHelper.showTimePicker(mContext, date -> {
            mMeasureAppConfirmTime = TimeUtil.timeMillsFormat(date, "yyyy-MM-dd");
            tv.setText(TimeUtil.timeMillsFormat(date));
        });
    }

    public void setSysCodeItemBean(SysCodeItemBean bean) {
        this.mSysCodeItemBean = bean;
        initItems();
    }

    public void setCurrentUserBean(CurrentUserBean bean) {
        this.mCurrentUserBean = bean;
//        setDefaultValue();
    }

    private void initItems() {
        List<AdapterItem.ChoiceItem> list;
        /*-------------------------------房屋面积---------------------------------------*/
        AdapterItem areaItem = new AdapterItem(0, mContext.getString(R.string.followup_house_area_tips2), true, false);
        list = addToList(mSysCodeItemBean.getCustomerAreaType());
        if (!TextUtils.isEmpty(mAreaType)) {  //已选的房屋面积
            int index = list.indexOf(new AdapterItem.ChoiceItem(mAreaType));
            if (index >= 0 && index < list.size()) {
                mCacheSingle.put(0, index);
            }
        }
        areaItem.items.addAll(list);
        mItems.add(areaItem);
        /*-------------------------------户型---------------------------------------*/
        AdapterItem houseTypeItem = new AdapterItem(1, mContext.getString(R.string.followup_house_type_tips2), false, false);
        list = addToList(mSysCodeItemBean.getCustomerHouseType());
        if (!TextUtils.isEmpty(mHouseType)) { //已选的户型
            int index = list.indexOf(new AdapterItem.ChoiceItem(mHouseType));
            if (index >= 0 && index < list.size()) {
                mCacheSingle.put(1, index);
            }
        }
        houseTypeItem.items.addAll(list);
        mItems.add(houseTypeItem);
        /*-------------------------------家庭成员---------------------------------------*/
        AdapterItem familyItem = new AdapterItem(2, mContext.getString(R.string.followup_family_member_tips2), false, true);
        list = addToList(mSysCodeItemBean.getFamilyMember());
        if (!TextUtils.isEmpty(mFamilyMember)) {  //已选中的家庭成员
            try {
                List<AdapterItem.ChoiceItem> selectItems = new ArrayList<>();
                String[] array = mFamilyMember.split(",");
                for (String code : array) {
                    AdapterItem.ChoiceItem item = new AdapterItem.ChoiceItem(code);
                    if (list.contains(item)) {
                        selectItems.add(item);
                    }
                }
                mCacheMultiple.put(2, selectItems);
            } catch (Exception ignored) {

            }
        }
        familyItem.items.addAll(list);
        mItems.add(familyItem);
        /*-------------------------------每平方米房价---------------------------------------*/
        AdapterItem meterItem = new AdapterItem(3, mContext.getString(R.string.followup_price_square_meter), false, false);
        list = addToList(mSysCodeItemBean.getHousePriceType());
        if (!TextUtils.isEmpty(mHousePriceTypeCode)) { //已选的每平方米房价
            int index = list.indexOf(new AdapterItem.ChoiceItem(mHousePriceTypeCode));
            if (index >= 0 && index < list.size()) {
                mCacheSingle.put(3, index);
            }
        }
        meterItem.items.addAll(list);
        mItems.add(meterItem);
        /*-------------------------------量尺沟通预算---------------------------------------*/
        AdapterItem budgetItem = new AdapterItem(4, mContext.getString(R.string.followup_measure_communication_budget), true, false);
        list = addToList(mSysCodeItemBean.getCustomerBudgetType());
        if (!TextUtils.isEmpty(mMeasureBudgetTypeCode)) { //已选的沟通预算
            int index = list.indexOf(new AdapterItem.ChoiceItem(mMeasureBudgetTypeCode));
            if (index >= 0 && index < list.size()) {
                mCacheSingle.put(4, index);
            }
        }
        budgetItem.items.addAll(list);
        mItems.add(budgetItem);
        /*-------------------------------定制空间---------------------------------------*/
        AdapterItem spaceItem = new AdapterItem(5, mContext.getString(R.string.followup_custom_space_tips2), true, true);
        list = addToList(mSysCodeItemBean.getCustomerMeasureSpace());
        if (!TextUtils.isEmpty(mCustomizeTheSpace)) {  //已选中的定制空间集合
            try {
                List<AdapterItem.ChoiceItem> selectItems = new ArrayList<>();
                String[] array = mCustomizeTheSpace.split(",");
                for (String code : array) {
                    AdapterItem.ChoiceItem item = new AdapterItem.ChoiceItem(code);
                    if (list.contains(item)) {
                        selectItems.add(item);
                    }
                }
                mCacheMultiple.put(5, selectItems);
            } catch (Exception ignored) {

            }
        }
        spaceItem.items.addAll(list);
        mItems.add(spaceItem);
        /*-------------------------------家具需求---------------------------------------*/
        AdapterItem householdItem = new AdapterItem(6, mContext.getString(R.string.followup_household_demand_tips2), true, true);
        list = addToList(mSysCodeItemBean.getFurnitureDemand());
        if (!TextUtils.isEmpty(mFurnitureDemand)) {  //已选中的家具需求集合
            try {
                List<AdapterItem.ChoiceItem> selectItems = new ArrayList<>();
                String[] array = mFurnitureDemand.split(",");
                for (String code : array) {
                    AdapterItem.ChoiceItem item = new AdapterItem.ChoiceItem(code);
                    if (list.contains(item)) {
                        selectItems.add(item);
                    }
                }
                mCacheMultiple.put(6, selectItems);
            } catch (Exception ignored) {

            }
        }
        householdItem.items.addAll(list);
        mItems.add(householdItem);
        /*-------------------------------装修风格---------------------------------------*/
        AdapterItem styleItem = new AdapterItem(7, mContext.getString(R.string.followup_decoration_style_tips2), false, false);
        list = addToList(mSysCodeItemBean.getDecorationStyle());
        if (!TextUtils.isEmpty(mPreferenceStyle)) { //已选的装修风格
            int index = list.indexOf(new AdapterItem.ChoiceItem(mPreferenceStyle));
            if (index >= 0 && index < list.size()) {
                mCacheSingle.put(7, index);
            }
        }
        styleItem.items.addAll(list);
        mItems.add(styleItem);
        /*-------------------------------房屋状态---------------------------------------*/
        AdapterItem statusItem = new AdapterItem(8, mContext.getString(R.string.followup_house_status_tips2), false, false);
        list = addToList(mSysCodeItemBean.getHouseStatus());
        if (!TextUtils.isEmpty(mDecorateProperties)) {    //已选的房屋状态
            int index = list.indexOf(new AdapterItem.ChoiceItem(mDecorateProperties));
            if (index >= 0 && index < list.size()) {
                mCacheSingle.put(8, index);
            }
        }
        statusItem.items.addAll(list);
        mItems.add(statusItem);
        /*-------------------------------装修进度---------------------------------------*/
        AdapterItem progressItem = new AdapterItem(9, mContext.getString(R.string.followup_decoration_progress_tips2), false, false);
        list = addToList(mSysCodeItemBean.getDecorateProgress());
        if (!TextUtils.isEmpty(mDecorateProgress)) {    //已选的房屋状态
            int index = list.indexOf(new AdapterItem.ChoiceItem(mDecorateProgress));
            if (index >= 0 && index < list.size()) {
                mCacheSingle.put(9, index);
            }
        }
        progressItem.items.addAll(list);
        mItems.add(progressItem);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<AdapterItem.ChoiceItem> addToList(@NonNull Map<String, String> params) {
        List<AdapterItem.ChoiceItem> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            list.add(new AdapterItem.ChoiceItem(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    static class AdapterItem {
        int targetPosition;
        String title;
        boolean isMust; //必填
        boolean isMultiChoice; //多选
        List<ChoiceItem> items = new ArrayList<>();

        AdapterItem(int targetPosition, String title, boolean isMust, boolean isMultiChoice) {
            this.targetPosition = targetPosition;
            this.title = title;
            this.isMust = isMust;
            this.isMultiChoice = isMultiChoice;
        }

        static class ChoiceItem {
            String id;
            String content;

            ChoiceItem(String id) {
                this.id = id;
            }

            ChoiceItem(String id, String content) {
                this.id = id;
                this.content = content;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (obj == this) return true;
                if (obj instanceof ChoiceItem) {
                    return TextUtils.equals(id, ((ChoiceItem) obj).id);
                } else {
                    return false;
                }
            }
        }
    }

    interface OnMultipleChoiceListener {
        void onSingleChoice(int targetPosition, AdapterItem.ChoiceItem item);

        void onMultipleChoice(int targetPosition, List<AdapterItem.ChoiceItem> selectedItems);
    }

    class ItemAdapter extends CommonAdapter<AdapterItem> {

        OnMultipleChoiceListener mMultipleChoiceListener;

        void setMultipleChoiceListener(OnMultipleChoiceListener multipleChoiceListener) {
            this.mMultipleChoiceListener = multipleChoiceListener;
        }

        ItemAdapter(Context context, List<AdapterItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_customer_houseinfo;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, AdapterItem item, int position) {
            if (position == 0) {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_top_white_5dp);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_title, item.title);
            holder.setVisibility(R.id.tv_star, item.isMust);
            RecyclerView rvFlow = holder.obtainView(R.id.rv_flow);
            rvFlow.setNestedScrollingEnabled(false);
            CommonAdapter adapter;
            if (item.isMultiChoice) {
                adapter = new MultipleChoiceItemAdapter(mContext, item.items, item.targetPosition, mCacheMultiple.get(item.targetPosition));
            } else {
                adapter = new SingleChoiceItemAdapter(mContext, item.items, item.targetPosition, mCacheSingle.get(item.targetPosition));
            }
            rvFlow.setAdapter(adapter);
        }

        class SingleChoiceItemAdapter extends CommonAdapter<AdapterItem.ChoiceItem> {
            private int mTargetPosition;
            private int mSelectPosition = -1;

            SingleChoiceItemAdapter(Context context, List<AdapterItem.ChoiceItem> mDatas, int targetPosition, @Nullable Integer selectPosition) {
                super(context, mDatas);
                this.mTargetPosition = targetPosition;
                if (selectPosition != null) {
                    this.mSelectPosition = selectPosition;
                }
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_flexbox_optional;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, final AdapterItem.ChoiceItem choiceItem, int position) {
                ImageView ivSelect = holder.obtainView(R.id.iv_select);
                TextView tvContent = holder.obtainView(R.id.tv_content);
                if (mSelectPosition == position) {
                    ivSelect.setImageResource(R.drawable.ic_radio_button_checked);
                    tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                } else {
                    ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked);
                    tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
                }
                holder.itemView.setOnClickListener(view -> {
                    mSelectPosition = position;
                    mCacheSingle.put(mTargetPosition, mSelectPosition);
                    if (mMultipleChoiceListener != null) {
                        mMultipleChoiceListener.onSingleChoice(mTargetPosition, choiceItem);
                    }
                    notifyDataSetChanged();
                });
                tvContent.setText(choiceItem.content);
            }
        }

        class MultipleChoiceItemAdapter extends CommonAdapter<AdapterItem.ChoiceItem> {
            private int mTargetPosition;
            private List<AdapterItem.ChoiceItem> mSelectedItems;

            MultipleChoiceItemAdapter(Context context, List<AdapterItem.ChoiceItem> mDatas, int targetPosition, @Nullable List<AdapterItem.ChoiceItem> selectItems) {
                super(context, mDatas);
                this.mTargetPosition = targetPosition;
                mSelectedItems = new ArrayList<>();
                if (selectItems != null && !selectItems.isEmpty()) {
                    mSelectedItems.addAll(selectItems);
                }
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_flexbox_optional;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, final AdapterItem.ChoiceItem item, int position) {
                final ImageView ivSelect = holder.obtainView(R.id.iv_select);
                final TextView tvContent = holder.obtainView(R.id.tv_content);
                setChecked(ivSelect, tvContent, mSelectedItems.contains(item));
                holder.itemView.setOnClickListener(view -> {
                    if (mSelectedItems.contains(item)) {
                        mSelectedItems.remove(item);
                        setChecked(ivSelect, tvContent, false);
                    } else {
                        mSelectedItems.add(item);
                        setChecked(ivSelect, tvContent, true);
                    }
                });
                tvContent.setText(item.content);
            }

            void setChecked(ImageView iv, TextView tv, boolean isChecked) {
                if (isChecked) {
                    iv.setImageResource(R.drawable.cus_scale_space_sel);
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                } else {
                    iv.setImageResource(R.drawable.cus_scale_space_nor);
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
                }
                mCacheMultiple.put(mTargetPosition, mSelectedItems);
                if (mMultipleChoiceListener != null) {
                    mMultipleChoiceListener.onMultipleChoice(mTargetPosition, mSelectedItems);
                }
            }
        }
    }

    /*点击保存按钮*/
    public void onSave() {
        if (TextUtils.isEmpty(mAreaType)) {  //房屋面积必填
            mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_house_area_tips2));
        } else {
            if (TextUtils.isEmpty(mMeasureBudgetTypeCode)) {  //量尺沟通预算必填
                mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_measure_communication_budget));
            } else {
                if (TextUtils.isEmpty(mCustomizeTheSpace)) { //定制空间必填
                    mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_custom_space_tips2));
                } else {
                    if (TextUtils.isEmpty(mFurnitureDemand)) { //家具需求必填
                        mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_household_demand_tips2));
                    } else {
                        if (TextUtils.isEmpty(mAmountOfDate)) { //实际量房日期必选
                            mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_real_measure_time2));
                        } else {
                            if (TextUtils.isEmpty(mMeasureShopId)) {  //量尺门店 必选
                                mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_actual_shop));
                            } else {
                                if (TextUtils.isEmpty(mMeasureBy)) { //量尺人员必填
                                    mCallback.onRequired(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_actual_surveyor2));
                                } else {
                                    List<String> images = mImageHelper.getSelectedImages();
                                    List<String> targetImages = new ArrayList<>(); //只上传新添加进来的图片，即从相册新增进来的图片
                                    for (String image : images) { //过滤掉从详情带过来的图片（不作上传，从相册新添加进来的才做上传）
                                        if (!mSelectedImages.contains(image)) {
                                            targetImages.add(image);
                                        }
                                    }
                                    mCallback.onSaved(ParamHelper.Customer.finishMeasure(mRemovedImages, mHouseId, mAreaType,
                                            mMeasureBudgetTypeCode, mCustomizeTheSpace, mFurnitureDemand,
                                            mAmountOfDate, mMeasureShopId, mMeasureBy, mHouseType, mFamilyMember, mHousePriceTypeCode,
                                            mPreferenceStyle, mDecorateProperties, mDecorateProgress, mPlannedStayDate,
                                            mMeasureAppConfirmTime, mRemarkEditText.getText().toString()), targetImages);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public interface MeasureResultCallback {
        void onRequestSysCode();

        void onShowContentView();

        void onQueryUserInfo();

        void onQueryMeasurer(String shopId);

        void onRequired(String text);

        void onSaved(Map<String, Object> params, List<String> images);
    }
}
