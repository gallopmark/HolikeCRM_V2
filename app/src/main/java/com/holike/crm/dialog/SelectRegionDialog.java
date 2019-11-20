package com.holike.crm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.RegionBean;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.presenter.RegionPresenter;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.RegionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pony on 2019/8/2.
 * Copyright holike possess 2019.
 * 选择区域(省市区)
 */
public class SelectRegionDialog extends CommonDialog implements RegionView, DialogInterface.OnDismissListener {

    private List<Tab> mTabList;
    private TabAdapter mTabAdapter;
    private RegionPresenter mPresenter;
    private Dialog mLoadingDialog;
    private RecyclerView mRegionRv;
    private int mGetType;
    private int mSelectProvince = -1; //选择的省份位置
    private List<RegionBean> mProvinceDatas;
    private String mCurrentProvinceName, mCurrentProvinceCode,
            mCurrentCityName, mCurrentCityCode,
            mCurrentDistrictName, mCurrentDistrictCode;
    private OnRegionSelectedListener mListener;
    private Map<String, List<RegionBean>> mCityCache;
    private Map<String, Integer> mSelectCityCache;
    private Map<String, List<RegionBean>> mDistrictCache;
    private Map<String, Integer> mSelectDistrictCache;

    private String mEqualsText;

    public void setOnRegionSelectedListener(OnRegionSelectedListener listener) {
        this.mListener = listener;
    }

    public SelectRegionDialog(Context context) {
        super(context);
        mEqualsText = mContext.getString(R.string.tips_please_select);
        mTabList = new ArrayList<>();
        mCityCache = new HashMap<>();
        mSelectCityCache = new HashMap<>();
        mDistrictCache = new HashMap<>();
        mSelectDistrictCache = new HashMap<>();
        mPresenter = new RegionPresenter();
        mPresenter.attach(this);
        setOnDismissListener(this);
        setCanceledOnTouchOutside(false);
        setup();
    }

    private void setup() {
        LinearLayout contentLayout = mContentView.findViewById(R.id.ll_content_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
        params.bottomMargin = DensityUtil.getStatusHeight(mContext);
        mContentView.findViewById(R.id.view_outside).setOnClickListener(view -> hide());
        ImageView ivClose = mContentView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(view -> hide());
        RecyclerView rvTab = mContentView.findViewById(R.id.rv_tab);
        mTabList.add(new Tab(mEqualsText));
        mTabAdapter = new TabAdapter(mContext, mTabList);
        mTabAdapter.setOnItemClickListener((adapter, holder, view, position) -> setCurrentTab(position));
        rvTab.setAdapter(mTabAdapter);
        mRegionRv = mContentView.findViewById(R.id.rv_region);
    }

    private void setCurrentTab(int position) {
        if (position == 0) {
            updateProvince();
        } else if (position == 1) {
            updateCityLevel();
        } else {
            updateDistrictLevel();
        }
        mTabAdapter.setSelectPosition(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        showLoading();
        mGetType = 1;
        mPresenter.getProvince();
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.show();
    }

    private void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    private void getChildRegion(int getType, String regionCode, int regionLevel) {
        this.mGetType = getType;
        showLoading();
        mPresenter.getChildRegion(regionCode, String.valueOf(regionLevel));
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
    protected int bindContentView() {
        return R.layout.dialog_select_region;
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
    public void onSuccess(List<RegionBean> list) {
        hideLoading();
        if (mGetType == 1) {  //获取省份成功
            if (list != null && !list.isEmpty()) {
                mProvinceDatas = new ArrayList<>(list);
                updateProvince();
            }
        } else if (mGetType == 2) { //获取市成功
            if (list == null || list.isEmpty()) {  //选择香港、澳门、台湾等  没有市区
                onSelected();
            } else {
                mCityCache.put(mCurrentProvinceCode, list);
                updateCity(list);
            }
        } else {    //获取区县
            if (list == null || list.isEmpty()) {
                onSelected();
            } else {
                mDistrictCache.put(mCurrentCityCode, list);
                updateDistrict(list);
            }
        }
    }

    private void updateProvince() {
        if (mProvinceDatas == null || mProvinceDatas.isEmpty()) return;
        RegionAdapter regionAdapter = new RegionAdapter(mContext, mProvinceDatas, mSelectProvince);
        mRegionRv.setAdapter(regionAdapter);
        regionAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            regionAdapter.setSelectPosition(position);
            setSelectProvince(position, mProvinceDatas);
        });
    }

    private void setSelectProvince(int position, final List<RegionBean> list) {
        if (list.isEmpty() || position < 0 || position > list.size()) return;
        mSelectProvince = position;
        String regionCode = list.get(position).regionCode;
        for (int i = 0; i < mTabList.size(); i++) {
            if (i > 0) {
                mTabList.get(i).title = mEqualsText;
            }
        }
        mTabList.get(0).title = list.get(position).regionName;
        if (mTabList.size() >= 2) {
            if (mTabList.size() == 3) {
                mTabList.remove(2);
            }
            mTabList.remove(1);
            mTabAdapter.notifyDataSetChanged();
        }
        resetAll();
        mCurrentProvinceCode = regionCode;
        mCurrentProvinceName = list.get(position).regionName;
        updateCityLevel();
    }

    private void resetAll() {
        resetCity();
        resetDistrict();
    }

    /*选择省份时重置城市值*/
    private void resetCity() {
        mCurrentCityCode = null;
        mCurrentCityName = null;
    }

    /*选择省份或市时重置区县值*/
    private void resetDistrict() {
        mCurrentDistrictCode = null;
        mCurrentDistrictName = null;
    }

    private void updateCityLevel() {
        List<RegionBean> list = mCityCache.get(mCurrentProvinceCode);
        if (list != null) {
            updateCity(list);
        } else {
            getChildRegion(2, mCurrentProvinceCode, 2);
        }
    }

    private void updateCity(final List<RegionBean> list) {
        if (list == null || list.isEmpty()) return;
        if (mTabList.size() == 1) {
            mTabList.add(new Tab(mEqualsText));
        }
        mTabAdapter.setSelectPosition(1);
        int selectPosition = -1;
        Integer index = mSelectCityCache.get(mCurrentProvinceCode);
        if (index != null) {
            selectPosition = index;
        }
        RegionAdapter regionAdapter = new RegionAdapter(mContext, list, selectPosition);
        mRegionRv.setAdapter(regionAdapter);
        regionAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            regionAdapter.setSelectPosition(position);
            setSelectCity(position, list);
        });
    }

