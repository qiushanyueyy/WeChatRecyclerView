package com.yangy.wechatrecyclerview.bean;

import java.util.List;

/**
 * 动态实体类
 * Created by yangy on 2017/05/12
 */
public class CircleOfFriendsBean {

    private String content;//动态内容
    private User user;//人员信息
    private LinkBean linkBean;//链接详细信息
    private List<ImageInfo> imageList;//图片集合
    private List<User> userList;//点赞列表
    private List<CommentItem> comments;//评论列表
    private int viewType;//viewType  1.head  2.link  3.image

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LinkBean getLinkBean() {
        return linkBean;
    }

    public void setLinkBean(LinkBean linkBean) {
        this.linkBean = linkBean;
    }

    public List<ImageInfo> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageInfo> imageList) {
        this.imageList = imageList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<CommentItem> getComments() {
        return comments;
    }

    public void setComments(List<CommentItem> comments) {
        this.comments = comments;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
