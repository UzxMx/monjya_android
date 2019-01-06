package com.monjya.android.util;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.monjya.android.R;

/**
 * Created by xmx on 2016/12/1.
 */

public abstract class ListViewLoadMoreWrapper {

    private static final String TAG = "ListViewLoadMoreWrapper";

    public int offset = 0;

    private boolean hasMore = true;

    private boolean loading = false;

    private ViewGroup containerViewLoading;

    private View viewLoading;

    public ListViewLoadMoreWrapper(Context context, ListView listView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.footer_load_more, listView, false);
        listView.addFooterView(view);
        containerViewLoading = (ViewGroup) view;
        viewLoading = view.findViewById(R.id.tv_loading);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!hasMore || loading) {
                    return;
                }

                if (firstVisibleItem + visibleItemCount == totalItemCount) {
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
                    viewLoading.setVisibility(View.VISIBLE);
                } else {
                    viewLoading.setVisibility(View.GONE);
                }
            }
        });

    }
}
