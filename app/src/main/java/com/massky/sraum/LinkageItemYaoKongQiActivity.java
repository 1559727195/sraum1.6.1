package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.AppManager;
import com.Util.SharedPreferencesUtil;
import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by zhu on 2018/6/19.
 */

public class LinkageItemYaoKongQiActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.project_select)
    TextView project_select;


    @InjectView(R.id.fangdao_linear)
    LinearLayout fangdao_linear;
    @InjectView(R.id.rel_fangdao_open)
    RelativeLayout rel_fangdao_open;
    @InjectView(R.id.rel_fangdao_close)
    RelativeLayout rel_fangdao_close;
    private Map device_map = new HashMap();
    private Map sensor_map = new HashMap();//传感器map


    @Override
    protected int viewId() {
        return R.layout.linkage_item_yaokongqi_lay;
    }

    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        onEvent();
        onData();
    }

    private void onData() {
        device_map = (Map) getIntent().getSerializableExtra("device_map");
        sensor_map = (Map) getIntent().getSerializableExtra("sensor_map");

    }

    private void onEvent() {
        rel_fangdao_open.setOnClickListener(this);
        rel_fangdao_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                LinkageItemYaoKongQiActivity.this.finish();
                break;
            case R.id.rel_fangdao_open:
//                intent.putExtra("device_map", (Serializable) map);
//                intent.putExtra("sensor_map", (Serializable) sensor_map);
                into_editlink_device_result_act("1", "开");
                break;
            case R.id.rel_fangdao_close:
                into_editlink_device_result_act("0", "关");
                break;
        }
    }

    private void into_editlink_device_result_act(String status, String action) {
        Intent intent;
        device_map.put("status", status);
        device_map.put("action", action);
        AppManager.getAppManager().finishActivity_current(SelectiveLinkageActivity.class);
        AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity.class);
        intent = new Intent(
                LinkageItemYaoKongQiActivity.this,
                EditLinkDeviceResultActivity.class
        );
        intent.putExtra("device_map", (Serializable) device_map);
        intent.putExtra("sensor_map", (Serializable) sensor_map);
        startActivity(intent);
        LinkageItemYaoKongQiActivity.this.finish();
    }
}
