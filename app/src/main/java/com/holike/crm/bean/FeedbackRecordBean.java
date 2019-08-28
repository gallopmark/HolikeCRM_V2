package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/7/17.
 * 反馈记录
 */

public class FeedbackRecordBean implements Serializable {
    /**
     * answer : s
     * create_time : 2018-07-13 10:27:13
     * expenseName : 严重
     * itemNameList : ["太小","太大"]
     * param1 : 123
     * questionDescribe : 问题描述
     * questionImg : https://file.holike.com/929461a5-72ea-4fbd-8085-973c34f0a9db.jpg@https://file.holike.com/1aa19351-88ce-479a-9fd6-42444e8366db.jpg@https://file.holike.com/301f1b8c-f63b-4796-ad80-9cbc6a9241fe.jpg@https://file.holike.com/e6be1812-cdb3-4b94-bfb1-85763946a843.jpg@https://file.holike.com/1aa19351-88ce-479a-9fd6-42444e8366db.jpg@https://file.holike.com/301f1b8c-f63b-4796-ad80-9cbc6a9241fe.jpg@https://file.holike.com/e6be1812-cdb3-4b94-bfb1-85763946a843.jpg@
     * questionName : 尺寸不对
     * questionPicture : ["https://file.holike.com/929461a5-72ea-4fbd-8085-973c34f0a9db.jpg","https://file.holike.com/1aa19351-88ce-479a-9fd6-42444e8366db.jpg","https://file.holike.com/301f1b8c-f63b-4796-ad80-9cbc6a9241fe.jpg","https://file.holike.com/e6be1812-cdb3-4b94-bfb1-85763946a843.jpg","https://file.holike.com/1aa19351-88ce-479a-9fd6-42444e8366db.jpg","https://file.holike.com/301f1b8c-f63b-4796-ad80-9cbc6a9241fe.jpg","https://file.holike.com/e6be1812-cdb3-4b94-bfb1-85763946a843.jpg"]
     * solveDescribe : sdd
     * solvePicture : ["https://file.holike.com/929461a5-72ea-4fbd-8085-973c34f0a9db.jpg","https://file.holike.com/1aa19351-88ce-479a-9fd6-42444e8366db.jpg","https://file.holike.com/301f1b8c-f63b-4796-ad80-9cbc6a9241fe.jpg","https://file.holike.com/e6be1812-cdb3-4b94-bfb1-85763946a843.jpg","https://file.holike.com/1aa19351-88ce-479a-9fd6-42444e8366db.jpg","https://file.holike.com/301f1b8c-f63b-4796-ad80-9cbc6a9241fe.jpg","https://file.holike.com/e6be1812-cdb3-4b94-bfb1-85763946a843.jpg"]
     */

    private String answer;
    private String create_time;
    private String expenseName;
    private String param1;
    private String questionDescribe;
    private String questionImg;
    private String questionName;
    private String solveDescribe;
    private List<String> itemNameList;
    private List<String> questionPicture;
    private List<String> solvePicture;
    private boolean isShow;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getQuestionDescribe() {
        return questionDescribe;
    }

    public void setQuestionDescribe(String questionDescribe) {
        this.questionDescribe = questionDescribe;
    }

    public String getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getSolveDescribe() {
        return solveDescribe;
    }

    public void setSolveDescribe(String solveDescribe) {
        this.solveDescribe = solveDescribe;
    }

    public List<String> getItemNameList() {
        return itemNameList;
    }

    public void setItemNameList(List<String> itemNameList) {
        this.itemNameList = itemNameList;
    }

    public List<String> getQuestionPicture() {
        return questionPicture;
    }

    public void setQuestionPicture(List<String> questionPicture) {
        this.questionPicture = questionPicture;
    }

    public List<String> getSolvePicture() {
        return solvePicture;
    }

    public void setSolvePicture(List<String> solvePicture) {
        this.solvePicture = solvePicture;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
