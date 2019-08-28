package com.holike.crm.presenter.fragment;

import android.content.Context;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.bean.UploadByRelationBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.model.fragment.PayDetailsModel;
import com.holike.crm.view.fragment.PayInfoView;

import java.util.List;

public class PayInfoPresenter extends BasePresenter<PayInfoView, PayDetailsModel> {
    private UploadImgHelper uploadImgHelper = new UploadImgHelper();

    /**
     * 修改-在线申报
     *
     * @param creditAmout
     * @param date
     * @param id
     * @param recipAccNo
     * @param recipBkName
     * @param recipBkNo
     * @param recipName
     * @param relationId
     */
    public void modifyByOnline(String creditAmout, String date, String id, String recipAccNo, String recipBkName, String recipBkNo, String recipName, String relationId) {
        model.getDataOnline(creditAmout, date, id, recipAccNo, recipBkName, recipBkNo, recipName, relationId, PayDetailsPresenter.xg, new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (getView() != null)
                    getView().onSuccess(PayDetailsPresenter.xg);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }


    /**
     * 新增-在线申报
     * 直接回到列表页面
     *
     * @param creditAmout
     * @param date
     * @param recipAccNo
     * @param recipBkName
     * @param recipBkNo
     * @param recipName
     * @param relationId
     */
    public void addByOnline(String creditAmout, String date, String recipAccNo, String recipBkName, String recipBkNo, String recipName, String relationId) {
        model.getDataOnline(creditAmout, date, "", recipAccNo, recipBkName, recipBkNo, recipName, relationId, PayDetailsPresenter.xz, new PayDetailsModel.PayDetailsListner() {
            @Override
            public void onSuccess() {
                if (getView() != null)
                    getView().onSuccess(PayDetailsPresenter.xz);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }


    public void selectImg(Context mContext, String relationId, String img, int uploadIndex) {
        uploadImgHelper.uploadByRelation(mContext, relationId, img, new UploadImgHelper.UploadByRelationListener() {
            @Override
            public void success(UploadByRelationBean bean) {
                if (getView() != null)
                    getView().onRelationSuccess(bean, uploadIndex);
            }

            @Override
            public void failed(String failed) {
                if(getView()!=null)
                    getView().onRelationFailed(failed);
            }
        });

    }

    /**
     * 删除凭证
     *
     * @param img
     */
    public void deleteImage(List<PayListBean.ImageListBean> imgListBean, List<String> imgs, String img) {
        if (imgListBean.size() == 0) return;
        int index = imgs.indexOf(img);
        String id = imgListBean.get(index).getImageId();
        MyHttpClient.get(UrlPath.URL_GET_DELET_IMG + id, new RequestCallBack<String>() {
            @Override

            public void onFailed(String failReason) {
                if (getView() != null)
                    getView().onFail(failReason);
            }

            @Override
            public void onSuccess(String result) {
                imgListBean.remove(index);
                if (getView() != null)
                    getView().onSuccess(PayDetailsPresenter.scpz);
            }
        });
    }

    @Override
    public void deAttach() {
        super.deAttach();
        uploadImgHelper.cancel();
    }
}
