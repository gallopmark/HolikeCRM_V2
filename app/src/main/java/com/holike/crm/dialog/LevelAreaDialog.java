package com.holike.crm.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.EvaluateTypeBean;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.presenter.GeneralReportPresenter;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.GeneralReportView;

import java.util.ArrayList;
import java.util.List;

import galloped.xcode.widget.TitleBar;

/**
 * Created by pony on 2019/10/23.
 * Copyright holike possess 2019.
 * 三级区域选择对话框
 */
public class LevelAreaDialog extends CommonDialog implements GeneralReportView, DialogInterface.OnDismissListener {

    private OnAreaSelectListener mListener;
    private boolean mRequired = false;  //是否要选到第三级才能点确定
    private GeneralReportPresenter mPresenter;
    private String mType = "-1", mCityCode;
    private int mRequestType;
    private LevelAreaAdapter mLevelOneAdapter, mLevelTwoAdapter, mLevelThreeAdapter;

    public LevelAreaDialog(Context context) {
        super(context);
        mPresenter = new GeneralReportPresenter();
        mPresenter.attach(this);
        setOnDismissListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_level_area;
    }

    @Override
    protected void initView(View contentView) {
        LinearLayout contentLayout = contentView.findViewById(R.id.ll_content_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
        params.height = mContext.getResources().getDimensionPixelSize(R.dimen.dp_20)
                + mContext.getResources().getDimensionPixelSize(R.dimen.dp_36) * 5;
        params.bottomMargin = DensityUtil.getStatusHeight(mContext);
        contentView.findViewById(R.id.view_outside).setOnClickListener(view -> hide());
        RecyclerView levelOneRv = contentView.findViewById(R.id.rv_level_one);
        RecyclerView levelTwoRv = contentView.findViewById(R.id.rv_level_two);
        RecyclerView levelThreeRv = contentView.findViewById(R.id.rv_level_three);
        mLevelOneAdapter = new LevelAreaAdapter(mContext, new ArrayList<>());
        levelOneRv.setAdapter(mLevelOneAdapter);
        mLevelTwoAdapter = new LevelAreaAdapter(mContext, new ArrayList<>());
        levelTwoRv.setAdapter(mLevelTwoAdapter);
        mLevelThreeAdapter = new LevelAreaAdapter(mContext, new ArrayList<>());
        levelThreeRv.setAdapter(mLevelThreeAdapter);
        TextView tvNegative = contentView.findViewById(R.id.tv_negative);
        tvNegative.setOnClickListener(view -> hide());
        TextView tvPositive = contentView.findViewById(R.id.tv_positive);
        tvPositive.setOnClickListener(view -> {
            EvaluateTypeBean bean = mLevelOneAdapter.getSelectItem();
            if (mListener != null && bean != null) {
                String name = bean.getName();
                String type = bean.type;
                String cityCode = bean.cityCode;
                EvaluateTypeBean bean2 = mLevelTwoAdapter.getSelectItem();
                boolean isSelectDealer = false;
                if (bean2 != null) {
                    name += (">" + bean2.getName());
                    type = bean2.type;
                    cityCode = bean2.cityCode;
                    EvaluateTypeBean bean3 = mLevelThreeAdapter.getSelectItem();
                    if (bean3 != null) {
                        name += (">" + bean3.getName());
                        type = bean3.type;
                        cityCode = bean3.cityCode;
                        isSelectDealer = true;
                    }
                }
                if (mRequired && !isSelectDealer) {
                    mListener.onDismissDealer();
                } else {
                    hide();
                    mListener.onAreaSelected(name, type, cityCode, isSelectDealer);
                }
            }
        });
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(mContext.getString(titleId));
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        ((TitleBar) mContentView.findViewById(R.id.titleBar)).setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(1);
    }

    private void initData(int requestType) {
        mRequestType = requestType;
        mPresenter.getEvaluateType(mType, mCityCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object obj) {
        List<EvaluateTypeBean> dataList = (List<EvaluateTypeBean>) obj;
        if (mRequestType == 1) {
            setLevelOne(dataList);
        } else if (mRequestType == 2) {
            setLevelTwo(dataList);
        } else if (mRequestType == 3) {
            setLevelThree(dataList);
        }
    }

    private void setLevelOne(List<EvaluateTypeBean> dataList) {
        mLevelOneAdapter.addAll(dataList);
        mLevelOneAdapter.setOnItemSelectListener((bean, position) -> {
            mType = bean.type;
            mCityCode = bean.cityCode;
            clearData();
            initData(2);
        });
    }

    private void clearData() {
        mLevelTwoAdapter.clear();
        mLevelThreeAdapter.clear();
    }

    private void setLevelTwo(List<EvaluateTypeBean> dataList) {
        mLevelTwoAdapter.addAll(dataList);
        mLevelTwoAdapter.setOnItemSelectListener((bean, position) -> {
            mType = bean.type;
            mCityCode = bean.cityCode;
            mLevelThreeAdapter.clear();
            initData(3);
        });
    }

    private void setLevelThree(List<EvaluateTypeBean> dataList) {
        mLevelThreeAdapter.addAll(dataList);
        mLevelThreeAdapter.setOnItemSelectListener((bean, position) -> {
            mType = bean.type;
            mCityCode = bean.cityCode;
        });
    }

    @Override
    public void onFailure(String failReason) {
        AppToastCompat.makeText(mContext.getApplicationContext(), failReason, Toast.LENGTH_SHORT).show();
    }

    public LevelAreaDialog setRequired(boolean isRequired) {
        this.mRequired = isRequired;
        return this;
    }

    public LevelAreaDialog setOnAreaSelectListener(OnAreaSelectListener listener) {
        this.mListener = listener;
        return this;
    }

    @Nullable
    @Override
    public ViewGroup.MarginLayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DensityUtil.getScreenHeight(mContext));
    }

