package com.monjya.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.monjya.android.R;
import com.monjya.android.order.Order;

/**
 * Created by xmx on 2017/3/12.
 */

public class PayOrderSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViewAndInitCustomActionBar(R.layout.activity_pay_order_success).setTitle(R.string.pay_success).setLeftBtnAsUpBtn(this);

        TextView btnReturnHome = (TextView) findViewById(R.id.btn_return_home);
        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuBack();
            }
        });
    }

    @Override
    protected void onMenuBack() {
        Intent intent = new Intent(PayOrderSuccessActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
