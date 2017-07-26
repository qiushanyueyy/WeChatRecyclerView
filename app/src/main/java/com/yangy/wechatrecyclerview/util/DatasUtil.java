package com.yangy.wechatrecyclerview.util;

import android.text.TextUtils;
import android.util.Log;

import com.yangy.wechatrecyclerview.bean.CommentItem;
import com.yangy.wechatrecyclerview.bean.ImageInfo;
import com.yangy.wechatrecyclerview.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 数据集
 * Created by yangy on 2017/05/12
 */
public class DatasUtil {
    public static final String[] CONTENTS = {
            "哈哈，18123456789,ChinaAr  http://www.ChinaAr.com;一个不错的VR网站。哈哈，ChinaAr  http://www.ChinaAr.com;一个不错的VR网站。哈哈，ChinaAr  http://www.ChinaAr.com;一个不错的VR网站。哈哈，ChinaAr  http://www.ChinaAr.com;一个不错的VR网站。",
            "今天是个好日子，http://www.ChinaAr.com;一个不错的VR网站,18123456789",
            "呵呵，http://www.ChinaAr.com;一个不错的VR网站,18123456789",
            "只有http|https|ftp|svn://开头的网址才能识别为网址，正则表达式写的不太好，如果你又更好的正则表达式请评论告诉我，谢谢！",
            "VR（Virtual Reality，即虚拟现实，简称VR），是由美国VPL公司创建人拉尼尔（Jaron Lanier）在20世纪80年代初提出的。其具体内涵是：综合利用计算机图形系统和各种现实及控制等接口设备，在计算机上生成的、可交互的三维环境中提供沉浸感觉的技术。其中，计算机生成的、可交互的三维环境称为虚拟环境（即Virtual Environment，简称VE）。虚拟现实技术是一种可以创建和体验虚拟世界的计算机仿真系统的技术。它利用计算机生成一种模拟环境，利用多源信息融合的交互式三维动态视景和实体行为的系统仿真使用户沉浸到该环境中。",
            "张勐，八零后生人，烟标收藏家。天津市礼仪服务协会副秘书长、天津市模特专业委员会副主任、斯美瑞广告传媒创始人。资深品牌策划人、主持人。"};
    public static final String[] HEADIMG = {
            "http://img.wzfzl.cn/uploads/allimg/140820/co140R00Q925-14.jpg",
            "http://www.feizl.com/upload2007/2014_06/1406272351394618.png",
            "http://v1.qzone.cc/avatar/201308/30/22/56/5220b2828a477072.jpg%21200x200.jpg",
            "http://v1.qzone.cc/avatar/201308/22/10/36/521579394f4bb419.jpg!200x200.jpg",
            "http://v1.qzone.cc/avatar/201408/20/17/23/53f468ff9c337550.jpg!200x200.jpg",
            "http://cdn.duitang.com/uploads/item/201408/13/20140813122725_8h8Yu.jpeg",
            "http://img.woyaogexing.com/touxiang/nv/20140212/9ac2117139f1ecd8%21200x200.jpg",
            "http://p1.qqyou.com/touxiang/uploadpic/2013-3/12/2013031212295986807.jpg"};

