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
 * 上传方案
 */
@Deprecated
public class UploadPlanFragment extends WorkflowFragment implements WorkflowView {
    private final int TYPE_PLAN = 1;
    private final int TYPE_IMG = 2;
    @BindView(R.id.tv_upload_plan_time)
    TextView tvTime;
    @BindView(R.id.tv_upload_plan_uploader)
    TextView tvUploader;
    @BindView(R.id.et_upload_plan_note)
    EditText etNote;
    @BindView(R.id.tv_upload_plan_img_time)
    TextView tvImgTime;
    @BindView(R.id.rv_upload_plan_img)
    RecyclerView rv;
    @BindView(R.id.tv_upload_plan_save)
    TextView tvUploadPlanSave;

    private int timeType;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_upload_plan;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_upload_plan));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initImgs(rv, getString(R.string.house_manage_add_plan_img));
        tvTime.setText(TimeUtil.dateToString(new Date(), "yyyy.MM.dd"));
        mPresenter.getAssociate(houseInfoBean.getShopId());
        showLoading();
    }

    /**
     * 获取人员/保存成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof AssociateBean) {
            professionBeans = ((AssociateBean) success).getProfession();
            if (isTextEmpty(tvUploader)) {
                professionBean = mPresenter.getCurrentProfession(professionBeans);
                if (professionBean != null) tvUploader.setText(professionBean.getUserName());
            } else {
                showPickerView(professionBeans, getText(tvUploader), etNote);
            }
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 获取人员/保存失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @OnClick({R.id.tv_upload_plan_time, R.id.tv_upload_plan_uploader, R.id.tv_upload_plan_img_time, R.id.tv_upload_plan_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload_plan_time:
                timeType = TYPE_PLAN;
                showTimePickerView(getActivity(), getText(tvTime), etNote);
                break;
            case R.id.tv_upload_plan_uploader:
                if (professionBeans == null) {
                    mPresenter.getAssociate(houseInfoBean.getShopId());
                    showLoading();
                } else {
                    showPickerView(professionBeans, getText(tvUploader), etNote);
                }
                break;
            case R.id.tv_upload_plan_img_time:
                timeType = TYPE_IMG;
                showTimePickerView(getActivity(), getText(tvImgTime), etNote);
                break;
            case R.id.tv_upload_plan_save:
                if (isTextEmpty(tvTime) || professionBean == null) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.uploadPlan(mContext, getText(etNote), customerStatus, getText(tvImgTime), houseId, professionBean.getUserId(), getText(tvTime), operateCode, personalId, prepositionRuleStatus, imgs);
                    showLoading();
                }
                break;
        }
    }

    /**
     * 选择日期
     *
     * @param date
     */
    @Override
    protected void selectTime(Date date) {
        switch (timeType) {
            case TYPE_PLAN:
                tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
                break;
            case TYPE_IMG:
                tvImgTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
                break;
        }
    }

    /**
     * 选择上传人
     *
     * @param options
     */
    @Override
    protected void optionsSelect(int options) {
        professionBean = professionBeans.get(options);
        tvUploader.setText(professionBean.getUserName());
    }
}
