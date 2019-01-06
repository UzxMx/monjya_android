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

public class PayOrderActivity extends BaseActivity {

    public static final String INTENT_TYPE = "type";

    public static final int TYPE_DEFAULT = 0;

    public static final int TYPE_CREATE_SUCCESS = 1;

    public static final String INTENT_ORDER = "order";

    private int type;

    private TextView btnPayNow;

    private Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        type = intent.getIntExtra(INTENT_TYPE, TYPE_DEFAULT);
        order = (Order) intent.getSerializableExtra(INTENT_ORDER);

        setContentViewAndInitCustomActionBar(R.layout.activity_pay_order).setLeftBtnAsUpBtn(this);

        TextView tvPriceLable = (TextView) findViewById(R.id.tv_price_label);
        TextView tvPrice = (TextView) findViewById(R.id.tv_price);
        btnPayNow = (TextView) findViewById(R.id.btn_pay_now);

        switch (type) {
            case TYPE_DEFAULT:
                getCustomActionBar().setTitle(R.string.pay_order);
                tvPriceLable.setText(R.string.order_needs_to_pay);
                break;
            case TYPE_CREATE_SUCCESS:
                getCustomActionBar().setTitle(R.string.create_success);
                tvPriceLable.setText(R.string.order_created_success);
                break;
        }
        tvPrice.setText("¥" + order.getPrice());

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Intent intent = new Intent(PayOrderActivity.this, PayOrderSuccessActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onMenuBack() {
        Intent intent = new Intent(PayOrderActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
