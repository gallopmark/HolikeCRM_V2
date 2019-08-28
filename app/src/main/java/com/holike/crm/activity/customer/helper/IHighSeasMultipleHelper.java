package com.holike.crm.activity.customer.helper;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.helper.TextSpanHelper;
import com.holike.crm.http.CustomerUrlPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/8/18.
 * Copyright holike possess 2019.
 */
class IHighSeasMultipleHelper {

    static class OperateCode {
        static final String CODE_GUIDE = "01";
        static final String CODE_UNMEASURED = "04";
        static final String CODE_DESIGNER = "05";
        static final String CODE_MEASURED = "06";
        static final String CODE_UPLOAD_PLAN = "07";
        static final String CODE_ROUNDS = "08";
        static final String CODE_CONTRACT = "09";
        static final String CODE_ORDER = "11";
        static final String CODE_LOSE = "12";
        static final String CODE_UNINSTALL = "15";
        static final String CODE_INSTALLED = "16";
        static final String CODE_INSTALL_DRAWING = "17";
        static final String CODE_MESSAGE_BOARD = "18";
        static final String CODE_RECEIPT = "19";
    }

    TextSpanHelper mTextHelper;
//    LayoutInflater mLayoutInflater;

    SpannableString transform(@StringRes int id, String content) {
        return transform(id, content, null);
    }

    SpannableString transform(@StringRes int id, String content, @Nullable TextView tipsView) {
        String origin = id == 0 ? "" : mActivity.getString(id);
        if (TextUtils.isEmpty(content)) {
            int color = R.color.textColor21;
            content = mActivity.getString(R.string.not_filled_in);
            if (tipsView != null) {
                tipsView.setTextColor(ContextCompat.getColor(mActivity, color));
            }
            return mTextHelper.generalTypefaceStyle(origin, content, color, Typeface.NORMAL);
        } else {
            if (tipsView != null) {
                tipsView.setTextColor(ContextCompat.getColor(mActivity, R.color.textColor8));
            }
            return mTextHelper.obtainColorBoldStyle(origin, content, R.color.textColor4);
        }
    }

    SpannableString transform2(@StringRes int id, String content) {
        String origin = id == 0 ? "" : mActivity.getString(id);
        if (TextUtils.isEmpty(content)) {
            content = mActivity.getString(R.string.never);
            return mTextHelper.generalTypefaceStyle(origin, content, R.color.textColor21, Typeface.NORMAL);
        } else {
            return mTextHelper.obtainColorBoldStyle(origin, content, R.color.textColor4);
        }
    }

    private static final String URL_SHOW_IMAGE = CustomerUrlPath.URL_SHOW_IMAGE;

    BaseActivity mActivity;

//    CustomerManagerV2Bean mManagerV2Bean;
    CustomerManagerV2Bean.HouseDetailBean mCurrentHouseDetailBean; //当前选中的房屋信息
    CustomerManagerV2Bean.HouseInfoBean mCurrentHouseInfoBean; //当前选中的房屋
    List<CustomerManagerV2Bean.HistoryBean> mCurrentHistoryList; //房屋历史记录
    /*操作记录代码，01分配导购，04预约量房，05分配设计师，06量房完成，07上传方案，08主管查房，
    09合同登记，11下单，12流失，15预约安装，16安装完成，17上传安装图纸，18留言板，19收款*/

    IHighSeasMultipleHelper(BaseActivity<?, ?> activity) {
        this.mActivity = activity;
        this.mTextHelper = TextSpanHelper.from(mActivity);
//        this.mLayoutInflater = LayoutInflater.from(mActivity);
    }

    class MultiHouseItem implements MultiItem {
        String operator;
        String operateTime;
        String remark;

        void setHistory(@Nullable CustomerManagerV2Bean.HistoryBean bean) {
            if (bean != null) {
                operator = bean.operator;
                operateTime = bean.getOperateTime();
                remark = bean.remark;
            }
        }

