package com.monjya.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.net.RequestManager;
import com.monjya.android.util.ListUtils;
import com.monjya.android.util.StringUtils;
import com.monjya.android.visitor.Visitor;
import com.monjya.android.visitor.VisitorsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmx on 2017/3/8.
 */

public class SelectVisitorActivity extends BaseActivity {

    private static final int RC_ADD_VISITOR = 1;

    private View rootContainer;

    private ListView listView;

    private VisitorsAdapter visitorsAdapter;

    private VisitorsManager.GetVisitorsCallback getVisitorsCallback;

    private ArrayList<Visitor> selectedVisitors = new ArrayList<>();

    private TextView btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        selectedVisitors = (ArrayList<Visitor>) intent.getSerializableExtra("visitors");
        if (selectedVisitors == null) {
            selectedVisitors = new ArrayList<>();
        }

        setContentViewAndInitCustomActionBar(R.layout.activity_select_visitor).setTitle(R.string.select_visitor)
                .setLeftBtnAsUpBtn(this);

        TextView btnRight = getCustomActionBar().getRightTextBtn();
        btnRight.setText(R.string.add);
        btnRight.setTextColor(Color.WHITE);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectVisitorActivity.this, VisitorActivity.class);
                startActivityForResult(intent, RC_ADD_VISITOR);
            }
        });

        rootContainer = findViewById(R.id.root_container);
        listView = (ListView) findViewById(R.id.list_view);
        btnConfirm = (TextView) findViewById(R.id.btn_confirm);

        rootContainer.setVisibility(View.GONE);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("visitors", selectedVisitors);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Visitor visitor = (Visitor) visitorsAdapter.getItem(position);
                if (selectedVisitors.contains(visitor)) {
                    selectedVisitors.remove(visitor);
                } else {
                    selectedVisitors.add(visitor);
                }
                visitorsAdapter.notifyDataSetChanged();
            }
        });

        showProgressDialog();
        getVisitorsCallback = new VisitorsManager.GetVisitorsCallback() {
            @Override
            public void onSuccess(final List<Visitor> visitors) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();

                        rootContainer.setVisibility(View.VISIBLE);

                        visitorsAdapter = new VisitorsAdapter(visitors);
                        listView.setAdapter(visitorsAdapter);
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
        VisitorsManager.getInstance().getVisitors(getVisitorsCallback);
    }

    private class VisitorsAdapter extends BaseAdapter {

        private List<Visitor> list;

        public VisitorsAdapter(List<Visitor> visitors) {
            this.list = visitors;
        }

        public void addVisitor(Visitor visitor) {
            if (this.list == null) {
                this.list = new ArrayList<>();
            }
            this.list.add(visitor);
        }

        public void replaceVisitor(Visitor visitor) {
            if (!ListUtils.isEmpty(list)) {
                for (int i = 0; i < list.size(); ++i) {
                    Visitor v = list.get(i);
                    if (v.getId().equals(visitor.getId())) {
                        list.remove(i);
                        list.add(i, visitor);
                        break;
                    }
                }
            }
        }

        public void removeVisitor(Long visitorId) {
            if (!ListUtils.isEmpty(list)) {
                for (int i = 0; i < list.size(); ++i) {
                    Visitor v = list.get(i);
                    if (v.getId().equals(visitorId)) {
                        list.remove(i);
                        break;
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_select_visitor, parent, false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tvTelephone = (TextView) convertView.findViewById(R.id.tv_telephone);
            ImageView ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);

            final Visitor visitor = (Visitor) getItem(position);
            tvName.setText(visitor.getName());
            if (!StringUtils.isBlank(visitor.getTelephone())) {
                tvTelephone.setVisibility(View.VISIBLE);
                tvTelephone.setText(visitor.getTelephone());
            } else {
                tvTelephone.setVisibility(View.GONE);
            }

            if (selectedVisitors.contains(visitor)) {
                ivIndicator.setImageResource(R.drawable.icon_tick);
            } else {
                ivIndicator.setImageBitmap(null);
            }

            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_VISITOR) {
            if (resultCode == RESULT_OK) {
                Visitor visitor = (Visitor) data.getSerializableExtra("visitor");
                selectedVisitors.add(visitor);
                btnConfirm.performClick();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
