package com.holike.crm.presenter.fragment;

import android.content.Context;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.WriteCityBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.model.fragment.WriteCityModel;
import com.holike.crm.view.fragment.WirteCityView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by wqj on 2018/6/26.
 * 主动营销-填写城市
 */

public class WirteCityPresenter extends BasePresenter<WirteCityView, WriteCityModel> {

    /**
     * 获取数据
     */
    public void getData() {
        model.getData(new WriteCityModel.GetDataListener() {
            @Override
            public void success(WriteCityBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 保存城市
     *
     * @param selectDataBean
     * @param start
     * @param end
     */
    public void saveCity(final WriteCityBean.SelectDataBean selectDataBean, String start, String end, final String time) {
        if (selectDataBean == null) {
            if (getView() != null)
                getView().tips("请选择服务城市");
        } else if (start == null || end == null) {
            if (getView() != null)
                getView().tips("请选择服务日期");
        } else {
            if (getView() != null)
                getView().loading();
            model.saveCity(selectDataBean.getDealerId(), String.valueOf(Long.parseLong(start)), String.valueOf(Long.parseLong(end)), new WriteCityModel.SaveCityListener() {
                @Override
                public void success(String success) {
                    WriteCityBean.ActiveRecordBean bean = new WriteCityBean.ActiveRecordBean(selectDataBean.getDealerId(), selectDataBean.getDealerName(), time);
                    if (getView() != null)
                        getView().saveSucess(success, bean);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    /**
     * 删除城市
     *
     * @param dealerId
     */
    public void delCity(String dealerId, final int position) {
        model.delCity(dealerId, new WriteCityModel.DelCityListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().delSuccess(success, position);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 选择时间
     */
    public void selectDate(Context context, List<Date> mSelectedDates, final CalendarPickerDialog.OnCalendarRangeSelectedListener listener) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 10);
        new CalendarPickerDialog.Builder(context)
                .maxDate(calendar.getTime())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(listener).show();
    }
}
