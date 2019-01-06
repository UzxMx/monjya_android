package com.monjya.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monjya.android.account.AccountManager;
import com.monjya.android.order.OrdersManager;

/**
 * Created by xmx on 2017/3/14.
 */

public class OngoingOrdersFragment extends AbstractOrdersFragment {

    @Override
    protected void load(int offset, OrdersManager.GetOrdersCallback callback) {
        OrdersManager.getInstance().getOrders(offset, true, callback);
    }
}
