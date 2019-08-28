package com.holike.crm.activity.message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.adapterhelper.SuperRecyclerAdapter;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.MessageDetailsBean;
import com.holike.crm.presenter.activity.MessageDetailsPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.MessageDetailsView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 消息详情
 */
public class MessageDetailsActivity extends MyFragmentActivity<MessageDetailsPresenter, MessageDetailsView> implements MessageDetailsView {
    @BindView(R.id.mRecyclerView)
    WrapperRecyclerView mRecyclerView;
    private View mHeaderView;
    private List<String> mPictures;
    private ImageAdapter mAdapter;

    /**
     * 打开公告详情
     */
    public static void open(BaseFragment<?, ?> fragment, String messageId, int requestCode) {
        if (fragment.getActivity() == null) return;
        MobclickAgent.onEvent(fragment.getActivity(), "message_announce_details");
        Intent intent = new Intent(fragment.getActivity(), MessageDetailsActivity.class);
        intent.putExtra(Constants.MESSAGE_ID, messageId);
        fragment.openActivityForResult(intent, requestCode, null);
    }

    @Override
    protected MessageDetailsPresenter attachPresenter() {
        return new MessageDetailsPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_message_details;
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.message_detail_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mHeaderView = getLayoutInflater().inflate(R.layout.header_message_detail, new LinearLayout(this), false);
        mRecyclerView.addHeaderView(mHeaderView);
        mPictures = new ArrayList<>();
        mAdapter = new ImageAdapter(this, mPictures);
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        showLoading();
        mPresenter.getMessageDetails(getIntent().getStringExtra(Constants.MESSAGE_ID));
    }

    /**
     * 获取消息详情成功
     */
    @Override
    public void getMessageDetailsSuccess(MessageDetailsBean bean) {
        dismissLoading();
        mRecyclerView.setVisibility(View.VISIBLE);
        TextView tvTitle = mHeaderView.findViewById(R.id.tv_message_title);
        tvTitle.setText(bean.getTitle());
        TextView tvDate = mHeaderView.findViewById(R.id.tv_message_date);
        tvDate.setText(bean.getCreateDate());
        TextView tvSender = mHeaderView.findViewById(R.id.tv_message_sender);
        String publisher = getString(R.string.message_publisher);
        if (!TextUtils.isEmpty(bean.getName()))
            publisher += bean.getName();
        tvSender.setText(publisher);
        showImages(bean.getPictureList());
    }

    /**
     * 显示图片
     */
    private void showImages(List<String> images) {
        if (images != null && !images.isEmpty()) {
            this.mPictures.addAll(images);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取消息详情失败
     */
    @Override
    public void getMessageDetailsFailed(String failed) {
        dismissLoading();
        mRecyclerView.setVisibility(View.GONE);
        dealWithFailed(failed, true);
    }

    private class ImageAdapter extends SuperRecyclerAdapter<String> {

        ImageAdapter(Context context, List<String> mDatas) {
            super(context, mDatas);
        }

        @NonNull
        @Override
        protected View bindItemView(int viewType, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            return imageView;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, String url, int position) {
            Glide.with(mContext).load(Uri.parse(url)).into((ImageView) holder.itemView);
        }
    }
}
