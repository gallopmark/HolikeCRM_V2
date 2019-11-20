package com.holike.crm.fragment.report.target;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.CashierInputFilter;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.bean.BusinessTargetBean;
import com.holike.crm.bean.DictionaryBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.LogCat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 */
class SetEmployeeTargetHelper extends FragmentHelper {

    static final class SystemCode {
        static List<DictionaryBean> getDictionaries(Context context) {
            List<DictionaryBean> list = new ArrayList<>();
            list.add(new DictionaryBean("1", context.getString(R.string.number_of_new_customers)));
            list.add(new DictionaryBean("2", context.getString(R.string.number_of_reservation_scale)));
            list.add(new DictionaryBean("3", context.getString(R.string.transaction_million2)));
            list.add(new DictionaryBean("4", context.getString(R.string.payback_million)));
            list.add(new DictionaryBean("5", context.getString(R.string.number_of_measuring)));
            list.add(new DictionaryBean("6", context.getString(R.string.number_of_output_graphs)));
            list.add(new DictionaryBean("7", context.getString(R.string.order_million)));
            return list;
        }

        static String getValue(Context context, String key) {
            if (TextUtils.isEmpty(key)) return "";
            List<DictionaryBean> list = getDictionaries(context);
            for (DictionaryBean bean : list) {
                if (TextUtils.equals(bean.id, key)) {
                    return bean.name;
                }
            }
            return "";
        }
    }

    private final class EmployeeBean implements MultiItem {
        String shopId;
        String shopName;
        String employeeId;
        String employeeName;
        List<BusinessTargetBean.UserBean.ArrBean> userList;

        EmployeeBean(String shopId, String shopName, String employeeId, String employeeName) {
            this.shopId = shopId;
            this.shopName = shopName;
            this.employeeId = employeeId;
            this.employeeName = employeeName;
        }

        @Override
        public int getItemType() {
            return 0;
        }
    }

    private final class InnerBean implements MultiItem {
        String id;
        String param;
        String paramName;
        String targetValue;

        InnerBean(String id, String param, String paramName, String targetValue) {
            this.id = id;
            this.param = param;
            this.paramName = paramName;
            this.targetValue = targetValue;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof InnerBean) {
                return TextUtils.equals(id, ((InnerBean) obj).id);
            } else {
                return false;
            }
        }

