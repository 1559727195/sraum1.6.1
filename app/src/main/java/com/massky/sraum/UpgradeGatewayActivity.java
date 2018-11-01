package com.massky.sraum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.Util.view.RoundProgressBar;
import com.base.Basecactivity;
import com.data.User;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.InjectView;
import static com.massky.sraum.GuJianWangGuanActivity.UPDATE_GRADE_BOX;

/**
 * Created by masskywcy on 2016-11-30.
 */

public class UpgradeGatewayActivity extends Basecactivity {
    //    public static String ACTION_SRAUM_SETBOX = "ACTION_SRAUM_SETBOX";//notifactionId = 8 ->设置网关模式，sraum_setBox
//    @InjectView(R.id.backrela_id)
//    RelativeLayout backrela_id;
//    @InjectView(R.id.addroomrelative_id)
//    RelativeLayout addroomrelative_id;
//    @InjectView(R.id.titlecen_id)
//    TextView titlecen_id;
//    //    @InjectView(R.id.addscroll)
////    ScrollView addscroll;
//    @InjectView(R.id.search_image)
//    ImageView search_image;
//    @InjectView(R.id.search_image_rel)
//    RelativeLayout search_image_rel;

    @InjectView(R.id.roundProgressBar2)
    RoundProgressBar roundProgressBar2;
    @InjectView(R.id.sraum_rela_act)
    RelativeLayout sraum_rela_act;
    @InjectView(R.id.btn_cancel_wangguan)
    Button btn_cancel_wangguan;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.back)
    ImageView back;
    private boolean is_index = true;
    private MessageReceiver mMessageReceiver;
    private String name;
    private DialogUtil dialogUtil;
    private String panelType;
    private String panelName;
    private String panelNumber;
    private String deviceNumber;
    private String panelMAC;
    private List<User.device> deviceList = new ArrayList<>();
    private List<User.panellist> panelList = new ArrayList<>();
    private String currentVersion;
    private String newVersion;

    @Override
    protected int viewId() {
        return R.layout.upgrade_gateway_lay;
    }

    @Override
    protected void onView() {
        dialogUtil = new DialogUtil(this);
//        startState();
        btn_cancel_wangguan.setOnClickListener(this);
        registerMessageReceiver();
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        init_status_bar();
        //
        getgateway_version();

    }

    private void init_status_bar() {
        roundProgressBar2.setAdd_Delete("delete");
        roundProgressBar2.setMax(20);
        final int[] index = {20};
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (is_index) {
                    index[0]--;
                    try {
                        Thread.sleep(1000);
                        roundProgressBar2.setProgress(i++);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (index[0] < 0) {
//                        sraum_setBox_quit("");
                        UpgradeGatewayActivity.this.finish();
                        //停止添加网关
                        is_index = false;
                    }
                }
            }
        }).start();
    }

    /**
     * 获取网关版本号
     */

    private void getgateway_version() {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
//        String phoned = getDeviceId(this);
        map.put("token", TokenUtil.getToken(this));
        String boxnumber = (String) SharedPreferencesUtil.getData(this, "boxnumber", "");
        map.put("boxNumber", boxnumber);
//        map.put("phoneId", phoned);
//        map.put("status", "0");//进入设置模式
//        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_getBoxVersion, map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {//
//
                        getgateway_version();

                    }
                }, UpgradeGatewayActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        currentVersion = user.currentVersion;
                        newVersion = user.newVersion;

                        if (newVersion != null) {
                            if (Integer.parseInt(newVersion) >= 56) {

                                //int d = Integer.valueOf("ff",16);   //d=255
                                if (Integer.valueOf(newVersion, 16) > Integer.valueOf(currentVersion, 16)) {
                                    updatebox_version();
                                } else {
                                    ToastUtil.showToast(UpgradeGatewayActivity.this, "网关版本已最新");
                                    is_index = false;
                                    UpgradeGatewayActivity.this.finish();
                                    //停止添加网关
                                }
                            } else {
                                ToastUtil.showToast(UpgradeGatewayActivity.this, "网关版本过低不支持" +
                                        "更新");
                            }
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void wrongBoxnumber() {
                        ToastUtil.showToast(UpgradeGatewayActivity.this, "该网关不存在");
                    }
                }
        );
    }

    /**
     * 获取网关版本号
     */

    private void updatebox_version() {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
//        String phoned = getDeviceId(this);
        String regId = (String) SharedPreferencesUtil.getData(UpgradeGatewayActivity.this, "regId", "");
        map.put("token", TokenUtil.getToken(this));
        String boxnumber = (String) SharedPreferencesUtil.getData(this, "boxnumber", "");
        map.put("boxNumber", boxnumber);
        map.put("regId", regId);
//        map.put("status", "0");//进入设置模式
//        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_updateBox, map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {//
                        updatebox_version();
                    }
                }, UpgradeGatewayActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {

                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void wrongBoxnumber() {
                        ToastUtil.showToast(UpgradeGatewayActivity.this, "该网关不存在");
                    }
                }
        );
    }

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_GRADE_BOX);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(UPDATE_GRADE_BOX)) {
                int messflag = intent.getIntExtra("notifactionId", 0);
                String panelid = intent.getStringExtra("panelid");
                if (messflag == 50) {//notifactionId = 8 ->设置网关模式，sraum_setBox
                    //收到服务器端设置网关成功以后，跳转到修改面板名称，以及该面板下设备列表名称

                    //在网关转圈界面，下去拉设备，判断设备类型，不是我们的。网关不关，是我们的设备类型；在关网关。
                    //然后跳转到显示设备列表界面。
//                    ToastUtil.showToast(MacdeviceActivity.this,"messflag:" + messflag);
//                    getPanel_devices(panelid);
//                    String panelid = "62a001ff1006,1";
                    String[] items = panelid.split(",");
                    if (items != null) {
                        if (items[1].equals("1")) {
                            ToastUtil.showToast(UpgradeGatewayActivity.this, "网关" +
                                    "升级成功");
                            is_index = false;
                            UpgradeGatewayActivity.this.finish();
                        } else {
                            ToastUtil.showToast(UpgradeGatewayActivity.this, "网关" +
                                    "升级失败");
                            is_index = false;
                            UpgradeGatewayActivity.this.finish();
                        }
                    }
                }
            }
        }
    }

