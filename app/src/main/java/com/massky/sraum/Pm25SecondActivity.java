package com.massky.sraum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

import static com.fragment.MacFragment.ACTION_INTENT_RECEIVER_TO_SECOND_PAGE;
import static com.fragment.MacFragment.MACFRAGMENT_PM25;

/**
 * Created by zhu on 2018/1/25.
 */

public class Pm25SecondActivity extends Basecactivity {
    @InjectView(R.id.pm25)
    TextView pm25;
    //wendu
    @InjectView(R.id.wendu)
    TextView wendu;
    //shidu
    @InjectView(R.id.shidu)
    TextView shidu;
    @InjectView(R.id.back)
    ImageView back;
    private Map mapdevice = new HashMap();
    @InjectView(R.id.kongqi_zhiliang)
    TextView kongqi_zhiliang;
    private MessageReceiver mMessageReceiver;
    @InjectView(R.id.project_select)
    TextView project_select;

    @Override
    protected int viewId() {
        return R.layout.pm25_act_second;
    }

    @Override
    protected void onView() {
        registerMessageReceiver();
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        mapdevice = (Map) getIntent().getSerializableExtra("mapdevice");
        init_map();
    }

    private void init_map() {
        if (mapdevice == null) return;
        String pm = mapdevice.get("dimmer").toString();
        String name = mapdevice.get("name").toString();
        if (pm == null) return;
        if (project_select == null) return;
        project_select.setText(name);
        String temperature = mapdevice.get("temperature").toString();
        String shidu_txt = mapdevice.get("speed").toString();
        wendu.setText(temperature + "℃");
        shidu.setText(shidu_txt + "%");
        pm25.setText(pm);
        if (Integer.parseInt(pm) < 51) {
            kongqi_zhiliang.setText("优");
        } else if (Integer.parseInt(pm) > 51 && Integer.parseInt(pm) < 101) {
            kongqi_zhiliang.setText("良");
        } else if (Integer.parseInt(pm) > 101 && Integer.parseInt(pm) < 151) {
            kongqi_zhiliang.setText("轻度污染");
        } else if (Integer.parseInt(pm) > 151 && Integer.parseInt(pm) < 201) {
            kongqi_zhiliang.setText("中度污染");
        } else if (Integer.parseInt(pm) > 201 && Integer.parseInt(pm) < 301) {
            kongqi_zhiliang.setText("重度污染");
        } else if (Integer.parseInt(pm) > 301) {
            kongqi_zhiliang.setText("严重污染");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Pm25SecondActivity.this.finish();
                break;
        }
    }

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MACFRAGMENT_PM25);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(MACFRAGMENT_PM25)) {
                mapdevice = (Map) intent.getSerializableExtra("mapdevice");
                init_map();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
