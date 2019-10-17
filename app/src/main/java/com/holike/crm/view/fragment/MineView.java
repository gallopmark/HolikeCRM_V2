package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.bean.UserInfoBean;

/**
 * Created by wqj on 2018/2/24.
 * 我的
 */

public interface MineView extends BaseView {
    void getUserInfoSuccess(UserInfoBean userInfoBean);

    void getUserInfoFailed(String failed);

    void checkForUpdateSuccess(UpdateBean updateBean, boolean hasNewVersion);

    void checkForUpdateFailure(String failReason);
}
