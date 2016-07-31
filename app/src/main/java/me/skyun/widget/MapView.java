package me.skyun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
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

    public static final int INTRINSIC_BRICK_PIXELS = 100; // 一个砖块对应的像素数

    private Matrix mMatrix = new Matrix();
    private boolean mIsEditingMap = false; // 地图编辑模式
    private Paint mFloorPaint;
    private int mTempColor = 0x88ff4080;
    private Paint mTempPaint; // 用于画一些临时的,还未固定在Map上的item
    private BitmapShader mFloorShader;
    private GestureDetector mGestureDetector;
    private Rect mBounds;
    private Scroller mScroller;
    private ArrayList<MapItem> mMapItems = new ArrayList<>();
    private Rect mCurBrick;
    private ArrayList<Point> mSelectedBrick = new ArrayList<>();
    private MapItem mCurItem;

    public void setMapScale(float scale, float px, float py) {
        mMatrix.setScale(scale, scale, px, py);
    }

    public void setCurItem(MapItem curItem) {
        mCurItem = curItem;
    }

    public boolean isEditingMap() {
        return mIsEditingMap;
    }

    public void setEditingMap(boolean editingMap) {
        mIsEditingMap = editingMap;
        if (!mIsEditingMap) {
            mCurBrick = null;
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
        int xSizePixel = array.getInt(R.styleable.MapView_xBricks, 0) * INTRINSIC_BRICK_PIXELS;
        int ySizePixel = array.getInt(R.styleable.MapView_yBricks, 0) * INTRINSIC_BRICK_PIXELS;
        array.recycle();

        setWillNotDraw(false);

        mBounds = new Rect(-xSizePixel, -ySizePixel, xSizePixel, ySizePixel);

        Bitmap texture = BitmapFactory.decodeResource(getContext().getResources(), shaderRes);
        mFloorShader = new BitmapShader(texture, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mFloorPaint = new Paint();
        mFloorPaint.setShader(mFloorShader);

        mTempPaint = new Paint();
        mTempPaint.setColor(mTempColor);

        mGestureDetector = new GestureDetector(getContext(), new MapGestureListener());
        mScroller = new Scroller(getContext());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.concat(mMatrix);

        Rect drawingRect = new Rect();
        getDrawingRect(drawingRect);

        // draw floor
        Rect floorRect = new Rect(drawingRect);
        if (!floorRect.intersect(mBounds)) {
            return;
        }

        canvas.drawRect(floorRect, mFloorPaint);

        // draw items
        for (MapItem item : mMapItems) {
            Rect itemRect = item.getRect();
            if (Rect.intersects(itemRect, floorRect)) { // item 在当前显示的floor范围内
                Drawable drawable = item.getDrawable(getContext());
                if (mCurBrick != null && itemRect.contains(mCurBrick)) {
                    drawable.setColorFilter(mTempColor, PorterDuff.Mode.LIGHTEN);
                    mCurBrick = itemRect;
                } else {
                    drawable.setColorFilter(null);
                }
                drawable.setBounds(itemRect);
                drawable.draw(canvas);

                getMatrix().setRotate(30, 800, 800);
                canvas.concat(getMatrix());
                drawable.draw(canvas);
            }
        }

        // draw cur item
        if (mCurItem != null) {
            Rect curItemShowingRect = new Rect(drawingRect);
            curItemShowingRect.inset(drawingRect.width() / 2, drawingRect.height() / 2);
            Rect curItemRawRect = mCurItem.getRect();
            curItemShowingRect.inset(-curItemRawRect.width() / 2, -curItemRawRect.height() / 2);
            Drawable curItemDrawable = mCurItem.getDrawable(getContext());
            curItemDrawable.setBounds(curItemShowingRect);
            curItemDrawable.draw(canvas);
        }

        // draw brick
        if (mCurBrick != null) {
            canvas.drawRect(mCurBrick, mTempPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScroller.forceFinished(true);
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private class MapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!mIsEditingMap) {
                return false;
            }
            setCurRect(e);
            return true;
        }

        private void setCurRect(MotionEvent e) {
            float[] transformedPoint = new float[]{e.getX() + getScrollX(), e.getY() + getScrollY()};
            Matrix invertMatrix = new Matrix();
            mMatrix.invert(invertMatrix);

            float[] rawPoint = new float[2];
            invertMatrix.mapPoints(rawPoint, transformedPoint);

            rawPoint[0] -= rawPoint[0] % INTRINSIC_BRICK_PIXELS;
            rawPoint[1] -= rawPoint[1] % INTRINSIC_BRICK_PIXELS;
            if (rawPoint[0] < 0) {
                rawPoint[0] -= INTRINSIC_BRICK_PIXELS;
            }
            if (rawPoint[1] < 0) {
                rawPoint[1] -= INTRINSIC_BRICK_PIXELS;
            }
            mCurBrick = new Rect(0, 0, INTRINSIC_BRICK_PIXELS, INTRINSIC_BRICK_PIXELS);
            mCurBrick.offset((int) rawPoint[0], (int) rawPoint[1]);

            playSoundEffect(SoundEffectConstants.CLICK);

            invalidate();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            RectF transformedBound = new RectF(mBounds);
            mMatrix.mapRect(transformedBound);

            Rect drawingRect = new Rect();
            getDrawingRect(drawingRect);
            drawingRect.offset((int) distanceX, (int) distanceY);

            // 先简单处理,只要滚动后Map的中心点还在transformedBounds之内,就还可以滚动,否则就不能滚动了
            if (transformedBound.contains(drawingRect.centerX(), drawingRect.centerY())) {
                scrollBy((int) distanceX, (int) distanceY);
                return true;
            }
            return false;
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
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        int x = mScroller.getCurrX();
        int y = mScroller.getCurrY();

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

