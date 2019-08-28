package com.holike.crm.popupwindown;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/1/3.
 * 筛选popupWindown
 */

public class FilterPopupWindow extends BasePopupWindow {
    private RecyclerView rv;
    private SelectListener selectListener;
    private View view;
    private View outSide;
    private LinearLayout.LayoutParams rvLayoutParams;

    public FilterPopupWindow(Context context) {
        super(context);
        init();
    }

    public List<Map.Entry<String, String>> listData;

    @Override
    int setContentView() {
        return R.layout.popup_filter;
    }

    private void init() {
        outSide = mContentView.findViewById(R.id.view_outside);
        rv = mContentView.findViewById(R.id.rv_popup_filter);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        rvLayoutParams = (LinearLayout.LayoutParams) rv.getLayoutParams();
        outSide.setOnClickListener(view -> dismiss());
    }

    @Override
    protected int setColorDrawable() {
        return R.color.bg_transparent;
    }

    public FilterPopupWindow setData(final Map<String, String> datas, final String selectId, View view) {
        this.view = view;

        datas.put("", "全部");
        if (datas.size() > 6) {//最多6.5条可见
            rvLayoutParams.height = (int) (DensityUtil.dp2px(40.5f) * 6.5f);
        }
        final List<Map.Entry<String, String>> listData = new ArrayList<>();
        for (Map.Entry<String, String> entry : datas.entrySet()) {
            if (entry.getValue().equals("全部")) {
                listData.add(0, entry);
            } else {
                listData.add(entry);
            }
        }
//        /**
//         * Anonymous new Comparator<Map.Entry<String, String>>() can be replaced with lambda less... (Ctrl+F1)
//         * This inspection reports all anonymous classes which can be replaced with lambda expressions
//         * Lambda syntax is not supported under Java 1.7 or earlier JVMs.
//         */
//        Collections.sort(listData, (o1, o2) -> {
//            try {
//                return Integer.parseInt(o1.getKey()) > Integer.parseInt(o2.getKey()) ? 1 : -1;
//            } catch (Exception e) {
//                return 0;
//            }
//        });
        this.listData = listData;
        rv.setAdapter(new CommonAdapter<Map.Entry<String, String>>(mContext, listData) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_popup_filter;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, final Map.Entry<String, String> entry, int position) {
                TextView tv = holder.obtainView(R.id.tv_item_rv_popup_filter);
                View dv = holder.obtainView(R.id.dv_item_rv_popup_filter);
                if (position == listData.size() - 1) {//最后一项不要分割线
                    dv.setVisibility(View.GONE);
                } else {
                    dv.setVisibility(View.VISIBLE);
                }
                if (TextUtils.equals(entry.getKey(), selectId)) {
                    tv.setBackgroundResource(R.color.textColor5);
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_while));
                } else {
                    tv.setBackgroundResource(R.color.color_while);
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
                }
                tv.setText(entry.getValue());
                holder.itemView.setOnClickListener(v -> {
                    dismiss();
                    if (selectListener != null) {
                        selectListener.select(entry.getKey(), entry.getValue());
                    }
                });
            }
        });
        return this;
    }


    public FilterPopupWindow setSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    public interface SelectListener {
        void select(String selectId, String selectValue);

        //这个方法是为了销毁window时滞空popupwindow的实例，popupwindow为空时，才能创建popupwindow。以达到不重复创建popupwindow
        void onDismiss();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        }
        if (view != null) {
            view.setRotationX(180);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (view != null) {
            view.setRotationX(180);
        }
    }

    @Override
    public void dismiss() {
        if (selectListener != null) {
            selectListener.onDismiss();
        }
        super.dismiss();
        if (view != null) {
            view.setRotationX(0);
            view = null;
        }
    }

}
