package com.monjya.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.monjya.android.R;
import com.monjya.android.net.RequestManager;
import com.monjya.android.util.EditTextUtils;
import com.monjya.android.util.StringUtils;
import com.monjya.android.visitor.Visitor;
import com.monjya.android.visitor.VisitorsManager;

/**
 * Created by xmx on 2017/3/8.
 */

public class VisitorActivity extends BaseActivity {

    private EditText etName;

    private EditText etTelephone;

    private Button btnSave;

    private Visitor visitor;

    private boolean addVisitor;

    private VisitorsManager.PostVisitorCallback postVisitorCallback;

    private VisitorsManager.PutVisitorCallback putVisitorCallback;

    private VisitorsManager.DeleteVisitorCallback deleteVisitorCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        visitor = (Visitor) intent.getSerializableExtra("visitor");
        addVisitor = visitor == null;

        setContentViewAndInitCustomActionBar(R.layout.activity_visitor).setLeftBtnAsUpBtn(this);
        getCustomActionBar().setTitle(addVisitor ? R.string.add_visitor : R.string.edit_visitor);
        if (!addVisitor) {
            TextView btnRight = getCustomActionBar().getRightTextBtn();
            btnRight.setTextColor(Color.WHITE);
            btnRight.setText(R.string.remove);
            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    deleteVisitorCallback = new VisitorsManager.DeleteVisitorCallback() {
                        @Override
                        public void onSuccess() {
                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();
                                    Intent data = new Intent();
                                    data.putExtra("delete", true);
                                    data.putExtra("visitor_id", visitor.getId());
                                    setResult(RESULT_OK, data);
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
                    VisitorsManager.getInstance().deleteVisitor(visitor.getId(), deleteVisitorCallback);
                }
            });
        }

        etName = (EditText) findViewById(R.id.et_name);
        etTelephone = (EditText) findViewById(R.id.et_telephone);
        btnSave = (Button) findViewById(R.id.btn_save);

        if (!addVisitor) {
            EditTextUtils.setText(etName, visitor.getName());
            EditTextUtils.setText(etTelephone, visitor.getTelephone());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                if (StringUtils.isBlank(name)) {
                    showToast(R.string.pls_input_name);
                    return;
                }

                String telephone = etTelephone.getText().toString();
                if (StringUtils.isBlank(telephone)) {
                    telephone = null;
                }

                showProgressDialog();
                if (addVisitor) {
                    postVisitorCallback = new VisitorsManager.PostVisitorCallback() {
                        @Override
                        public void onSuccess(final Visitor visitor) {
                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();

                                    Intent data = new Intent();
                                    data.putExtra("visitor", visitor);
                                    setResult(RESULT_OK, data);
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
                    VisitorsManager.getInstance().postVisitor(name, telephone, postVisitorCallback);
                } else {
                    final String finalTelephone = telephone;
                    putVisitorCallback = new VisitorsManager.PutVisitorCallback() {
                        @Override
                        public void onSuccess() {
                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();

                                    visitor.setName(name);
                                    visitor.setTelephone(finalTelephone);

                                    Intent data = new Intent();
                                    data.putExtra("edit", true);
                                    data.putExtra("visitor", visitor);
                                    setResult(RESULT_OK, data);
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
                    VisitorsManager.getInstance().putVisitor(visitor.getId(), name, telephone, putVisitorCallback);
                }
            }
        });
    }
}