    private void setSelectCity(int position, final List<RegionBean> list) {
        if (list.isEmpty() || position < 0 || position > list.size()) return;
        mSelectCityCache.put(mCurrentProvinceCode, position);
        String regionCode = list.get(position).regionCode;
        if (mTabList.size() == 3) {
            mTabList.get(2).title = mEqualsText;
        }
        mTabList.get(1).title = list.get(position).regionName;
        resetAll();
        mCurrentCityCode = regionCode;
        mCurrentCityName = list.get(position).regionName;
        updateDistrictLevel();
    }

    private void updateDistrictLevel() {
        List<RegionBean> list = mDistrictCache.get(mCurrentCityCode);
        if (list != null) {
            updateDistrict(list);
        } else {
            getChildRegion(3, mCurrentCityCode, 3);
        }
    }

    private void updateDistrict(final List<RegionBean> list) {
        if (list == null || list.isEmpty()) return;
        if (mTabList.size() == 2) {
            mTabList.add(new Tab(mEqualsText));
        }
        mTabAdapter.setSelectPosition(2);
        int selectPosition = -1;
        Integer index = mSelectDistrictCache.get(mCurrentCityCode);
        if (index != null) {
            selectPosition = index;
        }
        final RegionAdapter regionAdapter = new RegionAdapter(mContext, list, selectPosition);
        mRegionRv.setAdapter(regionAdapter);
        regionAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            regionAdapter.setSelectPosition(position);
            setSelectDistrict(position, list);
            onSelected();
        });
    }

    private void setSelectDistrict(int position, List<RegionBean> list) {
        mTabList.get(2).title = list.get(position).regionName;
        mTabAdapter.notifyDataSetChanged();
        mSelectDistrictCache.put(mCurrentCityCode, position);
        mCurrentDistrictCode = list.get(position).regionCode;
        mCurrentDistrictName = list.get(position).regionName;
    }

    private void onSelected() {
        if (mListener != null) {
            String address = mCurrentProvinceName;
            if (!TextUtils.isEmpty(mCurrentCityName)) {
                address += "\u3000" + mCurrentCityName;
            }
            if (!TextUtils.isEmpty(mCurrentDistrictName)) {
                address += "\u3000" + mCurrentDistrictName;
            }
            mListener.onRegionSelected(mCurrentProvinceName, mCurrentProvinceCode, mCurrentCityName,
                    mCurrentCityCode, mCurrentDistrictName, mCurrentDistrictCode, address);
        }
        hide();
    }

    @Override
    public void onFailure(String failReason) {
        hideLoading();
        AppToastCompat.makeText(mContext, failReason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mPresenter.deAttach();
    }

    @Override
    public void onBackPressed() {
        hide();
    }

    class Tab {
        String title;

        Tab(String title) {
            this.title = title;
        }
    }

    class TabAdapter extends CommonAdapter<Tab> {

        int mSelectPosition = 0;

        TabAdapter(Context context, List<Tab> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.dialog_item_tab;
        }

        void setSelectPosition(int position) {
            this.mSelectPosition = position;
            notifyDataSetChanged();
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, Tab tab, int position) {
            if (position == mSelectPosition) {
                holder.setVisibility(R.id.v_tab, View.VISIBLE);
            } else {
                holder.setVisibility(R.id.v_tab, View.INVISIBLE);
            }
            if (!TextUtils.equals(tab.title, mContext.getString(R.string.tips_please_select))) {
                holder.setTypeface(R.id.tv_tab, Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.setTypeface(R.id.tv_tab, Typeface.defaultFromStyle(Typeface.NORMAL));
            }
            holder.setText(R.id.tv_tab, tab.title);
        }
    }

    class RegionAdapter extends CommonAdapter<RegionBean> {

        Drawable mDrawableLeft;
        int mSelectPosition;

        RegionAdapter(Context context, List<RegionBean> mDatas, int selectPosition) {
            super(context, mDatas);
            mDrawableLeft = ContextCompat.getDrawable(mContext, R.drawable.ic_check_24dp);
            this.mSelectPosition = selectPosition;
        }

        void setSelectPosition(int selectPosition) {
            this.mSelectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.dialog_item_region;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, RegionBean bean, int position) {
            holder.setText(R.id.tv_region, bean.regionName);
            if (position == mSelectPosition) {
                holder.setDrawableLeft(R.id.tv_region, mDrawableLeft);
            } else {
                holder.setDrawableLeft(R.id.tv_region, null);
            }
        }
    }

    public interface OnRegionSelectedListener {
        void onRegionSelected(String provinceName, String provinceCode, String cityName,
                              String cityCode, String districtName, String districtCode,
                              String address);
    }
}
