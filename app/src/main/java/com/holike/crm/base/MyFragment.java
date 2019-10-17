package com.holike.crm.base;

import android.text.TextUtils;
import com.holike.crm.fragment.FragmentBackHandler;
import com.holike.crm.util.NumberUtil;

/**
 * Created by wqj on 2018/4/11.
 * 实现activity功能的fragment
 */

public abstract class MyFragment<P extends BasePresenter, V extends BaseView> extends BaseFragment<P, V>{

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
