package com.yangy.wechatrecyclerview.view;

import android.app.Dialog;
import android.content.Context;

import com.yangy.wechatrecyclerview.R;
/**
 * Created by yangy on 17/4/21.
 */

/**
 * 耗时操作时弹出
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.dialog_operate);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
}
