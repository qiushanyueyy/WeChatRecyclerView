package com.yangy.wechatrecyclerview.bean;

/**
 * 发布链接的内容
 * Created by yangy on 2017/05/12
 */

public class LinkBean {
    private String linkUrl;//链接地址
    private String linkIcon;//图标
    private String linkContent;//文本内容

    public LinkBean(String linkUrl, String linkIcon, String linkContent) {
        this.linkUrl = linkUrl;
        this.linkIcon = linkIcon;
        this.linkContent = linkContent;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(String linkIcon) {
        this.linkIcon = linkIcon;
    }

    public String getLinkContent() {
        return linkContent;
    }

    public void setLinkContent(String linkContent) {
        this.linkContent = linkContent;
    }
}
