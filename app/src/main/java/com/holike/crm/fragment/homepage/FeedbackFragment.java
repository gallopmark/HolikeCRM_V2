package com.holike.crm.fragment.homepage;

import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gallopmark.imagepicker.model.ImagePicker;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.homepage.FeedbackRecordActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.manager.FlowLayoutManager;
import com.holike.crm.presenter.activity.FeedbackPresenter;
import com.holike.crm.view.activity.FeedbackView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/7/18.
 * 售后体验反馈
 */

public class FeedbackFragment extends MyFragment<FeedbackPresenter, FeedbackView> implements FeedbackView {
    @BindView(R.id.et_feedback_order)
    EditText etOrder;
    @BindView(R.id.tv_feedback_problem_classify)
    TextView tvProblemClassify;
    @BindView(R.id.rv_feedback_problem_classify1)
    RecyclerView rvProblemClassify1;
    @BindView(R.id.rv_feedback_problem_classify2)
    RecyclerView rvProblemClassify2;
    @BindView(R.id.et_feedback_problem_describe)
    EditText etProblemDescribe;
    @BindView(R.id.rv_feedback_problem_describe)
    RecyclerView rvDescribe;
    @BindView(R.id.et_feedback_loss)
    EditText etLoss;
    @BindView(R.id.et_feedback_solution)
    EditText etSolution;
    @BindView(R.id.rv_feedback_solution)
    RecyclerView rvSolution;
    @BindView(R.id.btn_feedback_save)
    TextView btnSave;

