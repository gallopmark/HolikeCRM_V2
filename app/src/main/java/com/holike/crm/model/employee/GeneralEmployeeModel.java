package com.holike.crm.model.employee;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.EmployeeBeanV2;
import com.holike.crm.bean.EmployeeDetailV2Bean;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 */
public class GeneralEmployeeModel extends BaseModel {


    /*获取员工列表*/
    public void getEmployeeList(String content, String shopId, String status, String roleCode, RequestCallBack<List<EmployeeBeanV2>> callBack) {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        Map<String, String> params = ParamHelper.Employee.employeeList(content, shopId, status, roleCode);
        postByTimeout(UrlPath.URL_EMPLOYEE_LIST, header, params, 60, callBack);
    }

    /*获取员工信息*/
    public void getEmployeeInfo(String userId, RequestCallBack<EmployeeDetailV2Bean> callBack) {
        postByTimeout(UrlPath.URL_EMPLOYEE_DATA, ParamHelper.Employee.employeeInfo(userId), 60, callBack);
    }

    /*新增或修改员工*/
    public void saveEmployee(Map<String, String> params, RequestCallBack<String> callBack) {
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        post(UrlPath.URL_EMPLOYEE_EDIT, ParamHelper.nonNullWrap(params), callBack);
    }
}
