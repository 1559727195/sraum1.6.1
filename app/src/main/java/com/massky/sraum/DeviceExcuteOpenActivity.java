package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import butterknife.InjectView;
import lecho.lib.hellocharts.model.Line;

/**
 * Created by zhu on 2018/1/12.
 */

public class DeviceExcuteOpenActivity extends Basecactivity{
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.kaiguan_linear)
    LinearLayout kaiguan_linear;

    @Override
    protected int viewId() {
        return R.layout.device_execute_openact;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        String type = (String) getIntent().getSerializableExtra("type");
        switch (type) {
            case "1":
                kaiguan_linear.setVisibility(View.VISIBLE);
                break;
            case "3":

                break;
        }
    }

//    @Override
//    protected void onView() {
//        StatusUtils.setFullToStatusBar(this);  // StatusBar.
//    }
//
//    @Override
//    protected void onEvent() {
//        back.setOnClickListener(this);
//        next_step_txt.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onData() {
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                DeviceExcuteOpenActivity.this.finish();
                break;
            case R.id.next_step_txt://场景设置
                Intent intent = new Intent(DeviceExcuteOpenActivity.this,
                        SetSelectLinkActivity.class);
                intent.putExtra("excute","auto");//自动的
                startActivity(intent);
                break;
        }
    }
}
