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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/6.
 * 安装完成
 */
@Deprecated
public class InstalledFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_installed_time)
    TextView tvTime;
    @BindView(R.id.tv_installed_installer)
    TextView tvInstaller;
    @BindView(R.id.et_installed_note)
    EditText etNote;
    @BindView(R.id.rv_installed_img)
    RecyclerView rv;

    private String bookingInstallTime;
    private AssociateBean.Installer installer;
    private List<AssociateBean.Installer> installers;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_installed;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_installed));
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        initImgs(rv, getString(R.string.house_manage_add_installed_img));
        bookingInstallTime = houseInfoBean.getAppointmentToInstallDate();
        tvTime.setText(bookingInstallTime == null ? "" : TimeUtil.stampToString(bookingInstallTime, "yyyy.MM.dd"));
    }

    /**
     * 保存成功
     *
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof AssociateBean) {
            installers = ((AssociateBean) success).getInstaller();
            showPickerView(installers, getText(tvInstaller), etNote);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 保存失败
     *
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    protected void optionsSelect(int options) {
        installer = installers.get(options);
        tvInstaller.setText(installer.getUserName());
    }

    @OnClick({R.id.tv_installed_time, R.id.tv_installed_installer, R.id.tv_installed_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_installed_time:
                showTimePickerView(getActivity(), getText(tvTime), tvInstaller);
                break;
            case R.id.tv_installed_installer:
                if (installers != null) {
                    showPickerView(installers, getText(tvInstaller), etNote);
                } else {
                    showLoading();
                    mPresenter.getAssociate(houseInfoBean == null ? "" : houseInfoBean.getShopId());
                }
                break;
            case R.id.tv_installed_save:
                if (isTextEmpty(tvTime) || isTextEmpty(tvInstaller)) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.installed(mContext, getText(etNote), customerStatus, houseId, installer.getUserId(), getText(tvTime), operateCode, personalId, prepositionRuleStatus, imgs);
                    showLoading();
                }
                break;
        }
    }

    /**
     * 选择时间
     *
     * @param date
     */
    @Override
    protected void selectTime(Date date) {
        tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
    }
}
