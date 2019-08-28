package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/8/28.
 * 收款记录bean
 */

public class CollectionBean implements Serializable {
    /**
     * balance : 0
     * depoist : {"depositAmount":"测试内容c493"}
     * money : 0
     * payment : [{"depositAmount":"测试内容pay4"}]
     */

    private int balance;
    private PaymentBean depoist;
    private int money;
    private List<PaymentBean> payment;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public PaymentBean getDepoist() {
        return depoist;
    }

    public void setDepoist(PaymentBean depoist) {
        this.depoist = depoist;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<PaymentBean> getPayment() {
        return payment;
    }

    public void setPayment(List<PaymentBean> payment) {
        this.payment = payment;
    }


    public static class PaymentBean implements Serializable {
        /**
         * depositAmount : 测试内容pay4
         */

        private String depositAmount;
        private String operateTime;

        public String getDepositAmount() {
            return depositAmount;
        }

        public void setDepositAmount(String depositAmount) {
            this.depositAmount = depositAmount;
        }

        public String getOperateTime() {
            return operateTime;
        }

        public void setOperateTime(String operateTime) {
            this.operateTime = operateTime;
        }
    }
}
