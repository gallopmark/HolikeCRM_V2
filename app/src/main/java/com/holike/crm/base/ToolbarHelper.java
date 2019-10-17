package com.holike.crm.base;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.holike.crm.R;
import com.holike.crm.util.DensityUtil;

/**
 * Created by gallop on 2019/7/31.
 * Copyright holike possess 2019.
 */
public class ToolbarHelper {

    static void inflateMenu(@NonNull Toolbar toolbar, @MenuRes int menuId) {
        toolbar.inflateMenu(menuId);
        /*取消menu长按显示Toast*/
        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            toolbar.getMenu().getItem(i).setTitle("");
        }
    }

    public static void showPopupWindow(PopupWindow popupWindow, View parent) {
        Context context = parent.getContext();
        popupWindow.showAtLocation(parent, Gravity.TOP | Gravity.END, context.getResources().getDimensionPixelSize(R.dimen.dp_6),
                DensityUtil.getStatusHeight(context) + context.getResources().getDimensionPixelSize(R.dimen.dp_45));
    }

    public static int[] defaultMargin(Context context) {
        return new int[]{0, context.getResources().getDimensionPixelOffset(R.dimen.dp_56)};
    }

    private static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    @SuppressLint("ClickableViewAccessibility")
    static EditText addSearchContainer(@NonNull Toolbar toolbar, CharSequence hint,
                                       @DrawableRes int resId, OnSearchClickListener onSearchClickListener) {
        Context context = toolbar.getContext();
        View searchView = getSearchContainer(toolbar);
        if (searchView == null) { //没有添加过searchContainer，为空判断为了避免重复添加toolbar view
            LayoutInflater.from(context).inflate(R.layout.include_search_layout, toolbar, true);
        }
        EditText searchEditText = toolbar.findViewById(R.id.app_search_view);
        int pixels = getScreenWidth(context) - context.getResources().getDimensionPixelSize(R.dimen.dp_56) * 2
                - context.getResources().getDimensionPixelSize(R.dimen.dp_6) * 2;
        searchEditText.setMinWidth(pixels);
        searchEditText.setMaxWidth(pixels);
        if (resId != -1) {
            searchEditText.setBackgroundResource(resId);
        }
        searchEditText.setHint(hint);
        searchEditText.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (onSearchClickListener != null) {
                    onSearchClickListener.onSearch(searchEditText, actionId, event);
                }
            }
            return false;
        });
        searchEditText.addTextChangedListener(new SimpleTextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (TextUtils.isEmpty(s)) {
                    setIconChange(searchEditText, 0);
                } else {
                    setIconChange(searchEditText, R.drawable.ic_clear_default_24dp);
                }
            }
        });
        searchEditText.setOnTouchListener((view, event) -> {
            int action = event.getAction();
            Drawable drawableRight = searchEditText.getCompoundDrawables()[2];
            //监听手指的按下、滑动、抬起来达到图片的点击，按住效果
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (drawableRight != null && !TextUtils.isEmpty(searchEditText.getText())) {
                        boolean touchable = event.getX() > (searchEditText.getWidth() - searchEditText.getTotalPaddingRight())
                                && (event.getX() < ((searchEditText.getWidth() - searchEditText.getPaddingRight())));
                        if (touchable) {
                            setIconChange(searchEditText, R.drawable.ic_clear_pressed_24dp);
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (drawableRight != null && !TextUtils.isEmpty(searchEditText.getText())) {
                        boolean touchable = (event.getX() > (searchEditText.getWidth() - searchEditText.getTotalPaddingRight()) && (event
                                .getX() < ((searchEditText.getWidth() - searchEditText.getPaddingRight()))))
                                && (event.getY() > 0 && event.getY() < searchEditText.getHeight() + 100);
                        if (touchable) {
                            setIconChange(searchEditText, R.drawable.ic_clear_pressed_24dp);
                        } else {
                            setIconChange(searchEditText, R.drawable.ic_clear_default_24dp);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (drawableRight != null && !TextUtils.isEmpty(searchEditText.getText())) {
                        setIconChange(searchEditText, R.drawable.ic_clear_default_24dp);
                        boolean touchable = event.getX() > (searchEditText.getWidth() - searchEditText.getTotalPaddingRight())
                                && (event.getX() < ((searchEditText.getWidth() - searchEditText.getPaddingRight())))
                                && (event.getY() > 0 && event.getY() < searchEditText.getHeight());
                        if (touchable) {
                            searchEditText.setText("");
                        }
                    }
                    break;
            }
            return false;
        });
        return searchEditText;
    }

    private static void setIconChange(EditText editText, int resId) {
        Context context = editText.getContext();
        Drawable clearDrawable = resId == 0 ? null : context.getResources().getDrawable(resId);
        if (clearDrawable != null) {
            final int size = context.getResources().getDimensionPixelSize(R.dimen.dp_20);
            clearDrawable.setBounds(0, 0, size, size); //第一0是距左边距离，第二0是距上边距离，30、35分别是长宽
        }
        editText.setCompoundDrawables(editText.getCompoundDrawables()[0],
                editText.getCompoundDrawables()[1], clearDrawable, editText.getCompoundDrawables()[3]);
    }

    private static boolean isMenuEmpty(@NonNull Toolbar toolbar) {
        if (toolbar.getMenu() == null) return true;
        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            if (toolbar.getMenu().getItem(i).isVisible()) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    static View getSearchContainer(@NonNull Toolbar toolbar) {
        return toolbar.findViewById(R.id.app_search_view);
    }

    @Deprecated
    public static void addToolbarSearchMargin(Toolbar toolbar) {
        if (isMenuEmpty(toolbar)) {  //没有添加任何菜单，或者添加的菜单全部不可见，设置搜索布局的右边距为56dp
            int[] margins = defaultMargin(toolbar.getContext());
            addToolbarSearchMargin(toolbar, margins[0], margins[1]);
        } else { //添加了菜单 搜索布局的左右边距设置为0
            addToolbarSearchMargin(toolbar, 0, 0);
        }
    }

    private static void addToolbarSearchMargin(Toolbar toolbar, int leftMargin, int rightMargin) {
        View searchLayout = getSearchContainer(toolbar);
        if (searchLayout == null) return;
        if (searchLayout.getLayoutParams() instanceof Toolbar.LayoutParams) {
            Toolbar.LayoutParams params = (Toolbar.LayoutParams) searchLayout.getLayoutParams();
            params.leftMargin = leftMargin;
            params.rightMargin = rightMargin;
        }
    }

    public interface OnSearchClickListener {
        void onSearch(EditText searchView, int actionId, KeyEvent event);
    }
}
