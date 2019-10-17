package com.holike.crm.http;


import androidx.annotation.Nullable;

import com.holike.crm.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/8/1.
 * Copyright holike possess 2019.
 */
public class ParamHelper {

    public static Map<String, String> general() {
        return new HashMap<>();
    }

    public static Map<String, Object> objectMap() {
        return new HashMap<>();
    }

    /*传参value不能为空 否则请求报错*/
    public static Map<String, String> nonNullWrap(Map<String, String> params) {
        Map<String, String> map = general();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            map.put(entry.getKey(), noNullWrap(entry.getValue()));
        }
        return map;
    }

    public static String noNullWrap(String content) {
        if (content == null) return "";
        return content;
    }

    /*参数转json body*/
    public static String toBody(Map<String, String> params) {
        return MyJsonParser.fromBeanToJson(params);
    }

    /*客户管理 请求参数*/
    public static class Customer {

        /*客户列表参数*/
        public static Map<String, String> customerList(String content, String customerStatusId, String customerTypeId,
                                                       String orderby, String startTime, String endTime,
                                                       int pageNo, int pageSize) {
            Map<String, String> params = general();
            params.put("content", noNullWrap(content));
            params.put("customerStatusId", noNullWrap(customerStatusId));
            params.put("customerTypeId", noNullWrap(customerTypeId));
            params.put("orderby", noNullWrap(orderby));
            params.put("startTime", noNullWrap(startTime));
            params.put("endTime", noNullWrap(endTime));
            params.put("pageNo", String.valueOf(pageNo));
            params.put("pageSize", String.valueOf(pageSize));
            return params;
        }

        /**
         * 客户状态列表
         *
         * @param statusMove      01 潜在客户 02待量尺 3待出图 14待查房 04待签约 09待收全款
         * @param earnestStatus   1有收取定金 0 无收取定金
         * @param intentionStatus 客户意向(A级，B级,C级)
         * @param customerStatus  客户状态 多个用@隔开
         * @param tailStatus      尾款情况
         * @param seaStatus       公海分类
         * @param orderBy         desc 升序 asc 降序
         * @param pageNo          页码
         * @param pageSize        页面大小
         * @return map
         */
        public static Map<String, String> customerStatusList(String statusMove, String earnestStatus, String intentionStatus,
                                                             String customerStatus, String tailStatus, String seaStatus,
                                                             String orderBy, int pageNo, int pageSize) {
            Map<String, String> params = general();
            params.put("statusMove", noNullWrap(statusMove));
            params.put("earnestStstus", noNullWrap(earnestStatus));
            params.put("intentionStstus", noNullWrap(intentionStatus));
            params.put("customerStatus", noNullWrap(customerStatus));
            params.put("tailStatus", noNullWrap(tailStatus));
            params.put("seaStatus", noNullWrap(seaStatus));
            params.put("orderby", noNullWrap(orderBy));
            params.put("pageNo", String.valueOf(pageNo));
            params.put("pageSize", String.valueOf(pageSize));
            return params;
        }

        /*客户管理-添加通话记录*/
        public static String savePhoneRecord(String personalId, String houseId, String dialPersonId,
                                             String dialPerson, String talkTime, String phoneNumber) {
            Map<String, String> params = general();
            params.put("personalId", noNullWrap(personalId));
            params.put("houseId", noNullWrap(houseId));
            params.put("dailPersonId", noNullWrap(dialPersonId));
            params.put("dailPerson", noNullWrap(dialPerson));
            params.put("talkTime", noNullWrap(talkTime));
            params.put("phoneNumber", noNullWrap(phoneNumber));
            return toBody(params);
        }

        /**
         * 分配导购
         *
         * @param houseId        房屋id
         * @param shopId         导购门店id
         * @param groupId        导购分组id(导购存在分组时传入)
         * @param guideId        导购id
         * @param promoterShopId 业务员门店id
         * @param promoterId     业务员id
         */
        public static String assignGuide(String houseId, String shopId, String groupId,
                                         String guideId, String promoterShopId, String promoterId) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("shopId", noNullWrap(shopId));
            params.put("groupId", noNullWrap(groupId));
            params.put("guideId", noNullWrap(guideId));
            params.put("promoterShopId", promoterShopId);
            params.put("promoterId", noNullWrap(promoterId));
            return toBody(params);
        }

        //customermanager/api/customerHouseInfo/assignDesigner?houseId=60d336e48a434627ac6db477bb9bb528&designerShopId=201700001&designerId=13109100-0002
        /*分配设计师传参*/
        public static String assignDesigner(String houseId, String designerShopId, String designerId) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("designerShopId", noNullWrap(designerShopId));
            params.put("designerId", noNullWrap(designerId));
            return toBody(params);
        }

        /*预约量尺*/
        public static String appointMeasure(String houseId, String appointTime, String appointMeasureSpace,
                                            String appointShopId, String appointMeasureBy, String remark) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("appointTime", noNullWrap(appointTime));
            params.put("appointMeasureSpace", noNullWrap(appointMeasureSpace));
            params.put("appointShopId", noNullWrap(appointShopId));
            params.put("appointMeasureBy", noNullWrap(appointMeasureBy));
            params.put("remark", noNullWrap(remark));
            return toBody(params);
        }

        /**
         * 客户管理-收取订金，尾款，退款
         *
         * @param personalId 客户id
         * @param houseId    房屋id
         * @param shopId     门店id
         * @param type       收款类别
         * @param payTime    收款时间
         * @param receiverId 收款人 id
         * @param amount     金额
         * @param category   定制品类
         * @param remark     备注
         */
        public static Map<String, Object> savePayment(String personalId, String houseId, String shopId,
                                                      String type, String payTime, String receiverId, String receiver,
                                                      String amount, String category, String remark) {
            Map<String, Object> params = objectMap();
            params.put("dealerId", SharedPreferencesUtils.getDealerId());
            params.put("personalId", noNullWrap(personalId));
            params.put("houseId", noNullWrap(houseId));
            params.put("shopId", noNullWrap(shopId));
            params.put("type", noNullWrap(type));
            params.put("payTime", noNullWrap(payTime));
            params.put("receiverId", noNullWrap(receiverId));
            params.put("receiver", noNullWrap(receiver));
            params.put("amount", noNullWrap(amount));
            params.put("category", noNullWrap(category));
            params.put("remark", noNullWrap(remark));
            return params;
        }

        /**
         * 合同登记
         *
         * @param houseId         房屋id
         * @param contractDate    签约日期(必填
         * @param salesAmount     成交金额(必填)
         * @param amount          收款金额
         * @param lastRemaining   剩余尾款(欠款)-(必填)
         * @param appDeliveryDate 预定交货日期(必填)
         * @param contractor      , 签约人(必填)
         * @param signName        签约人名字
         * @param remark          备注
         */
        public static Map<String, Object> contractRegister(String houseId, String contractDate, String salesAmount,
                                                           String amount, String lastRemaining, String appDeliveryDate,
                                                           String contractor, String signName, String remark) {
            Map<String, Object> params = objectMap();
            params.put("houseId", noNullWrap(houseId));
            params.put("contractDate", noNullWrap(contractDate));
            params.put("salesAmount", noNullWrap(salesAmount));
            params.put("amount", noNullWrap(amount));
            params.put("lastRemaining", noNullWrap(lastRemaining));
            params.put("appDeliveryDate", noNullWrap(appDeliveryDate));
            params.put("contractor", noNullWrap(contractor));
            params.put("signName", noNullWrap(signName));
            params.put("remark", noNullWrap(remark));
            return params;
        }

        /*发布留言*/
        public static String publishMessage(String houseId, String message) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("message", noNullWrap(message));
            return toBody(params);
        }

        /*新建客户输入地址检查*/
        public static String checkAddress(String provinceCode, String cityCode, String districtCode, String address) {
            Map<String, String> params = general();
            params.put("dealerId", noNullWrap(SharedPreferencesUtils.getDealerId()));
            params.put("provinceCode", noNullWrap(provinceCode));
            params.put("cityCode", noNullWrap(cityCode));
            params.put("districtCode", noNullWrap(districtCode));
            params.put("address", noNullWrap(address));
            return toBody(params);
        }

        /*新建客户输入手机号检测*/
        public static String checkPhone(String phone) {
            Map<String, String> params = general();
            params.put("dealerId", SharedPreferencesUtils.getDealerId());
            params.put("phone", noNullWrap(phone));
            return toBody(params);
        }

        /*新建客户输入手机号、微信号检查是否存在*/
        public static String checkWechat(String wxNumber) {
            Map<String, String> params = general();
            params.put("dealerId", SharedPreferencesUtils.getDealerId());
            params.put("wxNumber", noNullWrap(wxNumber));
            return toBody(params);
        }

        /**
         * 创建客户
         *
         * @param userName          客户名字
         * @param phoneNumber       手机号码
         * @param wxNumber          微信号
         * @param gender            性别
         * @param ageType           年龄段
         * @param source            来源
         * @param intentionLevel    意向评级
         * @param nextFollowTime    下次跟进时间
         * @param activityPolicy    活动政策
         * @param provinceCode      省编码
         * @param cityCode          城市编码
         * @param districtCode      区县编码
         * @param address           地址
         * @param shopId            门店id
         * @param budgetTypeCode    定制预算
         * @param spareContact      备用联系人
         * @param spareContactPhone 备用手机号
         * @param remark            备注信息
         */
        public static String createCustomer(String userName, String phoneNumber, String wxNumber,
                                            String gender, String ageType, String source, String intentionLevel,
                                            String nextFollowTime, String activityPolicy, String provinceCode,
                                            String cityCode, String districtCode, String address,
                                            String shopId, String groupId, String budgetTypeCode,
                                            String spareContact, String spareContactPhone, String remark) {
            Map<String, String> params = general();
            params.put("dealerId", noNullWrap(SharedPreferencesUtils.getDealerId()));
            params.put("userName", noNullWrap(userName));
            params.put("phoneNumber", phoneNumber);
            params.put("wxNumber", wxNumber);
            params.put("gender", noNullWrap(gender));
            params.put("ageType", noNullWrap(ageType));
            params.put("source", noNullWrap(source));
            params.put("intentionLevel", noNullWrap(intentionLevel));
            params.put("nextFollowTime", noNullWrap(nextFollowTime));
            params.put("activityPolicy", noNullWrap(activityPolicy));
            params.put("provinceCode", noNullWrap(provinceCode));
            params.put("cityCode", noNullWrap(cityCode));
            params.put("districtCode", noNullWrap(districtCode));
            params.put("address", noNullWrap(address));
            params.put("shopId", noNullWrap(shopId));
            params.put("groupId", noNullWrap(groupId));
            params.put("budgetTypeCode", noNullWrap(budgetTypeCode));
            params.put("spareContact", noNullWrap(spareContact));
            params.put("spareContactPhone", noNullWrap(spareContactPhone));
            params.put("remark", noNullWrap(remark));
            return toBody(params);
        }

        /**
         * 修改客户信息
         *
         * @param personalId     客户id
         * @param recordStatus   客户状态
         * @param versionNumber  版本
         * @param userName       客户姓名
         * @param phoneNumber    手机号
         * @param wxNumber       微信号
         * @param gender         性别
         * @param ageType        年龄段
         * @param source         来源
         * @param intentionLevel 意向评级
         * @param nextFollowTime 下次跟进时间
         * @param activityPolicy 活动政策
         */
        public static String alterCustomer(String personalId, String recordStatus, String versionNumber,
                                           String userName, String phoneNumber, String wxNumber,
                                           String gender, String ageType, String source, String intentionLevel,
                                           String nextFollowTime, String activityPolicy) {
            Map<String, String> params = general();
            params.put("dealerId", noNullWrap(SharedPreferencesUtils.getDealerId()));
            params.put("personalId", noNullWrap(personalId));
            params.put("recordStatus", noNullWrap(recordStatus));
            params.put("versionNumber", noNullWrap(versionNumber));
            params.put("userName", noNullWrap(userName));
            params.put("phoneNumber", noNullWrap(phoneNumber));
            params.put("wxNumber", noNullWrap(wxNumber));
            params.put("gender", noNullWrap(gender));
            params.put("ageType", noNullWrap(ageType));
            params.put("source", noNullWrap(source));
            params.put("intentionLevel", noNullWrap(intentionLevel));
            params.put("nextFollowTime", noNullWrap(nextFollowTime));
            params.put("activityPolicy", noNullWrap(activityPolicy));
            return toBody(params);
        }

        /**
         * 新增房屋信息
         *
         * @param personalId        客户id(必填)
         * @param provinceCode      省份代码(必填)
         * @param cityCode          城市(必填)
         * @param districtCode      区县代码(必填)
         * @param address           地址(必填)
         * @param shopId            门店id(必填)
         * @param groupId           组织id
         * @param budgetTypeCode    定制预算
         * @param spareContact      备用联系人
         * @param spareContactPhone 备用联系电话
         * @param remark            备注信息
         * @return body
         */
        public static String addHouseInfo(String personalId, String provinceCode, String cityCode, String districtCode,
                                          String address, String shopId,String groupId, String budgetTypeCode, String spareContact,
                                          String spareContactPhone, String remark) {
            Map<String, String> params = general();
            params.put("dealerId", noNullWrap(SharedPreferencesUtils.getDealerId()));
            params.put("personalId", noNullWrap(personalId));
            params.put("provinceCode", noNullWrap(provinceCode));
            params.put("cityCode", noNullWrap(cityCode));
            params.put("districtCode", noNullWrap(districtCode));
            params.put("address", noNullWrap(address));
            params.put("shopId", noNullWrap(shopId));
            params.put("groupId",noNullWrap(groupId));
            params.put("budgetTypeCode", noNullWrap(budgetTypeCode));
            params.put("spareContact", noNullWrap(spareContact));
            params.put("spareContactPhone", noNullWrap(spareContactPhone));
            params.put("remark", noNullWrap(remark));
            return toBody(params);
        }

        /**
         * 修改房屋信息
         *
         * @param personalId        客户id(必填)
         * @param houseId           房屋id
         * @param recordStatus      recordStatus
         * @param versionNumber     version
         * @param provinceCode      省份代码(必填)
         * @param cityCode          城市(必填)
         * @param districtCode      区县代码(必填)
         * @param address           地址(必填)
         * @param shopId            门店id(必填)
         * @param groupId           组织id
         * @param budgetTypeCode    定制预算
         * @param spareContact      备用联系人
         * @param spareContactPhone 备用联系电话
         * @param remark            备注信息
         * @return body
         */
        public static String updateHouseInfo(String personalId, String houseId, String recordStatus, String versionNumber,
                                             String provinceCode, String cityCode, String districtCode, String address,
                                             String shopId, String groupId, String budgetTypeCode, String spareContact,
                                             String spareContactPhone, String remark) {
            Map<String, String> params = general();
            params.put("dealerId", noNullWrap(SharedPreferencesUtils.getDealerId()));
            params.put("personalId", noNullWrap(personalId));
            params.put("houseId", noNullWrap(houseId));
            params.put("recordStatus", noNullWrap(recordStatus));
            params.put("versionNumber", noNullWrap(versionNumber));
            params.put("provinceCode", noNullWrap(provinceCode));
            params.put("cityCode", noNullWrap(cityCode));
            params.put("districtCode", noNullWrap(districtCode));
            params.put("address", noNullWrap(address));
            params.put("shopId", noNullWrap(shopId));
            params.put("groupId", noNullWrap(groupId));
            params.put("budgetTypeCode", noNullWrap(budgetTypeCode));
            params.put("spareContact", noNullWrap(spareContact));
            params.put("spareContactPhone", noNullWrap(spareContactPhone));
            params.put("remark", noNullWrap(remark));
            return toBody(params);
        }

        /**
         * 上传方案记录
         *
         * @param removedImages 被删除的图片集合
         * @param houseId       房屋id
         * @param bookOrderDate 预约确图日期
         * @param product       产品
         * @param series        系列
         * @param style         风格
         * @param remark        备注信息
         */
        public static Map<String, Object> uploadPlan(@Nullable List<String> removedImages, String houseId, String bookOrderDate, String product,
                                                     String series, String style, String remark) {
            Map<String, Object> params = new HashMap<>();
            params.put("houseId", noNullWrap(houseId));
            params.put("bookOrderDate", noNullWrap(bookOrderDate));
            params.put("product", noNullWrap(product));
            params.put("series", noNullWrap(series));
            params.put("style", noNullWrap(style));
            params.put("remark", noNullWrap(remark));
            params.put("deleteSchemeId", removedImages == null ? new ArrayList<>() : removedImages);
            return params;
        }

        public static Map<String, Object> uploadInstallDrawing(List<String> removedImages, final String houseId, final String installId,
                                                               final String remark) {
            final Map<String, Object> params = objectMap();
            params.put("houseId", noNullWrap(houseId));
            params.put("installId", noNullWrap(installId));
            params.put("remark", noNullWrap(remark));
            params.put("deleteInstallImgId", removedImages == null ? new ArrayList<>() : removedImages);
            return params;
        }

        /**
         * 删除量尺，方案图片记录
         *
         * @param schemeImgId 图片id
         */
        public static String deleteSchemeImg(String schemeImgId) {
            List<String> images = new ArrayList<>();
            images.add(schemeImgId);
            return deleteSchemeImg(images);
        }

        public static String deleteSchemeImg(List<String> images) {
            return MyJsonParser.fromBeanToJson(images);
        }

        /**
         * 客户管理-量尺结果保存
         *
         * @param houseId               房屋id
         * @param areaType              房屋面积
         * @param measureBudgetTypeCode 量尺沟通预算
         * @param customizeTheSpace     定制空间
         * @param furnitureDemand       家具需求
         * @param amountOfDate          量房完成时间
         * @param measureBy             量房人员
         * @param houseType             户型
         * @param familyMember          家庭成员
         * @param housePriceTypeCode    每平方米房价
         * @param preferenceStyle       装修风格
         * @param decorateProperties    房屋状态
         * @param decorateProgress      装修进度
         * @param plannedStayDate       计划入住日期
         * @param measureAppConfirmTime 预约确图日期
         * @param remark                备注信息
         */
        public static Map<String, Object> finishMeasure(List<String> removeImages, String houseId, String areaType, String measureBudgetTypeCode,
                                                        String customizeTheSpace, String furnitureDemand, String amountOfDate,
                                                        String measureShopId, String measureBy, String houseType,
                                                        String familyMember, String housePriceTypeCode, String preferenceStyle,
                                                        String decorateProperties, String decorateProgress, String plannedStayDate,
                                                        String measureAppConfirmTime, String remark) {
            Map<String, Object> params = objectMap();
            params.put("houseId", noNullWrap(houseId));
            params.put("areaType", noNullWrap(areaType));
            params.put("houseType", noNullWrap(houseType));
            params.put("flamilyMember", noNullWrap(familyMember));
            params.put("housePriceTypeCode", noNullWrap(housePriceTypeCode));
            params.put("measureBudgetTypeCode", noNullWrap(measureBudgetTypeCode));
            params.put("customizeTheSpace", noNullWrap(customizeTheSpace));
            params.put("furnitureDemand", noNullWrap(furnitureDemand));
            params.put("preferenceStyle", noNullWrap(preferenceStyle));
            params.put("decorateProperties", noNullWrap(decorateProperties));
            params.put("decorateProgress", noNullWrap(decorateProgress));
            params.put("plannedStayDate", noNullWrap(plannedStayDate));
            params.put("amountOfDate", noNullWrap(amountOfDate));
            params.put("measureShopId", noNullWrap(measureShopId));
            params.put("measureBy", noNullWrap(measureBy));
            params.put("measureAppComfirmTime", noNullWrap(measureAppConfirmTime));
            params.put("remark", noNullWrap(remark));
            params.put("deleteSchemeId", removeImages == null ? new ArrayList<>() : removeImages);
            return params;
        }

        /**
         * 客户管理-流失房屋
         *
         * @param houseId       房屋id
         * @param leaveReason   流失原因
         * @param leaveToSeries 客户去向
         * @param remark        备注信息
         */
        public static String leaveHouse(String houseId, String leaveReason, String leaveToSeries, String remark) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("leaveReason", noNullWrap(leaveReason));
            params.put("leaveToSeries", noNullWrap(leaveToSeries));
            params.put("remark", noNullWrap(remark));
            return toBody(params);
        }

        /*客户管理-主管查房*/
        public static String supervisorRounds(String houseId, String result, String remark) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("result", noNullWrap(result));
            params.put("remark", noNullWrap(remark));
            return toBody(params);
        }

        /*客户管理-安装完成*/
        public static Map<String, Object> finishInstall(String houseId, String installId, String actualInstallDate,
                                                        String installUserId, String installUserName, String installArea,
                                                        String installState, String remark) {
            Map<String, Object> params = objectMap();
            params.put("dealerId", SharedPreferencesUtils.getDealerId());
            params.put("houseId", noNullWrap(houseId));
            params.put("installId", noNullWrap(installId));
            params.put("actualInstallDate", noNullWrap(actualInstallDate));
            params.put("installUserId", noNullWrap(installUserId));
            params.put("installUserName", noNullWrap(installUserName));
            params.put("installArea", noNullWrap(installArea));
            params.put("installState", noNullWrap(installState));
            params.put("remark", noNullWrap(remark));
            return params;
        }

        /*客户管理-公海客户-领取客户*/
        public static String receiveHouse(String houseId, String shopId, String groupId, String salesId) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("shopId", noNullWrap(shopId));
            params.put("groupId", noNullWrap(groupId));
            params.put("salesId", noNullWrap(salesId));
            return toBody(params);
        }

        /**
         * 客户管理-推送系统消息(重新分配客户)
         *
         * @param personalId 客户id
         * @param houseId    房屋id
         * @param status     客户最新状态
         * @param statusTime 客户状态时间
         * @param reason     重新分配客户理由
         * @param shopId     门店id
         * @param groupId    分组id
         */
        public static String distributionMsgPush(String personalId, String houseId, String status,
                                                 String statusTime, String reason, String shopId,
                                                 String groupId) {
            Map<String, String> params = general();
            params.put("personalId", noNullWrap(personalId));
            params.put("houseId", noNullWrap(houseId));
            params.put("status", noNullWrap(status));
            params.put("statusTime", noNullWrap(statusTime));
            params.put("reason", noNullWrap(reason));
            params.put("shopId", noNullWrap(shopId));
            params.put("groupId", noNullWrap(groupId));
            return toBody(params);
        }

        /*客户管理-确认流失房屋*/
        public static String confirmLoseHouse(String houseId) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            return toBody(params);
        }

        /*无效退回*/
        public static String invalidReturn(String houseId, String efficientCause) {
            Map<String, String> params = general();
            params.put("houseId", noNullWrap(houseId));
            params.put("efficientCause", noNullWrap(efficientCause));
            return toBody(params);
        }
    }

    /*员工管理*/
    public static class Employee {
        /*获取员工列表传参*/
        public static Map<String, String> employeeList(String content, String shopId, String status, String roleCode) {
            Map<String, String> params = general();
            params.put("content", noNullWrap(content));
            params.put("shopId", noNullWrap(shopId));
            params.put("status", noNullWrap(status));
            params.put("roleCode", noNullWrap(roleCode));
            return params;
        }

        public static Map<String, String> employeeInfo(String userId) {
            Map<String, String> params = general();
            params.put("userId", userId);
            return params;
        }
    }

    public static class Homepage {

        /*本月数据传参*/
        public static Map<String, String> monthData(String type, String cityCode, String startTime, String endTime) {
            Map<String, String> params = general();
            params.put("type", noNullWrap(type));
            params.put("cityCode", noNullWrap(cityCode));
            params.put("startTime", noNullWrap(startTime));
            params.put("endTime", noNullWrap(endTime));
            return params;
        }
    }
}
