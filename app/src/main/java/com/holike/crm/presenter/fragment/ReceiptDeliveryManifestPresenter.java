package com.holike.crm.presenter.fragment;

import android.content.Context;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.ReceiptDeliveryManifestBean;
import com.holike.crm.customView.listener.TouchPressListener;
import com.holike.crm.model.fragment.ReceiptDeliveryManifestModel;
import com.holike.crm.view.fragment.ReceiptDeliveryManifestView;

import java.util.List;

public class ReceiptDeliveryManifestPresenter extends BasePresenter<ReceiptDeliveryManifestView, ReceiptDeliveryManifestModel> {
    private int selectPosition = 0;
    private int selectSerialPosition = 0;

    public void getData(String orderId) {
        model.getData(orderId, new ReceiptDeliveryManifestModel.ReceiptDeliveryManifestListener() {
            @Override
            public void success(final List<ReceiptDeliveryManifestBean> messageBean) {
                if (messageBean.size() > 0) {
                    if (getView() != null)
                        getView().onInitTopSuccess(messageBean);
                    if (messageBean.get(0).getPackingList().size() > 0) {
                        if (getView() != null)
                            getView().onInitSideSuccess(messageBean);
                        if (messageBean.get(0).getPackingList().get(0).getPackingData().size() > 0) {
                            if (getView() != null)
                                getView().onInitContentSuccess(messageBean);
                        }
                    }
                } else {
                    if (getView() != null)
                        getView().onFail("没有数据!");
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().onFail(failed);
            }
        });

    }


    public void setTopBarAdapter(final Context ctx, final RecyclerView rv, final RecyclerView rvSerial, final List<ReceiptDeliveryManifestBean> datas) {
        rv.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new CommonAdapter<ReceiptDeliveryManifestBean>(ctx, datas) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_receipt_delivery_title_bar;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ReceiptDeliveryManifestBean bean, int position) {
                holder.itemView.setSelected(selectPosition == position);
                TextView tv = holder.obtainView(R.id.item_tv_tab_bar);
                tv.setText(bean.getLargeSizeComponentsIdentify());
                tv.setBackgroundResource(holder.itemView.isSelected() ? R.drawable.bg_tab_bar_blue : 0);
                tv.setTextColor(holder.itemView.isSelected() ? ContextCompat.getColor(ctx, R.color.color_while) : ContextCompat.getColor(ctx, R.color.textColor5));
                if (getView() != null)
                    getView().onTopBarSelect(position, datas.get(selectPosition));
                holder.itemView.setOnTouchListener(new TouchPressListener(ctx, R.color.color_while, R.color.textColor5, R.color.light_blue, new View[]{tv}, v -> {
                    if (!holder.itemView.isSelected()) {
                        selectPosition = position;
                        selectSerialPosition = 0;
                        rv.scrollToPosition(position);
                        if (getView() != null)
                            getView().onTopBarSelect(position, datas.get(position));
                        setPackageSerialAdapter(ctx, rvSerial, datas.get(position).getPackingList());
                    }
                    notifyDataSetChanged();
                }));
            }
        });
    }

    public void setPackageSerialAdapter(final Context ctx, final RecyclerView rv, final List<ReceiptDeliveryManifestBean.PackingListBean> datas) {

        rv.setLayoutManager(new LinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<ReceiptDeliveryManifestBean.PackingListBean>(ctx, datas) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_receipt_delivery_package_serial;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ReceiptDeliveryManifestBean.PackingListBean bean, int position) {
                holder.itemView.setSelected(position == selectSerialPosition);
                TextView tvSerial = holder.obtainView(R.id.item_tv_serial);
                TextView tvBarcode = holder.obtainView(R.id.item_tv_barcode);
                LinearLayout llBg = holder.obtainView(R.id.item_ll_serial_bg);
                tvSerial.setText(TextUtils.isEmpty(bean.getPackingSerialNumber()) ? "-" : bean.getPackingSerialNumber());
                tvSerial.setTextColor(holder.itemView.isSelected() ? ContextCompat.getColor(ctx, R.color.color_while) :  ContextCompat.getColor(ctx, R.color.textColor5));
                tvBarcode.setText(TextUtils.isEmpty(bean.getPackingBarCode()) ? "-" : bean.getPackingBarCode());
                tvBarcode.setTextColor(holder.itemView.isSelected() ?  ContextCompat.getColor(ctx, R.color.color_while) :  ContextCompat.getColor(ctx, R.color.textColor5));

                llBg.setBackgroundColor(holder.itemView.isSelected() ?  ContextCompat.getColor(ctx, R.color.textColor5) :  ContextCompat.getColor(ctx, R.color.color_while));
                holder.itemView.setOnTouchListener(new TouchPressListener(ctx, R.color.color_while, R.color.textColor5, R.color.light_blue, new View[]{tvSerial, tvBarcode}, v -> {
                    if (!holder.itemView.isSelected()) {
                        selectSerialPosition = position;
                        rv.scrollToPosition(position);
                        if (getView() != null)
                            getView().onSerialSelect(position, bean.getPackingData());

                    }
                    notifyDataSetChanged();
                }));
            }
        });
    }

    public void setPackageInfoAdapter(final Context ctx, final RecyclerView rv, final List<ReceiptDeliveryManifestBean.PackingListBean.PackingDataBean> datas) {
        new Handler().postDelayed(() -> {
            rv.setLayoutManager(new LinearLayoutManager(ctx));
            rv.setAdapter(new CommonAdapter<ReceiptDeliveryManifestBean.PackingListBean.PackingDataBean>(ctx, datas) {

                @Override
                protected int bindView(int viewType) {
                    return R.layout.item_receipt_delivery_package_info;
                }

                @Override
                public void onBindHolder(RecyclerHolder holder, ReceiptDeliveryManifestBean.PackingListBean.PackingDataBean bean, int position) {
                    holder.setText(R.id.item_tv_project_line_number, TextUtils.isEmpty(bean.getRowNumber()) ? "-" : bean.getRowNumber());
                    holder.setText(R.id.item_tv_material_description_material, TextUtils.isEmpty(bean.getMaterialsDescription()) ? "-" : bean.getMaterialsDescription());
                    holder.setText(R.id.item_tv_color, TextUtils.isEmpty(bean.getAssortment()) ? "-" : bean.getAssortment());
                    holder.setText(R.id.item_tv_length, TextUtils.isEmpty(String.valueOf(bean.getLength())) ? "-" : String.valueOf(bean.getLength()));
                    holder.setText(R.id.item_tv_with, TextUtils.isEmpty(String.valueOf(bean.getWidth())) ? "-" : String.valueOf(bean.getWidth()));
                    holder.setText(R.id.item_tv_thick, TextUtils.isEmpty(String.valueOf(bean.getPly())) ? "-" : String.valueOf(bean.getPly()));
                    holder.setText(R.id.item_tv_count, TextUtils.isEmpty(String.valueOf(bean.getQuantity())) ? "-" : String.valueOf(bean.getQuantity()));
                    holder.setText(R.id.item_tv_unit, TextUtils.isEmpty(String.valueOf(bean.getUnit())) ? "-" : String.valueOf(bean.getUnit()));
                }
            });
        }, 20);

    }


}
