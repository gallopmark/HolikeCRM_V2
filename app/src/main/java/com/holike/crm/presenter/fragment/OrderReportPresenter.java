package com.holike.crm.presenter.fragment;

import android.content.Context;
import android.content.DialogInterface;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.bean.OrderReportBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.model.fragment.OrderReportModel;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.view.fragment.OrderReportView;

import java.util.Date;
import java.util.List;

/**
 * Created by wqj on 2018/3/7.
 * 定单交易情况
 */

public class OrderReportPresenter extends BasePresenter<OrderReportView, OrderReportModel> {

    /**
     * 获取订单交易报表数据
     */
    public void getOrderReport(String cityCode, String startTime, String endTime, String type, String time) {
        model.getOrderReport(cityCode == null ? "" : cityCode, startTime == null ? "" : String.valueOf(ParseUtils.parseLong(startTime)), endTime == null ? "" : String.valueOf(ParseUtils.parseLong(endTime)), type == null ? "" : type, time == null ? "" : time, new OrderReportModel.GetOrderReportListener() {
            @Override
            public void success(OrderReportBean orderReportBean) {
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
     * 获取各月完成率
     */
    public void getCompleteData(String cityCode, String type) {
        model.getCompleteData(cityCode == null ? "" : cityCode, type == null ? "" : type, new OrderReportModel.GetCompleteListener() {
            @Override
            public void success(List<MonthCompleteBean> beans) {
                if (getView() != null)
                    getView().getCompleteDataSuccess(beans, beans.get(0).getArea());
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
    public static void selectDate(Context context, List<Date> selectedDates, final CalendarPickerDialog.OnCalendarRangeSelectedListener listener) {
        new CalendarPickerDialog.Builder(context).maxDate(new Date()).withSelectedDates(selectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(listener).show();
//        DatePickerPopupWindow datePickerPopupWindow = new DatePickerPopupWindow(context).setDateScope(TimeUtil.stringToDate("2018年2月1日", "yyyy年MM月dd日"), new Date()).setListener(new DatePickerPopupWindow.SelectDateListener() {
//            @Override
//            public void callBack(Date startDate, Date endDate) {
//                listener.callBack(startDate, endDate);
//            }
//        });
//        datePickerPopupWindow.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);

    }

    /**
     * 显示日期选择
     */
    public static void selectDate(Context context, List<Date> mSelectedDates, final CalendarPickerDialog.OnCalendarRangeSelectedListener listener,
                                  DialogInterface.OnDismissListener onDismissListener) {
        new CalendarPickerDialog.Builder(context).maxDate(new Date())
                .clickToClear(true)
                .withSelectedDates(mSelectedDates)
                .calendarRangeSelectedListener(listener).dismissListener(onDismissListener).show();
//        DatePickerPopupWindow datePickerPopupWindow = new DatePickerPopupWindow(context).setDateScope(TimeUtil.stringToDate("2018年2月1日", "yyyy年MM月dd日"), new Date()).setListener(new DatePickerPopupWindow.SelectDateListener() {
//            @Override
//            public void callBack(Date startDate, Date endDate) {
//                listener.callBack(startDate, endDate);
//            }
//        });
//        datePickerPopupWindow.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);
//        datePickerPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                dismissListener.onDismiss();
//            }
//        });
    }

    public static int getSelectPosition(String time, List<OrderReportBean.SelectDataBean> selectDataBeans) {
        for (int i = 0, size = selectDataBeans.size(); i < size; i++) {
            if (time != null && time.equals(selectDataBeans.get(i).getTime())) {
                return i;
            }
        }
        return 0;
    }
}
