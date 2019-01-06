package com.monjya.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.account.Account;
import com.monjya.android.account.AccountManager;
import com.monjya.android.util.StringUtils;

/**
 * Created by xmx on 2017/3/5.
 */

public class SignInActivity extends BaseActivity {

    private static final String TAG = "SignInActivity";

    private static final int RC_SIGN_UP = 1;

    private EditText etUserName;

    private EditText etPassword;

    private Button btnSignIn;

    private AccountManager.SignInCallback signInCallback;

    private int authenticationTrigger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        authenticationTrigger = intent.getIntExtra(AccountManager.AUTHENTICATION_TRIGGER, AccountManager.AUTHENTICATION_TRIGGER_BY_ACCOUNT_MANAGER);

        setContentViewAndInitCustomActionBar(R.layout.activity_sign_in).setTitle(R.string.sign_in)
            .setLeftBtnAsUpBtn(this);
        TextView rightTextBtn = getCustomActionBar().getRightTextBtn();
        rightTextBtn.setText(R.string.sign_up);
        rightTextBtn.setTextColor(Color.WHITE);
        rightTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, RC_SIGN_UP);
            }
        });

        etUserName = (EditText) findViewById(R.id.et_account);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUserName.getText().toString();
                if (StringUtils.isBlank(username)) {
                    showToast(R.string.pls_input_username);
                    return;
                }

                String password = etPassword.getText().toString();
                if (StringUtils.isBlank(password)) {
                    showToast(R.string.pls_input_password);
                    return;
                }

                signInCallback = new AccountManager.SignInCallback() {
                    @Override
                    public void onSuccess(Account account) {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();

                                onAuthenticated();
                            }
                        });
                    }

                    @Override
                    public void onError(VolleyError error, Integer errno) {
                        AccountManager.showErrorForSignIn(error, errno);

                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();
                            }
                        });
                    }
                };
                showProgressDialog();
                AccountManager.getInstance().signIn(username, password, signInCallback);
            }
        });
    }

    private void onAuthenticated() {
        if (authenticationTrigger == AccountManager.AUTHENTICATION_TRIGGER_BY_ACCOUNT_MANAGER) {
            AccountManager.getInstance().triggerAfterAuthenticatedRunnable();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_UP) {
            if (AccountManager.getInstance().isAuthenticated()) {
                onAuthenticated();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
