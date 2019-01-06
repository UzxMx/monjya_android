package com.monjya.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.net.RequestManager;
import com.monjya.android.order.Order;
import com.monjya.android.order.OrdersManager;
import com.monjya.android.util.ListUtils;
import com.monjya.android.util.StringUtils;
import com.monjya.android.view.SelectedVisitorsView;
import com.monjya.android.visitor.Visitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xmx on 2017/3/7.
 */

public class CreateOrderActivity extends BaseActivity {

    private static final int RC_SELECT_DATE = 1;

    private static final int RC_SELECT_VISITOR = 2;

    public static final String INTENT_UNIT_PRICE = "unit_price";

    public static final String INTENT_PRODUCT_ID = "product_id";

    private TextView tvStartDate;

    private EditText etStartPlace;

    private View btnSelectVisitor;

    private View containerNoVisitor;

    private SelectedVisitorsView selectedVisitorsView;

    private TextView tvPrice1;

    private TextView tvPrice2;

    private Button btnSubmit;

    private ArrayList<Visitor> selectedVisitors = new ArrayList<>();

    private Long productId;

    private float unitPrice;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private OrdersManager.PostOrderCallback postOrderCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        productId = intent.getLongExtra(INTENT_PRODUCT_ID, -1);
        unitPrice = intent.getFloatExtra(INTENT_UNIT_PRICE, 0);

        setContentViewAndInitCustomActionBar(R.layout.activity_create_order).setTitle(R.string.create_order)
                .setLeftBtnAsUpBtn(this);

        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        etStartPlace = (EditText) findViewById(R.id.et_start_place);
        btnSelectVisitor = findViewById(R.id.btn_select_visitor);
        containerNoVisitor = findViewById(R.id.container_no_visitor);
        selectedVisitorsView = (SelectedVisitorsView) findViewById(R.id.selected_visitors_view);
        tvPrice1 = (TextView) findViewById(R.id.tv_price_1);
        tvPrice2 = (TextView) findViewById(R.id.tv_price_2);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrderActivity.this, SelectStartDateActivity.class);
                intent.putExtra(SelectStartDateActivity.INTENT_DATE, tvStartDate.getText());
                startActivityForResult(intent, RC_SELECT_DATE);
            }
        });

        btnSelectVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrderActivity.this, SelectVisitorActivity.class);
                intent.putExtra("visitors", selectedVisitors);
                startActivityForResult(intent, RC_SELECT_VISITOR);
            }
        });

        selectedVisitorsView.setCallback(new SelectedVisitorsView.Callback() {
            @Override
            public void onRemoveVisitor(Visitor visitor) {
                selectedVisitors.remove(visitor);
                updateSelectedVisitorsView();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date startDate = null;
                try {
                    startDate = simpleDateFormat.parse(tvStartDate.getText().toString());
                } catch (ParseException e) {
                }
                if (startDate == null) {
                    showToast(R.string.pls_select_start_date);
                    return;
                }

                String startPlace = etStartPlace.getText().toString();
                if (StringUtils.isBlank(startPlace)) {
                    showToast(R.string.pls_input_start_place);
                    return;
                }

                if (ListUtils.isEmpty(selectedVisitors)) {
                    showToast(R.string.pls_select_visitor);
                    return;
                }

                List<Long> visitorIds = new ArrayList<Long>();
                for (Visitor visitor : selectedVisitors) {
                    visitorIds.add(visitor.getId());
                }

                showProgressDialog();
                postOrderCallback = new OrdersManager.PostOrderCallback() {
                    @Override
                    public void onSuccess(final Order order) {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();

                                Intent intent = new Intent(CreateOrderActivity.this, PayOrderActivity.class);
                                intent.putExtra(PayOrderActivity.INTENT_TYPE, PayOrderActivity.TYPE_CREATE_SUCCESS);
                                intent.putExtra(PayOrderActivity.INTENT_ORDER, order);
                                startActivity(intent);
                                finish();
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
                OrdersManager.getInstance().postOrder(productId, simpleDateFormat.format(startDate), startPlace, visitorIds, postOrderCallback);
            }
        });

        updatePriceView();
    }

    private void updateSelectedVisitorsView() {
        if (ListUtils.isEmpty(selectedVisitors)) {
            containerNoVisitor.setVisibility(View.VISIBLE);
            selectedVisitorsView.setVisibility(View.GONE);
        } else {
            selectedVisitorsView.setSelectedVisitors(selectedVisitors);
            containerNoVisitor.setVisibility(View.GONE);
            selectedVisitorsView.setVisibility(View.VISIBLE);
        }

        updatePriceView();
    }

    private void updatePriceView() {
        int count = selectedVisitors.size();
        tvPrice1.setText(count + " * " + unitPrice + " = ");
        tvPrice2.setText("¥" + count * unitPrice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SELECT_DATE) {
            if (resultCode == RESULT_OK) {
                tvStartDate.setText(data.getStringExtra(SelectStartDateActivity.INTENT_DATE));
                return;
            }
        } else if (requestCode == RC_SELECT_VISITOR) {
            if (resultCode == RESULT_OK) {
                selectedVisitors = (ArrayList<Visitor>) data.getSerializableExtra("visitors");
                updateSelectedVisitorsView();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
