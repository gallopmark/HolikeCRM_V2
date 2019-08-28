package com.holike.crm.popupwindown;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holike.crm.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by wqj on 2018/4/17.
 * 日期选择器
 */
@Deprecated
public class DatePickerPopupWindow extends BasePopupWindow {
    private CalendarPickerView calendar;
    private Date startDate, endDate;
    private SelectDateListener listener;

    public DatePickerPopupWindow(Context context) {
        super(context);
        init();
    }

    @Override
    int setContentView() {
        return R.layout.popup_date_picker;
    }

    @Override
    protected int setColorDrawable() {
        return R.color.bg_transparent;
    }

    public DatePickerPopupWindow setListener(SelectDateListener listener) {
        this.listener = listener;
        return this;
    }

    public DatePickerPopupWindow setDateScope(Date minDate, Date maxDate) {
        if (calendar != null) {
            Calendar calendar2 = new GregorianCalendar();
            calendar2.setTime(maxDate);
            calendar2.add(Calendar.DATE, 1);//把日期往后增加一天
            calendar.init(minDate, calendar2.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDate(new Date());
            startDate = maxDate;
        }
        return this;
    }

    private void init() {
        View outSide = mContentView.findViewById(R.id.view_outside);
        TextView btnCancel = mContentView.findViewById(R.id.btn_popup_date_picker_cancel);
        TextView btnSure = mContentView.findViewById(R.id.btn_popup_date_picker_sure);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        outSide.setOnClickListener(v -> dismiss());
        btnCancel.setOnClickListener(v -> dismiss());
        btnSure.setOnClickListener(v -> {
            if (listener != null) {
                if (endDate != null) {
                    listener.callBack(startDate, endDate);
                    dismiss();
                } else if (startDate != null) {
                    listener.callBack(startDate, startDate);
                    dismiss();
                }
            }
        });
        calendar = mContentView.findViewById(R.id.calendarView);
        calendar.setTypeface(Typeface.DEFAULT);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(final Date date) {
                if (startDate == null) {
                    startDate = date;
                } else {
                    endDate = date;
                }
            }

            @Override
            public void onDateUnselected(Date date) {
                startDate = null;
                endDate = null;
            }
        });
    }

    public interface SelectDateListener {
        void callBack(Date startDate, Date endDate);
    }
}
