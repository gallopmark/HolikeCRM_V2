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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.holike.crm.R;
import com.holike.crm.util.DensityUtil;

import galloped.xcode.widget.TitleBar;

public class ToolbarHelper {

    static void setTitleBackgroundResource(@Nullable TitleBar titleBar, @DrawableRes int resId) {
        if (titleBar != null) {
            titleBar.setBackgroundResource(resId);
        }
    }

    @Nullable
    static FrameLayout setRightMenu(@Nullable TitleBar titleBar, @Nullable CharSequence menuText, View.OnClickListener actionClickListener) {
        if (titleBar == null) return null;
        titleBar.getMenu().clear();
        if (TextUtils.isEmpty(menuText)) {
            return null;
        }
        inflateMenu(titleBar, R.menu.menu_main);
        final View actionView = titleBar.getMenu().findItem(R.id.menu_main).getActionView();
        TextView tvMenu = actionView.findViewById(R.id.right_menu_view);
        tvMenu.setText(menuText);
        if (titleBar.getTag() != null) {
            tvMenu.setTextColor(ContextCompat.getColor(titleBar.getContext(), R.color.color_while));
        }
        actionView.setOnClickListener(actionClickListener);
        return actionView.findViewById(R.id.right_menu_layout);
    }

    static void setOptionsMenu(@Nullable TitleBar titleBar, @MenuRes int menuId, TitleBar.OnMenuItemClickListener menuItemClickListener) {
        if (titleBar != null) {
            titleBar.getMenu().clear();
            inflateMenu(titleBar, menuId);
            titleBar.setOnMenuItemClickListener(menuItemClickListener);
        }
    }

    static void hideOptionsMenu(@Nullable TitleBar titleBar) {
        if (titleBar != null) {
            titleBar.getMenu().clear();
        }
    }

    static void setMessageMenu(@Nullable FrameLayout menuLayout, boolean hasNewMsg) {
        if (hasNewMsg) {
            setMessageRedDot(menuLayout);
        } else {
            hideMessageRedDot(menuLayout);
        }
    }

    /*显示消息红点*/
    static void setMessageRedDot(@Nullable FrameLayout menuLayout) {
        if (menuLayout != null) {
            Context context = menuLayout.getContext();
            ImageView ivDot = menuLayout.findViewById(R.id.right_menu_dot);
            if (ivDot == null) {
                ivDot = new ImageView(context);
                ivDot.setId(R.id.right_menu_dot);
                int size = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
                params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_6);
                params.topMargin = size;
                params.gravity = Gravity.END;
                ivDot.setLayoutParams(params);
                menuLayout.addView(ivDot);
            }
            ivDot.setImageResource(R.drawable.ic_red_point);
        }
    }

    /*移除消息红点*/
    static void hideMessageRedDot(@Nullable FrameLayout menuLayout) {
        if (menuLayout != null) {
            View vDot = menuLayout.findViewById(R.id.right_menu_dot);
            if (vDot != null) {
                menuLayout.removeView(vDot);
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    static void inflateMenu(@NonNull TitleBar titleBar, @MenuRes int menuId) {
        titleBar.inflateMenu(menuId);
        /*取消menu长按显示Toast*/
        for (int i = 0; i < titleBar.getMenu().size(); i++) {
            titleBar.getMenu().getItem(i).setTitle(null);
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
    static EditText addSearchContainer(@Nullable TitleBar titleBar, CharSequence hint,
                                       @DrawableRes int resId, OnSearchClickListener onSearchClickListener) {
        if (titleBar == null) {
            return null;
        }
        Context context = titleBar.getContext();
        View searchView = getSearchContainer(titleBar);
        if (searchView == null) { //没有添加过searchContainer，为空判断为了避免重复添加toolbar view
            LayoutInflater.from(context).inflate(R.layout.include_search_layout, titleBar, true);
        }
        EditText searchEditText = titleBar.findViewById(R.id.app_search_view);
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

    @SuppressWarnings("WeakerAccess")
    @Nullable
    static View getSearchContainer(@NonNull TitleBar titleBar) {
        return titleBar.findViewById(R.id.app_search_view);
    }

    public interface OnSearchClickListener {
        void onSearch(EditText searchView, int actionId, KeyEvent event);
    }
}
