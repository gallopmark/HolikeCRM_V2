package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.WriteCityBean;
import com.holike.crm.util.Constants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/6/27.
 * 选择服务城市
 */

public class SelectCityFragment extends MyFragment {
    @BindView(R.id.statusView)
    View statusView;
    @BindView(R.id.rv_select_city)
    RecyclerView recyclerView;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_select_city;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_table_serve_city));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        Bundle bundle = getArguments();
        if (bundle != null) {
            WriteCityBean writeCityBean = (WriteCityBean) bundle.getSerializable(Constants.WRITE_CITY_BEAN);
            if (writeCityBean == null || writeCityBean.getSelectData() == null || writeCityBean.getSelectData().isEmpty()) {
                return;
            }
            recyclerView.setAdapter(new CommonAdapter<WriteCityBean.SelectDataBean>(mContext, writeCityBean.getSelectData()) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_select_city;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, WriteCityBean.SelectDataBean selectDataBean, int position) {
                    TextView textView = holder.obtainView(R.id.tv_item_rv_select_area);
                    textView.setText(selectDataBean.getDealerName());
                    holder.itemView.setOnClickListener(v -> {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.DEALER_ID, selectDataBean);
                        finishFragment(0, Constants.RESULT_CODE_DEALER_ID, params);
                    });
                }
            });
        }
    }
}