        static final int TYPE_OPERATE = 1; //操作菜单 recyclerView
//        static final int TYPE_TITLE_TIPS = 2;   //跟进记录 textView
        static final int TYPE_GUIDE = 3; //分配导购
        static final int TYPE_CALL_LOGS = 4; //通话记录
        static final int TYPE_UNMEASURED = 5; //预约量尺
        static final int TYPE_DESIGNER = 6;
        static final int TYPE_MEASURE_RESULT = 7; //量尺结果
        static final int TYPE_UPLOAD_PLAN = 8; //上传方案
        static final int TYPE_ROUNDS = 9; //主管查房
        static final int TYPE_RECEIPT_TITLE = 10;
        static final int TYPE_RECEIPT = 11; //收款
        static final int TYPE_CONTRACT_REGISTER = 12; //合同登记
        static final int TYPE_ORDER_TITLE = 13;
        static final int TYPE_ORDER = 14; //生成订单
        static final int TYPE_UNINSTALL = 15; //预约安装
        static final int TYPE_INSTALL_DRAWING = 16; //上传安装图纸
        static final int TYPE_INSTALLED_TITLE = 17;
        static final int TYPE_INSTALLED = 18; //安装完成
        static final int TYPE_MESSAGE_LOGS_TITLE = 19; //留言记录提示
        static final int TYPE_MESSAGE_LOGS = 20; //留言记录

        private int itemType;

        MultiHouseItem(int itemType) {
            this.itemType = itemType;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }

    class OperateItem extends MultiHouseItem {
        List<CustomerManagerV2Bean.OperateItemBean> beans;

        OperateItem(@NonNull List<CustomerManagerV2Bean.OperateItemBean> beans) {
            super(MultiHouseItem.TYPE_OPERATE);
            this.beans = beans;
        }
    }

    /*通话记录item*/
    class PhoneRecordItem extends MultiHouseItem {
        CustomerManagerV2Bean.PhoneRecordBean recordBean;

        PhoneRecordItem(CustomerManagerV2Bean.PhoneRecordBean bean) {
            super(MultiHouseItem.TYPE_CALL_LOGS);
            recordBean = bean;
        }
    }

    /*量尺结果item*/
    class MeasureResultItem extends MultiHouseItem {
        List<String> images;

        MeasureResultItem(List<CustomerManagerV2Bean.MeasureResultImgBean> imageList) {
            super(MultiHouseItem.TYPE_MEASURE_RESULT);
            setHistory(getMeasuredHistory());
            images = new ArrayList<>();
            for (CustomerManagerV2Bean.MeasureResultImgBean bean : imageList) {
                images.add(URL_SHOW_IMAGE + bean.resourceId);
            }
        }
    }

    /*方案图纸*/
    class UploadPlanItem extends MultiHouseItem {
        List<String> images;

        UploadPlanItem(List<CustomerManagerV2Bean.DesignRenderImgBean> list) {
            super(MultiHouseItem.TYPE_UPLOAD_PLAN);
            setHistory(getUploadPlanHistory());
            images = new ArrayList<>();
            for (CustomerManagerV2Bean.DesignRenderImgBean bean : list) {
                images.add(URL_SHOW_IMAGE + bean.resourceId);
            }
        }
    }

    /*收款item*/
    class ReceiptItem extends MultiHouseItem {
        CustomerManagerV2Bean.PaymentBean bean;
        String customProduct; //定制品类
        boolean isLastPosition;
        List<String> images = new ArrayList<>();

