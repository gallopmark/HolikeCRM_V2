package com.holike.crm.bean;

import androidx.annotation.NonNull;

import com.contrarywind.interfaces.IPickerViewData;

import org.parceler.Parcel;

import java.util.Map;

/**
 * Created by wqj on 2018/6/4.
 * 客户管理条件选择bean
 */
@Parcel
public class TypeIdBean {

     Map<String, String> CUSTOMER_HOUSE_TYPE;//房屋类型
     Map<String, String> CUSTOMER_DECORATE_PROPERTIES;//装修性质
     Map<String, String> CUSTOMER_TYPE_CODE;//客户类型
     Map<String, String> CUSTOMER_SOURCE_CODE;//客户来源
     Map<String, String> CUSTOMER_DECORATE_TYPE;//装修类型
     Map<String, String> HOUSE_STATUE_CODE;//客户状态
     Map<String, String> CUSTOMER_GENDER_CODE;//客户性别
     Map<String, String> shopId;//所属门店
     Map<String, String> CUSTOMER_CONTACT_CODE;//沟通方式
     Map<String, String> CUSTOMER_PATIENT_ROUNDS;//查房结果
     Map<String, String> DIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE;//量房空间
     Map<String, String> DIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND;//家具需求
     Map<String, String> STYLE_SERIES_ZE_FG;//花色偏好
     Map<String, String> ORDER_MATERIAL_ZE_CZ;//材质偏好
     Map<String, String> CUSTOMER_ESPECIALLY_TYPE;//特别客源
     Map<String, String> CUSTOMER_LOST_REASON;//流失原因
     Map<String, String> DIGITAL_INVALIDITY_RESON;//无效原因
     Map<String, String> LOSE_OF_THE_BRAND;//流失去向
     Map<String, String> CUSTOMER_STATUS_MOVE;//客户端移动状态
     Map<String, String> CUSTOMER_EARNESTHOUSE_TYPE;//订金类型
     Map<String, String> CUSTOMER_CHECKBULIDING_CODE;//是否收楼
     Map<String, String> CUSTOMER_DECORATION_PROGRESS;//是否装修


    public Map<String, String> getCUSTOMER_CHECKBULIDING_CODE() {
        return CUSTOMER_CHECKBULIDING_CODE;
    }

    public void setCUSTOMER_CHECKBULIDING_CODE(Map<String, String> CUSTOMER_CHECKBULIDING_CODE) {
        this.CUSTOMER_CHECKBULIDING_CODE = CUSTOMER_CHECKBULIDING_CODE;
    }

    public Map<String, String> getCUSTOMER_DECORATION_PROGRESS() {
        return CUSTOMER_DECORATION_PROGRESS;
    }

    public void setCUSTOMER_DECORATION_PROGRESS(Map<String, String> CUSTOMER_DECORATION_PROGRESS) {
        this.CUSTOMER_DECORATION_PROGRESS = CUSTOMER_DECORATION_PROGRESS;
    }

    public Map<String, String> getDIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND() {
        return DIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND;
    }

    public void setDIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND(Map<String, String> DIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND) {
        this.DIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND = DIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND;
    }

    public Map<String, String> getCUSTOMER_EARNESTHOUSE_TYPE() {
        return CUSTOMER_EARNESTHOUSE_TYPE;
    }

    public void setCUSTOMER_EARNESTHOUSE_TYPE(Map<String, String> CUSTOMER_EARNESTHOUSE_TYPE) {
        this.CUSTOMER_EARNESTHOUSE_TYPE = CUSTOMER_EARNESTHOUSE_TYPE;
    }

    public Map<String, String> getCUSTOMER_HOUSE_TYPE() {
        return CUSTOMER_HOUSE_TYPE;
    }

    public void setCUSTOMER_HOUSE_TYPE(Map<String, String> CUSTOMER_HOUSE_TYPE) {
        this.CUSTOMER_HOUSE_TYPE = CUSTOMER_HOUSE_TYPE;
    }

    public Map<String, String> getCUSTOMER_DECORATE_PROPERTIES() {
        return CUSTOMER_DECORATE_PROPERTIES;
    }

    public void setCUSTOMER_DECORATE_PROPERTIES(Map<String, String> CUSTOMER_DECORATE_PROPERTIES) {
        this.CUSTOMER_DECORATE_PROPERTIES = CUSTOMER_DECORATE_PROPERTIES;
    }

    public Map<String, String> getCUSTOMER_TYPE_CODE() {
        return CUSTOMER_TYPE_CODE;
    }

    public void setCUSTOMER_TYPE_CODE(Map<String, String> CUSTOMER_TYPE_CODE) {
        this.CUSTOMER_TYPE_CODE = CUSTOMER_TYPE_CODE;
    }

    public Map<String, String> getCUSTOMER_SOURCE_CODE() {
        return CUSTOMER_SOURCE_CODE;
    }

    public void setCUSTOMER_SOURCE_CODE(Map<String, String> CUSTOMER_SOURCE_CODE) {
        this.CUSTOMER_SOURCE_CODE = CUSTOMER_SOURCE_CODE;
    }

