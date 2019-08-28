package com.holike.crm.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.OrderRankingBean;
import com.holike.crm.util.DensityUtil;

import java.util.List;

/**
 * Created by wqj on 2018/4/28.
 * 选择地区
 */

public class SelectAreaDialog extends CommonDialog {
    private Context context;
    private String title;
    private String cityCode;
    private List<OrderRankingBean.SelectDataBean> selectDataBeans;
    private SelectAreaListener selectAreaListener;

    public SelectAreaDialog(@NonNull Context context, String title, String cityCode, List<OrderRankingBean.SelectDataBean> selectDataBeans, SelectAreaListener selectAreaListener) {
        super(context);
        this.context = context;
        this.title = title;
        this.cityCode = cityCode;
        this.selectDataBeans = selectDataBeans;
        this.selectAreaListener = selectAreaListener;
        init();
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialgo_select_area;
    }

    private void init() {
        ImageView ivClose = mContentView.findViewById(R.id.iv_dialog_select_area_close);
        TextView tvTitle = mContentView.findViewById(R.id.tv_dialog_select_area_title);
        RecyclerView recyclerView = mContentView.findViewById(R.id.rv_dialog_select_area);
        tvTitle.setText(title);
        ivClose.setOnClickListener(v -> dismiss());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new CommonAdapter<OrderRankingBean.SelectDataBean>(context, selectDataBeans) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_select_area;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, OrderRankingBean.SelectDataBean selectDataBean, int position) {
                TextView textView = holder.obtainView(R.id.tv_item_rv_select_area);
                textView.setText(selectDataBean.getName());
                if (!TextUtils.isEmpty(cityCode) && cityCode.equals(selectDataBean.getCityCode())) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.color_while));
                    textView.setBackgroundResource(R.drawable.bg_corners30dp_textcolor5);
                } else {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.textColor6));
                    textView.setBackgroundResource(R.drawable.bg_corners30dp_textcolor9);
                }
                holder.itemView.setOnClickListener(v -> {
                    if (selectAreaListener != null) {
                        selectAreaListener.select(selectDataBean);
                        dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getWidth() {
        return (DensityUtil.getScreenWidth(mContext) - DensityUtil.dp2px(mContext.getResources().getDimension(R.dimen.dp_20)));
    }

    public interface SelectAreaListener {
        void select(OrderRankingBean.SelectDataBean selectDataBean);
    }
}
