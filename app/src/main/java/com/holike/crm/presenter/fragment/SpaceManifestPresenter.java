package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.SpaceManifestBean;
import com.holike.crm.bean.SpaceManifestSubtitleInfoBean;
import com.holike.crm.customView.listener.TouchPressListener;
import com.holike.crm.model.fragment.SpaceManifestModel;
import com.holike.crm.view.fragment.SpaceManifestView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaceManifestPresenter extends BasePresenter<SpaceManifestView, SpaceManifestModel> {

    private int selectSidePosition = 0;
    private int selectSubtitlePosition = 0;

    public void getData(String orderId) {
        model.getData(orderId, new SpaceManifestModel.SpaceManifestListener() {
            @Override
            public void success(List<SpaceManifestBean> beans) {
                if (getView() != null)
                    getView().onSuccess(beans);
            }

            @Override
            public void fail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }

    public void setSideAdapter(final Context ctx, final RecyclerView rv, final List<SpaceManifestBean.DataListBean> beans) {
        rv.setLayoutManager(new LinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<SpaceManifestBean.DataListBean>(ctx, beans) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_space_manifest;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, SpaceManifestBean.DataListBean b, int position) {
                holder.itemView.setSelected(selectSidePosition == position);
                TextView textView = holder.obtainView(R.id.tv_item_name);
                textView.setText(b.getProductName());
                holder.setVisibility(R.id.v_select, holder.itemView.isSelected());
                textView.setTextColor(holder.itemView.isSelected() ? ContextCompat.getColor(ctx, R.color.textColor5) : ContextCompat.getColor(ctx, R.color.textColor6));
                holder.itemView.setOnTouchListener(new TouchPressListener(ctx, R.color.textColor5, R.color.textColor6, R.color.textColor8, new View[]{textView}, v -> {
                    if (selectSidePosition != position) {
                        selectSidePosition = position;
                        //选择side时 更新subtitle
                        selectSubtitlePosition = 0;
                        rv.scrollToPosition(position);
                        mSubtitleRecyclerView.getAdapter().notifyDataSetChanged();
                        mSubtitleRecyclerView.scrollToPosition(0);//滚动到第一个选项
                        if (getView() != null)
                            getView().onSubTitleInfo(position, beans.get(selectSidePosition));
                    }
                    notifyDataSetChanged();
                }));
            }
        });
    }

    private RecyclerView mSubtitleRecyclerView;

    public void setSubtitleAdapter(final Context ctx, final RecyclerView rv, final List<SpaceManifestBean.DataListBean> beans) {
        this.mSubtitleRecyclerView = rv;
        rv.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new CommonAdapter<String>(ctx, Arrays.asList(initData())) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_space_manifest_subtitle;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, String name, int position) {
                holder.itemView.setSelected(selectSubtitlePosition == position);
                TextView tv = holder.obtainView(R.id.tv_item_subtitle_name);
                tv.setText(name);
                holder.setVisibility(R.id.v_select, holder.itemView.isSelected());
                tv.setTextColor(holder.itemView.isSelected() ? ctx.getResources().getColor(R.color.textColor5) : ctx.getResources().getColor(R.color.textColor6));
//                setFormAdapter(ctx, formRv,subtitleBeans);
                holder.itemView.setOnTouchListener(new TouchPressListener(ctx, R.color.textColor5, R.color.textColor6, R.color.textColor8, new View[]{tv}, new TouchPressListener.OnPressListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectSubtitlePosition != position) {
                            selectSubtitlePosition = position;
                            rv.scrollToPosition(position);
                        }
                        if (getView() != null)
                            getView().onSelectInfo(position, beans.get(selectSidePosition));
                        notifyDataSetChanged();
                    }
                }));
            }
        });
    }

    private String[] initData() {
        return new String[]{"产品详情", "出入库", "描述", "备注"};
    }

    private List<SpaceManifestSubtitleInfoBean> initSubtitleData(SpaceManifestBean.DataListBean beans, int position) {
        String[] ss = new String[]{"标识", "数量", "免费", "补板",
                "加急", "加急费", "退单", "齐套发货",
                "发货时间", "预计到达", "花色", "材质",
                "风格", "木纹方向", "总面积", "促销",
                "备注"};
        List<SpaceManifestSubtitleInfoBean> subtitleBean = new ArrayList<>(0);
        switch (position) {
            case 0:
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[0], beans.getProductType()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[1], String.valueOf(beans.getProduct_Qty())));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[2], beans.getOr_free()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[3], beans.getPatching()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[4], beans.getOrUrgent()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[5], String.valueOf(beans.getUrgentPrice())));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[6], beans.getIsBack()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[7], beans.getUnifiedDelivery()));
                break;
            case 1:
//                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[8], "-"));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[9], beans.getPlanDeliveryDate()));
                break;
            case 2:
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[10], beans.getColor()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[11], beans.getTexture()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[12], beans.getStyle()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[13], beans.getGrainDirection()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[14], String.valueOf(beans.getTotalArea())));
                break;
            case 3:
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[15], beans.getJoinActivityName()));
                subtitleBean.add(new SpaceManifestSubtitleInfoBean(ss[16], beans.getRemark()));

                break;
        }
        return subtitleBean;
    }

    public void setFormAdapter(final Context ctx, RecyclerView rv, final SpaceManifestBean.DataListBean beans, final int selectPosition) {
        List<SpaceManifestSubtitleInfoBean> names = initSubtitleData(beans, selectPosition);
        rv.setLayoutManager(new LinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<SpaceManifestSubtitleInfoBean>(ctx, names) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_space_manifest_form;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, SpaceManifestSubtitleInfoBean b, int position) {
                holder.setText(R.id.tv_item_from_name, b.getName());
                holder.setText(R.id.tv_item_from_content, TextUtils.isEmpty(b.getContent()) ? "-" : b.getContent());
            }
        });
    }


}
