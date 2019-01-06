package com.monjya.android.activity;

import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.monjya.android.R;
import com.monjya.android.adapter.pager.MainViewPagerAdapter;
import com.monjya.android.app.Monjya;
import com.monjya.android.util.ToastManager;
import com.monjya.android.view.CustomTabView;

/**
 * Created by xmx on 2017/2/12.
 */

public class MainActivity extends BaseActivity {

    private TabLayout mainTabLayout;

    private ViewPager mainViewPager;

    private MainViewPagerAdapter mainViewPagerAdapter;

    private long lastTimeClickBackBtn = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        mainViewPager = (ViewPager) findViewById(R.id.main_view_pager);

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), this);

        mainViewPager.setOffscreenPageLimit(5);

        mainViewPager.setAdapter(mainViewPagerAdapter);
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainViewPagerAdapter.initTabLayout(mainTabLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTimeClickBackBtn > Monjya.CLICK_BACK_BTN_INTERVAL) {
                lastTimeClickBackBtn = currentTime;
                showToast(R.string.main_activity_back_btn_hint);
                return true;
            }

            ToastManager.cancelToast();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
