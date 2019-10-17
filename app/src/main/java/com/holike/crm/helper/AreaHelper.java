package com.holike.crm.helper;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.holike.crm.R;
import com.holike.crm.bean.AreaBean;
import com.holike.crm.util.ParseUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wqj on 2017/10/12.
 * 地区选择器帮助类
 */

public class AreaHelper {
    private AreaBean bean;

    /**
     * 获取地区数据
     *
     * @param context
     * @param listener
     */
    public void getArea(final Context context, final getAreaListener listener) {
        Observable.just("").subscribeOn(Schedulers.io()).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                bean = getArea(context);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                listener.success(bean);
            }
        });

    }

    public interface getAreaListener {
        void success(AreaBean areaBean);
    }

    public AreaBean getArea(Context context) {
        AreaBean bean = new AreaBean();
        List<AreaBean.Province> options1Items = parser(context);
        List<List<String>> options2Items = new ArrayList<>();
        List<List<List<String>>> options3Items = new ArrayList<>();
        for (AreaBean.Province province : options1Items) {
            List<String> options2 = new ArrayList<>();
            List<List<String>> options3 = new ArrayList<>();
            List<AreaBean.City> cityList = province.getCitys();
            for (AreaBean.City city : cityList) {
                options2.add(city.getName());
                List<AreaBean.District> districtList = city.getDistricts();
                List<String> districts = new ArrayList<>();
                for (AreaBean.District district : districtList) {
                    districts.add(district.getName());
                }
                options3.add(districts);
            }
            options2Items.add(options2);
            options3Items.add(options3);
        }
        bean.setOptions1Items(options1Items);
        bean.setOptions2Items(options2Items);
        bean.setOptions3Items(options3Items);
        return bean;
    }

    /**
     * 解析本地xml文件，返回地区列表
     *
     * @param context
     * @return
     */
    public List<AreaBean.Province> parser(Context context) {
        List<AreaBean.Province> list = null;
        AreaBean.Province province = null;

        List<AreaBean.City> cities = null;
        AreaBean.City city = null;

        List<AreaBean.District> districts = null;
        AreaBean.District district = null;

        // 创建解析器，并制定解析的xml文件
        XmlResourceParser parser = context.getResources().getXml(R.xml.province_data);
        try {
            int type = parser.getEventType();
            while (type != 1) {
                String tag = parser.getName();//获得标签名
                switch (type) {
                    case XmlResourceParser.START_DOCUMENT:
                        list = new ArrayList<AreaBean.Province>();
                        break;
                    case XmlResourceParser.START_TAG:
                        if ("province".equals(tag)) {
                            province = new AreaBean.Province();
                            cities = new ArrayList<AreaBean.City>();
                            int n = parser.getAttributeCount();
                            for (int i = 0; i < n; i++) {
                                //获得属性的名和值
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if ("zipcode".equals(name)) {
                                    province.setId(value);
                                } else if ("name".equals(name)) {
                                    province.setName(value);
                                }
                            }
                        }
                        if ("city".equals(tag)) {//城市
                            city = new AreaBean.City();
                            districts = new ArrayList<AreaBean.District>();
                            int n = parser.getAttributeCount();
                            for (int i = 0; i < n; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if ("zipcode".equals(name)) {
                                    city.setId(value);
                                } else if ("name".equals(name)) {
                                    city.setName(value);
                                }
                            }
                        }
                        if ("district".equals(tag)) {
                            district = new AreaBean.District();
                            int n = parser.getAttributeCount();
                            for (int i = 0; i < n; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if ("zipcode".equals(name)) {
                                    district.setId(value);
                                } else if ("name".equals(name)) {
                                    district.setName(value);
                                }
                            }
                            districts.add(district);
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        if ("city".equals(tag)) {
                            city.setDistricts(districts);
                            cities.add(city);
                        }
                        if ("province".equals(tag)) {
                            province.setCitys(cities);
                            list.add(province);
                        }
                        break;
                    default:
                        break;
                }
                type = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 显示地区选择器
     *
     * @param context
     * @param areaBean
     * @param listener
     */
    public static void showAreaPickerView(Context context, final AreaBean areaBean, String districtId, final SelectAreaListener listener) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
            String province = areaBean.getOptions1Items().get(options1).getName();//省
            String city = areaBean.getOptions2Items().get(options1).get(options2);//市
            String district = areaBean.getOptions3Items().get(options1).get(options2).get(options3);//区
            StringBuffer area = new StringBuffer();
            if (province.equals(city)) {//直辖市
                area.append(province).append(" ").append(district);
            } else {
                area.append(province).append(" ").append(city).append(" ").append(district);
            }
            String areaId = areaBean.getOptions1Items().get(options1).getCitys().get(options2).getDistricts().get(options3).getId();
            listener.selectCallBack(areaId, area.toString());
        }).build();
        pvOptions.setPicker(areaBean.getOptions1Items(), areaBean.getOptions2Items(), areaBean.getOptions3Items());
        int[] selectOptions = getSelectOptions(areaBean, districtId);
        pvOptions.setSelectOptions(selectOptions[0], selectOptions[1], selectOptions[2]);
        pvOptions.show();
    }

    public static interface SelectAreaListener {
        void selectCallBack(String areaId, String district);
    }

    /**
     * 获取地区ID对应的Options
     *
     * @param areaBean
     * @param id
     * @return
     */
    public static int[] getSelectOptions(final AreaBean areaBean, String id) {
        int[] result = new int[3];
        if (!TextUtils.isEmpty(id) && ParseUtils.parseInt(id) > 0) {
            List<AreaBean.Province> provinces = areaBean.getOptions1Items();
            for (int i1 = 0, length1 = provinces.size(); i1 < length1; i1++) {
                AreaBean.Province province = provinces.get(i1);
                if (province.getId().equals(id)) {
                    result = new int[]{i1, 0, 0};
                } else {
                    List<AreaBean.City> cities = province.getCitys();
                    for (int i2 = 0, length2 = cities.size(); i2 < length2; i2++) {
                        AreaBean.City city = cities.get(i2);
                        if (city.getId().equals(id)) {
                            result = new int[]{i1, i2, 0};
                        } else {
                            List<AreaBean.District> districts = city.getDistricts();
                            for (int i3 = 0, length3 = districts.size(); i3 < length3; i3++) {
                                AreaBean.District district = districts.get(i3);
                                if (district.getId().equals(id)) {
                                    result = new int[]{i1, i2, i3};
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据地区id返回地区信息
     *
     * @param areaBean
     * @param id
     * @return
     */
    public static String getSelectName(final AreaBean areaBean, String id) {
        int[] options = getSelectOptions(areaBean, id);
        StringBuffer area = new StringBuffer().append(areaBean.getOptions1Items().get(options[0]).getName()).append(" ").append(areaBean.getOptions2Items().get(options[0]).get(options[1])).append(" ").append(areaBean.getOptions3Items().get(options[0]).get(options[1]).get(options[2]));
        return area.toString();
    }
}
