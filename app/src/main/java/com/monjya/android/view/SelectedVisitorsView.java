package com.monjya.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monjya.android.R;
import com.monjya.android.util.ListUtils;
import com.monjya.android.util.UnitUtils;
import com.monjya.android.visitor.Visitor;

import java.util.List;

/**
 * Created by xmx on 2017/3/12.
 */

public class SelectedVisitorsView extends LinearLayout {

    private Callback callback;

    public SelectedVisitorsView(Context context) {
        this(context, null);
    }

    public SelectedVisitorsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedVisitorsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setSelectedVisitors(List<Visitor> visitors) {
        removeAllViews();
        if (ListUtils.isEmpty(visitors)) {
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        int height = UnitUtils.convertDpToPixel(getContext(), 50);
        for (int i = 0; i < visitors.size(); ++i) {
            final Visitor visitor = visitors.get(i);

            View view = layoutInflater.inflate(R.layout.item_selected_visitor, this, false);
            TextView textView = (TextView) view.findViewById(R.id.text_view);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

            textView.setText(visitor.getName());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onRemoveVisitor(visitor);
                    }
                }
            });

            if (i == visitors.size() - 1) {
                view.setBackgroundResource(R.drawable.layer_container_top_bottom_border);
            } else {
                view.setBackgroundResource(R.drawable.layer_container_top_border);
            }

            addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }
    }

    public interface Callback {
        void onRemoveVisitor(Visitor visitor);
    }
}
