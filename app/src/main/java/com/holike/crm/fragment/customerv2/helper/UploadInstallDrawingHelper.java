package com.holike.crm.fragment.customerv2.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.helper.IImageSelectHelper;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/7/30.
 * Copyright holike possess 2019.
 * 上传安装图纸
 */
public class UploadInstallDrawingHelper extends IImageSelectHelper {

    private String mHouseId,//房屋id
            mInstallId, //安装单id(必填)
            mInstallUserId,//安装工用户id(安装反馈时上传图片记录必填)
            mRemark; //备注信息
    private List<String> mSelectedImages;  //已选择的图片 （从客户管理详情也带过来，属于网络图片）
    private List<String> mInstallImages; //删除图片去schemeImgId，后台真是骚 搞不懂？？？？？？？？
    private List<String> mRemovedImages; //被移除的网络图片
    private EditText mRemarkEditText;
    private UploadInstallDrawingCallback mCallback;

    public UploadInstallDrawingHelper(BaseFragment<?, ?> fragment, UploadInstallDrawingCallback callback) {
        super(fragment);
        this.mCallback = callback;
        mSelectedImages = new ArrayList<>();
        mInstallImages = new ArrayList<>();
        mRemovedImages = new ArrayList<>();
        obtainBundleValue(fragment.getArguments());
        WrapperRecyclerView recyclerView = fragment.getContentView().findViewById(R.id.wrapperRecyclerView);
        setup(recyclerView);
        TextView tvSave = fragment.getContentView().findViewById(R.id.tvSave);
        tvSave.setOnClickListener(view -> onSaved());
    }

    private void obtainBundleValue(Bundle bundle) {
        if (bundle != null) {
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
            mInstallId = bundle.getString("installId");
            mInstallUserId = bundle.getString("installUserId");
            mRemark = bundle.getString("remark");
            ArrayList<String> images = bundle.getStringArrayList("images");
            ArrayList<String> installImages = bundle.getStringArrayList("installImages");
            if (images != null && !images.isEmpty()) {
                mSelectedImages.addAll(images);
            }
            if (installImages != null && !installImages.isEmpty()) {
                mInstallImages.addAll(installImages);
            }
        }
    }

    private void setup(WrapperRecyclerView recyclerView) {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_customer_installdrawing, new LinearLayout(mContext), false);
        mRemarkEditText = headerView.findViewById(R.id.et_remark);
        recyclerView.addHeaderView(headerView);
        int space = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, space, true, 1));
        setupSelectImages(recyclerView, R.string.tips_add_install_drawings_images, true, 18); //安装图纸最多添加18张
        mImageHelper.imageOptionsListener((position, path) -> {
            mImageHelper.remove(position);
            if (mSelectedImages.contains(path) && position >= 0 && position < mInstallImages.size()) {  //删除网络图片
                mSelectedImages.remove(position);
                String imageUrl = mInstallImages.remove(position);
                if (!mRemovedImages.contains(imageUrl)) {
                    mRemovedImages.add(imageUrl);
                }
            }
        }).setImagePaths(new ArrayList<>(mSelectedImages));
        mRemarkEditText.setText(mRemark);
    }

    private void onSaved() {
        String remark = mRemarkEditText.getText().toString();
        List<String> images = mImageHelper.getSelectedImages();
        if (images.isEmpty()) {  //安装图纸必选
            mCallback.onRequired(mContext.getString(R.string.please) + mContext.getString(R.string.tips_add_install_drawings_images));
        } else {
            List<String> targetImages = new ArrayList<>(); //只上传新添加进来的图片，即从相册新增进来的图片
            for (String image : images) { //过滤掉从详情带过来的图片（不作上传，从相册新添加进来的才做上传）
                if (!mSelectedImages.contains(image)) {
                    targetImages.add(image);
                }
            }
            mCallback.onSaved(mRemovedImages, mHouseId, mInstallId, mInstallUserId, remark, targetImages);
        }
    }

    public interface UploadInstallDrawingCallback {
        void onRequired(String text);

        void onSaved(List<String> removedImages, String houseId, String installId, String installUserId, String remark, List<String> imagePaths);
    }
}