    private HomepageBean.QuestionTypeDataBean questionTypeDataBean;
    private List<HomepageBean.QuestionTypeDataBean.ItemListBean> itemListBeans = new ArrayList<>();
    private List<String> imgsDescrribe = new ArrayList<>();
    private List<String> imgsLoss = new ArrayList<>();
    protected UploadImgHelper.OnClickListener descrribeListener, lossListener;
    private boolean isDescrribe;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected FeedbackPresenter attachPresenter() {
        return new FeedbackPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.feedback));
        setLeft(getString(R.string.homepage));
        setRightMenu(getString(R.string.feedback_record));
        rvProblemClassify1.setNestedScrollingEnabled(false);
        rvProblemClassify2.setNestedScrollingEnabled(false);
        rvDescribe.setNestedScrollingEnabled(false);
        rvSolution.setNestedScrollingEnabled(false);
        rvProblemClassify1.setLayoutManager(new LinearLayoutManager(mContext));
        rvProblemClassify2.setLayoutManager(new FlowLayoutManager());
        rvDescribe.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvSolution.setLayoutManager(new GridLayoutManager(mContext, 3));
        mPresenter.stateChangeListener(etOrder);
        mPresenter.stateChangeListener(tvProblemClassify);
        mPresenter.stateChangeListener(etProblemDescribe);
        mPresenter.stateChangeListener(etLoss);
        initImgList();
    }

    /**
     * 反馈记录
     */
    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        super.clickRightMenu(menuText, actionView);
        startActivity(FeedbackRecordActivity.class);
    }

    /**
     * 初始化上传图片列表
     */
    private void initImgList() {
        descrribeListener = new UploadImgHelper.OnClickListener() {
            @Override
            public void addImg() {
                ImagePicker.builder().maxSelectCount(9 - imgsDescrribe.size()).start(FeedbackFragment.this);
                isDescrribe = true;
            }

            @Override
            public void delImg(String img) {
                imgsDescrribe.remove(img);
                UploadImgHelper.setImgList(mContext, rvDescribe, imgsDescrribe, getString(R.string.feedback_problem_upload_img), 9, descrribeListener);
                stateChange();
            }
        };
        lossListener = new UploadImgHelper.OnClickListener() {
            @Override
            public void addImg() {
                ImagePicker.builder().maxSelectCount(9 - imgsLoss.size()).start(FeedbackFragment.this);
                isDescrribe = false;
            }

            @Override
            public void delImg(String img) {
                imgsLoss.remove(img);
                UploadImgHelper.setImgList(mContext, rvSolution, imgsLoss, getString(R.string.feedback_solution_img), 9, lossListener);
            }
        };
        UploadImgHelper.setImgList(mContext, rvDescribe, imgsDescrribe, getString(R.string.feedback_problem_upload_img), 9, descrribeListener);
        UploadImgHelper.setImgList(mContext, rvSolution, imgsLoss, getString(R.string.feedback_solution_img), 9, lossListener);
    }

    @OnClick({R.id.tv_feedback_find_order, R.id.ll_feedback_problem_classify, R.id.btn_feedback_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_feedback_find_order:
                startFragment(null, new FeedbackGetOrderFragment());
                break;
            case R.id.ll_feedback_problem_classify:
                showProblemList();
                break;
            case R.id.btn_feedback_save:
                showLoading();
                mPresenter.save(mContext, etOrder.getText().toString(), questionTypeDataBean.getQuestionId(), mPresenter.getItemId(itemListBeans), etProblemDescribe.getText().toString(), imgsDescrribe, etLoss.getText().toString(), etSolution.getText().toString(), imgsLoss);
                break;
        }
    }

    /**
     * 显示问题列表
     */
    protected void showProblemList() {
        hideSoftInput(etOrder);
        rvProblemClassify1.setVisibility(rvProblemClassify1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        List<HomepageBean.QuestionTypeDataBean> questions = IntentValue.getInstance().getHomeQuestionData();
        if (questions != null && !questions.isEmpty()) {
            rvProblemClassify1.setAdapter(new CommonAdapter<HomepageBean.QuestionTypeDataBean>(mContext, questions) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_feedback_problem;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, HomepageBean.QuestionTypeDataBean bean, int position) {
                    TextView tv = holder.obtainView(R.id.tv_item_rv_feedback_problem);
                    tv.setText(bean.getQuestionName());
                    holder.itemView.setOnClickListener(v -> {
                        questionTypeDataBean = bean;
                        tvProblemClassify.setText(bean.getQuestionName());
                        rvProblemClassify1.setVisibility(View.GONE);
                        showProblemItemList(bean.getItemList());
                    });
                }

            });
        }
    }

    /**
     * 显示问题子项列表
     */
    protected void showProblemItemList(List<HomepageBean.QuestionTypeDataBean.ItemListBean> listBeans) {
        itemListBeans.clear();
        stateChange();
        rvProblemClassify2.setVisibility(View.VISIBLE);
        mPresenter.showProblemItemList(mContext, rvProblemClassify2, listBeans, itemListBeans);
    }

    /**
     * 保存成功
     */
    @Override
    public void saveSuccess(String success) {
        dismissLoading();
        showShortToast(success);
        finishFragment();
    }

    /**
     * 保存失败
     */
    @Override
    public void saveFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * TextView状态改变
     */
    @Override
    public void stateChange() {
        mPresenter.stateChage(etOrder.getText().toString(), questionTypeDataBean, itemListBeans, etProblemDescribe.getText().toString(), imgsDescrribe, etLoss.getText().toString(), btnSave);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImagePicker.DEFAULT_REQUEST_CODE) {
            if (data != null) {
                //获取选择器返回的数据
                List<String> images = new ArrayList<>();
                ArrayList<String> list = data.getStringArrayListExtra(ImagePicker.SELECT_RESULT);
                if (list != null && !list.isEmpty()) {
                    images.addAll(list);
                }
                if (isDescrribe) {
                    imgsDescrribe.addAll(images);
                    stateChange();
                } else {
                    imgsLoss.addAll(images);
                }
                if (isDescrribe) {
                    UploadImgHelper.setImgList(mContext, rvDescribe, imgsDescrribe, getString(R.string.feedback_problem_upload_img), 9, descrribeListener);
                } else {
                    UploadImgHelper.setImgList(mContext, rvSolution, imgsLoss, getString(R.string.feedback_solution_img), 9, lossListener);
                }
            }
        }
    }
}
