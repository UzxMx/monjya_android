package com.monjya.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.mapapi.map.MapView;
import com.monjya.android.R;

/**
 * Created by xmx on 2017/3/8.
 */

public class SelectStartPlaceActivity extends BaseActivity {

    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViewAndInitCustomActionBar(R.layout.activity_select_start_place).setTitle(R.string.select_start_place)
                .setLeftBtnAsUpBtn(this);

        mapView = (MapView) findViewById(R.id.map_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }
}
