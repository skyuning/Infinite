package me.skyun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.view.ScaleGestureDetector;
import android.view.SoundEffectConstants;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.Collections;

import me.skyun.infinite.R;
import me.skyun.utils.Utils;

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
    private GestureDetector.SimpleOnGestureListener mOnGestureListener;
    private ScaleGestureDetector mScaleGestureDetector;
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
        invalidate();
    }

    public void putCurItem() {
        if (mCurItem != null) {
            mMapItems.add(mCurItem);
            mCurItem = null;
            invalidate();
        }
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

        mOnGestureListener = new MapGestureListener();
        mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new MapScaleListener());
        mScroller = new Scroller(getContext());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.concat(mMatrix);

        // draw floor
        Rect drawingRect = new Rect();
        getDrawingRect(drawingRect);

        RectF rawShowingRect = new RectF(drawingRect);
        Utils.getInvertMatrix(mMatrix).mapRect(rawShowingRect);
        if (!rawShowingRect.intersect(new RectF(mBounds))) {
            return;
        }

        Rect rawFloorClip = new Rect();
        rawShowingRect.roundOut(rawFloorClip);
        canvas.drawRect(rawFloorClip, mFloorPaint);

        // draw items
        for (MapItem item : mMapItems) {
            Rect itemRect = item.getRect();
            if (Rect.intersects(itemRect, rawFloorClip)) { // item 在当前显示的floor范围内
                Drawable drawable = item.getDrawable(getContext());
                if (mCurBrick != null && Rect.intersects(itemRect, mCurBrick)) {
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
            RectF tempRect = new RectF(drawingRect);
            tempRect.inset(tempRect.width() / 2, tempRect.height() / 2); // 屏幕中点

            Utils.getInvertMatrix(mMatrix).mapRect(tempRect); // 当前地图中点

            Rect curItemRawRect = mCurItem.getRect();
            tempRect.inset(-curItemRawRect.width() / 2, -curItemRawRect.height() / 2); // 当前地图居中的矩形

            Point brickLeftTop = getBrickLeftTop((int) tempRect.left, (int) tempRect.top);
            tempRect.offsetTo(brickLeftTop.x, brickLeftTop.y); // 要显示的位置

            Drawable curItemDrawable = mCurItem.getDrawable(getContext());
            curItemDrawable.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
            tempRect.roundOut(mCurItem.getRect());
            curItemDrawable.setBounds(mCurItem.getRect());
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
        mScaleGestureDetector.onTouchEvent(event);
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
            float[] rawPoint = new float[2];
            Utils.getInvertMatrix(mMatrix).mapPoints(rawPoint, transformedPoint);

            Point brickLeftTop = getBrickLeftTop((int) rawPoint[0], (int) rawPoint[1]);

            mCurBrick = new Rect(0, 0, INTRINSIC_BRICK_PIXELS, INTRINSIC_BRICK_PIXELS);
            mCurBrick.offset(brickLeftTop.x, brickLeftTop.y);

            playSoundEffect(SoundEffectConstants.CLICK);

            invalidate();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            RectF transformedBound = new RectF(mBounds);
            mMatrix.mapRect(transformedBound);

            Rect drawingRect = new Rect();
            getDrawingRect(drawingRect);
            RectF insetDrawingRect = new RectF(drawingRect);
            insetDrawingRect.inset(300, 300);

            if (distanceX > 0 && insetDrawingRect.left + distanceX > transformedBound.right) {
                distanceX = transformedBound.right - insetDrawingRect.left;
            } else if (distanceX < 0 && insetDrawingRect.right + distanceX < transformedBound.left) {
                distanceX = transformedBound.left - insetDrawingRect.right;
            }
            if (distanceY > 0 && insetDrawingRect.top + distanceY > transformedBound.bottom) {
                distanceY = transformedBound.bottom - insetDrawingRect.top;
            } else if (distanceY < 0 && insetDrawingRect.bottom + distanceY < transformedBound.top) {
                distanceY = transformedBound.top - insetDrawingRect.bottom;
            }
            if (distanceX == 0 && distanceY == 0) {
                return false;
            } else {
                scrollBy((int) distanceX, (int) distanceY);
                return true;
            }
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

    private class MapScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float[] focusPoint =
                    new float[]{detector.getFocusX() + getScrollX(), detector.getFocusY() + getScrollY()};
            float scale = detector.getScaleFactor();
            mMatrix.postScale(scale, scale, focusPoint[0], focusPoint[1]);
            return true;
        }
    }

    @Override
    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        int dx = mScroller.getCurrX() - getScrollX();
        int dy = mScroller.getCurrY() - getScrollY();
        if (!mOnGestureListener.onScroll(null, null, dx, dy)) {
            mScroller.forceFinished(true);
        }
    }

    private Point getBrickLeftTop(int x, int y) {
        x -= x % INTRINSIC_BRICK_PIXELS;
        y -= y % INTRINSIC_BRICK_PIXELS;
        if (x < 0) {
            x -= INTRINSIC_BRICK_PIXELS;
        }
        if (y < 0) {
            y -= INTRINSIC_BRICK_PIXELS;
        }
        return new Point(x, y);
    }
}

