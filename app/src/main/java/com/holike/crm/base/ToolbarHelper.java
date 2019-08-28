package com.holike.crm.base;


import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

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

    static EditText addSearchContainer(@NonNull Toolbar toolbar, CharSequence hint, OnSearchClickListener onSearchClickListener) {
        Context context = toolbar.getContext();
        View searchLayout = toolbar.findViewById(R.id.mSearchContainer);
        if (searchLayout == null) { //没有添加过searchContainer，为空判断为了避免重复添加toolbar view
            searchLayout = LayoutInflater.from(context).inflate(R.layout.include_search_layout, toolbar, false);
            toolbar.addView(searchLayout);
        }
        EditText searchEditText = searchLayout.findViewById(R.id.mSearchEditText);
        searchEditText.setHint(hint);
        final ImageView mClearImageView = searchLayout.findViewById(R.id.mClearImageView);
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
                    mClearImageView.setVisibility(View.GONE);
                } else {
                    mClearImageView.setVisibility(View.VISIBLE);
                }
            }
        });
        mClearImageView.setOnClickListener(view -> searchEditText.setText(""));
        return searchEditText;
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
    private static View getSearchContainer(@NonNull Toolbar toolbar) {
        return toolbar.findViewById(R.id.mSearchContainer);
    }

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
