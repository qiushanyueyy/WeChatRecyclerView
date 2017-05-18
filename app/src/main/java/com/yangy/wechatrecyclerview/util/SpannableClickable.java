package com.yangy.wechatrecyclerview.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.yangy.wechatrecyclerview.MyApplication;
import com.yangy.wechatrecyclerview.R;

/**
 * Created by yangy on 2017/05/12
 */
public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.color_38ADFF;
    /**
     * text颜色
     */
    private int textColor;

    public SpannableClickable() {
        this.textColor = MyApplication.getContext().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
