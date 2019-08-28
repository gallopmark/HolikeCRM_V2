package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.decorationhepler.LinearItemDecoration;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.adapter.SingleChoiceAdapter;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.manager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/7/24.
 * Copyright holike possess 2019.
 * 主管查房帮助类
 */
public class SupervisorRoundsHelper extends GeneralHelper {
    private BaseActivity<?, ?> mActivity;
    private Callback mCallback;
    private WrapperRecyclerView mRecyclerView;
    private View mFooterView;
    private EditText mRemarkEditText;
    private List<String> mTotalPictures;
    private List<String> mDataImages;
    private ImageAdapter mAdapter;
    private static final int DEFAULT_SIZE = 9;

    private String mResult, mResultName; //查房结果

    public SupervisorRoundsHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mActivity = (BaseActivity<?, ?>) fragment.getActivity();
        this.mCallback = callback;
        mTotalPictures = new ArrayList<>();
        mDataImages = new ArrayList<>();
        mAdapter = new ImageAdapter(mActivity);
        initView(fragment.getContentView());
        setBundleValue(fragment.getArguments());
        onLayout();
        SysCodeItemBean systemCode = IntentValue.getInstance().getSystemCode();
        if (systemCode == null) {
            mCallback.onQuerySystemCode();
        } else {
            initFooterView(systemCode);
        }
    }

    private void initView(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.rv_pictures);
        TextView saveTextView = contentView.findViewById(R.id.tvSave);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mFooterView = LayoutInflater.from(mActivity).inflate(R.layout.footer_customer_supervisorrounds, mRecyclerView, false);
        mRemarkEditText = mFooterView.findViewById(R.id.et_remark);
        mRecyclerView.addFooterView(mFooterView);
        mRecyclerView.setAdapter(mAdapter);
        saveTextView.setOnClickListener(view -> onSaved());
    }

    private void setBundleValue(Bundle bundle) {
        if (bundle != null) {
            ArrayList<String> schemeImages = bundle.getStringArrayList("images");
            if (schemeImages != null && !schemeImages.isEmpty()) {
                mTotalPictures.addAll(schemeImages);
                if (mTotalPictures.size() > 9) {
                    mDataImages.addAll(mTotalPictures.subList(0, DEFAULT_SIZE));
                } else {
                    mDataImages.addAll(mTotalPictures);
                }
//                mAdapter.notifyDataSetChanged();
                mAdapter.addImages(mDataImages, true);
            }
            mResult = bundle.getString("result");
            mResultName = bundle.getString("resultName");
            mRemarkEditText.setText(bundle.getString("remark"));
        }
    }

    private void onLayout() {
        View expandLayout = mFooterView.findViewById(R.id.fl_expand_layout);
        if (mTotalPictures.size() > 0) {
            View headerView = LayoutInflater.from(mActivity).inflate(R.layout.header_customer_supervisorrounds, mRecyclerView, false);
            mRecyclerView.addHeaderView(headerView);
            if (mTotalPictures.size() > DEFAULT_SIZE) {
                expandLayout.setVisibility(View.VISIBLE);
                expandLayout.findViewById(R.id.tv_expand).setOnClickListener(new View.OnClickListener() {
                    private boolean isExpanded;
                    private Drawable mArrowDown = ContextCompat.getDrawable(mActivity, R.drawable.ic_arrow_down_accent);
                    private Drawable mArrowUp = ContextCompat.getDrawable(mActivity, R.drawable.ic_arrow_up_accent);

                    @Override
                    public void onClick(View view) {
                        TextView tvExpand = (TextView) view;
                        if (isExpanded) {
                            mDataImages.clear();
                            mDataImages.addAll(mTotalPictures.subList(0, DEFAULT_SIZE));
                            mAdapter.addImages(mDataImages, true);
//                        mAdapter.notifyDataSetChanged();
                            tvExpand.setText(mActivity.getString(R.string.click_to_expand));
                            tvExpand.setCompoundDrawablesWithIntrinsicBounds(null, null, mArrowDown, null);
                            isExpanded = false;
                        } else {
                            mDataImages.addAll(mTotalPictures.subList(DEFAULT_SIZE, mTotalPictures.size()));
                            mAdapter.notifyDataSetChanged();
                            mAdapter.addImages(mDataImages, false);
//                        mAdapter.notifyDataSetChanged();
                            tvExpand.setText(mActivity.getString(R.string.click_to_fold));
                            tvExpand.setCompoundDrawablesWithIntrinsicBounds(null, null, mArrowUp, null);
                            isExpanded = true;
                        }
                    }
                });
            }
        } else {
            expandLayout.setVisibility(View.GONE);
        }
    }

    public void setSystemCode(SysCodeItemBean bean) {
        initFooterView(bean);
    }

    private void initFooterView(SysCodeItemBean bean) {
        RecyclerView rvSelect = mFooterView.findViewById(R.id.rv_select);
        rvSelect.setLayoutManager(new FlowLayoutManager());
        List<DictionaryBean> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : bean.getReviewHousePlan().entrySet()) {
            list.add(new DictionaryBean(entry.getKey(), entry.getValue()));
        }
        SingleChoiceAdapter adapter = new SingleChoiceAdapter(mActivity, list, list.indexOf(new DictionaryBean(mResult, mResultName)));
        adapter.setSingleChoiceListener(bean1 -> mResult = bean1.id);
        rvSelect.setAdapter(adapter);
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        private Context mContext;
        SparseArray<List<String>> mData;

        int mDp10;

        ImageAdapter(Context context) {
            this.mContext = context;
            mDp10 = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
            mData = new SparseArray<>();
        }

        void addImages(List<String> images, boolean isClear) {
            if (isClear) {
                mData.clear();
            }
            int splitCount = 3;
            int totalCount = images.size();
            int m = totalCount % splitCount;
            int lineCount;
            if (m > 0) {
                lineCount = totalCount / splitCount + 1;
            } else {
                lineCount = totalCount / splitCount;
            }
            for (int i = 0; i < lineCount; i++) {
                List<String> list;
                if (m == 0) {
                    list = images.subList(i * splitCount, splitCount * (i + 1));
                } else {
                    if (i == lineCount - 1) {
                        list = images.subList(i * splitCount, totalCount);
                    } else {
                        list = images.subList(i * splitCount, splitCount * (i + 1));
                    }
                }
                mData.put(i, list);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image_grid_rv, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mRv.setLayoutManager(new GridLayoutManager(mContext, 3));
            if (holder.tag == null) {
                LinearItemDecoration itemDecoration = new LinearItemDecoration(RecyclerView.HORIZONTAL, mDp10,
                        ContextCompat.getColor(mContext, R.color.color_while));
                holder.mRv.addItemDecoration(itemDecoration);
                holder.tag = itemDecoration;
            }
            holder.mRv.setAdapter(new ImageGridAdapter(mContext, mData.get(position)));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView mRv;
            Object tag;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                mRv = itemView.findViewById(R.id.rv);
                mRv.setNestedScrollingEnabled(false);
            }
        }
    }

    class ImageGridAdapter extends CommonAdapter<String> {

        ImageGridAdapter(Context context, List<String> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_image_grid;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, final String s, int position) {
            ImageView iv = holder.obtainView(R.id.iv_image);
            Glide.with(mContext).load(s).apply(new RequestOptions().error(0).placeholder(R.drawable.loading_photo).centerCrop()).into(iv);
            holder.itemView.setOnClickListener(view -> {
                int index = 0;
                if (mDataImages.indexOf(s) >= 0) {
                    index = mDataImages.indexOf(s);
                }
                PhotoViewActivity.openImage(mActivity, index, mTotalPictures);
            });
        }
    }

    private void onSaved() {
        if (TextUtils.isEmpty(mResult)) {
            showToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.followup_house_result2));
        } else {
            mCallback.onSaved(ParamHelper.Customer.supervisorRounds(mHouseId, mResult, mRemarkEditText.getText().toString()));
        }
    }

    public interface Callback {
        void onQuerySystemCode();

        void onSaved(String body);
    }
}
