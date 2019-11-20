package com.holike.crm.fragment.report.target;


import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.helper.PickerHelper;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 */
class SetBusinessTargetHelper extends FragmentHelper {

    private final class TargetBean implements MultiItem {
        String target;
        String datetime;

        TargetBean(String target, String datetime) {
            this.target = target;
            this.datetime = datetime;
        }

        @Override
        public int getItemType() {
            return 0;
        }
    }

    private final class InnerBean implements MultiItem {
        String id;
        String shopId;
        String shopName;
        String performance;

        InnerBean(String id, String shopId, String shopName, String performance) {
            this.id = id;
            this.shopId = shopId;
            this.shopName = shopName;
            this.performance = performance;
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

        InnerAdapter(Context context, List<MultiItem> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == 0) return R.layout.item_set_business_target_header;
            return R.layout.item_set_business_target;
        }

        void addTarget() {
            mDatas.add(new InnerBean(null, null, null, null));
            mAdapter.notifyItemInserted(mDatas.size());
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem item, int position) {
            if (holder.getItemViewType() == 0) {
                final TargetBean bean = (TargetBean) item;
                holder.setText(R.id.tv_select_time, bean.datetime);
                holder.setOnClickListener(R.id.tv_select_time, view -> onSelectDatetime(holder, bean));
                EditText etTarget = holder.obtainView(R.id.et_target_performance);
                etTarget.setFilters(new InputFilter[]{new CashierInputFilter()});
                if (etTarget.getTag() instanceof TextWatcher) {
                    etTarget.removeTextChangedListener((TextWatcher) etTarget.getTag());
                }
                TextWatcher textWatcher = new SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                        bean.target = cs.toString();
                    }
                };
                etTarget.addTextChangedListener(textWatcher);
                etTarget.setTag(textWatcher);
                etTarget.setText(bean.target);
            } else {
                final InnerBean bean = (InnerBean) item;
                holder.setText(R.id.tv_select_shop, bean.shopName);
                EditText etPerformance = holder.obtainView(R.id.et_target_performance);
                etPerformance.setFilters(new InputFilter[]{new CashierInputFilter()});
                if (etPerformance.getTag() instanceof TextWatcher) {
                    etPerformance.removeTextChangedListener((TextWatcher) etPerformance.getTag());
                }
                TextWatcher textWatcher = new SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                        bean.performance = cs.toString();
                    }
                };
                etPerformance.addTextChangedListener(textWatcher);
                etPerformance.setTag(textWatcher);
                etPerformance.setText(bean.performance);
                holder.setOnClickListener(R.id.tv_select_shop, view -> onSelectShop(holder, bean));
                holder.itemView.setOnLongClickListener(view -> {
                    showDeleteTips(position);
                    return true;
                });
            }
        }

        /*选择时间*/
        private void onSelectDatetime(final RecyclerHolder holder, final TargetBean bean) {
            new CalendarPickerDialog.Builder(mContext)
                    .clickToClear(true)
                    .withSelectedDates(mSelectDates)
                    .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                        @Override
                        public void onLeftClicked(CalendarPickerDialog dialog) {

                        }

                        @Override
                        public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                            if (TimeUtil.isSameDay(end, new Date()) || end.after(new Date())) {  //当天或者未来时间
                                dialog.dismiss();
                                mSelectDates = selectedDates;
                                mStartDate = start;
                                mEndDate = end;
                                String datetime = TimeUtil.timeMillsFormat(start) + "-" + TimeUtil.timeMillsFormat(end);
                                bean.datetime = datetime;
                                holder.setText(R.id.tv_select_time, datetime);
                            } else {
                                mFragment.showShortToast("结束时间只能选择当前或未来");
                            }
                        }
                    }).show();
        }

        /*选择门店*/
        private void onSelectShop(final RecyclerHolder holder, InnerBean bean) {
            List<DictionaryBean> optionItems = new ArrayList<>();
            for (BusinessTargetBean.ShopBean shopBean : mTargetBean.getShopList()) {
                optionItems.add(new DictionaryBean(shopBean.shopId, shopBean.shopName));
            }
            PickerHelper.showOptionsPicker(mContext, optionItems, ((p, b) -> {
                if (isSameShop(b.id)) {
                    // 若选择了一样的门店，保存时出现提示：请勿设置相同门店；
                    mFragment.showShortToast(mContext.getString(R.string.donot_set_the_same_store));
                } else {
                    bean.shopId = b.id;
                    bean.shopName = b.name;
                    holder.setText(R.id.tv_select_shop, b.name);
                }
            }));
        }

        private boolean isSameShop(String shopId) {
            for (MultiItem item : mDatas) {
                if (item.getItemType() == 1 && TextUtils.equals(((InnerBean) item).shopId, shopId)) {
                    return true;
                }
            }
            return false;
        }

        private void showDeleteTips(final int position) {
            new MaterialDialog.Builder(mContext)
                    .message(R.string.dialog_message_delete_target_performance)
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

    private BusinessTargetBean mTargetBean;
    private RecyclerView mRecyclerView;
    private List<MultiItem> mDataList;
    private InnerAdapter mAdapter;
    private List<InnerBean> mRemoveItems;  //被移除的数据集
    private TextView mAddNewItemTextView;

    private Date mStartDate, mEndDate;
    private List<Date> mSelectDates;

    SetBusinessTargetHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        mTargetBean = (BusinessTargetBean) IntentValue.getInstance().removeBy("businessTargetBean");
        init(callback);
    }

    private void init(final Callback callback) {
        mAddNewItemTextView = obtainView(R.id.tv_add_item);
        mRecyclerView = obtainView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDataList = new ArrayList<>();
        if (!TextUtils.isEmpty(mTargetBean.getMoney())) {  //有设置记录
            mStartDate = TimeUtil.stampToDate(mTargetBean.dealerId);
            mEndDate = TimeUtil.stampToDate(mTargetBean.dealerNo);
            String datetime = TimeUtil.timeMillsFormat(mStartDate)
                    + "-" + TimeUtil.timeMillsFormat(mEndDate);
            mDataList.add(new TargetBean(mTargetBean.getMoney(), datetime));
        } else {
            mDataList.add(new TargetBean(null, null));
        }
        //有设置记录
        for (BusinessTargetBean.ShopTargetBean bean : mTargetBean.getShopTarget()) {
            mDataList.add(new InnerBean(bean.id, bean.crmOrderNo, bean.name, bean.getMoney()));
        }
        mAdapter = new InnerAdapter(mContext, mDataList);
        mRecyclerView.setAdapter(mAdapter);
        checkAndInvalidate();
        mAddNewItemTextView.setOnClickListener(view -> checkAndAddItem());
        obtainView(R.id.tv_save).setOnClickListener(view -> onSave(callback));
    }

    //最多可增加经销商实际门店数的门店业绩目标（门店状态含转让、运营中）
    private void checkAndAddItem() {
        if (getItemCount() < mTargetBean.getShopList().size()) {
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
        if (getItemCount() >= mTargetBean.getShopList().size()) {
            mAddNewItemTextView.setEnabled(false);
        } else {
            if (!mAddNewItemTextView.isEnabled()) {
                mAddNewItemTextView.setEnabled(true);
            }
        }
    }

    private void onSave(Callback callback) {
        if (mStartDate == null || mEndDate == null) {   //请选择目标时间
            mFragment.showShortToast(mContext.getString(R.string.tips_please_select) + mContext.getString(R.string.target_time));
        } else {
            TargetBean targetBean = (TargetBean) mDataList.get(0);
            String value = targetBean.target;
            if (TextUtils.isEmpty(value)) { //请输入成交目标
                mFragment.showShortToast(mContext.getString(R.string.enter) + mContext.getString(R.string.transaction_target));
            } else {
                if (!isFillAll()) {
                    mFragment.showShortToast(mContext.getString(R.string.tips_complete_information));
                } else {
                    //下面门店目标业绩之和，不能超过目标业绩，超过目标业绩保存时出现提示：当前门店目标业绩之和已超过目标业绩
                    if (getTotal() > getTarget(value)) {
                        mFragment.showShortToast(mContext.getString(R.string.business_target_fill_in_error));
                    } else {
                        List<Param> list = new ArrayList<>();
                        Param p = new Param();
                        p.money = ParamHelper.urlEncode(value);
                        p.param2 = "50";
                        p.dealerId = TimeUtil.dateToStamp(mStartDate, false);
                        p.dealerNo = TimeUtil.dateToStamp(mEndDate, true);
                        list.add(p);
                        for (MultiItem item : mDataList) {
                            if (item.getItemType() == 1) {
                                InnerBean bean = (InnerBean) item;
                                Param param = new Param();
                                param.id = bean.id;
                                param.param2 = "51";
                                param.crmOrderNo = bean.shopId;
                                param.money = ParamHelper.urlEncode(bean.performance);
                                list.add(param);
                            }
                        }
                        String ids = "";
                        if (mRemoveItems != null && !mRemoveItems.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < mRemoveItems.size(); i++) {
                                sb.append(mRemoveItems.get(i).id);
                                if (i < mRemoveItems.size() - 1)
                                    sb.append(",");
                            }
                            ids = sb.toString();
                            LogCat.e("removeItems", sb.toString());
                        }
                        callback.onSave(MyJsonParser.fromBeanToJson(list), ids);
                    }
                }
            }
        }
    }

    /*是否填写了所有信息*/
    private boolean isFillAll() {
        for (MultiItem item : mDataList) {
            if (item instanceof InnerBean && (TextUtils.isEmpty(((InnerBean) item).shopId)
                    || TextUtils.isEmpty(((InnerBean) item).performance))) {
                return false;
            }
        }
        return true;
    }

    private double getTotal() {
        double value = 0;
        for (MultiItem item : mDataList) {
            if (item.getItemType() == 1) {
                value += ParseUtils.parseDouble(((InnerBean) item).performance);
            }
        }
        return value;
    }

    private double getTarget(String target) {
        return ParseUtils.parseDouble(target);
    }

    //crmOrderNo 经销商id或者门店id或者员工id
// dealerId 开始10位时间戳
// dealerNo 结束10位时间戳
// param2 50总业绩 51门店业绩 52个人业绩
// money 业绩 urlencode一下再传
// param1 门店id（52时需要传）
// phone 目标参数
    private final class Param {
        String id;
        String crmOrderNo;
        String dealerId;
        String dealerNo;
        String param2;
        String money;
    }

    interface Callback {
        void onSave(String param, String ids);
    }
}
