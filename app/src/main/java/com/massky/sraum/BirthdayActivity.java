package com.massky.sraum;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.DatepicketColor;
import com.Util.LogUtil;
import com.base.Basecactivity;

import java.lang.reflect.Field;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-11-14.
 */

public class BirthdayActivity extends Basecactivity {
    @InjectView(R.id.datePicker)
    DatePicker datePicker;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    private int yearb, monthb, dayb;

    @Override
    protected int viewId() {
        return R.layout.birthday;
    }

    @Override
    protected void onView() {
        setDatePickerDividerColor(datePicker);
        backrela_id.setOnClickListener(this);
        String type = (String) getIntent().getSerializableExtra("type");
        String birthday = (String) getIntent().getSerializableExtra("birthday");
        switch (type) {
            case "birthday":
                titlecen_id.setText(R.string.birth);

                break;
            case "select_time":
                titlecen_id.setText("选择时间");
                break;
        }
        //Intent intent = getIntent();
        //String birthtime = intent.getStringExtra("birthtime");

        if (birthday == null) {
            initTime();
        } else {
            switch (birthday.trim()) {
                case "":
                    initTime();
                    break;
                default:
                    String[] splits = birthday.split("-");
                    yearb = Integer.parseInt(splits[0]);
                    monthb = Integer.parseInt(splits[1]) - 1;
                    dayb = Integer.parseInt(splits[2]);
                    break;
            }
        }
        /*
        * if (birthtime == null || birthtime.equals("")) {

        } else {
            yearb = Integer.parseInt(birthtime.substring(0, 4).trim());
            monthb = Integer.parseInt(birthtime.substring(5, 7).trim());
            dayb = Integer.parseInt(birthtime.substring(8, 10).trim());
        }*/
        datePicker.init(yearb, monthb, dayb, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                LogUtil.i("日期选择", year + "年" + monthOfYear + "月" + dayOfMonth + "日");
                yearb = year;
                monthb = monthOfYear + 1;
                dayb = dayOfMonth;
            }
        });
    }


    /**
     * 设置时间选择器的分割线颜色
     *
     * @param datePicker
     */
    private void setDatePickerDividerColor(DatePicker datePicker) {
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(getResources().getColor(R.color.transparent)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void initTime() {
        DatepicketColor.setDatePickerDividerColor(datePicker);
        Time time = new Time("GMT+8");
        time.setToNow();
        yearb = time.year;
        monthb = time.month;
        dayb = time.monthDay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                String month;
                String day;
                if (monthb < 10) {
                    month = "0" + monthb;
                } else {
                    month = Integer.toString(monthb);
                }
                if (dayb < 10) {
                    day = "0" + dayb;
                } else {
                    day = Integer.toString(dayb);
                }
                //String str = yearb + "-" + month + "-" + day;
                Intent intent = new Intent();
                intent.putExtra("birthactivity", yearb + "-" + month + "-" + day);// 把数据塞入intent里面
                BirthdayActivity.this.setResult(10, intent);// 跳转回原来的activity
                BirthdayActivity.this.finish();
                break;
        }
    }
}
