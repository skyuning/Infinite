package me.skyun.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by linyun on 16/7/30.
 */
public class MapItem {

    private int mResId;
    private Rect mRect;

    public MapItem(int resId, Rect rect) {
        mResId = resId;
        mRect = rect;
    }

    public MapItem(int resId, int width, int height) {
        mResId = resId;
        mRect = new Rect(0, 0, width, height);
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public Rect getRect() {
        return mRect;
    }

    public void setRect(Rect rect) {
        mRect = rect;
    }

    public Drawable getDrawable(Context context) {
        return context.getResources().getDrawable(mResId);
    }

    public Rect getDrawableRect(Context context) {
        Drawable drawable = getDrawable(context);
        return new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }
}
