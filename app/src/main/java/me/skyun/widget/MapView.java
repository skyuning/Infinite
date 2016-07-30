package me.skyun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.Collections;

import me.skyun.infinite.R;

/**
 * Created by linyun on 16/7/29.
 */
public class MapView extends RelativeLayout {

    public static final int PIXEL_PER_BRICK = 100; // 一个砖块对应100像素

    private boolean mIsEditingMap = false; // 地图编辑模式
    private Paint mFloorPaint;
    private Paint mBrickPaint;
    private BitmapShader mFloorShader;
    private GestureDetector mGestureDetector;
    private Rect mBounds;
    private Scroller mScroller;
    private ArrayList<MapItem> mMapItems = new ArrayList<>();
    private Rect mCurRect;
    private ArrayList<Point> mSelectedBrick = new ArrayList<>();

    public boolean isEditingMap() {
        return mIsEditingMap;
    }

    public void setEditingMap(boolean editingMap) {
        mIsEditingMap = editingMap;
        if (!mIsEditingMap) {
            mCurRect = null;
        }
    }

    public void addItems(MapItem... items) {
        Collections.addAll(mMapItems, items);
    }

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
        int xSizePixel = array.getInt(R.styleable.MapView_xMeter, 0) * PIXEL_PER_BRICK;
        int ySizePixel = array.getInt(R.styleable.MapView_yMeter, 0) * PIXEL_PER_BRICK;
        array.recycle();

        setWillNotDraw(false);

        mBounds = new Rect(-xSizePixel, -ySizePixel, xSizePixel, ySizePixel);

        Bitmap texture = BitmapFactory.decodeResource(getContext().getResources(), shaderRes);
        mFloorShader = new BitmapShader(texture, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mFloorPaint = new Paint();
        mFloorPaint.setShader(mFloorShader);

        mBrickPaint = new Paint();
        mBrickPaint.setColor(0x88ff4080);

        mGestureDetector = new GestureDetector(getContext(), new MapGestureListener());
        mScroller = new Scroller(getContext());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect drawingRect = new Rect();
        getDrawingRect(drawingRect);

        int brickOffsetLeft = drawingRect.left % PIXEL_PER_BRICK;
        int brickOffsetTop = drawingRect.top % PIXEL_PER_BRICK;

        Rect showingFloorRect = new Rect(drawingRect);

        if (isEditingMap()) {
            canvas.save();
            canvas.translate(brickOffsetLeft, brickOffsetTop);
            showingFloorRect.offset(-brickOffsetLeft, -brickOffsetTop);
        }

        if (!showingFloorRect.intersect(mBounds)) {
            return;
        }

        // draw floor
        canvas.drawRect(showingFloorRect, mFloorPaint);

        // draw brick
        if (mCurRect != null) {
            canvas.drawRect(mCurRect, mBrickPaint);
        }

        // draw items
        for (MapItem item : mMapItems) {
            Rect itemRect = item.getRect();
            if (Rect.intersects(itemRect, showingFloorRect)) { // item 在当前显示的floor范围内
                Bitmap bitmap = item.getBitmap(getContext());
                Rect bitmapRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                if (mCurRect != null && itemRect.contains(mCurRect)) {
                    canvas.drawBitmap(bitmap, bitmapRect, itemRect, mBrickPaint);
                    mCurRect = itemRect;
                } else {
                    canvas.drawBitmap(bitmap, bitmapRect, itemRect, null);
                }
            }
        }

        if (isEditingMap()) {
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private class MapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onShowPress(MotionEvent e) {
            if (!mIsEditingMap) {
                return;
            }
            setCurRect(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!mIsEditingMap) {
                return false;
            }
            setCurRect(e);
            return true;
        }

        private void setCurRect(MotionEvent e) {
            int x = (int) e.getX() + getScrollX();
            int y = (int) e.getY() + getScrollY();
            x -= x % PIXEL_PER_BRICK;
            y -= y % PIXEL_PER_BRICK;
            mCurRect = new Rect(x, y, x + PIXEL_PER_BRICK, y + PIXEL_PER_BRICK);
            playSoundEffect(SoundEffectConstants.CLICK);
        }

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

