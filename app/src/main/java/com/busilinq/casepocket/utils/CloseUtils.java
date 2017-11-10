package com.busilinq.casepocket.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/17
 * author: shiquan.lu
 */

public class CloseUtils {

    private CloseUtils(){};

    public static void closeQuietly(Closeable... closeables){
        if(null != closeables){
            for (Closeable c: closeables) {
                try {
                    if(c != null){
                        c.close();
                        c = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