    public static final String[] linkContent = {"百度一下，你就知道！", "百度翻译！", "我的github！"};
    public static final String[] linkUrl = {"https://www.baidu.com/?tn=62095104_oem_dg", "http://fanyi.baidu.com/?aldtype=16047#auto/zh", "https://github.com/qiushanyueyy"};
    public static final String[] linkIcon = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494417394742&di=a3a8ae52d8488b51b6911f3f09ee0410&imgtype=0&src=http%3A%2F%2Fcuimg.zuyushop.com%2FcuxiaoPic%2F201412%2F2014120020045241723.jpg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494417394742&di=a3a8ae52d8488b51b6911f3f09ee0410&imgtype=0&src=http%3A%2F%2Fcuimg.zuyushop.com%2FcuxiaoPic%2F201412%2F2014120020045241723.jpg"
            , "https://avatars2.githubusercontent.com/u/27658115?v=3&u=ebaec7478cf38cae003878c8890c96dd78931ef9&s=400"};
    public static final String[] image = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494420063017&di=25e3717ee58b099afa11f18839d683ee&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201504%2F04%2F20150404H0420_dcrLa.thumb.700_0.jpeg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494420133676&di=dd39b9e4471d6b6b40b8f37070e666fc&imgtype=0&src=http%3A%2F%2Fi1.17173.itc.cn%2F2016%2Fnews%2F2016%2Fhongren%2F0101%2Ftop05.jpg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494420146190&di=21a74f65f6016302f104a9101abf7a65&imgtype=jpg&src=http%3A%2F%2Fimg4.imgtn.bdimg.com%2Fit%2Fu%3D183831790%2C1047042445%26fm%3D214%26gp%3D0.jpg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494420158124&di=0258e30485af657a1ba2ef6d74684bc4&imgtype=0&src=http%3A%2F%2Fa-ssl.duitang.com%2Fuploads%2Fitem%2F201510%2F03%2F20151003013149_ErJXW.jpeg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494420166645&di=a525d48ffd612d6237d28a08565298f9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D362d38e1dc2a283443a636036bb4c92e%2F2258cdc79f3df8dc8774a76dc511728b4610281c.jpg"
            , "http://upload.ldnews.cn/2016/0527/1464347280261.jpg"
            , "http://img3.duitang.com/uploads/item/201511/15/20151115091213_uPyWQ.jpeg"
            , "http://img1.imgtn.bdimg.com/it/u=2830219948,2566768538&fm=214&gp=0.jpg"
            , "http://image.tianjimedia.com/uploadImages/2015/295/45/5W3IY0OD140F_iQnOgwZ_600.jpg"};

    public static List<User> users = new ArrayList<User>();
    /**
     * 评论id自增长
     */
    private static int commentId = 0;
    /**
     * 自己的信息
     */
    public static final User curUser = new User("0", "秋山月", HEADIMG[0]);

    static {
        User user1 = new User("1", "勐哥", HEADIMG[1]);
        User user2 = new User("2", "唐小卡", HEADIMG[2]);
        User user3 = new User("3", "冬冬姐", HEADIMG[3]);
        User user4 = new User("4", "东东婊", HEADIMG[4]);
        User user5 = new User("5", "UI", HEADIMG[5]);
        User user6 = new User("6", "唐哥", HEADIMG[6]);
        User user7 = new User("7", "尼古拉斯·利东·赵", HEADIMG[7]);

        users.add(curUser);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
    }

    public static String[] getContents = {" 好了,别聊了,赶紧抓紧时间写BUG吧", "害你加班的BUG就是我写的", "全部都留下加班", "一脸懵比", "感觉身体被掏空", "百度上搜吗"};

    public static User getUser() {
        return users.get(getRandomNum(users.size()));
    }

    public static String getContent() {
        return CONTENTS[getRandomNum(CONTENTS.length)];
    }

    public static int getRandomNum(int max) {
        Random random = new Random();
        int result = random.nextInt(max);
        return result;
    }

    /**
     * 创建图片假数据
     *
     * @return
     */
    public static List<ImageInfo> createImages() {
        List<ImageInfo> photos = new ArrayList<ImageInfo>();
        List<String> urls = new ArrayList<String>();
        int size = getRandomNum(image.length+1);
        if (size > 0) {
            if (size > 9) {
                size = 9;
            }
            for (int i = 0; i < size; i++) {
                ImageInfo photo = new ImageInfo(image[getRandomNum(size)]);
                if (!urls.contains(photo.url)) {
                    photos.add(photo);
                    urls.add(photo.url);
                } else {
                    i--;
                }
            }
        }
        return photos;
    }

    /**
     * 创建评论的假数据
     *
     * @return
     */
    public static List<CommentItem> createCommentItemList() {
        List<CommentItem> items = new ArrayList<CommentItem>();
        int size = getRandomNum(10);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                items.add(createComment());
            }
        }
        return items;
    }

    /**
     * 评论的假数据
     *
     * @return
     */
    public static CommentItem createComment() {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(commentId++));
        item.setContent(getContents[getRandomNum(getContents.length)]);
        User user = getUser();
        item.setUser(user);
        if (getRandomNum(10) % 2 == 0) {
            while (true) {
                User replyUser = getUser();
                if (!user.getId().equals(replyUser.getId())) {
                    item.setToReplyUser(replyUser);
                    break;
                }
            }
        }
        return item;
    }

    /**
     * 创建发布评论
     *
     * @return
     */
    public static CommentItem createPublicComment(String content) {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(commentId++));
        item.setContent(content);
        item.setUser(curUser);
        return item;
    }

    /**
     * 创建回复评论
     *
     * @return
     */
    public static CommentItem createReplyComment(User replyUser, String content) {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(commentId++));
        item.setContent(content);
        item.setUser(curUser);
        item.setToReplyUser(replyUser);
        return item;
    }


    /**
     * 判断是否已点赞
     *
     * @param curUserId 用户ID
     * @return 为空表示没有点赞  点赞列表中有用户id就表示已赞
     */
    public static String getCurUserFavortId(String curUserId) {
        String favortid = "";
        if (!TextUtils.isEmpty(curUserId)) {//点赞列表中匹配用户id
            for (User item : users) {
                if (curUserId.equals(item.getId())) {
                    favortid = item.getId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
