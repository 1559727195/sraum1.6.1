package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import com.yaokan.sdk.utils.Logger;
import com.yaokan.sdk.utils.Utility;
import com.yaokan.sdk.wifi.DeviceManager;
import com.yaokan.sdk.wifi.GizWifiCallBack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by zhu on 2018/1/3.
 */

public class SelectZigbeeDeviceActivity extends Basecactivity {
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.macfragritview_id)
    GridView macfragritview_id;
    private SelectDevTypeAdapter adapter;
    @InjectView(R.id.mac_wifi_dev_id)
    GridView mac_wifi_dev_id;
    @InjectView(R.id.back)
    ImageView back;
    private int[] icon = {
            R.drawable.icon_type_yijiandk_90, R.drawable.icon_type_liangjiandk_90,
            R.drawable.icon_type_sanjiandk_90, R.drawable.icon_type_sijiandk_90, R.drawable.icon_type_yilutiaoguang_90,
            R.drawable.icon_type_lianglutiaoguang_90, R.drawable.icon_type_sanlutiaoguang_90, R.drawable.icon_type_chuanglianmb_90,
            R.drawable.icon_type_menci_90, R.drawable.icon_type_rentiganying_90,
            R.drawable.icon_type_toa_90, R.drawable.icon_type_yanwucgq_90, R.drawable.icon_type_tianranqibjq_90,
            R.drawable.icon_type_jinjianniu_90, R.drawable.icon_type_zhinengmensuo_90, R.drawable.icon_type_pm25,
            R.drawable.icon_type_shuijin, R.drawable.icon_type_duogongneng,R.drawable.icon_kaiguan_socket_90
    };

    //"B301"暂时为多功能模块
    private String[] types = {
            "A201", "A202", "A203", "A204", "A301", "A302", "A303", "A401",
            "A801", "A901", "A902", "AB01", "AB04", "B001", "B201", "AD01", "AC01", "B301", "B101"
    };

    //        //type：设备类型，1-灯，2-调光，3-空调，4-窗帘，5-新风，6-地暖,7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手

    //wifi类型
    private String[] types_wifi = { //红外转发器类型暂定为hongwai,遥控器类型暂定为yaokong
            "hongwai", "yaokong", "101", "103"
    };

    private int[] icon_wifi = {
            R.drawable.hongwai_s,
            R.drawable.icon_type_yaokongqi,
            R.drawable.icon_type_shexiangtou_90,
            // R.drawable.icon_type_pmmofang_90,
            R.drawable.icon_keshimenling

    };
    private int[] iconNam_wifi = {R.string.hongwai, R.string.yaokongqi, R.string.shexiangtou, R.string.keshimenling};//, R.string.pm_mofang
    private int[] iconName = {R.string.yijianlight, R.string.liangjianlight, R.string.sanjianlight, R.string.sijianlight,
            R.string.yilutiaoguang1, R.string.lianglutiaoguang1, R.string.sanlutiao1, R.string.window_panel1
            , R.string.menci, R.string.rentiganying, R.string.jiuzuo, R.string.yanwu, R.string.tianranqi, R.string.jinjin_btn,
            R.string.zhineng, R.string.pm25, R.string.shuijin, R.string.duogongneng,R.string.cha_zuo_1
    };
    private SelectWifiDevAdapter adapter_wifi;
    private DialogUtil dialogUtil;
    private List<Map> list_hand_scene = new ArrayList<>();

    /**
     * 小苹果绑定列表
     */
    private DeviceManager mDeviceManager;
    List<GizWifiDevice> wifiDevices = new ArrayList<GizWifiDevice>();
    private List<String> deviceNames = new ArrayList<String>();
    private GizWifiDevice mGizWifiDevice = null;
    private List<Map> wifi_apple_list = new ArrayList<>();

    private String TAG = "robin debug";
    private String mac;
    private String number;
    private Handler handler = new Handler();

    @Override
    protected int viewId() {
        return R.layout.sel_dev_typ_act;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        onData();
        onWifi();
    }

    private void onWifi() {
        mDeviceManager = DeviceManager
                .instanceDeviceManager(getApplicationContext());
    }

    private void onData() {
        dialogUtil = new DialogUtil(SelectZigbeeDeviceActivity.this);
        adapter = new SelectDevTypeAdapter(SelectZigbeeDeviceActivity.this, icon, iconName);
        macfragritview_id.setAdapter(adapter);
        adapter_wifi = new SelectWifiDevAdapter(SelectZigbeeDeviceActivity.this, icon_wifi, iconNam_wifi);
        mac_wifi_dev_id.setAdapter(adapter_wifi);
        macfragritview_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent_position = null;
//                switch (types[position]) {
//                    case "A501":
//                    case "A511":
//                        break;
//                    default:
//                        intent_position = new Intent(SelectZigbeeDeviceActivity.this, AddZigbeeDevActivity.class);
//                        intent_position.putExtra("type", types[position]);
//                        startActivity(intent_position);
//                        break;
//                }
                switch (types[position]) {
                    case "A201":
                    case "A202":
                    case "A203":
                    case "A204":
                    case "A301":
                    case "A302":
                    case "A303":
                    case "A401":
                    case "B101":
                    case "B301":
                    case "A902":
                    case "AD01":
                    case "A501":
//                    case "A511":
                        sraum_setBox_accent(types[position], "normal");
                        break;
                    case "A801":
                    case "A901":
                    case "AB01":
                    case "AB04":
                    case "B001":
                    case "AC01":
                        sraum_setBox_accent(types[position], "zigbee");
                        break;
                    case "B201":
                        Intent intent_position = new Intent(SelectZigbeeDeviceActivity.this, SelectSmartDoorLockActivity.class);
                        intent_position.putExtra("type", types[position]);
                        startActivity(intent_position);
                        break;
                }
            }
        });

