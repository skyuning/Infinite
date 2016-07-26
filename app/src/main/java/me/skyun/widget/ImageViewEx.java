package me.skyun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import junit.framework.Assert;

import java.util.Locale;

import me.skyun.infinite.R;

/**
 * Created by linyun on 16-4-27.
 * 提供各种方便的方法，定制图片
 */
public class ImageViewEx extends ImageView {

    private static String sImageHost = "";
    private static final Xfermode XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    /**
     * 图形变换
     */
    private boolean mIsSquare = false; // 正方形，宽高相同
    private float mHWRatio = -1; // 指定宽高比，H随W变化
    private boolean mKeepDrawableRatio = false; // 正方形，宽高相同
    private boolean mIsOval = false;

    /**
     * 图片源
     */
    private int mPlaceHolderId;
    private String mRemoteUrl;

    public static void setImageHost(String imageHost) {
        sImageHost = imageHost;
    }

    public void setOval(boolean oval) {
        mIsOval = oval;
    }

    public void setSquare(boolean square) {
        mIsSquare = square;
    }

    public void setHWRatio(float HWRatio) {
        mHWRatio = HWRatio;
    }

    public void setPlaceHolderId(int placeHolderId) {
        mPlaceHolderId = placeHolderId;
        Picasso.with(getContext())
                .load(placeHolderId)
                .placeholder(placeHolderId)
                .into(this);
    }

    public void setRemoteUrl(String remoteUrl) {
        setRemoteUrl(remoteUrl, getWidth(), getHeight());
    }

    public void setRemoteUrl(String remoteUrl, int width, int height) {
        mRemoteUrl = remoteUrl;
        String scaledUrl = mRemoteUrl +
                String.format(Locale.getDefault(), "@%dw_%dh_1e_1c", width, height);

        if (!scaledUrl.startsWith("http")) {
            scaledUrl = sImageHost + scaledUrl;
        }
        Picasso.with(getContext())
                .load(Uri.parse(scaledUrl))
                .placeholder(mPlaceHolderId)
                .into(this);
    }

    public ImageViewEx(Context context) {
        super(context);
    }

    public ImageViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        parseAttributes(attrs);
    }

    public ImageViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(attrs);
    }

    public void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageViewEx);
        mIsSquare = a.getBoolean(R.styleable.ImageViewEx_square, false);
        mIsOval = a.getBoolean(R.styleable.ImageViewEx_oval, false);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height;
        if (mIsSquare) {
            height = width;
        } else if (mHWRatio != -1) {
            height = (int) (mHWRatio * width);
        } else if (mKeepDrawableRatio) {
            Drawable drawable = getDrawable();
            float ratio = drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
            height = (int) (ratio * width);
        } else {
            height = getMeasuredHeight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsOval) {
            ovalDraw(canvas);
            return;
        }
        super.onDraw(canvas);
    }

    private void ovalDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        Assert.assertTrue(getDrawable() instanceof BitmapDrawable);

        int saveFlags = Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG
                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, saveFlags);

        Drawable oval = getResources().getDrawable(R.drawable.oval);
        oval.setBounds(0, 0, getWidth(), getHeight());
        oval.draw(canvas);
        BitmapDrawable image = (BitmapDrawable) getDrawable();
        image.setBounds(0, 0, getWidth(), getHeight());
        image.getPaint().setXfermode(XFERMODE);
        image.draw(canvas);

        canvas.restore();
    }

    private float mLastTouchY = 0;
    private float mRawHeight = 0;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float curY = event.getY();
//        final ViewGroup.LayoutParams params = getLayoutParams();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mRawHeight = getHeight();
//                mLastTouchY = curY;
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                int deltaY = (int) (curY - mLastTouchY);
//                params.height += deltaY / 3;
//                setLayoutParams(params);
//                mLastTouchY = curY;
//                break;
//            case MotionEvent.ACTION_UP:
//                ValueAnimator animator = ValueAnimator.ofFloat(params.height, mRawHeight);
//                animator.setDuration(100);
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        float height = (float) animation.getAnimatedValue();
//                        params.height = (int) height;
//                        setLayoutParams(params);
//                        invalidate();
//                    }
//                });
//                animator.start();
//
//                params.height = (int) mRawHeight;
//                setLayoutParams(params);
//                invalidate();
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}

