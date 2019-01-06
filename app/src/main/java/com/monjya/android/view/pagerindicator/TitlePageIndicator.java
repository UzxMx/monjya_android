package com.monjya.android.view.pagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.monjya.android.R;
import com.monjya.android.util.UnitUtils;
import com.viewpagerindicator.PageIndicator;

/**
 * Created by xuemingxiang on 16/6/2.
 */
public class TitlePageIndicator extends View implements PageIndicator {

    private static final int DEFAULT_TEXT_SIZE_IN_SP = 15;

    private static final int DEFAULT_TEXT_PADDING_IN_DP = 5;

    private static final int DEFAULT_UNDERLINE_HEIGHT_IN_DP = 2;

    private static final int DEFAULT_CIRCLE_RADIUS_IN_DP = 1;

    private static final int DEFAULT_GAP_IN_DP = 50;

    private ViewPager viewPager;

    private int currentPage;

    private float positionOffset;

    private int scrollState;

    private int textSize;

    private int textPadding;

    private int textColor;

    private int textActiveColor;

    private int underlineColor;

    private int underlineHeight;

    private int circleRadius;

    private int gap;

    private String[] stringArrayAdapter;

    private Paint textPaint;

    private Paint activeTextPaint;

    private Paint underlinePaint;

    private ViewPager.OnPageChangeListener onPageChangeListener;

    public TitlePageIndicator(Context context) {
        this(context, null);
    }

