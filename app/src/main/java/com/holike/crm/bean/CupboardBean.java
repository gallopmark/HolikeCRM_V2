package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/27.
 * 橱柜报表
 */

public class CupboardBean implements Serializable {
    /**
     * dealerList : [{"countsComplete":"测试内容l175","month":"测试内容58bi"}]
     * describe : 测试内容x207
     * isDealer : 测试内容o3a7
     * isOrder : 测试内容0t6j
     * percentData : [{"area":"四大区","cityCode":"","countsComplete":"7","countsTarget":"30","countsTodayComplete":"0","isChange":0,"isClick":0,"name":"那崇奇","percentComplete":"22.1%","rank":"-","type":2},{"area":"销售一部","cityCode":"1000","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"舒文进","percentComplete":"","rank":"-","type":1},{"area":"销售二部","cityCode":"2000","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"周家红","percentComplete":"","rank":"-","type":1},{"area":"销售三部","cityCode":"3000","countsComplete":"2","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"章峰海","percentComplete":"","rank":"-","type":1},{"area":"销售四部","cityCode":"4000","countsComplete":"5","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"陈少华","percentComplete":"","rank":"-","type":1},{"area":"浙江","cityCode":"001","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"石小明","percentComplete":"","rank":"-","type":2},{"area":"湖南","cityCode":"002","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"周志华","percentComplete":"","rank":"-","type":2},{"area":"江西","cityCode":"003","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"张振有","percentComplete":"","rank":"-","type":2},{"area":"江苏","cityCode":"004","countsComplete":"2","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"杨俊杰","percentComplete":"","rank":"-","type":2},{"area":"东三省","cityCode":"005","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"孙华龙","percentComplete":"","rank":"-","type":2},{"area":"河北","cityCode":"006","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"刘谊","percentComplete":"","rank":"-","type":2},{"area":"鲁东","cityCode":"007","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"练有为","percentComplete":"","rank":"-","type":2},{"area":"鲁北","cityCode":"008","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"肖翔","percentComplete":"","rank":"-","type":2},{"area":"京津","cityCode":"009","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"张君","percentComplete":"","rank":"-","type":2},{"area":"安徽","cityCode":"010","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"石航","percentComplete":"","rank":"-","type":2},{"area":"云南","cityCode":"011","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"黄为明","percentComplete":"","rank":"-","type":2},{"area":"广西","cityCode":"012","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"叶文发","percentComplete":"","rank":"-","type":2},{"area":"四川","cityCode":"013","countsComplete":"3","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"上官显儒","percentComplete":"","rank":"-","type":2},{"area":"湖北","cityCode":"014","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"陈迎","percentComplete":"","rank":"-","type":2},{"area":"广东海南","cityCode":"015","countsComplete":"1","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"刘辉顺","percentComplete":"","rank":"-","type":2},{"area":"福建","cityCode":"016","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"胡成","percentComplete":"","rank":"-","type":2},{"area":"晋蒙","cityCode":"017","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"李东升","percentComplete":"","rank":"-","type":2},{"area":"陕宁","cityCode":"018","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"潘东升","percentComplete":"","rank":"-","type":2},{"area":"贵州","cityCode":"019","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"鲁喜平","percentComplete":"","rank":"-","type":2},{"area":"重庆","cityCode":"020","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"陶成汉","percentComplete":"","rank":"-","type":2},{"area":"豫南","cityCode":"021","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"李泽鸿","percentComplete":"","rank":"-","type":2},{"area":"豫北","cityCode":"022","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"杨志坤","percentComplete":"","rank":"-","type":2},{"area":"甘青新藏","cityCode":"023","countsComplete":"0","countsTarget":"","countsTodayComplete":"0","isChange":0,"isClick":1,"name":"张立仁","percentComplete":"","rank":"-","type":2}]
     * selectData : [{"name":"测试内容lmja","time":"测试内容s40s"}]
     * time : 五月
     * timeData : 2018年01月01日-2018年05月24日
     * total : 测试内容wo1u
     */

    private String describe;
    private String isDealer;
    private String isOrder;
    private String time;
    private String timeData;
    private String total;
    private List<OriginalBoardBean.DealerDataBean> dealerList;
    private List<PerformanceBean.PercentDataBean> percentData;
    private List<PerformanceBean.SelectDataBean> selectData;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(String isDealer) {
        this.isDealer = isDealer;
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<OriginalBoardBean.DealerDataBean> getDealerList() {
        return dealerList;
    }

    public void setDealerList(List<OriginalBoardBean.DealerDataBean> dealerList) {
        this.dealerList = dealerList;
    }

    public List<PerformanceBean.PercentDataBean> getPercentData() {
        return percentData;
    }

    public void setPercentData(List<PerformanceBean.PercentDataBean> percentData) {
        this.percentData = percentData;
    }

    public List<PerformanceBean.SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<PerformanceBean.SelectDataBean> selectData) {
        this.selectData = selectData;
    }

}