//        mac_wifi_dev_id.setAdapter(adapter);//Wi-Fi设备

        mac_wifi_dev_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_wifi = null;
                switch (types_wifi[position]) {
                    case "101":
                    case "103":
                        intent_wifi = new Intent(SelectZigbeeDeviceActivity.this, AddWifiDevActivity.class);
                        intent_wifi.putExtra("type", types_wifi[position]);
                        startActivity(intent_wifi);
                        break;
                    case "102":
                        break;
                    case "hongwai":
                        intent_wifi = new Intent(SelectZigbeeDeviceActivity.this, AddWifiDevActivity.class);
                        intent_wifi.putExtra("type", types_wifi[position]);
                        startActivity(intent_wifi);
                        break;
                    case "yaokong":
                        getOtherDevices();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDeviceManager.setGizWifiCallBack(mGizWifiCallBack);
        update(mDeviceManager.getCanUseGizWifiDevice());
    }


    private GizWifiCallBack mGizWifiCallBack = new GizWifiCallBack() {

        @Override
        public void didUnbindDeviceCd(GizWifiErrorCode result, String did) {
            super.didUnbindDeviceCd(result, did);
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 解绑成功
                Logger.d(TAG, "解除绑定成功");
            } else {
                // 解绑失败
                Logger.d(TAG, "解除绑定失败");
            }
        }

        @Override
        public void didBindDeviceCd(GizWifiErrorCode result, String did) {    //
            super.didBindDeviceCd(result, did);
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {//
                // 绑定成功
                Logger.d(TAG, "绑定成功");
            } else {
                // 绑定失败
                Logger.d(TAG, "绑定失败");
            }
        }

        @Override
        public void didSetSubscribeCd(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            super.didSetSubscribeCd(result, device, isSubscribed);
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 解绑成功
                Logger.d(TAG, (isSubscribed ? "订阅" : "取消订阅") + "成功");
            } else {
                // 解绑失败
                Logger.d(TAG, "订阅失败");
            }
        }

        @Override
        public void discoveredrCb(GizWifiErrorCode result,
                                  List<GizWifiDevice> deviceList) {
            Logger.d(TAG,
                    "discoveredrCb -> deviceList size:" + deviceList.size()
                            + "  result:" + result);
            switch (result) {
                case GIZ_SDK_SUCCESS:
                    Logger.e(TAG, "load device  sucess");
                    update(deviceList);
//                    if(deviceList.get(0).getNetStatus()==GizWifiDeviceNetStatus.GizDeviceOffline)

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取门磁等第三方设备
     */
    private void getOtherDevices() {
        if (dialogUtil != null) {
            dialogUtil.loadDialog();
        }
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(SelectZigbeeDeviceActivity.this));
//        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(SelectSensorActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_getWifiAppleInfos
                , mapdevice, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {//刷新togglen数据
                        getOtherDevices();
                    }
                }, SelectZigbeeDeviceActivity.this, dialogUtil) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void pullDataError() {
                        super.pullDataError();
                    }

                    @Override
                    public void emptyResult() {
                        super.emptyResult();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                        //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen
                    }

                    @Override
                    public void wrongBoxnumber() {
                        super.wrongBoxnumber();
                    }

                    @Override
                    public void onSuccess(final User user) {
                        list_hand_scene = new ArrayList<>();
                        for (int i = 0; i < user.controllerList.size(); i++) {
                            Map<String, String> mapdevice = new HashMap<>();
                            mapdevice.put("name", user.controllerList.get(i).name);
                            mapdevice.put("number", user.controllerList.get(i).number);
                            mapdevice.put("type", user.controllerList.get(i).type);
                            mapdevice.put("controllerId", user.controllerList.get(i).controllerId);
                            list_hand_scene.add(mapdevice);
                        }

                        if (list_hand_scene.size() == 1) {
                            if (wifiDevices.size() != 0) {
                                choose_the_brand(0);
                            } else {
//                                update(mDeviceManager.getCanUseGizWifiDevice());
                                ToastUtil.showToast(SelectZigbeeDeviceActivity.this, "请与" + list_hand_scene.get(0)
                                        .get("name").toString()
                                        +
                                        "在同一网络后再添加");
                            }
                        } else {
                            Intent intent_wifi =
                                    new Intent(SelectZigbeeDeviceActivity.this,
                                            SelectInfraredForwardActivity.class);
                            intent_wifi.putExtra("list_hand_scene", (Serializable) list_hand_scene);
                            startActivity(intent_wifi);
                        }
                    }
                });
    }

    /**
     * 跳转到选择品牌界面
     *
     * @param position
     */
    private void choose_the_brand(int position) {
        mac = (String) list_hand_scene.get(position).get("controllerId");
        number = list_hand_scene.get(position).get("number").toString();
        //去根据mac去服务器端下载GizWifiDevice
        String apple_name = "";
        for (int i = 0; i < list_hand_scene.size(); i++) {
            if (list_hand_scene.get(i).get("controllerId").equals(mac)) {
                apple_name = list_hand_scene.get(i).get("name").toString();

            }
        }
        get_to_wifi(mac, apple_name);
    }

    private void get_to_wifi(String mac, String apple_name) {

        for (int i = 0; i < wifiDevices.size(); i++) {
            if (wifiDevices.get(i).getMacAddress().equals(mac)) {
                mGizWifiDevice = wifiDevices.get(i);
            }
        }
        if (!Utility.isEmpty(mGizWifiDevice)) { //
            mDeviceManager.bindRemoteDevice(mGizWifiDevice);
            final GizWifiDevice finalMGizWifiDevice = mGizWifiDevice;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDeviceManager.setSubscribe(finalMGizWifiDevice, true);
                }
            }, 1000);
        } else {
            ToastUtil.showToast(SelectZigbeeDeviceActivity.this, "请与" + apple_name
                    +
                    "在同一网络后再控制");
            return;
        }
        toControlApplianAct();
    }

    private void toControlApplianAct() {
        Intent intent = new Intent(SelectZigbeeDeviceActivity.this, SelectControlApplianceActivity.class);
        if (mGizWifiDevice == null) {
            return;
        }
        intent.putExtra("GizWifiDevice", mGizWifiDevice);
//        intent.putExtra("tid", getIntent().getSerializableExtra("tid"));
        intent.putExtra("number", number);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                SelectZigbeeDeviceActivity.this.finish();
                break;
        }
    }

    void update(List<GizWifiDevice> gizWifiDevices) {
        GizWifiDevice mGizWifiDevice = null;


        if (gizWifiDevices == null) {
            deviceNames.clear();
        } else if (gizWifiDevices != null && gizWifiDevices.size() >= 1) {
//            wifiDevices.clear();
            wifiDevices.addAll(gizWifiDevices);
            HashSet<GizWifiDevice> h = new HashSet<GizWifiDevice>(wifiDevices);
            wifiDevices.clear();
            for (GizWifiDevice gizWifiDevice : h) {
                wifiDevices.add(gizWifiDevice);
            }
            deviceNames.clear();
//            for (int i = 0; i < wifiDevices.size(); i++) {
////                deviceNames.add(wifiDevices.get(i).getProductName() + "("
////                        + wifiDevices.get(i).getMacAddress() + ") "
////                        + getBindInfo(wifiDevices.get(i).isBind()) + "\n"
////                        + getLANInfo(wifiDevices.get(i).isLAN()) + "  " + getOnlineInfo(wifiDevices.get(i).getNetStatus()));
//                mGizWifiDevice = wifiDevices.get(i);
//                // list_hand_scene
//                // 绑定选中项
//                if (!Utility.isEmpty(mGizWifiDevice)) { //
//                    mDeviceManager.bindRemoteDevice(mGizWifiDevice);
//                    final GizWifiDevice finalMGizWifiDevice = mGizWifiDevice;
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mDeviceManager.setSubscribe(finalMGizWifiDevice, true);
//                        }
//                    }, 1000);
//                }
//            }
            //去绑定和订阅
        }
//        adapter.notifyDataSetChanged();
    }

    private void sraum_setBox_accent(final String type, final String yangshi) {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
//        String phoned = getDeviceId(SelectZigbeeDeviceActivity.this);
        map.put("token", TokenUtil.getToken(SelectZigbeeDeviceActivity.this));
        String boxnumber = (String) SharedPreferencesUtil.getData(SelectZigbeeDeviceActivity.this,
                "boxnumber", "");
        map.put("boxNumber", boxnumber);
        String regId = (String) SharedPreferencesUtil.getData(SelectZigbeeDeviceActivity.this, "regId", "");
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
                }, SelectZigbeeDeviceActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        Intent intent_position = null;
                        switch (yangshi) {
                            case "normal":
                            case "zigbee":
                                switch (type) {
//                                    case "B201":
//                                        intent_position = new Intent(SelectZigbeeDeviceActivity.this, SelectSmartDoorLockActivity.class);
//                                        break;
                                    default:
                                        intent_position = new Intent(SelectZigbeeDeviceActivity.this, AddZigbeeDevActivity.class);
                                        break;
                                }
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
                        ToastUtil.showToast(SelectZigbeeDeviceActivity.this,
                                "该网关不存在");
                    }
                }
        );
    }
}
