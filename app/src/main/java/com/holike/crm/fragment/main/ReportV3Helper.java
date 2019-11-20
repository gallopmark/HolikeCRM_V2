package com.holike.crm.fragment.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.report.ReportGridActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.bean.ReportV3IconBean;
import com.holike.crm.helper.ReportGridItemClickHelper;

import java.util.List;

class ReportV3Helper extends FragmentHelper {

    private FrameLayout mContainer;

    ReportV3Helper(BaseFragment<?, ?> fragment) {
        super(fragment);
        mContainer = obtainView(R.id.fl_container);
    }

    void onSuccess(List<ReportV3IconBean> dataList) {
        mContainer.removeAllViews();
        if (dataList == null || dataList.isEmpty()) {
            noAuthority();
        } else {
            requestUpdate(dataList);
        }
    }

    private void requestUpdate(List<ReportV3IconBean> dataList) {
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mContainer.addView(recyclerView);
        recyclerView.setAdapter(new GridAdapter(mContext, dataList));
    }

    private final class GridAdapter extends CommonAdapter<ReportV3IconBean> {

        GridAdapter(Context context, List<ReportV3IconBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_reportv3_grid;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, ReportV3IconBean bean, int position) {
            ImageView iv = holder.obtainView(R.id.iv_icon);
            TextView tv = holder.obtainView(R.id.tv_title);
            Glide.with(mContext).load(bean.imageUrl).apply(new RequestOptions().placeholder(R.drawable.analysis_default).error(R.drawable.analysis_default)).into(iv);
            tv.setText(bean.title);
            holder.itemView.setOnClickListener(v -> onItemClick(bean));
        }
    }

    private void onItemClick(ReportV3IconBean bean) {
        if (bean.getType() == -1) {
            ReportGridActivity.startGrid((BaseActivity<?, ?>) mContext, bean.title, bean.getItemMap());
        } else if (bean.getType() == -2) {
            ReportGridActivity.startMulti((BaseActivity<?, ?>) mContext, bean.title, bean.getArrIconBig());
        } else {
            ReportGridItemClickHelper.dealWith((BaseActivity<?, ?>) mContext, bean.getType(), bean.title);
        }
    }

    void onFailure(String failReason) {
        mContainer.removeAllViews();
        if (isNoAuth(failReason)) {
            noAuthority();
        } else {
            LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainer, true);
            mFragment.noNetwork(failReason);
        }
    }

    private boolean isNoAuth(String failed) {
        return TextUtils.equals(failed, mContext.getString(R.string.noAuthority))
                || TextUtils.equals(failed, mContext.getString(R.string.tips_nopermissions));
    }

    /*无权限*/
    private void noAuthority() {
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainer, true);
        mFragment.noAuthority();
    }
}
