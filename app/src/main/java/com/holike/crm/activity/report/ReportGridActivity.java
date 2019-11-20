package com.holike.crm.activity.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.ReportV3IconBean;
import com.holike.crm.helper.ReportGridItemClickHelper;
import com.holike.crm.http.MyJsonParser;

import java.util.List;

/**
 * Created by pony on 2019/10/31.
 * Version v3.0 app报表
 * 报表分类
 */
public class ReportGridActivity extends MyFragmentActivity {

    private static final String TYPE_GRID = "type-grid";
    private static final String TYPE_MULTI = "type-multi";

    public static void startGrid(BaseActivity<?, ?> activity, String title, List<ReportV3IconBean.IconBean> dataList) {
        Intent intent = new Intent(activity, ReportGridActivity.class);
        intent.setType(TYPE_GRID);
        intent.putExtra("title", title);
        IntentValue.getInstance().put("data-list-json", MyJsonParser.fromBeanToJson(dataList));
        activity.openActivity(intent);
    }

    public static void startMulti(BaseActivity<?, ?> activity, String title, List<ReportV3IconBean.ArrIconBigBean> dataList) {
        Intent intent = new Intent(activity, ReportGridActivity.class);
        intent.setType(TYPE_MULTI);
        intent.putExtra("title", title);
        IntentValue.getInstance().put("data-list-json", MyJsonParser.fromBeanToJson(dataList));
        activity.openActivity(intent);
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_report_grid;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            setTitle(R.string.report_title);
        } else {
            setTitle(title);
        }
        String type = getIntent().getType();
        RecyclerView recyclerView = findViewById(R.id.rv_grid);
        if (TextUtils.equals(type, TYPE_GRID)) {
            String json = (String) IntentValue.getInstance().removeBy("data-list-json");
            List<ReportV3IconBean.IconBean> dataList = MyJsonParser.fromJsonToList(json, ReportV3IconBean.IconBean.class);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(new GridAdapter(this, dataList));
        } else if (TextUtils.equals(type, TYPE_MULTI)) {
            String json = (String) IntentValue.getInstance().removeBy("data-list-json");
            List<ReportV3IconBean.ArrIconBigBean> dataList = MyJsonParser.fromJsonToList(json, ReportV3IconBean.ArrIconBigBean.class);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MultiGridAdapter(this, dataList));
        }
    }

    private final class GridAdapter extends CommonAdapter<ReportV3IconBean.IconBean> {

        GridAdapter(Context context, List<ReportV3IconBean.IconBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_reportv3_grid;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, ReportV3IconBean.IconBean bean, int position) {
            ImageView iv = holder.obtainView(R.id.iv_icon);
            TextView tv = holder.obtainView(R.id.tv_title);
            Glide.with(mContext).load(bean.imageUrl).apply(new RequestOptions().placeholder(R.drawable.analysis_default)
                    .error(R.drawable.analysis_default)).into(iv);
            tv.setText(bean.title);
            holder.itemView.setOnClickListener(v -> ReportGridItemClickHelper.dealWith((BaseActivity<?, ?>) mContext, bean.getType(), bean.title));
        }
    }

    private final class MultiGridAdapter extends CommonAdapter<ReportV3IconBean.ArrIconBigBean> {

        MultiGridAdapter(Context context, List<ReportV3IconBean.ArrIconBigBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_reportv3_multi_grid;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, ReportV3IconBean.ArrIconBigBean bean, int position) {
            holder.setText(R.id.tv_title, bean.title);
            RecyclerView recyclerView = holder.obtainView(R.id.rv_grid);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(new GridAdapter(mContext, bean.getArrIcon()));
        }
    }
}
