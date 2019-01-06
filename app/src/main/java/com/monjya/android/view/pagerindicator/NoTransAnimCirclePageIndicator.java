package com.monjya.android.view.pagerindicator;

/**
 * @author xmx
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import static android.widget.LinearLayout.HORIZONTAL;

public class NoTransAnimCirclePageIndicator extends CirclePageIndicator {

    private static final String TAG = "NoTransAnimCirclePageIndicator";

    public NoTransAnimCirclePageIndicator(Context context) {
        super(context);
    }

    public NoTransAnimCirclePageIndicator(Context context, AttributeSet attrs,
                                          int defStyle) {
        super(context, attrs, defStyle);
    }

    public NoTransAnimCirclePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        boolean invalidate = false;
        if (position < mCurrentPage && positionOffset == 0) {
            mCurrentPage = position;
            invalidate = true;
        } else if (position > mCurrentPage) {
            mCurrentPage = position;
            invalidate = true;
        }

        if (invalidate) {
            int widthNoPadding = getWidth() - getPaddingLeft() - getPaddingRight();
            int x = (int) (mCurrentPage * 4 * mRadius - getScrollX());
            if (widthNoPadding > 0) {
                if (x < 0) {
                    mScroller.startScroll(getScrollX(), getScrollY(), x, 0);
                    invalidate();
                } else if (x + 2 * mRadius > widthNoPadding) {
                    int delta = (int) (x + 2 * mRadius - widthNoPadding);
                    mScroller.startScroll(getScrollX(), getScrollY(), delta, 0);
                }
            }
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if (mViewPager == null || mViewPager.getAdapter() == null) {
                return;
            }
            final int count = mViewPager.getAdapter().getCount();
            if (count == 0) {
                return;
            }

            if (mCurrentPage >= count) {
                setCurrentItem(count - 1);
                return;
            }

            int longSize;
            int longPaddingBefore;
            int longPaddingAfter;
            int shortPaddingBefore;
            if (mOrientation == HORIZONTAL) {
                longSize = getWidth();
                longPaddingBefore = getPaddingLeft();
                longPaddingAfter = getPaddingRight();
                shortPaddingBefore = getPaddingTop();
            } else {
                longSize = getHeight();
                longPaddingBefore = getPaddingTop();
                longPaddingAfter = getPaddingBottom();
                shortPaddingBefore = getPaddingLeft();
            }

            final float threeRadius = mRadius * 4;
            final float shortOffset = shortPaddingBefore + mRadius;
            float longOffset = longPaddingBefore + mRadius;
            float contentWidth = count * threeRadius - 2 * mRadius;
            int widthNoPadding = longSize - longPaddingBefore - longPaddingAfter;
            if (contentWidth <= widthNoPadding) {
                longOffset += (widthNoPadding - contentWidth) / 2.0f;
            }

            float dX;
            float dY;

            float pageFillRadius = mRadius;
            if (mPaintStroke.getStrokeWidth() > 0) {
                pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
            }

            //Draw stroked circles
            for (int iLoop = 0; iLoop < count; iLoop++) {
                float drawLong = longOffset + (iLoop * threeRadius);
                if (mOrientation == HORIZONTAL) {
                    dX = drawLong;
                    dY = shortOffset;
                } else {
                    dX = shortOffset;
                    dY = drawLong;
                }
                // Only paint fill if not completely transparent
                if (mPaintPageFill.getAlpha() > 0) {
                    canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
                }

                // Only paint stroke if a stroke width was non-zero
                if (pageFillRadius != mRadius) {
                    canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
                }
            }

            //Draw the filled circle according to the current scroll
            float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
            if (!mSnap) {
                cx += mPageOffset * threeRadius;
            }
            if (mOrientation == HORIZONTAL) {
                dX = longOffset + cx;
                dY = shortOffset;
            } else {
                dX = shortOffset;
                dY = longOffset + cx;
            }
            canvas.drawCircle(dX, dY, mRadius, mPaintFill);
        } catch (Throwable throwable) {
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
