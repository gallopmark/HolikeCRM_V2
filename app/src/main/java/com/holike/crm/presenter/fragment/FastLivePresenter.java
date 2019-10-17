package com.holike.crm.presenter.fragment;

import android.content.Context;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.FastLiveBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.model.fragment.FastLiveModel;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.view.fragment.FastLiveView;

import java.util.Date;
import java.util.List;

/**
 * Created by wqj on 2018/3/7.
 * 定单交易情况
 */

public class FastLivePresenter extends BasePresenter<FastLiveView, FastLiveModel> {

    /**
     * 获取订单交易报表数据
     */
    public void getOrderReport(String cityCode, String startTime, String endTime, String type, String time) {
        model.getOrderReport(cityCode == null ? "" : cityCode, startTime == null ? "" : String.valueOf(ParseUtils.parseLong(startTime)), endTime == null ? "" : String.valueOf(ParseUtils.parseLong(endTime)), type == null ? "" : type, time == null ? "" : time, new FastLiveModel.GetOrderReportListener() {
            @Override
            public void success(FastLiveBean orderReportBean) {
                if (getView() != null)
                    getView().getOrderReportSuccess(orderReportBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }


    /**
     * 显示日期选择
     */
    public static void selectDate(Context context, List<Date> mSelectedDates, final CalendarPickerDialog.OnCalendarRangeSelectedListener listener) {
        new CalendarPickerDialog.Builder(context).maxDate(new Date())
                .clickToClear(true)
                .withSelectedDates(mSelectedDates).calendarRangeSelectedListener(listener).show();
//        DatePickerPopupWindow datePickerPopupWindow = new DatePickerPopupWindow(context).setDateScope(TimeUtil.stringToDate("2018年2月1日", "yyyy年MM月dd日"), new Date()).setListener(listener);
//        datePickerPopupWindow.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);
    }

    public static int getSelectPosition(String time, List<FastLiveBean.SelectDataBean> selectDataBeans) {
        for (int i = 0, size = selectDataBeans.size(); i < size; i++) {
            if (time != null && time.equals(selectDataBeans.get(i).getSelectTime())) {
                return i;
            }
        }
        return 0;
    }
}
