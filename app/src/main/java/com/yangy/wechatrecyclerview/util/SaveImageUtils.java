package com.yangy.wechatrecyclerview.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.yangy.wechatrecyclerview.view.ActionSheetDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 保存图片到本地并刷新系统图库
 * Created by yangy on 2017/05/12
 */

public class SaveImageUtils {
    // 原图片bitmap对象
    private static Bitmap bitmapNet;
    private static Context mContext;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (bitmapNet != null) {
                    saveImageToGallery(bitmapNet);
                }
            }
        }
    };

    /**
     * 弹出保存图片的方式Dialog
     */
    public static void showSavedPhotoDialog(final String mImageUrl, final Context context) {
        mContext = context;
        new ActionSheetDialog(mContext)
                .builder()
                .setCanceledOnTouchOutside(true)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("保存图片", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onSheetItemClick(int which) {
                                /**网络请求需要启动子线程**/
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bitmapNet = GetLocalOrNetBitmap(mImageUrl);
                                        handler.sendEmptyMessage(0);
                                    }
                                }).start();
                            }
                        }
                ).show();
    }

    //保存图片并刷新系统图库
    public static void saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "WeChatRecyclerView/photo");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //刷新系统图库
        String fileNameUri = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bmp, "", "");
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = mContext.getContentResolver().query(Uri.parse(fileNameUri), proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        MediaScannerConnection.scanFile(mContext, new String[]{img_path}, null, null);
        // 回收bitmap的内存
        bitmapNet.recycle();
    }

    //将网络图片转换成Bitmap
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 2 * 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 2 * 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[2 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
}
