package com.monjya.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.monjya.android.R;
import com.monjya.android.fragment.OrdersFragment;

/**
 * Created by xmx on 2017/3/14.
 */

public class OrdersActivity extends BaseActivity {

    private OrdersFragment ordersFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViewAndInitCustomActionBar(R.layout.activity_orders).setTitle(R.string.my_orders)
            .setLeftBtnAsUpBtn(this);

        ordersFragment = (OrdersFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_orders);
    }
}
