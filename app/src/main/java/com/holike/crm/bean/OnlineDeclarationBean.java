package com.holike.crm.bean;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

@Parcel
public class OnlineDeclarationBean implements Serializable {


    /**
     * create_date : 2018-12-27
     * credit_amount : 11110
     * dealer_name : 测试内容r0ek
     * dealer_no : 1
     * id : 0ae02a6cabe8454e9512112e19b49f73
     * imageList : [{"imageId":"测试内容vf8u","imageUrl":"测试内容4fb3"}]
     * recip_acc_no : 0000000000000
     * recip_bk_name : 0000000000000000
     * recip_bk_no : 测试内容83ho
     * recip_name : 测试内容2rup
     * relationId : 测试内容t6ex
     * status_code : 01
     * status_name : 未提交
     * transaction_date : 2018-12-27
     */

    String create_date;
    String credit_amount;
    String dealer_name;
    String dealer_no;
    String id;
    String recip_acc_no;
    String recip_bk_name;
    String recip_bk_no;
    String recip_name;
    String relationId;
    String status_code;
    String status_name;
    String transaction_date;
    List<ImageListBean> imageList;

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCredit_amount() {
        return credit_amount;
    }

    public void setCredit_amount(String credit_amount) {
        this.credit_amount = credit_amount;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getDealer_no() {
        return dealer_no;
    }

    public void setDealer_no(String dealer_no) {
        this.dealer_no = dealer_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecip_acc_no() {
        return recip_acc_no;
    }

    public void setRecip_acc_no(String recip_acc_no) {
        this.recip_acc_no = recip_acc_no;
    }

    public String getRecip_bk_name() {
        return recip_bk_name;
    }

    public void setRecip_bk_name(String recip_bk_name) {
        this.recip_bk_name = recip_bk_name;
    }

    public String getRecip_bk_no() {
        return recip_bk_no;
    }

    public void setRecip_bk_no(String recip_bk_no) {
        this.recip_bk_no = recip_bk_no;
    }

    public String getRecip_name() {
        return recip_name;
    }

    public void setRecip_name(String recip_name) {
        this.recip_name = recip_name;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public List<ImageListBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageListBean> imageList) {
        this.imageList = imageList;
    }

    @Parcel
    public static class ImageListBean implements Serializable{
        /**
         * imageId : 测试内容vf8u
         * imageUrl : 测试内容4fb3
         */

        String imageId;
        String imageUrl;

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
