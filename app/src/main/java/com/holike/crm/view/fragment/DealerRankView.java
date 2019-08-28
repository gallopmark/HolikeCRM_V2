package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.DealerRankBean;

/**
 * Created by wqj on 2018/5/28.
 * 经销商排行
 */

public interface DealerRankView extends BaseView {

    void getData(String cityCode);

    void enterRank(DealerRankBean bean);

    void getDataFailed(String failed);

    void enterPersonal(DealerRankBean bean);
}
