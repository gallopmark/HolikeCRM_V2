package com.holike.crm.helper;

import com.holike.crm.bean.CustomerStateListBean;
import com.holike.crm.bean.NewItemBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.manager.ObserverManager;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewItemHelper {
    public NewItemHelper() {
    }

    public static NewItemHelper getInstance() {
        return NewItemHelperHolder.instance;
    }

    private static class NewItemHelperHolder {
        private static NewItemHelper instance = new NewItemHelper();
    }

    public void getNewItem(final CustomerStateListBean bean, String stateName, final NewItemListener listener) {
        if (stateName.equals("待下单客户") ||
                stateName.equals("已流失客户")) {
            if (listener == null) return;
            listener.onSuccess(bean);
            return;
        }
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        params.put("houseId", params(bean));
        params.put("type", switchStateName(stateName));
        MyHttpClient.postByCliId(UrlPath.URL_NEW_ITEM, header, params, new RequestCallBack<List<NewItemBean>>() {
            @Override
            public void onFailed(String failReason) {
                if (listener == null) return;
                listener.onFail(failReason);
            }

            @Override
            public void onSuccess(final List<NewItemBean> result) {
                ObserverManager.getInstance().doTask(new ObserverManager.CallBack() {
                    @Override
                    public void task() {
                        bean.setDate(shortData(bean, result));
                    }

                    @Override
                    public void onComplete() {
                        if (listener == null) return;
                        listener.onSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFail(t.getMessage());
                    }
                });
            }
        });
    }

    /**
     * 旧数据重新按阿桂的排序排序
     *
     * @param customerStateListBean
     * @param newItemBeans
     * @return
     */
    private List<CustomerStateListBean.DateBean> shortData(CustomerStateListBean customerStateListBean, List<NewItemBean> newItemBeans) {
        List<CustomerStateListBean.DateBean> oldDatas = new ArrayList<>(0);
        for (int i = 0; i < newItemBeans.size(); i++) {
            for (CustomerStateListBean.DateBean oldData : customerStateListBean.getDate()) {
                if (oldData.getHouseId().equals(newItemBeans.get(i).getHouseId())) {
                    oldData.setDefiniteTime(newItemBeans.get(i).getDefiniteTime());
                    oldData.setHouseId(newItemBeans.get(i).getHouseId());
                    oldData.setInstall(newItemBeans.get(i).getInstall());
                    oldData.setNextTime(newItemBeans.get(i).getNextTime());
                    oldData.setName(newItemBeans.get(i).getName());
                    oldData.setMeasureTime(newItemBeans.get(i).getMeasureTime());
                    oldData.setIsRed(newItemBeans.get(i).getIsRed());
                    oldDatas.add(oldData);
                }
            }
        }

        return oldDatas;
    }


    private String switchStateName(String stateName) {
        String type = null;
        switch (stateName) {
            case "意向客户跟进":
                type = "1";
                break;
            case "待量房客户":
                type = "2";
                break;
            case "待出图客户":
                type = "3";
                break;
            case "待签约客户":
                type = "4";
                break;
            case "已安装客户":
                type = "5";
                break;
            case "待安装客户":
                type = "6";
                break;
            case "待复尺客户":
                type = "7";
                break;
            case "待收全款客户":
                type = "8";
                break;
        }
        return type;
    }

    private String params(CustomerStateListBean beans) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (CustomerStateListBean.DateBean bean : beans.getDate()) {
            sb.append(i == 0 || i == beans.getSize() ? bean.getHouseId() : "@" + bean.getHouseId());
            i++;
        }
        return sb.toString();
    }

    public interface NewItemListener {
        void onSuccess(CustomerStateListBean data);

        void onFail(String result);
    }
}
