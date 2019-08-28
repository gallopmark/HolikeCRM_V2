package com.holike.crm.presenter.fragment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.BaseRecyclerAdapter;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.LineAttractBean;
import com.holike.crm.customView.WrapContentLinearLayoutManager;
import com.holike.crm.customView.listener.TouchPressListener;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.model.fragment.OnlineAttractReportModel;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.fragment.OnlineAttractReportView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线上引流Presenter
 */
public class OnlineAttractReportPresenter extends BasePresenter<OnlineAttractReportView, OnlineAttractReportModel> {

    private List<LineAttractBean.DealerDataBean> storeList = new ArrayList<>();
    private StoreListAdapter storeListAdapter;

    /*门店列表适配器*/
    private class StoreListAdapter extends CommonAdapter<LineAttractBean.DealerDataBean> {
        private int selectPosition = 0;
        private LineAttractBean attractBean;

        StoreListAdapter(Context context, List<LineAttractBean.DealerDataBean> mDatas) {
            super(context, mDatas);
        }

        void addDataBean(LineAttractBean attractBean) {
            this.attractBean = attractBean;
            this.mDatas.clear();
            selectPosition = 0;
            if (getView() != null) getView().onSmoothScroll(selectPosition);
            if (attractBean.getDealerData() != null && !attractBean.getDealerData().isEmpty()) {
                this.mDatas.addAll(attractBean.getDealerData());
            }
            notifyDataSetChanged();
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_receipt_delivery_title_bar;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, LineAttractBean.DealerDataBean dealerDataBean, int position) {
            holder.itemView.setSelected(selectPosition == position);
            TextView tv = holder.obtainView(R.id.item_tv_tab_bar);
            String store = mContext.getString(R.string.store) + (position + 1);
            tv.setText(store);
            tv.setBackgroundResource(holder.itemView.isSelected() ? R.drawable.bg_tab_bar_blue : 0);
            tv.setTextColor(holder.itemView.isSelected() ? ContextCompat.getColor(mContext, R.color.color_while) : ContextCompat.getColor(mContext, R.color.textColor5));
            holder.itemView.setOnTouchListener(new TouchPressListener(mContext, R.color.color_while, R.color.textColor5, R.color.light_blue, new View[]{tv}, v -> {
//                        getView().onTagClickStart(datas.getInstance(position).getDataList().size() > 11);
                if (!holder.itemView.isSelected()) {
                    selectPosition = position;
                    if (getView() != null) getView().onSmoothScroll(selectPosition);
                    if (getView() != null)
                        getView().onTagSelect(position, attractBean);
                    notifyDataSetChanged();
                }
            }));
        }
    }

    public void setStoreAdapter(RecyclerView rvStore) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(rvStore.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvStore.setLayoutManager(layoutManager);
        storeListAdapter = new StoreListAdapter(rvStore.getContext(), storeList);
        rvStore.setAdapter(storeListAdapter);
    }

    public void addStoreBean(LineAttractBean attractBean) {
        if (attractBean != null) {
            storeListAdapter.addDataBean(attractBean);
        }
    }

    private List<ListViewBean> titleList = new ArrayList<>();
    private TitleListAdapter titleListAdapter;

    private class TitleListAdapter extends CommonAdapter<ListViewBean> {

        TitleListAdapter(Context context, List<ListViewBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_online_drainage_title;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, ListViewBean bean, int position) {
            holder.setText(R.id.tv_online_drainage_title, bean.getName());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.obtainView(R.id.fl_online_drainage_title).getLayoutParams();
            params.width = DensityUtil.dp2px(bean.getLayoutWith());
            holder.itemView.setLayoutParams(params);
        }
    }

    public void setTitleListAdapter(RecyclerView rvTitle) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(rvTitle.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTitle.setLayoutManager(layoutManager);
        titleListAdapter = new TitleListAdapter(rvTitle.getContext(), titleList);
        rvTitle.setAdapter(titleListAdapter);
    }

    public void addTitleData(Context context, boolean isDealer) {
        List<ListViewBean> beans = titleData(context, isDealer);
        this.titleList.clear();
        this.titleList.addAll(beans);
        titleListAdapter.notifyDataSetChanged();
    }

    public void getData(String cityCode, String startTime, String endTime, String time, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", TextUtils.isEmpty(cityCode) ? "" : cityCode);
        params.put("startTime", TextUtils.isEmpty(startTime) ? "" : startTime);
        params.put("endTime", TextUtils.isEmpty(endTime) ? "" : endTime);
        params.put("time", TextUtils.isEmpty(time) ? "" : time);
        params.put("type", TextUtils.isEmpty(type) ? "" : type);
        model.getData(params, new OnlineAttractReportModel.OnlineAttractReportListener() {
            @Override
            public void onSuccess(LineAttractBean result) {
                if (getView() != null)
                    getView().getDataSuccess(result);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().getDataFail(errorMsg);
            }
        });
    }

