package com.busilinq.casepocket.utils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/17
 * author: shiquan.lu
 */

public class BitmapUtil {


    private static String TAG = BitmapUtil.class.getName();
    /**
     * 图片二进制转为Bitmap
     * @return Bitmap  位图
     */
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {


        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    /**
     * 加密
     * @param byteContent 待加密二进制
     * @param password    密码
     * @return   byte[]   加密后的二进制
     */
    public static byte[]  encrypt(byte [] byteContent, String password) {
        byte[] result=null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            //byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 解密
     * @param content   待解密二进制
     * @param password  密码
     * @return  byte [] 解密后的二进制
     */
    public static byte[] decrypt(byte[] content, String password){
        byte[] result =null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            result= cipher.doFinal(content);
            return result; // 加密 Svn中文网 nm
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * Drawable 转 bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable){
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap() ;
        }else if(drawable instanceof NinePatchDrawable){
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }else{
            return null ;
        }
    }
    /**
     * 以最省内存的方式读取本地资源的图片
     * @return
     */
    public static Bitmap readBitMap(File file){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
        opt.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一


        //获取资源图片
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
// is = context.openFileInput(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is,null,opt);
    }
    /**获取带黑边的图片有效区域
     * @param bitmapOrg
     * 图片
     * @return
     * 上下左右的截取位置坐标，像素点
     */
    public static int[] getValidArea ( Bitmap bitmapOrg )
    {
        int h = bitmapOrg.getHeight();
        int w = bitmapOrg.getWidth();
        int[] pixels = new int[w * h];
        bitmapOrg.getPixels(pixels, 0, bitmapOrg.getWidth(), 0, 0,
                bitmapOrg.getWidth(), bitmapOrg.getHeight());


        int x1 = w;
        int y1 = h;
        int x2 = 0;
        int y2 = 0;
        for (int i = 0; i < h; i += 5)
        {
            for (int j = 0; j < w; j += 5)
            {
                int index = i * w + j;
                int r = (pixels[index] >> 16) & 0xff;
                int g = (pixels[index] >> 8) & 0xff;
                int b = pixels[index] & 0xff;
                if (r > 45 || g > 45 || b > 45)
                {
                    if (j < x1)
                    {
                        x1 = j;
                    }
                    if (j > x2)
                    {
                        x2 = j;
                    }


                    if (i < y1)
                    {
                        y1 = i;
                    }
                    if (i > y2)
                    {
                        y2 = i;
                    }
                }
            }
        }
        x2 += 1;
        y2 += 1;
        Log.d(TAG, "x1 = " + x1 + " y1 = " + y1 + " x2 = " + x2 + " y2 = "
                + y2);


        return new int[]
                { x1, y1, x2, y2 };
    }
    /**
     * 等比例缩放图片
     *
     * @param orgBitmap
     * @param ratio
     *            缩放比
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap orgBitmap, float ratio) {
        if (ratio==1.0) {
            return orgBitmap;
        }
        // 获取这个图片的宽和高
        int width = orgBitmap.getWidth();
        int height = orgBitmap.getHeight();


        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(ratio, ratio);
        Log.d(TAG , "width="+width+",height="+height + " ratio="+ratio);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, width, height, matrix, true);
        Log.d(TAG , "bitmap.width="+bitmap.getWidth()+",bitmap.height="+bitmap.getWidth());
        if(orgBitmap != null  && !orgBitmap.isRecycled()){//先判断图片是否已释放了
            orgBitmap.recycle();
        }
        // 创建新的图片
        return bitmap;
    }
    /**
     * 按指定角度旋转图片
     *
     * @param orgBitmap
     * @param angle
     *            旋转角度
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap orgBitmap, double angle) {
        // 获取这个图片的宽和高
        int width = orgBitmap.getWidth();
        System.out.println("bitmap width:" + width);
        int height = orgBitmap.getHeight();
        System.out.println("bitmap height:" + height);


        Matrix matrix = new Matrix();
        // 旋转图片 动作
        matrix.setRotate((float) angle);


        // 创建新的图片
        Bitmap bitmap = Bitmap
                .createBitmap(orgBitmap, 0, 0, width, height, matrix, true);
        //释放资源，防止内存溢出
        if(orgBitmap != null && !orgBitmap.isRecycled()){//先判断图片是否已释放了
            orgBitmap.recycle();
        }
        return bitmap;
    }
    /**
     * 按指定大小缩放图片
     *
     * @param orgBitmap
     *            缩放比
     * @return
     */
    public static Bitmap zoomPhotoBitmap(Bitmap orgBitmap, int width,int height) {

        // 获取这个图片的宽和高
        int height_bitmap = orgBitmap.getHeight();

//         计算缩放率，新尺寸除原始尺寸
//         float ratioWidth = ((float) width) / width;
        float ratioHeight = ((float)height)/ height_bitmap;
        float ratioWidth = ratioHeight;
        int Width = (int) (ratioWidth*orgBitmap.getWidth());
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(ratioWidth, ratioHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, Width, height, matrix, true);
        //释放资源，防止内存溢出
        if(orgBitmap != null  && !orgBitmap.isRecycled()){//先判断图片是否已释放了
            orgBitmap.recycle();
        }
        // 创建新的图片
        return bitmap;
    }



    //获得指定文件的byte数组
    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    //根据byte数组，生成文件
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }
}