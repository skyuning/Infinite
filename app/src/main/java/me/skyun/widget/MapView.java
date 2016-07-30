package me.skyun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import me.skyun.infinite.R;

/**
 * Created by linyun on 16/7/29.
 */
public class MapView extends RelativeLayout {

    private Paint mFloorPaint;
    private BitmapShader mFloorShader;
    private GestureDetector mGestureDetector;
    private Rect mBounds;
    private Scroller mScroller;

    public MapView(Context context) {
        super(context);
        init(null);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MapView);
        int shaderRes = array.getResourceId(R.styleable.MapView_shader, 0);
        // 一米对应100像素
        int sizeX = array.getInt(R.styleable.MapView_xMeter, 0) * 100;
        int sizeY = array.getInt(R.styleable.MapView_yMeter, 0) * 100;
        array.recycle();

        setWillNotDraw(false);

        mBounds = new Rect(-sizeX, -sizeY, sizeX, sizeY);

        Bitmap texture = BitmapFactory.decodeResource(getContext().getResources(), shaderRes);
        mFloorShader = new BitmapShader(texture, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mFloorPaint = new Paint();
        mFloorPaint.setShader(mFloorShader);

        mGestureDetector = new GestureDetector(getContext(), new MapGestureListener());
        mScroller = new Scroller(getContext());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect rect = new Rect();
        getDrawingRect(rect);
        if (rect.intersect(mBounds)) {
//        canvas.drawRect(0, 0, getWidth(), getHeight(), mFloorPaint);
            canvas.drawRect(rect, mFloorPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private class MapGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) distanceX, (int) distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Rect bound = new Rect(mBounds);
            bound.offset(-getWidth() / 2, -getHeight() / 2);
            mScroller.fling(getScrollX(), getScrollY(), (int) -velocityX, (int) -velocityY,
                    -Integer.MAX_VALUE, Integer.MAX_VALUE, -Integer.MAX_VALUE, Integer.MAX_VALUE);
            return true;
        }
    }

    @Override
    public void computeScroll() {
        int x, y;
        if (mScroller.computeScrollOffset()) {
            x = mScroller.getCurrX();
            y = mScroller.getCurrY();
        } else {
            x = getScrollX();
            y = getScrollY();
        }

        Rect scrollBound = new Rect(mBounds); // 可以scroll的范围
        scrollBound.offset(-getWidth() / 2, -getHeight() / 2);

        if (x < scrollBound.left) {
            x = scrollBound.left;
        } else if (x > scrollBound.right) {
            x = scrollBound.right;
        }
        if (y < scrollBound.top) {
            y = scrollBound.top;
        } else if (y > scrollBound.bottom) {
            y = scrollBound.bottom;
        }
        scrollTo(x, y);

        invalidate();
    }
}

