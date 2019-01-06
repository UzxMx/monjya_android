package com.monjya.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.account.AccountManager;
import com.monjya.android.activity.OrderDetailsActivity;
import com.monjya.android.net.RequestManager;
import com.monjya.android.order.Order;
import com.monjya.android.order.OrdersManager;
import com.monjya.android.product.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmx on 2017/3/14.
 */

public abstract class AbstractOrdersFragment extends BaseFragment {

    protected ListView listView;

    private OrdersAdapter ordersAdapter;

    protected ListViewLoadMoreWrapper listViewLoadMoreWrapper;

    private OrdersManager.Listener ordersListener = new OrdersManager.Listener() {
        @Override
        public void onOrdersChanged() {
            listViewLoadMoreWrapper.offset = 0;
            listViewLoadMoreWrapper.loadMore();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_abstract_orders, container, false);
        }

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) ordersAdapter.getItem(position);

                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                intent.putExtra("order_id", order.getId());
                startActivity(intent);
            }
        });

        initView(view);

        listViewLoadMoreWrapper = new ListViewLoadMoreWrapper(getContext(), listView);

        ordersAdapter = new OrdersAdapter();
        listView.setAdapter(ordersAdapter);

        if (AccountManager.getInstance().isAuthenticated()) {
            listViewLoadMoreWrapper.loadMore();
        }

        OrdersManager.getInstance().registerListener(ordersListener);

        return view;
    }

    protected void initView(View view) {
    }

    protected abstract void load(int offset, OrdersManager.GetOrdersCallback callback);

    private class OrdersAdapter extends BaseAdapter {

        private List<Order> orders = new ArrayList<>();

        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        public void addItems(List<Order> items) {
            if (items != null) {
                orders.addAll(items);
            }
        }

        public void removeAllItems() {
            this.orders.clear();
        }

        @Override
        public int getCount() {
            return orders == null ? 0 : orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
            }

            TextView tvOid = (TextView) convertView.findViewById(R.id.tv_oid);
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            TextView tvScene = (TextView) convertView.findViewById(R.id.tv_scene);
            TextView tvStartDate = (TextView) convertView.findViewById(R.id.tv_start_date);

            Order order = (Order) getItem(position);
            Product product = order.getProduct();
            tvOid.setText(order.getOid());
            tvStatus.setText(order.getStatusDescription(getResources()));
            tvScene.setText(product.getName());
            tvStartDate.setText(simpleDateFormat.format(order.getStartDate()));

            return convertView;
        }
    }

    public class ListViewLoadMoreWrapper extends com.monjya.android.util.ListViewLoadMoreWrapper {

        private OrdersManager.GetOrdersCallback getOrdersCallback;

        public ListViewLoadMoreWrapper(Context context, ListView listView) {
            super(context, listView);
        }

        @Override
        protected void loadMore() {
            synchronized (this) {
                if (isLoading()) {
                    return;
                }
                setLoading(true);
            }

            getOrdersCallback = new OrdersManager.GetOrdersCallback() {
                @Override
                public void onSuccess(List<Order> orders, boolean hasMore) {
                    if (offset == 0) {
                        ordersAdapter.removeAllItems();
                    }
                    ordersAdapter.addItems(orders);
                    int size = orders == null ? 0 : orders.size();
                    offset += size;

                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            ordersAdapter.notifyDataSetChanged();
                        }
                    });

                    setHasMore(hasMore, getHandler());
                    setLoading(false);
                }

                @Override
                public void onError(VolleyError error) {
                    RequestManager.showError(error);

                    setLoading(false);
                }
            };
            load(offset, getOrdersCallback);
        }
    }
}
