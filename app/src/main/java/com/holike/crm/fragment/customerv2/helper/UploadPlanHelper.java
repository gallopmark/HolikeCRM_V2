package com.holike.crm.fragment.customerv2.helper;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.IImageSelectHelper;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;
import com.holike.crm.util.KeyBoardUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by pony on 2019/7/25.
 * Copyright holike possess 2019.
 * 上传方案帮助类
 */
public class UploadPlanHelper extends IImageSelectHelper implements View.OnClickListener {

    private UploadPlanCallback mCallback;
    private TextView mReservationDateTextView;
    private TextView mProductTextView;
    private TextView mSeriesTextView;
    private TextView mStyleTextView;
    private EditText mRemarkEditText;
    private String mHouseId; //房屋id bundle传递过来
    private String mBookOrderDate; //预约确图日期
    private String mProduct; //产品
    private String mSeries; //系列
    private String mStyle; //风格
    private SysCodeItemBean mSysCodeBean;
    private int mSelectType = 0;

    private List<String> mSelectedImages;  //已选择的图片 （从客户管理详情也带过来，属于网络图片）
    private List<String> mSchemeImages; //删除图片去schemeImgId，后台真是骚 搞不懂？？？？？？？？
    private List<String> mRemovedImages;  //被删除的网络图片数据

    public UploadPlanHelper(BaseFragment<?, ?> fragment, UploadPlanCallback callback) {
        super(fragment);
        Bundle bundle = fragment.getArguments();
        mSelectedImages = new ArrayList<>();
        mSchemeImages = new ArrayList<>();
        mRemovedImages = new ArrayList<>();
        if (bundle != null) {
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mBookOrderDate = bundle.getString("bookOrderDate");
            mProduct = bundle.getString("product");
            mSeries = bundle.getString("series");
            mStyle = bundle.getString("style");
            ArrayList<String> images = bundle.getStringArrayList("images");
            ArrayList<String> schemeImages = bundle.getStringArrayList("schemeImages");
            if (images != null && !images.isEmpty()) {
                mSelectedImages.addAll(images);
            }
            if (schemeImages != null && !schemeImages.isEmpty()) {
                mSchemeImages.addAll(schemeImages);
            }
        }
        mSysCodeBean = IntentValue.getInstance().getSystemCode();
        this.mCallback = callback;
        initViews(fragment.getContentView(), bundle);
    }

