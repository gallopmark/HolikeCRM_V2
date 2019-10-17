package com.holike.crm.activity.customer.helper;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;


import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.web.WebViewActivity;
import com.holike.crm.adapter.SquareImageGridAdapter;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CustomerOnlineLogBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.TextSpanHelper;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOnlineLogHelper extends ActivityHelper {

    static class DictionaryPattern {
        /*房屋状态*/
        static String getHouseStatus(String houseStatus) {
            return singlePattern(houseStatus, getHouseStatusDict());
        }

        static Map<String, String> getHouseStatusDict() {
            Map<String, String> map = new HashMap<>();
            map.put("01", "精装房");
            map.put("02", "毛坯房");
            map.put("03", "自建房");
            map.put("04", "二手房");
            map.put("05", "在住房");
            map.put("06", "简装");
            map.put("07", "旧房翻新");
            map.put("08", "其他");
            return map;
        }

        /*房屋类型*/
        static String getHouseType(String houseType) {
            return singlePattern(houseType, getHouseTypeDict());
        }

        static Map<String, String> getHouseTypeDict() {
            Map<String, String> map = new HashMap<>();
            map.put("01", "单间");
            map.put("02", "一房一厅");
            map.put("03", "二房一厅");
            map.put("04", "二房二厅");
            map.put("05", "三房一厅");
            map.put("06", "三房二厅");
            map.put("07", "四房二厅");
            map.put("08", "别墅");
            map.put("09", "复式");
            map.put("10", "其他");
            return map;
        }

        /*预约类型*/
        static String getFollowType(String followCode) {
            return singlePattern(followCode, getFollowTypeDict());
        }

        static Map<String, String> getFollowTypeDict() {
            Map<String, String> map = new HashMap<>();
            map.put("0", "预约进店");
            map.put("1", "预约量尺");
            map.put("2", "待跟进");
            map.put("", "无");
            return map;
        }

        /*装修情况*/
        static String getDecorationProgress(String decorationProgress) {
            return singlePattern(decorationProgress, getDecorationProgressDict());
        }

        static Map<String, String> getDecorationProgressDict() {
            Map<String, String> map = new HashMap<>();
            map.put("01", "已装修");
            map.put("02", "未装修");
            map.put("03", "改主体");
            map.put("04", "水电");
            map.put("05", "铺地砖");
            map.put("06", "刷墙");
            map.put("07", "地暖");
            map.put("08", "其它");
            return map;
        }

        /*定制品类*/
        static String getCustomClass(String customClass) {
            return multiplePattern(customClass, getCustomClassDict());
        }

        static Map<String, String> getCustomClassDict() {
            Map<String, String> map = new HashMap<>();
            map.put("01", "全屋定制");
            map.put("02", "橱柜定制");
            map.put("03", "木门定制");
            map.put("04", "成品宅配");
            map.put("05", "其它");
            return map;
        }

        /*定制空间*/
        static String getCustomSpace(String customSpace) {
            return multiplePattern(customSpace, getCustomSpaceDict());
        }

        static Map<String, String> getCustomSpaceDict() {
            Map<String, String> map = new HashMap<>();
            map.put("01", "卧室");
            map.put("02", "客厅");
            map.put("03", "厨房");
            map.put("04", "儿童房");
            map.put("05", "玄关");
            map.put("06", "餐厅");
            map.put("07", "阳台");
            map.put("08", "书房");
            map.put("09", "功能房");
            return map;
        }

        /*家具需求*/
        static String getFurnitureDemand(String furnitureDemand) {
            return multiplePattern(furnitureDemand, getFurnitureDemandDict());
        }

        static Map<String, String> getFurnitureDemandDict() {
            //['衣柜','橱柜','榻榻米','电视柜','餐边柜','酒柜','鞋柜','隔断','玄关柜','书柜',
            // '书桌','飘窗柜','上下床','阳台柜','衣帽间','沙发','床','餐桌椅','其他']
            Map<String, String> map = new HashMap<>();   //装修情况
            map.put("01", "衣柜");
            map.put("02", "橱柜");
            map.put("03", "榻榻米");
            map.put("04", "电视柜");
            map.put("05", "餐边柜");
            map.put("06", "酒柜");
            map.put("07", "鞋柜");
            map.put("08", "隔断");
            map.put("09", "玄关柜");
            map.put("10", "书柜");
            map.put("11", "书桌");
            map.put("12", "飘窗柜");
            map.put("13", "上下床");
            map.put("14", "阳台柜");
            map.put("15", "衣帽间");
            map.put("16", "沙发");
            map.put("17", "床");
            map.put("18", "餐桌椅");
            map.put("19", "其他");
            return map;
        }

        /*板材喜好*/
        static String getBoardType(String boardType) {
            return singlePattern(boardType, getBoardTypeDict());
        }

        static Map<String, String> getBoardTypeDict() {
            //['原态板','颗粒板','中纤板','实木板','实木多层板','其它']
            Map<String, String> map = new HashMap<>();
            map.put("01", "原态板");
            map.put("02", "颗粒板");
            map.put("03", "中纤板");
            map.put("04", "实木板");
            map.put("05", "实木多层板");
            map.put("06", "其它");
            return map;
        }

        static String getDecorationStyle(String decorationStyle) {
            return singlePattern(decorationStyle, getDecorationStyleDict());
        }

        static Map<String, String> getDecorationStyleDict() {
            //['现代简约','简欧','中式','田园','美式','北欧','日式','欧式','地中海','其他']
            Map<String, String> map = new HashMap<>();
            map.put("01", "现代简约");
            map.put("02", "简欧");
            map.put("03", "中式");
            map.put("04", "田园");
            map.put("05", "美式");
            map.put("06", "北欧");
            map.put("07", "日式");
            map.put("08", "欧式");
            map.put("09", "地中海");
            map.put("10", "其他");
            return map;
        }

        static String singlePattern(String typeCode, Map<String, String> typeMap) {
            if (TextUtils.isEmpty(typeCode)) return "";
            if (typeMap.containsKey(typeCode)) {
                return typeMap.get(typeCode);
            }
            return "";
        }

        static String multiplePattern(String typeCode, Map<String, String> typeMap) {
            if (TextUtils.isEmpty(typeCode)) return "";
            try {
                String[] array = typeCode.split(",");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < array.length; i++) {
                    String value = typeMap.get(array[i]);
                    if (!TextUtils.isEmpty(value)) {
                        sb.append(value);
                        if (i < array.length - 1) {
                            sb.append("、");
                        }
                    }
                }
                return sb.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }

    private RecyclerView mRecyclerView;
    private OnlineLogCallback mCallback;
    private List<MultipleItem> mDataList;
    private OnlineLogAdapter mAdapter;

    public CustomerOnlineLogHelper(BaseActivity<?, ?> activity, OnlineLogCallback callback) {
        super(activity);
        this.mCallback = callback;
        mDataList = new ArrayList<>();
        mRecyclerView = obtainView(R.id.rv_content);
        mAdapter = new OnlineLogAdapter(mActivity, mDataList);
        mRecyclerView.setAdapter(mAdapter);
        getOnlineLog();
    }

    private void getOnlineLog() {
        String customerId = mActivity.getIntent().getStringExtra(CustomerValue.PERSONAL_ID);
        mCallback.onRequestOnlineLog(customerId);
    }

    public void onSuccess(CustomerOnlineLogBean bean) {
        mActivity.hasData();
        mRecyclerView.setVisibility(View.VISIBLE);
        bindData(bean);
    }

    private void bindData(CustomerOnlineLogBean bean) {
        if (!bean.getPromotionList().isEmpty()) {
            MultipleItem titleItem = new MultipleItem(MultipleItem.TYPE_TITLE_PROMOTION);
            titleItem.childItem = new PromotionItem(bean.getPromotionList().get(0));
            mDataList.add(titleItem);
            mDataList.add(titleItem.childItem);
        }
        CustomerOnlineLogBean.CustomerLogBean logBean = bean.getCustomerLogBean();
        if (logBean != null) {
            MultipleItem customerInfoTitleItem = new MultipleItem(MultipleItem.TYPE_TITLE_CUSTOMER_INFO);
            customerInfoTitleItem.childItem = new CustomerInfoItem(logBean);
            mDataList.add(customerInfoTitleItem);
            mDataList.add(customerInfoTitleItem.childItem);
            MultipleItem followupTitleItem = new MultipleItem(MultipleItem.TYPE_TITLE_FOLLOWUP);
            followupTitleItem.childItem = new FollowupItem(logBean);
            mDataList.add(followupTitleItem);
            mDataList.add(followupTitleItem.childItem);
        }
        if (mDataList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mActivity.noResult();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onFailure(String failReason) {
        mRecyclerView.setVisibility(View.GONE);
        mActivity.noNetwork(failReason);
    }

    public void reload() {
        getOnlineLog();
    }

    public interface OnlineLogCallback {
        void onRequestOnlineLog(String customerId);
    }

    class MultipleItem implements MultiItem {
        static final int TYPE_TITLE_PROMOTION = 1;
        static final int TYPE_PROMOTION = 2;
        static final int TYPE_TITLE_CUSTOMER_INFO = 3;
        static final int TYPE_CUSTOMER_INFO = 4;
        static final int TYPE_TITLE_FOLLOWUP = 5;
        static final int TYPE_FOLLOWUP = 6;

        boolean isCollapse; //是否收起
        int itemType;

        MultipleItem childItem;

        MultipleItem(int itemType) {
            this.itemType = itemType;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }

    class PromotionItem extends MultipleItem {
        CustomerOnlineLogBean.PromotionBean bean;

        PromotionItem(CustomerOnlineLogBean.PromotionBean bean) {
            super(TYPE_PROMOTION);
            this.bean = bean;
        }
    }

    class CustomerInfoItem extends MultipleItem {
        String customerName; //客户姓名
        String customerPhone; //客户手机
        String provinceArea;
        String tmallId; // 京东/旺旺id
        String orderNumber; //订单编号
        String orderAmount;//订单金额
        String gift; // 赠品/优惠券
        String waitShopDatetime; //下发客户时间
        String reutrnDateTime; //无效驳回时间
        String signupRemark; //客服跟进留言

        String buildingInformation; //楼盘信息
        String address;  //详细地址
        String houseStatus;  //房屋状态
        String houseArea;  //房屋面积
        String houseType;  //房屋户型
        String checkBulidingValue;  //是否收楼(01为已收楼，02为未收楼)
        String checkbulidingTime;  //收楼时间
        String decorationProgress;  //装修情况 (01-已装修/装修中，02-未装修)
        String decorationStartDatetime;  //装修日期
        String customClass;  //定制品类
        String customSpace;  //定制空间
        String furnitureDemand;  //家具需求
        String boardType;  //板材喜好
        String decorationStyle;  //风格喜好
        String decorationBudget;  //定制预算
        String expectMeasureDatetime; //预约量尺时间

        CustomerInfoItem(CustomerOnlineLogBean.CustomerLogBean bean) {
            super(TYPE_CUSTOMER_INFO);
            CustomerOnlineLogBean.SignUpBean signUpBean = bean.getSignUpBean();
            if (signUpBean != null) {
                customerName = signUpBean.customerName;
                customerPhone = signUpBean.customerPhone;
                provinceArea = (TextUtils.isEmpty(signUpBean.province) ? "" : signUpBean.province)
                        + (TextUtils.isEmpty(signUpBean.city) ? "" : signUpBean.city)
                        + (TextUtils.isEmpty(signUpBean.district) ? "" : signUpBean.district);
                tmallId = signUpBean.tmallId;
                orderNumber = signUpBean.orderNumber;
                orderAmount = signUpBean.orderAmount;
                gift = signUpBean.gift;
                waitShopDatetime = signUpBean.waitShopDatetime;
                reutrnDateTime = signUpBean.reutrnDateTime;
                signupRemark = signUpBean.signupRemark;
            }
            CustomerOnlineLogBean.InterviewBean interviewBean = bean.getInterviewBean();
            if (interviewBean != null) {
                buildingInformation = interviewBean.buildingInformation;
                address = interviewBean.address;
                houseStatus = DictionaryPattern.getHouseStatus(interviewBean.houseStatus);
                houseArea = interviewBean.houseArea;
                houseType = DictionaryPattern.getHouseType(interviewBean.houseType);
                checkBulidingValue = TextUtils.equals(interviewBean.checkbulidingCode, "01") ? "是" : "否";
                checkbulidingTime = interviewBean.getCheckbulidingTime();
                decorationProgress = DictionaryPattern.getDecorationProgress(interviewBean.decorationProgress);
                decorationStartDatetime = interviewBean.getDecorationStartDatetime();
                customClass = DictionaryPattern.getCustomClass(interviewBean.customClass);
                customSpace = DictionaryPattern.getCustomSpace(interviewBean.customSpace);
                furnitureDemand = DictionaryPattern.getFurnitureDemand(interviewBean.furnitureDemand);
                boardType = DictionaryPattern.getBoardType(interviewBean.boardType);
                decorationStyle = DictionaryPattern.getDecorationStyle(interviewBean.decorationStyle);
                decorationBudget = interviewBean.decorationBudget;
                expectMeasureDatetime = interviewBean.getExpectMeasureDatetime();
            }
        }
    }

    class FollowupItem extends MultipleItem {
        String followTypeTime; //预约类型时间
        String waitShopDatetime; //下发客户时间
        String returnDateTime; //无效驳回时间
        String signupRemark; //客服跟进留言
        String recordValidDatetime; //确认有效时间
        String followUpRecord; //聊天记录
        List<String> images = new ArrayList<>();

        FollowupItem(CustomerOnlineLogBean.CustomerLogBean bean) {
            super(TYPE_FOLLOWUP);
            CustomerOnlineLogBean.SignUpBean infoBean = bean.getSignUpBean();
            if (infoBean != null) {
                waitShopDatetime = TimeUtil.timeMillsFormat(infoBean.waitShopDatetime);
                returnDateTime = TimeUtil.timeMillsFormat(infoBean.reutrnDateTime);
                signupRemark = TextUtils.isEmpty(infoBean.signupRemark) ? "" : infoBean.signupRemark.replaceAll("=", "\n");
                followUpRecord = infoBean.followUpRecord;
                followTypeTime = DictionaryPattern.getFollowType(infoBean.followCode);
            }
            CustomerOnlineLogBean.InterviewBean interviewBean = bean.getInterviewBean();
            if (interviewBean != null) {
                if (TextUtils.isEmpty(followTypeTime)) {
                    followTypeTime = interviewBean.getExpectMeasureDatetime();
                } else {
                    followTypeTime += "\u3000" + interviewBean.getExpectMeasureDatetime();
                }
            }
            CustomerOnlineLogBean.ShopDataBean shopDataBean = bean.getShopData();
            if (shopDataBean != null) {
                recordValidDatetime = TimeUtil.timeMillsFormat(shopDataBean.recordValidDatetime);
            }
            for (CustomerOnlineLogBean.ResourceDataBean rdBean : bean.getResourceData()) {
                images.add(rdBean.filePath);
            }
        }
    }

    class OnlineLogAdapter extends CommonAdapter<MultipleItem> {

        TextSpanHelper mTextHelper;

        OnlineLogAdapter(Context context, List<MultipleItem> mDatas) {
            super(context, mDatas);
            mTextHelper = TextSpanHelper.from(mContext);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == MultipleItem.TYPE_TITLE_PROMOTION || viewType == MultipleItem.TYPE_TITLE_CUSTOMER_INFO ||
                    viewType == MultipleItem.TYPE_TITLE_FOLLOWUP) {
                return R.layout.item_customer_onlinelog_title;
            } else if (viewType == MultipleItem.TYPE_PROMOTION) {
                return R.layout.item_customer_onlinelog_promotion;
            } else if (viewType == MultipleItem.TYPE_CUSTOMER_INFO) {
                return R.layout.item_customer_onlinelog_customerinfo;
            }
            return R.layout.item_customer_onlinelog_followup;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultipleItem multipleItem, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == MultipleItem.TYPE_TITLE_PROMOTION) {
                holder.setText(R.id.tv_title, mContext.getString(R.string.tips_title_promotion));
                bindExtensibility(holder, multipleItem, position);
            } else if (itemType == MultipleItem.TYPE_PROMOTION) {
                PromotionItem item = (PromotionItem) multipleItem;
                bindPromotionItem(holder, item.bean);
            } else if (itemType == MultipleItem.TYPE_TITLE_CUSTOMER_INFO) {
                holder.setText(R.id.tv_title, mContext.getString(R.string.tips_title_customer_info));
                bindExtensibility(holder, multipleItem, position);
            } else if (itemType == MultipleItem.TYPE_CUSTOMER_INFO) {
                CustomerInfoItem item = (CustomerInfoItem) multipleItem;
                bindCustomerInfoItem(holder, item);
            } else if (itemType == MultipleItem.TYPE_TITLE_FOLLOWUP) {
                holder.setText(R.id.tv_title, mContext.getString(R.string.followup_record));
                bindExtensibility(holder, multipleItem, position);
            } else {
                FollowupItem item = (FollowupItem) multipleItem;
                bindFollowupItem(holder, item);
            }
        }

        private void bindExtensibility(RecyclerHolder holder, MultipleItem multipleItem, int position) {
            if (multipleItem.isCollapse) {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_white_5dp);
                holder.setDrawableRight(R.id.tv_title, R.drawable.ic_arrow_down_accent);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_top_white_5dp);
                holder.setDrawableRight(R.id.tv_title, R.drawable.ic_arrow_up_accent);
            }
            holder.itemView.setOnClickListener(view -> {
                if (multipleItem.isCollapse) {  //已收起，则展开
                    expandItem(holder, multipleItem, position);
                } else {
                    collapseItem(holder, multipleItem, position);
                }
            });
        }

        /*收起*/
        private void collapseItem(RecyclerHolder holder, MultipleItem multipleItem, int position) {
            holder.setDrawableRight(R.id.tv_title, R.drawable.ic_arrow_down_accent);
            multipleItem.isCollapse = true;
            int index = position + 1;
            mDatas.remove(index);
            notifyDataSetChanged();
        }

        /*展开*/
        private void expandItem(RecyclerHolder holder, MultipleItem multipleItem, int position) {
            holder.setDrawableRight(R.id.tv_title, R.drawable.ic_arrow_up_accent);
            multipleItem.isCollapse = false;
            int index = position + 1;
            mDatas.add(index, multipleItem.childItem);
            notifyDataSetChanged();
        }

        private void bindPromotionItem(RecyclerHolder holder, CustomerOnlineLogBean.PromotionBean bean) {
            holder.setText(R.id.tv_primary_channel, bean.parentName);
            holder.setText(R.id.tv_secondary_channel, bean.twoAdName);
            holder.setText(R.id.tv_program_name, bean.adName);
            if (!TextUtils.isEmpty(bean.URL)) {
                holder.setText(R.id.tv_registration_path, bean.URL);
                holder.setTextColorRes(R.id.tv_registration_path, R.color.colorAccent);
                holder.setEnabled(R.id.tv_registration_path, true);
            } else {
                holder.setText(R.id.tv_registration_path, mContext.getString(R.string.not_filled_in));
                holder.setTextColorRes(R.id.tv_registration_path, R.color.textColor4);
                holder.setEnabled(R.id.tv_registration_path, false);
            }
            holder.setOnClickListener(R.id.tv_registration_path, view -> WebViewActivity.open(mActivity, bean.URL, mContext.getString(R.string.tips_registration_path)));
        }

        private void bindCustomerInfoItem(RecyclerHolder holder, CustomerInfoItem item) {
            holder.setText(R.id.tv_customer_name, obtainText(R.string.customer_name_tips, item.customerName));
            holder.setText(R.id.tv_customer_phone, obtainText(R.string.customer_phone_tips, item.customerPhone));
            holder.setText(R.id.tv_province_area, obtainText(0, item.provinceArea));
            holder.setText(R.id.tv_detail_address, obtainText(0, item.address));
            holder.setText(R.id.tv_house_status, obtainText(R.string.followup_house_status_tips, item.houseStatus));
            holder.setText(R.id.tv_house_area, obtainText(R.string.followup_house_area_tips, item.houseArea));
            holder.setText(R.id.tv_whether_repossess, obtainText(R.string.tips_whether_repossess, item.checkBulidingValue));
            holder.setText(R.id.tv_repossession_date, obtainText(R.string.tips_repossession_date, item.checkbulidingTime));
            holder.setText(R.id.tv_renovation_condition, obtainText(R.string.tips_renovation_condition, item.decorationProgress));
            holder.setText(R.id.tv_renovation_date, obtainText(R.string.tips_renovation_date, item.decorationStartDatetime));
            holder.setText(R.id.tv_custom_products, obtainText(0, item.customClass));
            holder.setText(R.id.tv_custom_space, obtainText(0, item.customSpace));
            holder.setText(R.id.tv_household_demand, obtainText(0, item.furnitureDemand));
            holder.setText(R.id.tv_plate_preference, obtainText(R.string.tips_plate_preference, item.boardType));
            holder.setText(R.id.tv_style_preferences, obtainText(R.string.tips_style_preferences, item.decorationStyle));
            holder.setText(R.id.tv_JD_WW_ID, obtainText(0, item.tmallId));
            holder.setText(R.id.tv_order_number, obtainText(0, item.orderNumber));
            holder.setText(R.id.tv_order_amount, obtainText(R.string.tips_order_amount, item.orderAmount));
            holder.setText(R.id.tv_gifts_coupons, obtainText(R.string.tips_gifts_coupons, item.gift));
        }

        private void bindFollowupItem(RecyclerHolder holder, FollowupItem item) {
            holder.setText(R.id.tv_appointment_type, obtainText(0, item.followTypeTime));
            holder.setText(R.id.tv_deliver_customers, obtainText(0, item.waitShopDatetime));
            holder.setText(R.id.tv_invalid_refusal, obtainText(0, item.returnDateTime));
            holder.setText(R.id.tv_confirm_valid, obtainText(0, item.recordValidDatetime));
            holder.setText(R.id.tv_customer_service_message, obtainText(0, item.signupRemark));
            if (item.images.isEmpty()) {
                holder.setVisibility(R.id.ll_pictures_layout, View.GONE);
            } else {
                holder.setVisibility(R.id.ll_pictures_layout, View.VISIBLE);
                RecyclerView rvPictures = holder.obtainView(R.id.rv_pictures);
                if (rvPictures.getItemDecorationCount() == 0) {
                    rvPictures.addItemDecoration(new GridSpacingItemDecoration(3, mContext.getResources().getDimensionPixelSize(R.dimen.dp_10), false));
                }
                rvPictures.setAdapter(new SquareImageGridAdapter(mContext, item.images));
            }
            holder.setText(R.id.tv_chat_logs, obtainText(0, item.followUpRecord));
        }

        private CharSequence obtainText(int stringRes, String source) {
            String content = TextUtils.isEmpty(source) ? mContext.getString(R.string.not_filled_in) : source;
            return mTextHelper.obtainColorBoldStyle(stringRes, content, R.color.textColor4);
        }
    }
}
