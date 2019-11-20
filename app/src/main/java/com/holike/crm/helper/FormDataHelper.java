package com.holike.crm.helper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.util.RecyclerUtils;

/**
 * 表格类数据处理帮助类
 */
public class FormDataHelper {

    public static void attachView(View contentView, @NonNull FormDataBinder binder) {
        attachView(contentView, binder, null);
    }

    public static void attachView(View contentView, @NonNull FormDataBinder binder, @Nullable FormDataCallback callback) {
        Context context = contentView.getContext();
        FrameLayout flContainer = contentView.findViewById(R.id.fl_form_data_container);
        //第一列数据
        View firstColumnView = LayoutInflater.from(context).inflate(binder.bindFirstColumnLayoutRes(), flContainer, false);
        ((TextView) firstColumnView.findViewById(R.id.tv_first_name)).setText(binder.bindSideTitle());
        flContainer.addView(firstColumnView, 1);  //动态添加layout 第一列数据展示布局
        if (callback != null) {
            callback.bindFirstColumn(firstColumnView);
        }
        LinearLayout scrollableLayout = contentView.findViewById(R.id.ll_scrollable_content);
        /*第一行数据*/
        View firstRowView = LayoutInflater.from(context).inflate(binder.bindFirstRowLayoutRes(), scrollableLayout, false);
        scrollableLayout.addView(firstRowView, 0);
        if (callback != null) {
            callback.bindFirstRow(firstRowView);
        }
        RecyclerView rvSide = firstColumnView.findViewById(R.id.rv_side);
        rvSide.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView rvContent = flContainer.findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(context));
        if (callback != null) {
            callback.bindFormContent(contentView);
        }
        rvSide.setAdapter(binder.bindSideAdapter());
        rvContent.setAdapter(binder.bindContentAdapter());
        RecyclerUtils.setScrollSynchronous(rvSide, rvContent);  //设置两个recyclerView联动滚动
    }

    public interface FormDataBinder {
        //第一列
        @LayoutRes
        int bindFirstColumnLayoutRes();  //include_form_data_column_60dp、include_form_data_column_70dp、include_form_data_column_80dp

        CharSequence bindSideTitle();

        //第一行
        @LayoutRes
        int bindFirstRowLayoutRes();

        /*设置第一列数据适配器*/
        RecyclerView.Adapter bindSideAdapter();

        /*设置表格适配器*/
        RecyclerView.Adapter bindContentAdapter();
    }

    public interface FormDataCallback {
        void bindFirstColumn(View view);

        void bindFirstRow(View view);

        void bindFormContent(View view);
    }

    public static class SimpleFormDataCallback implements FormDataCallback {

        @Override
        public void bindFirstColumn(View view) {

        }

        @Override
        public void bindFirstRow(View view) {

        }

        @Override
        public void bindFormContent(View view) {

        }
    }
}
