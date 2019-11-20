package com.holike.crm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holike.crm.R;
import com.holike.crm.customView.AppToastCompat;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarPickerDialog extends Dialog {
    private Date minDate, maxDate;
    private List<Date> selectedDates;
    private CharSequence left, right;
    private boolean clickToClear;
    private OnCalendarRangeSelectedListener onCalendarRangeSelectedListener;

    private CalendarPickerDialog(Builder builder) {
        super(builder.context, R.style.Dialog);
        this.minDate = builder.minDate;
        this.maxDate = builder.maxDate;
        this.selectedDates = builder.selectedDates;
        this.left = builder.left;
        this.right = builder.right;
        this.clickToClear = builder.clickToClear;
        this.onCalendarRangeSelectedListener = builder.onCalendarRangeSelectedListener;
        setOnDismissListener(builder.onDismissListener);
        setOnShowListener(builder.onShowListener);
    }

    private static abstract class DoubleClickListener implements View.OnClickListener {
        private static final long DOUBLE_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View view) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
                onDoubleClick(view);
            }
            lastClickTime = currentTimeMillis;
        }

        abstract void onDoubleClick(View view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_calendarpicker, new LinearLayout(getContext()), false);
        FrameLayout titleLayout = contentView.findViewById(R.id.mTitleLayout);
        TextView cancelTextView = contentView.findViewById(R.id.mCancelTextView);
        TextView confirmTextView = contentView.findViewById(R.id.mConfirmTextView);
        CalendarPickerView calendarView = contentView.findViewById(R.id.calendarView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) calendarView.getLayoutParams();
        params.height = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_calendar_height);
        calendarView.setLayoutParams(params);
        if (!TextUtils.isEmpty(left)) {
            cancelTextView.setText(left);
        }
        if (!TextUtils.isEmpty(right)) {
            confirmTextView.setText(right);
        }
        initMinDate();
        initMaxDate();
        List<Date> selected = new ArrayList<>();
        if (selectedDates != null && !selectedDates.isEmpty()) {
            if (selectedDates.size() >= 2) {
                Collections.sort(selectedDates);
                selected.add(selectedDates.get(0));
                selected.add(selectedDates.get(selectedDates.size() - 1));
            } else {
                selected.addAll(selectedDates);
            }
        }
        calendarView.init(minDate, maxDate)
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(selected);
        calendarView.setTypeface(Typeface.DEFAULT);
        calendarView.setDateTypeface(Typeface.DEFAULT);
        calendarView.setOnInvalidDateSelectedListener(date -> {
            DateFormat fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
            showShortToast(getContext().getString(R.string.dialog_calendar_select_error, fullDateFormat.format(minDate),
                    fullDateFormat.format(maxDate)));
        });
        titleLayout.setOnClickListener(new DoubleClickListener() {
            @Override
            void onDoubleClick(View view) {
                calendarView.selectDate(new Date(), true);
            }
        });
        cancelTextView.setOnClickListener(view -> {
            if (clickToClear)
                calendarView.clearSelectedDates();
            if (onCalendarRangeSelectedListener != null) {
                onCalendarRangeSelectedListener.onLeftClicked(CalendarPickerDialog.this);
            }
        });
        confirmTextView.setOnClickListener(view -> {
            if (onCalendarRangeSelectedListener != null) {
                List<Date> selectedDates = calendarView.getSelectedDates();
                int size = selectedDates.size();
                Date start, end;
                if (size > 0) {
                    start = calendarView.getSelectedDates().get(0);
                    end = calendarView.getSelectedDates().get(size - 1);
                } else {
                    start = new Date();
                    end = new Date();
                }
                onCalendarRangeSelectedListener.onRightClick(CalendarPickerDialog.this, selectedDates, start, end);
            }
            if (clickToClear)
                calendarView.clearSelectedDates();
        });
        setContentView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        if (window != null) {
            renderWindow(window);
        }
    }

    private void renderWindow(Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setWindowAnimations(R.style.Dialog_Anim);
        window.setGravity(Gravity.BOTTOM);
    }

    private void initMinDate() {
        if (minDate == null) {
            String source = "2018-2-1";   //开始日期默认为2018-2-1 CRM从2018年开始
            try {
                minDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(source);
            } catch (Exception e) {
                minDate = new Date();
            }
        }
    }

    private void initMaxDate() {
        if (maxDate == null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR); //当前年份
            if (year <= 2100) {
                year = 2100;
            } else {
                year += 3; //往前三年
            }
//            int year = calendar.get(Calendar.YEAR) + 3;  //往前三年
            String source = year + "-" + "2-1";
            try {
                maxDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(source);
            } catch (Exception e) {
                maxDate = new Date();
            }
        } else {
            Calendar calendar = Calendar.getInstance();   //最大日期往后加一天
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            maxDate = calendar.getTime();
        }
    }

    private void showShortToast(CharSequence text) {
        if (TextUtils.isEmpty(text)) return;
        AppToastCompat.from(getContext())
                .with(text)
                .setDuration(Toast.LENGTH_SHORT)
                .show();
    }

    public static class Builder {
        private Context context;
        private Date minDate, maxDate;
        private List<Date> selectedDates;
        private CharSequence left, right;
        private boolean clickToClear = false;
        private OnCalendarRangeSelectedListener onCalendarRangeSelectedListener;
        private OnDismissListener onDismissListener;
        private OnShowListener onShowListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder minDate(Date minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder maxDate(Date maxDate) {
            this.maxDate = maxDate;
            return this;
        }

        public Builder withSelectedDates(List<Date> selectedDates) {
            this.selectedDates = selectedDates;
            return this;
        }

        public Builder left(CharSequence left) {
            this.left = left;
            return this;
        }

        public Builder right(CharSequence right) {
            this.right = right;
            return this;
        }

        public Builder clickToClear(boolean clickToClear) {
            this.clickToClear = clickToClear;
            return this;
        }

        public Builder calendarRangeSelectedListener(OnCalendarRangeSelectedListener onCalendarRangeSelectedListener) {
            this.onCalendarRangeSelectedListener = onCalendarRangeSelectedListener;
            return this;
        }

        public Builder dismissListener(OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public Builder onShowListener(OnShowListener onShowListener) {
            this.onShowListener = onShowListener;
            return this;
        }

        public CalendarPickerDialog create() {
            return new CalendarPickerDialog(this);
        }

        public void show() {
            create().show();
        }
    }

    public interface OnCalendarRangeSelectedListener {
        void onLeftClicked(CalendarPickerDialog dialog);

        void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end);
    }
}
