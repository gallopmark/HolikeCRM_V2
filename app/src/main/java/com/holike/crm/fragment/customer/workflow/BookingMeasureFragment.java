package com.holike.crm.fragment.customer.workflow;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.fragment.customer.WorkflowFragment;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.manager.FlowLayoutManager;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.view.fragment.WorkflowView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/3.
 * 预约量房
 */

public class BookingMeasureFragment extends WorkflowFragment implements WorkflowView {
    @BindView(R.id.tv_booking_measure_time)
    TextView tvTime;
    @BindView(R.id.tv_booking_measure_man)
    TextView tvMan;
    @BindView(R.id.rv_booking_measure)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_booking_measure_note)
    EditText etNote;

    private List<Boolean> selectList;
    private List<TypeIdBean.TypeIdItem> rooms;
    private String ids;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_booking_measure;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.house_manage_booking_measure));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new FlowLayoutManager());
        mPresenter.getTypeId();
        showLoading();
    }

    @OnClick({R.id.tv_booking_measure_time, R.id.tv_booking_measure_man, R.id.tv_booking_measure_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_booking_measure_time:
                showTimePickerView(mContext, getText(tvTime), etNote);
                break;
            case R.id.tv_booking_measure_man:
                if (professionBeans == null) {
                    mPresenter.getAssociate(houseInfoBean.getShopId());
                    showLoading();
                } else {
                    showPickerView(professionBeans, getText(tvMan), etNote);
                }
                break;
            case R.id.tv_booking_measure_save:
                ids = null;
                for (int i = 0, size = rooms.size(); i < size; i++) {
                    if (selectList.get(i)) {
                        if (ids == null) {
                            ids = rooms.get(i).getId();
                        } else {
                            ids = ids + "," + rooms.get(i).getId();
                        }
                    }
                }
                if (isTextEmpty(tvTime) || ids == null || professionBean == null) {
                    showShortToast(R.string.tips_complete_information);
                } else {
                    mPresenter.bookingMeasure(getText(etNote), customerStatus, houseId, ids, getText(tvTime), operateCode, personalId, prepositionRuleStatus, professionBean.getUserId());
                    showLoading();
                }
                break;
        }
    }

    /**
     * 选中时间
     *
     * @param date
     */
    @Override
    protected void selectTime(Date date) {
        tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
    }

    /**
     * 选中量房人员
     *
     * @param options
     */
    @Override
    protected void optionsSelect(int options) {
        professionBean = professionBeans.get(options);
        tvMan.setText(professionBean.getUserName());
    }

    /**
     * 获取人员/获取类型id/保存成功
     *
     * @param success
     */
    @Override
    public void success(Object success) {
        dismissLoading();
        if (success instanceof AssociateBean) {
            professionBeans = ((AssociateBean) success).getProfession();
            showPickerView(professionBeans, getText(tvMan), etNote);
        } else if (success instanceof TypeIdBean) {
            selectList = new ArrayList<>();
            rooms = AddCustomerPresenter.getTypeIdItems(((TypeIdBean) success).getDIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE());
            for (TypeIdBean.TypeIdItem ignored : rooms) {
                selectList.add(false);
            }

            mRecyclerView.setAdapter(new CommonAdapter<TypeIdBean.TypeIdItem>(mContext, rooms) {
                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_rv_booking_measure_room;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, TypeIdBean.TypeIdItem typeIdItem, int position) {
                    TextView tv = holder.obtainView(R.id.tv_item_rv_booking_measure);
                    CheckBox cb = holder.obtainView(R.id.cb_item_rv_booking_measure);
                    tv.setText(typeIdItem.getName());
                    if (!selectList.get(position)) {
                        tv.setTextColor(Color.parseColor("#999999"));
                        cb.setChecked(false);
                    } else {
                        tv.setTextColor(Color.parseColor("#222222"));
                        cb.setChecked(true);
                    }
                    holder.itemView.setOnClickListener(v -> {
                        selectList.set(position, !selectList.get(position));
                        notifyDataSetChanged();
                    });
                }
            });
        } else {
            showShortToast(MyJsonParser.getShowMessage((String) success));
            operateSuccess();
        }
    }


    /**
     * 获取人员/获取类型id/保存失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
