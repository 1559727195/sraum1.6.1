package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.SelectDevTypeAdapter;
import com.adapter.SelectWifiDevAdapter;
import com.base.Basecactivity;
import com.data.User;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import java.util.HashMap;
import java.util.Map;
import butterknife.InjectView;
import static com.fragment.Mainviewpager.getDeviceId;

/**
 * Created by zhu on 2018/1/3.
 */

public class SelectControlApplianceActivity extends Basecactivity {
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.mac_wifi_dev_id)
    GridView mac_wifi_dev_id;
    @InjectView(R.id.back)
    ImageView back;

    //wifi类型
    private String[] types_wifi = { //红外转发器类型暂定为hongwai,遥控器类型暂定为yaokong
            "202", "206","204","210"
    };

    private int[] icon_wifi = {
            R.drawable.icon_type_dianshiji_90,
            R.drawable.icon_type_kongtiao_90,
            R.drawable.icon_type_touyingyi_90,
            R.drawable.icon_type_yinxiang_90
    };
    private int[] iconNam_wifi = {R.string.dianshiji, R.string.kongtiao,
    R.string.touyingyi,R.string.yinxiang};

    private SelectWifiDevAdapter adapter_wifi;
    private DialogUtil dialogUtil;

    @Override
    protected int viewId() {
        return R.layout.sel_control_appliance_act;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        dialogUtil = new DialogUtil(SelectControlApplianceActivity.this);
        adapter_wifi = new SelectWifiDevAdapter(SelectControlApplianceActivity.this, icon_wifi, iconNam_wifi);
        mac_wifi_dev_id.setAdapter(adapter_wifi);

//        mac_wifi_dev_id.setAdapter(adapter);//Wi-Fi设备

        mac_wifi_dev_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_wifi = null;
                String tid = "";
                switch (types_wifi[position]) {//DeviceType [tid=7, name=空调],DeviceType [tid=2, name=电视机]
                    case "202":
                        tid = "2";
                        break;
                    case "206":
                        tid = "7";
                        break;
                }
                intent_wifi = new Intent(SelectControlApplianceActivity.this, YKCodeAPIActivity.class);
                intent_wifi.putExtra("tid", tid);
                intent_wifi.putExtra("GizWifiDevice", getIntent().getParcelableExtra(
                        "GizWifiDevice"));
                intent_wifi.putExtra("number", getIntent().getSerializableExtra("number"));
                // intent.putExtra("number", list_hand_scene.get(position).get("number").toString())
                startActivity(intent_wifi);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                SelectControlApplianceActivity.this.finish();
                break;
        }
    }

    private void sraum_setBox_accent(final String type, final String yangshi) {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
//        String phoned = getDeviceId(SelectControlApplianceActivity.this);
        map.put("token", TokenUtil.getToken(SelectControlApplianceActivity.this));
        String boxnumber = (String) SharedPreferencesUtil.getData(SelectControlApplianceActivity.this,
                "boxnumber", "");
        map.put("boxNumber", boxnumber);
        String regId = (String) SharedPreferencesUtil.getData(SelectControlApplianceActivity.this, "regId", "");
        map.put("phoneId", regId);
        switch (yangshi) {
            case "normal":
                map.put("status", "1");//普通进入设置模式
                break;
            case "zigbee":
                map.put("status", "12");//zigbee进入设置模式
                break;
        }
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_setBox, map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_setBox_accent(type, yangshi);
                    }
                }, SelectControlApplianceActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        Intent intent_position = null;
                        switch (yangshi) {
                            case "normal":
                            case "zigbee":
                                intent_position = new Intent(SelectControlApplianceActivity.this, AddZigbeeDevActivity.class);
                                intent_position.putExtra("type", type);
                                startActivity(intent_position);
                                break;
//                            case "wifi":
//                                intent_position = new Intent(SelectZigbeeDeviceActivity.this, AddWifiDevActivity.class);
//                                intent_position.putExtra("position", position);
//                                startActivity(intent_position);
//                                break;
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void wrongBoxnumber() {
                        ToastUtil.showToast(SelectControlApplianceActivity.this,
                                "该网关不存在");
                    }
                }
        );
    }
}
