package com.holike.crm.presenter.activity;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.CustomerBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.bean.UploadCallBackBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.model.activity.AddCustomerModel;
import com.holike.crm.model.activity.CustomerManageModel;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.activity.AddCustomerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 添加客户
 */

public class AddCustomerPresenter extends BasePresenter<AddCustomerView, AddCustomerModel> {
    private UploadImgHelper uploadImgHelper;

    @Override
    public void deAttach() {
        super.deAttach();
        cancel();
    }

    /**
     * 取消
     */
    public void cancel() {
        if (uploadImgHelper != null) {
            uploadImgHelper.cancel();
        }
    }

    /**
     * 上传图片
     */
    public void uploadImg(Context mContext, List<String> filePaths, UploadImgHelper.UploadImgListener uploadImgListener) {
        if (uploadImgHelper == null) {
            uploadImgHelper = new UploadImgHelper();
        }
        if (filePaths.size() > 0) {
            uploadImgHelper.upload(mContext,"", "", "", "03", "1", filePaths, uploadImgListener);
        }
    }

    /**
     * 获取客户选择条件数据
     */
    public void getTypeId() {
        new CustomerManageModel().getTypeId(new CustomerManageModel.GetTypeIdListener() {
            @Override
            public void success(TypeIdBean bean) {
                if (getView() != null)
                    getView().getTypeIdSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getTypeIdFailed(failed);
            }
        });
    }

    /**
     * 添加客户
     */
    public void addCustomer(Context mContext, final String area, final String associates, final String budget, final String buildingName,
                            final String buildingNumber, final String decorateProperties, final String decorateType,
                            final String antecedentPrice, final String favColorCode, final String favTextureCode,
                            final String gender, final String houseType, final String isEarnest, final String number,
                            final String phoneNumber, final String remark, final String shopId, final String source,
                            final String specialCustomers, final String userName, List<String> filePaths,
                            final String name, final String houseId, final String decorateDate, final String floorDate, final String decorateState, final String whetherFloor
            , final String spaceIds, final String fIds) {
        if (getView() != null)
            getView().loading();
        if (isEarnest.equals("1")) {
            uploadImg(mContext, filePaths, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean uploadCallBackBean) {
                    List<String> relationIds = new ArrayList<>();
                    for (Map.Entry<String, String> entry : uploadCallBackBean.getAdress().entrySet()) {
                        relationIds.add(entry.getKey());
                    }
                    CustomerBean bean = new CustomerBean(area, associates, budget, buildingName, buildingNumber,
                            decorateProperties, decorateType, antecedentPrice, favColorCode, favTextureCode, gender,
                            houseType, isEarnest, name, number, "", phoneNumber, remark, shopId, source,
                            specialCustomers, userName, houseId, decorateDate, floorDate, decorateState, whetherFloor, spaceIds, fIds, new HashMap<>());
                    model.addCustomer(bean, getRelationId(relationIds), new AddCustomerModel.EditCustomerListener() {
                        @Override
                        public void success(String success) {
                            if (getView() != null)
                                getView().addCustomerSuccess();
                        }

                        @Override
                        public void failed(String failed) {
                            if (getView() != null)
                                getView().addCustomerFailed(failed);
                        }
                    });
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().addCustomerFailed(failed);
                }
            });
        } else {
            CustomerBean bean = new CustomerBean(area, associates, budget, buildingName, buildingNumber,
                    decorateProperties, decorateType, antecedentPrice, favColorCode, favTextureCode, gender,
                    houseType, isEarnest, name, number, "", phoneNumber, remark, shopId, source, specialCustomers,
                    userName, houseId, decorateDate, floorDate, decorateState, whetherFloor, spaceIds, fIds, new HashMap<>());
            model.addCustomer(bean, "", new AddCustomerModel.EditCustomerListener() {
                @Override
                public void success(String success) {
                    if (getView() != null)
                        getView().addCustomerSuccess();
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().addCustomerFailed(failed);
                }
            });
        }
    }

    /**
     * relationIds数组转化string
     */
    public static String getRelationId(List<String> relationIds) {
        String relationId = "";
        if (relationIds != null && relationIds.size() > 0) {
            for (String url : relationIds) {
                relationId = relationId.equals("") ? url : relationId + "@" + url;
            }
        }
        return relationId;
    }

    /**
     * 获取所属人
     */
    public void getAssociateData(TypeIdBean.TypeIdItem shop) {
        if (shop == null) {
            if (getView() != null)
                getView().getAssociateFailed("请先选择门店！");
        } else {
            if (getView() != null)
                getView().loading();
            model.getAssociateData(shop.getId(), new AddCustomerModel.GetAssociateListener() {
                @Override
                public void success(AssociateBean bean) {
                    if (getView() != null)
                        getView().getAssociateSuccess(bean.getGuide());
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().getAssociateFailed(failed);
                }
            });
        }
    }

    /**
     * 获取类型id
     */
    public static List<TypeIdBean.TypeIdItem> getTypeIdItems(Map<String, String> datas) {
        List<TypeIdBean.TypeIdItem> typeIdItems = new ArrayList<>();
        try {
            for (Map.Entry<String, String> entry : datas.entrySet()) {
                typeIdItems.add(new TypeIdBean.TypeIdItem(entry.getKey(), entry.getValue()));
            }
            Collections.sort(typeIdItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeIdItems;
    }

    /**
     * 获取选择器现在位置
     */
    public static int getSelectOption(List<? extends TypeIdBean.TypeIdItem> dates, String select) {
        if (!TextUtils.isEmpty(select)) {
            for (int i = 0, size = dates.size(); i < size; i++) {
                if (select.equals(dates.get(i).getPickerViewText())) {
                    return i;
                }
            }
        }
        return 0;
    }

    private TimePickerView pvTime;

    /**
     * 选择时间
     */
    public void showTimePickerView(Context context, String time, final TextView view) {
        if (view != null) {
            KeyBoardUtil.hideKeyboard(view);
        }
        if (pvTime == null) {
            pvTime = new TimePickerBuilder(context, (date, v) -> {
                if (getView() != null) {
                    getView().selectTime(date);
                }
            }).setType(new boolean[]{true, true, true, false, false, false}).build();
        }
        if (!TextUtils.isEmpty(time)) {
            pvTime.setDate(TimeUtil.stringToCalendar(time, "yyyy.MM.dd"));
        } else {
            pvTime.setDate(Calendar.getInstance());
        }
        pvTime.show();
    }


}
