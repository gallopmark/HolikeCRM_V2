package com.holike.crm.fragment.customer.workflow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * 上传复尺结果
 */
@Deprecated
public class UploadReMeasureFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_upload_remeasure_time)
    TextView tvTime;
    @BindView(R.id.tv_upload_remeasure_man)
    TextView tvMan;
    @BindView(R.id.rb_upload_remeasure_usable)
    RadioButton rbUsable;
    @BindView(R.id.rb_upload_remeasure_unusable)
    RadioButton rbUnusable;
    @BindView(R.id.rg_upload_remeasure_result)
    RadioGroup rgResult;
    @BindView(R.id.et_upload_remeasure_note)
    EditText etNote;
    @BindView(R.id.rv_upload_remeasure_img)
    RecyclerView rv;
    @BindView(R.id.tv_upload_remeasure_save)
    TextView tvUploadRemeasureSave;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_upload_remeasure;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_upload_remeasure));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initImgs(rv, getString(R.string.house_manage_add_remeasure_img));
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
            if (isTextEmpty(tvMan)) {
                professionBean = mPresenter.getCurrentProfession(professionBeans);
                if (professionBean != null) tvMan.setText(professionBean.getUserName());
            } else {
                showPickerView(professionBeans, getText(tvMan), etNote);
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

    @OnClick({R.id.tv_upload_remeasure_time, R.id.tv_upload_remeasure_man, R.id.rb_upload_remeasure_usable, R.id.rb_upload_remeasure_unusable, R.id.tv_upload_remeasure_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload_remeasure_time:
                showTimePickerView(getActivity(), getText(tvTime), etNote);
                break;
            case R.id.tv_upload_remeasure_man:
                if (professionBeans == null) {
                    mPresenter.getAssociate(houseInfoBean.getShopId());
                    showLoading();
                } else {
                    showPickerView(professionBeans, getText(tvMan), etNote);
                }
                break;
            case R.id.rb_upload_remeasure_usable:
                rbUsable.setChecked(true);
                rbUnusable.setChecked(false);
                break;
            case R.id.rb_upload_remeasure_unusable:
                rbUsable.setChecked(false);
                rbUnusable.setChecked(true);
                break;
            case R.id.tv_upload_remeasure_save:
                if (isTextEmpty(tvTime) || isTextEmpty(tvMan) || (!rbUsable.isChecked() && !rbUnusable.isChecked())) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.upLoadRemeasure(mContext,professionBean.getUserId(),getText(etNote), customerStatus, houseId, getText(tvTime), operateCode, rbUsable.isChecked() ? "1" : "0", personalId, prepositionRuleStatus, imgs);
                    showLoading();
                }
                break;
        }
    }

    /**
     * 选择人员
     *
     * @param options
     */
    @Override
    protected void optionsSelect(int options) {
        professionBean = professionBeans.get(options);
        tvMan.setText(professionBean.getUserName());
    }

    /**
     * 选择日期
     *
     * @param date
     */
    @Override
    protected void selectTime(Date date) {
        tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
    }
}
