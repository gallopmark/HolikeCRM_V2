package com.holike.crm.bean;


import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2017/9/30.
 * 地区bean
 */

public class AreaBean implements Serializable {
    private List<Province> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    public List<Province> getOptions1Items() {
        return options1Items;
    }

    public void setOptions1Items(List<Province> options1Items) {
        this.options1Items = options1Items;
    }

    public List<List<String>> getOptions2Items() {
        return options2Items;
    }

    public void setOptions2Items(List<List<String>> options2Items) {
        this.options2Items = options2Items;
    }

    public List<List<List<String>>> getOptions3Items() {
        return options3Items;
    }

    public void setOptions3Items(List<List<List<String>>> options3Items) {
        this.options3Items = options3Items;
    }

    public static class Province implements IPickerViewData, Serializable {//省份内
        private String id;
        private String name;
        private List<City> citys;

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

        public List<City> getCitys() {
            return citys;
        }

        public void setCitys(List<City> citys) {
            this.citys = citys;
        }

        @Override
        public String getPickerViewText() {
            return name;
        }

        @Override
        public String toString() {
            return "Province{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", citys=" + citys + '}';
        }
    }

    public static class City implements IPickerViewData, Serializable {//城市类
        private String id;
        private String name;
        private List<District> districts;

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


        public List<District> getDistricts() {
            return districts;
        }

        public void setDistricts(List<District> districts) {
            this.districts = districts;
        }

        @Override
        public String toString() {
            return "City{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", districts=" + districts + '}';
        }

        @Override
        public String getPickerViewText() {
            return name;
        }
    }

    public static class District implements IPickerViewData, Serializable {//地区类
        private String id;
        private String name;

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
        public String toString() {
            return "District{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
        }

        @Override
        public String getPickerViewText() {
            return name;
        }
    }
}
