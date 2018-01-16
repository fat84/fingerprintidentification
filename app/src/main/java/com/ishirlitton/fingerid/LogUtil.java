package com.ishirlitton.fingerid;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * 作者：littonishir on 2017/8/22 10:14
 */
public final class LogUtil {
    /**
     * all Log print on-off
     */
    private final static boolean all = BuildConfig.DEBUG;
    /**
     * info Log print on-off
     */
    private final static boolean i = true;
    /**
     * debug Log print on-off
     */
    private final static boolean d = true;
    /**
     * err Log print on-off
     */
    private final static boolean e = true;
    /**
     * verbose Log print on-off
     */
    private final static boolean v = true;
    /**
     * warn Log print on-off
     */
    private final static boolean w = true;
    /**
     * default print tag
     */
    private final static String defaultTag = "littonishir";

    private LogUtil() {
    }

    /**
     * info Log print,default print tag
     *
     * @param msg :print message
     */
    public static void i(String msg) {
        if (all && i) {
            android.util.Log.i(defaultTag, msg);
        }
    }

    /**
     * info Log print
     *
     * @param tag :print tag
     * @param msg :print message
     */
    public static void i(String tag, String msg) {
        if (all && i) {
            android.util.Log.i(tag, msg);
        }
    }

    /**
     * debug Log print,default print tag
     *
     * @param msg :print message
     */
    public static void d(String msg) {
        if (all && d) {
            android.util.Log.d(defaultTag, msg);
        }
    }

    /**
     * debug Log print
     *
     * @param tag :print tag
     * @param msg :print message
     */
    public static void d(String tag, String msg) {
        if (all && d) {
            android.util.Log.d(tag, msg);
        }
    }

    /**
     * err Log print,default print tag
     *
     * @param msg :print message
     */
    public static void e(String msg) {
        if (all && e) {
            try {
                android.util.Log.e(defaultTag, msg);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * err Log print
     *
     * @param tag :print tag
     * @param msg :print message
     */
    public static void e(String tag, String msg) {
        if (all && e) {
            android.util.Log.e(tag, msg);
        }
    }

    /**
     * verbose Log print,default print tag
     *
     * @param msg :print message
     */
    public static void v(String msg) {
        if (all && v) {
            android.util.Log.v(defaultTag, msg);
        }
    }

    /**
     * verbose Log print
     *
     * @param tag :print tag
     * @param msg :print message
     */
    public static void v(String tag, String msg) {
        if (all && v) {
            android.util.Log.v(tag, msg);
        }
    }

    /**
     * warn Log print,default print tag
     *
     * @param msg :print message
     */
    public static void w(String msg) {
        if (all && w) {
            android.util.Log.w(defaultTag, msg);
        }
    }

    /**
     * warn Log print
     *
     * @param tag :print tag
     * @param msg :print message
     */
    public static void w(String tag, String msg) {
        if (all && w) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void showToast(final Context ctx, final String msg) {

        if (ctx != null) {

            //如果是主线程，直接弹出toast
            if ("main".equals(Thread.currentThread().getName())) {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static void showToast(final Activity ctx, final String msg) {

        if (ctx != null) {

            //如果是主线程，直接弹出toast
            if ("main".equals(Thread.currentThread().getName())) {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            } else {
                //如果不是主线程，则调用context中 runOnUIThread方法弹出toast
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

    }

}
