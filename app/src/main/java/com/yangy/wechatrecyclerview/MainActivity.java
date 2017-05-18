package com.yangy.wechatrecyclerview;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yangy.wechatrecyclerview.bean.CircleOfFriendsBean;
import com.yangy.wechatrecyclerview.bean.CommentConfig;
import com.yangy.wechatrecyclerview.bean.CommentItem;
import com.yangy.wechatrecyclerview.bean.LinkBean;
import com.yangy.wechatrecyclerview.bean.User;
import com.yangy.wechatrecyclerview.util.CommonUtils;
import com.yangy.wechatrecyclerview.util.DatasUtil;
import com.yangy.wechatrecyclerview.view.CommentListView.CommentListView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private LinearLayout edittextbody;
    private EditText editText;
    private ImageView sendIv;
    private LinearLayout ll_title;
    private int selectCircleItemH;
    private int selectCommentItemOffset;
    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int HEAD_HEIGHT = 600;//listview的headview高度

    private SwipeAdapter mSwipeAdapter;

    private List<CircleOfFriendsBean> mCircleOfFriendsBeanList;
    private CommentConfig mCommentConfig;

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;

    private int size = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        // 这里只是模拟数据，模拟Item的ViewType，根据ViewType决定显示什么菜单，到时候你可以根据你的数据来决定ViewType。
        mCircleOfFriendsBeanList = new ArrayList<>();
        addDataList(true);

        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        mSwipeMenuRecyclerView.setLayoutManager(layoutManager);// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mSwipeMenuRecyclerView.addOnScrollListener(mOnScrollListener);

        mSwipeAdapter = new SwipeAdapter(mCircleOfFriendsBeanList);
        mSwipeAdapter.setPresenterListener(presenterListener);
        mSwipeMenuRecyclerView.setAdapter(mSwipeAdapter);

        edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        editText = (EditText) findViewById(R.id.circleEt);
        sendIv = (ImageView) findViewById(R.id.sendIv);
        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenterListener != null) {
                    //发布评论
                    String content = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(MainActivity.this, "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    presenterListener.addComment(content, mCommentConfig);
                }
                presenterListener.showEditTextBody(View.GONE, null);
            }
        });
        setViewTreeObserver();
    }

    private PresenterListener presenterListener = new PresenterListener() {
        /**
         * 链接
         * @param position
         */
        @Override
        public void onLinkClick(int position) {
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
            intent.putExtra("url", mCircleOfFriendsBeanList.get(position).getLinkBean().getLinkUrl());
            startActivity(intent);
        }

        /**
         * 点赞
         * @param position
         * @param userItem
         */
        @Override
        public void updateAddFabulous(int position, User userItem) {
            if (userItem != null) {
                mCircleOfFriendsBeanList.get(position).getUserList().add(userItem);
                mSwipeAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 取消赞
         * @param position
         * @param id
         */
        @Override
        public void updateDeleteFabulous(int position, String id) {
            for (int i = 0; i < mCircleOfFriendsBeanList.get(position).getUserList().size(); i++) {
                if (id.equals(mCircleOfFriendsBeanList.get(position).getUserList().get(i).getId())) {
                    mCircleOfFriendsBeanList.get(position).getUserList().remove(i);
                    mSwipeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }

        /**
         * 删除评论
         * @param circlePosition 哪一条数据
         * @param commentId 评论ID
         */
        @Override
        public void deleteComment(int circlePosition, int commentPosition, String commentId) {
            CommentItem item = mCircleOfFriendsBeanList.get(circlePosition).getComments().get(commentPosition);
            if (commentId.equals(item.getId())) {
                mCircleOfFriendsBeanList.get(circlePosition).getComments().remove(commentPosition);
                mSwipeAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 回复别人的评论
         * @param commentConfig
         */
        @Override
        public void showEditTextBody(int visibility, CommentConfig commentConfig) {
            mCommentConfig = commentConfig;
            edittextbody.setVisibility(visibility);

            calculatedCircleItemAltitude(commentConfig);

            if (View.VISIBLE == visibility) {
                editText.requestFocus();
                //弹出键盘
                CommonUtils.showSoftInput(editText.getContext(), editText);
            } else if (View.GONE == visibility) {
                //隐藏键盘
                CommonUtils.hideSoftInput(editText.getContext(), editText);
            }
        }

        /**
         * 增加评论
         * @param content 评论内容
         * @param config
         */
        @Override
        public void addComment(String content, CommentConfig config) {
            CommentItem newItem = null;
            if (config.commentType == CommentConfig.Type.PUBLIC) {
                newItem = DatasUtil.createPublicComment(content);
            } else if (config.commentType == CommentConfig.Type.REPLY) {
                newItem = DatasUtil.createReplyComment(config.replyUser, content);
            }
            if (newItem != null) {
                mCircleOfFriendsBeanList.get(config.circlePosition).getComments().add(newItem);
                mSwipeAdapter.notifyDataSetChanged();
            }
            //清空评论文本
            editText.setText("");
        }

        /**
         * 关闭输入框
         */
        @Override
        public void closeInputBox() {
            if (edittextbody.getVisibility() == View.VISIBLE) {
                presenterListener.showEditTextBody(View.GONE, null);
            }
        }
    };

    /**
     * 获取评论高度
     *
     * @param commentConfig
     */
    private void calculatedCircleItemAltitude(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight() - HEAD_HEIGHT;
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = mSwipeRefreshLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mSwipeRefreshLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = mSwipeRefreshLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = editText.getHeight() + editText.getPaddingTop() + editText.getPaddingBottom();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    presenterListener.showEditTextBody(View.GONE, null);
                    return;
                }
                //偏移listview
                if (layoutManager != null && mCommentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(mCommentConfig.circlePosition, getListviewOffset(mCommentConfig));
                }
            }
        });
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度。
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - ll_title.getHeight();
//        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - titleBar.getHeight();
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        Log.i(MainActivity.class.getSimpleName(), "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 刷新监听。
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeMenuRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    size = 50;
                    mCircleOfFriendsBeanList.clear();
                    addDataList(true);
                    mSwipeAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };

    /**
     * 加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。

                Toast.makeText(MainActivity.this, "滑到最底部了，去加载更多吧！", Toast.LENGTH_SHORT).show();
                size += 50;
                addDataList(false);
                mSwipeAdapter.notifyDataSetChanged();
            }
        }
    };


    /**
     * 添加假数据
     *
     * @param top 第一次加载数据时加载头部
     */
    int tag = 0;//随机添加数据

    private void addDataList(boolean top) {
        if (top) {
            CircleOfFriendsBean circleOfFriendsBean = new CircleOfFriendsBean();
            circleOfFriendsBean.setViewType(SwipeAdapter.TYPE_HEAD);
            mCircleOfFriendsBeanList.add(circleOfFriendsBean);
        }

        for (int i = size - 50; i < size; i++) {
            CircleOfFriendsBean circleOfFriendsBean = new CircleOfFriendsBean();
            tag = DatasUtil.getRandomNum(2);
            switch (tag) {
                case 0:
                    int link = DatasUtil.getRandomNum(DatasUtil.linkIcon.length);
                    circleOfFriendsBean.setViewType(SwipeAdapter.TYPE_LINK);
                    circleOfFriendsBean.setContent(DatasUtil.CONTENTS[DatasUtil.getRandomNum(DatasUtil.CONTENTS.length)]);
                    circleOfFriendsBean.setUser(DatasUtil.users.get(DatasUtil.getRandomNum(DatasUtil.users.size())));
                    circleOfFriendsBean.setLinkBean(new LinkBean(DatasUtil.linkUrl[link], DatasUtil.linkIcon[link], DatasUtil.linkContent[link]));
                    circleOfFriendsBean.setUserList(DatasUtil.users);
                    circleOfFriendsBean.setComments(DatasUtil.createCommentItemList());
                    break;
                case 1:
                    circleOfFriendsBean.setViewType(SwipeAdapter.TYPE_IMAGE);
                    circleOfFriendsBean.setContent(DatasUtil.CONTENTS[DatasUtil.getRandomNum(DatasUtil.CONTENTS.length)]);
                    circleOfFriendsBean.setUser(DatasUtil.users.get(DatasUtil.getRandomNum(DatasUtil.users.size())));
                    circleOfFriendsBean.setImageList(DatasUtil.createImages());
                    circleOfFriendsBean.setUserList(DatasUtil.users);
                    circleOfFriendsBean.setComments(DatasUtil.createCommentItemList());
                    break;
                default:
                    break;
            }
            mCircleOfFriendsBeanList.add(circleOfFriendsBean);
        }
    }
}
