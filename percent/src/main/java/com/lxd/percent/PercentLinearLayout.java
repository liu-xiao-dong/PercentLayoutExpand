package com.lxd.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author：LiuXiaoDong on 2018/1/15 18:20.
 */
public class PercentLinearLayout extends LinearLayout {

    private boolean mEnableClip;             // 是否开启裁剪，使用必须置为true,以提高性能
    private ClipHelper mClipHelper;

    /**
     * 纵横比测量
     */
    private final PercentAspectRatioMeasure.Spec mMeasureSpec = new PercentAspectRatioMeasure.Spec();
    private PercentLayoutHelper mPercentLayoutHelper;
    /**
     * 宽高显示比例
     */
    private float mAspectRatio = -1f;

    public PercentLinearLayout(Context context) {
        this(context,null);
    }

    public PercentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mClipHelper = new ClipHelper();
        initCustomAttrs(context, attrs);
        mPercentLayoutHelper = new PercentLayoutHelper(this);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout_Layout);
        mAspectRatio = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_selfAspectRatio,1,1,-1f);
        mEnableClip = mClipHelper.initClipData(typedArray);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPercentLayoutHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        if(mAspectRatio == -1f){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else{
            changeAspectRatio(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(mMeasureSpec.width, mMeasureSpec.height);
        }
        if (mPercentLayoutHelper.handleMeasuredStateTooSmall()) {
            if(mAspectRatio == -1f){
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            else{
                changeAspectRatio(widthMeasureSpec, heightMeasureSpec);
                super.onMeasure(mMeasureSpec.width, mMeasureSpec.height);
            }
        }
    }

    private void changeAspectRatio(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureSpec.width = widthMeasureSpec;
        mMeasureSpec.height = heightMeasureSpec;
        PercentAspectRatioMeasure.updateMeasureSpec(
                mMeasureSpec,
                mAspectRatio,
                getLayoutParams(),
                getPaddingLeft() + getPaddingRight(),
                getPaddingTop() + getPaddingBottom());
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mEnableClip)mClipHelper.onSizeChange(this,w,h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null, Canvas
                .ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        if(mEnableClip)mClipHelper.dispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mEnableClip && !mClipHelper.getmAreaRegion().contains((int) ev.getX(), (int) ev.getY())) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mPercentLayoutHelper.restoreOriginalParams();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends LinearLayout.LayoutParams
            implements PercentLayoutHelper.PercentLayoutParams {
        private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        @Override
        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            return mPercentLayoutInfo;
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

    }

}
