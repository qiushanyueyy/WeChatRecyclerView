package com.yangy.wechatrecyclerview.bean;

/**
 * 发表评论或者回复评论的人员信息
 * Created by yangy on 2017/05/12
 */
public class CommentConfig {
    public static enum Type {
        PUBLIC("public"), REPLY("reply");

        private String value;

        private Type(String value) {
            this.value = value;
        }

    }

    public int circlePosition;
    public int commentPosition;
    public Type commentType;
    public User replyUser;

    @Override
    public String toString() {
        String replyUserStr = "";
        if (replyUser != null) {
            replyUserStr = replyUser.toString();
        }
        return "circlePosition = " + circlePosition
                + "; commentPosition = " + commentPosition
                + "; commentType ＝ " + commentType
                + "; replyUser = " + replyUserStr;
    }
}
