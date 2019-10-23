package com.holike.crm.fragment.employee;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.gallopmark.recycler.widgetwrapper.WrapperRecyclerView;
import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.helper.FlexboxManagerHelper;

import java.util.ArrayList;

import java.util.List;


/**
 * Created by gallop on 2019/8/7.
 * Copyright holike possess 2019.
 * 员工设置权限帮助类
 */
class EmployeePermissionHelper {
    interface OnRoleSelectedListener {
        void onRoleSelectChanged();
    }

    /*选择角色适配器*/
    class SelectRoleAdapter extends CommonAdapter<RoleDataBean> {

        void setOnRoleSelectedListener(OnRoleSelectedListener listener) {
            this.mOnRoleSelectedListener = listener;
        }

        OnRoleSelectedListener mOnRoleSelectedListener;

        SelectRoleAdapter(Context context, List<RoleDataBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_multiplechoicev2;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, RoleDataBean bean, int position) {
            ImageView ivSelect = holder.obtainView(R.id.iv_select);
            TextView tvContent = holder.obtainView(R.id.tv_content);
            setChecked(ivSelect, tvContent, mSelectedRoleList.contains(bean));
            holder.itemView.setOnClickListener(view -> {
                if (mSelectedRoleList.contains(bean)) {
                    mSelectedRoleList.remove(bean);
                    setChecked(ivSelect, tvContent, false);
                } else {
                    mSelectedRoleList.add(bean);
                    setChecked(ivSelect, tvContent, true);
                }
                if (mOnRoleSelectedListener != null) {
                    mOnRoleSelectedListener.onRoleSelectChanged();
                }
            });
            tvContent.setText(bean.roleName);
        }

        private void setChecked(ImageView iv, TextView tv, boolean isChecked) {
            if (isChecked) {
                iv.setImageResource(R.drawable.cus_scale_space_sel);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
            } else {
                iv.setImageResource(R.drawable.cus_scale_space_nor);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
            }
        }
    }

    private Context mContext;
    private SelectRoleAdapter mRoleAdapter;
    private List<RoleDataBean.AuthInfoBean> mAuthItems;
    private AuthItemAdapter mAuthAdapter;
    /*选择角色后默认勾选的权限集合*/
    private List<RoleDataBean> mOldRoleList; //从详情进来 被选中的角色集合
    private List<RoleDataBean.AuthInfoBean.PArrBean> mOldAuthList;  //从详情进来被选中的权限结婚

    private List<RoleDataBean> mSelectedRoleList; //已选择的角色集合
    private List<RoleDataBean.AuthInfoBean.PArrBean> mSelectedAuthList; //已选择的权限集合

    EmployeePermissionHelper(Context context, List<RoleDataBean> roleList, List<RoleDataBean.AuthInfoBean> authList) {
        this.mContext = context;
        List<RoleDataBean> roleDataList;
        List<RoleDataBean> selectRoles = new ArrayList<>();
        if (roleList != null) { //从员工详情进来
            roleDataList = new ArrayList<>(roleList);
            List<RoleDataBean> defaultList = IntentValue.getInstance().getRoleData();
            for (RoleDataBean bean : roleDataList) {
                int index = defaultList.indexOf(bean);
                if (index >= 0) {   //设置角色的默认权限
                    bean.setActionList(defaultList.get(index).getActionList());
                }
                if (bean.isSelected()) {
                    selectRoles.add(bean);
                }
            }
        } else { //新增员工，从缓存冲获取角色数据
            roleDataList = new ArrayList<>(IntentValue.getInstance().getRoleData());
            for (RoleDataBean bean : roleDataList) {    //设置全部未选中
                if (bean.isSelected()) {
                    bean.setIsSelect("0");
                }
            }
        }
        mOldRoleList = new ArrayList<>(selectRoles);
        mSelectedRoleList = new ArrayList<>(selectRoles);
//        List<RoleDataBean> roleDataBeanList = new ArrayList<>(IntentValue.getInstance().getRoleData());
        mRoleAdapter = new SelectRoleAdapter(mContext, roleDataList);
        List<RoleDataBean.AuthInfoBean> authDataList;
        List<RoleDataBean.AuthInfoBean.PArrBean> selectArr = new ArrayList<>();
        if (authList != null) {
            authDataList = new ArrayList<>(authList);
            for (RoleDataBean.AuthInfoBean bean : authDataList) {
                for (RoleDataBean.AuthInfoBean.PArrBean arrBean : bean.getAuthData()) {
                    if (arrBean.isSelect()) {
                        selectArr.add(arrBean);
                    }
                }
            }
        } else {
            authDataList = new ArrayList<>(IntentValue.getInstance().getEmployeeAuthInfo());
        }
        mOldAuthList = new ArrayList<>(selectArr);
        mSelectedAuthList = new ArrayList<>(selectArr);
        mAuthItems = new ArrayList<>(authDataList);
        mAuthAdapter = new AuthItemAdapter(mContext, mAuthItems);
    }

    void attach(WrapperRecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addHeaderView(obtain(recyclerView));
        recyclerView.setAdapter(mAuthAdapter);
    }

