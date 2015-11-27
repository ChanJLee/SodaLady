package com.os.magic.progressbar.soda.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.o.democanvas.R;
import com.os.magic.progressbar.soda.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chan on 15-11-26.
 * 橘子汽水进度条
 * 汽水小公举
 */
public class OrangeSodaProgressBar extends View {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;
    // 果粒飘动一个周期所花的时间
    private static final long TIME_FLOAT = 3000;
    // 果粒旋转一周需要的时间
    private static final long TIME_ROTATE = 2000;
    // 用于控制绘制的进度条距离左／上／下的距离
    private static final int LEFT_MARGIN = 9;
    // 用于控制绘制的进度条距离右的距离
    private static final int RIGHT_MARGIN = 25;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 由颜色的画笔
     */
    private Paint m_whitePaint, m_orangePaint;
    /**
     * 画出图片的画笔
     */
    private Paint m_bitmapPaint;
    /**
     * 当前的进度值
     */
    private int m_currentProgressPosition = 0;
    /**
     * 最大的长度
     */
    private int m_maxLength = 100;
    /**
     * 当前处理到的部分
     */
    private int m_progress = 0;
    /**
     * 左右边距
     */
    private int m_leftMargin, m_rightMargin;
    /**
     * 白色矩形 橙色矩形 扇形区域
     */
    private RectF m_whiteRectF, m_orangeRectF, m_arcRectF;
    /**
     * 所绘制的进度条部分的宽度
     */
    private int m_progressWidth;
    /**
     * 弧形的半径
     */
    private int m_arcRadius;

    private int m_totalWidth;
    private int m_totalHeight;

    // arc的右上角的x坐标，也是矩形x坐标的起始点
    private int m_arcRightLocation;
    /**
     * 果粒集群
     */
    private List<Pulp> m_pulps;

    private float m_floatDuration = TIME_FLOAT;
    private float m_rotateDuration = TIME_ROTATE;

    private Bitmap m_pulpBitmap;
    private Bitmap m_outRoundedBitmap;
    private int m_pulpWidth;
    private int m_pulpHeight;
    private int m_outerWidth;
    private int m_outerHeight;
    private Rect m_outerSrcRect;
    private Rect m_outerDestRect;
    private Bitmap m_orangeBitmap;
    private int m_orangeWidth, m_orangeHeight;
    private int m_orangeAngle = 0;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public OrangeSodaProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrangeSodaProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OrangeSodaProgressBar(Context context, AttributeSet attrs,
                                 int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 做一些初始化工作
     *
     * @param context
     * @param attributeSet
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {

        initAttribute(context, attributeSet, defStyleAttr);
        initBase();
        initBitmap();
        initPaint();
    }

    /**
     * 初始化属性
     * @param context
     * @param attributeSet
     * @param defStyleAttr
     */
    private void initAttribute(Context context,AttributeSet attributeSet,int defStyleAttr){

        TypedArray typedArray  = context.obtainStyledAttributes(attributeSet,
                R.styleable.OrangeSodaProgressBar, defStyleAttr, 0);
        try {
            final int length = typedArray.getIndexCount();
            for(int i = 0; i < length; ++i) {
                switch (typedArray.getIndex(i)) {
                    case R.styleable.OrangeSodaProgressBar_maxLength:
                        m_maxLength = typedArray.getInt(i, 100);
                        break;
                }
            }
        } finally {
            if (typedArray != null)
                typedArray.recycle();
        }
    }

    /**
     * 初始基础部分
     */
    private void initBase() {

        Context context = getContext();

        m_leftMargin = Util.dp2Px(context, LEFT_MARGIN);
        m_rightMargin = Util.dp2Px(context, RIGHT_MARGIN);

        m_pulps = PulpFactory.newInstances(m_floatDuration);
    }

    /**
     * 初始化要显示的图片
     */
    @SuppressWarnings("deprecated")
    private void initBitmap(){

        Resources resources = getResources();

        m_pulpBitmap = ((BitmapDrawable)
                resources.getDrawable(R.drawable.pupl)).getBitmap();
        m_pulpWidth = m_pulpBitmap.getWidth();
        m_pulpHeight = m_pulpBitmap.getHeight();

        m_outRoundedBitmap = ((BitmapDrawable)
                resources.getDrawable(R.drawable.bar)).getBitmap();
        m_outerWidth = m_outRoundedBitmap.getWidth();
        m_outerHeight = m_outRoundedBitmap.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        m_orangeBitmap = BitmapFactory.decodeResource(resources, R.drawable.orange, options);
        m_orangeWidth = m_orangeBitmap.getWidth();
        m_orangeHeight = m_orangeBitmap.getHeight();
    }

    /**
     * 初始化画刷
     */
    private void initPaint(){

        m_bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);

        m_orangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_orangePaint.setColor(ORANGE_COLOR);

        m_whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_whitePaint.setColor(WHITE_COLOR);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        initSizeParameter(w, h, ow, oh);
    }

