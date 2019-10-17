package com.holike.crm.bean.internal;

import android.text.TextUtils;

import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.SysCodeItemBean;

import java.util.Map;

/*字典匹配工具类*/
public class SystemCodePattern {

    /*当前房屋状态*/
    public static String getHouseStatus(String statusCode) {
        if (TextUtils.isEmpty(statusCode)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getCustomerStatusMove().containsKey(statusCode))
            return "";
        return bean.getCustomerStatusMove().get(statusCode);
    }

    /*定制预算*/
    public static String getCustomBudget(String budgetTypeCode) {
        if (TextUtils.isEmpty(budgetTypeCode)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getCustomerBudgetType().containsKey(budgetTypeCode))
            return "";
        return bean.getCustomerBudgetType().get(budgetTypeCode);
    }

    /*装修进度*/
    public static String getDecorateProgress(String decorateProgress) {
        if (TextUtils.isEmpty(decorateProgress)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getDecorateProgress().containsKey(decorateProgress))
            return "";
        return bean.getDecorateProgress().get(decorateProgress);
    }

    /*房屋状态-(精装房、毛坯房、自主新增等)*/
    public static String getDecorateProperties(String decorateProperties) {
        if (TextUtils.isEmpty(decorateProperties)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getHouseStatus().containsKey(decorateProperties))
            return "";
        return bean.getHouseStatus().get(decorateProperties);
    }

    /*查房结果*/
    public static String getMeasureResult(String validatePassed) {
        if (TextUtils.isEmpty(validatePassed)) {
            return "";
        }
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getReviewHousePlan().containsKey(validatePassed)) return "";
        return bean.getReviewHousePlan().get(validatePassed);
    }

    /*定制空间*/
    public static String getCustomSpace(String customizeTheSpace) {
        if (TextUtils.isEmpty(customizeTheSpace)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) return "";
        try {
            String[] array = customizeTheSpace.split(",");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                String value = bean.getCustomerMeasureSpace().get(array[i]);
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

    /*量尺空间*/
    public static String getAppointMeasureSpace(String appointMeasureSpace) {
        if (TextUtils.isEmpty(appointMeasureSpace)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) {
            return "";
        }
        try {
            String[] array = appointMeasureSpace.split(",");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                String value = bean.getCustomerMeasureSpace().get(array[i]);
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

    /*每平方米房价*/
    public static String getHousePrice(String housePriceTypeCode) {
        if (TextUtils.isEmpty(housePriceTypeCode)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getHousePriceType().containsKey(housePriceTypeCode))
            return "";
        return bean.getHousePriceType().get(housePriceTypeCode);
    }

    /*量尺沟通预算*/
    public static String getMeasureBudget(String measureBudgetTypeCode) {
        if (TextUtils.isEmpty(measureBudgetTypeCode)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getCustomerBudgetType().containsKey(measureBudgetTypeCode))
            return "";
        return bean.getCustomerBudgetType().get(measureBudgetTypeCode);
    }

    /*户型*/
    public static String getHouseType(String houseType) {
        if (TextUtils.isEmpty(houseType)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getCustomerHouseType().containsKey(houseType))
            return "";
        return bean.getCustomerHouseType().get(houseType);
    }

    /*装修风格*/
    public static String getPreferenceStyle(String preferenceStyle) {
        if (TextUtils.isEmpty(preferenceStyle)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getDecorationStyle().containsKey(preferenceStyle))
            return "";
        return bean.getDecorationStyle().get(preferenceStyle);
    }

    /*家庭成员*/
    public static String getFamilyMember(String familyMemberCode) {
        if (TextUtils.isEmpty(familyMemberCode)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) return "";
        try {
            String[] array = familyMemberCode.split(",");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (bean.getFamilyMember().containsKey(array[i])) {
                    String value = bean.getFamilyMember().get(array[i]);
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

    /*家具需求*/
    public static String getFurnitureDemand(String furnitureDemand) {
        if (TextUtils.isEmpty(furnitureDemand)) return "";
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) return "";
        try {
            String[] array = furnitureDemand.split(",");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                String value = bean.getFurnitureDemand().get(array[i]);
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

    /*房屋面积*/
    public static String getHouseArea(String areaType) {
        if (TextUtils.isEmpty(areaType)) {
            return "";
        }
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) return "";
        return bean.getCustomerAreaType().get(areaType);
    }


    /*产品*/
    public static String getProduct(String product) {
        if (TextUtils.isEmpty(product)) {
            return "";
        }
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getCustomerProduct().containsKey(product)) return "";
        return bean.getCustomerProduct().get(product);
    }


    /*系列*/
    public static String getSeries(String product, String series) {
        if (TextUtils.isEmpty(series)) {
            return "";
        }
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null) return "";
        Map<String, String> seriesMap = null;
        Map<String, String> productMap = bean.getCustomerProduct();
        String productName = productMap.get(product);
        if (TextUtils.equals(product, "WHOLE_HOUSE_PRODUCT") || TextUtils.equals(productName, "全屋定制产品")) {  //全屋定制系列
            seriesMap = bean.getHouseSeries();
        } else if (TextUtils.equals(product, "CUPBOARD_PRODUCT") || TextUtils.equals(productName, "橱柜产品")) { //橱柜系列
            seriesMap = bean.getCupboardSeries();
        } else if (TextUtils.equals(product, "DOOR_PRODUCT") || TextUtils.equals(productName, "木门产品")) { //木门系列
            seriesMap = bean.getDoorSeries();
        }
        if (seriesMap != null) {
            return seriesMap.get(series);
        }
        return "";
    }

    /*风格*/
    public static String getStyle(String style) {
        if (TextUtils.isEmpty(style)) {
            return "";
        }
        SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
        if (bean == null || !bean.getDecorationStyle().containsKey(style)) return "";
        return bean.getDecorationStyle().get(style);
    }

    /*定制品类*/
    public static String getCustomClass(String customClass) {
        if (TextUtils.isEmpty(customClass)) return "";
        SysCodeItemBean systemCode = IntentValue.getInstance().getSystemCode();
        if (systemCode == null) return "";
        try {
            StringBuilder sb = new StringBuilder();
            String[] array = customClass.split(",");
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
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
