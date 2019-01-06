package com.monjya.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monjya.android.R;
import com.monjya.android.account.AccountManager;
import com.monjya.android.activity.BaseActivity;
import com.monjya.android.order.OrdersManager;

/**
 * Created by xmx on 2017/3/7.
 */

public class MainOrdersFragment extends BaseFragment {

    private BaseActivity.CustomActionBar customActionBar;

    private OngoingOrdersFragment ordersFragment;

    private Runnable pendingRunnable;

    private AccountManager.Listener onAccountListener = new AccountManager.Listener() {
        @Override
        public void onAuthenticated() {
            updateAuthenticationView();
        }

        @Override
        public void onLogout() {
            updateAuthenticationView();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_orders, container, false);
        }

        View actionBar = view.findViewById(R.id.action_bar);
        customActionBar = new BaseActivity.CustomActionBar(actionBar);
        customActionBar.setTitle(R.string.tab_order);

        AccountManager.getInstance().registerListener(onAccountListener);

        updateAuthenticationView();

        return view;
    }

    private void updateAuthenticationView() {
        Runnable runnable = null;
        if (AccountManager.getInstance().isAuthenticated()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    OngoingOrdersFragment fragment = new OngoingOrdersFragment();
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    ordersFragment = fragment;
                }
            };
        } else if (ordersFragment != null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.remove(ordersFragment);
                    transaction.commit();
                    ordersFragment = null;
                }
            };
        }

        if (runnable != null) {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                pendingRunnable = runnable;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (pendingRunnable != null) {
            pendingRunnable.run();
        }
    }
}