    /**
     * 当大小发生改变时 变更新一下绘制的参数
     * @param newWidth
     * @param newHeight
     * @param prevWidth
     * @param prevHeight
     */
    private void initSizeParameter(int newWidth, int newHeight, int prevWidth, int prevHeight) {

        //算出总的长度
        m_totalWidth = newWidth;
        m_totalHeight = newHeight;

        //计算出进度条实际的长度
        m_progressWidth = m_totalWidth - m_leftMargin - m_rightMargin;
        //本来是扇形的半径应该是高度的一半减去上下两个边界
        m_arcRadius = (m_totalHeight - 2 * m_leftMargin) / 2;

        //绘制图片
        m_outerSrcRect = new Rect(
                0,
                0,
                m_outerWidth,
                m_outerHeight
        );
        m_outerDestRect = new Rect(
                0,
                0,
                m_totalWidth,
                m_totalHeight
        );

        //绘制颜色
        m_whiteRectF = new RectF(
                m_leftMargin + m_currentProgressPosition,
                m_leftMargin,
                m_totalWidth - m_rightMargin,
                m_totalHeight - m_leftMargin
        );
        m_orangeRectF = new RectF(
                m_leftMargin + m_arcRadius,
                m_leftMargin,
                m_currentProgressPosition
                , m_totalHeight - m_leftMargin
        );
        m_arcRectF = new RectF(
                m_leftMargin,
                m_leftMargin,
                m_leftMargin + 2 * m_arcRadius,
                m_totalHeight - m_leftMargin
        );

        //扇形和举行的分界点
        m_arcRightLocation = m_leftMargin + m_arcRadius;

        //橘子的高度
        m_orangeBitmapSize = Math.min(Math.min(newHeight,
                m_orangeHeight), m_orangeWidth) - 20;
    }

