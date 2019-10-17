package com.holike.crm.base;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeDetailBean;
import com.holike.crm.bean.EmployeeDetailV2Bean;
import com.holike.crm.bean.EmployeeEditResultBean;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.http.MyJsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/7/11.
 * Copyright holike possess 2019.
 * 替换activity、fragment等大数据传值
 */
public class IntentValue {
    private volatile static IntentValue mInstance;
    private Map<String, Object> mValueMap;

    private static final String SYSTEM_CODE = "sysCode";
    private static final String CURRENT_USER_INFO = "current_user";
    private static final String UPDATE_BEAN = "updateBean";
    private static final String HOME_BEAN = "homeBean";

    private static final String EMPLOYEE_EDIT_RESULT = "employee_edit_result";

    private IntentValue() {
        mValueMap = new HashMap<>();
    }

    public static IntentValue getInstance() {
        if (mInstance == null) {
            synchronized (IntentValue.class) {
                if (mInstance == null) {
                    mInstance = new IntentValue();
                }
            }
        }
        return mInstance;
    }

    /*首页数据保存到缓存中，提供其他页面调用*/
    public void setHomeBean(HomepageBean bean) {
        mValueMap.put(HOME_BEAN, bean);
    }

    @Nullable
    public HomepageBean getHomeBean() {
        Object object = mValueMap.get(HOME_BEAN);
        if (object == null) return null;
        if (object instanceof HomepageBean) {
            return (HomepageBean) object;
        }
        return null;
    }

    public void setUpdateBean(UpdateBean bean) {
        mValueMap.put(UPDATE_BEAN, bean);
    }

    @Nullable
    public UpdateBean getUpdateBean() {
        Object object = mValueMap.get(UPDATE_BEAN);
        if (object == null) return null;
        if (object instanceof UpdateBean) {
            return (UpdateBean) object;
        }
        return null;
    }

    /*全局保存业务字典*/
    public void setSystemCode(SysCodeItemBean bean) {
        mValueMap.put(SYSTEM_CODE, bean);
    }

    @Nullable
    public SysCodeItemBean getSystemCode() {
        Object object = mValueMap.get(SYSTEM_CODE);
        if (object == null) return null;
        if (object instanceof SysCodeItemBean) {
            return (SysCodeItemBean) object;
        }
        return null;
    }

    public void setCurrentUserInfo(CurrentUserBean bean) {
        mValueMap.put(CURRENT_USER_INFO, bean);
    }

    @Nullable
    public CurrentUserBean getCurrentUser() {
        Object object = mValueMap.get(CURRENT_USER_INFO);
        if (object == null) return null;
        if (object instanceof CurrentUserBean) {
            return (CurrentUserBean) object;
        }
        return null;
    }

    /*门店数据*/
    @NonNull
    public List<DistributionStoreBean> getShopData() {
        HomepageBean bean = getHomeBean();
        if (bean == null || bean.getTypeList() == null || bean.getTypeList().getShopData().isEmpty()) {
            return new ArrayList<>();
        }
        return bean.getTypeList().getShopData();
    }

    /*角色数据*/
    @NonNull
    public List<RoleDataBean> getRoleData() {
        HomepageBean bean = getHomeBean();
        if (bean == null || bean.getTypeList() == null || bean.getTypeList().getRoleData().isEmpty()) {
            return new ArrayList<>();
        }
        return bean.getTypeList().getRoleData();
    }

    /*权限数据*/
    public List<RoleDataBean.AuthInfoBean> getEmployeeAuthInfo() {
        HomepageBean bean = getHomeBean();
        if (bean == null || bean.getAuthInfo() == null) {
            return new ArrayList<>();
        }
        return bean.getAuthInfo();
    }

    @Nullable
    public List<HomepageBean.TypeListBean.BrankDataBean> getHomeBankData() {
        HomepageBean bean = getHomeBean();
        if (bean == null || bean.getTypeList() == null || bean.getTypeList().getBrankData() == null)
            return null;
        return bean.getTypeList().getBrankData();
    }

    @Nullable
    public List<HomepageBean.QuestionTypeDataBean> getHomeQuestionData() {
        HomepageBean bean = getHomeBean();
        if (bean == null || bean.getQuestionTypeData() == null) return null;
        return bean.getQuestionTypeData();
    }

    /*员工信息保存成功后*/
    public void setEmployeeEditResult(@Nullable EmployeeEditResultBean bean) {
        put(EMPLOYEE_EDIT_RESULT, bean);
    }

    @Nullable
    public EmployeeEditResultBean getEmployeeEditResult() {
        Object obj = get(EMPLOYEE_EDIT_RESULT);
        if (obj == null) return null;
        return (EmployeeEditResultBean) obj;
    }

    public void removeEmployeeEditResult() {
        remove(EMPLOYEE_EDIT_RESULT);
    }

    public void put(String key, String value) {
        mValueMap.put(key, value);
    }

    public void put(String key, Object object) {
        mValueMap.put(key, object);
    }

    public void putAsJson(String key, Object object) {
        mValueMap.put(key, MyJsonParser.fromBeanToJson(object));
    }

    @Nullable
    public Object get(String key) {
        return mValueMap.get(key);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getAs(String key, T t) {
        Object o = mValueMap.get(key);
        if (o == null) return null;
        return (T) o;
    }

    @Nullable
    public <T> T get(String key, Class<T> clazz) {
        if (get(key) instanceof String) {
            String json = (String) get(key);
            return MyJsonParser.fromJsonToBean(json, clazz);
        }
        return null;
    }

    @Nullable
    public <T> List<T> getAsList(String key, Class<T> clazz) {
        if (get(key) instanceof String) {
            String json = (String) get(key);
            return MyJsonParser.fromJsonToList(json, clazz);
        }
        return null;
    }

    public void remove(String key) {
        mValueMap.remove(key);
    }

    public Object removeBy(String key){
        return mValueMap.remove(key);
    }
}
