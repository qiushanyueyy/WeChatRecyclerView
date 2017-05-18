package com.yangy.wechatrecyclerview;

import android.app.Application;
import android.content.Context;

/**
 * MyApplication
 * Created by yangy on 2017/05/12
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
