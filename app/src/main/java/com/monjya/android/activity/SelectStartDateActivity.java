package com.monjya.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.monjya.android.R;
import com.monjya.android.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xmx on 2017/3/7.
 */

public class SelectStartDateActivity extends BaseActivity {

    private static final String TAG = "SelectStartDateActivity";

    public static final String INTENT_DATE = "date";

    private CalendarView calendarView;

    private TextView btnConfirm;

    private SimpleDateFormat sdf;

    private Date selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        Intent intent = getIntent();
        String dateStr = intent.getStringExtra(INTENT_DATE);
        Date date = null;
        if (!StringUtils.isBlank(dateStr)) {
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
            }
        }

        setContentViewAndInitCustomActionBar(R.layout.activity_select_start_date).setTitle(R.string.start_date)
                .setLeftBtnAsUpBtn(this);

        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        btnConfirm = (TextView) findViewById(R.id.btn_confirm);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        calendarView.setMinDate(tomorrow.getTime());

        if (date == null) {
            calendarView.setDate(tomorrow.getTime());
            selectedDate = tomorrow;
        } else {
            calendarView.setDate(date.getTime());
            selectedDate = date;
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                try {
                    selectedDate = sdf.parse(year + "-" + (month + 1) + "-" + dayOfMonth);
                } catch (ParseException e) {
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(INTENT_DATE, sdf.format(selectedDate));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}
