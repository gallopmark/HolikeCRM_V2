package com.holike.crm.activity.mine;


import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.homepage.FeedbackFragment;

/**
 * Created by wqj on 2018/7/16.
 * 售后体验反馈
 */

public class FeedbackActivity extends MyFragmentActivity {

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

//        HomepageBean homepageBean = (HomepageBean) getIntent().getSerializableExtra(Constants.HOME_PAGE_BEAN);
//        Map<String, Serializable> params = new HashMap<>();
//        params.put(Constants.HOME_PAGE_BEAN, homepageBean);
        startFragment(new FeedbackFragment());
//        startFragment(params, new FeedbackFragment(), false);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 10086 && data != null) {
//            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
//            if (mFragmentManager.getFragments().size() > 0) {
//                FeedbackFragment fragment = (FeedbackFragment) mFragmentManager.getFragments().getInstance(0);
//                fragment.onActivityResult(images);
//            }
//        }
//    }
}