    /**
     * 设置左边一列的数据--经销商
     */
    public void setListSide(Context context, RecyclerView rv, LineAttractBean beans, int page) {
        if (beans.getDealerData() == null || beans.getDealerData().size() == 0) return;
        if (page < 0 || page > beans.getDealerData().size()) return;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new CommonAdapter<LineAttractBean.DealerDataBean.DataListBean>(context, beans.getDealerData().get(page).getDataList()) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_online_drainage_side;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, LineAttractBean.DealerDataBean.DataListBean bean, int position) {
                TextView mSideTv = holder.obtainView(R.id.item_tv_side);
                if (position == 0) {
                    mSideTv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
                    mSideTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.textSize_13));
                } else {
                    mSideTv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor8));
                    mSideTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.textSize_12));
                }
                mSideTv.setText(bean.getName());
                holder.setText(R.id.item_tv_side, bean.getName());
                setLayoutWidth(holder, R.id.fl_online_drainage_side, 60);
                holder.setBackgroundColor(R.id.fl_online_drainage_side, context.getResources().getColor(position % 2 == 1 ? R.color.bg_transparent : R.color.container_divider));
            }

        });
    }


    /**
     * 设置左边一列的数据--非经销商
     */
    public void setListSide(Context context, RecyclerView rv, LineAttractBean beans, ClickListener listener) {
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new CommonAdapter<LineAttractBean.PercentDataBean>(context, beans.getPercentData()) {

            @Override
            protected int bindView(int viewType) {
                return  R.layout.item_online_drainage_side;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, LineAttractBean.PercentDataBean bean, int position) {
                TextView tvSide = holder.obtainView(R.id.item_tv_side);
                holder.setText(R.id.item_tv_side, bean.getArea());
                setLayoutWidth(holder, R.id.fl_online_drainage_side, 64);
                tvSide.setTextColor(ContextCompat.getColor(mContext, bean.getIsClick().equals("1") ? R.color.textColor5 : R.color.textColor4));
                holder.setBackgroundColor(R.id.fl_online_drainage_side, context.getResources().getColor(position % 2 == 1 ? R.color.bg_transparent : R.color.light_text5));
                if (TextUtils.equals(bean.getIsClick(), "1")) {
                    tvSide.setOnClickListener(v -> listener.onSideClick(bean));
                } else {
                    tvSide.setOnClickListener(null);
                }
            }
        });
    }

    public interface ClickListener {
        void onSideClick(LineAttractBean.PercentDataBean bean);
    }

    /**
     * 列表内容--非经销商
     */
    public void setListContent(Context context, RecyclerView rv, LineAttractBean beans) {
        rv.setLayoutManager(new WrapContentLinearLayoutManager(context));
        List<ListViewBean> list = titleData(context, beans.isDealer());
        rv.setAdapter(new CommonAdapter<LineAttractBean.PercentDataBean>(context, beans.getPercentData()) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_online_drainage_content;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, LineAttractBean.PercentDataBean bean, int position) {
                boolean line1Visible = position != 0;
                boolean line2Visible = position == getItemCount() - 1;
                holder.setVisibility(R.id.mDividerV, line1Visible);
                holder.setVisibility(R.id.mDividerV2, line2Visible);
                holder.setText(R.id.tv_online_drainage_1, bean.getCustomerTotal()); //下发客户数
                holder.setText(R.id.tv_online_drainage_2, bean.getCustomerYes()); //有效客户数
                holder.setText(R.id.tv_online_drainage_3, bean.getCustomerNo()); //无效客户数
                holder.setText(R.id.tv_online_drainage_4, bean.getCustomerNoPercent()); //无效率
                holder.setText(R.id.tv_online_drainage_5, bean.getScaleCount()); //量尺数
                holder.setText(R.id.tv_online_drainage_6, bean.getScaleCountPercent()); //量尺率
                holder.setText(R.id.tv_online_drainage_7, bean.getEarnestCount()); //订单数
                holder.setText(R.id.tv_online_drainage_8, bean.getEarnestMoney()); //订金金额(万)
                holder.setText(R.id.tv_online_drainage_9, bean.getSigningCount()); //签约数
                holder.setText(R.id.tv_online_drainage_10, bean.getSigningMoney()); //合同金额(万)
                holder.setText(R.id.tv_online_drainage_11, bean.getOrderPercent()); //订单转化率
                holder.setVisibility(R.id.fl_online_drainage_12, false);
                holder.setVisibility(R.id.fl_online_drainage_13, false);
                setLayoutWidth(holder, R.id.fl_online_drainage_1, list.get(0).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_2, list.get(1).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_3, list.get(2).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_4, list.get(3).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_5, list.get(4).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_6, list.get(5).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_7, list.get(6).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_8, list.get(7).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_9, list.get(8).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_10, list.get(9).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_11, list.get(10).getLayoutWith());
                holder.setBackgroundColor(R.id.ll_product_info_content_parent, ContextCompat.getColor(context, position % 2 == 1 ? R.color.bg_transparent : R.color.container_divider));
            }
        });
    }


    /**
     * 列表内容--经销商
     */
    public void setListContent(Context context, RecyclerView rv, LineAttractBean beans, int page) {
        if (beans.getDealerData() == null || beans.getDealerData().size() == 0) return;
        if (page < 0 || page > beans.getDealerData().size()) return;
        rv.setLayoutManager(new WrapContentLinearLayoutManager(context));
        List<ListViewBean> list = titleData(context, beans.isDealer());
        rv.setAdapter(new CommonAdapter<LineAttractBean.DealerDataBean.DataListBean>(context, beans.getDealerData().get(page).getDataList()) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_online_drainage_content;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, LineAttractBean.DealerDataBean.DataListBean bean, int position) {
                boolean line1Visible = position != 0;
                boolean line2Visible = position == getItemCount() - 1;
                holder.setVisibility(R.id.mDividerV, line1Visible);
                holder.setVisibility(R.id.mDividerV2, line2Visible);
                holder.setText(R.id.tv_online_drainage_1, bean.getCustomerTotal()); //分配数
                holder.setText(R.id.tv_online_drainage_2, bean.getCustomerCommunicate());  //已沟通数
                holder.setText(R.id.tv_online_drainage_3, bean.getCustomerNo());    //无效退回数
                holder.setText(R.id.tv_online_drainage_4, bean.getCustomerLoss());  //已流失数
                holder.setText(R.id.tv_online_drainage_5, bean.getCustomerAppointment());   //预约量尺数
                holder.setText(R.id.tv_online_drainage_6, bean.getCustomerMeasure());   //已量尺数
                holder.setText(R.id.tv_online_drainage_7, bean.getCustomerDeposit());  //已收定金数
                holder.setText(R.id.tv_online_drainage_8, bean.getCustomerDepositMoney());   //已收定金金额(万)
                holder.setText(R.id.tv_online_drainage_9, bean.getCustomerBill());  //已签合同数
                holder.setText(R.id.tv_online_drainage_10, bean.getSigningMoney()); //已签合同金额(万)
                holder.setText(R.id.tv_online_drainage_11, bean.getScalePercent()); //量尺转化率
                holder.setText(R.id.tv_online_drainage_12, bean.getOrderPercent()); //定金转化率
//                holder.setText(R.id.tv_online_drainage_13, bean.getEarnestMoney());
                holder.setVisibility(R.id.fl_online_drainage_13, false);
                setLayoutWidth(holder, R.id.fl_online_drainage_1, list.get(0).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_2, list.get(1).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_3, list.get(2).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_4, list.get(3).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_5, list.get(4).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_6, list.get(5).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_7, list.get(6).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_8, list.get(7).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_9, list.get(8).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_10, list.get(9).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_11, list.get(10).getLayoutWith());
                setLayoutWidth(holder, R.id.fl_online_drainage_12, list.get(11).getLayoutWith());
//                setLayoutWidth(holder, R.id.fl_online_drainage_13, list.getInstance(12).getLayoutWith());
                holder.setBackgroundColor(R.id.ll_product_info_content_parent, context.getResources().getColor(position % 2 == 1 ? R.color.bg_transparent : R.color.light_text5));
            }

        });
    }

    private void setLayoutWidth(BaseRecyclerAdapter.RecyclerHolder holder, int id, int width) {
        FrameLayout frameLayout = holder.obtainView(id);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
        params.width = DensityUtil.dp2px(width);
        frameLayout.setLayoutParams(params);
    }


    private List<ListViewBean> titleData(Context context, boolean isDealer) {
        String[] titles;
        int[] sizes;
        if (isDealer) {
            titles = context.getResources().getStringArray(R.array.online_attract_dealer_title);
            sizes = new int[]{50, 50, 50, 50, 50, 50, 50, 60, 60, 60, 60, 60};
        } else {
            titles = context.getResources().getStringArray(R.array.online_attract_nonDealer_title);
            sizes = new int[]{50, 50, 46, 46, 46, 46, 46, 60, 46, 60, 60};
        }
        List<ListViewBean> beans = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            beans.add(new ListViewBean(titles[i], sizes[i]));
        }
        return beans;
    }


    /**
     * 显示日期选择
     */
    public static void selectDate(Context context, List<Date> mSelectedDates, CalendarPickerDialog.OnCalendarRangeSelectedListener listener) {
        new CalendarPickerDialog.Builder(context).maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(listener).show();
    }

    public static int getSelectPosition(String time, List<LineAttractBean.SelectDataBean> selectDataBeans) {
        for (int i = 0, size = selectDataBeans.size(); i < size; i++) {
            if (time != null && time.equals(selectDataBeans.get(i).getSelectTime())) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 设置2个 RecyclerView 同步滚动
     *
     * @param hostRv   导向者
     * @param syncerRv 被导向者
     */
    public void setScrollSynchronous(RecyclerView hostRv, final RecyclerView syncerRv) {
        hostRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    syncerRv.scrollBy(dx, dy);
                }

            }
        });
    }

    class ListViewBean {
        String name;
        int layoutWith;

        ListViewBean(String name, int layoutWith) {
            this.name = name;
            this.layoutWith = layoutWith;
        }

        public String getName() {
            return name;
        }

        int getLayoutWith() {
            return layoutWith;
        }

    }
}
