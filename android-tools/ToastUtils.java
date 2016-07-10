package com.seeknovel.bpmcsps.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * <p/>
 * Created by imtianx on 2016-5-29.
 */
public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(Context mContext, String id) {
        if (toast == null) {
            toast = Toast.makeText(mContext, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }
}