        ReceiptItem(CustomerManagerV2Bean.PaymentBean bean, boolean isLastPosition) {
            super(MultiHouseItem.TYPE_RECEIPT);
            this.bean = bean;
            if (TextUtils.isEmpty(bean.category)) customProduct = "";
            SysCodeItemBean systemCode = IntentValue.getInstance().getSystemCode();
            if (systemCode != null) {  //通过字典去匹配
                try {
                    StringBuilder sb = new StringBuilder();
                    String[] array = this.bean.category.split(",");
                    Map<String, String> typeMap = systemCode.getCustomerEarnestHouse();
                    for (int i = 0; i < array.length; i++) {
                        String value = typeMap.get(array[i]);
                        if (!TextUtils.isEmpty(value)) {
                            sb.append(value);
                            if (i < array.length - 1) {
                                sb.append("、");
                            }
                        }
                    }
                    customProduct = sb.toString();
                } catch (Exception e) {
                    customProduct = "";
                }
            }
            this.isLastPosition = isLastPosition;
            for (CustomerManagerV2Bean.GeneralImageBean imageBean : bean.getPaymentImg()) {
                this.images.add(CustomerUrlPath.URL_SHOW_IMAGE + imageBean.resourceId);
            }
        }
    }

    /*合同登记图片*/
    class ContractItem extends MultiHouseItem {
        List<String> images;

        ContractItem(List<CustomerManagerV2Bean.ContractImgBean> list) {
            super(MultiHouseItem.TYPE_CONTRACT_REGISTER);
            setHistory(getContractHistory());
            images = new ArrayList<>();
            for (CustomerManagerV2Bean.ContractImgBean bean : list) {
                images.add(URL_SHOW_IMAGE + bean.resourceId);
            }
        }
    }

    /*生成订单*/
    class OrderPlacedItem extends MultiHouseItem {
        CustomerManagerV2Bean.GenerateOrderBean bean;
        boolean isLastPosition;

        OrderPlacedItem(CustomerManagerV2Bean.GenerateOrderBean bean, boolean isLastPosition) {
            super(MultiHouseItem.TYPE_ORDER);
            this.bean = bean;
            this.isLastPosition = isLastPosition;
        }
    }

    /*预约安装*/
    class UninstallItem extends MultiHouseItem {
        String date;
        CharSequence area;
        String installUser;

