package com.holike.crm.presenter.fragment;


import com.holike.crm.base.BasePresenter;
import com.holike.crm.model.event.EventQRCodeScanResult;
import com.holike.crm.model.fragment.ScanByPhoneModel;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.ScanByPhoneView;

import java.util.List;

public class ScanByPhonePresenter extends BasePresenter<ScanByPhoneView, ScanByPhoneModel> {
//    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    if (getView() != null)
//                        getView().resumeCamera();
//                    break;
//                case 1:
//                    if (getView() != null)
//                        getView().onDelayDone();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    @Override
    protected void onDestroy() {
//        handler.removeMessages(0);
        super.onDestroy();
    }

    public void delayEvent(long ms) {
//        handler.sendEmptyMessageDelayed(1, ms);
    }

//    private long startTime = 0;
//    private long endTime = 0;

    public void getCodeInfo(final String code, final List<EventQRCodeScanResult> results) {
        if (travers(results, code)) {//本地判断是否重复
//            rescan(code, false);
            if (getView() != null)
                getView().onFail(Constants.SCAN_REPEAT);
            return;
        }
//        startTime = System.currentTimeMillis();
        model.getCodeInfo(code, new ScanByPhoneModel.ScanByPhoneListener() {
            @Override
            public void success(String messageBean) {
//                long useTime = System.currentTimeMillis() - startTime;
//                rescan(code, useTime > 1000);
                results.add(new EventQRCodeScanResult(code));

                if (getView() != null)
                    getView().onSuccess(code, results);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().onFail(failed);
//                endTime = System.currentTimeMillis();
//                long useTime = endTime - startTime;
//                rescan(code, useTime > 1500);
            }
        });
    }

    /**
     * 遍历是否重复
     *
     * @param results
     * @param code
     * @return
     */
    private boolean travers(List<EventQRCodeScanResult> results, String code) {
        for (EventQRCodeScanResult r : results) {
            if (r.getResult().equals(code)) {
                return true;
            }
        }
        return false;
    }
//
//    private void rescan(String result, boolean isLongTime) {
//        Message message = Message.obtain();
//        message.what = 0;
//        message.obj = result;
//        if (isLongTime) {
//            handler.sendMessage(message);
//        } else {
//            handler.sendMessageDelayed(message, 1500);
//        }
//
//    }
}
