package com.monjya.android.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.monjya.android.R;
import com.monjya.android.fragment.BaseFragment;
import com.monjya.android.view.pagerindicator.NoTransAnimCirclePageIndicator;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by xmx on 2017/2/26.
 */

public class ImageGalleryView extends FrameLayout {

    private ViewPager viewPager;

    private ImageGalleryPagerAdapter imageGalleryPagerAdapter;

    private NoTransAnimCirclePageIndicator pageIndicator;

    public ImageGalleryView(Context context) {
        this(context, null);
    }

    public ImageGalleryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageGalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.view_image_gallery, this, false);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pageIndicator = (NoTransAnimCirclePageIndicator) view.findViewById(R.id.page_indicator);

        addView(view);
    }

    public void setImages(List<String> images, FragmentManager fragmentManager) {
        if (imageGalleryPagerAdapter == null) {
            imageGalleryPagerAdapter = new ImageGalleryPagerAdapter(fragmentManager);
            viewPager.setAdapter(imageGalleryPagerAdapter);
            pageIndicator.setViewPager(viewPager);
        }

        imageGalleryPagerAdapter.setImages(images);
        imageGalleryPagerAdapter.notifyDataSetChanged();
    }

    private static class ImageGalleryPagerAdapter extends FragmentPagerAdapter {

        private List<String> images;

        public ImageGalleryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public Fragment getItem(int position) {
            ImageFragment imageFragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", images.get(position));
            imageFragment.setArguments(bundle);
            return imageFragment;
        }

        @Override
        public int getCount() {
            if (images != null) {
                return images.size();
            }
            return 0;
        }
    }

    public static class ImageFragment extends BaseFragment {

        private ImageView imageView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if (view == null) {
                view = inflater.inflate(R.layout.fragment_image, container, false);
            }

            imageView = (ImageView) view.findViewById(R.id.image_view);

            Bundle bundle = getArguments();
            String url = bundle.getString("url");

            Picasso.with(getContext()).load(url).into(imageView);

            return view;
        }
    }
}
