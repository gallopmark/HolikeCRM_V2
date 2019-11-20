package com.holike.crm.helper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.holike.crm.dialog.CalendarPickerDialog;

import java.util.Date;
import java.util.List;

/**
 * Created by pony 2019/7/4
 * Copyright (c) 2019 holike
 */
public class CalendarPickerHelper {

    public static void showCalendarDialog(Context context, List<Date> selectedDates, @NonNull final OnCalendarOperateListener listener) {
        new CalendarPickerDialog.Builder(context)
                .maxDate(new Date())   //只允许选择过去及当前时间
                .withSelectedDates(selectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        listener.onCalendarPicker(dialog, selectedDates, start, end);
                    }
                }).onShowListener(dialogInterface -> listener.onShow()).dismissListener(dialogInterface -> listener.onDismiss()).show();
    }

    public abstract static class SimpleCalendarPickerListener implements OnCalendarOperateListener{

        @Override
        public void onShow() {

        }

        @Override
        public void onDismiss() {

        }
    }

    public interface OnCalendarOperateListener {
        void onShow();

        void onCalendarPicker(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end);

        void onDismiss();
    }
}
