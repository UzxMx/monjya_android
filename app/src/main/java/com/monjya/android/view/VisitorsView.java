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
import com.monjya.android.util.StringUtils;
import com.monjya.android.util.UnitUtils;
import com.monjya.android.visitor.Visitor;

import java.util.List;

/**
 * Created by xmx on 2017/3/14.
 */

public class VisitorsView extends LinearLayout {

    public VisitorsView(Context context) {
        this(context, null);
    }

    public VisitorsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisitorsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
    }

    public void setVisitors(List<Visitor> visitors) {
        removeAllViews();
        if (ListUtils.isEmpty(visitors)) {
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        int height = UnitUtils.convertDpToPixel(getContext(), 50);
        for (int i = 0; i < visitors.size(); ++i) {
            final Visitor visitor = visitors.get(i);

            View view = layoutInflater.inflate(R.layout.item_visitor, this, false);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvTelephone = (TextView) view.findViewById(R.id.tv_telephone);
            view.findViewById(R.id.btn_edit).setVisibility(GONE);

            tvName.setText(visitor.getName());
            if (!StringUtils.isBlank(visitor.getTelephone())) {
                tvTelephone.setVisibility(View.VISIBLE);
                tvTelephone.setText(visitor.getTelephone());
            } else {
                tvTelephone.setVisibility(View.GONE);
            }

            if (i == visitors.size() - 1) {
                view.setBackgroundResource(R.drawable.layer_container_top_bottom_border);
            } else {
                view.setBackgroundResource(R.drawable.layer_container_top_border);
            }

            addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }
    }
}
