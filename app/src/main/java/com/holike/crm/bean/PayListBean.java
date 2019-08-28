package com.holike.crm.bean;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class PayListBean implements Serializable,MultiItem {

    /**
     * dealer_name : 测试内容rp81
     * dealer_no : 测试内容2nq5
     * imageList : [{"imageId":"测试内容c7el","imageUrl":"测试内容9aho"}]
     * recip_bk_no : 1
     * recip_name : 1
     * relationId : 测试内容3756
     * create_date : 2018-12-26
     * credit_amount : 23000
     * id : 65003192632c447eba67c39782072fa4
     * recip_acc_no : 6214130003002241638
     * recip_bk_name :
     * status_code : 01
     * status_name : 未提交
     * transaction_date : 2018-12-26
     */
    String category;
    String dealer_name;
    String create_date;
    String dealer_no;
    String recip_bk_no;
    String recip_name;
    String relationId;
    String credit_amount;
    String id;
    String recip_acc_no;
    String recip_bk_name;
    String status_code;
    String status_name;
    String transaction_date;
    List<ImageListBean> imageList = new ArrayList<>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    @Override
    public int getItemType() {
        return 1;
    }

    @Parcel
    public static class ImageListBean implements Serializable {


        /**
         * imageId : 测试内容c7el
         * imageUrl : 测试内容9aho
         */


        String imageId;
        String imageUrl;

        @ParcelConstructor
        public ImageListBean(String imageId, String imageUrl) {
            this.imageId = imageId;
            this.imageUrl = imageUrl;
        }

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
