package com.monjya.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class VisitorsActivity extends BaseActivity {

    private static final int RC_ADD_VISITOR = 1;

    private static final int RC_EDIT_VISITOR = 2;

    private ListView listView;

    private VisitorsAdapter visitorsAdapter;

    private VisitorsManager.GetVisitorsCallback getVisitorsCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViewAndInitCustomActionBar(R.layout.activity_visitors).setTitle(R.string.visitor_info)
                .setLeftBtnAsUpBtn(this);

        TextView btnRight = getCustomActionBar().getRightTextBtn();
        btnRight.setText(R.string.add);
        btnRight.setTextColor(Color.WHITE);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitorsActivity.this, VisitorActivity.class);
                startActivityForResult(intent, RC_ADD_VISITOR);
            }
        });

        listView = (ListView) findViewById(R.id.list_view);

        showProgressDialog();
        getVisitorsCallback = new VisitorsManager.GetVisitorsCallback() {
            @Override
            public void onSuccess(final List<Visitor> visitors) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();

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
                convertView = getLayoutInflater().inflate(R.layout.item_visitor, parent, false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tvTelephone = (TextView) convertView.findViewById(R.id.tv_telephone);
            View btnEdit = convertView.findViewById(R.id.btn_edit);

            final Visitor visitor = (Visitor) getItem(position);
            tvName.setText(visitor.getName());
            if (!StringUtils.isBlank(visitor.getTelephone())) {
                tvTelephone.setVisibility(View.VISIBLE);
                tvTelephone.setText(visitor.getTelephone());
            } else {
                tvTelephone.setVisibility(View.GONE);
            }

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VisitorsActivity.this, VisitorActivity.class);
                    intent.putExtra("visitor", visitor);
                    startActivityForResult(intent, RC_EDIT_VISITOR);
                }
            });

            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_VISITOR) {
            if (resultCode == RESULT_OK) {
                Visitor visitor = (Visitor) data.getSerializableExtra("visitor");
                visitorsAdapter.addVisitor(visitor);
                visitorsAdapter.notifyDataSetChanged();
            }
            return;
        } else if (requestCode == RC_EDIT_VISITOR) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("edit", false)) {
                    Visitor visitor = (Visitor) data.getSerializableExtra("visitor");
                    visitorsAdapter.replaceVisitor(visitor);
                } else if (data.getBooleanExtra("delete", false)) {
                    Long visitorId = data.getLongExtra("visitor_id", 0);
                    visitorsAdapter.removeVisitor(visitorId);
                }
                visitorsAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
