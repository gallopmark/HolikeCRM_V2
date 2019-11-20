package com.holike.crm.fragment.employee.child;

import android.content.Context;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.dialog.CalendarPickerDialog;

import java.util.Date;
import java.util.List;

abstract class EmployeeRankHelper extends FragmentHelper {
    Date mStartDate, mEndDate;
    private List<Date> mSelectDates;

    EmployeeRankHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
    }

    void onCalendarPicker() {
        new CalendarPickerDialog.Builder(mContext).maxDate(new Date())
                .withSelectedDates(mSelectDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        if (selectedDates.size() >= 1) {
                            mSelectDates = selectedDates;
                            mStartDate = start;
                            mEndDate = end;
                        } else {
                            mStartDate = null;
                            mEndDate = null;
                        }
                        doRequest();
                    }
                }).onShowListener(dialogInterface -> onShowCalendar())
                .dismissListener(dialogInterface -> onDismissCalendar())
                .show();
    }

    abstract void onShowCalendar();

    abstract void onDismissCalendar();

    abstract void doRequest();

    abstract class DataAdapter extends AbsFormAdapter<EmployeeRankingBean.RankingDataBean> {

        DataAdapter(Context context, List<EmployeeRankingBean.RankingDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, EmployeeRankingBean.RankingDataBean bean, int position) {
            if (position == 0 || position == 1 || position == 2) {
                holder.setVisibility(R.id.iv_rank, View.VISIBLE);
                holder.setVisibility(R.id.tv_rank, View.INVISIBLE);
                if (position == 0) {
                    holder.setImageResource(R.id.iv_rank, R.drawable.ranking_first);
                } else if (position == 1) {
                    holder.setImageResource(R.id.iv_rank, R.drawable.ranking_second);
                } else {
                    holder.setImageResource(R.id.iv_rank, R.drawable.ranking_third);
                }
            } else {
                holder.setVisibility(R.id.iv_rank, View.INVISIBLE);
                holder.setVisibility(R.id.tv_rank, View.VISIBLE);
            }
            holder.setText(R.id.tv_rank, bean.rank);
            holder.setText(R.id.tv_city, bean.dealerName);
            holder.setText(R.id.tv_store, bean.shopName);
            holder.setText(R.id.tv_name, bean.userName);
            bindTypeData(holder, bean, position);
        }

        abstract void bindTypeData(RecyclerHolder holder, EmployeeRankingBean.RankingDataBean bean, int position);
    }

    /*个人签单-列表适配器*/
    class SigningAdapter extends DataAdapter {

        SigningAdapter(Context context, List<EmployeeRankingBean.RankingDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_rank_signing;
        }

        @Override
        void bindTypeData(RecyclerHolder holder, EmployeeRankingBean.RankingDataBean bean, int position) {
            holder.setText(R.id.tv_sign_count, bean.Counts); //签单数
        }
    }

    /*成交-列表适配器*/
    class TransactionAdapter extends DataAdapter {

        TransactionAdapter(Context context, List<EmployeeRankingBean.RankingDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_rank_transaction;
        }

        @Override
        void bindTypeData(RecyclerHolder holder, EmployeeRankingBean.RankingDataBean bean, int position) {
            holder.setText(R.id.tv_transaction_amount, bean.Sum);
        }
    }

    /*回款-列表适配器*/
    class PaybackAdapter extends DataAdapter {

        PaybackAdapter(Context context, List<EmployeeRankingBean.RankingDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_rank_payback;
        }

        @Override
        void bindTypeData(RecyclerHolder holder, EmployeeRankingBean.RankingDataBean bean, int position) {
            holder.setText(R.id.tv_payback_amout, bean.Receiver);
        }
    }

    /*下单排行-列表适配器*/
    class OrderAdapter extends DataAdapter {

        OrderAdapter(Context context, List<EmployeeRankingBean.RankingDataBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_employee_rank_order;
        }

        @Override
        void bindTypeData(RecyclerHolder holder, EmployeeRankingBean.RankingDataBean bean, int position) {
            holder.setText(R.id.tv_order_amount, bean.Order);
        }
    }
}
