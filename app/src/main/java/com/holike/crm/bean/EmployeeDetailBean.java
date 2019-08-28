package com.holike.crm.bean;

import java.util.List;

/*
 *    authInfo|14	权限信息	array<object>
 *         pArr2		array<object>
 *             actionId	动作id	string	@mock=$order('8f4cd2886ae34661bd2e4634d4899814','d98f342f34ba48ccb072e6abd5dbfb4d')
 *             actionName	动作名称	string	@mock=$order('分配门店','查看详情')
 *             isSelect	是否被选中	string	@mock=$order('0','0')
 *         pName	权限名称	string	@mock=$order('网络资源客户','木门商城下单','成品配套商城','安装服务：安装师配置','安装服务：安装师任务','通知公告','可查看零售价','可查看出厂价','合作伙伴：我的客户','经销商订单','经销商售后订单','可查看非本人创建的客户','经销商打款(经销商)','系统安全：经销商用户管理')
 *     shopInfo|4	店铺信息	array<object>
 *         isSelect	是否别选中	string	@mock=$order('1','0','0','0')
 *         shopId	店铺id	string	@mock=$order('20701678','20701679','20701680','20702110')
 *         shopName	店铺名称	string	@mock=$order('南宁市富安居店','南宁市快环综合建材精品馆','南宁市富安居二店','南宁市万达茂靓家居')
 *         status	状态	string	@mock=$order('运营中','运营中','运营中','运营中')
 */
public class EmployeeDetailBean {

    private int isBoss = 0; //1是经销商老板 0不是
    private EmployeeBean userinfo;
    private List<DistributionStoreBean> shopInfo;
    private List<RoleDataBean.AuthInfoBean> authInfo;

    public boolean isBoss() {
        return isBoss == 1;
    }

    public EmployeeBean getUserInfo() {
        return userinfo;
    }

    public List<DistributionStoreBean> getShopInfo() {
        return shopInfo;
    }

    public List<RoleDataBean.AuthInfoBean> getAuthInfo() {
        return authInfo;
    }
}