    public Map<String, String> getCUSTOMER_DECORATE_TYPE() {
        return CUSTOMER_DECORATE_TYPE;
    }

    public void setCUSTOMER_DECORATE_TYPE(Map<String, String> CUSTOMER_DECORATE_TYPE) {
        this.CUSTOMER_DECORATE_TYPE = CUSTOMER_DECORATE_TYPE;
    }

    public Map<String, String> getHOUSE_STATUE_CODE() {
        return HOUSE_STATUE_CODE;
    }

    public void setHOUSE_STATUE_CODE(Map<String, String> HOUSE_STATUE_CODE) {
        this.HOUSE_STATUE_CODE = HOUSE_STATUE_CODE;
    }

    public Map<String, String> getCUSTOMER_GENDER_CODE() {
        return CUSTOMER_GENDER_CODE;
    }

    public void setCUSTOMER_GENDER_CODE(Map<String, String> CUSTOMER_GENDER_CODE) {
        this.CUSTOMER_GENDER_CODE = CUSTOMER_GENDER_CODE;
    }

    public Map<String, String> getShopId() {
        return shopId;
    }

    public void setShopId(Map<String, String> shopId) {
        this.shopId = shopId;
    }

    public Map<String, String> getCUSTOMER_CONTACT_CODE() {
        return CUSTOMER_CONTACT_CODE;
    }

    public void setCUSTOMER_CONTACT_CODE(Map<String, String> CUSTOMER_CONTACT_CODE) {
        this.CUSTOMER_CONTACT_CODE = CUSTOMER_CONTACT_CODE;
    }

    public Map<String, String> getCUSTOMER_PATIENT_ROUNDS() {
        return CUSTOMER_PATIENT_ROUNDS;
    }

    public void setCUSTOMER_PATIENT_ROUNDS(Map<String, String> CUSTOMER_PATIENT_ROUNDS) {
        this.CUSTOMER_PATIENT_ROUNDS = CUSTOMER_PATIENT_ROUNDS;
    }

    public Map<String, String> getDIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE() {
        return DIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE;
    }

    public void setDIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE(Map<String, String> DIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE) {
        this.DIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE = DIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE;
    }

    public Map<String, String> getSTYLE_SERIES_ZE_FG() {
        return STYLE_SERIES_ZE_FG;
    }

    public void setSTYLE_SERIES_ZE_FG(Map<String, String> STYLE_SERIES_ZE_FG) {
        this.STYLE_SERIES_ZE_FG = STYLE_SERIES_ZE_FG;
    }

    public Map<String, String> getORDER_MATERIAL_ZE_CZ() {
        return ORDER_MATERIAL_ZE_CZ;
    }

    public void setORDER_MATERIAL_ZE_CZ(Map<String, String> ORDER_MATERIAL_ZE_CZ) {
        this.ORDER_MATERIAL_ZE_CZ = ORDER_MATERIAL_ZE_CZ;
    }

    public Map<String, String> getCUSTOMER_ESPECIALLY_TYPE() {
        return CUSTOMER_ESPECIALLY_TYPE;
    }

    public void setCUSTOMER_ESPECIALLY_TYPE(Map<String, String> CUSTOMER_ESPECIALLY_TYPE) {
        this.CUSTOMER_ESPECIALLY_TYPE = CUSTOMER_ESPECIALLY_TYPE;
    }

    public Map<String, String> getCUSTOMER_LOST_REASON() {
        return CUSTOMER_LOST_REASON;
    }

    public void setCUSTOMER_LOST_REASON(Map<String, String> CUSTOMER_LOST_REASON) {
        this.CUSTOMER_LOST_REASON = CUSTOMER_LOST_REASON;
    }

    public Map<String, String> getDIGITAL_INVALIDITY_RESON() {
        return DIGITAL_INVALIDITY_RESON;
    }

    public void setDIGITAL_INVALIDITY_RESON(Map<String, String> DIGITAL_INVALIDITY_RESON) {
        this.DIGITAL_INVALIDITY_RESON = DIGITAL_INVALIDITY_RESON;
    }

    public Map<String, String> getLOSE_OF_THE_BRAND() {
        return LOSE_OF_THE_BRAND;
    }

    public void setLOSE_OF_THE_BRAND(Map<String, String> LOSE_OF_THE_BRAND) {
        this.LOSE_OF_THE_BRAND = LOSE_OF_THE_BRAND;
    }

    public Map<String, String> getCUSTOMER_STATUS_MOVE() {
        return CUSTOMER_STATUS_MOVE;
    }

    public void setCUSTOMER_STATUS_MOVE(Map<String, String> CUSTOMER_STATUS_MOVE) {
        this.CUSTOMER_STATUS_MOVE = CUSTOMER_STATUS_MOVE;
    }

    @Parcel
    public static class TypeIdItem implements IPickerViewData, Comparable<TypeIdItem> {
         String id;
         String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getPickerViewText() {
            return name;
        }

        public TypeIdItem() {
            id = "";
            name = "";
        }

        public TypeIdItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public int compareTo(@NonNull TypeIdItem o) {
            try {
                return Integer.parseInt(this.getId()) > Integer.parseInt(o.getId()) ? 1 : -1;
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
