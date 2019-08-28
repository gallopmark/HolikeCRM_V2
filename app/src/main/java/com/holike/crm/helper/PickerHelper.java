package com.holike.crm.helper;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.List;


/**
 * Created by gallop on 2019/7/25.
 * Copyright holike possess 2019.
 */
public class PickerHelper {

    /*显示时间选择器，默认 “年”“月”“日”*/
    public static void showTimePicker(Context context, OnTimeSelectListener listener) {
        new TimePickerBuilder(context, listener).setType(new boolean[]{true, true, true, false, false, false}).build().show();
    }

    /*显示时间选择器,“年”“月”“日”“时”“分”*/
    public static void showTimeYMDHMPicker(Context context, OnTimeSelectListener listener) {
        new TimePickerBuilder(context, listener).setType(new boolean[]{true, true, true, true, true, false}).build().show();
    }

    /*显示时间选择器，“时”，“分”*/
    public static void showTimeHMPicker(Context context, OnTimeSelectListener listener) {
        new TimePickerBuilder(context, listener).setType(new boolean[]{false, false, false, true, true, false}).build().show();
    }

    public static void showTimeHMSPicker(Context context, OnTimeSelectListener listener) {
        new TimePickerBuilder(context, listener).setType(new boolean[]{false, false, false, true, true, true}).build().show();
    }

    public static void showTimePicker(Context context, OnTimeSelectListener listener, View parent) {
        new TimePickerBuilder(context, listener).setType(new boolean[]{true, true, true, false, false, false}).build().show(parent);
    }

    public static void showTimeHMPicker(Context context, OnTimeSelectListener listener, View parent) {
        new TimePickerBuilder(context, listener).setType(new boolean[]{true, true, true, true, true, false}).build().show(parent);
    }

    public static void showOptionsPicker(Context context, List<String> optionsItems, OnOptionsSelectListener listener) {
        showOptionsPicker(context, optionsItems, listener, 0);
    }

    public static void showOptionsPicker(Context context, List<String> optionsItems, OnOptionsSelectListener listener, int selectPosition) {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(context, listener).build();
        pickerView.setPicker(optionsItems);
        pickerView.setSelectOptions(selectPosition);
        pickerView.show();
    }
}
