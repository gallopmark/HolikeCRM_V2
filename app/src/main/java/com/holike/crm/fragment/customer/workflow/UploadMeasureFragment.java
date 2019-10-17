package com.holike.crm.fragment.customer.workflow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WorkflowView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/6.
 * 上传量房结果
 */
@Deprecated
public class UploadMeasureFragment extends WorkflowFragment implements WorkflowView {
    private final int TYPE_MEASURE = 1;
    private final int TYPE_IMG = 2;

    @BindView(R.id.tv_upload_measure_time)
    TextView tvMeasureTime;
    @BindView(R.id.tv_upload_measure_man)
    TextView tvMeasureMan;
    @BindView(R.id.et_upload_measure)
    EditText et;
    @BindView(R.id.tv_upload_measure_img_time)
    TextView tvImgTime;
    @BindView(R.id.rv_upload_measure)
    RecyclerView rv;

    private int timeType;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_upload_measure;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_upload_measure_result));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initImgs(rv, getString(R.string.house_manage_add_measure_img));
        tvMeasureTime.setText(TimeUtil.dateToString(new Date(), "yyyy.MM.dd"));
        mPresenter.getAssociate(houseInfoBean.getShopId());
        showLoading();
    }

    /**
     * 获取所属人/保存成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof AssociateBean) {
            professionBeans = ((AssociateBean) success).getProfession();
            if (isTextEmpty(tvMeasureMan)) {
                professionBean = mPresenter.getCurrentProfession(professionBeans);
                if (professionBean != null) tvMeasureMan.setText(professionBean.getUserName());
            } else {
                showPickerView(professionBeans, getText(tvMeasureMan), et);
            }
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 获取所属人/保存失败
     *
     * @param failed
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @OnClick({R.id.tv_upload_measure_time, R.id.tv_upload_measure_man, R.id.tv_upload_measure_img_time, R.id.tv_upload_measure_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload_measure_time:
                timeType = TYPE_MEASURE;
                showTimePickerView(getActivity(), getText(tvMeasureTime), et);
                break;
            case R.id.tv_upload_measure_man:
                if (professionBeans == null) {
                    mPresenter.getAssociate(houseInfoBean.getShopId());
                    showLoading();
                } else {
                    showPickerView(professionBeans, getText(tvMeasureMan), et);
                }
                break;
            case R.id.tv_upload_measure_img_time:
                timeType = TYPE_IMG;
                showTimePickerView(getActivity(), getText(tvImgTime), et);
                break;
            case R.id.tv_upload_measure_save:
                if (isTextEmpty(tvMeasureTime) || professionBean == null) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.uploadMeasureResult(mContext, getText(et), customerStatus, getText(tvImgTime), houseId, professionBean.getUserId(), getText(tvMeasureTime), operateCode, personalId, prepositionRuleStatus, imgs);
                    showLoading();
                }
                break;
        }
    }

    /**
     * 选中所属人
     *
     * @param options
     */
    @Override
    protected void optionsSelect(int options) {
        professionBean = professionBeans.get(options);
        tvMeasureMan.setText(professionBean.getUserName());
    }

    /**
     * 选择时间
     *
     * @param date
     */
    @Override
    protected void selectTime(Date date) {
        switch (timeType) {
            case TYPE_MEASURE:
                tvMeasureTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
                break;
            case TYPE_IMG:
                tvImgTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
                break;
        }
    }
}
