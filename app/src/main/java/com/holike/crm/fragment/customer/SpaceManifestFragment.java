package com.holike.crm.fragment.customer;

import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.SpaceManifestBean;
import com.holike.crm.popupwindown.SpaceManifestPopupWindow;
import com.holike.crm.presenter.fragment.SpaceManifestPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.SpaceManifestView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 空间清单
 */
public class SpaceManifestFragment extends MyFragment<SpaceManifestPresenter, SpaceManifestView> implements SpaceManifestView, SpaceManifestPopupWindow.OnSelectListener {
    @BindView(R.id.tv_space_manifest_info_current)
    TextView tvSpaceTitle;
    //    @BindView(R.id.tv_space_content)
//    TextView tvSpaceContent;
    @BindView(R.id.rv_current_space)
    RecyclerView rvCurrentSpace;
    @BindView(R.id.rv_space_manifest_subtitle)
    RecyclerView rvSubtitle;
    @BindView(R.id.rv_space_manifest_content)
    RecyclerView rvContent;
    @BindView(R.id.ll_space_manifest_drop_down)
    LinearLayout llDropDown;
    @BindView(R.id.iv_drop)
    ImageView ivDrop;
    private String orderId;
    private SpaceManifestPopupWindow mPopupWindow;

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.space_manifest_info));
        if (getArguments() != null) {
            orderId = (String) getArguments().getSerializable(Constants.ORDER_ID);
        }
        mPresenter.getData(orderId);

    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_space_manifest;
    }

    @Override
    protected SpaceManifestPresenter attachPresenter() {
        return new SpaceManifestPresenter();
    }

    @Override
    public void onSuccess(List<SpaceManifestBean> beans) {
        tvSpaceTitle.setText(beans.get(0).getHouseName());
        mPopupWindow = new SpaceManifestPopupWindow(mContext, beans, ivDrop, this);
        mPresenter.setSideAdapter(mContext, rvCurrentSpace, beans.get(0).getDataList());
        mPresenter.setSubtitleAdapter(mContext, rvSubtitle, beans.get(0).getDataList());
        mPresenter.setFormAdapter(mContext, rvContent, beans.get(0).getDataList().get(0), 0);
    }

    @Override
    public void onFail(String errorMsg) {
        noResult(getString(R.string.tips_no_corresponding_space_listing));
    }

    @Override
    public void onSelectInfo(int position, SpaceManifestBean.DataListBean bean) {
        mPresenter.setFormAdapter(mContext, rvContent, bean, position);
    }

    @Override
    public void onSubTitleInfo(int position, SpaceManifestBean.DataListBean bean) {
        mPresenter.setFormAdapter(mContext, rvContent, bean, 0);
    }

    @Override
    public void onPopupSelect(SpaceManifestBean bean) {
        tvSpaceTitle.setText(bean.getHouseName());

        mPresenter.setSideAdapter(mContext, rvCurrentSpace, bean.getDataList());
        mPresenter.setSubtitleAdapter(mContext, rvSubtitle, bean.getDataList());
        mPresenter.setFormAdapter(mContext, rvContent, bean.getDataList().get(0), 0);
    }

    @OnClick(R.id.ll_space_manifest_drop_down)
    public void onViewClicked(View v) {
        if (v.getId() == R.id.ll_space_manifest_drop_down) {
            if (mPopupWindow != null)
                mPopupWindow.showAsDropDown(llDropDown, 0, 0, Gravity.TOP);
        }
    }
}
