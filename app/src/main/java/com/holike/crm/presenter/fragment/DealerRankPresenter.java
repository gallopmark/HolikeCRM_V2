package com.holike.crm.presenter.fragment;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.DealerRankBean;
import com.holike.crm.model.fragment.DealerRankModel;
import com.holike.crm.view.fragment.DealerRankView;

/**
 * Created by wqj on 2018/5/28.
 * 经销商排行
 */

public class DealerRankPresenter extends BasePresenter<DealerRankView, DealerRankModel> {

    /**
     * 获取数据
     */
    public void getData(String cityCode) {
        model.getData(cityCode == null ? "" : cityCode, new DealerRankModel.GetDataListener() {
            @Override
            public void success(DealerRankBean bean) {
                if (TextUtils.equals(bean.getIsDealer(), "1")) {
                    if (getView() != null)
                        getView().enterPersonal(bean);
                } else {
                    if (getView() != null)
                        getView().enterRank(bean);
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }

    /**
     * 获取一个空PagerAdapter
     */
    @NonNull
    public static PagerAdapter getPagerAdapter(final int size) {
        return new PagerAdapter() {
            @Override
            public int getCount() {
                return size;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = new View(container.getContext());
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        };
    }
}
