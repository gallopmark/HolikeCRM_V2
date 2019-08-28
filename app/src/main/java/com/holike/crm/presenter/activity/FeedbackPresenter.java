package com.holike.crm.presenter.activity;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.model.activity.FeedbackModel;
import com.holike.crm.view.activity.FeedbackView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/7/16.
 * 售后体验反馈
 */

public class FeedbackPresenter extends BasePresenter<FeedbackView, FeedbackModel> {

    /**
     * 保存
     *
     * @param orderNo
     * @param questionId
     * @param itemId
     * @param questionDescribe
     * @param questionImg
     * @param expenseId
     * @param solveDescribe
     * @param solveImg
     */
    public void save(Context context, final String orderNo, final String questionId, final String itemId, final String questionDescribe, List<String> questionImg, final String expenseId, final String solveDescribe, List<String> solveImg) {
        List<List<String>> lists = new ArrayList<>();
        lists.add(questionImg);
        lists.add(solveImg);
        model.uploadImg(context, lists, new UploadImgHelper.UploadListener() {
            @Override
            public void success(List<List<String>> imgUrls) {
                model.save(orderNo, questionId, itemId, questionDescribe, getImgUrl(imgUrls.get(0)), expenseId, solveDescribe == null ? "" : solveDescribe, getImgUrl(imgUrls.get(1)), new FeedbackModel.SaveListener() {
                    @Override
                    public void failed(String failed) {
                        if (getView() != null)
                            getView().saveFailed(failed);
                    }

                    @Override
                    public void success(String success) {
                        if (getView() != null)
                            getView().saveSuccess("提交成功");
                    }
                });
            }

            @Override
            public void failed(String result) {
                if (getView() != null)
                    getView().saveFailed(result);
            }
        });

    }

    /**
     * 图片列表转化string
     *
     * @param list
     * @return
     */
    public String getImgUrl(List<String> list) {
        String result = "";
        for (String url : list) {
            if (TextUtils.isEmpty(result)) {
                result = url;
            } else {
                result = result + "@" + url;
            }
        }
        return result;
    }

    /**
     * 问题子项列表转化string
     *
     * @param itemListBeans
     * @return
     */
    public String getItemId(List<HomepageBean.QuestionTypeDataBean.ItemListBean> itemListBeans) {
        String result = null;
        for (HomepageBean.QuestionTypeDataBean.ItemListBean bean : itemListBeans) {
            if (result == null) {
                result = bean.getItemId();
            } else {
                result = result + "@" + bean.getItemId();
            }
        }
        return result;
    }


    /**
     * TextView状态改变监听
     *
     * @param textView
     */
    public void stateChangeListener(TextView textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getView() != null)
                    getView().stateChange();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 保存状态改变
     *
     * @param orderNo
     * @param questionTypeDataBean
     * @param itemListBeans
     * @param questionDescribe
     * @param loss
     * @param btnSave
     */
    public void stateChage(String orderNo, HomepageBean.QuestionTypeDataBean questionTypeDataBean, List<HomepageBean.QuestionTypeDataBean.ItemListBean> itemListBeans, String questionDescribe, List<String> imgsDescribe, String loss, TextView btnSave) {
        if (orderNo.length() > 0 && questionTypeDataBean != null && itemListBeans.size() > 0 && questionDescribe.length() > 0 && imgsDescribe.size() > 0 && loss.length() > 0) {
            btnSave.setEnabled(true);
            btnSave.setBackgroundResource(R.drawable.bg_btn_login_can_click);
        } else {
            btnSave.setEnabled(false);
            btnSave.setBackgroundResource(R.drawable.bg_btn_login_cannot_click);
        }
    }

    /**
     * 问题子项列表
     *
     * @param context
     * @param rv
     * @param listBeans
     * @param itemListBeans
     */
    public void showProblemItemList(final Context context, RecyclerView rv, List<HomepageBean.QuestionTypeDataBean.ItemListBean> listBeans, final List<HomepageBean.QuestionTypeDataBean.ItemListBean> itemListBeans) {
        for (HomepageBean.QuestionTypeDataBean.ItemListBean bean : listBeans) {
            bean.setSelect(false);
        }
        rv.setAdapter(new CommonAdapter<HomepageBean.QuestionTypeDataBean.ItemListBean>(context, listBeans) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_feedback_problem2;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, HomepageBean.QuestionTypeDataBean.ItemListBean bean, int position) {
                TextView tv = holder.obtainView(R.id.tv_item_rv_feedback_problem2);
                tv.setText(bean.getItemName());
                if (bean.isSelect()) {
                    tv.setBackgroundResource(R.drawable.bg_btn_rounded_corner_default_13);
                    tv.setTextColor(ContextCompat.getColor(context, R.color.color_while));
                } else {
                    tv.setBackgroundResource(R.drawable.bg_btn_rounded_corner_border_13);
                    tv.setTextColor(ContextCompat.getColor(context, R.color.textColor5));
                }
                holder.itemView.setOnClickListener(v -> {
                    bean.setSelect(!bean.isSelect());
                    if (bean.isSelect()) {
                        itemListBeans.add(bean);
                    } else {
                        itemListBeans.remove(bean);
                    }
                    if (getView() != null)
                        getView().stateChange();
                    notifyDataSetChanged();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        model.cancel();
    }
}
