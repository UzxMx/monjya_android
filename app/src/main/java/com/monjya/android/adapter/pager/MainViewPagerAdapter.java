package com.monjya.android.adapter.pager;

import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.monjya.android.R;
import com.monjya.android.activity.BaseActivity;
import com.monjya.android.fragment.MainOrdersFragment;
import com.monjya.android.fragment.MyFragment;
import com.monjya.android.fragment.ProductsFragment;
import com.monjya.android.view.CustomTabView;

import java.lang.ref.WeakReference;

/**
 * Created by xmx on 2017/2/12.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private static final int[] titles = {R.string.tab_home, R.string.tab_order, R.string.tab_my};

    private static final int[] images = {R.drawable.icon_home, R.drawable.icon_order, R.drawable.icon_my};

    private static final int[] images_active = {R.drawable.icon_home_active, R.drawable.icon_order_active, R.drawable.icon_my_active};

    private WeakReference<BaseActivity> activityWeakReference;

    public MainViewPagerAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activityWeakReference = new WeakReference<BaseActivity>(activity);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProductsFragment productsFragment = new ProductsFragment();
                return productsFragment;
            case 1:
                return new MainOrdersFragment();
            case 2:
                return new MyFragment();
        }
        return null;
    }

    public void initTabLayout(TabLayout tabLayout) {
        Resources resources = tabLayout.getContext().getResources();
        for (int i = 0; i < titles.length; ++i) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            CustomTabView tabView = new CustomTabView(tabLayout.getContext());
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, resources.getDrawable(images_active[i]));
            stateListDrawable.addState(new int[]{}, resources.getDrawable(images[i]));
            tabView.imageView.setImageDrawable(stateListDrawable);

            tabView.textView.setText(titles[i]);

            tab.setCustomView(tabView);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        BaseActivity activity = activityWeakReference.get();
        if (activity == null) {
            return null;
        }
        return activity.getResources().getString(titles[position]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
