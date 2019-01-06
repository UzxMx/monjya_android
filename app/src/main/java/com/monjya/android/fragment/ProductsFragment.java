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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.activity.BaseActivity;
import com.monjya.android.activity.ProductActivity;
import com.monjya.android.net.RequestManager;
import com.monjya.android.product.Product;
import com.monjya.android.product.ProductsManager;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmx on 2017/2/19.
 */

public class ProductsFragment extends BaseFragment {

    private BaseActivity.CustomActionBar customActionBar;

    private ListView productsListView;

    private ProductsAdapter productsAdapter;

    private ListViewLoadMoreWrapper listViewLoadMoreWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_products, container, false);
        }

        View actionBar = view.findViewById(R.id.action_bar);
        customActionBar = new BaseActivity.CustomActionBar(actionBar);
        customActionBar.setTitle("景点");

        productsListView = (ListView) view.findViewById(R.id.list_view);
        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) productsAdapter.getItem(position);
                Intent intent = new Intent(getContext(), ProductActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }
        });

        listViewLoadMoreWrapper = new ListViewLoadMoreWrapper(getContext(), productsListView);

        productsAdapter = new ProductsAdapter(getContext());
        productsListView.setAdapter(productsAdapter);

        listViewLoadMoreWrapper.loadMore();

        return view;
    }

    private static class ProductsAdapter extends BaseAdapter {

        private WeakReference<Context> contextWeakReference;

        private List<Product> products = new ArrayList<>();

        public ProductsAdapter(Context context) {
            contextWeakReference = new WeakReference<Context>(context);
        }

        public void addItems(List<Product> items) {
            if (items != null) {
                products.addAll(items);
            }
        }

        public void removeAllItems() {
            this.products.clear();
        }

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int position) {
            return products.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Context context = contextWeakReference.get();
                if (context == null) {
                    return null;
                }
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.item_product, parent, false);
            }

            ImageView ivThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tvBriefDescription = (TextView) convertView.findViewById(R.id.tv_brief_description);
            TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            TextView tvAddress = (TextView) convertView.findViewById(R.id.tv_address);

            Product product = (Product) getItem(position);
            Picasso.with(contextWeakReference.get()).load(product.getThumbPhotoUrl()).into(ivThumb);
            tvName.setText(product.getName());
            tvBriefDescription.setText(product.getBriefDescription());
            tvPrice.setText("¥" + product.getPrice());
            tvAddress.setText(product.getAddress());

            return convertView;
        }
    }

    private class ListViewLoadMoreWrapper extends com.monjya.android.util.ListViewLoadMoreWrapper {

        private ProductsManager.GetProductsCallback getProductsCallback;

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

            getProductsCallback = new ProductsManager.GetProductsCallback() {
                @Override
                public void onSuccess(List<Product> products, boolean hasMore) {
                    if (offset == 0) {
                        productsAdapter.removeAllItems();
                    }
                    productsAdapter.addItems(products);
                    int size = products == null ? 0 : products.size();
                    offset += size;

                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            productsAdapter.notifyDataSetChanged();
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
            ProductsManager.getInstance().getProducts(offset, getProductsCallback);
        }
    }
}
