package com.monjya.android.util;

import android.os.Handler;
import android.view.View;

import com.monjya.android.view.CustomScrollView;

/**
 * Created by xmx on 2016/12/1.
 */

public abstract class ScrollViewLoadMoreWrapper {

    private View loadingView;

    private boolean loading = false;

    private boolean hasMore = true;

    public int offset = 0;

    public ScrollViewLoadMoreWrapper(CustomScrollView customScrollView, final View loadingView) {
        this.loadingView = loadingView;
        customScrollView.addOnScrollChangedListener(new CustomScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(CustomScrollView scrollView, int l, int t, int oldl, int oldt) {
                if (!hasMore || loading) {
                    return;
                }
                if (t + scrollView.getHeight() > loadingView.getTop()) {
                    loadMore();
                }
            }
        });
    }

    protected abstract void loadMore();

    public boolean isLoading() {
        return this.loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setHasMore(final boolean hasMore, Handler handler) {
        this.hasMore = hasMore;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (hasMore) {
                    loadingView.setVisibility(View.VISIBLE);
                } else {
                    loadingView.setVisibility(View.GONE);
                }
            }
        });
    }
}
