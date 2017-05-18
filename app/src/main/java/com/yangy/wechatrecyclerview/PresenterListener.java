package com.yangy.wechatrecyclerview;

import com.yangy.wechatrecyclerview.bean.CommentConfig;
import com.yangy.wechatrecyclerview.bean.User;

/**
 * 点击事件回调接口
 * Created by yangy on 2017/05/12
 */

public interface PresenterListener {
    void onLinkClick(int position);//链接

    void updateAddFabulous(int position, User userItem);//点赞

    void updateDeleteFabulous(int position, String id);//取消赞

    void deleteComment(int circlePosition, int commentPosition, String commentId);//删除评论

    void showEditTextBody(int visibility, CommentConfig commentConfig);//显示评论输入框,回复别人的评论

    void addComment(String content, CommentConfig config);//增加评论

    void closeInputBox();//关闭输入框
}
