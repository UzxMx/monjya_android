package com.monjya.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.account.Account;
import com.monjya.android.account.AccountManager;
import com.monjya.android.util.StringUtils;

/**
 * Created by xmx on 2017/3/5.
 */

public class SignUpActivity extends BaseActivity {

    private static final int RC_SELECT_TRAVEL_AGENT = 1;

    private EditText etUserName;

    private EditText etPassword;

    private EditText etConfirmPassword;

    private Button btnSignUp;

    private AccountManager.SignUpCallback signUpCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViewAndInitCustomActionBar(R.layout.activity_sign_up).setTitle(R.string.sign_up)
                .setLeftBtnAsUpBtn(this);

        etUserName = (EditText) findViewById(R.id.et_account);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
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

                if (!password.equals(etConfirmPassword.getText().toString())) {
                    showToast(R.string.password_mismatch);
                    return;
                }

                signUpCallback = new AccountManager.SignUpCallback() {
                    @Override
                    public void onSuccess(Account account) {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();

                                Intent intent = new Intent(SignUpActivity.this, SelectTravelAgentActivity.class);
                                startActivityForResult(intent, RC_SELECT_TRAVEL_AGENT);
                            }
                        });
                    }

                    @Override
                    public void onError(VolleyError error, Integer errno) {
                        AccountManager.showErrorForSignUp(error, errno);

                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();
                            }
                        });
                    }
                };
                showProgressDialog();
                AccountManager.getInstance().signUp(username, password, signUpCallback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SELECT_TRAVEL_AGENT) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
