package com.holike.crm.presenter.activity;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.FeedbackRecordBean;
import com.holike.crm.manager.FlowLayoutManager;
import com.holike.crm.model.activity.FeedbackRecordModel;
import com.holike.crm.view.activity.FeedbackRecordView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/7/17.
 * 反馈记录
 */

public class FeedbackRecordPresenter extends BasePresenter<FeedbackRecordView, FeedbackRecordModel> {

    private List<FeedbackRecordBean> mBeans = new ArrayList<>();
    private FeedbackRecordAdapter adapter;

    private class FeedbackRecordAdapter extends CommonAdapter<FeedbackRecordBean> {

        FeedbackRecordAdapter(Context context, List<FeedbackRecordBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_rv_feedback_record;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, FeedbackRecordBean bean, int position) {
            TextView tvNum = holder.obtainView(R.id.tv_item_rv_feedback_num);
            TextView tvDate = holder.obtainView(R.id.tv_item_rv_feedback_date);
            final TextView tvProblem = holder.obtainView(R.id.tv_item_rv_feedback_problem);
            TextView tvDescribe = holder.obtainView(R.id.tv_item_rv_feedback_describe);
            TextView tvLoss = holder.obtainView(R.id.tv_item_rv_feedback_loss);
            TextView tvSolution = holder.obtainView(R.id.tv_item_rv_feedback_solution);
            TextView tvReply = holder.obtainView(R.id.tv_item_rv_feedback_reply);
            RecyclerView rvProblem = holder.obtainView(R.id.rv_item_rv_feedback_problem);
            RecyclerView rvDescribe = holder.obtainView(R.id.rv_item_rv_feedback_describe);
            RecyclerView rvSolution = holder.obtainView(R.id.rv_item_rv_feedback_solution);
            LinearLayout llShow = holder.obtainView(R.id.ll_item_rv_feedback_show);
            ImageView ivShow = holder.obtainView(R.id.iv_item_rv_feedback_show);
            rvProblem.setNestedScrollingEnabled(false);
            rvDescribe.setNestedScrollingEnabled(false);
            rvSolution.setNestedScrollingEnabled(false);
            rvProblem.setLayoutManager(new FlowLayoutManager());
            rvDescribe.setLayoutManager(new GridLayoutManager(mContext, 3));
            rvSolution.setLayoutManager(new GridLayoutManager(mContext, 3));
            tvLoss.setVisibility(bean.isShow() ? View.VISIBLE : View.GONE);
            tvSolution.setVisibility(bean.isShow() ? View.VISIBLE : View.GONE);
            rvSolution.setVisibility(bean.isShow() ? View.VISIBLE : View.GONE);
            tvDescribe.setSingleLine(!bean.isShow());
            tvDescribe.setEllipsize(bean.isShow() ? null : TextUtils.TruncateAt.END);
            tvReply.setVisibility(TextUtils.isEmpty(bean.getAnswer()) ? View.GONE : View.VISIBLE);
            ivShow.setRotationX(bean.isShow() ? 180 : 0);
            String numText = mContext.getString(R.string.feedback_record_order);
            if (!TextUtils.isEmpty(bean.getParam1())) {
                numText += bean.getParam1();
            }
            tvNum.setText(numText);
            tvDate.setText(bean.getCreate_time());
            String problem = mContext.getString(R.string.feedback_record_classify);
            if (!TextUtils.isEmpty(bean.getQuestionName())) problem += bean.getQuestionName();
            tvProblem.setText(problem);
            String describe = mContext.getString(R.string.feedback_record_problem);
            if (!TextUtils.isEmpty(bean.getQuestionDescribe()))
                describe += bean.getQuestionDescribe();
            tvDescribe.setText(describe);
            String loss = mContext.getString(R.string.feedback_record_loss);
            if (!TextUtils.isEmpty(bean.getExpenseName())) loss += bean.getExpenseName();
            tvLoss.setText(loss);
            String solution = mContext.getString(R.string.feedback_record_deal);
            if (!TextUtils.isEmpty(bean.getSolveDescribe())) solution += bean.getSolveDescribe();
            tvSolution.setText(solution);
            String reply = mContext.getString(R.string.feedback_record_response);
            if (!TextUtils.isEmpty(bean.getAnswer())) reply += bean.getAnswer();
            tvReply.setText(reply);
            showProblemList(mContext, rvProblem, bean.getItemNameList());
            showImg(mContext, rvDescribe, bean.getQuestionPicture());
            showImg(mContext, rvSolution, bean.getSolvePicture());
            llShow.setOnClickListener(v -> {
                bean.setShow(!bean.isShow());
                notifyItemChanged(position);
//                notifyItemChanged(position - 1);
            });
        }

    }

    public void setAdapter(RecyclerView recyclerView) {
        adapter = new FeedbackRecordAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(adapter);
    }

    public void onRefreshCompleted(List<FeedbackRecordBean> beans) {
        this.mBeans.clear();
        onLoadMoreCompleted(beans);
    }

    public void onLoadMoreCompleted(List<FeedbackRecordBean> beans) {
        this.mBeans.addAll(beans);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取记录列表
     */
    public void getRecord(int pageNo) {
        final int pageSize = 10;
        model.getRecord(String.valueOf(pageNo), String.valueOf(pageNo), new FeedbackRecordModel.GetRecordListener() {
            @Override
            public void success(List<FeedbackRecordBean> list) {
                if (getView() != null)
                    getView().getRecordSuccess(list);
                if (list == null || list.size() < pageSize) {
                    if (getView() != null)
                        getView().loadAll();
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getRecordFailed(failed);
            }
        });
    }

    /**
     * 显示图片列表
     */
    private void showImg(final Context context, RecyclerView rv, final List<String> list) {
        rv.setAdapter(new CommonAdapter<String>(context, list) {
            @Override
            protected int bindView(int viewType) {
                return  R.layout.item_rv_feedback_record_img;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, String imgUrl, int position) {
                ImageView iv = holder.obtainView(R.id.iv_item_rv_feedback_record_img);
                Glide.with(context).load(imgUrl).apply(new RequestOptions().placeholder(R.drawable.loading_photo)).into(iv);
//                Glide.with(context).load(imgUrl).placeholder(R.drawable.loading_photo).into(iv);
                iv.setOnClickListener(v -> PhotoViewActivity.openImage((Activity) context, position, list));
            }
        });
    }

    /**
     * 显示问题列表
     */
    private void showProblemList(Context context, RecyclerView rv, List<String> list) {
        rv.setAdapter(new CommonAdapter<String>(context, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_feedback_problem2;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, String name, int position) {
                holder.setText(R.id.tv_item_rv_feedback_problem2, name);
            }
        });
    }
}
