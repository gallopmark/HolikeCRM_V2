package com.holike.crm.fragment.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gallopmark.imagepicker.model.ImagePicker;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.presenter.fragment.WorkflowPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WorkflowView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wqj on 2018/8/27.
 * 操作流程fragment
 */

public abstract class WorkflowFragment extends MyFragment<WorkflowPresenter, WorkflowView> {
    protected Bundle bundle;
    protected CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean listStatusBean;
    protected CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean;
    protected CustomerDetailBean.PersonalBean personal;
    protected String customerStatus, houseId, operateCode, personalId, prepositionRuleStatus;
    protected TypeIdBean typeIdBean;
    protected AssociateBean.ProfessionBean professionBean;
    protected List<AssociateBean.ProfessionBean> professionBeans;
    protected OptionsPickerView mPickerView;

    protected List<String> imgs;
    protected UploadImgHelper.OnClickListener clickImgListener;
    private RecyclerView rvImg;
    private String imgText;
    protected String houseType;
    private TimePickerView pvTime;

    @Override
    protected WorkflowPresenter attachPresenter() {
        return new WorkflowPresenter();
    }

    @Override
    protected void init() {
        super.init();
        bundle = getArguments();
        if (bundle != null) {
            listStatusBean = (CustomerDetailBean.CustomerDetailInfoListBean.ListStatusBean) bundle.getSerializable(Constants.CUSTOMER_STATUS);
            houseInfoBean = (CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean) bundle.getSerializable(Constants.HOUSE_INFO);
            personal = (CustomerDetailBean.PersonalBean) bundle.getSerializable(Constants.PERSONAL_BEAN);
            customerStatus = listStatusBean == null ? "" : listStatusBean.getCustomerStatus();
            operateCode = listStatusBean == null ? "" : listStatusBean.getOptCode();
            prepositionRuleStatus = listStatusBean == null ? "" : listStatusBean.getPrepositionRuleStatus();
            houseId = houseInfoBean == null ? "" : houseInfoBean.getHouseId();
            personalId = personal == null ? "" : personal.getPersonalId();
            houseType = (String) bundle.getSerializable(Constants.HOUSE_TYPE);
        }
    }

    protected void initImgs(final RecyclerView rv, String text) {
        rvImg = rv;
        imgText = text;
        imgs = new ArrayList<>();
        clickImgListener = new UploadImgHelper.OnClickListener() {
            @Override
            public void addImg() {
                ImagePicker.builder().maxSelectCount(9 - imgs.size()).start(WorkflowFragment.this);
            }

            @Override
            public void delImg(String img) {
                imgs.remove(img);
                UploadImgHelper.setImgList(mContext, rv, imgs, imgText, 9, clickImgListener);
            }
        };
        UploadImgHelper.setImgList(mContext, rv, imgs, imgText, 9, clickImgListener);
    }

    /**
     * 选中
     *
     * @param options
     */
    protected void optionsSelect(int options) {
    }

    /**
     * 条件选择弹窗
     *
     * @param dates
     */
    public void showPickerView(List<? extends TypeIdBean.TypeIdItem> dates, String select, View view) {
        if (view != null) {
            hideSoftInput(view);
        }
        if (mPickerView == null) {
            mPickerView = new OptionsPickerBuilder(mContext, (options1, options2, options3, v) -> optionsSelect(options1)).build();
        }
        if (dates == null || dates.size() < 1) {
            showShortToast("没有选择数据！");
        } else {
            mPickerView.setPicker(dates);
            mPickerView.setSelectOptions(AddCustomerPresenter.getSelectOption(dates, select));
            mPickerView.show();
        }
    }


    protected void selectTime(Date date) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.DEFAULT_REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImagePicker.SELECT_RESULT);
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            imgs.addAll(images);
//                for (ImageItem imageItem : images) {
//                    imgs.add(imageItem.path);
//                }
            UploadImgHelper.setImgList(mContext, rvImg, imgs, imgText, 9, clickImgListener);
        }
    }

    /**
     * 操作成功关闭页面
     */
    protected void operateSuccess() {
        finishFragment(0, Constants.RESULT_CODE_OPERATE_SUCCESS, null);

    }

    /**
     * 选择时间
     *
     * @param context
     * @param time
     */
    protected void showTimePickerView(Context context, String time, View view) {
        if (view != null) {
            hideSoftInput(view);
        }
        if (pvTime == null) {
            pvTime = new TimePickerBuilder(context, (date, v) ->
                    selectTime(date)).setType(new boolean[]{true, true, true, false, false, false}).build();
        }
        if (!TextUtils.isEmpty(time)) {
            pvTime.setDate(TimeUtil.stringToCalendar(time, "yyyy.MM.dd"));
        } else {
            pvTime.setDate(Calendar.getInstance());
        }
        pvTime.show();
    }

}
