package com.holike.crm.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerImagePreviewHelper;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gallop on 2019/8/2.
 * Copyright holike possess 2019.
 * 客户管理点击展开图片 图片查看
 */
public class CustomerImagePreviewActivity extends MyFragmentActivity {
    static String IMAGE_DATA = "image_data";
    @BindView(R.id.rv_pictures)
    RecyclerView mPhotoRecyclerView;

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_imagepreview;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        List<String> images = (List<String>) IntentValue.getInstance().get(IMAGE_DATA);
        new CustomerImagePreviewHelper(this).setImageData(mPhotoRecyclerView, images);
    }

    public static void open(Context context, String title, List<String> images) {
        Intent intent = new Intent(context, CustomerImagePreviewActivity.class);
        intent.putExtra("title", title);
        IntentValue.getInstance().put(IMAGE_DATA, images);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        IntentValue.getInstance().remove(IMAGE_DATA);
        super.onDestroy();
    }
}
