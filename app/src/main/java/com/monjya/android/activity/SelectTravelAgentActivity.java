package com.monjya.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.account.AccountManager;
import com.monjya.android.net.RequestManager;
import com.monjya.android.travelagent.TravelAgent;
import com.monjya.android.travelagent.TravelAgentsManager;

import java.util.List;

/**
 * Created by xmx on 2017/3/7.
 */

public class SelectTravelAgentActivity extends BaseActivity {

    private ListView listView;

    private Button btnSubmit;

    private Button btnSkip;

    private TravelAgentsManager.GetTravelAgentsCallback getTravelAgentsCallback;

    private AccountManager.SetTravelAgentCallback setTravelAgentCallback;

    private TravelAgentsAdapter travelAgentsAdapter;

    private int selectedPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViewAndInitCustomActionBar(R.layout.activity_select_travel_agent).setTitle(R.string.select_travel_agent)
            .setLeftBtnAsUpBtn(this);

        listView = (ListView) findViewById(R.id.list_view);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSkip = (Button) findViewById(R.id.btn_skip);

        btnSubmit.setEnabled(false);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                setTravelAgentCallback = new AccountManager.SetTravelAgentCallback() {
                    @Override
                    public void onSuccess() {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();

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
                TravelAgent travelAgent = (TravelAgent) travelAgentsAdapter.getItem(selectedPosition);
                AccountManager.getInstance().setTravelAgent(travelAgent.getId(), setTravelAgentCallback);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                travelAgentsAdapter.notifyDataSetChanged();
                btnSubmit.setEnabled(true);
            }
        });

        showProgressDialog();
        getTravelAgentsCallback = new TravelAgentsManager.GetTravelAgentsCallback() {
            @Override
            public void onSuccess(final List<TravelAgent> travelAgents) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();

                        travelAgentsAdapter = new TravelAgentsAdapter(travelAgents);
                        listView.setAdapter(travelAgentsAdapter);
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
        TravelAgentsManager.getInstance().getTravelAgents(getTravelAgentsCallback);
    }

    private class TravelAgentsAdapter extends BaseAdapter {

        private List<TravelAgent> list;

        public TravelAgentsAdapter(List<TravelAgent> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
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
                convertView = getLayoutInflater().inflate(R.layout.item_travel_agent, parent, false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            ImageView ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);

            TravelAgent travelAgent = (TravelAgent) getItem(position);

            tvName.setText(travelAgent.getName());
            if (position == selectedPosition) {
                ivIndicator.setImageResource(R.drawable.icon_tick);
            } else {
                ivIndicator.setImageBitmap(null);
            }

            return convertView;
        }
    }
}
