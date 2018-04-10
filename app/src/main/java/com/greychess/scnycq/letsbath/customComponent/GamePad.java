package com.greychess.scnycq.letsbath.customComponent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GamePad extends View {
    private int width = 320;    //控件宽
    private int height = 320;   //控件高
    private Paint mPaint;   //画笔
    private float bigCircleX = 160; //大圆在 x 轴的坐标
    private float bigCircleY = 160; //大圆在 y 轴的坐标
    private float smallCircleX = 160;   //小圆在 x 轴的坐标
    private float smallCircleY = 160;   //小圆在 y 轴的坐标
    private float bigCircleR = 120; //大圆的半径
    private float smallCircleR = 40;    //小圆的半径
    private OnDirectionListener mOnDirectionListener;   //移动方向监听器

    public void setOnDirectionListener(OnDirectionListener onDirectionListener) {
        this.mOnDirectionListener = onDirectionListener;
    }

    public GamePad(Context context) {
        this(context, null);
    }

    public GamePad(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //新建画笔
        mPaint = new Paint();
        //设置画笔粗细
        mPaint.setStrokeWidth(2);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置画笔样式
        mPaint.setStyle(Paint.Style.STROKE);
        //设置画笔颜色
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画大圆
        canvas.drawCircle(bigCircleX, bigCircleY, bigCircleR, mPaint);
        //画小圆
        canvas.drawCircle(smallCircleX, smallCircleY, smallCircleR, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            //记录触摸的位置,根据这个位置来重绘小圆
            if (motionEvent.getX() > width / 2 && motionEvent.getY() < height / 2) {    //第一象限
                if (Math.sqrt(Math.pow(motionEvent.getX() - width / 2, 2) + Math.pow(height / 2 - motionEvent.getY(), 2)) > bigCircleR) {   //圆外
                    double sin = (height / 2 - motionEvent.getY()) / Math.sqrt(Math.pow(motionEvent.getX() - width / 2, 2) + Math.pow(height / 2 - motionEvent.getY(), 2));
                    double cos = (motionEvent.getX() - width / 2) / Math.sqrt(Math.pow(motionEvent.getX() - width / 2, 2) + Math.pow(height / 2 - motionEvent.getY(), 2));
                    smallCircleX = (float) (cos * bigCircleR + width / 2);
                    smallCircleY = (float) (height / 2 - sin * bigCircleR);
                } else {    //圆内
                    smallCircleX = motionEvent.getX();
                    smallCircleY = motionEvent.getY();
                }
            } else if (motionEvent.getX() < width / 2 && motionEvent.getY() < height / 2) { //第二象限
                if (Math.sqrt(Math.pow(width / 2 - motionEvent.getX(), 2) + Math.pow(height / 2 - motionEvent.getY(), 2)) > bigCircleR) {   //圆外
                    double sin = (height / 2 - motionEvent.getY()) / Math.sqrt(Math.pow(width / 2 - motionEvent.getX(), 2) + Math.pow(height / 2 - motionEvent.getY(), 2));
                    double cos = (width / 2 - motionEvent.getX()) / Math.sqrt(Math.pow(width / 2 - motionEvent.getX(), 2) + Math.pow(height / 2 - motionEvent.getY(), 2));
                    smallCircleX = (float) (width / 2 - cos * bigCircleR);
                    smallCircleY = (float) (height / 2 - sin * bigCircleR);
                } else {    //圆内
                    smallCircleX = motionEvent.getX();
                    smallCircleY = motionEvent.getY();
                }
            } else if (motionEvent.getX() < width / 2 && motionEvent.getY() > height / 2) { //第三象限
                if (Math.sqrt(Math.pow(width / 2 - motionEvent.getX(), 2) + Math.pow(motionEvent.getY() - height / 2, 2)) > bigCircleR) {   //圆外
                    double sin = (motionEvent.getY() - height / 2) / Math.sqrt(Math.pow(width / 2 - motionEvent.getX(), 2) + Math.pow(motionEvent.getY() - height / 2, 2));
                    double cos = (width / 2 - motionEvent.getX()) / Math.sqrt(Math.pow(width / 2 - motionEvent.getX(), 2) + Math.pow(motionEvent.getY() - height / 2, 2));
                    smallCircleX = (float) (width / 2 - cos * bigCircleR);
                    smallCircleY = (float) (height / 2 + sin * bigCircleR);
                } else {    //圆内
                    smallCircleX = motionEvent.getX();
                    smallCircleY = motionEvent.getY();
                }
            } else if (motionEvent.getX() > width / 2 && motionEvent.getY() > height / 2) { //第四象限
                if (Math.sqrt(Math.pow(motionEvent.getX() - width / 2, 2) + Math.pow(motionEvent.getY() - height / 2, 2)) > bigCircleR) {   //圆外
                    double sin = (motionEvent.getY() - height / 2) / Math.sqrt(Math.pow(motionEvent.getX() - width / 2, 2) + Math.pow(motionEvent.getY() - height / 2, 2));
                    double cos = (motionEvent.getX() - width / 2) / Math.sqrt(Math.pow(motionEvent.getX() - width / 2, 2) + Math.pow(motionEvent.getY() - height / 2, 2));
                    smallCircleX = (float) (width / 2 + cos * bigCircleR);
                    smallCircleY = (float) (height / 2 + sin * bigCircleR);
                } else {    //圆内
                    smallCircleX = motionEvent.getX();
                    smallCircleY = motionEvent.getY();
                }
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            //在手指抬起离开屏幕后将小圆的位置还原
            smallCircleX = width / 2;
            smallCircleY = height / 2;
        }
        //添加移动方向监听器
        if (mOnDirectionListener != null) {
            if (motionEvent.getY() < height / 4) {
                mOnDirectionListener.onUp();
            } else if (motionEvent.getY() > height / 4 * 3) {
                mOnDirectionListener.onDown();
            } else if (motionEvent.getX() < width / 4) {
                mOnDirectionListener.onLeft();
            } else if (motionEvent.getX() > width / 4 * 3) {
                mOnDirectionListener.onRight();
            }
        }
        //重绘
        invalidate();
        return true;
    }

    public interface OnDirectionListener {
        void onUp();

        void onDown();

        void onLeft();

        void onRight();
    }
}