    private int m_orangeBitmapSize;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgressBar(canvas);
        drawBackground(canvas);
        drawOrange(canvas);
        postInvalidate();
    }

    /**
     * 绘制橘子
     * @param canvas
     */
    private void drawOrange(Canvas canvas) {
        m_orangeAngle = (m_orangeAngle + 5) % 360;
        canvas.save();
        Matrix matrix = new Matrix();

        float dx = m_totalWidth - m_totalHeight / 2 - m_orangeWidth / 2;
        float dy = m_totalHeight / 2 - m_orangeHeight / 2;
        matrix.postTranslate(dx, dy);

        float px = dx + m_orangeWidth / 2;
        float py = dy + m_orangeHeight / 2;
        matrix.postRotate(m_orangeAngle, px, py);

        canvas.drawBitmap(m_orangeBitmap, matrix, m_bitmapPaint);
        canvas.restore();
    }
    /**
     * 绘制背景
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(m_outRoundedBitmap, m_outerSrcRect, m_outerDestRect, m_bitmapPaint);
    }

    //取一个不可能的值
    private float m_previousProgressPosition = -1;

    /**
     * 绘制出进度条的形状
     * @param canvas
     */
    private void drawProgressBar(Canvas canvas){

        //计算出当前的位置
        m_currentProgressPosition = m_progressWidth * m_progress /
                (m_maxLength == 0 ? 1 : m_maxLength);

        //如果超出长度 那么就是最长的长度 不能再增加
        if(m_currentProgressPosition > m_progressWidth)
            m_currentProgressPosition = m_progressWidth;

        //如果进度还是在扇形之内
        if(m_currentProgressPosition < m_arcRadius) {

            //首先要画出所有白色的部分
            canvas.drawArc(m_arcRectF, 90, 180, false, m_whitePaint);
            m_whiteRectF.left = m_arcRightLocation;
            canvas.drawRect(m_whiteRectF, m_whitePaint);

            //之后是橘色的扇形部分
            //计算出sin值
            float sinXValue = (m_arcRadius - m_currentProgressPosition) / m_arcRadius;
            float angleX = (float) Math.asin(sinXValue);
            float sweep = 2 * (90 - angleX);
            float start = 90 + angleX;
            canvas.drawArc(m_arcRectF, start, sweep, false, m_whitePaint);
        }else if(m_currentProgressPosition >= m_arcRadius) {

            //橘色扇形 橘色矩形部分
            canvas.drawArc(m_arcRectF, 90, 180, false, m_orangePaint);
            m_orangeRectF.right = m_currentProgressPosition;
            m_orangeRectF.left = m_arcRightLocation;
            canvas.drawRect(m_orangeRectF,m_orangePaint);

            //画出白色矩形部分
            m_whiteRectF.left = m_currentProgressPosition;
            canvas.drawRect(m_whiteRectF,m_whitePaint);
        }

//        //记录一下现在的长度
//        m_previousProgressPosition = m_currentProgressPosition;

        //绘制果粒
        drawPulps(canvas);
    }

    private void drawPulps(Canvas canvas) {
        final int size = m_pulps.size();
        long current = System.currentTimeMillis();

        for (int i = 0; i < size; ++i) {

            Pulp pulp = m_pulps.get(i);
            if(current < pulp.m_startTime) continue;

            calPulpParameter(pulp);

            canvas.save();

            Matrix matrix = new Matrix();

            float dx = m_leftMargin + pulp.m_x;
            float dy = m_leftMargin + pulp.m_y;
            matrix.postTranslate(dx, dy);

            float rotate = pulp.m_rotateAngle;
            float px = dx + m_pulpWidth / 2;
            float py = dy + m_pulpHeight / 2;
            matrix.postRotate(rotate, px, py);

            canvas.drawBitmap(m_pulpBitmap, matrix, m_bitmapPaint);
            canvas.restore();
        }
    }

    /**
     *
     * @param pulp 计算果粒的参数
     */
    private void calPulpParameter(Pulp pulp) {
        long currentTime = System.currentTimeMillis();
        calPulpLocation(pulp, currentTime);
        calPulpRotateAngle(pulp, currentTime);
    }

    /**
     * 计算果粒的旋转角度
     * @param pulp
     * @param currentTime
     */
    private void calPulpRotateAngle(Pulp pulp,long currentTime){

        // 通过时间关联旋转角度
        float rotateFraction = ((currentTime - pulp.m_startTime) % m_rotateDuration)
                / (float) m_rotateDuration;
        int angle = (int) (rotateFraction * 360);
        // 根据叶子旋转方向确定叶子旋转角度
        pulp.m_rotateAngle = (pulp.m_rotateOrientation == Pulp.ORIENTATION_LEFT
                ? angle : -angle);
    }

    /**
     * 计算果粒的当前位置
     * @param pulp 果粒
     * @param currentTime 当前的时间
     */
    private void calPulpLocation(Pulp pulp, long currentTime) {

        //获得时间间隔 如果还没有准备好要播放 那就不执行下面的代码
        long intervalTime = currentTime - pulp.m_startTime;
        if (intervalTime < 0) {
            return;
        }

        //如果已经超过了漂浮的时间 就重新计算一次开始的时间 以循环使用果粒
        if (intervalTime > m_floatDuration) {
            pulp.m_startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) m_floatDuration);
            return;
        }

        pulp.m_x = calPulpX(intervalTime);
        pulp.m_y = calPulpY(pulp);

        if(pulp.m_x < m_currentProgressPosition){
            pulp.m_x = m_progressWidth;
        }
    }

    /**
     * 计算果粒的x坐标
     * @param intervalTime
     * @return
     */
    private float calPulpX(long intervalTime){
        //计算完成度
        float fraction = (float) intervalTime / m_floatDuration;
        return (m_progressWidth - m_progressWidth * fraction);
    }

    /**
     * 计算果粒的y
     * @param pulp
     * @return
     */
    private float calPulpY(Pulp pulp) {

        // y = A(wx+Q)+h

        float w = (float) ((float) 2 * Math.PI / m_progressWidth);
        float a = MIDDLE_AMPLITUDE;
        switch (pulp.m_type) {
            case Pulp.TYPE_LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = MIDDLE_AMPLITUDE - AMPLITUDE_DISPARITY;
                break;
            case Pulp.TYPE_MID:
                a = MIDDLE_AMPLITUDE;
                break;
            case Pulp.TYPE_HIGH:
                // 大振幅 ＝ 中等振幅 + 振幅差
                a = MIDDLE_AMPLITUDE + AMPLITUDE_DISPARITY;
                break;
            default:
                break;
        }

        //三角函数
        return (int) (a * Math.sin(w * pulp.m_x)) + m_arcRadius * 2 / 3;
    }

    /**
     * 设置
     * @return
     */
    public int getProgress() {
        return m_progress;
    }

    public void setProgress(int progress) {
        m_progress = progress;
    }

    public int getMaxLength() {
        return m_maxLength;
    }

    public void setMaxLength(int maxLength) {
        m_maxLength = maxLength;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 飘动的橘子果粒
     */
    static final private class Pulp{
        ////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 果粒漂浮时候的幅度 分别为小中大
         */
        private static final short TYPE_LITTLE = 0x01;
        private static final short TYPE_MID = 0x02;
        private static final short TYPE_HIGH = 0x03;
        /**
         * 左旋
         */
        private static final short ORIENTATION_LEFT = 0x01;
        /**
         * 右旋
         */
        private static final short ORIENTATION_RIGHT = 0x02;
        ////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 开始的x y 坐标
         */
        private float m_x, m_y;
        /**
         * 震动的幅度
         */
        private short m_type;
        /**
         * 旋转的方向
         */
        private short m_rotateOrientation;
        /**
         * 旋转的角度
         */
        private int m_rotateAngle;
        /**
         * 开始的时间
         */
        private long m_startTime;
        ////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * @param x 果粒的坐标
         * @param y
         * @param type 飘动幅度
         *             {@link OrangeSodaProgressBar.Pulp#TYPE_HIGH}
         *             {@link OrangeSodaProgressBar.Pulp#TYPE_LITTLE}
         *             {@link OrangeSodaProgressBar.Pulp#TYPE_MID}
         * @param rotateOrientation 旋转方向
         *                          {@link OrangeSodaProgressBar.Pulp#ORIENTATION_LEFT}
         *                          {@link OrangeSodaProgressBar.Pulp#ORIENTATION_RIGHT}
         * @param rotateAngle 旋转的角度
         * @param startTime 开始的时间
         */
        public Pulp(float x, float y, short type, short rotateOrientation, int rotateAngle, long startTime) {
            m_x = x;
            m_y = y;
            m_type = type;
            m_rotateOrientation = rotateOrientation;
            m_startTime = startTime;
            m_rotateAngle = rotateAngle;
        }
    }

    /**
     * 果粒工厂
     */
    static final private class PulpFactory {
        ////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 随机数产生器
         */
        private static final Random RANDOM = new Random();
        /**
         * 默认的果粒个数
         */
        private static final short DEFAULT_SIZE = 0x0006;
        /**
         * 延迟的时间
         */
        private static int s_addTime = 0;
        ////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * 产生一个果粒
         * @param floatTime 果粒飘动的时间
         * @return
         */
        static public Pulp newInstance(float floatTime) {

            //随机产生果粒飘动的幅度
            short type = Pulp.TYPE_LITTLE;
            switch (RANDOM.nextInt(3)) {
                case 0:
                    type = Pulp.TYPE_LITTLE;
                    break;
                case 1:
                    type = Pulp.TYPE_MID;
                    break;
                case 2:
                    type = Pulp.TYPE_HIGH;
                    break;
            }

            //随机产生果粒旋转的方向
            short rotateOrientation = Pulp.ORIENTATION_LEFT;
            switch (RANDOM.nextInt(2)) {
                case 0:
                    rotateOrientation = Pulp.ORIENTATION_LEFT;
                    break;
                case 1:
                    rotateOrientation = Pulp.ORIENTATION_RIGHT;
                    break;
            }

            //旋转的角度
            int rotateAngle = RANDOM.nextInt(360);

            //播放时候延迟的时间
            s_addTime += RANDOM.nextInt((int) (Math.abs(floatTime) * 2));
            long startTime = s_addTime + System.currentTimeMillis();

            //果粒默认的起始坐标为0 0
            return new Pulp(0, 0, type, rotateOrientation, rotateAngle, startTime);
        }

        /**
         * 产生果粒集合
         * @param count 产生果粒的总个数
         * @param floatTime 果粒飘动的时间
         * @return
         */
        static public List<Pulp> newInstances(int count, float floatTime) {
            List<Pulp> pulpList = new ArrayList<>();
            for (int i = 0; i < count; ++i) {
                pulpList.add(newInstance(floatTime));
            }
            return pulpList;
        }

        /**
         * 产生默认大小的果粒集合
         * @param floatTime
         * @return
         */
        static public List<Pulp> newInstances(float floatTime) {
            List<Pulp> pulpList = new ArrayList<>();
            for (int i = 0; i < DEFAULT_SIZE; ++i) {
                pulpList.add(newInstance(floatTime));
            }
            return pulpList;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
