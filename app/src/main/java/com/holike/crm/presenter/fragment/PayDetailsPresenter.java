package com.holike.crm.presenter.fragment;

import android.content.Context;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.bean.UploadByRelationBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.model.fragment.PayDetailsModel;
import com.holike.crm.view.fragment.PayDetailsView;

import java.util.List;

public class PayDetailsPresenter extends BasePresenter<PayDetailsView, PayDetailsModel> {
    //                        01修改      02撤回      03 删除    04 提交    05提交凭证   06新增
    public static final String xg = "01", ch = "02", sc = "03", tj = "04", tjpz = "05", xz = "06", scpz = "07";

    /**
     * 撤回-打款信息
     *
     * @param id
     * @param relationId
     */
    public void backByPayList(String id, String relationId) {
        model.getDataPayList(id, ch, relationId, new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (getView() != null)
                    getView().onSuccess(ch);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }

    /**
     * 提交-打款信息
     *
     * @param id
     */
    public void submitByPayList(String id) {
        model.getDataPayList(id, tj, "", new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (getView() != null)
                    getView().onSuccess(tj);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }


    /**
     * 上传凭证
     *
     * @param id
     * @param relationId 凭证id
     */
    public void submitRelationByPayList(String id, String relationId, int imageSize, int loopSize) {
        model.getDataPayList(id, tjpz, relationId, new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (imageSize == loopSize + 1) {
                    if (getView() != null)
                        getView().onSuccess(tjpz);
                } else {
                    //上传未完成
                }
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }


    /**
     * 撤回-在线申报
     *
     * @param id
     * @param relationId
     */
    public void backByOnline(String id, String relationId) {
        model.getDataOnline("", "", id, "", "", "", "", relationId, ch, new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (getView() != null)
                    getView().onSuccess(ch);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }

    /**
     * 删除-在线申报
     *
     * @param id
     * @param relationId
     */
    public void deletByOnline(String id, String relationId) {
        model.getDataOnline("", "", id, "", "", "", "", relationId, sc, new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (getView() != null)
                    getView().onSuccess(sc);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }


    /**
     * 提交-在线申报
     *
     * @param id
     */
    public void submitByOnline(String id) {
        model.getDataOnline("", "", id, "", "", "", "", "", tj, new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (getView() != null)
                    getView().onSuccess(tj);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }

    /**
     * 提交-在线申报
     *
     * @param img
     */
    public void deleteImage(List<PayListBean.ImageListBean> imgListBean, List<String> imgs, String img) {
        if (imgListBean.size() == 0) return;
        int index = imgs.indexOf(img);
        String id = imgListBean.get(index).getImageId();
        MyHttpClient.get(UrlPath.URL_GET_DELET_IMG + id, new RequestCallBack<CustomerDetailBean.PersonalBean>() {
            @Override
            public void onFailed(String failReason) {
                if (getView() != null)
                    getView().onFail(failReason);
            }

            @Override
            public void onSuccess(CustomerDetailBean.PersonalBean result) {
                imgListBean.remove(index);
                if (getView() != null)
                    getView().onSuccess(scpz);
            }
        });
    }

    public void imgloder(Context mContext, PayListBean payListBean, String imgs, int uploadIndex) {
        UploadImgHelper.getInstance().uploadByRelation(mContext, payListBean.getRelationId(), imgs, new UploadImgHelper.UploadByRelationListener() {
            @Override
            public void success(UploadByRelationBean bean) {
                if (getView() != null)
                    getView().onRelationSuccess(bean, uploadIndex);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().onFail(failed);
            }
        });
    }
}
