package com.yangy.wechatrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yangy.wechatrecyclerview.bean.ActionItem;
import com.yangy.wechatrecyclerview.bean.CircleOfFriendsBean;
import com.yangy.wechatrecyclerview.bean.CommentConfig;
import com.yangy.wechatrecyclerview.bean.CommentItem;
import com.yangy.wechatrecyclerview.bean.ImageInfo;
import com.yangy.wechatrecyclerview.util.DatasUtil;
import com.yangy.wechatrecyclerview.util.UrlUtils;
import com.yangy.wechatrecyclerview.view.CommentDialog;
import com.yangy.wechatrecyclerview.view.CommentListView.CommentListView;
import com.yangy.wechatrecyclerview.view.ExpandTextView.ExpandTextView;
import com.yangy.wechatrecyclerview.view.MultiImageView.MultiImageView;
import com.yangy.wechatrecyclerview.view.PraiseListView.PraiseListView;
import com.yangy.wechatrecyclerview.view.SnsPopupWindow;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangy on 2017/05/12
 */
public class SwipeAdapter extends SwipeMenuAdapter<SwipeAdapter.DefaultViewHolder> {
    public static final int TYPE_HEAD = 1;
    public static final int TYPE_LINK = 2;
    public static final int TYPE_IMAGE = 3;

    private List<CircleOfFriendsBean> mCircleOfFriendsBeanList;

    private PresenterListener presenterListener;//回调接口

    public SwipeAdapter(List<CircleOfFriendsBean> mCircleOfFriendsBeanList) {
        this.mCircleOfFriendsBeanList = mCircleOfFriendsBeanList;
    }

