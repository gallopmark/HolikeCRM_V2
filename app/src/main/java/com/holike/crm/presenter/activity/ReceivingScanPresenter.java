package com.holike.crm.presenter.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.model.activity.ReceivingScanModel;
import com.holike.crm.model.event.EventQRCodeScanResult;
import com.holike.crm.util.LogCat;
import com.holike.crm.view.activity.ReceivingScanView;

import java.util.List;

public class ReceivingScanPresenter extends BasePresenter<ReceivingScanView, ReceivingScanModel> {
    public static final int FAIL_REASON_REPEAT = 10;
    public static final int FAIL_REASON_REPEAT_INPUTTED = 20;
    private long lastTime;

    public void submit(List<EventQRCodeScanResult> results) {
        model.submitCodeInfo(results, new ReceivingScanModel.ReceivingScanListener() {
            @Override
            public void success(String result) {
                if (getView() != null)
                    getView().onSuccess(result);
            }

            @Override
            public void failed(String result) {
                if (getView() != null)
                    getView().onFail(result);
            }
        });
    }

    public void getCodeInfo(final String code, final List<EventQRCodeScanResult> results) {
        if (travers(results, code)) {//本地判断是否重复
            if (getView() != null)
                getView().onAddResultFail(FAIL_REASON_REPEAT_INPUTTED);
            return;
        }
        model.getCodeInfo(code, new ReceivingScanModel.ReceivingScanCodeListener() {
            @Override
            public void success(String messageBean) {
                results.add(new EventQRCodeScanResult(code));
                if (getView() != null)
                    getView().onAddResultSuccess(results, results.size() > 0);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().onFail(failed);
            }
        });
    }

    /**
     * 设置输入框监听
     *
     */
    public void setEditTextWatcher(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogCat.d("Editable:" + s + "count:" + count + "--lastTime:" + lastTime);
                count++;
                lastTime = System.currentTimeMillis();
                if (count > 0) {
                    if (System.currentTimeMillis() - lastTime < 200) {
                        lastTime = System.currentTimeMillis();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 监听文本框的内容长度
     *
     * @param et 要监听的文本框
     */
    public void textLengthListener(EditText et) {
        if (et == null) return;
        if (System.currentTimeMillis() - lastTime > 200 &&
                !et.getText().toString().isEmpty()) {
            lastTime = 0;
            LogCat.d("onScanGunSuccess");
            if (getView() != null)
                getView().onScanGunSuccess(et.getText().toString().trim());
        }
    }

    public void getResult(List<EventQRCodeScanResult> results) {
        if (getView() != null)
            getView().onAddResultSuccess(results, results.size() > 0);
    }


    /**
     * 遍历是否重复
     *
     */
    private boolean travers(List<EventQRCodeScanResult> results, String code) {
        for (EventQRCodeScanResult r : results) {
            if (r.getResult().equals(code)) {
                return true;
            }
        }
        return false;
    }

}
