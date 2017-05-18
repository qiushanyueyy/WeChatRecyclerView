package com.yangy.wechatrecyclerview.bean;

/**
 * 人员信息实体类
 * Created by yangy on 2017/05/12
 */
public class User {
    private String id;//人员id
    private String name;//名字
    private String headUrl;//头像

    public User(String id, String name, String headUrl) {
        this.id = id;
        this.name = name;
        this.headUrl = headUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "id = " + id
                + "; name = " + name
                + "; headUrl = " + headUrl;
    }
}