    public void setPresenterListener(PresenterListener presenterListener) {
        this.presenterListener = presenterListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mCircleOfFriendsBeanList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mCircleOfFriendsBeanList == null ? 0 : mCircleOfFriendsBeanList.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEAD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe, parent, false);
                break;
        }
        return view;
    }

    @Override
    public SwipeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SwipeAdapter.DefaultViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                holder.setHeadData();
                break;
            default:
                holder.setContentData(position, mCircleOfFriendsBeanList.get(position));
                break;
        }
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout ll_item_content;//item的ID  用来关闭键盘

        ExpandTextView tvContent;//动态内容
        ImageView snsBtn;//弹出点赞和评论
        ViewStub viewStub;
        View subViw;
        LinearLayout digCommentBody;//点赞和评论的Layout
        View lin_dig;//点赞和评论的分割线

        ImageView iv_bg;//头部背景
        ImageView iv_head;//头像
        TextView tv_name;//名称

        /**
         * 点赞列表
         */
        PraiseListView praiseListView;
        /**
         * 评论列表
         */
        CommentListView commentList;

        LinearLayout ll_link;//链接
        ImageView iv_link;
        TextView tv_link;

        MultiImageView multiImageView;//图片

        public DefaultViewHolder(View itemView, int viewType) {
            super(itemView);

            switch (viewType) {
                case TYPE_HEAD:
                    iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
                    iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
                    tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                    break;
                case TYPE_LINK:
                    tvContent = (ExpandTextView) itemView.findViewById(R.id.tvContent);
                    ll_item_content = (LinearLayout) itemView.findViewById(R.id.ll_item_content);
                    praiseListView = (PraiseListView) itemView.findViewById(R.id.praiseListView);
                    commentList = (CommentListView) itemView.findViewById(R.id.commentList);
                    digCommentBody = (LinearLayout) itemView.findViewById(R.id.digCommentBody);
                    lin_dig = itemView.findViewById(R.id.lin_dig);
                    iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
                    tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                    viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
                    snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);
                    viewStub.setLayoutResource(R.layout.viewstub_link);
                    subViw = viewStub.inflate();
                    ll_link = (LinearLayout) subViw.findViewById(R.id.ll_link);
                    iv_link = (ImageView) subViw.findViewById(R.id.iv_link);
                    tv_link = (TextView) subViw.findViewById(R.id.tv_link);
                    break;
                case TYPE_IMAGE:
                    tvContent = (ExpandTextView) itemView.findViewById(R.id.tvContent);
                    ll_item_content = (LinearLayout) itemView.findViewById(R.id.ll_item_content);
                    praiseListView = (PraiseListView) itemView.findViewById(R.id.praiseListView);
                    commentList = (CommentListView) itemView.findViewById(R.id.commentList);
                    digCommentBody = (LinearLayout) itemView.findViewById(R.id.digCommentBody);
                    lin_dig = itemView.findViewById(R.id.lin_dig);
                    iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
                    tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                    viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
                    snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);
                    viewStub.setLayoutResource(R.layout.viewstub_image);
                    subViw = viewStub.inflate();
                    multiImageView = (MultiImageView) subViw.findViewById(R.id.multiImagView);
                    break;
            }
        }

        /**
         * 设置头部
         */
        public void setHeadData() {
            Glide.with(itemView.getContext()).load(DatasUtil.image[DatasUtil.getRandomNum(DatasUtil.image.length)]).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.iv_bg);
            Glide.with(itemView.getContext()).load(DatasUtil.image[DatasUtil.getRandomNum(DatasUtil.image.length)]).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.iv_head);
            iv_bg.setOnClickListener(this);
            iv_head.setOnClickListener(this);
        }

        /**
         * 设置共有数据
         *
         * @param circlePosition
         * @param bean
         */
        public void setContentData(final int circlePosition, final CircleOfFriendsBean bean) {
            this.ll_item_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    presenterListener.closeInputBox();//item的触摸事件，触发时键盘为弹出状态就隐藏键盘
                    return true;
                }
            });
            this.tv_name.setText(bean.getUser().getName());//发布动态的人员名称
            Glide.with(itemView.getContext()).load(bean.getUser().getHeadUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.iv_head);//发布动态的人员头像
            /**点赞和评论的PopupWindow**/
            final SnsPopupWindow snsPopupWindow = new SnsPopupWindow(itemView.getContext());
            //判断是否已点赞
            String curUserFavortId = DatasUtil.getCurUserFavortId(DatasUtil.curUser.getId());
            if (!TextUtils.isEmpty(curUserFavortId)) {
                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
            } else {
                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
            }
            snsPopupWindow.update();
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, curUserFavortId));
            this.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出popupwindow
                    snsPopupWindow.showPopupWindow(view);
                }
            });
            /**点赞和评论**/
            if (bean.getUserList() != null && bean.getUserList().size() != 0 || bean.getComments() != null && bean.getComments().size() != 0) {
                this.digCommentBody.setVisibility(View.VISIBLE);
                //点赞
                if (bean.getUserList() != null && bean.getUserList().size() != 0) {
                    this.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            String userName = bean.getUserList().get(position).getName();
                            String userId = bean.getUserList().get(position).getId();
                            Toast.makeText(MyApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                        }
                    });
                    this.praiseListView.setDatas(bean.getUserList());
                    this.praiseListView.setVisibility(View.VISIBLE);
                } else {
                    this.praiseListView.setVisibility(View.GONE);
                }
                //评论
                if (bean.getComments() != null && bean.getComments().size() != 0) {
                    this.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int commentPosition) {
                            CommentItem commentItem = bean.getComments().get(commentPosition);
                            if (DatasUtil.curUser.getId().equals(commentItem.getUser().getId())) {//复制或者删除自己的评论

                                CommentDialog dialog = new CommentDialog(itemView.getContext(), presenterListener, commentItem, circlePosition, commentPosition);
                                dialog.show();
                            } else {//回复别人的评论
                                if (presenterListener != null) {
                                    CommentConfig config = new CommentConfig();
                                    config.circlePosition = circlePosition;
                                    config.commentPosition = commentPosition;
                                    config.commentType = CommentConfig.Type.REPLY;
                                    config.replyUser = commentItem.getUser();
                                    presenterListener.showEditTextBody(View.VISIBLE, config);
                                }
                            }
                        }
                    });
                    this.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int commentPosition) {
                            //长按进行复制或者删除
                            CommentItem commentItem = bean.getComments().get(commentPosition);
                            CommentDialog dialog = new CommentDialog(itemView.getContext(), presenterListener, commentItem, circlePosition, commentPosition);
                            dialog.show();
                        }
                    });
                    this.commentList.setDatas(bean.getComments());
                    this.commentList.setVisibility(View.VISIBLE);
                } else {
                    this.commentList.setVisibility(View.GONE);
                }
            } else {
                this.digCommentBody.setVisibility(View.GONE);
            }
            this.lin_dig.setVisibility(bean.getUserList() != null && bean.getUserList().size() != 0 && bean.getComments() != null
                    && bean.getComments().size() != 0 ? View.VISIBLE : View.GONE);//如果点赞和评论有一项没有数据就隐藏分割线
            if (!TextUtils.isEmpty(bean.getContent())) {
                this.tvContent.setText(UrlUtils.formatUrlString(bean.getContent()));
            }
            this.tvContent.setVisibility(TextUtils.isEmpty(bean.getContent()) ? View.GONE : View.VISIBLE);

            if (bean.getLinkBean() != null) {/**链接**/
                this.subViw.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(bean.getLinkBean().getLinkUrl())) {
                    this.ll_link.setOnClickListener(this);
                }
                if (!TextUtils.isEmpty(bean.getLinkBean().getLinkContent())) {
                    this.tv_link.setText(bean.getLinkBean().getLinkContent());
                }
                if (!TextUtils.isEmpty(bean.getLinkBean().getLinkIcon())) {
                    Glide.with(MyApplication.getContext()).load(bean.getLinkBean().getLinkIcon()).into(this.iv_link);
                }
            } else if (bean.getImageList() != null) {/**图片**/
                this.subViw.setVisibility(View.VISIBLE);
                this.multiImageView.setList(bean.getImageList());
                this.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //imagesize是作为loading时的图片size
                        ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                        List<String> imageInfos = new ArrayList<String>();
                        for (ImageInfo imageInfo : bean.getImageList()) {
                            imageInfos.add(imageInfo.url);
                        }
                        ImagePagerActivity.startImagePagerActivity(itemView.getContext(), imageInfos, position, imageSize);
                    }
                });
            } else {
                this.subViw.setVisibility(View.GONE);
            }
        }

        /**
         * 点击事件
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_link://链接
                    if (presenterListener != null) {
                        presenterListener.onLinkClick(getAdapterPosition());
                    }
                    break;
                case R.id.iv_bg://头部背景图片
                    Glide.with(itemView.getContext()).load(DatasUtil.image[DatasUtil.getRandomNum(DatasUtil.image.length)]).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.iv_bg);
                    break;
                case R.id.iv_head://头像
                    Glide.with(itemView.getContext()).load(DatasUtil.image[DatasUtil.getRandomNum(DatasUtil.image.length)]).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.iv_head);
                    break;
            }
        }
    }


    /**
     * PopupWindow的Item点击事件
     */
    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String id;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;

        public PopupItemClickListener(int circlePosition, String favorId) {
            this.id = favorId;
            this.mCirclePosition = circlePosition;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (presenterListener != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            presenterListener.updateAddFabulous(mCirclePosition, DatasUtil.curUser);
                        } else {//取消点赞
                            presenterListener.updateDeleteFabulous(mCirclePosition, id);
                        }
                    }
                    break;
                case 1://发布评论
                    if (presenterListener != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        presenterListener.showEditTextBody(View.VISIBLE, config);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
