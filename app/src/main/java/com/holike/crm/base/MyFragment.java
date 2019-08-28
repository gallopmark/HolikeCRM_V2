package com.holike.crm.base;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.holike.crm.R;
import com.holike.crm.customView.CompatToast;
import com.holike.crm.fragment.FragmentBackHandler;
import com.holike.crm.util.CopyUtil;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.NumberUtil;
import com.holike.crm.util.TimeUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by wqj on 2018/4/11.
 * 实现activity功能的fragment
 */

public abstract class MyFragment<P extends BasePresenter, V extends BaseView> extends BaseFragment<P, V> implements FragmentBackHandler {

//    @Override
//    protected void init() {
//        setBack();
//    }

    /*
     * fragment加载动画
     *
     * @param transit
     * @param enter
     * @param nextAnim
     * @return
     */
//    @Override
//    public Animation onCreateAnimation(int transit, final boolean enter, int nextAnim) {
//        if (enter) {
//            try {
//                Animation anim = AnimationUtils.loadAnimation(mContext, nextAnim);
//                anim.setAnimationListener(new Animation.AnimationListener() {
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//
//                    public void onAnimationEnd(Animation animation) {
//                        initFragment();
//                    }
//                });
//                return anim;
//            } catch (Exception e) {
//                initFragment();
//            }
//        }
//        return super.onCreateAnimation(transit, enter, nextAnim);
//    }

    public static String textEmpty(String content) {
        return TextUtils.isEmpty(content) ? "-" : content;
    }

    public static String textEmpty(String content, String symbol) {
        return TextUtils.isEmpty(content) ? symbol : content;
    }

    public static String textEmptyNumber(String content) {
        try {
            return TextUtils.isEmpty(content) ? "-" : NumberUtil.format(content);
        } catch (Exception e) {
            return content;
        }
    }
}
