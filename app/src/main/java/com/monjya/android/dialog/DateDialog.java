package com.monjya.android.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.monjya.android.R;
import com.monjya.android.util.UnitUtils;

import java.util.Calendar;

/**
 * Created by xuemingxiang on 16-11-14.
 */

public class DateDialog extends WheelPickerDialog {

    private Callback callback;

    private WheelDatePicker datePicker;

    public DateDialog(Context context) {
        super(context, R.layout.dialog_date);
    }

    @Override
    protected void init(Context context, View contentView) {
        datePicker = (WheelDatePicker) contentView.findViewById(R.id.date_picker);
        datePicker.setAtmospheric(true);
        datePicker.setCurved(true);
        datePicker.setCyclic(true);

        Resources resources = context.getResources();
        datePicker.setItemTextColor(resources.getColor(R.color.primary_gray));
        int blackColor = resources.getColor(R.color.primary_black);
        datePicker.setSelectedItemTextColor(blackColor);
        datePicker.setItemTextSize(UnitUtils.convertSpToPixel(context, 18));

        TextView tv = datePicker.getTextViewYear();
        tv.setTextColor(blackColor);
        tv = datePicker.getTextViewMonth();
        tv.setTextColor(blackColor);
        tv = datePicker.getTextViewDay();
        tv.setTextColor(blackColor);

        datePicker.setYearStart(2000);
        Calendar calendar = Calendar.getInstance();
        datePicker.setYearEnd(calendar.get(Calendar.YEAR));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onCancel() {
        if (callback != null) {
            callback.onCancel(this);
        }
    }

    @Override
    protected void onConfirm() {
        if (callback != null) {
            int year = datePicker.getCurrentYear();
            int month = datePicker.getCurrentMonth();
            int day = datePicker.getCurrentDay();
            callback.onConfirm(this, year, month, day);
        }
    }

    public void setDate(int year, int month, int day) {
        datePicker.setSelectedYear(year);
        datePicker.setSelectedMonth(month);
        datePicker.setSelectedDay(day);
    }

    public interface Callback {
        void onConfirm(DateDialog dialog, int year, int month, int day);
        void onCancel(DateDialog dialog);
    }
}
