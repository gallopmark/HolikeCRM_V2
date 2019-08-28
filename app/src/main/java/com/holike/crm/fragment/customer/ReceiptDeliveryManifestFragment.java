package com.holike.crm.fragment.customer;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.ReceiptDeliveryManifestBean;
import com.holike.crm.presenter.fragment.ReceiptDeliveryManifestPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.ReceiptDeliveryManifestView;

import java.util.List;

import butterknife.BindView;

/**
 * 收发货清单
 */
public class ReceiptDeliveryManifestFragment extends MyFragment<ReceiptDeliveryManifestPresenter, ReceiptDeliveryManifestView> implements ReceiptDeliveryManifestView {
    @BindView(R.id.rv_receipt)
    RecyclerView rvReceipt;
    @BindView(R.id.tv_sales_order)
    TextView tvSalesOrder;
    @BindView(R.id.tv_creation_date)
    TextView tvCreationDate;
    @BindView(R.id.tv_warehousing_operator)
    TextView tvWarehousingOperator;
    @BindView(R.id.tv_warehousing_operation_time)
    TextView tvWarehousingOperationTime;
    @BindView(R.id.tv_inventory_status)
    TextView tvInventoryStatus;
    @BindView(R.id.tv_outbound_operator)
    TextView tvOutboundOperator;
    @BindView(R.id.tv_outbound_operation_time)
    TextView tvOutboundOperationTime;
    @BindView(R.id.tv_delivery_statue)
    TextView tvDeliveryStatue;
    @BindView(R.id.tv_total_number_packages)
    TextView tvTotalNumberPackages;
    @BindView(R.id.tv_received_packets)
    TextView tvReceivedPackets;
    @BindView(R.id.tv_send_package)
    TextView tvSendPackets;
    @BindView(R.id.rv_package_serial_barcode)
    RecyclerView rvPackageSerialBarcode;
    @BindView(R.id.rv_package_message)
    RecyclerView rvPackageMessage;
    private String orderId;

    @Override
    protected void init() {
        super.init();
        showLoading();
        setTitle(getString(R.string.receipt_and_delivery_lis));
        if (getArguments() != null) {
            orderId = (String) getArguments().getSerializable(Constants.ORDER_ID);
        }
        mPresenter.getData(orderId);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_receipt_delivery_manifest;
    }

    @Override
    protected ReceiptDeliveryManifestPresenter attachPresenter() {
        return new ReceiptDeliveryManifestPresenter();
    }


    @Override
    public void onInitTopSuccess(List<ReceiptDeliveryManifestBean> bean) {
        dismissLoading();
        mPresenter.setTopBarAdapter(getActivity(), rvReceipt, rvPackageSerialBarcode, bean);

    }

    @Override
    public void onInitSideSuccess(List<ReceiptDeliveryManifestBean> bean) {
        mPresenter.setPackageSerialAdapter(getActivity(), rvPackageSerialBarcode, bean.get(0).getPackingList());

    }

    @Override
    public void onInitContentSuccess(List<ReceiptDeliveryManifestBean> bean) {
        mPresenter.setPackageInfoAdapter(getActivity(), rvPackageMessage, bean.get(0).getPackingList().get(0).getPackingData());

    }

    @Override
    public void onFail(String errorMsg) {
        dismissLoading();
        noResult(getString(R.string.tips_no_corresponding_receiving_shipping_information));
    }

    @Override
    public void onTopBarSelect(int position, final ReceiptDeliveryManifestBean bean) {
        tvSalesOrder.setText(TextUtils.isEmpty(bean.getSoOrderId()) ? "-" : String.format(getString(R.string.receipt_and_delivery_lis_order_id), bean.getSoOrderId()));
        tvCreationDate.setText(TextUtils.isEmpty(bean.getCreateTime()) ? "-" : String.format(getString(R.string.receipt_and_delivery_lis_creation_date), bean.getCreateTime()));
        tvWarehousingOperator.setText(TextUtils.isEmpty(bean.getIncomingOperator()) ? "-" : bean.getIncomingOperator());
        tvWarehousingOperationTime.setText(TextUtils.isEmpty(bean.getIncomingTime()) ? "-" : bean.getIncomingTime());
        tvInventoryStatus.setText(TextUtils.isEmpty(bean.getInStatus()) ? "-" : bean.getInStatus());
        tvOutboundOperator.setText(TextUtils.isEmpty(bean.getOutStorageOperator()) ? "-" : bean.getOutStorageOperator());
        tvOutboundOperationTime.setText(TextUtils.isEmpty(bean.getOutStorageTime()) ? "-" : bean.getOutStorageTime());
        tvDeliveryStatue.setText(TextUtils.isEmpty(bean.getOutStatus()) ? "-" : bean.getOutStatus());
        tvTotalNumberPackages.setText(TextUtils.isEmpty(bean.getTotalPackage()) ? "-" : bean.getTotalPackage());
        tvReceivedPackets.setText(TextUtils.isEmpty(bean.getReceivePackages()) ? "-" : bean.getReceivePackages());
        tvSendPackets.setText(TextUtils.isEmpty(bean.getSendPackages()) ? "-" : bean.getSendPackages());
        if(bean.getPackingList().size()>0){
            mPresenter.setPackageInfoAdapter(getActivity(), rvPackageMessage, bean.getPackingList().get(0).getPackingData());
        }

    }

    @Override
    public void onSerialSelect(int position, List<ReceiptDeliveryManifestBean.PackingListBean.PackingDataBean> bean) {

        mPresenter.setPackageInfoAdapter(getActivity(), rvPackageMessage, bean);

    }


}
