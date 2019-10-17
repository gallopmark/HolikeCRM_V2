package com.holike.crm.fragment.satisfaction;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.bean.CustomerSatisfactionBean;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.helper.TextSpanHelper;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*客户满意度帮助类*/
class SatisfactionHelper extends FragmentHelper {

    private Callback mCallback;
    private String mType;
    private String mCityCode;
    private Date mDatetime;
    private FrameLayout mContainerLayout;
    private boolean mAnimation;

    SatisfactionHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        initView();
        obtainValue();
        long delayMillis = 0;
        if (mAnimation) {
            delayMillis = 300;
        }
        mContainerLayout.postDelayed(mRequestRun, delayMillis);
    }

    private Runnable mRequestRun = this::doRequest;

    private void initView() {
        mContainerLayout = mFragmentView.findViewById(R.id.fl_container);
    }

    private void obtainValue() {
        Bundle bundle = mFragment.getArguments();
        if (bundle != null) {
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
            mDatetime = (Date) bundle.getSerializable("datetime");
            mAnimation = bundle.getBoolean("isAnimation", false);
        }
    }

    void doRequest() {
        String datetime = "";
        if (mDatetime != null) {
            datetime += TimeUtil.getFirstDayOfMonth(mDatetime) + "@" + TimeUtil.getLastDayOfMonth(mDatetime);
        }
        String type = TextUtils.isEmpty(mType) ? "" : mType;
        String cityCode = TextUtils.isEmpty(mCityCode) ? "" : mCityCode;
        mCallback.onHttpRequest(type, cityCode, datetime);
    }

    void onHttpSuccess(CustomerSatisfactionBean bean) {
        if (bean == null) {
            mFragment.noResult();
        } else {
            mContainerLayout.setVisibility(View.VISIBLE);
            if (bean.isShop()) {  //判断加载哪个布局
                setShopData(bean);
            } else {
                setSelectData(bean);
            }
        }
    }

    /*列表式*/
    private void setShopData(CustomerSatisfactionBean bean) {
        mContainerLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_customer_satisfaction_list, mContainerLayout, true);
        final TextView tvDatetime = view.findViewById(R.id.tv_datetime);
        setDatetime(tvDatetime, bean.time);
        tvDatetime.setOnClickListener(v -> PickerHelper.showTimeYMPicker(mContext, date -> {
            mDatetime = date;
            setDatetime(tvDatetime, TimeUtil.timeMillsFormat(date, "yyyy.MM"));
            doRequest();
        }));
        TextView tvEmpty = view.findViewById(R.id.tv_empty);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if (bean.getShopData().isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new ShopDataItemAdapter(mContext, bean.getShopData()));
        }
    }

    private void setDatetime(TextView tvDatetime, String datetime) {
        String timeTemp = mContext.getString(R.string.order_time_tips) + (TextUtils.isEmpty(datetime) ? "" : datetime);
        tvDatetime.setText(timeTemp);
    }

    /*表格式*/
    private void setSelectData(CustomerSatisfactionBean bean) {
        mContainerLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_customer_satisfaction_form, mContainerLayout, true);
        final TextView tvDatetime = view.findViewById(R.id.tv_datetime);
        tvDatetime.setText(bean.time);
        tvDatetime.setOnClickListener(v -> PickerHelper.showTimeYMPicker(mContext, date -> {
            mDatetime = date;
            tvDatetime.setText(TimeUtil.timeMillsFormat(date, "yyyy.MM"));
            doRequest();
        }));
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new FormDataAdapter(mContext, bean.getSelectData()));
    }

    void onHttpFailure(String failReason) {
        mContainerLayout.setVisibility(View.GONE);
        mFragment.noNetwork(failReason);
    }

    void deDetach() {
        mContainerLayout.removeCallbacks(mRequestRun);
    }

    //shopData
    class ShopDataItemAdapter extends CommonAdapter<CustomerSatisfactionBean.ShopDataBean> {

        TextSpanHelper mSpanHelper;

        ShopDataItemAdapter(Context context, List<CustomerSatisfactionBean.ShopDataBean> mDatas) {
            super(context, mDatas);
            mSpanHelper = TextSpanHelper.from(mContext);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_customer_satisfaction_list;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, CustomerSatisfactionBean.ShopDataBean bean, int position) {
            holder.setText(R.id.tv_customer_phone, mSpanHelper.obtainColorBoldStyle(R.string.customer_telephone_tips, bean.phoneNumber, R.color.textColor4));
            holder.setText(R.id.tv_order_time, mSpanHelper.obtainColorBoldStyle(R.string.order_time_tips, bean.time, R.color.textColor4));
            holder.setText(R.id.tv_service_satisfaction, mSpanHelper.obtainColorBoldStyle(R.string.customer_satisfaction_service, bean.service, R.color.textColor4));
            holder.setText(R.id.tv_design_satisfaction, mSpanHelper.obtainColorBoldStyle(R.string.customer_satisfaction_design, bean.design, R.color.textColor4));
            holder.setText(R.id.tv_delivery_satisfaction, mSpanHelper.obtainColorBoldStyle(R.string.customer_satisfaction_delivery, bean.delivery, R.color.textColor4));
            holder.setText(R.id.tv_install_satisfaction, mSpanHelper.obtainColorBoldStyle(R.string.customer_satisfaction_install, bean.install, R.color.textColor4));
            if (!TextUtils.isEmpty(bean.content)) {
                holder.setVisibility(R.id.ll_content_layout, View.VISIBLE);
                holder.setText(R.id.tv_message, bean.content);
            } else {
                holder.setVisibility(R.id.ll_content_layout, View.GONE);
            }
            if (bean.getImages().isEmpty()) {
                holder.setVisibility(R.id.ll_photo_layout, View.GONE);
            } else {
                holder.setVisibility(R.id.ll_photo_layout, View.VISIBLE);
                RecyclerView rvPhoto = holder.obtainView(R.id.rv_photo);
                rvPhoto.setNestedScrollingEnabled(false);
                if (rvPhoto.getItemDecorationCount() == 0) {
                    rvPhoto.addItemDecoration(new GridSpacingItemDecoration(5, mContext.getResources().getDimensionPixelSize(R.dimen.dp_10)));
                }
                rvPhoto.setAdapter(new UrlDataAdapter(mContext, bean.getUrlList()));
            }
        }

        class UrlDataAdapter extends CommonAdapter<CustomerSatisfactionBean.ShopDataBean.UrlBean> {
            List<String> mImages;

            UrlDataAdapter(Context context, List<CustomerSatisfactionBean.ShopDataBean.UrlBean> dataList) {
                super(context, dataList);
                mImages = new ArrayList<>();
                for (CustomerSatisfactionBean.ShopDataBean.UrlBean bean : dataList) {
                    if (!bean.isMp4()) {
                        mImages.add(bean.url);
                    }
                }
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_customer_satisfaction_list_child;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, CustomerSatisfactionBean.ShopDataBean.UrlBean bean, int position) {
                ImageView iv = holder.obtainView(R.id.iv_image);
                Glide.with(mContext).load(bean.getShowUrl()).apply(new RequestOptions()
                        .placeholder(R.drawable.loading_photo)
                        .error(0).centerCrop()).into(iv);
                holder.itemView.setOnClickListener(view -> {
                    if (bean.isMp4()) { //播放视频 跳转播放页面
                    } else {
                        PhotoViewActivity.openImage(mContext, position, mImages);
                    }
                });
            }
        }
    }

    class FormDataAdapter extends CommonAdapter<CustomerSatisfactionBean.SelectDataBean> {

        FormDataAdapter(Context context, List<CustomerSatisfactionBean.SelectDataBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_customer_satisfaction_form;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, CustomerSatisfactionBean.SelectDataBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_division, bean.area);
            holder.setText(R.id.tv_service_satisfaction, bean.service);
            holder.setText(R.id.tv_design_satisfaction, bean.design);
            holder.setText(R.id.tv_delivery_satisfaction, bean.delivery);
            holder.setText(R.id.tv_install_satisfaction, bean.install);
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
                holder.setEnabled(R.id.tv_division, true);
            } else {
                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
                holder.setEnabled(R.id.tv_division, false);
            }
//            if (bean.isChange()) {
//                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
//            } else {
//                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
//            }
            holder.setOnClickListener(R.id.tv_division, view -> mCallback.toNextLevel(obtainBundle(bean.type, bean.cityCode)));
        }
    }

    private Bundle obtainBundle(String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("datetime", mDatetime);
        bundle.putBoolean("isAnimation", true);
        return bundle;
    }

    interface Callback {
        void onHttpRequest(String type, String cityCode, String datetime);

        void toNextLevel(Bundle bundle);
    }
}
