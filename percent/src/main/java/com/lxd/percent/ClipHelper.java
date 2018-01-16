package com.lxd.percent;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.View;

/**
 * @author：LiuXiaoDong on 2018/1/15 18:20.
 */

public class ClipHelper {
    private float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    private Path mClipPath;                 // 剪裁区域路径
    private Path mStrokePath;               // 描边区域路径
    private Paint mPaint;                   // 画笔
    private boolean mRoundAsCircle = false; // 圆形
    private int mStrokeColor;               // 描边颜色
    private int mStrokeWidth;               // 描边半径
    private Region mAreaRegion;             // 内容区域
    private boolean mEnableClip;             // 是否开启裁剪，使用必须置为true,以提高性能

    public ClipHelper() {
    }

    public Region getmAreaRegion() {
        return mAreaRegion;
    }

    public boolean initClipData(TypedArray typedArray){
        mEnableClip = typedArray.getBoolean(R.styleable.PercentLayout_Layout_enable_clip, false);
        if(!mEnableClip)return false;
        mRoundAsCircle = typedArray.getBoolean(R.styleable.PercentLayout_Layout_round_as_circle, false);
        mRoundAsCircle = typedArray.getBoolean(R.styleable.PercentLayout_Layout_round_as_circle, false);
        mStrokeColor = typedArray.getColor(R.styleable.PercentLayout_Layout_layout_stroke_color, Color.WHITE);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.PercentLayout_Layout_layout_stroke_width, 0);
        int roundCorner = typedArray.getDimensionPixelSize(R.styleable.PercentLayout_Layout_round_corner, 0);
        int roundCornerTopLeft = typedArray.getDimensionPixelSize(
                R.styleable.PercentLayout_Layout_round_corner_top_left, roundCorner);
        int roundCornerTopRight = typedArray.getDimensionPixelSize(
                R.styleable.PercentLayout_Layout_round_corner_top_right, roundCorner);
        int roundCornerBottomLeft = typedArray.getDimensionPixelSize(
                R.styleable.PercentLayout_Layout_round_corner_bottom_left, roundCorner);
        int roundCornerBottomRight = typedArray.getDimensionPixelSize(
                R.styleable.PercentLayout_Layout_round_corner_bottom_right, roundCorner);

        radii[0] = roundCornerTopLeft;
        radii[1] = roundCornerTopLeft;

        radii[2] = roundCornerTopRight;
        radii[3] = roundCornerTopRight;

        radii[4] = roundCornerBottomRight;
        radii[5] = roundCornerBottomRight;

        radii[6] = roundCornerBottomLeft;
        radii[7] = roundCornerBottomLeft;

        mClipPath = new Path();
        mStrokePath = new Path();
        mAreaRegion = new Region();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        return mEnableClip;
    }

    public void onSizeChange(View view,int w, int h){
        RectF areas = new RectF();
        areas.left = view.getPaddingLeft();
        areas.top = view.getPaddingTop();
        areas.right = w - view.getPaddingRight();
        areas.bottom = h - view.getPaddingBottom();
        mClipPath.reset();
        if (mRoundAsCircle) {
            float d = areas.width() >= areas.height() ? areas.height() : areas.width();
            float r = d / 2;
            PointF center = new PointF(w / 2, h / 2);
            mClipPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            mClipPath.addRect(areas, Path.Direction.CW);
            mClipPath.addCircle(center.x, center.y, r, Path.Direction.CW);
            mStrokePath.addCircle(center.x, center.y, r, Path.Direction.CW);
        } else {
            mClipPath.setFillType(Path.FillType.EVEN_ODD);
            mClipPath.addRoundRect(areas, radii, Path.Direction.CW);
            mStrokePath.addRoundRect(areas, radii, Path.Direction.CW);
        }
        Region clip = new Region((int) areas.left, (int) areas.top,
                (int) areas.right, (int) areas.bottom);
        mAreaRegion.setPath(mStrokePath, clip);
    }

    public void dispatchDraw(Canvas canvas){
        if (mStrokeWidth > 0) {
            mPaint.setXfermode(null);
            mPaint.setStrokeWidth(mStrokeWidth * 2);
            mPaint.setColor(mStrokeColor);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mStrokePath, mPaint);
        }
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mClipPath, mPaint);
        canvas.restore();
    }
}
