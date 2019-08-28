package com.holike.crm.helper;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class HouseMutilSelectHelper {
    private static final int CUSTOMIZETHESPACE = 0;
    private static final int FURNITUREDEMAND = 1;

    public HouseMutilSelectHelper() {
    }

    public static HouseMutilSelectHelper getInstance() {
        return HouseMutilSelectHelper.HouseMutilSelectHelperHolder.instance;
    }

    private static class HouseMutilSelectHelperHolder {
        private static HouseMutilSelectHelper instance = new HouseMutilSelectHelper();
    }


    private List<Boolean> mbs = new ArrayList<>(0);
    private List<Boolean> mbsSpace = new ArrayList<>(0);


    public void setList(Context ctx, RecyclerView rv, final List<TypeIdBean.TypeIdItem> datas, final CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean, final int type, final HouseMutilSelectListener lsn) {
        for (int i = 0; i < datas.size(); i++) {
            mbsSpace.add(i, false);
            mbs.add(i, false);
        }
        final int width = ((Activity) ctx).getWindowManager().getDefaultDisplay().getWidth();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ctx, width);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //根据文字长度和内边距计算出每项的宽 文字宽度+左右padding宽度+左右间隔
                int textWidth = datas.get(position).getName().length() * DensityUtil.dp2px(14) + DensityUtil.dp2px(20 + 2 + 10 + 18 + 5);
                //如果文字的宽度超过屏幕的宽度，那么就设置为屏幕宽度
                return textWidth > width ? width : textWidth;
            }
        });
        rv.setHasFixedSize(true);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(new CommonAdapter<TypeIdBean.TypeIdItem>(ctx, datas) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_blue_rect;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, TypeIdBean.TypeIdItem typeIdItem, int position) {
                final CheckBox checkBox = holder.itemView.findViewById(R.id.cb_options);
                holder.setText(R.id.item_tv_rect, typeIdItem.getName());
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    holder.setTextColor(R.id.item_tv_rect, mContext.getResources().getColor(isChecked ? R.color.textColor4 : R.color.textColor6));
                    switch (type) {
                        case CUSTOMIZETHESPACE:
                            mbsSpace.set(position, isChecked);
                            if (lsn != null) lsn.sustomSpace(addText(mbsSpace, datas));
                            break;
                        case FURNITUREDEMAND:
                            mbs.set(position, isChecked);
                            if (lsn != null) lsn.furnitureDemand(addText(mbs, datas));
                            break;
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                });

                holder.setChecked(R.id.cb_options, false);
                holder.setTextColor(R.id.item_tv_rect, mContext.getResources().getColor(R.color.textColor6));
                if (houseInfoBean == null) return;

                String ids = type == 0 ? houseInfoBean.getCustomizeTheSpace() : houseInfoBean.getFurnitureDemand();

                for (int i = 0; i < convertStrToArray(ids).length; i++) {
                    if (TextUtils.equals(typeIdItem.getId(), convertStrToArray(ids)[i])) {
                        holder.setChecked(R.id.cb_options, true);
                    }
                }
            }
        });
    }

    private static String[] convertStrToArray(String str) {
        if (TextUtils.isEmpty(str)) return new String[]{};
        return str.split(",");
    }

    private String addText(List<Boolean> mbs, List<? extends TypeIdBean.TypeIdItem> lists) {
        StringBuilder ids = null;
        for (int i = 0, size = lists.size(); i < size; i++) {
            if (mbs.get(i)) {
                if (ids == null) {
                    ids = new StringBuilder(lists.get(i).getId());
                } else {
                    ids.append(",").append(lists.get(i).getId());
                }
            }
        }
        return ids == null ? "" : ids.toString();
    }

    public interface HouseMutilSelectListener {
        void sustomSpace(String sustomSpace);

        void furnitureDemand(String furnitureDemand);
    }
}
