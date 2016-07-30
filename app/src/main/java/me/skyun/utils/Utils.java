package me.skyun.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

import me.skyun.infinite.global.GlobalPref;

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

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        return new Gson().fromJson(reader, clz);
    }

    public static <T> T fromGlobalPref(Context context, Class<T> clz, String key) {
        String json = GlobalPref.get(context, key);
        try {
            return fromJson(json, clz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            GlobalPref.set(context, key, null);
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            return true;
        } else {
            return false;
        }
    }
}
