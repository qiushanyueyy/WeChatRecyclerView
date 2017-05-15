package com.yangy.wechatrecyclerview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yangy.wechatrecyclerview.bean.LinkBean;
import com.yangy.wechatrecyclerview.bean.ImageInfo;
import com.yangy.wechatrecyclerview.bean.User;
import com.yangy.wechatrecyclerview.bean.ViewTypeBean;
import com.yangy.wechatrecyclerview.util.DatasUtil;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Activity mContext;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SwipeAdapter mSwipeAdapter;

    private List<ViewTypeBean> mViewTypeBeanList;

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;

    private int size = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        // 这里只是模拟数据，模拟Item的ViewType，根据ViewType决定显示什么菜单，到时候你可以根据你的数据来决定ViewType。
        mViewTypeBeanList = new ArrayList<>();
        addDataList(true);

        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//        mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。
        // 添加滚动监听。
        mSwipeMenuRecyclerView.addOnScrollListener(mOnScrollListener);

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
//        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
//        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mSwipeAdapter = new SwipeAdapter(mViewTypeBeanList);
        mSwipeAdapter.setPresenterListener(presenterListener);
        mSwipeMenuRecyclerView.setAdapter(mSwipeAdapter);
    }

    private PresenterListener presenterListener = new PresenterListener() {
        /**
         * 链接
         * @param position
         */
        @Override
        public void onLinkClick(int position) {
            Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
            intent.putExtra("url",mViewTypeBeanList.get(position).getLinkBean().getLinkUrl());
            startActivity(intent);
        }
        /**
         * 点赞
         * @param position
         * @param userItem
         */
        @Override
        public void updateAddFabulous(int position, User userItem) {
            if (userItem != null){
                mViewTypeBeanList.get(position).getUserList().add(userItem);
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
            for(int i=0; i<mViewTypeBeanList.get(position).getUserList().size(); i++){
                if(id.equals(mViewTypeBeanList.get(position).getUserList().get(i).getId())){
                    mViewTypeBeanList.get(position).getUserList().remove(i);
                    mSwipeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    };
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
                    mViewTypeBeanList.clear();
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
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
                        .setImage(R.mipmap.ic_action_add) // 图标。
                        .setWidth(width) // 宽度。
                        .setHeight(height); // 高度。
                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);

                swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
            }
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
         *                        #RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
                mViewTypeBeanList.remove(adapterPosition);
                mSwipeAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_all_activity, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        } else if (item.getItemId() == R.id.menu_open_rv_menu) {
//            mSwipeMenuRecyclerView.smoothOpenRightMenu(0);
//        }
//        return true;
//    }



    /**
     * 添加假数据
     *
     * @param top 第一次加载数据时加载头部
     */
    private void addDataList(boolean top) {
        if (top) {
            ViewTypeBean viewTypeBean = new ViewTypeBean();
            viewTypeBean.setViewType(SwipeAdapter.TYPE_HEAD);
            mViewTypeBeanList.add(viewTypeBean);
        }
        for (int i = size - 50, j = 0; i < size; i++, j++) {
            ViewTypeBean viewTypeBean = new ViewTypeBean();
            if (j == 0) {
                int link = DatasUtil.getRandomNum(DatasUtil.linkIcon.length);
                viewTypeBean.setViewType(SwipeAdapter.TYPE_LINK);
                viewTypeBean.setContent(DatasUtil.CONTENTS[DatasUtil.getRandomNum(DatasUtil.CONTENTS.length)]);
                viewTypeBean.setLinkBean(new LinkBean(DatasUtil.linkUrl[link],DatasUtil.linkIcon[link],DatasUtil.linkContent[link]));
                viewTypeBean.setUserList(DatasUtil.users);
                viewTypeBean.setComments(DatasUtil.createCommentItemList());
            } else if (j == 1) {
                viewTypeBean.setViewType(SwipeAdapter.TYPE_IMAGE);
                viewTypeBean.setContent(DatasUtil.CONTENTS[DatasUtil.getRandomNum(DatasUtil.CONTENTS.length)]);
                viewTypeBean.setImageList(DatasUtil.createImages());
                viewTypeBean.setUserList(DatasUtil.users);
                j = -1;
            }
            mViewTypeBeanList.add(viewTypeBean);
        }
    }
}
