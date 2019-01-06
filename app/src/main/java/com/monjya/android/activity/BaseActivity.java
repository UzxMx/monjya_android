package com.monjya.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monjya.android.R;
import com.monjya.android.dialog.ProgressDialog;
import com.monjya.android.util.ToastManager;
import com.monjya.android.util.UnitUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xmx on 2017/2/12.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private CustomActionBar customActionBar;

    private Handler handler = new Handler();

    private ProgressDialog progressDialog;

    private AtomicInteger progressDialogRefCount = new AtomicInteger(0);

    private List<Runnable> executionsOnStart = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private CustomActionBar initCustomActionBar() {
        if (this.customActionBar == null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View view = getLayoutInflater().inflate(R.layout.view_action_bar, null, false);
            this.customActionBar = new CustomActionBar(view);
            getSupportActionBar().setCustomView(view);
            if (view.getParent() instanceof Toolbar) {
                Toolbar toolbar = (Toolbar) view.getParent();
                toolbar.setPadding(0, 0, 0, 0);
                toolbar.setContentInsetsAbsolute(0, 0);
            }

            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.layer_action_bar));
        }
        return this.customActionBar;
    }

    protected CustomActionBar setContentViewAndInitCustomActionBar(int res) {
        setContentView(res);
        return initCustomActionBar();
    }

    protected CustomActionBar getCustomActionBar() {
        return this.customActionBar;
    }

    public int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int actionBarSize = 0;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            actionBarSize = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        if (actionBarSize <= 0) {
            actionBarSize = UnitUtils.convertDpToPixel(this, 56);
        }
        return actionBarSize;
    }

    protected void onMenuBack() {
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onMenuBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class CustomActionBar {

        private RelativeLayout container;

        private TextView tvTitle;

        private ImageView ivLeftBtn;

        private ImageView ivRightBtn;

        private TextView tvRightBtn;

        public CustomActionBar(View view) {
            this.container = (RelativeLayout) view;
            tvTitle = (TextView) view.findViewById(R.id.custom_action_bar_title);
            ivLeftBtn = (ImageView) view.findViewById(R.id.custom_action_bar_left_btn);
        }

        public CustomActionBar setTitle(String title) {
            tvTitle.setText(title);
            return this;
        }

        public CustomActionBar setTitle(int res) {
            tvTitle.setText(res);
            return this;
        }

        public RelativeLayout getContainer() {
            return this.container;
        }

        public TextView getTitleTextView() {
            return this.tvTitle;
        }

        public CustomActionBar setLeftButton(int imgRes, View.OnClickListener listener) {
            ivLeftBtn.setImageResource(imgRes);
            ivLeftBtn.setOnClickListener(listener);
            return this;
        }

        public CustomActionBar setRightButton(int imgRes, View.OnClickListener listener) {
            ensureRightImageBtnExisted();
            ivRightBtn.setImageResource(imgRes);
            ivRightBtn.setOnClickListener(listener);
            return this;
        }

        public void setRightButtonVisibility(int visibility) {
            ivRightBtn.setVisibility(visibility);
        }

        public CustomActionBar setRightButtonRes(int imgRes) {
            ensureRightImageBtnExisted();
            ivRightBtn.setImageResource(imgRes);
            return this;
        }

        private void ensureRightImageBtnExisted() {
            if (ivRightBtn == null) {
                Context context = container.getContext();
                ivRightBtn = new ImageView(context);
                int dist = context.getResources().getDimensionPixelSize(R.dimen.dist_n);
                ivRightBtn.setPadding(dist, 0, dist, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                container.addView(ivRightBtn, layoutParams);
            }
        }

        public TextView getRightTextBtn() {
            if (tvRightBtn == null) {
                Context context = container.getContext();
                tvRightBtn = new TextView(context);
                tvRightBtn.setGravity(Gravity.CENTER);
                int dist = context.getResources().getDimensionPixelSize(R.dimen.dist_n);
                tvRightBtn.setPadding(dist, 0, dist, 0);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                container.addView(tvRightBtn, layoutParams);
            }
            return tvRightBtn;
        }

        public CustomActionBar setLeftBtnAsCloseBtn(BaseActivity activity) {
            final WeakReference<BaseActivity> activityWeakReference = new WeakReference<BaseActivity>(activity);

            setLeftButton(R.drawable.icon_close, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseActivity a = activityWeakReference.get();
                    if (a != null) {
                        a.onMenuBack();
                    }
                }
            });
            return this;
        }

        public CustomActionBar setLeftBtnAsUpBtn(BaseActivity activity) {
            final WeakReference<BaseActivity> activityWeakReference = new WeakReference<BaseActivity>(activity);

            setLeftButton(R.drawable.icon_back, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseActivity a = activityWeakReference.get();
                    if (a != null) {
                        a.onMenuBack();
                    }
                }
            });
            return this;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showToast(int strRes, Integer gravity, int duration) {
        ToastManager.showToast(this, strRes, gravity, duration);
    }

    public void showToast(int strRes) {
        showToast(strRes, null, Toast.LENGTH_SHORT);
    }

    public void showToast(String str, Integer gravity, int duration) {
        ToastManager.showToast(this, str, gravity, duration);
    }

    public void showToast(String str) {
        showToast(str, null, Toast.LENGTH_SHORT);
    }

    public Handler getHandler() {
        return handler;
    }

    public void showProgressDialog() {
        if (progressDialogRefCount.get() == 0) {
            if (progressDialog != null) {
                progressDialog.cancel();
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
        }
        progressDialogRefCount.incrementAndGet();
    }

    public void hideProgressDialog() {
        if (progressDialogRefCount.decrementAndGet() == 0) {
            if (progressDialog != null) {
                progressDialog.cancel();
            }
            progressDialog = null;
        }
    }

    public void addExecutionOnStart(Runnable runnable) {
        executionsOnStart.add(runnable);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Iterator<Runnable> iterator = executionsOnStart.iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            getHandler().post(runnable);
            iterator.remove();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}