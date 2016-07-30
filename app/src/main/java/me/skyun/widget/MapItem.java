package me.skyun.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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

    public Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), mResId);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        return bitmap;
    }
}
