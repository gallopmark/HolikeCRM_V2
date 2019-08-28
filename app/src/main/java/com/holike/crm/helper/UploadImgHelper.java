package com.holike.crm.helper;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.UploadBean;
import com.holike.crm.bean.UploadByRelationBean;
import com.holike.crm.bean.UploadCallBackBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.manager.ObserverManager;
import com.holike.crm.util.Constants;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.MD5Util;
import com.holike.crm.util.MultipartUtils;
import com.holike.crm.util.SharedPreferencesUtils;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

/**
 * Created by wqj on 2018/7/17.
 * 上传图片类
 */

public class UploadImgHelper {

    public UploadImgHelper() {
    }

    static class UploadImgHelperHolder {
        static UploadImgHelper instance = new UploadImgHelper();
    }

    public static UploadImgHelper getInstance() {
        return UploadImgHelperHolder.instance;
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private boolean cancel;

    public void cancel() {
        cancel = true;
        compositeDisposable.dispose();
    }

    public void uploadImg(Context context, final List<List<String>> lists, final UploadListener uploadListener) {
        cancel = false;
        MyHttpClient.postByCliId(UrlPath.URL_UPLOAD_PARAM, null, null, new RequestCallBack<UploadBean>() {
            @Override
            public void onFailed(String failReason) {
                uploadListener.failed(failReason);
            }

            @Override
            public void onSuccess(final UploadBean bean) {
                compositeDisposable.add(ObserverManager.getInstance().doTaskJust(lists, new ObserverManager.ListCallBack() {
                    @Override
                    public void task(List<List<String>> results) {
                        List<String> result = new ArrayList<>();
                        for (List<String> list : lists) {
                            if (cancel) return;
                            for (String path : list) {
                                if (cancel) return;
                                try {
                                    path = compressImg(context, path);
                                    final String key = MD5Util.MD5(String.valueOf(System.currentTimeMillis()) + UUID.randomUUID().toString()) + ".jpg";
                                    String endpoint = bean.getEndpoint();
                                    OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(bean.getAccessId(), bean.getAccessSecret(), URLDecoder.decode(bean.getSecurityToken()));
                                    OSS oss = new OSSClient(MyApplication.getInstance(), endpoint, credentialProvider);
                                    PutObjectRequest put = new PutObjectRequest(bean.getBucketName(), key, path);
                                    oss.putObject(put);
                                    String imgUrl = bean.getHost() + key;
                                    result.add(imgUrl);
                                } catch (Exception e) {
                                }
                            }
                            results.add(result);
                        }
                    }

                    @Override
                    public void onComplete(List<List<String>> list) {
                        if (list.size() == 0 && !cancel) {
                            uploadListener.failed("上传图片失败");
                        } else if (lists.size() > 0) {
                            uploadListener.success(list);
                        }
                    }
                }));
            }
        });
    }

    public interface UploadListener {
        void failed(String result);

        void success(List<List<String>> lists);
    }


    /**
     * 上传图片
     */
    public void upload(Context mContext, final String customerId, final String optCode, final String houseId, final String customerStatus, final String type, final List<String> filePaths, final UploadImgListener listener) {
        compositeDisposable.add(Observable.just(filePaths).subscribeOn(Schedulers.io()).map(imgPaths -> {
            List<String> compressImgPaths = new ArrayList<>();
            for (String path : imgPaths) {
                compressImgPaths.add(compressImg(mContext, path));
            }
            return compressImgPaths;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            Map<String, String> params = new HashMap<>();
            params.put("customerId", customerId);
            params.put("optCode", optCode);
            params.put("houseId", houseId);
            params.put("customerStatus", customerStatus);
            params.put("type", type);
            Map<String, String> header = new HashMap<>();
            header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
            header.put(Constants.USER_PSW, SharedPreferencesUtils.getString(Constants.USER_PSW));
//            List<MultipartBody.Part> parts = files2Parts(s, MediaType.parse("image"));
            List<MultipartBody.Part> parts = MultipartUtils.pathsToMultipartBodyParts("file", s);
            MyHttpClient.upload(UrlPath.URL_UPLOAD_FILE, header, params, parts, new RequestCallBack<UploadCallBackBean>() {
                @Override
                public void onFailed(String failReason) {
                    listener.failed(failReason);
                }

                @Override
                public void onSuccess(UploadCallBackBean result) {
                    LogCat.e("上传图片成功", result.toString());
                    listener.success(result);
                }
            });
        }, throwable -> {
            if (!TextUtils.isEmpty(throwable.getMessage()))
                LogCat.e("上传图片失败", throwable.getMessage());
        }));
    }

    /**
     * 上传图片
     */
    public void uploadByRelation(Context mContext, final String relationId, final String filePaths, final UploadByRelationListener listener) {
        compositeDisposable.add(Observable.just(filePaths).subscribeOn(Schedulers.io()).map(imgPaths -> {
            List<String> compressImgPaths = new ArrayList<>();
            compressImgPaths.add(compressImg(mContext, filePaths));
            return compressImgPaths;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            Map<String, String> header = new HashMap<>();
            header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
            header.put(Constants.USER_PSW, SharedPreferencesUtils.getString(Constants.USER_PSW));
            if (s.size() == 0) {
                UploadByRelationBean bean = new UploadByRelationBean();
                bean.setRelationId(relationId);
                listener.success(bean);
                return;
            }
            List<MultipartBody.Part> parts = MultipartUtils.pathsToMultipartBodyPartsByMD5("file", s);
//            List<MultipartBody.Part> parts = files2PartsByRelation(s, "file", MediaType.parse("image"));
            MyHttpClient.upload(UrlPath.URL_UPLOAD_FILE_BANK, header, relationId, parts, new RequestCallBack<UploadByRelationBean>() {
                @Override
                public void onFailed(String failReason) {
                    listener.failed(failReason);
                }

                @Override
                public void onSuccess(UploadByRelationBean result) {
                    LogCat.e("上传图片成功", result.toString());
                    listener.success(result);
                }
            });
        }, throwable -> {
            if (!TextUtils.isEmpty(throwable.getMessage()))
                LogCat.e("上传图片失败", throwable.getMessage());
        }));
    }

    public interface UploadImgListener {
        void success(UploadCallBackBean bean);

        void failed(String failed);
    }

    public interface UploadByRelationListener {
        void success(UploadByRelationBean bean);

        void failed(String failed);
    }

    /**
     * 压缩图片
     */
    public String compressImg(Context context, String imgPath) {
        try {
            return Luban.with(context).load(imgPath).get(imgPath).getAbsolutePath();
        } catch (Exception e) {
            return imgPath;
        }
    }

    /**
     * 图片地址转换成Part
     */
    @Deprecated
    public List<MultipartBody.Part> files2Parts(List<String> filePaths, MediaType imageType) {
        List<MultipartBody.Part> parts = new ArrayList<>(filePaths.size());
        for (String filePath : filePaths) {
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(imageType, file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(file.getPath(), file.getName(), requestBody);

            parts.add(part);
        }
        return parts;
    }

    /**
     * 图片地址转换成Part
     */
    @Deprecated
    public List<MultipartBody.Part> files2PartsByRelation(List<String> filePaths, String formKey, MediaType imageType) {
        List<MultipartBody.Part> parts = new ArrayList<>(filePaths.size());
        for (String filePath : filePaths) {
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(imageType, file);
            final String key = MD5Util.MD5(String.valueOf(System.currentTimeMillis()) + UUID.randomUUID().toString()) + ".jpg";
            MultipartBody.Part part = MultipartBody.Part.createFormData(formKey, key, requestBody);
            parts.add(part);
        }
        return parts;
    }

    public static void setImgList(final Context context, RecyclerView recyclerView, final List<String> imgPaths, final String text, final int size, final OnClickListener listener) {
        final List<String> list = new ArrayList<>(imgPaths);
        if (imgPaths.size() < size) {
            list.add("add");
        }
        recyclerView.setAdapter(new CommonAdapter<String>(context, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_deposit_amount;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, String s, int position) {
                FrameLayout flDepositAmount = holder.obtainView(R.id.fl_deposit_amount_img);
                ImageView ivDepositAmount = holder.obtainView(R.id.iv_deposit_amount_img);
                ImageView ivDepositAmountDel = holder.obtainView(R.id.iv_deposit_amount_img_del);
                FrameLayout flDepositAmountAdd = holder.obtainView(R.id.fl_deposit_amount_add);
                TextView tvDepositAmountAdd = holder.obtainView(R.id.tv_deposit_amount_count);
                if (TextUtils.equals(s, "add")) {
                    flDepositAmountAdd.setVisibility(View.VISIBLE);
                    flDepositAmount.setVisibility(View.GONE);
                    tvDepositAmountAdd.setText(imgPaths.size() == 0 ? Html.fromHtml(text.replace("*", "<font color = \"#FF0018\">*</font>")) : imgPaths.size() + "/" + size);
                    flDepositAmountAdd.setOnClickListener(v -> listener.addImg());
                } else {
                    flDepositAmountAdd.setVisibility(View.GONE);
                    flDepositAmount.setVisibility(View.VISIBLE);
                    Glide.with(context).load(s).apply(new RequestOptions().error(0).placeholder(R.drawable.loading_photo)).into(ivDepositAmount);
//                    Glide.with(context).load(s).error(0).placeholder(R.drawable.loading_photo).into(ivDepositAmount);
                    ivDepositAmountDel.setOnClickListener(v -> listener.delImg(s));
                    ivDepositAmount.setOnClickListener(v -> {
                        List<String> images = new ArrayList<>(imgPaths);
                        images.remove("add");
                        PhotoViewActivity.openImage((Activity) context, position, images);
                    });
                }
            }
        });
    }

    public static void setImagListNormal(final Context context, RecyclerView recyclerView, final List<String> imgPaths) {
        recyclerView.setAdapter(new CommonAdapter<String>(context, imgPaths) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_deposit_amount;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, String s, int position) {
                FrameLayout flDepositAmount = holder.obtainView(R.id.fl_deposit_amount_img);
                ImageView ivDepositAmount = holder.obtainView(R.id.iv_deposit_amount_img);
                FrameLayout flDepositAmountAdd = holder.obtainView(R.id.fl_deposit_amount_add);
                ImageView flDepositAmountDel = holder.obtainView(R.id.iv_deposit_amount_img_del);
                flDepositAmountDel.setVisibility(View.GONE);
                flDepositAmountAdd.setVisibility(View.GONE);
                flDepositAmount.setVisibility(View.VISIBLE);
                Glide.with(context).load(s).apply(new RequestOptions().error(0).placeholder(R.drawable.loading_photo)).into(ivDepositAmount);
//                Glide.with(context).load(s).error(0).placeholder(R.drawable.loading_photo).into(ivDepositAmount);
                ivDepositAmount.setOnClickListener(v -> {
                    List<String> images = new ArrayList<>(imgPaths);
                    images.remove("add");
                    PhotoViewActivity.openImage((Activity) context, position, images);
                });
            }
        });
    }

    public interface OnClickListener {
        void addImg();

        void delImg(String img);
    }
}
