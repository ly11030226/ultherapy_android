package com.aimyskin.miscmodule.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import androidx.annotation.DrawableRes;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * 2023年4月6日
 *
 * @author kang
 * app需要的图片转换工具类 compress
 */
public class AppBitmapUtils {

    public static Bitmap createEmptyBitmap(Bitmap bitmap) {
        return Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    }

    public static Bitmap bytesToBitmap(byte[] frame, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(frame.clone()));
        return bitmap;
    }

    public static Bitmap intArrayToBitmap(int[] pixels, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    // 将Bitmap转换为byte[]数组
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        int size = bitmap.getRowBytes() * bitmap.getHeight();
        byte[] bytes = new byte[size];
        bitmap.copyPixelsToBuffer(java.nio.ByteBuffer.wrap(bytes));
        return bytes;
    }

    public static void saveToBitmapFile(byte[] frame, String filePath, int width, int height) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(frame.clone()));
            FileOutputStream fos = new FileOutputStream(file);
            stitchBmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            stitchBmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void saveToBitmapFileCompress(byte[] frame, String filePath, int width, int height) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(frame.clone()));
            FileOutputStream fos = new FileOutputStream(file);
            stitchBmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
            stitchBmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Drawable下图片路径
     */
    public static String getResourcesUri(Context context, @DrawableRes int id) {
        Resources resources = context.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }
}
