package com.holike.crm.fragment.bank;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.imagepicker.model.ImagePicker;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.base.ToolbarHelper;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.bean.UploadByRelationBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.model.event.MessageEvent;
import com.holike.crm.popupwindown.ListMenuPopupWindow;
import com.holike.crm.presenter.fragment.PayDetailsPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.fragment.PayDetailsView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.holike.crm.http.UrlPath.URL_GET_PATH_IMG;

/**
 * 在线申报-详情
 */
public class DetailsFragment extends MyFragment<PayDetailsPresenter, PayDetailsView> implements ListMenuPopupWindow.ItemSelectListener, PayDetailsView {

    @BindView(R.id.tv_details_creation_time)
    TextView tvDetailsCreationTime;
    @BindView(R.id.tv_details_the_current_state)
    TextView tvDetailsTheCurrentState;
    @BindView(R.id.tv_details_bank_line_number)
    TextView tvDetailsBankLineNumber;
    @BindView(R.id.tv_details_bank_name)
    TextView tvDetailsBankName;
    @BindView(R.id.tv_details_the_bank_account)
    TextView tvDetailsTheBankAccount;
    @BindView(R.id.tv_details_bank_account_name)
    TextView tvDetailsBankAccountName;
    @BindView(R.id.tv_details_amount_of_money)
    TextView tvDetailsAmountOfMoney;
    @BindView(R.id.tv_details_trading_hours)
    TextView tvDetailsTradingHours;
    @BindView(R.id.tv_details_dealer_number)
    TextView tvDetailsDealerNumber;
    @BindView(R.id.tv_details_dealer_name)
    TextView tvDetailsDealerName;
    @BindView(R.id.bt_commit)
    TextView btCommit;
    @BindView(R.id.tv_relation_tip)
    TextView tvRelationTip;
    @BindView(R.id.ll_commit)
    LinearLayout llCommit;
    @BindView(R.id.rv_add_customer)
    RecyclerView mRecyclerView;
    private PayListBean payListBean;
    private UploadImgHelper.OnClickListener clickImgListener;
    private List<String> images = new ArrayList<>();
    private boolean needRefresh = false;
    private String deletImg;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_credit_details;
    }

    @Override
    protected PayDetailsPresenter attachPresenter() {
        return new PayDetailsPresenter();
    }

    @Override
    protected void init() {
        super.init();
        if (getArguments() != null)
            payListBean = (PayListBean) getArguments().getSerializable(Constants.PAY_LIST);
        if (payListBean != null) {
            setTitle(getString(payListBean.getCategory().equals(Constants.PAY_LIST) ? R.string.play_details : R.string.online_declaration));
            setImageData();
            setViewData();
            setView();
        }
    }

    private void setImageData() {
        if (payListBean.getImageList() != null) {
            images.clear();
            for (int i = 0; i < payListBean.getImageList().size(); i++) {
                images.add(payListBean.getImageList().get(i).getImageUrl());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (needRefresh) {
            EventBus.getDefault().post(new MessageEvent(Constants.EVENT_REFRESH));
        }

    }

    /*   01未提交 02待初审 03初审退回 04待复审05复审退回 06已复审 07已退回 */
    private void setView() {
        String statusCode = payListBean.getStatus_code();
        if (TextUtils.equals(statusCode, "01")) {
            llCommit.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(payListBean.getCategory()) && payListBean.getCategory().equals(Constants.ONLINE_DECLARATION)) {
//                setRightMenu(R.drawable.details_more);
                setOptionsMenu(R.menu.menu_more);
            }
            tvRelationTip.setVisibility(View.VISIBLE);
            UploadImgHelper.setImgList(mContext, mRecyclerView, images, getString(R.string.details_confidential_information_add), 9, clickImgListener);
        } else if (TextUtils.equals(statusCode, "02")) {
            UploadImgHelper.setImagListNormal(mContext, mRecyclerView, images);
            setRightMenu("撤回");
        } else if (TextUtils.equals(statusCode, "03")) {
            llCommit.setVisibility(View.VISIBLE);
            if (payListBean.getCategory().equals(Constants.ONLINE_DECLARATION)) {
//                setRightMenu(R.drawable.details_more);
                setOptionsMenu(R.menu.menu_more);
            }
            UploadImgHelper.setImgList(mContext, mRecyclerView, images, getString(R.string.feedback_add_deposit_receipt), 9, clickImgListener);
            tvRelationTip.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(statusCode, "04")
                || TextUtils.equals(statusCode, "05")
                || TextUtils.equals(statusCode, "06")
                || TextUtils.equals(statusCode, "07")) {
            UploadImgHelper.setImagListNormal(mContext, mRecyclerView, images);
        }
    }

    @Override
    protected void clickRightMenu(String text, View actionView) {
        super.clickRightMenu(text, actionView);
        switch (text) {
            case "":
                ListMenuPopupWindow popupWindow = new ListMenuPopupWindow(mContext, R.array.title_credit_info, R.array.title_credit_info_id, this);
//                popupWindow.showAsDropDown(mContentView.findViewById(R.id.iv_right_menu), -DensityUtil.dp2px(6), 0);
                ToolbarHelper.showPopupWindow(popupWindow, actionView);
                break;
            case "修改":
                Map<String, Serializable> params = new HashMap<>();
                params.put(Constants.PAY_LIST, payListBean);
                startFragment(params, new AddOrModifyPayInfoFragment());
                break;
            case "撤回":
                if (payListBean.getCategory().equals(Constants.ONLINE_DECLARATION))
                    mPresenter.backByOnline(payListBean.getId(), payListBean.getRelationId());
                else
                    mPresenter.backByPayList(payListBean.getId(), payListBean.getRelationId());
                break;
        }

    }

    private void setViewData() {
        tvDetailsCreationTime.setText(textEmpty(payListBean.getCreate_date()));
        tvDetailsTheCurrentState.setText(textEmpty(payListBean.getStatus_name()));
        tvDetailsBankLineNumber.setText(textEmpty(payListBean.getRecip_bk_no()));
        tvDetailsBankName.setText(textEmpty(payListBean.getRecip_bk_name()));
        tvDetailsTheBankAccount.setText(textEmpty(payListBean.getRecip_acc_no()));
        tvDetailsBankAccountName.setText(textEmpty(payListBean.getRecip_name()));
        tvDetailsAmountOfMoney.setText(textEmpty(payListBean.getCredit_amount()));
        tvDetailsTradingHours.setText(textEmpty(payListBean.getTransaction_date()));
        tvDetailsDealerNumber.setText(textEmpty(payListBean.getDealer_no()));
        tvDetailsDealerName.setText(textEmpty(payListBean.getDealer_name()));

        btCommit.setOnClickListener(v -> {
            if (payListBean.getCategory().equals(Constants.ONLINE_DECLARATION))
                mPresenter.submitByOnline(payListBean.getId());
            else
                mPresenter.submitByPayList(payListBean.getId());
        });
        mRecyclerView.setNestedScrollingEnabled(false);
        clickImgListener = new UploadImgHelper.OnClickListener() {
            @Override
            public void addImg() {
                ImagePicker.builder().maxSelectCount(9 - images.size()).start(DetailsFragment.this);
            }

            @Override
            public void delImg(String img) {
                deletImg = img;
                showLoading();
                mPresenter.deleteImage(payListBean.getImageList(), images, img);
            }
        };

    }

    int selectedImgSize;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.DEFAULT_REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImagePicker.SELECT_RESULT);
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            selectedImgSize = images.size();
            for (int i = 0; i < images.size(); i++) {
                this.images.add(images.get(i));
                mPresenter.imgloder(mContext, payListBean, images.get(i), i);
            }
            showLoading();
        }
    }


    @Override
    public void onSelect(String title) {
        switch (title) {
            case "删除信息":
                needRefresh = true;
                mPresenter.deletByOnline(payListBean.getId(), payListBean.getRelationId());
                break;
            case "修改信息":
                Map<String, Serializable> params = new HashMap<>();
                params.put(Constants.PAY_LIST, payListBean);
                startFragment(params, new AddOrModifyPayInfoFragment());
                break;
        }
    }

    @Override
    public void onSuccess(String stateCode) {
        needRefresh = true;
        dismissLoading();
        switch (stateCode) {
            case PayDetailsPresenter.tj:
            case PayDetailsPresenter.ch:
            case PayDetailsPresenter.sc:
                finishFragment();
                break;
            case PayDetailsPresenter.tjpz:
                UploadImgHelper.setImgList(mContext, mRecyclerView, images, getString(R.string.details_confidential_information_add), 9, clickImgListener);
                break;
            case PayDetailsPresenter.scpz:
                images.remove(deletImg);
                UploadImgHelper.setImgList(mContext, mRecyclerView, images, getString(R.string.details_confidential_information_add), 9, clickImgListener);
                break;
        }
    }


    @Override
    public void onFail(String errorMsg) {
        dismissLoading();
        showShortToast(errorMsg);
    }

    @Override
    public void onRelationSuccess(UploadByRelationBean bean, int loopIndex) {
        dismissLoading();
        payListBean.getImageList().add(new PayListBean.ImageListBean(bean.getFileId(), URL_GET_PATH_IMG + bean.getFileUrl()));
        UploadImgHelper.setImgList(mContext, mRecyclerView, images, getString(R.string.details_confidential_information_add), 9, clickImgListener);
        if (payListBean.getCategory().equals(Constants.PAY_LIST))
            mPresenter.submitRelationByPayList(payListBean.getId(), bean.getRelationId(), selectedImgSize, loopIndex);

    }

    @Override
    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
        super.onFinishResult(requestCode, resultCode, result);
        if (resultCode == Constants.RESULT_CODE_OPERATE_SUCCESS) {
            images.clear();
            setImageData();
            setViewData();
            setTitle(getString(payListBean.getCategory().equals(Constants.PAY_LIST) ? R.string.play_details : R.string.online_declaration));
            setView();
            needRefresh = true;
        }
    }


}