//    private void sraum_setBox_quit(final String panelid) {
//        //在这里先调
//        //设置网关模式-sraum-setBox
//        Map map = new HashMap();
//        String phoned = getDeviceId(this);
//        map.put("token", TokenUtil.getToken(this));
//        String boxnumber = (String) SharedPreferencesUtil.getData(this, "boxnumber", "");
//        map.put("boxNumber", boxnumber);
//        map.put("phoneId", phoned);
//        map.put("status", "0");//进入设置模式
////        dialogUtil.loadDialog();
//        MyOkHttp.postMapObject(ApiHelper.sraum_setBox, map, new Mycallback(new AddTogglenInterfacer() {
//                    @Override
//                    public void addTogglenInterfacer() {
//                        sraum_setBox_quit(panelid);
//                    }
//                }, UpgradeGatewayActivity.this, null) {
//                    @Override
//                    public void onSuccess(User user) {
//                        //退出设置网关模式成功，后，
////                        if (!panelid.equals("")) {
////                            Intent intent = new Intent(MacdeviceActivity.this,
////                                    ChangePanelAndDeviceActivity.class);
////                            intent.putExtra("panelid", panelid);
////
////                            startActivity(intent);
////                            MacdeviceActivity.this.finish();
////                        }
//
////
////                        deviceList.addAll(user.deviceList);
////
////                        //面板的详细信息
////                        panelType = user.panelType;
////                        panelName = user.panelName;
////                        panelMAC = user.panelMAC;
//
//                        if (!panelid.equals("")) {
//                            Intent intent = new Intent(UpgradeGatewayActivity.this,
//                                    ChangePanelAndDeviceActivity.class);
//                            intent.putExtra("panelid", panelid);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("deviceList", (Serializable) deviceList);
////                            intent.putExtra("deviceList", (Serializable) deviceList);
//                            intent.putExtra("panelType", panelType);
//                            intent.putExtra("panelName", panelName);
//                            intent.putExtra("panelMAC", panelMAC);
//                            intent.putExtra("bundle_panel", bundle);
//                            intent.putExtra("findpaneltype", "wangguan_status");
//                            startActivity(intent);
//                            UpgradeGatewayActivity.this.finish();
//                        }
//                    }
//
//                    @Override
//                    public void wrongToken() {
//                        super.wrongToken();
//                    }
//
//                    @Override
//                    public void wrongBoxnumber() {
//                        ToastUtil.showToast(UpgradeGatewayActivity.this, "该网关不存在");
//                    }
//                }
//        );
//    }
//
//
//    /**
//     * 添加面板下的设备信息
//     *
//     * @param panelid
//     */
//    private void getPanel_devices(final String panelid) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", TokenUtil.getToken(UpgradeGatewayActivity.this));
//        map.put("boxNumber", TokenUtil.getBoxnumber(UpgradeGatewayActivity.this));
//        map.put("panelNumber", panelid);
//        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
//                new Mycallback(new AddTogglenInterfacer() {
//                    @Override
//                    public void addTogglenInterfacer() {
//                        getPanel_devices(panelid);
//                    }
//                }, UpgradeGatewayActivity.this, dialogUtil) {
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        super.onError(call, e, id);
//                    }
//
//                    @Override
//                    public void onSuccess(User user) {
//                        super.onSuccess(user);
//                        panelList.clear();
//                        deviceList.clear();
//                        deviceList.addAll(user.deviceList);
//
//                        //面板的详细信息
//                        panelType = user.panelType;
//                        panelName = user.panelName;
//                        panelMAC = user.panelMAC;
////                        panelmac.setText(panelMAC);
////                        panelname.setText(panelName);
////                        paneltype.setText(panelType);
////                        Log.e("robin debug"," panelType:" +  panelType + ",panelName:" + panelName);
////                        show_device_from_panel(panelType);
////                        //在这里显示设备列表信息
////                        switch (deviceList.size()) {
////                            case 1:
////                                onekey_device_txt.setText(deviceList.get(0).deviceName);
////                                break;
////                            case 2:
////                                onekey_device_txt.setText(deviceList.get(0).deviceName);
////                                twokey_device_txt.setText(deviceList.get(1).deviceName);
////                                break;
////                            case 3:
////                                onekey_device_txt.setText(deviceList.get(0).deviceName);
////                                twokey_device_txt.setText(deviceList.get(1).deviceName);
////                                threekey_device_txt.setText(deviceList.get(2).deviceName);
////                                break;
////                            case 4:
////                                onekey_device_txt.setText(deviceList.get(0).deviceName);
////                                twokey_device_txt.setText(deviceList.get(1).deviceName);
////                                threekey_device_txt.setText(deviceList.get(2).deviceName);
////                                fourkey_device_txt.setText(deviceList.get(3).deviceName);
////                                break;
////                        }
////                    }
//
//
//                        switch (panelType) {
//                            case "A201"://一灯控
//                            case "A202"://二灯控
//                            case "A203"://三灯控
//                            case "A204"://四灯控
//                            case "A301"://一键调光，3键灯控  设备4个
//                            case "A302"://两键调光，2键灯控
//                            case "A303"://三键调光，一键灯控
//                            case "A401"://设备2个
//                            case "A501"://空调-设备1个
//                            case "A601"://新风
//                            case "A701"://地暖
//                                sraum_setBox_quit(panelid);
//                                break;
//                            default://其他的面板类型的面，界面不跳转并且，网关不关闭
//
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void wrongToken() {
//                        super.wrongToken();
//                    }
//                });
//    }


    /**
     * 获取手机唯一标示码
     *
     * @param context
     * @return
     */
    public String getDeviceId(Context context) {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(UpgradeGatewayActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }


//    private void startState() {
//        backrela_id.setOnClickListener(this);
//        Intent intent = getIntent();
//        name = intent.getStringExtra("name");
//        switch (name) {
//            case "1":
//                titlecen_id.setText(R.string.addroom);
//
//                addroomrelative_id.setVisibility(View.VISIBLE);
////                addscroll.setVisibility(View.GONE);
//                sraum_rela_act.setVisibility(View.GONE);
//                break;
//            case "2":
////                search_image.setVisibility(View.VISIBLE);
////                search_image_rel.setVisibility(View.VISIBLE);
////                search_image_rel.setOnClickListener(this);
////                titlecen_id.setText(R.string.addmac);
//                registerMessageReceiver();
//                addroomrelative_id.setVisibility(View.GONE);
////                addscroll.setVisibility(View.VISIBLE);
//                sraum_rela_act.setVisibility(View.VISIBLE);
//                titlecen_id.setText("");
//                init_status_bar();
//                break;
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
//                if (name.equals("2")) {
//                    sraum_setBox_quit("");
//                    //停止添加网关
//                    is_index = false;
//                }
                is_index = false;
                UpgradeGatewayActivity.this.finish();
                break;
            case R.id.search_image_rel:
                Intent intent = new Intent();
                intent.setAction("UPSTATUS");
                intent.putExtra("type", "device");
                sendBroadcast(intent);
                UpgradeGatewayActivity.this.finish();
                //跳转到侧滑，智能设备列表
                break;
            case R.id.btn_cancel_wangguan:
//                sraum_setBox_quit("");
                UpgradeGatewayActivity.this.finish();
                //停止添加网关
                is_index = false;
                break;
            case R.id.back:
                UpgradeGatewayActivity.this.finish();
                is_index = false;
                break;
        }
    }

    @Override
    protected void onDestroy() {//
        super.onDestroy();
//        if (name.equals("2"))
        unregisterReceiver(mMessageReceiver);
        //停止添加网关
        is_index = false;
    }
}
