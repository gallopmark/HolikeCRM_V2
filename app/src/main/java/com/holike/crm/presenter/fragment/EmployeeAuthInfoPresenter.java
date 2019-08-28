package com.holike.crm.presenter.fragment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.fragment.OnFragmentDataChangedListener;
import com.holike.crm.local.MainDataSource;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.view.fragment.EmployeeAuthInfoView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAuthInfoPresenter extends BasePresenter<EmployeeAuthInfoView, EmployeeModel> {

    private List<MultiItem> items = new ArrayList<>();
    private AuthInfoAdapter adapter;
    private List<RoleDataBean.AuthInfoBean.PArrBean> resultBeans = new ArrayList<>();

    private static class AuthInfoAdapter extends CommonAdapter<MultiItem> {

        private OnItemCheckChangedListener onItemCheckChangedListener;

        void setOnItemCheckChangedListener(OnItemCheckChangedListener onItemCheckChangedListener) {
            this.onItemCheckChangedListener = onItemCheckChangedListener;
        }

        AuthInfoAdapter(Context context, List<MultiItem> items) {
            super(context, items);
        }

        public void addAll(List<RoleDataBean.AuthInfoBean> beans) {
            mDatas.clear();
            mDatas.add(new RoleDataBean.AuthInfoBean.PArrBean(mContext.getString(R.string.employee_settings_tips1), true));
            for (RoleDataBean.AuthInfoBean bean : beans) {
                mDatas.add(bean);
                if (bean.getAuthData() != null && !bean.getAuthData().isEmpty()) {
                    for (int i = 0; i < bean.getAuthData().size(); i++) {
                        RoleDataBean.AuthInfoBean.PArrBean arrBean = bean.getAuthData().get(i);
                        if (i < bean.getAuthData().size() - 1) {
                            arrBean.setShowLine(true);
                        } else {
                            arrBean.setShowLine(false);
                        }
                        mDatas.add(arrBean);
                    }
                }
            }
            notifyDataSetChanged();
        }

        void setSelectItems(List<RoleDataBean.AuthInfoBean.PArrBean> pArrBeans) {
            for (RoleDataBean.AuthInfoBean.PArrBean pArrBean : pArrBeans) {
                int index = mDatas.indexOf(pArrBean);
                if (index >= 0 && index < mDatas.size()) {
                    mDatas.set(index, pArrBean);
                }
            }
            int select = isAllOpen() ? 1 : 0;
            ((RoleDataBean.AuthInfoBean.PArrBean) mDatas.get(0)).setIsSelect(select);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == 1) {
                return R.layout.item_employee_authinfo1;
            }
            return R.layout.item_employee_authinfo2;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem item, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == 1) {
                RoleDataBean.AuthInfoBean bean = (RoleDataBean.AuthInfoBean) item;
                holder.setText(R.id.mSmallTipsTv, bean.pName);
            } else {
                RoleDataBean.AuthInfoBean.PArrBean bean = (RoleDataBean.AuthInfoBean.PArrBean) item;
                holder.setText(R.id.mTipsTv, bean.getActionName());
                SwitchCompat mSwitch = holder.obtainView(R.id.mSwitch);
                holder.setVisibility(R.id.vUnderLine, bean.isShowLine());
                mSwitch.setOnCheckedChangeListener(null);
                mSwitch.setChecked(bean.getIsSelect() == 1);
                mSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                    if (bean.isHeader()) {
                        onHeaderChecked(isChecked);
                    } else {
                        if (isChecked) {
                            bean.setIsSelect(1);
                        } else {
                            bean.setIsSelect(0);
                        }
                        mDatas.set(position, bean);
                        onItemCheckedChanged();
                    }
                    if (onItemCheckChangedListener != null) {
                        onItemCheckChangedListener.onItemCheckChanged(getSelectItems());
                    }
                });
            }
        }

        private List<RoleDataBean.AuthInfoBean.PArrBean> getSelectItems() {
            List<RoleDataBean.AuthInfoBean.PArrBean> list = new ArrayList<>();
            for (MultiItem item : mDatas) {
                if (item instanceof RoleDataBean.AuthInfoBean.PArrBean) {
                    RoleDataBean.AuthInfoBean.PArrBean arrBean = (RoleDataBean.AuthInfoBean.PArrBean) item;
                    if (!arrBean.isHeader() && arrBean.getIsSelect() == 1) {
                        list.add(arrBean);
                    }
                }
            }
            return list;
        }

        private void onHeaderChecked(boolean isChecked) {
            int select = isChecked ? 1 : 0;
            for (MultiItem item : mDatas) {
                if (item instanceof RoleDataBean.AuthInfoBean.PArrBean) {
                    ((RoleDataBean.AuthInfoBean.PArrBean) item).setIsSelect(select);
                }
            }
            notifyDataSetChanged();
        }

        private void onItemCheckedChanged() {
            if (isAllOpen()) {
                ((RoleDataBean.AuthInfoBean.PArrBean) mDatas.get(0)).setIsSelect(1);
                notifyItemChanged(0);
            } else if (isAllClosed()) {
                ((RoleDataBean.AuthInfoBean.PArrBean) mDatas.get(0)).setIsSelect(0);
                notifyItemChanged(0);
            }
        }

        private boolean isAllOpen() {
            boolean isAllOpen = true;
            for (MultiItem item : mDatas) {
                if (item instanceof RoleDataBean.AuthInfoBean.PArrBean) {
                    RoleDataBean.AuthInfoBean.PArrBean bean = (RoleDataBean.AuthInfoBean.PArrBean) item;
                    if (!bean.isHeader() && bean.getIsSelect() != 1) {
                        isAllOpen = false;
                        break;
                    }
                }
            }
            return isAllOpen;
        }

        private boolean isAllClosed() {
            boolean isAllClosed = true;
            for (MultiItem item : mDatas) {
                if (item instanceof RoleDataBean.AuthInfoBean.PArrBean) {
                    RoleDataBean.AuthInfoBean.PArrBean bean = (RoleDataBean.AuthInfoBean.PArrBean) item;
                    if (!bean.isHeader() && bean.getIsSelect() == 1) {
                        isAllClosed = false;
                        break;
                    }
                }
            }
            return isAllClosed;
        }

        public interface OnItemCheckChangedListener {
            void onItemCheckChanged(List<RoleDataBean.AuthInfoBean.PArrBean> mSelectItems);
        }
    }

    public void setAdapter(RecyclerView recyclerView, @NonNull OnFragmentDataChangedListener onFragmentDataChangedListener) {
        adapter = new AuthInfoAdapter(recyclerView.getContext(), items);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemCheckChangedListener(mSelectItems -> onFragmentDataChangedListener.onFragmentDataChanged(resultBeans.size() != mSelectItems.size() || !resultBeans.containsAll(mSelectItems)));
    }

    public void addAll(List<RoleDataBean.AuthInfoBean> beans) {
        if (adapter == null) return;
        adapter.addAll(beans);
    }

    public void setSelectItems(List<RoleDataBean.AuthInfoBean> beans) {
        if (adapter == null) return;
        for (RoleDataBean.AuthInfoBean bean : beans) {
            List<RoleDataBean.AuthInfoBean.PArrBean> arrBeans = bean.getAuthData();
            if (arrBeans != null && !arrBeans.isEmpty()) {
                for (RoleDataBean.AuthInfoBean.PArrBean arrBean : arrBeans) {
                    if (arrBean.getIsSelect() == 1) {
                        resultBeans.add(arrBean);
                    }
                }
            }
        }
        adapter.setSelectItems(resultBeans);
    }

    public List<RoleDataBean.AuthInfoBean.PArrBean> getSelectItems() {
        if (adapter == null) return new ArrayList<>();
        return adapter.getSelectItems();
    }

    public void getAuthInfo(Context context) {
        List<RoleDataBean.AuthInfoBean> infoBeans = MainDataSource.getAuthInfo(context);
        if (infoBeans != null) {
            if (getView() != null) {
                getView().onGetAuthInfo(infoBeans);
            }
            return;
        }
        model.getAuthInfo(new EmployeeModel.OnGetAuthInfoCallback() {
            @Override
            public void onLoading() {
                if (getView() != null) getView().onShowLoading();
            }

            @Override
            public void onGetAuthInfo(List<RoleDataBean.AuthInfoBean> infoBeans) {
                MainDataSource.saveAuthInfo(context, infoBeans);
                if (getView() != null) getView().onGetAuthInfo(infoBeans);
            }

            @Override
            public void onGetAuthInfoFailure(String message) {
                if (getView() != null) getView().onGetAuthInfoFail(message);
            }

            @Override
            public void onLoadingEnd() {
                if (getView() != null) getView().onHideLoading();
            }
        });
    }
}
