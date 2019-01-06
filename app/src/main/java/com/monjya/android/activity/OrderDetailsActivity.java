package com.monjya.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.net.RequestManager;
import com.monjya.android.order.Order;
import com.monjya.android.order.OrdersManager;
import com.monjya.android.product.Product;
import com.monjya.android.view.VisitorsView;

import java.text.SimpleDateFormat;

/**
 * Created by xmx on 2017/3/14.
 */

public class OrderDetailsActivity extends BaseActivity {

    private Long orderId;

    private View rootContainer;

    private TextView tvOid;

    private TextView tvStatus;

    private TextView tvPrice;

    private TextView tvScene;

    private TextView tvStartDate;

    private TextView tvStartPlace;

    private VisitorsView visitorsView;

    private TextView btnPayNow;

    private Order order;

    private OrdersManager.GetOrderCallback getOrderCallback;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        orderId = intent.getLongExtra("order_id", -1);

        setContentViewAndInitCustomActionBar(R.layout.activity_order_details).setTitle(R.string.order_details)
            .setLeftBtnAsUpBtn(this);

        rootContainer = findViewById(R.id.root_container);
        tvOid = (TextView) findViewById(R.id.tv_oid);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvScene = (TextView) findViewById(R.id.tv_scene);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvStartPlace = (TextView) findViewById(R.id.tv_start_place);
        visitorsView = (VisitorsView) findViewById(R.id.visitors_view);
        btnPayNow = (TextView) findViewById(R.id.btn_pay_now);

        rootContainer.setVisibility(View.GONE);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsActivity.this, PayOrderActivity.class);
                intent.putExtra(PayOrderActivity.INTENT_TYPE, PayOrderActivity.TYPE_DEFAULT);
                intent.putExtra(PayOrderActivity.INTENT_ORDER, order);
                startActivity(intent);
            }
        });

        showProgressDialog();
        getOrderCallback = new OrdersManager.GetOrderCallback() {
            @Override
            public void onSuccess(final Order order) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();

                        rootContainer.setVisibility(View.VISIBLE);

                        OrderDetailsActivity.this.order = order;

                        Product product = order.getProduct();
                        tvOid.setText(order.getOid());
                        tvStatus.setText(order.getStatusDescription(getResources()));
                        tvPrice.setText("¥" + order.getPrice());
                        tvScene.setText(product.getName());
                        tvStartDate.setText(simpleDateFormat.format(order.getStartDate()));
                        tvStartPlace.setText(order.getStartPlace());
                        visitorsView.setVisitors(order.getVisitors());

                        String status = order.getStatus();
                        if (!Order.STATUS_NEW_CREATED.equals(status)) {
                            btnPayNow.setVisibility(View.GONE);
                        }

                        TextView tvRightBtn = getCustomActionBar().getRightTextBtn();
                        tvRightBtn.setTextColor(Color.WHITE);
                        tvRightBtn.setText(R.string.cancel_order);
                        tvRightBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(VolleyError error) {
                RequestManager.showError(error);
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });
            }
        };
        OrdersManager.getInstance().getOrder(orderId, getOrderCallback);
    }
}
