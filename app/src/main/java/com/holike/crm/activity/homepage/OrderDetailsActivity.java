package com.holike.crm.activity.homepage;

import android.content.Intent;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.homepage.OrderDetailsFragment;
import com.holike.crm.util.Constants;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends MyFragmentActivity {

    public static void open(BaseFragment<?, ?> fragment, String orderId) {
        if (fragment.getActivity() == null) return;
        open(fragment, orderId, "");
    }

    public static void open(BaseFragment<?, ?> fragment, String orderId, String messageId) {
        if (fragment.getActivity() == null) return;
        Intent intent = new Intent(fragment.getActivity(), OrderDetailsActivity.class);
        intent.putExtra(Constants.ORDER_ID, orderId);
        intent.putExtra(Constants.MESSAGE_ID, messageId);
        fragment.openActivityForResult(intent);
    }

    public static void open(BaseActivity<?, ?> activity, String orderId) {
        open(activity, orderId, "");
    }

    public static void open(BaseActivity<?, ?>activity, String orderId, String messageId) {
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra(Constants.ORDER_ID, orderId);
        intent.putExtra(Constants.MESSAGE_ID, messageId);
        activity.openActivity(intent);
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_fragment_depend;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        Map<String, Serializable> map = new HashMap<>(0);
//        map.put(Constants.ORDER_ID, getIntent().getStringExtra(Constants.ORDER_ID));
//        map.put(Constants.MESSAGE_ID, getIntent().getStringExtra(Constants.MESSAGE_ID));
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ORDER_ID, getIntent().getStringExtra(Constants.ORDER_ID));
        bundle.putString(Constants.MESSAGE_ID, getIntent().getStringExtra(Constants.MESSAGE_ID));
        startFragment(new OrderDetailsFragment(), bundle, false);
//        startFragment(map, new OrderDetailsFragment(), false);
    }

}

