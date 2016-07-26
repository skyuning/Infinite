package me.skyun.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by linyun on 16/6/2.
 */

public class Utils {
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size;
    }
}
