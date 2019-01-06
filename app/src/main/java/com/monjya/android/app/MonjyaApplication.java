package com.monjya.android.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.monjya.android.util.LogManager;

/**
 * Created by xmx on 2017/2/12.
 */

public class MonjyaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        SDKInitializer.initialize(this);

        Monjya.getInstance().init(this);
    }
}