    @Override
    protected boolean fullWidth() {
        return true;
    }

    @Override
    public int getWindowAnimations() {
        return R.style.Dialog_Anim;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public void onBackPressed() {
        hide();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mPresenter.deAttach();
    }

    public interface OnAreaSelectListener {
        void onAreaSelected(String name, String type, String cityCode, boolean isSelectDealer);

        void onDismissDealer();
    }

    private static class LevelAreaAdapter extends CommonAdapter<EvaluateTypeBean> {
        OnItemSelectListener mListener;
        int mSelectPosition = -1;

        LevelAreaAdapter(Context context, List<EvaluateTypeBean> mDatas) {
            super(context, mDatas);
        }

        void clear() {
            mDatas.clear();
            setSelectPosition(-1);
        }

        void addAll(List<EvaluateTypeBean> dataList) {
            mDatas.clear();
            mDatas.addAll(dataList);
            notifyDataSetChanged();
        }

        void setSelectPosition(int position) {
            mSelectPosition = position;
            notifyDataSetChanged();
        }

        @Nullable
        EvaluateTypeBean getSelectItem() {
            if (mSelectPosition >= 0 && mSelectPosition < mDatas.size()) {
                return mDatas.get(mSelectPosition);
            }
            return null;
        }

        void setOnItemSelectListener(OnItemSelectListener listener) {
            this.mListener = listener;
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.dialog_item_level_area;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, final EvaluateTypeBean bean, final int position) {
            holder.setText(R.id.tv_area_name, bean.name);
            holder.itemView.setSelected(mSelectPosition == position);
            holder.itemView.setOnClickListener(view -> {
                if (mSelectPosition != position) {
                    setSelectPosition(position);
                    if (mListener != null) {
                        mListener.onItemSelect(bean, position);
                    }
                }
            });
        }

        interface OnItemSelectListener {
            void onItemSelect(EvaluateTypeBean bean, int position);
        }
    }
}
