/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yangy.wechatrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yangy.wechatrecyclerview.bean.ActionItem;
import com.yangy.wechatrecyclerview.bean.ViewTypeBean;
import com.yangy.wechatrecyclerview.util.DatasUtil;
import com.yangy.wechatrecyclerview.util.UrlUtils;
import com.yangy.wechatrecyclerview.view.CommentListView.CommentListView;
import com.yangy.wechatrecyclerview.view.ExpandTextView.ExpandTextView;
import com.yangy.wechatrecyclerview.view.MultiImageView.MultiImageView;
import com.yangy.wechatrecyclerview.view.PraiseListView.PraiseListView;
import com.yangy.wechatrecyclerview.view.SnsPopupWindow;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class SwipeAdapter extends SwipeMenuAdapter<SwipeAdapter.DefaultViewHolder> {
    public static final int TYPE_HEAD = 1;
    public static final int TYPE_LINK = 2;
    public static final int TYPE_IMAGE = 3;

    private List<ViewTypeBean> mViewTypeBeanList;

    private PresenterListener presenterListener;//回调接口

    public SwipeAdapter(List<ViewTypeBean> mViewTypeBeanList) {
        this.mViewTypeBeanList = mViewTypeBeanList;
    }

    public void setPresenterListener(PresenterListener presenterListener) {
        this.presenterListener = presenterListener;
    }
    @Override
    public int getItemViewType(int position) {
        return mViewTypeBeanList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mViewTypeBeanList == null ? 0 : mViewTypeBeanList.size();
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
        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView,viewType);
//        viewHolder.onLinkClickListener = onLinkClickListener;
//        viewHolder.fabulousListener = fabulousListener;
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(SwipeAdapter.DefaultViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                break;
            case TYPE_LINK:
                holder.setPopupWindow(position,mViewTypeBeanList.get(position));
                holder.setLinkData(mViewTypeBeanList.get(position));
                break;
            case TYPE_IMAGE:
                holder.setPopupWindow(position,mViewTypeBeanList.get(position));
                holder.setImageData(mViewTypeBeanList.get(position));
                break;
        }
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ExpandTextView tvContent;
        ImageView snsBtn;
        ViewStub viewStub;
        /** 点赞列表*/
        public PraiseListView praiseListView;
        /** 评论列表 */
        public CommentListView commentList;

        View subViw;
        LinearLayout ll_link;
        ImageView iv_link;
        TextView tv_link;

        MultiImageView multiImageView;

        public DefaultViewHolder(View itemView, int viewType) {
            super(itemView);
            praiseListView = (PraiseListView) itemView.findViewById(R.id.praiseListView);
            commentList = (CommentListView)itemView.findViewById(R.id.commentList);

            switch (viewType) {
                case TYPE_HEAD:
                    itemView.setOnClickListener(this);
                    break;
                case TYPE_LINK:
                    viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
                    snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);
                    viewStub.setLayoutResource(R.layout.viewstub_link);
                    subViw  = viewStub.inflate();
                    ll_link = (LinearLayout) subViw.findViewById(R.id.ll_link);
                    iv_link = (ImageView) subViw.findViewById(R.id.iv_link);
                    tv_link = (TextView) subViw.findViewById(R.id.tv_link);
                    itemView.setOnClickListener(this);
                    tvContent = (ExpandTextView) itemView.findViewById(R.id.tvContent);
                    break;
                case TYPE_IMAGE:
                    viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
                    snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);
                    viewStub.setLayoutResource(R.layout.viewstub_image);
                    subViw  = viewStub.inflate();
                    multiImageView = (MultiImageView) subViw.findViewById(R.id.multiImagView);
                    itemView.setOnClickListener(this);
                    tvContent = (ExpandTextView) itemView.findViewById(R.id.tvContent);
                    break;
            }
        }
        public void setPopupWindow(int position, final ViewTypeBean bean){
            final SnsPopupWindow snsPopupWindow =  new SnsPopupWindow(itemView.getContext());
            //判断是否已点赞
            String curUserFavortId = DatasUtil.getCurUserFavortId(DatasUtil.curUser.getId());
            if(!TextUtils.isEmpty(curUserFavortId)){
                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
            }else{
                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
            }
            snsPopupWindow.update();
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(position, curUserFavortId));
            this.snsBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //弹出popupwindow
                    snsPopupWindow.showPopupWindow(view);
                }
            });
            //点赞和评论
            if(bean.getUserList() != null && bean.getUserList().size() != 0){//处理点赞列表
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
            }else{
                this.praiseListView.setVisibility(View.GONE);
            }

        }
        public void setLinkData(ViewTypeBean bean) {
            if(!TextUtils.isEmpty(bean.getContent())){
                this.tvContent.setText(UrlUtils.formatUrlString(bean.getContent()));
            }
            if (bean.getLinkBean() != null) {
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
            }else {
                this.subViw.setVisibility(View.GONE);
            }
            this.tvContent.setVisibility(TextUtils.isEmpty(bean.getContent()) ? View.GONE : View.VISIBLE);
        }
        public void setImageData(ViewTypeBean bean) {
            if(!TextUtils.isEmpty(bean.getContent())){
                this.tvContent.setText(UrlUtils.formatUrlString(bean.getContent()));
            }
            if (bean.getImageList() != null) {
                this.subViw.setVisibility(View.VISIBLE);
                this.tvContent.setVisibility(TextUtils.isEmpty(bean.getContent()) ? View.GONE : View.VISIBLE);
                this.multiImageView.setList(bean.getImageList());
                this.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("position", "" + position);
//                    //imagesize是作为loading时的图片size
//                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
//
//                    List<String> photoUrls = new ArrayList<String>();
//                    for(ImageInfo photoInfo : photos){
//                        ImageInfo.add(photoInfo.url);
//                    }
//                    ImagePagerActivity.startImagePagerActivity(((MainActivity) context), photoUrls, position, imageSize);
//

                    }
                });
            }else{
                this.subViw.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_link:
                    if (presenterListener != null) {
                        presenterListener.onLinkClick(getAdapterPosition());
                    }
                    break;
            }
        }
    }



    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener{
        private String id;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;

        public PopupItemClickListener(int circlePosition, String favorId){
            this.id = favorId;
            this.mCirclePosition = circlePosition;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if(System.currentTimeMillis()-mLasttime<700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if(presenterListener != null){
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            presenterListener.updateAddFabulous(mCirclePosition, DatasUtil.curUser);
                        } else {//取消点赞
                            presenterListener.updateDeleteFabulous(mCirclePosition, id);
                        }
                    }
                    break;
//                case 1://发布评论
//                    if(presenter != null){
//                        CommentConfig config = new CommentConfig();
//                        config.circlePosition = mCirclePosition;
//                        config.commentType = CommentConfig.Type.PUBLIC;
//                        presenter.showEditTextBody(config);
//                    }
//                    break;
                default:
                    break;
            }
        }
    }
}
