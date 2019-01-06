package com.monjya.android.fragment;

import com.monjya.android.order.OrdersManager;

/**
 * Created by xmx on 2017/3/14.
 */

public class OrdersFragment extends AbstractOrdersFragment {

    @Override
    protected void load(int offset, OrdersManager.GetOrdersCallback callback) {
        OrdersManager.getInstance().getOrders(offset, false, callback);
    }
}
