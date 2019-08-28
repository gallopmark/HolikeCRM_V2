package com.holike.crm.fragment.customer.workflow;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.view.fragment.WorkflowView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/6.
 * 预约安装
 */

public class BookingInstallFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_booking_install_time)
    TextView tvTime;
    @BindView(R.id.tv_booking_install_man)
    TextView tvMan;
    @BindView(R.id.et_booking_install_area)
    EditText etArea;
    @BindView(R.id.et_booking_install_note)
    EditText etNote;

    private AssociateBean.Installer installer;
    private List<AssociateBean.Installer> installers;

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_booking_install));
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_booking_install;
    }

    /**
     * 保存成功
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof AssociateBean) {
            installers = ((AssociateBean) success).getInstaller();
            showPickerView(installers, getText(tvMan), etNote);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 保存失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    protected void optionsSelect(int options) {
        installer = installers.get(options);
        tvMan.setText(installer.getUserName());
    }

    @OnClick({R.id.tv_booking_install_time, R.id.tv_booking_install_man, R.id.tv_booking_install_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_booking_install_time:
                showTimePickerView(getActivity(), getText(tvTime), etArea);
                break;
            case R.id.tv_booking_install_man:
                if (installers != null) {
                    showPickerView(installers, getText(tvMan), etNote);
                } else {
                    showLoading();
                    mPresenter.getAssociate(houseInfoBean.getShopId());
                }
                break;
            case R.id.tv_booking_install_save:
                if (isTextEmpty(tvTime) || isTextEmpty(tvMan) || isTextEmpty(etArea)) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.bookingInstall(getText(etNote), customerStatus, houseId, installer.getUserId(), getText(tvTime), operateCode, personalId, prepositionRuleStatus, getText(etArea));
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
        tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
    }
}