        @Override
        public int getItemType() {
            return 1;
        }
    }

    private final class InnerAdapter extends CommonAdapter<MultiItem> {

        InnerAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == 0) return R.layout.item_set_employee_target_header;
            return R.layout.item_set_employee_target;
        }

        void addTarget() {
            mDatas.add(new InnerBean(null, null, null, null));
            mAdapter.notifyItemInserted(mDatas.size());
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem item, int position) {
            if (holder.getItemViewType() == 0) {
                EmployeeBean bean = (EmployeeBean) item;
                holder.setText(R.id.tv_select_shop, bean.shopName);
                holder.setText(R.id.tv_select_employee, bean.employeeName);
                holder.setOnClickListener(R.id.tv_select_shop, view -> onSelectShop(bean, position));
                holder.setOnClickListener(R.id.tv_select_employee, view -> onSelectEmployee(holder, bean));
            } else {
                InnerBean bean = (InnerBean) item;
                holder.setText(R.id.tv_select_param, bean.paramName);
                holder.setOnClickListener(R.id.tv_select_param, view -> onSelectParam(holder, bean));
                EditText etTarget = holder.obtainView(R.id.et_target_value);
                etTarget.setFilters(new InputFilter[]{new CashierInputFilter()});
                if (etTarget.getTag() instanceof TextWatcher) {
                    etTarget.removeTextChangedListener((TextWatcher) etTarget.getTag());
                }
                TextWatcher textWatcher = new SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                        bean.targetValue = cs.toString();
                    }
                };
                etTarget.addTextChangedListener(textWatcher);
                etTarget.setTag(textWatcher);
                etTarget.setText(bean.targetValue);
                holder.itemView.setOnLongClickListener(view -> {
                    showDeleteTips(position);
                    return true;
                });
            }
        }

        /*选择门店*/
        private void onSelectShop(EmployeeBean bean, final int position) {
            List<DictionaryBean> optionItems = new ArrayList<>();
            for (BusinessTargetBean.UserBean userBean : mTargetBean.getUserList()) {
                optionItems.add(new DictionaryBean(userBean.shopId, userBean.shopName));
            }
            PickerHelper.showOptionsPicker(mContext, optionItems, mSelectShopPosition, (p, b) -> {
                if (p != mSelectShopPosition) {
                    mSelectShopPosition = p;
                    bean.shopId = b.id;
                    bean.shopName = b.name;
                    bean.employeeId = null;
                    bean.employeeName = null;
                    mSelectEmployeePosition = -1;
                    bean.userList = new ArrayList<>(mTargetBean.getUserList().get(p).getArr());
                    notifyItemChanged(position);
                }
            });
        }

        /*选择员工，前提是要选择门店*/
        private void onSelectEmployee(RecyclerHolder holder, EmployeeBean bean) {
            if (bean.userList == null || bean.userList.isEmpty()) {
                String text = mContext.getString(R.string.tips_pleas_choose_first) + mContext.getString(R.string.store);
                mFragment.showShortToast(text);
            } else {
                List<DictionaryBean> optionItems = new ArrayList<>();
                for (BusinessTargetBean.UserBean.ArrBean arrBean : bean.userList) {
                    optionItems.add(new DictionaryBean(arrBean.userId, arrBean.userName));
                }
                PickerHelper.showOptionsPicker(mContext, optionItems, mSelectEmployeePosition, (p, b) -> {
                    if (p != mSelectEmployeePosition) {
                        mSelectEmployeePosition = p;
                        bean.employeeId = b.id;
                        bean.employeeName = b.name;
                        holder.setText(R.id.tv_select_employee, b.name);
                        invalidate(b.id);
                    }
                });
            }
        }

        private void invalidate(String employeeId) {
            EmployeeBean bean = (EmployeeBean) mDatas.remove(0);
            mDatas.clear();
            mDatas.add(0, bean);
            List<InnerBean> list = new ArrayList<>();
            for (BusinessTargetBean.UserTargetBean targetBean : mTargetBean.getUserTarget()) {
                if (TextUtils.equals(targetBean.crmOrderNo, employeeId)) {
                    list.add(new InnerBean(targetBean.id, targetBean.phone, SystemCode.getValue(mContext, targetBean.phone), targetBean.getMoney()));
                }
            }
            if (list.isEmpty()) {
                mDatas.add(new InnerBean(null, null, null, null));
            } else {
                mDatas.addAll(list);
            }
            notifyDataSetChanged();
        }

        /*选择目标参数*/
        private void onSelectParam(RecyclerHolder holder, InnerBean bean) {
            //1 新建客户数、2预约量尺数、3业绩（万）、4回款（万）、5量尺数、6出图数、7下单（万）
            List<DictionaryBean> optionItems = new ArrayList<>(SystemCode.getDictionaries(mContext));
            PickerHelper.showOptionsPicker(mContext, optionItems, ((position, b) -> {
                if (isSameParam(b.id)) {  //选择重复目标参数
                    mFragment.showShortToast(mContext.getString(R.string.donot_set_the_same_target_parameter));
                } else {
                    bean.param = b.id;
                    bean.paramName = b.name;
                    holder.setText(R.id.tv_select_param, b.name);
                    if (b.name.contains(mContext.getString(R.string.ten_thousand))) {
                        holder.setVisibility(R.id.tv_unit, View.VISIBLE);
                    } else {
                        holder.setVisibility(R.id.tv_unit, View.GONE);
                    }
                }
            }));
        }

        private boolean isSameParam(String param) {
            for (MultiItem item : mDatas) {
                if (item.getItemType() == 1 && TextUtils.equals(((InnerBean) item).param, param)) {
                    return true;
                }
            }
            return false;
        }

        private void showDeleteTips(final int position) {
            new MaterialDialog.Builder(mContext)
                    .message(R.string.dialog_message_delete_target_performance2)
                    .negativeButton(R.string.yes, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        remove(position);
                    }).positiveButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        }

        private void remove(int position) {
            InnerBean bean = (InnerBean) mDatas.remove(position);
            if (!TextUtils.isEmpty(bean.id)) {
                if (mRemoveItems == null) {
                    mRemoveItems = new ArrayList<>();
                }
                if (!mRemoveItems.contains(bean)) {
                    mRemoveItems.add(bean);
                }
            }
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mDatas.size() - position);
//            notifyItemRangeChanged(position, mDatas.size());
            checkAndInvalidate();
        }
    }

    private Callback mCallback;
    private RecyclerView mRecyclerView;
    private List<MultiItem> mDataList;
    private InnerAdapter mAdapter;
    private List<InnerBean> mRemoveItems;
    private TextView mAddNewItemTextView;

    private BusinessTargetBean mTargetBean;
    private int mSelectShopPosition = -1; //记录上次选择门店的位置

    private int mSelectEmployeePosition = -1; //记录上次选择员工的位置

    SetEmployeeTargetHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        mCallback = callback;
        mTargetBean = (BusinessTargetBean) IntentValue.getInstance().removeBy("businessTargetBean");
        init();
    }

    private void init() {
        mRecyclerView = obtainView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDataList = new ArrayList<>();
        mDataList.add(new EmployeeBean(null, null, null, null));
        mDataList.add(new InnerBean(null, null, null, null));
        mAdapter = new InnerAdapter(mContext, mDataList);
        mRecyclerView.setAdapter(mAdapter);

        //保存按钮
        TextView saveTextView = obtainView(R.id.tv_save);
        mAddNewItemTextView = obtainView(R.id.tv_increase_target);
        saveTextView.setOnClickListener(view -> onSave());
        mAddNewItemTextView.setOnClickListener(view -> checkAndAddItem());
    }

    private void checkAndAddItem() {
        if (getItemCount() < SystemCode.getDictionaries(mContext).size()) {
            mAdapter.addTarget();
            mRecyclerView.smoothScrollToPosition(mDataList.size());
            checkAndInvalidate();
        }
    }

    private int getItemCount() {
        int itemCount = 0;
        for (MultiItem item : mDataList) {
            if (item.getItemType() == 1) {
                itemCount += 1;
            }
        }
        return itemCount;
    }

    private void checkAndInvalidate() {
        if (getItemCount() >= SystemCode.getDictionaries(mContext).size()) {
            mAddNewItemTextView.setEnabled(false);
        } else {
            if (!mAddNewItemTextView.isEnabled()) {
                mAddNewItemTextView.setEnabled(true);
            }
        }
    }

    private void onSave() {
        EmployeeBean bean = (EmployeeBean) mDataList.get(0);
        if (TextUtils.isEmpty(bean.shopId)) {
            mFragment.showShortToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.store));
        } else {
            if (TextUtils.isEmpty(bean.employeeId)) {
                mFragment.showShortToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.employee));
            } else {
                if (!isFillAll()) {
                    mFragment.showShortToast(mContext.getString(R.string.tips_complete_information));
                } else {
                    List<Param> list = new ArrayList<>();
                    for (MultiItem item : mDataList) {
                        if (item.getItemType() == 1) {
                            InnerBean innerBean = (InnerBean) item;
                            Param param = new Param();
                            param.param1 = bean.shopId;
                            param.crmOrderNo = bean.employeeId;
                            param.id = innerBean.id;
                            param.param2 = "52";
                            param.phone = innerBean.param;
                            param.money = ParamHelper.urlEncode(innerBean.targetValue);
                            list.add(param);
                        }
                    }
                    String session = "";
                    if (mRemoveItems != null && !mRemoveItems.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mRemoveItems.size(); i++) {
                            sb.append(mRemoveItems.get(i).id);
                            if (i < mRemoveItems.size() - 1)
                                sb.append(",");
                        }
                        session = sb.toString();
                        LogCat.e("removeItems", sb.toString());
                    }
                    mCallback.onSave(MyJsonParser.fromBeanToJson(list), session);
                }
            }
        }
    }

    /*是否填写了所有信息*/
    private boolean isFillAll() {
        for (MultiItem item : mDataList) {
            if (item instanceof InnerBean && (TextUtils.isEmpty(((InnerBean) item).param)
                    || TextUtils.isEmpty(((InnerBean) item).targetValue))) {
                return false;
            }
        }
        return true;
    }

    void onSaveSuccess(String message) {
        mFragment.showShortToast(message);
    }


    /**
     * crmOrderNo 经销商id或者门店id或者员工id
     * dealerId 开始10位时间戳
     * dealerNo 结束10位时间戳
     * param2 50总业绩 51门店业绩 52个人业绩
     * money 业绩 urlencode一下再传
     * param1 门店id（52时需要传）
     * phone 目标参数
     */
    private static final class Param {
        String id;
        String crmOrderNo;
        String param1;
        String param2;
        String phone;
        String money;
    }

    interface Callback {
        void onSave(String param, String session);
    }
}
