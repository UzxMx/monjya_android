package com.monjya.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.monjya.android.R;
import com.monjya.android.app.Monjya;
import com.monjya.android.app.MonjyaWebViewClient;

/**
 * Created by xmx on 2016/12/26.
 */

public class BaseWebViewFragment extends BaseFragment {

    public static final String BUNDLE_ARG_CONTENT = "content";

    protected WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflateView(inflater, container);
        }

        webView = (WebView) view.findViewById(R.id.web_view);

        webView.setWebViewClient(new MonjyaWebViewClient(getActivity()));

        init();

        return view;
    }

    protected void init() {
        Bundle bundle = getArguments();
        String content = bundle.getString(BUNDLE_ARG_CONTENT, null);
        if (content != null) {
            Monjya.setHtmlToWebViewWithStyle(content, webView);
        }
    }

    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.activity_base_web_view, container, false);
    }
}
