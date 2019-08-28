package com.holike.crm.fragment.message;

import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MessageBean;
import com.holike.crm.presenter.fragment.MessagePresenter;
import com.holike.crm.view.fragment.MessageView;

/**
 * Created by wqj on 2018/2/24.
 * 消息
 */

public class MessageFragment extends MyFragment<MessagePresenter, MessageView> implements MessageView {
//    protected final int LOAD_TYPE_REFRESH = 3;
//    protected final int LOAD_TYPE_LOADMORE = 4;

    protected int pageNo = 1;
    //    protected int loadType = LOAD_TYPE_REFRESH;
    protected int loadType = 0;
    protected MessageBean messageBean;

    @Override
    protected int setContentViewId() {
        return 0;
    }

    @Override
    protected MessagePresenter attachPresenter() {
        return new MessagePresenter();
    }

    @Override
    protected void init() {
    }

    @Override
    public void getNotifySuccess(MessageBean messageBean) {
    }

    @Override
    public void getNotifyFailed(String failed) {
    }

    @Override
    public void getAnnouncementSuccess(MessageBean messageBean) {
    }

    @Override
    public void getAnnouncementFailed(String failed) {
    }

    @Override
    public void openMessage(MessageBean.MessageListBean messageListBean) {
    }

    protected boolean isContainList(MessageBean messageBean) {
        return messageBean.getMessageList() != null && !messageBean.getMessageList().isEmpty();
    }

    protected void onLoadFailed(String failed) {
        if (isNoAuth(failed)) {
            mPresenter.clearData();
            noAuthority();
        } else {
            if (loadType == 0) {
                noNetwork();
            } else {
                showShortToast(failed);
            }
        }
    }
}
