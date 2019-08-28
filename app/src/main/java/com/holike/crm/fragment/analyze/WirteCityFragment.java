package com.holike.crm.fragment.analyze;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.WriteCityBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.presenter.fragment.WirteCityPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WirteCityView;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/6/26.
 * 主动营销-填写城市
 */

public class WirteCityFragment extends MyFragment<WirteCityPresenter, WirteCityView> implements WirteCityView {
    @BindView(R.id.ll_write_city)
    LinearLayout llMain;
    @BindView(R.id.tv_write_city_service_city)
    TextView tvServiceCity;
    @BindView(R.id.tv_write_city_service_data)
    TextView tvServiceData;
    @BindView(R.id.rv_write_city)
    RecyclerView rv;

    private WriteCityBean writeCityBean;
    private WriteCityBean.SelectDataBean selectDataBean;
    private String startTime, endTime;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_wirte_city;
    }

    @Override
    protected WirteCityPresenter attachPresenter() {
        return new WirteCityPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_active_market_write_city));
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mPresenter.getData();
        showLoading();
    }

    @Override
    public void getDataSuccess(WriteCityBean bean) {
        dismissLoading();
        writeCityBean = bean;
        rv.setAdapter(new CommonAdapter<WriteCityBean.ActiveRecordBean>(mContext, bean.getActiveRecord()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_write_city;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, WriteCityBean.ActiveRecordBean activeRecordBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_write_city);
                TextView tvCity = holder.obtainView(R.id.tv_item_rv_write_city_service_city);
                TextView tvDara = holder.obtainView(R.id.tv_item_rv_write_city_service_data);
                TextView tvOperate = holder.obtainView(R.id.tv_item_rv_write_city_service_operate);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvCity.setText(activeRecordBean.getName());
                tvDara.setText(activeRecordBean.getTime());
                tvOperate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loading();
                        mPresenter.delCity(activeRecordBean.getDealerId(), position);
                    }
                });
            }
        });
    }

    @Override
    public void failed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void saveSucess(String success, WriteCityBean.ActiveRecordBean bean) {
        dismissLoading();
        showShortToast(MyJsonParser.getShowMessage(success));
        writeCityBean.getActiveRecord().add(0, bean);
        if (rv.getAdapter() != null)
            rv.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void delSuccess(String success, int position) {
        dismissLoading();
        showShortToast(MyJsonParser.getShowMessage(success));
        writeCityBean.getActiveRecord().remove(position);
        if (rv.getAdapter() != null)
            rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void tips(String text) {
        showShortToast(text);
    }

    @Override
    public void loading() {
        showLoading();
    }

    @Override
    protected void onFinishResult(int requestCode, int resultCode, Map<String, Serializable> result) {
        super.onFinishResult(requestCode, resultCode, result);
        if (resultCode == Constants.RESULT_CODE_DEALER_ID) {
            selectDataBean = (WriteCityBean.SelectDataBean) result.get(Constants.DEALER_ID);
            if (selectDataBean != null)
                tvServiceCity.setText(selectDataBean.getDealerName());
        }
    }

    private List<Date> mSelectedDates;

    @OnClick({R.id.tv_write_city_service_city, R.id.tv_write_city_service_data, R.id.btn_write_city_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_write_city_service_city:
                Map<String, Serializable> params = new HashMap<>();
                params.put(Constants.WRITE_CITY_BEAN, writeCityBean);
                startFragment(params, new SelectCityFragment());
                break;
            case R.id.tv_write_city_service_data:
                mPresenter.selectDate(mContext, mSelectedDates, new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        mSelectedDates = selectedDates;
                        if (selectedDates.size() >= 1) {
                            startTime = TimeUtil.dateToStamp(start, false);
                            endTime = TimeUtil.dateToStamp(end, true);
//                            startTime = TimeUtil.dataToStamp(start, "yyyy年MM月dd日");
//                            endTime = TimeUtil.dataToStamp(end, "yyyy年MM月dd日");
                            tvServiceData.setText(TimeUtil.dateToString(start, "MM.dd") + "-" + TimeUtil.dateToString(end, "MM.dd"));
                        } else {
                            startTime = null;
                            endTime = null;
                            tvServiceData.setText(null);
                        }
                    }
                });
                break;
            case R.id.btn_write_city_save:
                mPresenter.saveCity(selectDataBean, startTime, endTime, tvServiceData.getText().toString());
                break;
        }
    }
}