    public TitlePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitlePageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitlePageIndicator, defStyleAttr, 0);
        int defaultTextSize = UnitUtils.convertSpToPixel(context, DEFAULT_TEXT_SIZE_IN_SP);
        textSize = typedArray.getDimensionPixelSize(R.styleable.TitlePageIndicator_textSize, defaultTextSize);
        int defaultTextPadding = UnitUtils.convertDpToPixel(context, DEFAULT_TEXT_PADDING_IN_DP);
        textPadding = typedArray.getDimensionPixelSize(R.styleable.TitlePageIndicator_textPadding, defaultTextPadding);

        textColor = typedArray.getColor(R.styleable.TitlePageIndicator_textColor, Color.GRAY);
        textActiveColor = typedArray.getColor(R.styleable.TitlePageIndicator_textActiveColor, Color.GREEN);

        int defaultUnderlineHeight = UnitUtils.convertDpToPixel(context, DEFAULT_UNDERLINE_HEIGHT_IN_DP);
        underlineHeight = typedArray.getDimensionPixelSize(R.styleable.TitlePageIndicator_underlineHeight, defaultUnderlineHeight);
        underlineColor = typedArray.getColor(R.styleable.TitlePageIndicator_underlineColor, Color.GREEN);

        int defaultCircleRadius = UnitUtils.convertDpToPixel(context, DEFAULT_CIRCLE_RADIUS_IN_DP);
        circleRadius = typedArray.getDimensionPixelSize(R.styleable.TitlePageIndicator_cicleRadius, defaultCircleRadius);

        int defaultGap = UnitUtils.convertDpToPixel(context, DEFAULT_GAP_IN_DP);
        gap = typedArray.getDimensionPixelSize(R.styleable.TitlePageIndicator_gap, defaultGap);

        typedArray.recycle();

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        activeTextPaint = new Paint();
        activeTextPaint.setColor(textActiveColor);
        activeTextPaint.setTextSize(textSize);

        underlinePaint = new Paint();
        underlinePaint.setStyle(Paint.Style.FILL);
        underlinePaint.setColor(underlineColor);
    }

    public void setStringArrayAdapter(String[] adapter) {
        this.stringArrayAdapter = adapter;

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }

        if (width == 0 || height == 0) {
            int totalWidth = 0;
            int totalHeight = 0;
            int maxHeight = 0;
            if (stringArrayAdapter != null && stringArrayAdapter.length > 0) {
                for (int i = 0; i < stringArrayAdapter.length; ++i) {
                    String title = stringArrayAdapter[i];
                    Rect bounds = new Rect();
                    textPaint.getTextBounds(title, 0, title.length(), bounds);
                    totalWidth += bounds.width() + textPadding * 2;
                    if (bounds.height() > maxHeight) {
                        maxHeight = bounds.height();
                    }
                }
                maxHeight += textPadding * 2;
            }
            totalWidth += gap * (stringArrayAdapter.length - 1);
            totalHeight += maxHeight + underlineHeight;

            if (width == 0) {
                width = totalWidth;
            }
            if (height == 0) {
                height = totalHeight;
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (viewPager == null) {
            return;
        }
        int count = viewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (currentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        if (stringArrayAdapter == null || stringArrayAdapter.length == 0) {
            return;
        }

        Rect[] boundsArray = new Rect[stringArrayAdapter.length];
        int totalWidth = 0;
        for (int i = 0; i < stringArrayAdapter.length; ++i) {
            String title = stringArrayAdapter[i];
            Rect bounds = new Rect();
            textPaint.getTextBounds(title, 0, title.length(), bounds);
            boundsArray[i] = bounds;
            totalWidth += bounds.width() + textPadding * 2;
        }

        totalWidth += gap * (stringArrayAdapter.length - 1);

        int left = (getWidth() - totalWidth) / 2;
        int i = 0;
        for (; i <= currentPage; ++i) {
            if (i != 0) {
                drawCircle(canvas, left, left + gap, activeTextPaint);
                left += gap;
            }
            String text = stringArrayAdapter[i];
            Rect bounds = boundsArray[i];
            left += textPadding;
            canvas.drawText(text, 0, text.length(), left, bounds.height() + textPadding, activeTextPaint);
            left += bounds.width() + textPadding;
        }

        for (; i < stringArrayAdapter.length; ++i) {
            if (i != 0) {
                drawCircle(canvas,left, left + gap, textPaint);
                left += gap;
            }
            String text = stringArrayAdapter[i];
            Rect bounds = boundsArray[i];
            left += textPadding;
            canvas.drawText(text, 0, text.length(), left, bounds.height() + textPadding, textPaint);
            left += bounds.width() + textPadding;
        }

        left = (getWidth() - totalWidth) / 2;
        for (i = 0; i < currentPage; ++i) {
            left += boundsArray[i].width() + textPadding * 2 + gap;
        }
        float underlineLeft = left + (boundsArray[currentPage].width() + textPadding * 2 + gap) * positionOffset;
        float underlineRight;
        if (currentPage < count - 1) {
            float maxRight = left + boundsArray[currentPage].width() + textPadding * 2 + gap + boundsArray[currentPage + 1].width() + textPadding * 2;
            underlineRight = underlineLeft + boundsArray[currentPage].width() + textPadding * 2;
            if (underlineRight > maxRight) {
                underlineRight = maxRight;
            }
        } else {
            underlineRight = underlineLeft + boundsArray[currentPage].width() + textPadding * 2;
        }
        canvas.drawRect(underlineLeft, getHeight() - underlineHeight, underlineRight, getHeight(),
                underlinePaint);
    }

    private void drawCircle(Canvas canvas, int left, int right, Paint paint) {
        Paint.Style oldStyle = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);

        int padding = 10;
        left += padding;
        right -= padding;

        int top = (getHeight() - underlineHeight) / 2 - circleRadius;
        int cy = top + circleRadius;
        int unitWidth = circleRadius * 2 + 6;
        int count = (right - left) / unitWidth;
        for (int i = 0; i < count; ++i) {
            canvas.drawCircle(left + unitWidth / 2, cy, circleRadius, paint);
            left += unitWidth;
        }

        paint.setStyle(oldStyle);
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if(this.viewPager != viewPager) {
            if(this.viewPager != null) {
                this.viewPager.setOnPageChangeListener(null);
            }

            if(viewPager.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            } else {
                this.viewPager = viewPager;
                this.viewPager.setOnPageChangeListener(this);
                invalidate();
            }
        }
    }

    @Override
    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if(viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        } else {
            viewPager.setCurrentItem(item);
            viewPager.setCurrentItem(item);
            currentPage = item;
            invalidate();
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPage = position;
        this.positionOffset = positionOffset;

        invalidate();
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (scrollState == 0) {
            currentPage = position;
            positionOffset = 0.0F;
            invalidate();
        }

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        scrollState = state;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
