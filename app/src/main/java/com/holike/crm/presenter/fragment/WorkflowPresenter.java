package com.holike.crm.presenter.fragment;

import android.content.Context;
import android.text.TextUtils;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.CollectionBean;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.bean.DivideGuideBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.bean.UploadCallBackBean;
import com.holike.crm.helper.UploadImgHelper;
import com.holike.crm.model.activity.AddCustomerModel;
import com.holike.crm.model.activity.CustomerManageModel;
import com.holike.crm.model.fragment.WorkflowModel;
import com.holike.crm.presenter.activity.AddCustomerPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;
import com.holike.crm.view.fragment.WorkflowView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/8/2.
 * 所有流程presenter
 */

public class WorkflowPresenter extends BasePresenter<WorkflowView, WorkflowModel> {
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
    public void uploadImg(Context context, String customerId, String optCode, String houseId, String cusomerStatus, List<String> filePaths, UploadImgHelper.UploadImgListener uploadImgListener) {
        if (uploadImgHelper == null) {
            uploadImgHelper = new UploadImgHelper();
        }
        if (filePaths.size() > 0) {
            uploadImgHelper.upload(context, customerId, optCode, houseId, cusomerStatus, "1", filePaths, uploadImgListener);
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
                    getView().success(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 获取所属人
     */
    public void getAssociate(String shopId) {
        new AddCustomerModel().getAssociateData(shopId, new AddCustomerModel.GetAssociateListener() {
            @Override
            public void success(AssociateBean bean) {
                if (getView() != null)
                    getView().success(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 获取分配导购
     */
    public void getDivideGuide(String houseId) {
        model.getDivideGuide(houseId, new WorkflowModel.GetDivideGuideListener() {
            @Override
            public void success(DivideGuideBean bean) {
                if (getView() != null)
                    getView().success(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 获取分配设计师
     */
    public void getDivideDesigner(String houseId) {
        model.getDivideDesigner(houseId, new WorkflowModel.GetDivideDesignerListener() {
            @Override
            public void success(DivideGuideBean bean) {
                if (getView() != null)
                    getView().success(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 获取收款记录
     */
    public void getCollection(String houseId, String personalId) {
        model.getCollection(houseId, personalId, new WorkflowModel.GetCollectionListener() {
            @Override
            public void success(CollectionBean collectionBean) {
                if (getView() != null)
                    getView().success(collectionBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 分配导购
     */
    public void divideGuide(String content, String customerStatus, String houseId, String id, String operateCode, String personalId, String prepositionRuleStatus, String type) {
        model.divideGuide(TextUtils.isEmpty(content) ? "" : content, customerStatus, houseId, id == null ? "" : id, operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 分配设计师
     */
    public void divideDesigner(String customerStatus, String houseId, String operateCode, String personalId, String prepositionRuleStatus, String type) {
        model.divideDesigner(customerStatus, houseId, operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 添加沟通记录
     */
    public void addRecord(String content, String customerStatus, String houseId, String nextFollowUpDate, String operateCode, String personalId, String prepositionRuleStatus, String type) {
        model.addRecord(content, customerStatus, houseId, TextUtils.isEmpty(nextFollowUpDate) ? "" : TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 预约量房
     */
    public void bookingMeasure(String content, String customerStatus, String houseId, String id, String nextFollowUpDate, String operateCode, String personalId, String prepositionRuleStatus, String type) {
        model.bookingMeasure(content, customerStatus, houseId, id, TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 上传量房结果
     */
    public void uploadMeasureResult(Context context, final String content, final String customerStatus, final String data, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String personalId, final String prepositionRuleStatus, List<String> imgPaths) {
        if (imgPaths == null || imgPaths.size() == 0) {
            uploadMeasureResult(content, customerStatus, data, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus);
        } else {
            uploadImg(context, personalId, operateCode, houseId, customerStatus, imgPaths, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean bean) {
                    uploadMeasureResult(content, customerStatus, data, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    public void uploadMeasureResult(final String content, final String customerStatus, final String data, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String personalId, final String prepositionRuleStatus) {
        model.uploadMeasureResult(TextUtils.isEmpty(content) ? "" : content, customerStatus, TextUtils.isEmpty(data) ? "" : TimeUtil.stringToStamp(data, "yyyy.MM.dd"), houseId, id, TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 收取订金
     */
    public void collectDeposit(Context context, final String content, final String customerStatus, final String houseId,
                               final String id,
                               final String nextFollowUpDate, final String operateCode,
                               final String personalId, final String prepositionRuleStatus,
                               final String type, List<String> imgPaths, final String earnestType) {
        if (imgPaths == null || imgPaths.size() == 0) {
            collectDeposit(content, customerStatus, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus, type, earnestType);
        } else {
            uploadImg(context, personalId, operateCode, houseId, customerStatus, imgPaths, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean bean) {
                    collectDeposit(content, customerStatus, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus, type, earnestType);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    public void collectDeposit(final String content, final String customerStatus, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String personalId, final String prepositionRuleStatus, final String type, final String earnestType) {
        model.collectDeposit(content,
                customerStatus,
                houseId,
                id,
                TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"),
                operateCode,
                personalId,
                TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus,
                type,
                earnestType,

                new WorkflowModel.WorkflowListener() {
                    @Override
                    public void success(String success) {
                        if (getView() != null)
                            getView().success(success);
                    }

                    @Override
                    public void failed(String failed) {
                        if (getView() != null)
                            getView().failed(failed);
                    }
                });
    }

    /**
     * 店长查房
     */
    public void shoperCheck(String content, String customerStatus, String houseId, String pass, String operateCode, String personalId, String prepositionRuleStatus) {
        model.shoperCheck(content, customerStatus, houseId, operateCode, pass, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 上传方案
     */
    public void uploadPlan(Context context, final String content, final String customerStatus, final String date, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String personalId, final String prepositionRuleStatus, List<String> imgPaths) {
        if (imgPaths == null || imgPaths.size() == 0) {
            uploadPlan(content, customerStatus, date, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus);
        } else {
            uploadImg(context, personalId, operateCode, houseId, customerStatus, imgPaths, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean bean) {
                    uploadPlan(content, customerStatus, date, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    public void uploadPlan(final String content, final String customerStatus, final String date, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String personalId, final String prepositionRuleStatus) {
        model.uploadPlan(content, customerStatus, TextUtils.isEmpty(date) ? "" : TimeUtil.stringToStamp(date, "yyyy.MM.dd"), houseId, id, TextUtils.isEmpty(nextFollowUpDate) ? "" : TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 签约
     */
    public void signed(Context context, final String content, final String customerStatus, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String pass, final String personalId, final String prepositionRuleStatus, final String type, final String depositAmount, final String lastMoney, List<String> imgPaths) {
        if (imgPaths == null || imgPaths.size() == 0) {
            signed(content, customerStatus, houseId, id, nextFollowUpDate, operateCode, pass, personalId, prepositionRuleStatus, type, depositAmount, lastMoney);
        } else {
            uploadImg(context, personalId, operateCode, houseId, customerStatus, imgPaths, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean bean) {
                    signed(content, customerStatus, houseId, id, nextFollowUpDate, operateCode, pass, personalId, prepositionRuleStatus, type, depositAmount, lastMoney);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    public void signed(final String content, final String customerStatus, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, String pass, final String personalId, final String prepositionRuleStatus, final String type, String depositAmount, String lastMoney) {
        model.signed(content, customerStatus, houseId, id, TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, pass, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, depositAmount.replace("已收订金：￥", ""), lastMoney.replace("￥", ""), new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 上传复尺结果
     */
    public void upLoadRemeasure(Context context, final String id, final String content, final String customerStatus, final String houseId, final String nextFollowUpDate, final String operateCode, final String pass, final String personalId, final String prepositionRuleStatus, List<String> imgPaths) {
        if (imgPaths == null || imgPaths.size() == 0) {
            upLoadRemeasure(id, content, customerStatus, houseId, nextFollowUpDate, operateCode, pass, personalId, prepositionRuleStatus);
        } else {
            uploadImg(context, personalId, operateCode, houseId, customerStatus, imgPaths, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean bean) {
                    upLoadRemeasure(id, content, customerStatus, houseId, nextFollowUpDate, operateCode, pass, personalId, prepositionRuleStatus);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    public void upLoadRemeasure(String id, final String content, final String customerStatus, final String houseId, final String nextFollowUpDate, final String operateCode, final String pass, final String personalId, final String prepositionRuleStatus) {
        model.upLoadRemeasure(id, content, customerStatus, houseId, TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, pass, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 预约安装
     */
    public void bookingInstall(String content, String customerStatus, String houseId, String id, String nextFollowUpDate, String operateCode, String personalId, String prepositionRuleStatus, String type) {
        model.bookingInstall(content, customerStatus, houseId, id, TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 安装完成
     */
    public void installed(Context mContext, final String content, final String customerStatus, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String personalId, final String prepositionRuleStatus, List<String> imgs) {
        if (imgs == null || imgs.size() == 0) {
            installed(content, customerStatus, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus);
        } else {
            uploadImg(mContext, personalId, operateCode, houseId, customerStatus, imgs, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean bean) {
                    installed(content, customerStatus, houseId, id, nextFollowUpDate, operateCode, personalId, prepositionRuleStatus);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    public void installed(final String content, final String customerStatus, final String houseId, final String id, final String nextFollowUpDate, final String operateCode, final String personalId, final String prepositionRuleStatus) {
        model.installed(content, customerStatus, houseId, id, TimeUtil.stringToStamp(nextFollowUpDate, "yyyy.MM.dd"), operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 收尾款
     */
    public void collectMoney(Context context, final String content, final String customerStatus, final String houseId, final String lastMoney, final String operateCode, final String personalId, final String prepositionRuleStatus, final String type, List<String> imgPaths) {
        if (imgPaths == null || imgPaths.size() == 0) {
            collectMoney(content, customerStatus, houseId, "", lastMoney.replace("￥", ""), operateCode, personalId, prepositionRuleStatus, type);
        } else {
            uploadImg(context, personalId, operateCode, houseId, customerStatus, imgPaths, new UploadImgHelper.UploadImgListener() {
                @Override
                public void success(UploadCallBackBean bean) {
                    List<String> relationIds = new ArrayList<>();
                    for (Map.Entry<String, String> entry : bean.getAdress().entrySet()) {
                        relationIds.add(entry.getKey());
                    }
                    String url = AddCustomerPresenter.getRelationId(relationIds);
                    collectMoney(content, customerStatus, houseId, url, lastMoney.replace("￥", ""), operateCode, personalId, prepositionRuleStatus, type);
                }

                @Override
                public void failed(String failed) {
                    if (getView() != null)
                        getView().failed(failed);
                }
            });
        }
    }

    public void collectMoney(final String content, final String customerStatus, final String houseId, String id, String lastMoney, final String operateCode, final String personalId, final String prepositionRuleStatus, final String type) {
        model.collectMoney(content, customerStatus, houseId, id, lastMoney, operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 已流失
     */
    public void lossed(String content, String customerStatus, String houseId, String id, String operateCode, String personalId, String prepositionRuleStatus, String type) {
        model.lossed(content, customerStatus, houseId, id, operateCode, personalId, TextUtils.isEmpty(prepositionRuleStatus) ? "" : prepositionRuleStatus, type, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 编辑客户
     */
    public void editCustomer(String associates, String gender, String personalId, String phoneNumber, String shopId, String source, String specialCustomers, String userName) {
        CustomerDetailBean.PersonalBean personalBean = new CustomerDetailBean.PersonalBean(associates, gender, shopId, specialCustomers, personalId, phoneNumber, source, userName);
        model.editCustimer(personalBean, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success("操作成功");
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 添加/编辑房屋
     */
    public void editHouse(String area, String budget, String buildingName, String buildingNumber,
                          String decorateType, String favColorCode, String favTextureCode,
                          String decorateProperties, String houseType, String remark, String houseId,
                          String name, String number, String personalId, String checkbulidingCode, String customizeTheSpace, String decorationProgress,
                          String furnitureDemand, String plannedBaseDecorateDate,
                          String plannedHouseDeliveryDate, String shopId, String salesId) {
        CustomerDetailBean.CustomerDetailInfoListBean.ListHouseInfoBean houseInfoBean
                = new CustomerDetailBean.CustomerDetailInfoListBean.
                ListHouseInfoBean(area, budget, buildingName, buildingNumber, decorateType,
                favColorCode, favTextureCode, decorateProperties, houseType, remark, houseId, checkbulidingCode,
                customizeTheSpace, decorationProgress, furnitureDemand, plannedBaseDecorateDate
                , plannedHouseDeliveryDate, shopId, salesId, "", "", "");
        model.editHouse(houseInfoBean, name == null ? "" : name, number, personalId, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 撤销
     */
    public void revoke(String cancelReason, String customerStatus, String houseId, String optCode) {
        model.revoke(cancelReason, customerStatus, houseId, optCode, new WorkflowModel.WorkflowListener() {
            @Override
            public void success(String success) {
                if (getView() != null)
                    getView().success(success);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().failed(failed);
            }
        });
    }

    /**
     * 获取当前人员
     */
    public AssociateBean.ProfessionBean getCurrentProfession(List<AssociateBean.ProfessionBean> professionBeans) {
        String userId = SharedPreferencesUtils.getString(Constants.USER_ID, "");
        for (AssociateBean.ProfessionBean bean : professionBeans) {
            if (userId.equals(bean.getUserId())) {
                return bean;
            }
        }
        return null;
    }



}
