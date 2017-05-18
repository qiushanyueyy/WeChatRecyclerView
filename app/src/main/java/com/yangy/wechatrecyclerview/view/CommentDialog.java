package com.yangy.wechatrecyclerview.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yangy.wechatrecyclerview.PresenterListener;
import com.yangy.wechatrecyclerview.R;
import com.yangy.wechatrecyclerview.bean.CommentItem;
import com.yangy.wechatrecyclerview.util.DatasUtil;

/**
 * 评论长按对话框，复制和删除
 * Created by yangy on 2017/05/12
 */
public class CommentDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Context mContext;
    private PresenterListener presenterListener;
    private CommentItem mCommentItem;
    private int mCirclePosition;
    private int mCommentPosition;

    public CommentDialog(Context context, PresenterListener presenterListener,
                         CommentItem commentItem, int circlePosition, int commentPosition) {
        super(context, R.style.comment_dialog);
        mContext = context;
        this.presenterListener = presenterListener;
        this.mCommentItem = commentItem;
        this.mCirclePosition = circlePosition;
        this.mCommentPosition = commentPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        TextView copyTv = (TextView) findViewById(R.id.copyTv);
        copyTv.setOnClickListener(this);
        TextView deleteTv = (TextView) findViewById(R.id.deleteTv);
        if (mCommentItem != null
                && DatasUtil.curUser.getId().equals(
                mCommentItem.getUser().getId())) {
            deleteTv.setVisibility(View.VISIBLE);
        } else {
            deleteTv.setVisibility(View.GONE);
        }
        deleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyTv:
                if (mCommentItem != null) {
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(mCommentItem.getContent());
                }
                dismiss();
                break;
            case R.id.deleteTv:
                if (presenterListener != null && mCommentItem != null) {
                    presenterListener.deleteComment(mCirclePosition, mCommentPosition, mCommentItem.getId());
                }
                dismiss();
                break;
            default:
                break;
        }
    }

}
