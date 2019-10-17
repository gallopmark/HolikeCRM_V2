package com.holike.crm.fragment.main.child;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*首页功能菜单*/
public class HomeMenuFragment extends MyFragment {
    @BindView(R.id.menuRecyclerView)
    RecyclerView mMenuRecyclerView;
    @BindView(R.id.menuProgressLayout)
    FrameLayout mMenuProgressLayout;
    @BindView(R.id.progressView)
    View mProgressView;
    private List<HomepageBean.NewDataBean.CreditItem> mItems = new ArrayList<>();
    private MenuAdapter mAdapter;
    private OnMenuClickListener mOnMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.mOnMenuClickListener = onMenuClickListener;
    }

    private class MenuAdapter extends CommonAdapter<HomepageBean.NewDataBean.CreditItem> {

        int mItemWidth;
        MenuAdapter(Context context, List<HomepageBean.NewDataBean.CreditItem> mDatas) {
            super(context, mDatas);
            mItemWidth = ((DensityUtil.getScreenWidth(mContext) - (int) (2 * mContext.getResources().getDimension(R.dimen.dp_12))) / 4);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, HomepageBean.NewDataBean.CreditItem item, int position) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.width = mItemWidth;
            holder.itemView.setLayoutParams(params);
            holder.setText(R.id.tv_operate_name, item.getName());
            Glide.with(mContext).load(item.getIcon()).into((ImageView) holder.obtainView(R.id.iv_operate_icon));
            holder.itemView.setOnClickListener(v -> {
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onMenuClick(item.getType(), item.getName());
                }
            });
        }
        @Override
        protected int bindView(int viewType) {
            return R.layout.item_homepage_operate;
        }
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_homepage_menu;
    }

    @Override
    protected void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mMenuRecyclerView.setLayoutManager(layoutManager);
        mMenuRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new MenuAdapter(mContext, mItems);
        mMenuRecyclerView.setAdapter(mAdapter);
    }

    public void update(@NonNull List<HomepageBean.NewDataBean.CreditItem> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        mAdapter.notifyDataSetChanged();
        updateProgress();
    }

    private void updateProgress() {
        int size = this.mItems.size();
        if (size > 4) {
            int totalWidth = mContext.getResources().getDimensionPixelSize(R.dimen.dp_50);
            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) mMenuProgressLayout.getLayoutParams();
            llParams.width = totalWidth;
            mMenuProgressLayout.setLayoutParams(llParams);
            mMenuProgressLayout.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mProgressView.getLayoutParams();
            params.width = (int) (totalWidth * ((float) 4 / 5));
            mProgressView.setLayoutParams(params);
            int canScrollX = totalWidth - params.width;
            mMenuRecyclerView.clearOnScrollListeners();
            mMenuRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int position = layoutManager.findLastCompletelyVisibleItemPosition();
                        if (position >= 0 && position < size) {
                            if (position == size - 1) {
                                mProgressView.setTranslationX(canScrollX);
                            } else {
                                int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
                                if (pos == 0) {
                                    mProgressView.setTranslationX(0);
                                } else {
                                    float x = (float) position / size;
                                    mProgressView.setTranslationX(canScrollX * x);
                                }
                            }
                        }
                    }
                }
            });
        } else {
            mMenuProgressLayout.setVisibility(View.GONE);
        }
    }

    public interface OnMenuClickListener {
        void onMenuClick(String type, String name);
    }
}
