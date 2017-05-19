package com.yangy.wechatrecyclerview.bean;

/**
 * 图片信息 (单张图片显示的时候需要图片的宽高来计算 (根据宽高值哪个较大) 宽度铺满高度适应||高度铺满宽度适应)
 * Created by yangy on 2017/05/12
 */
public class ImageInfo {

    public ImageInfo(String url) {
        this.url = url;
    }

    public String url;
    public int w;
    public int h;
}