    private void initViews(View contentView, Bundle bundle) {
        WrapperRecyclerView wrapperRecyclerView = contentView.findViewById(R.id.wrapperRecyclerView);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_customer_uploadplan, new LinearLayout(mContext), false);
        mReservationDateTextView = headerView.findViewById(R.id.tv_reservation_date);
        mProductTextView = headerView.findViewById(R.id.tv_product);
        mSeriesTextView = headerView.findViewById(R.id.tv_series);
        mStyleTextView = headerView.findViewById(R.id.tv_style);
        mRemarkEditText = headerView.findViewById(R.id.et_remark);
        wrapperRecyclerView.addHeaderView(headerView);
        int space = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
        wrapperRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, space, true, 1));
        setupSelectImages(wrapperRecyclerView, R.string.tips_add_plan_images, true, 180);
        mImageHelper.imageOptionsListener((position, path) -> {
            mImageHelper.remove(position);
            if (mSelectedImages.contains(path) && position >= 0 && position < mSchemeImages.size()) {  //删除网络图片
                mSelectedImages.remove(position);
                String imageUrl = mSchemeImages.remove(position);
                if (!mRemovedImages.contains(imageUrl)) {
                    mRemovedImages.add(imageUrl);
                }
            }
        }).setImagePaths(new ArrayList<>(mSelectedImages));
        if (bundle != null) {
            mReservationDateTextView.setText(bundle.getString("bookOrderDateName"));
            mProductTextView.setText(bundle.getString("productName"));
            mSeriesTextView.setText(bundle.getString("seriesName"));
            mStyleTextView.setText(bundle.getString("styleName"));
            mRemarkEditText.setText(bundle.getString("remark"));
        }
        mReservationDateTextView.setOnClickListener(this);
        mProductTextView.setOnClickListener(this);
        mSeriesTextView.setOnClickListener(this);
        mStyleTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput((Activity) mContext);
        switch (view.getId()) {
            case R.id.tv_reservation_date:
                onSelectDate(mReservationDateTextView);
                break;
            case R.id.tv_product:
                if (mSysCodeBean == null) {
                    mSelectType = 1;
                    mCallback.onRequestSystemCode();
                } else {
                    onSelectProduct(mProductTextView, mSysCodeBean.getCustomerProduct());
                }
                break;
            case R.id.tv_series:
                if (TextUtils.isEmpty(mProduct)) {
                    mCallback.onRequired(mContext.getString(R.string.tips_pleas_choose_first) + mContext.getString(R.string.followup_product_tips2));
                } else {
                    if (mSysCodeBean == null) {
                        mSelectType = 2;
                        mCallback.onRequestSystemCode();
                    } else {
                        onSelectSeries(mSeriesTextView);
                    }
                }
                break;
            case R.id.tv_style:
                if (mSysCodeBean == null) {
                    mSelectType = 3;
                    mCallback.onRequestSystemCode();
                } else {
                    onSelectStyle(mStyleTextView, mSysCodeBean.getDecorationStyle());
                }
                break;
        }
    }

    /*选择预约确图日期*/
    private void onSelectDate(TextView tv) {
        PickerHelper.showTimePicker(mContext, date -> {
            mBookOrderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
            tv.setText(new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date));
        });
    }

    public void setSystemCode(SysCodeItemBean bean) {
        this.mSysCodeBean = bean;
        if (mSelectType == 1) {
            onSelectProduct(mProductTextView, mSysCodeBean.getCustomerProduct());
        } else if (mSelectType == 2) {
            onSelectSeries(mSeriesTextView);
        } else if (mSelectType == 3) {
            onSelectStyle(mStyleTextView, mSysCodeBean.getDecorationStyle());
        }
    }

    /*选择产品*/
    private void onSelectProduct(final TextView tv, Map<String, String> customerProduct) {
        List<DictionaryBean> optionItems = PickerHelper.map2OptionItems(customerProduct);
        PickerHelper.showOptionsPicker(mContext, optionItems, (position, bean) -> {
            mProduct = bean.id;
            tv.setText(bean.name);
            resetSeries();
        });
    }

    /*重新选择产品后 重置系列*/
    private void resetSeries() {
        mSeries = null;
        mSeriesTextView.setText(null);
    }

    /*选择系列，前提是要选择产品之后*/
    private void onSelectSeries(final TextView tv) {
        /*"WHOLE_HOUSE_PRODUCT":"全屋定制产品",
"CUPBOARD_PRODUCT":"橱柜产品",
"DOOR_PRODUCT":"木门产品"*/
        Map<String, String> series = null;
        Map<String, String> productMap = mSysCodeBean.getCustomerProduct();
        String productName = productMap.get(mProduct);
        if (TextUtils.equals(mProduct, "WHOLE_HOUSE_PRODUCT")
                || TextUtils.equals(productName, "全屋定制产品")) {  //全屋定制系列
            //选择了“全屋定制产品”
            series = mSysCodeBean.getHouseSeries();
        } else if (TextUtils.equals(mProduct, "CUPBOARD_PRODUCT")
                || TextUtils.equals(productName, "橱柜产品")) { //橱柜系列
            /*选择了“橱柜产品”*/
            series = mSysCodeBean.getCupboardSeries();
        } else if (TextUtils.equals(mProduct, "DOOR_PRODUCT") || TextUtils.equals(productName, "木门产品")) { //木门系列
            /*选择了“木门产品”*/
            series = mSysCodeBean.getDoorSeries();
        }
        if (series != null) {
            List<DictionaryBean> optionItems = PickerHelper.map2OptionItems(series);
            PickerHelper.showOptionsPicker(mContext, optionItems, (position, bean) -> {
                mSeries = bean.id;
                tv.setText(bean.name);
            });
        }
    }

    /*选择风格*/
    private void onSelectStyle(final TextView tv, Map<String, String> decorationStyle) {
        final List<DictionaryBean> optionItems = PickerHelper.map2OptionItems(decorationStyle);
        PickerHelper.showOptionsPicker(mContext, optionItems, (position, bean) -> {
            mStyle = bean.id;
            tv.setText(bean.name);
        });
    }

    public void onSave() {
        String remark = mRemarkEditText.getText().toString();
        List<String> images = mImageHelper.getSelectedImages();
        List<String> targetImages = new ArrayList<>(); //只上传新添加进来的图片，即从相册新增进来的图片
        for (String image : images) { //过滤掉从详情带过来的图片（不作上传，从相册新添加进来的才做上传）
            if (!mSelectedImages.contains(image)) {
                targetImages.add(image);
            }
        }
        if (images.isEmpty()) {  //如果从未上传过图片，则必须要添加方案图片
            String tips = mContext.getString(R.string.please) + mContext.getString(R.string.tips_add_plan_images);
            mCallback.onRequired(tips);
        } else {
            mCallback.onSave(mRemovedImages, mHouseId, mBookOrderDate, mProduct, mSeries, mStyle, remark, targetImages);
        }
    }

    public interface UploadPlanCallback {
        void onRequestSystemCode();

        void onRequired(String tips);

        void onSave(List<String> removedImages, String houseId, String bookOrderDate, String product,
                    String series, String style, String remark,
                    List<String> images);
    }
}
