package com.monjya.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.account.AccountManager;
import com.monjya.android.fragment.BaseWebViewFragment;
import com.monjya.android.net.RequestManager;
import com.monjya.android.product.Product;
import com.monjya.android.product.ProductsManager;
import com.monjya.android.util.LogManager;
import com.monjya.android.util.ScreenUtils;
import com.monjya.android.view.ImageGalleryView;
import com.monjya.android.view.StoppableScrollView;
import com.monjya.android.view.StoppableWebView;

/**
 * Created by xmx on 2017/2/19.
 */

public class ProductActivity extends BaseActivity {

    private static final String TAG = "ProductActivity";

    private View rootContainer;

    private StoppableScrollView scrollView;

    private TextView tvName;

    private TextView tvPrice;

    private TextView tvOpenTime;

    private TextView tvAddress;

    private LinearLayout containerTabLayout;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private ProductPagerAdapter productPagerAdapter;

    private View btnPlaceOrder;

    private Long productId;

    private ProductsManager.GetProductCallback getProductCallback;

    private ImageGalleryView imageGalleryView;

    private Product product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViewAndInitCustomActionBar(R.layout.activity_product).setLeftBtnAsUpBtn(this);

        Intent intent = getIntent();
        productId = intent.getLongExtra("product_id", -1);

        rootContainer = findViewById(R.id.root_container);
        scrollView = (StoppableScrollView) findViewById(R.id.scroll_view);
        imageGalleryView = (ImageGalleryView) findViewById(R.id.image_gallery_view);
        imageGalleryView.getLayoutParams().height = (int) (ScreenUtils.getScreenWidth(this) * 3.0 / 6);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvOpenTime = (TextView) findViewById(R.id.tv_open_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        containerTabLayout = (LinearLayout) findViewById(R.id.container_tab_layout);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.product_view_pager);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        rootContainer.setVisibility(View.GONE);

        ViewGroup.LayoutParams layoutParams = containerTabLayout.getLayoutParams();
        int height = ScreenUtils.getScreenSize(this).y;
        LogManager.e(TAG, "action bar size: " + getActionBarSize() + " status bar height: " + ScreenUtils.getStatusBarHeight(this));
        layoutParams.height = height - getActionBarSize() - ScreenUtils.getStatusBarHeight(this) - btnPlaceOrder.getLayoutParams().height;

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {

            private Runnable runnable;

            @Override
            public void onClick(View v) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ProductActivity.this, CreateOrderActivity.class);
                        intent.putExtra(CreateOrderActivity.INTENT_UNIT_PRICE, product.getPrice());
                        intent.putExtra(CreateOrderActivity.INTENT_PRODUCT_ID, productId);
                        startActivity(intent);
                    }
                };
                AccountManager.getInstance().executeAfterAuthenticated(ProductActivity.this, runnable);
            }
        });

        showProgressDialog();
        getProductCallback = new ProductsManager.GetProductCallback() {
            @Override
            public void onSuccess(final Product product) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();

                        rootContainer.setVisibility(View.VISIBLE);

                        ProductActivity.this.product = product;
                        getCustomActionBar().setTitle(product.getName());
                        imageGalleryView.setImages(product.getPhotos(), getSupportFragmentManager());
                        tvName.setText(product.getName());
                        tvPrice.setText("¥" + product.getPrice() + "起/人");
                        tvOpenTime.setText(product.getOpenTime());
                        tvAddress.setText(product.getAddress());

                        productPagerAdapter = new ProductPagerAdapter(getSupportFragmentManager(), product);
                        viewPager.setAdapter(productPagerAdapter);
                        tabLayout.setTabMode(TabLayout.MODE_FIXED);
                        tabLayout.setupWithViewPager(viewPager);
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
        ProductsManager.getInstance().getProduct(productId, getProductCallback);
    }

    private class ProductPagerAdapter extends FragmentPagerAdapter {

        private Product product;

        public ProductPagerAdapter(FragmentManager fm, Product product) {
            super(fm);

            this.product = product;
        }

        @Override
        public Fragment getItem(int position) {
            String content;
            switch (position) {
                case 0:
                    content = product.getDetails();
                    break;
                case 1:
                    content = product.getJourney();
                    break;
                case 2:
                default:
                    content = product.getPlayMethod();
                    break;
            }
            BaseWebViewFragment fragment = new ProductWebViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(BaseWebViewFragment.BUNDLE_ARG_CONTENT, content);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int strRes;
            switch (position) {
                case 0:
                    strRes = R.string.details;
                    break;
                case 1:
                    strRes = R.string.journey;
                    break;
                case 2:
                default:
                    strRes = R.string.play_method;
                    break;
            }
            return getResources().getString(strRes);
        }
    }

    private void onWebViewOverScroll() {
        scrollView.enableScroll();
    }

    public static class ProductWebViewFragment extends BaseWebViewFragment {

        @Override
        protected View inflateView(LayoutInflater inflater, ViewGroup container) {
            View view = inflater.inflate(R.layout.fragment_product_web_view, container, false);
            StoppableWebView webView = (StoppableWebView) view.findViewById(R.id.web_view);
            webView.setOnOverScrollTopListener(new StoppableWebView.OnOverScrollTopListener() {
                @Override
                public void onOverScroll() {
                    ProductActivity schoolActivity = (ProductActivity) getActivity();
                    schoolActivity.onWebViewOverScroll();
                }
            });
            return view;
        }
    }
}