        UninstallItem() {
            super(MultiHouseItem.TYPE_UNINSTALL);
            setHistory(getUninstallHistory());
            StringBuilder sb = new StringBuilder();
            List<CustomerManagerV2Bean.InstallUserBean> list = mCurrentHouseInfoBean.getInstallUserInfo();
            for (int i = 0; i < list.size(); i++) {
                CustomerManagerV2Bean.InstallUserBean bean = list.get(i);
                sb.append(bean.installUserName);
                if (i < list.size() - 1) {
                    sb.append("、");
                }
            }
            installUser = sb.toString();
            CustomerManagerV2Bean.InstallInfoBean bean = mCurrentHouseInfoBean.getInstallInfo();
            if (bean != null) {
                date = bean.getInstallDate() + " " + bean.getInstallTime();
            }
            String tips = mActivity.getString(R.string.followup_installation_area);
            SpannableString ss;
            int flags = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
            if (bean == null || TextUtils.isEmpty(bean.installSquare)) {
                String content = mActivity.getString(R.string.not_filled_in);
                String source = tips + content;
                ss = new SpannableString(source);
                ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.textColor21)), 0, source.length(), flags);
                ss.setSpan(new StyleSpan(Typeface.NORMAL), 0, source.length(), flags);
            } else {
                String source = tips + (TextUtils.isEmpty(bean.installSquare) ? "" : bean.installSquare + "m2");
                ss = new SpannableString(source);
                int start = source.indexOf("2");
                int end = source.indexOf("2") + 1;
                ss.setSpan(new RelativeSizeSpan(0.8f), start, end, flags);
                ss.setSpan(new SuperscriptSpan(), start, end, flags); //数学上标（平方）
                start = tips.length();
                end = source.length();
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.textColor4));
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(colorSpan, start, end, flags);
                ss.setSpan(styleSpan, start, end, flags);
            }
            area = ss;
        }
    }

    /*安装图纸item*/
    class InstallDrawingItem extends MultiHouseItem {
        List<String> images;

        InstallDrawingItem(List<CustomerManagerV2Bean.InstallImgBean> list) {
            super(MultiHouseItem.TYPE_INSTALL_DRAWING);
            setHistory(getInstallDrawingHistory());
            images = new ArrayList<>();
            for (CustomerManagerV2Bean.InstallImgBean imgBean : list) {
                images.add(URL_SHOW_IMAGE + imgBean.resourceId);
            }
        }
    }

    /*收款item*/
    class InstalledItem extends MultiHouseItem {
        CustomerManagerV2Bean.InstalledInfoBean bean;
        boolean isLastPosition;
        List<String> images = new ArrayList<>();

        InstalledItem(CustomerManagerV2Bean.InstalledInfoBean bean, boolean isLastPosition) {
            super(MultiHouseItem.TYPE_INSTALLED);
            this.bean = bean;
            this.isLastPosition = isLastPosition;
            for (CustomerManagerV2Bean.GeneralImageBean imageBean : bean.getInstallRenderImgList()) {
                this.images.add(CustomerUrlPath.URL_SHOW_IMAGE + imageBean.resourceId);
            }
        }
    }

    /*留言item*/
    class MessageLogsItem extends MultiHouseItem {
        boolean isEmpty;
        CustomerManagerV2Bean.MessageBoardBean bean;

        MessageLogsItem(boolean isEmpty) {
            super(MultiHouseItem.TYPE_MESSAGE_LOGS);
            this.isEmpty = isEmpty;
        }

        MessageLogsItem(CustomerManagerV2Bean.MessageBoardBean bean) {
            this(false);
            this.bean = bean;
        }
    }

    /*导购历史记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getGuideHistory() {
        return getHistoryByCode(OperateCode.CODE_GUIDE);
    }

    /*预约量房记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getUnmeasuredHistory() {
        return getHistoryByCode(OperateCode.CODE_UNMEASURED);
    }

    /*分配设计师记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getDesignerHistory() {
        return getHistoryByCode(OperateCode.CODE_DESIGNER);
    }

    /*量房完成记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getMeasuredHistory() {
        return getHistoryByCode(OperateCode.CODE_MEASURED);
    }

    /*上传方案记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getUploadPlanHistory() {
        return getHistoryByCode(OperateCode.CODE_UPLOAD_PLAN);
    }

    /*主管查房记录*/
    CustomerManagerV2Bean.HistoryBean getRoundsHistory() {
        return getHistoryByCode(OperateCode.CODE_ROUNDS);
    }

    /*收款记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getReceiptHistory() {
        return getHistoryByCode(OperateCode.CODE_RECEIPT);
    }

    /*合同登记记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getContractHistory() {
        return getHistoryByCode(OperateCode.CODE_CONTRACT);
    }

    /*订单记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getOrderHistory() {
        return getHistoryByCode(OperateCode.CODE_ORDER);
    }

    /*流失记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getLoseHistory() {
        return getHistoryByCode(OperateCode.CODE_LOSE);
    }

    /*导购历史记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getUninstallHistory() {
        return getHistoryByCode(OperateCode.CODE_UNINSTALL);
    }

    /*安装完成记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getInstalledHistory() {
        return getHistoryByCode(OperateCode.CODE_INSTALLED);
    }

    /*上传安装图纸记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getInstallDrawingHistory() {
        return getHistoryByCode(OperateCode.CODE_INSTALL_DRAWING);
    }

    /*留言板记录*/
    @Nullable
    CustomerManagerV2Bean.HistoryBean getMessageBoardHistory() {
        return getHistoryByCode(OperateCode.CODE_MESSAGE_BOARD);
    }

    @Nullable
    private CustomerManagerV2Bean.HistoryBean getHistoryByCode(String operateCode) {
        CustomerManagerV2Bean.HistoryBean bean = CustomerManagerV2Bean.HistoryBean.newInstance(operateCode);
        int index = mCurrentHistoryList.indexOf(bean);
        if (index >= 0) {
            return mCurrentHistoryList.get(index);
        }
        return null;
    }
}
