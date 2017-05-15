package com.yangy.wechatrecyclerview;

import com.yangy.wechatrecyclerview.bean.User;

/**
 * Created by yangy on 2017/05/12 17:35
 */

public interface PresenterListener {
    void onLinkClick(int position);//链接
    void updateAddFabulous(int position,User userItem);//点赞
    void updateDeleteFabulous(int position,String id);//取消赞
}
