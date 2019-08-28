package com.holike.crm.popupwindown;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.HomepageBean;

import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 */
public class RoleListPopupWindow extends CommonPopupWindow {

    private List<HomepageBean.NewDataBean.RoleBean> mRoleList;
    private OnRoleSelectedListener mListener;

    public RoleListPopupWindow(Context context, List<HomepageBean.NewDataBean.RoleBean> roleList) {
        super(context);
        this.mRoleList = roleList;
        initView();
    }

    public void setOnRoleSelectedListener(OnRoleSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    public int bindLayoutId() {
        return R.layout.popupwindow_role_list;
    }

    private void initView() {
        mContentView.setOnClickListener(view -> dismiss());
        RecyclerView recyclerView = mContentView.findViewById(R.id.rv_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new CommonAdapter<HomepageBean.NewDataBean.RoleBean>(mContext, mRoleList) {
            @Override
            public void onBindHolder(RecyclerHolder holder, HomepageBean.NewDataBean.RoleBean bean, int position) {
                holder.setText(R.id.tv_name, bean.roleName);
                holder.itemView.setOnClickListener(view -> {
                    if (mListener != null) {
                        mListener.onRoleSelected(position, bean);
                    }
                    dismiss();
                });
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.popupwindow_item_role;
            }
        });
    }

    public interface OnRoleSelectedListener {
        void onRoleSelected(int position, HomepageBean.NewDataBean.RoleBean bean);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setBackgroundAlpha(0.3f);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public int getColorDrawable() {
        return R.color.bg_transparent;
    }

    @Override
    public void dismiss() {
        setBackgroundAlpha(1f);
        super.dismiss();
    }
}
