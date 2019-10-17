package com.holike.crm.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.holike.crm.bean.DepositBean;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.bean.SourceBean;
import com.holike.crm.http.MyJsonParser;

import java.util.List;

/*保存首页数据*/
@Deprecated
public class MainDataSource {

    private static final String FILE_LOCAL = "mainData";
    /*店铺数据*/
    private static final String SHOP_DATA = "shopData";
    /*客户来源*/
    private static final String SOURCE_DATA = "sourceData";
    /*定制预算*/
    private static final String DEPOSIT_DATA = "depositData";

    /*权限信息*/
    private static final String AUTH_INFO = "authInfo";

    public static void clear(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(FILE_LOCAL, Context.MODE_PRIVATE);
    }

    public static void saveHomepageBean(Context context, HomepageBean bean) {
        HomepageBean.TypeListBean listBean = bean.getTypeList();
        if (listBean != null) {
            /*店铺数据*/
            listBean.getShopData();
            if (!listBean.getShopData().isEmpty()) {
                saveShopData(context, MyJsonParser.fromBeanToJson(listBean.getShopData()));
            }
            /*客户来源*/
            if (listBean.sourceData != null && !listBean.sourceData.isEmpty()) {
                saveSourceData(context, MyJsonParser.fromBeanToJson(listBean.sourceData));
            }
            /*定制预算*/
            if (listBean.depositData != null && !listBean.depositData.isEmpty()) {
                saveDepositData(context, MyJsonParser.fromBeanToJson(listBean.depositData));
            }
        }
        /*员工权限信息*/
        List<RoleDataBean.AuthInfoBean> authInfoBeans = bean.getAuthInfo();
        if (authInfoBeans != null && !authInfoBeans.isEmpty()) {
            saveAuthInfo(context, authInfoBeans);
        }
    }

    public static void saveShopData(Context context, String shopData) {
        getSharedPreferences(context).edit().putString(SHOP_DATA, shopData).apply();
    }

    public static void clearShopData(Context context) {
        getSharedPreferences(context).edit().remove(SHOP_DATA).apply();
    }

    public static List<DistributionStoreBean> getShopData(Context context) {
        String json = getSharedPreferences(context).getString(SHOP_DATA, "");
        return MyJsonParser.fromJsonToList(json, DistributionStoreBean.class);
    }

    public static void saveSourceData(Context context, String sourceData) {
        getSharedPreferences(context).edit().putString(SOURCE_DATA, sourceData).apply();
    }

    public static List<SourceBean> getSourceData(Context context) {
        String json = getSharedPreferences(context).getString(SOURCE_DATA, "");
        return MyJsonParser.fromJsonToList(json, SourceBean.class);
    }

    public static void saveDepositData(Context context, String depositData) {
        getSharedPreferences(context).edit().putString(DEPOSIT_DATA, depositData).apply();
    }

    public static List<DepositBean> getDepositData(Context context) {
        String json = getSharedPreferences(context).getString(DEPOSIT_DATA, "");
        return MyJsonParser.fromJsonToList(json, DepositBean.class);
    }

    public static void saveAuthInfo(Context context, List<RoleDataBean.AuthInfoBean> infoBeans) {
        for (RoleDataBean.AuthInfoBean bean : infoBeans) {
            if (bean.getAuthData() != null && !bean.getAuthData().isEmpty()) {
                for (RoleDataBean.AuthInfoBean.PArrBean arrBean : bean.getAuthData()) {
                    arrBean.setIsSelect(0);
                }
            }
        }
        String authInfo = MyJsonParser.fromBeanToJson(infoBeans);
        getSharedPreferences(context).edit().putString(AUTH_INFO, authInfo).apply();
    }

    public static void clearAuthInfo(Context context) {
        getSharedPreferences(context).edit().remove(AUTH_INFO).apply();
    }

    public static List<RoleDataBean.AuthInfoBean> getAuthInfo(Context context) {
        String json = getSharedPreferences(context).getString(AUTH_INFO, "");
        if (TextUtils.isEmpty(json)) return null;
        return MyJsonParser.fromJsonToList(json, RoleDataBean.AuthInfoBean.class);
    }
}