    private View obtain(ViewGroup parent) {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_employee_permission, parent, false);
        RecyclerView rvRole = headerView.findViewById(R.id.rv_role);
        rvRole.setLayoutManager(FlexboxManagerHelper.getDefault(mContext));
        rvRole.setAdapter(mRoleAdapter);
        mRoleAdapter.setOnRoleSelectedListener(() -> {
            List<RoleDataBean.AuthInfoBean.PArrBean> list = new ArrayList<>();
            /*遍历选中的角色集合*/
            List<RoleDataBean.AuthInfoBean.PArrBean> allAuthList = getAllAuthList();
            List<RoleDataBean.Action> allSelectedList = getAllSelectedRoleActionList();
            for (RoleDataBean.AuthInfoBean.PArrBean bean : allAuthList) {
                for (RoleDataBean.Action action : allSelectedList) {
                    if (TextUtils.equals(action.actionId, bean.actionId)) {
                        list.add(bean);
                    }
                }
            }
            mSelectedAuthList.clear();
            mSelectedAuthList.addAll(list);
            mAuthAdapter.notifyDataSetChanged();
        });
        return headerView;
    }

    /*将已被选择的角色中的默认权限全部加到一个集合中*/
    private List<RoleDataBean.Action> getAllSelectedRoleActionList() {
        List<RoleDataBean.Action> actionList = new ArrayList<>();
        for (RoleDataBean bean : mSelectedRoleList) {
            actionList.addAll(bean.getActionList());
        }
        return actionList;
    }

    /*所有的权限信息集合*/
    private List<RoleDataBean.AuthInfoBean.PArrBean> getAllAuthList() {
        List<RoleDataBean.AuthInfoBean.PArrBean> list = new ArrayList<>();
        for (RoleDataBean.AuthInfoBean bean : mAuthItems) {
            list.addAll(bean.getAuthData());
        }
        return list;
    }

    class AuthItemAdapter extends CommonAdapter<RoleDataBean.AuthInfoBean> {

        AuthItemAdapter(Context context, List<RoleDataBean.AuthInfoBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_authinfov2;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, RoleDataBean.AuthInfoBean bean, int position) {
            holder.setText(R.id.tv_title, bean.pName);
            RecyclerView recyclerView = holder.obtainView(R.id.rv_auth_info);
            recyclerView.setLayoutManager(FlexboxManagerHelper.getDefault(mContext));
            recyclerView.setAdapter(new InnerFlowAdapter(mContext, bean.getAuthData()));
            if (getItemCount() == 1) {
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_white_5dp);
            } else {
                if (position == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.bg_corners_top_white_5dp);
                } else if (position == getItemCount() - 1) {
                    holder.itemView.setBackgroundResource(R.drawable.bg_corners_bottom_white_5dp);
                } else {
                    holder.itemView.setBackgroundResource(R.color.color_while);
                }
            }
            if (position == getItemCount() - 1) {
                holder.setVisibility(R.id.v_divider, View.GONE);
            } else {
                holder.setVisibility(R.id.v_divider, View.VISIBLE);
            }
        }

        class InnerFlowAdapter extends CommonAdapter<RoleDataBean.AuthInfoBean.PArrBean> {

            InnerFlowAdapter(Context context, List<RoleDataBean.AuthInfoBean.PArrBean> mDatas) {
                super(context, mDatas);
            }

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_multiplechoicev2;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, final RoleDataBean.AuthInfoBean.PArrBean bean, int position) {
                ImageView ivSelect = holder.obtainView(R.id.iv_select);
                TextView tvContent = holder.obtainView(R.id.tv_content);
                setChecked(ivSelect, tvContent, mSelectedAuthList.contains(bean));
                holder.itemView.setOnClickListener(view -> {
                    if (mSelectedAuthList.contains(bean)) {
                        mSelectedAuthList.remove(bean);
                        setChecked(ivSelect, tvContent, false);
                    } else {
                        mSelectedAuthList.add(bean);
                        setChecked(ivSelect, tvContent, true);
                    }
                });
                tvContent.setText(bean.actionName);
            }

            private void setChecked(ImageView iv, TextView tv, boolean isChecked) {
                if (isChecked) {
                    iv.setImageResource(R.drawable.cus_scale_space_sel);
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                } else {
                    iv.setImageResource(R.drawable.cus_scale_space_nor);
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor6));
                }
            }
        }
    }

    /*角色选择是否发生了改变*/
    private boolean isRoleSelectChanged() {
        if (mSelectedRoleList.size() != mOldRoleList.size()) {
            return true;
        }
        return !mSelectedRoleList.containsAll(mOldRoleList);
    }

    private boolean isAuthSelectChanged() {
        if (mSelectedAuthList.size() != mOldAuthList.size()) {
            return true;
        }
        return !mSelectedAuthList.containsAll(mOldAuthList);
    }

    /*角色或权限的选择是否发生了改变*/
    boolean isDataChanged() {
        return isRoleSelectChanged() || isAuthSelectChanged();
    }

    /*是否选择了角色*/
    boolean isSelectRoles() {
        return !mSelectedRoleList.isEmpty();
    }

    /*获取已选的角色id，多个用","隔开*/
    String getStationIds() {
        List<String> roleCodeList = new ArrayList<>();
        for (RoleDataBean bean : mSelectedRoleList) {
            if (!roleCodeList.contains(bean.roleCode)) {
                roleCodeList.add(bean.roleCode);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < roleCodeList.size(); i++) {
            sb.append(roleCodeList.get(i));
            if (i < roleCodeList.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /*获取已选的权限id*/
    String getAuthIds() {
        /*去重处理*/
        List<String> actionIdList = new ArrayList<>();
        for (RoleDataBean.AuthInfoBean.PArrBean bean : mSelectedAuthList) {
            if (!actionIdList.contains(bean.actionId)) {
                actionIdList.add(bean.actionId);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < actionIdList.size(); i++) {
            sb.append(actionIdList.get(i));
            if (i < actionIdList.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
