package com.monjya.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.monjya.android.R;
import com.monjya.android.account.AccountManager;
import com.monjya.android.activity.BaseActivity;
import com.monjya.android.activity.OrdersActivity;
import com.monjya.android.activity.SignInActivity;
import com.monjya.android.activity.VisitorsActivity;
import com.monjya.android.dialog.TwoButtonsDialog;

/**
 * Created by xmx on 2017/2/12.
 */

public class MyFragment extends BaseFragment {

    private BaseActivity.CustomActionBar customActionBar;

    private View containerUnauthenticated;

    private View containerAuthenticated;

    private Button btnSignIn;

    private Button btnSignOut;

    private View btnMyOrders;

    private View btnVisitorInfo;

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
            view = inflater.inflate(R.layout.fragment_my, container, false);
        }

        View actionBar = view.findViewById(R.id.action_bar);
        customActionBar = new BaseActivity.CustomActionBar(actionBar);
        customActionBar.setTitle(R.string.tab_my);

        containerUnauthenticated = view.findViewById(R.id.container_unauthenticated);
        containerAuthenticated = view.findViewById(R.id.container_authenticated);
        btnSignIn = (Button) view.findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) view.findViewById(R.id.btn_sign_out);

        btnMyOrders = view.findViewById(R.id.btn_my_orders);
        btnVisitorInfo = view.findViewById(R.id.btn_visitor_info);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignInActivity.class);
                intent.putExtra(AccountManager.AUTHENTICATION_TRIGGER, AccountManager.AUTHENTICATION_TRIGGER_BY_MY_FRAGMENT);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TwoButtonsDialog dialog = new TwoButtonsDialog(getContext());
                dialog.getTvContent().setText(R.string.confirm_to_exit);
                TextView btnLeft = dialog.getBtnLeft();
                btnLeft.setText(R.string.cancel);
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                TextView btnRight = dialog.getBtnRight();
                btnRight.setText(R.string.confirm);
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AccountManager.getInstance().logout();

                        updateAuthenticationView();

                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        AccountManager.getInstance().registerListener(onAccountListener);

        updateAuthenticationView();

        btnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });

        btnVisitorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VisitorsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateAuthenticationView() {
        if (AccountManager.getInstance().isAuthenticated()) {
            containerAuthenticated.setVisibility(View.VISIBLE);
            containerUnauthenticated.setVisibility(View.GONE);
        } else {
            containerAuthenticated.setVisibility(View.GONE);
            containerUnauthenticated.setVisibility(View.VISIBLE);
        }
    }
}
