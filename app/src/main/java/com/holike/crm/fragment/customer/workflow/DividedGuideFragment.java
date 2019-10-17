package com.holike.crm.fragment.customer.workflow;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.DivideGuideBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.view.fragment.WorkflowView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/2.
 * 分配导购
 */
@Deprecated
public class DividedGuideFragment extends WorkflowFragment implements WorkflowView {
    private final int TYPE_DIVIDE = 1;
    private final int TYPE_USEFUL = 2;
    private final int TYPE_REASON = 3;

    @BindView(R.id.tv_divide_guide_select)
    TextView tvSelect;
    @BindView(R.id.tv_divide_guide_current)
    TextView tvCurrent;
    @BindView(R.id.tv_divide_guide_time)
    TextView tvTime;
    @BindView(R.id.statusView)
    View statusView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_divide_guide_useful)
    TextView tvUseful;
    @BindView(R.id.ll_divide_guide_useful)
    LinearLayout llUseful;
    @BindView(R.id.tv_divide_guide_reason)
    TextView tvReason;
    @BindView(R.id.ll_divide_guide_reason)
    LinearLayout llReason;

    private DivideGuideBean divideGuideBean;
    private DivideGuideBean.ListBean listBean;
    private TypeIdBean.TypeIdItem useful, reason;
    private List<TypeIdBean.TypeIdItem> usefuls, reasons;
    private int type;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_divide_guide;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_divide_guide));
        if (houseInfoBean != null) {
            mPresenter.getDivideGuide(houseInfoBean.getHouseId());
            showLoading();
            if (personal != null && TextUtils.equals(personal.getSource(), "09") && !TextUtils.equals(houseInfoBean.getOutTimeFlag(), "1")) {
                llUseful.setVisibility(View.VISIBLE);
                usefuls = new ArrayList<>();
                usefuls.add(new TypeIdBean.TypeIdItem("0", "有效"));
                usefuls.add(new TypeIdBean.TypeIdItem("1", "无效"));
            } else {
                useful = new TypeIdBean.TypeIdItem();
                reason = new TypeIdBean.TypeIdItem();
            }
        }
    }

    /**
     * 选择导购/是否有效/无效原因
     *
     * @param options
     */
    @Override
    protected void optionsSelect(int options) {
        switch (type) {
            case TYPE_DIVIDE:
                listBean = divideGuideBean.getList().get(options);
                tvSelect.setText(listBean.getPickerViewText());
                break;
            case TYPE_USEFUL:
                useful = usefuls.get(options);
                tvUseful.setText(useful.getName());
                if (useful.getId().equals("1")) {
                    llReason.setVisibility(View.VISIBLE);
                } else {
                    llReason.setVisibility(View.GONE);
                    tvReason.setText("");
                    reason = null;
                }
                break;
            case TYPE_REASON:
                reason = reasons.get(options);
                tvReason.setText(reason.getName());
                break;
        }
    }

    /**
     * 获取分配导购/无效原因/分配导购成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof DivideGuideBean) {
            divideGuideBean = (DivideGuideBean) success;
            String text = mContext.getString(R.string.house_manage_current_guide);
            if (!TextUtils.isEmpty(divideGuideBean.getName())) {
                text += divideGuideBean.getName();
            }
            tvCurrent.setText(text);
            tvTime.setText(divideGuideBean.getTime());
        } else if (success instanceof TypeIdBean) {
            reasons = AddCustomerPresenter.getTypeIdItems(((TypeIdBean) success).getDIGITAL_INVALIDITY_RESON());
            showPickerView(reasons, getText(tvReason), tvCurrent);
        } else if (success instanceof String) {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }

    /**
     * 获取分配导购/无效原因/分配导购失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }


    @OnClick({R.id.tv_divide_guide_select, R.id.tv_divide_guide_save, R.id.tv_divide_guide_useful, R.id.tv_divide_guide_reason})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_divide_guide_select:
                type = TYPE_DIVIDE;
                if (divideGuideBean != null && divideGuideBean.getList() != null) {
                    showPickerView(divideGuideBean.getList(), getText(tvSelect), tvCurrent);
                }
                break;
            case R.id.tv_divide_guide_save:
                if (listBean == null || useful == null) {
                    showShortToast(R.string.tips_complete_information);
                } else if (useful.getId().equals("1") && reason == null) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.divideGuide(reason == null ? "" : reason.getId(), customerStatus, houseId, useful == null ? "" : useful.getId(), operateCode, personalId, prepositionRuleStatus, listBean.getUserId());
                    showLoading();
                }
                break;
            case R.id.tv_divide_guide_useful:
                type = TYPE_USEFUL;
                showPickerView(usefuls, getText(tvUseful), tvCurrent);
                break;
            case R.id.tv_divide_guide_reason:
                type = TYPE_REASON;
                if (typeIdBean == null) {
                    mPresenter.getTypeId();
                    showLoading();
                } else {
                    showPickerView(reasons, getText(tvReason), tvCurrent);
                }
                break;
        }
    }

}
