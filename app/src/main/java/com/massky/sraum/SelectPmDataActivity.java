package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.text.format.Time;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.AppManager;
import com.Util.DatepicketColor;
import com.Util.LogUtil;
import com.Util.SharedPreferencesUtil;
import com.base.Basecactivity;
import com.lee.wheel.Utils;
import com.lee.wheel.WheelViewPasswordActivity;
import com.lee.wheel.widget.TosAdapterView;
import com.lee.wheel.widget.TosGallery;
import com.lee.wheel.widget.WheelView;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-11-14.
 */

public class SelectPmDataActivity extends Basecactivity {
    //    @InjectView(R.id.datePicker)
//    DatePicker datePicker;
    private int yearb, monthb, dayb;
    @InjectView(R.id.wheel1)
    WheelView wheel1;
    @InjectView(R.id.wheel2)
    WheelView wheel2;
    @InjectView(R.id.wheel3)
    WheelView wheel3;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    @InjectView(R.id.project_select)
    TextView project_select;
    int[] mData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,};
    String text_pm = "";
    private Map map_link = new HashMap();
    private String condition = "0";

    @Override
    protected int viewId() {
        return R.layout.select_pm_data;
    }

    View mDecorView = null;

    boolean mStart = false;

    private TosAdapterView.OnItemSelectedListener mListener = new TosAdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
            formatData();
        }

        @Override
        public void onNothingSelected(TosAdapterView<?> parent) {

        }
    };


    private void formatData() {
        int pos1 = wheel1.getSelectedItemPosition();
        int pos2 = wheel2.getSelectedItemPosition();
        int pos3 = wheel3.getSelectedItemPosition();
        text_pm = String.format("%d%d%d", pos1, pos2, pos3);
//        mTextView.setText(text);
        condition = "1";
    }


    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        wheel1.setScrollCycle(true);
        wheel2.setScrollCycle(true);
        wheel3.setScrollCycle(true);

        wheel1.setAdapter(new NumberAdapter());
        wheel2.setAdapter(new NumberAdapter());
        wheel3.setAdapter(new NumberAdapter());

        wheel1.setOnItemSelectedListener(mListener);
        wheel2.setOnItemSelectedListener(mListener);
        wheel3.setOnItemSelectedListener(mListener);


        formatData();

        mDecorView = getWindow().getDecorView();
//        String type = (String) getIntent().getSerializableExtra("type");
//        switch (type) {
//            case "birthday":
//                titlecen_id.setText(R.string.birth);
//                break;
//            case "select_time":
//                titlecen_id.setText("选择时间");
//                break;
//        }
        //Intent intent = getIntent();
        //String birthtime = intent.getStringExtra("birthtime");
//        DatepicketColor.setDatePickerDividerColor(datePicker);
//        Time time = new Time("GMT+8");
//        time.setToNow();
//        yearb = time.year;
//        monthb = time.month;
//        dayb = time.monthDay;
//        /*
//        * if (birthtime == null || birthtime.equals("")) {
//
//        } else {
//            yearb = Integer.parseInt(birthtime.substring(0, 4).trim());
//            monthb = Integer.parseInt(birthtime.substring(5, 7).trim());
//            dayb = Integer.parseInt(birthtime.substring(8, 10).trim());
//        }*/
//        datePicker.init(yearb, monthb, dayb, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year,
//                                      int monthOfYear, int dayOfMonth) {
//                LogUtil.i("日期选择", year + "年" + monthOfYear + "月" + dayOfMonth + "日");
//                yearb = year;
//                monthb = monthOfYear + 1;
//                dayb = dayOfMonth;
//            }
//        });
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        map_link = (Map) getIntent().getSerializableExtra("map_link");
        if (map_link == null) return;
        project_select.setText(map_link.get("name").toString());
//        setPicture(map_link.get("deviceType").toString());
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.backrela_id:
//                String month;
//                String day;
//                if (monthb < 10) {
//                    month = "0" + monthb;
//                } else {
//                    month = Integer.toString(monthb);
//                }
//                if (dayb < 10) {
//                    day = "0" + dayb;
//                } else {
//                    day = Integer.toString(dayb);
//                }
//                //String str = yearb + "-" + month + "-" + day;
//                Intent intent = new Intent();
//                intent.putExtra("birthactivity", yearb + "-" + month + "-" + day);// 把数据塞入intent里面
//                SelectPmDataActivity.this.setResult(10, intent);// 跳转回原来的activity
//                SelectPmDataActivity.this.finish();
//                break;
        switch (v.getId()) {
            case R.id.back:
                SelectPmDataActivity.this.finish();
                break;
            case R.id.next_step_txt:
//                Intent intent = null;
//                switch (again_elements[position]) {
//                    case "10":
//                        intent = new Intent(SelectSensorActivity.this,  SelectPmDataActivity.class);
//                        startActivity(intent);
//                        break;//pm2.5
//                    default:
//                        intent = new Intent(SelectSensorActivity.this,  UnderWaterActivity.class);
//                        intent.putExtra("type",(Serializable) again_elements[position]);
//                        startActivityForResult(intent, REQUEST_SENSOR);
//                        break;
                boolean add_condition = (boolean) SharedPreferencesUtil.getData(SelectPmDataActivity.this, "add_condition", false);
                Intent intent = null;
                map_link.put("condition", condition);
                map_link.put("minValue", "");
                map_link.put("maxValue", text_pm);
                if (add_condition) {
//                    AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
                    //                AppManager.getAppManager().finishActivity_current(AirLinkageControlActivity.class);
//                AppManager.getAppManager().finishActivity_current(SelectiveLinkageDeviceDetailSecondActivity.class);
                    AppManager.getAppManager().finishActivity_current(SelectSensorActivity.class);
                    AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity.class);
                    intent = new Intent(SelectPmDataActivity.this, EditLinkDeviceResultActivity.class);
                    intent.putExtra("sensor_map", (Serializable) map_link);
                    startActivity(intent);
                    SelectPmDataActivity.this.finish();

                } else {
                    intent = new Intent(SelectPmDataActivity.this,
                            SelectiveLinkageActivity.class);
                    intent.putExtra("link_map", (Serializable) map_link);
                    startActivity(intent);
                }
                break;
        }

    }

    private class NumberAdapter extends BaseAdapter {
        int mHeight = 50;

        public NumberAdapter() {
            mHeight = (int) Utils.pixelToDp(SelectPmDataActivity.this, mHeight);
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.length : 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txtView = null;

            if (null == convertView) {
                convertView = new TextView(SelectPmDataActivity.this);
                convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
                txtView = (TextView) convertView;
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                txtView.setTextColor(Color.GRAY);
                txtView.setGravity(Gravity.CENTER);
            }

            String text = String.valueOf(mData[position]);
            if (null == txtView) {
                txtView = (TextView) convertView;
            }

            txtView.setText(text);

            return convertView;
        }
    }
}
