package com.fragment;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.App;
import com.Util.AppManager;
import com.Util.DbDevice;
import com.Util.DialogUtil;
import com.Util.EntityUtils;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MusicUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ParceUtil;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MacFragAdapter;
import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.dialog.MyApplication;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.google.gson.Gson;
import com.ipcamera.demo.AddCameraActivity;
import com.ipcamera.demo.BridgeService;
import com.ipcamera.demo.PlayActivity;
import com.ipcamera.demo.utils.ContentCommon;
import com.ipcamera.demo.utils.SystemValue;
import com.massky.sraum.AddWifiCameraScucessActivity;
import com.massky.sraum.AirControlActivity;
import com.massky.sraum.ConnWifiCameraActivity;
import com.massky.sraum.LamplightActivity;
import com.massky.sraum.MacdetailActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.Pm25Activity;
import com.massky.sraum.Pm25SecondActivity;
import com.massky.sraum.R;
import com.massky.sraum.SelectInfraredForwardActivity;
import com.massky.sraum.SelectZigbeeDeviceActivity;
import com.massky.sraum.TVShowActivity;
import com.massky.sraum.WaterSensorActivity;
import com.yaokan.sdk.ir.YKanHttpListener;
import com.yaokan.sdk.ir.YkanIRInterfaceImpl;
import com.yaokan.sdk.model.BaseResult;
import com.yaokan.sdk.model.KeyCode;
import com.yaokan.sdk.model.RemoteControl;
import com.yaokan.sdk.model.YKError;
import com.yaokan.sdk.utils.Logger;
import com.yaokan.sdk.utils.Utility;
import com.yaokan.sdk.wifi.DeviceManager;
import com.yaokan.sdk.wifi.GizWifiCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.InjectView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import vstc2.nativecaller.NativeCaller;

import static com.fragment.Mainviewpager.MESSAGE_TONGZHI_DOOR_FIRST;
import static com.massky.sraum.MyDeviceItemActivity.MESSAGE_TONGZHI_VIDEO_FROM_MYDEVICE;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于第一个fragment主界面*/
public class MacFragment extends Basecfragment implements
        AdapterView.OnItemClickListener
        , BridgeService.IpcamClientInterface, BridgeService.CallBackMessageInterface {
    public  static final String MESSAGE_TONGZHI_VIDEO_TO_MYDEVICE = "com.sraum.massky.to.mydevice";
    @InjectView(R.id.bottomimage_id)
    ImageView bottomimage_id;
    @InjectView(R.id.addmacbtn_id)
    Button addmacbtn_id;
    @InjectView(R.id.addmacrela_id)
    RelativeLayout addmacrela_id;
    @InjectView(R.id.macfragritview_id)
    GridView macfragritview_id;
    @InjectView(R.id.refresh_view)
    XRefreshView refresh_view;
    @InjectView(R.id.macstatus)
    TextView macstatus;
    private DialogUtil dialogUtil;
    private MacFragAdapter adapter;
    private DbDevice dbDevice;
    //网关编号,token
    private String loginPhone, status = "";
    //震动和音乐的判断值
    private boolean vibflag, musicflag;
    private ImageView lightimage_id, airimage_id, curimage_id;
    private List<Map> list = new ArrayList<>();
    private List<User.wifi_device> wifi_list = new ArrayList<>();
    //进行判断是否进行创建刷新
    private boolean isFirstIn = true;
    private RelativeLayout itemrela_id;
    private List<String> listtype = new ArrayList();
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private int index_toggen;
    private List<Map<String, Object>> listob;
    private boolean isjpush;
    private String TAG = MainfragmentActivity.class.getSimpleName();
    private boolean isSearched;
    private MyBroadCast receiver;
    private WifiManager manager = null;
    private MyWifiThread myWifiThread = null;
    private static final int SEARCH_TIME = 3000;
    private int option = ContentCommon.INVALID_OPTION;
    private int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
    private static final String STR_DID = "did";
    private static final String STR_MSG_PARAM = "msgparam";
    List<Map> list_wifi_camera = new ArrayList<>();
    private boolean playactivityfinsh = true;
    private String videofrom = "";
    public static String MACFRAGMENT_PM25 = "com.fragment.pm25";
    private Map video_item = new HashMap();//来自devicefragment


    /**
     * 小苹果绑定列表
     */
    private DeviceManager mDeviceManager;
    List<GizWifiDevice> wifiDevices = new ArrayList<GizWifiDevice>();
    private List<String> deviceNames = new ArrayList<String>();
    private YkanIRInterfaceImpl ykanInterface;
    private int index;
    private boolean again_connection;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            ToastUtil.showToast(getActivity(), "mggiz在线");
//            if (dialogUtil != null) dialogUtil.loadDialog();
            switch (msg.what) {
                case 0:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialogUtil != null) {
                                dialogUtil.loadDialog();
                            } else {
                                dialogUtil = new DialogUtil(getActivity());
                                dialogUtil.loadDialog();
                            }
                        }
                    });
                    break;
                case 1:

                    break;
                case 2:
                    switch (doit_wifi) {
                        case "air":
                            test_air_control(mapdevice.get("mac").toString());
                            break;
                        case "tv":
                            test_tv(mapdevice.get("mac").toString());
                            break;
                    }
                    break;
                case 3:
                    if (dialogUtil != null) dialogUtil.removeDialog();
                    break;
                case 6://下载码库次数过于频繁，所以提示
                    index++;
                    if (index >= 2) {
                        dialogUtil.removeDialog();
                        index = 0;
                        return;
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch (doit_wifi) {
                                case "air":
                                    test_air_control(mapdevice.get("mac").toString());
                                    break;
                                case "tv":
                                    test_tv(mapdevice.get("mac").toString());
                                    break;
                            }
                        }
                    }, 10000);//两次下载遥控器码值时间间隔10s,
                    break;
                case 7://下载遥控器码没有反应
                    switch (doit_wifi) {
                        case "air":
                            test_air_control(mapdevice.get("mac").toString());
                            break;
                        case "tv":
                            test_tv(mapdevice.get("mac").toString());
                            break;
                    }
                    break;
                case 10://wifi摄像头已经在线了
                    switch (videofrom) {//      videofrom = "macfragment";
                        case "macfragment":
                            mac_fragment_video_ok();
                            break;
                        case "devicefragment"://直接跳转到devicefragmentActivity
                            tongzhi_to_video("1");
                            break;
                    }
                    break;
                case 11://连接失败，在去连
                    switch (videofrom) {//      videofrom = "macfragment";
                        case "macfragment":
                            onitem_wifi_shexiangtou(mapdevice);
                            break;
                        case "devicefragment":
                            common_video(video_item);
                            break;
                    }
                    break;
            }
        }
    };

    @Subscribe
    public void onEvent(MyEvent event) {

    }


    /**
     * 发送给MyDeviceItemActivity视频的状态，获取摄像头状态，并返回
     */
    private void tongzhi_to_video(String status) {
//        Intent mIntent = new Intent(MESSAGE_TONGZHI_VIDEO_TO_MYDEVICE);
//        mIntent.putExtra("status", (Serializable) status);
//        getActivity().sendBroadcast(mIntent);
        MyEvent event = new MyEvent();
        event.setMsg(status);
//...设置event
        EventBus.getDefault().post(event);
    }


    /**
     * macfragment video ok
     */
    private void mac_fragment_video_ok() {
        String content = stringbuffer.toString();
        String[] splits = content.split(",");
        if (splits.length == 3) {
            if (splits[0].equals("未知状态") && splits[1].equals("正在连接") && splits[2].equals("在线")) {
                stringbuffer = new StringBuffer();
//                            AppManager.getAppManager().finishActivity_current(PlayActivity.class);
                Intent intent = new Intent("play_finish");
                getActivity().sendBroadcast(intent);
                again_connection = true;
            }
        } else {
            if (!playactivityfinsh) {
//                        AppManager.getAppManager().finishActivity_current(PlayActivity.class);
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                startActivity(intent);
            }
        }
    }

    private RemoteControl remoteControl;
    private GizWifiDevice mGizWifiDevice = null;
    private Map mapdevice = new HashMap();
    private List<Map> list_remotecontrol_air = new ArrayList<>();
    private Map remoteControl_map_air = new HashMap();
    private int index_wifi;
    private boolean is_control_air;
    private String doit_wifi = "";
    private GizWifiDevice mGizWifiDevice1 = null;
    private List<Map> wifi_apple_list = new ArrayList<>();
    private int index_click;
    private String deviceInfo = "";
    private List<Map> list_hand_scene = new ArrayList<>();

    private boolean blagg = false;
    private Intent intentbrod = null;
    private WifiInfo info = null;
    private String type = "";

    public static MacFragment newInstance() {
        MacFragment newFragment = new MacFragment();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected int viewId() {
        return R.layout.macfragment;
    }

    @Override
    public void onStart() {//onStart()-这个方法在屏幕唤醒时调用。
        super.onStart();
//        boolean tokenflag = TokenUtil.getTokenflag(getActivity());
//        if (tokenflag) {
//            if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                LogUtil.eLength("查看方法是否走起", "方法走动");
//                upload(true);
//                LogUtil.i("这是设备长度" + list.size());
//                for (User.device ud : list) {
//                    listtype.add(ud.status);
//                }
//            }
//        }
        android.util.Log.e("fei", "MacFragment->onStart():name:");
        if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
            LogUtil.eLength("查看方法是否走起", "方法走动");
            upload(true);
            LogUtil.i("这是设备长度" + list.size());
            for (Map map : list) {
                listtype.add(map.get("status").toString());
            }
        }
    }

    //判断网关是否在线
    private void boxStatus(boolean boxFlag, int sceflag) {
        if (boxFlag) {
            if (sceflag == 0) {
                addmacrela_id.setVisibility(View.VISIBLE);
                refresh_view.setVisibility(View.GONE);
            } else {
                addmacrela_id.setVisibility(View.GONE);
                refresh_view.setVisibility(View.VISIBLE);
            }
            macstatus.setVisibility(View.GONE);
            macfragritview_id.setVisibility(View.VISIBLE);
        } else {
            macstatus.setVisibility(View.VISIBLE);
            addmacrela_id.setVisibility(View.GONE);
            macfragritview_id.setVisibility(View.GONE);
        }
    }

    @Override

    protected void onView() {
//        SharedPreferencesUtil.saveData(getActivity(), "nihao", "nihao");
//        String str = (String) SharedPreferencesUtil.getData(getActivity(), "nihao", "");
        EventBus.getDefault().register(this);
        init_wifi_camera();
        registerMessageReceiver();
        // 遥控云数据接口分装对象对象
        ykanInterface = new YkanIRInterfaceImpl(getActivity().getApplicationContext());
        // 遥控云数据接口分装对象对象
        ykanInterface = new YkanIRInterfaceImpl(getActivity().getApplicationContext());
        /**
         * 小苹果初始化
         */
        mDeviceManager = DeviceManager
                .instanceDeviceManager(getActivity().getApplicationContext());
//        get_wifidevice(0);
        dbDevice = new DbDevice(getActivity());
        loginPhone = (String) SharedPreferencesUtil.getData(getActivity(), "loginPhone", "");
        SharedPreferences preferences = getActivity().getSharedPreferences("sraum" + loginPhone,
                Context.MODE_PRIVATE);
        vibflag = preferences.getBoolean("vibflag", false);
        musicflag = preferences.getBoolean("musicflag", false);
        LogUtil.i("这是震动二" + musicflag);
        View bottomview = getActivity().getLayoutInflater().inflate(R.layout.macfbottom, null);
        lightimage_id = (ImageView) bottomview.findViewById(R.id.lightimage_id);
        airimage_id = (ImageView) bottomview.findViewById(R.id.airimage_id);
        curimage_id = (ImageView) bottomview.findViewById(R.id.curimage_id);
        dialogUtil = new DialogUtil(getActivity(), bottomview, 1);
        dialogUtil.setCanCanCel(true);
        bottomimage_id.setOnClickListener(this);
        addmacbtn_id.setOnClickListener(this);
        lightimage_id.setOnClickListener(this);
        airimage_id.setOnClickListener(this);
        curimage_id.setOnClickListener(this);
        refresh_view.setScrollBackDuration(300);
        refresh_view.setPinnedTime(1000);
        refresh_view.setPullLoadEnable(false);

        adapter = new MacFragAdapter(getActivity(), list);
        macfragritview_id.setAdapter(adapter);

        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
//                if (TokenUtil.getTokenflag(getActivity())) {
//                    if (TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                        list.clear();
//                        adapter = new MacFragAdapter(getActivity(), list);
//                        macfragritview_id.setAdapter(adapter);
//                        refresh_view.stopRefresh();
//                    } else {
//                        upload(false);
//                    }
//                } else {
//                    refresh_view.stopRefresh();
//                }

                if (TokenUtil.getBoxnumber(getActivity()).equals("")) {
                    list.clear();
                    adapter = new MacFragAdapter(getActivity(), list);
                    macfragritview_id.setAdapter(adapter);
                    refresh_view.stopRefresh();
                } else {
                    upload(false);
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
            }
        });
        macfragritview_id.setOnItemClickListener(this);
    }

    Runnable updateThread = new Runnable() {

        public void run() {
            NativeCaller.StopSearch();
            Message msg = updateListHandler.obtainMessage();
            msg.what = 1;
            updateListHandler.sendMessage(msg);
        }
    };

    Handler updateListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            } else {

            }
        }

    };

    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    private void startSearch() {
//        listAdapter.ClearAll();
//        progressdlg.setMessage(getString(R.string.searching_tip));
//        progressdlg.show();
        new Thread(new SearchThread()).start();
        updateListHandler.postDelayed(updateThread, SEARCH_TIME);
    }

    private class SearchThread implements Runnable {
        @Override
        public void run() {
            Log.d("tag", "startSearch");
            NativeCaller.StartSearch();
        }
    }

    class MyTimerTask extends TimerTask {

        public void run() {
            updateListHandler.sendEmptyMessage(100000);
        }
    }

    ;

    class MyWifiThread extends Thread {
        @Override
        public void run() {
            while (blagg == true) {
                super.run();

                updateListHandler.sendEmptyMessage(100000);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    class StartPPPPThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
                startCameraPPPP();
            } catch (Exception e) {

            }
        }
    }

    private class MyBroadCast extends BroadcastReceiver {


        @Override
        public void onReceive(Context arg0, Intent arg1) {

//            AddCameraActivity.this.finish();
            Log.d("ip", "AddCameraActivity.this.finish()");
            if (arg1.getAction().equals("finish")) {
                playactivityfinsh = true;
                if (again_connection) {
                    Intent intent_new = new Intent(getActivity(), PlayActivity.class);
                    startActivity(intent_new);
                    again_connection = false;
                }
            }
        }
    }


    private void startCameraPPPP() {
        try {
            Thread.sleep(100);
        } catch (Exception e) {

        }

        if (SystemValue.deviceId.toLowerCase().startsWith("vsta")) {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK", 0);
        } else if (SystemValue.deviceId.toLowerCase().startsWith("vstd")) {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "HZLXSXIALKHYEIEJHUASLMHWEESUEKAUIHPHSWAOSTEMENSQPDLRLNPAPEPGEPERIBLQLKHXELEHHULOEGIAEEHYEIEK-$$", 1);
        } else if (SystemValue.deviceId.toLowerCase().startsWith("vstf")) {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "HZLXEJIALKHYATPCHULNSVLMEELSHWIHPFIBAOHXIDICSQEHENEKPAARSTELERPDLNEPLKEILPHUHXHZEJEEEHEGEM-$$", 1);
        } else if (SystemValue.deviceId.toLowerCase().startsWith("vste")) {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "EEGDFHBAKKIOGNJHEGHMFEEDGLNOHJMPHAFPBEDLADILKEKPDLBDDNPOHKKCIFKJBNNNKLCPPPNDBFDL", 0);
        } else if (SystemValue.deviceId.toLowerCase().startsWith("vstg")) {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "EEGDFHBOKCIGGFJPECHIFNEBGJNLHOMIHEFJBADPAGJELNKJDKANCBPJGHLAIALAADMDKPDGOENEBECCIK:vstarcam2018", 0);
        } else if (SystemValue.deviceId.toLowerCase().startsWith("vstb") || SystemValue.deviceId.toLowerCase().startsWith("vstc")) {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL", 0);
        } else {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "", 0);
        }
        //int result = NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceName,
        //		SystemValue.devicePass,1,"");
        //Log.i("ip", "result:"+result);
    }

    private void stopCameraPPPP() {
        NativeCaller.StopPPPP(SystemValue.deviceId);
    }

    private void init_wifi_camera() {
//        BridgeService.setAddCameraInterface(this);
        BridgeService.setCallBackMessage(this);
        receiver = new MyBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        getActivity().registerReceiver(receiver, filter);
        intentbrod = new Intent("drop");
    }


    private List<Map> list_mac_wifi = new ArrayList<>();
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
        public void didBindDeviceCd(GizWifiErrorCode result, String did) {
            super.didBindDeviceCd(result, did);
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
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

                Map map = new HashMap();
                map.put("mac", device.getMacAddress());
                String str = ParceUtil.object2String(device);
//                mGizWifiDevice1 = ParceUtil.unmarshall(str, GizWifiDevice.CREATOR);
                map.put("code", str);
                list_mac_wifi.add(map);
                SharedPreferencesUtil.saveInfo_List(getActivity(), "list_mac_wifi", list_mac_wifi);
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
//            ToastUtil.showToast(getActivity(),"wifiDevices.size():" + wifiDevices.size());
        }
//        adapter.notifyDataSetChanged();
    }

    private String getBindInfo(boolean isBind) {
        String strReturn = "";
        if (isBind == true)
            strReturn = "已绑定";
        else
            strReturn = "未绑定";
        return strReturn;
    }

    private String getLANInfo(boolean isLAN) {
        String strReturn = "";
        if (isLAN == true)
            strReturn = "局域网内设备";
        else
            strReturn = "远程设备";
        return strReturn;
    }


    private String getOnlineInfo(GizWifiDeviceNetStatus netStatus) {
        String strReturn = "";
        if (mDeviceManager.isOnline(netStatus) == true)//判断是否在线的方法
            strReturn = "在线";
        else
            strReturn = "离线";
        return strReturn;
    }


    private final String PROCESS_NAME = "com.massky.sraum";//进程名称

    /**
     * 判断是不是UI主进程，因为有些东西只能在UI主进程初始化
     */
    public boolean isAppMainProcess() {
        try {
            int pid = android.os.Process.myPid();
            String process = getAppNameByPID(App.getInstance(), pid);
            if (TextUtils.isEmpty(process)) {
                //第一次创建,系统中还不存在该process,所以一定是主进程
                return true;
            } else if (PROCESS_NAME.equalsIgnoreCase(process)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 根据Pid得到进程名
     */
    public String getAppNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                //主进程的pid是否和当前的pid相同,若相同,则对应的包名就是app的包名
                return processInfo.processName;
            }
        }
        return "";
    }


    @Override
    public void initData() {
        if (isAppMainProcess()) {//断当前进程是否是 APP 默认进程，只在主进程中进行初始化操作， APP 默认进程名就是包名
            SharedPreferencesUtil.saveData(getActivity(), "pagetag", "1");
//        boolean flag = TokenUtil.getTokenflag(getActivity());
//        if (flag) {
//            if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                upload(true);
//            } else {
//                list.clear();
//                adapter = new MacFragAdapter(getActivity(), list);
//                macfragritview_id.setAdapter(adapter);
//            }
//        }

            if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
                upload(true);
            } else {
                list.clear();
                adapter = new MacFragAdapter(getActivity(), list);
                macfragritview_id.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomimage_id:
                dialogUtil.loadViewBottomdialog();
                break;
            case R.id.addmacbtn_id:
                Intent intent = new Intent(getActivity(), MacdeviceActivity.class);
                intent.putExtra("name", "2");
                startActivity(intent);
                break;
            //进行分类   灯光，空调，窗帘
            case R.id.lightimage_id:
                getList("2", "1");
                break;
            case R.id.airimage_id:
                getList("3", "0");
                break;
            case R.id.curimage_id:
                getList("4", "0");
                break;
        }
    }

    private void getList(String type, String typetwo) {
        List<Map> listud = new ArrayList<>();
        if (typetwo.equals("1")) {
            for (Map map : list) {
                if (map.get("type").toString().equals("2") || map.get("type").toString().equals("1")) {
                    listud.add(map);
                }
            }
        } else {
            for (Map map : list) {
                if (map.get("type").toString().equals(type)) {
                    listud.add(map);
                }
            }
        }
        LogUtil.e("查看长度", listud.size() + "");
        adapter = new MacFragAdapter(getActivity(), listud);
        macfragritview_id.setAdapter(adapter);
        dialogUtil.removeviewBottomDialog();
    }

    //下拉刷新
    private void upload(final boolean flag) {
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(getActivity()));
        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        if (flag) {
            dialogUtil.loadDialog();
        }
        uploader_refresh(mapdevice);
    }

//    "wifiList": [{
//        "speed": "6",
//                "status": "0",
//                "name": "室内空调",
//                "mac": "5CCF7FB6C07C",小苹果红外模块MAC，
//                "number": "17",
//                "dimmer": "",
//                "type": "206",
//                "deviceId": "2016093013190705",-》遥控码ID
//                "temperature": "26",
//                "mode": "1"
//    }],

    private void uploader_refresh(final Map<String, String> mapdevice) {
        MyOkHttp.postMapString(ApiHelper.sraum_getAllDevice, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen数据
                Map<String, String> mapdevice = new HashMap<>();
                mapdevice.put("token", TokenUtil.getToken(getActivity()));
                mapdevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
                uploader_refresh(mapdevice);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                refresh_view.stopRefresh();
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
                refresh_view.stopRefresh();
            }

            @Override
            public void emptyResult() {
                super.emptyResult();
                refresh_view.stopRefresh();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
                refresh_view.stopRefresh();
                //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen

            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                refresh_view.stopRefresh();
            }

            @Override
            public void onSuccess(final User user) {
                Observable.create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                        emitter.onNext(user);//耗时动作
                    }
                }).timeout(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(User user) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                super.onSuccess(user);
                refresh_view.stopRefresh();
                list.clear();
                listtype.clear();
                for (int i = 0; i < user.deviceList.size(); i++) {
                    Map map = new HashMap();
                    map.put("name", user.deviceList.get(i).name);
                    map.put("number", user.deviceList.get(i).number);
                    map.put("type", user.deviceList.get(i).type);
                    map.put("status", user.deviceList.get(i).status);
                    map.put("mode", user.deviceList.get(i).mode);
                    map.put("dimmer", user.deviceList.get(i).dimmer);
                    map.put("temperature", user.deviceList.get(i).temperature);
                    map.put("speed", user.deviceList.get(i).speed);
                    map.put("boxNumber", user.deviceList.get(i).boxNumber);
                    map.put("boxName", user.deviceList.get(i).boxName);
                    //name1
                    //name2
                    //flag
                    map.put("name1", user.deviceList.get(i).name1);
                    map.put("name2", user.deviceList.get(i).name2);
                    map.put("flag", user.deviceList.get(i).flag);
                    map.put("panelName", user.deviceList.get(i).panelName);
                    map.put("mac", "");
                    map.put("deviceId", "");
                    list.add(map);
                }

                for (int i = 0; i < user.wifiList.size(); i++) {
                    Map map = new HashMap();
                    map.put("name", user.wifiList.get(i).name);
                    map.put("number", user.wifiList.get(i).number);
                    map.put("type", user.wifiList.get(i).type);
                    map.put("status", user.wifiList.get(i).status);
                    map.put("mode", user.wifiList.get(i).mode);
                    map.put("dimmer", user.wifiList.get(i).dimmer);
                    map.put("temperature", user.wifiList.get(i).temperature);
                    map.put("speed", user.wifiList.get(i).speed);
                    map.put("boxNumber", "");
                    map.put("boxName", "");
                    map.put("mac", user.wifiList.get(i).mac);
                    map.put("name1", "");
                    map.put("name2", "");
                    map.put("flag", "");
                    map.put("panelName", "");
                    map.put("deviceId", user.wifiList.get(i).deviceId);
                    list.add(map);
                }


//                list.addAll(user.deviceList);
//                wifi_list.addAll(user.wifiList);
                for (Map map : list) {
                    listtype.add(map.get("status").toString());
                }

                //保持本地WIFI设备与服务器端同步，服务端没有，本地有则把本地的删掉；

//                list_remotecontrol_air = SharedPreferencesUtil.getInfo_List(getActivity(), "remoteairlist");
//                for (int i = 0; i < list_remotecontrol_air.size(); i++) {
//                    int index = 0;
//                    String rid = list_remotecontrol_air.get(i).get("rid").toString();
//                    for (Map map : list) {
//                        if (!list_remotecontrol_air.get(i).get("rid").toString()
//                                .equals(map.get("deviceId").toString())) {
//                            index++;
//                            if (index == list.size()) {//说明服务端没有该rid，删除本地保存的rid
//                                //SharedPreferencesUtil.remove_current_index(getActivity(), "remoteairlist", i);
//                                SharedPreferencesUtil.remove_current_values(getActivity(), "remoteairlist", rid,
//                                        "rid");
//                            }
//                        }
//                    }
//                }

                send_broad_topm25();
                LogUtil.i("这是设备长度2", "" + list.size());
                macstatus.setVisibility(View.GONE);
                boxStatus(TokenUtil.getBoxflag(getActivity()), list.size());
//                adapter = new MacFragAdapter(getActivity(), list);
//                macfragritview_id.setAdapter(adapter);
                adapter.setList(list);//不要new adapter
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void send_broad_topm25() {
        for (Map map : list) {
            if (map.get("type").toString().equals("10")) {
                Map<String, Object> mapdevice = new HashMap<String, Object>();
                mapdevice.put("type", map.get("type").toString());
                mapdevice.put("number", map.get("number").toString());
                mapdevice.put("status", map.get("status").toString());
                mapdevice.put("dimmer", map.get("dimmer").toString());
                mapdevice.put("mode", map.get("mode").toString());
                mapdevice.put("temperature", map.get("temperature").toString());
                mapdevice.put("speed", map.get("speed").toString());
                mapdevice.put("mac", map.get("mac") == null ? "" :
                        map.get("mac").toString());
                mapdevice.put("deviceId", map.get("deviceId") == null ? "" :
                        map.get("deviceId").toString());
                mapdevice.put("name", map.get("name").toString());
                mapdevice.put("panelName", map.get("panelName") == null ? "" :
                        map.get("panelName").toString());

                sendBroad_pm25(mapdevice);
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        View v = parent.getChildAt(position - macfragritview_id.getFirstVisiblePosition());
        itemrela_id = (RelativeLayout) v.findViewById(R.id.itemrela_id);
        Map<String, Object> mapalldevice = new HashMap<String, Object>();
        listob = new ArrayList<Map<String, Object>>();
        if (listtype.get(position).equals("1")) {
            status = "0";
        } else {
            status = "1";
        }
        Map<String, Object> mapdevice = new HashMap<String, Object>();
        mapdevice.put("type", list.get(position).get("type").toString());
        mapdevice.put("number", list.get(position).get("number").toString());
        switch (list.get(position).get("type").toString()) {
            case "11":
                mapdevice.put("status", "0");
                break;
            case "15":
//            case "17":
                mapdevice.put("status", "1");
                break;
            default:
                mapdevice.put("status", status);
                break;
        }

        mapdevice.put("dimmer", list.get(position).get("dimmer").toString());
        mapdevice.put("mode", list.get(position).get("mode").toString());
        mapdevice.put("temperature", list.get(position).get("temperature").toString());
        mapdevice.put("speed", list.get(position).get("speed").toString());
        mapdevice.put("mac", list.get(position).get("mac") == null ? "" :
                list.get(position).get("mac").toString());
        mapdevice.put("deviceId", list.get(position).get("deviceId") == null ? "" :
                list.get(position).get("deviceId").toString());
        mapdevice.put("name", list.get(position).get("name").toString());
        mapdevice.put("panelName", list.get(position).get("panelName") == null ? "" :
                list.get(position).get("panelName").toString());

        listob.add(mapdevice);
        mapalldevice.put("token", TokenUtil.getToken(getActivity()));
        mapalldevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        mapalldevice.put("deviceInfo", listob);
        String name = "";

        switch (list.get(position).get("type").toString()) {
            case "7":
                name = "门磁";
                ToastUtil.showToast(getActivity(),
                        name + "无控制功能");
                break;
            case "8":
                name = "人体感应";
                ToastUtil.showToast(getActivity(),
                        name + "无控制功能");
                break;
            case "9":
                name = "水浸检测器";
                ToastUtil.showToast(getActivity(),
                        name + "无控制功能");
                break;
            case "10":
                name = "PM2.5";
//                ToastUtil.showToast(getActivity(),
//                        name + "无控制功能");
                break;
//            case "11":
//                name = "紧急按钮";
//                ToastUtil.showToast(getActivity(),
//                        name + "无控制功能");
//                break;
            case "12":
                name = "久坐报警器";
                ToastUtil.showToast(getActivity(),
                        name + "无控制功能");
                break;
            case "13":
                name = "烟雾报警器";
                ToastUtil.showToast(getActivity(),
                        name + "无控制功能");
                break;
            case "14":
                name = "天然气报警器";
                ToastUtil.showToast(getActivity(),
                        name + "无控制功能");
                break;
//            case "15":
//                name = "智能门锁";
//                ToastUtil.showToast(getActivity(),
//                        name + "无控制功能");
//                break;
//            case "16":
//                name = "直流电阀机械手";
//                ToastUtil.showToast(getActivity(),
//                        name + "无控制功能");
//                break;

//            case "17":
//                name = "86插座一位";
//                ToastUtil.showToast(getActivity(),name + "无控制功能");
//                break;
            //special_type_device(mapdevice);
//                test_pm25();
            // test_tv();
            //test_air_control();
//                water_sensor();
        }

        switch (list.get(position).get("type").toString()) {
            case "7":
            case "8":
            case "9":
//            case "10":
//            case "11":
            case "12":
            case "13":
            case "14":
//            case "15":
//            case "16":
                break;
            case "202"://电视机
                this.mapdevice = mapdevice;
                test_tv(mapdevice.get("mac").toString());
                break;
            case "10":
                this.mapdevice = mapdevice;
                test_pm25();
                break;
            case "206"://WIFI空调
                this.mapdevice = mapdevice;
                test_air_control(mapdevice.get("mac").toString());
                //special_type_device(mapdevice);
//                test_pm25();
                // test_tv();
                //test_air_control();
//                water_sensor();
                break;
            case "101"://wifi摄像头
            case "103":
//                map.put("token", TokenUtil.getToken(AddWifiCameraScucessActivity.this));
//                map.put("type", "AA03");
//                map.put("name", name);
//                map.put("mac", wificamera.get("strMac"));
//                map.put("controllerId", wificamera.get("strDeviceID"));
//                map.put("user", wificamera.get("strName"));
//                map.put("password", "888888");
//                map.put("wifi", wificamera.get("wifi"));
                this.mapdevice = mapdevice;
                this.videofrom = "macfragment";
                onitem_wifi_shexiangtou(mapdevice);
                break;
            default:
                curtains_and_light(position, mapalldevice);
                break;
        }
    }

    /**
     * 摄像头click
     *
     * @param mapdevice
     */
    private void onitem_wifi_shexiangtou(Map<String, Object> mapdevice) {
        playactivityfinsh = false;
        again_connection = false;
        this.mapdevice = mapdevice;
        common_video(mapdevice);
    }

    /**
     * 共同的视频
     *
     * @param mapdevice
     */
    private void common_video(Map<String, Object> mapdevice) {
        List<Map> list = SharedPreferencesUtil.getInfo_List(getActivity(), "list_wifi_camera_first");
        int tag = 0;
        for (int i = 0; i < list.size(); i++) {
            if (mapdevice.get("mode").toString()
                    .equals(list.get(i).get("did"))) {
                tag = (int) list.get(i).get("tag");
            }
        }

        if (tag == 1) {
            handler.sendEmptyMessage(10);//设备已经在线了
//            Toast.makeText(getActivity(), "设备已经是在线状态了", Toast.LENGTH_SHORT).show();
        } else if (tag == 2) {

            switch (videofrom) {//      videofrom = "macfragment";
                case "macfragment":
                    Toast.makeText(getActivity(), "设备不在线", Toast.LENGTH_SHORT).show();
                    break;
                case "devicefragment"://直接跳转到devicefragmentActivity-》设备已经断线
                    tongzhi_to_video("0");
                    break;
            }

        } else {
            done(mapdevice.get("dimmer").toString()
                    , mapdevice.get("temperature").toString()
                    , mapdevice.get("mode").toString());//String strUser,String strPwd,String strDID
        }
    }

    private void done(String strUser, String strPwd, String strDID) {
        Intent in = new Intent();
//        String strUser = userEdit.getText().toString();
//        String strPwd = pwdEdit.getText().toString();
//        String strDID = didEdit.getText().toString();

        if (strDID.length() == 0) {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.input_camera_id), Toast.LENGTH_SHORT).show();
            return;
        } //

        if (strUser.length() == 0) {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.input_camera_user), Toast.LENGTH_SHORT).show();
            return;
        }

        if (option == ContentCommon.INVALID_OPTION) {
            option = ContentCommon.ADD_CAMERA;
        }

        in.putExtra(ContentCommon.CAMERA_OPTION, option);
        in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
        in.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
        in.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
        in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);
//        progressBar.setVisibility(View.VISIBLE);
        if (dialogUtil != null)
            dialogUtil.loadDialog();
        SystemValue.deviceName = strUser;
        SystemValue.deviceId = strDID;
        SystemValue.devicePass = strPwd;
        BridgeService.setIpcamClientInterface(this);
        NativeCaller.Init();
        new Thread(new StartPPPPThread()).start();
    }


    /**
     * 测试水浸传感器
     */
    private void water_sensor() {
        startActivity(new Intent(getActivity(), WaterSensorActivity.class));
    }

    /**
     * 测试空调
     */
    private void test_air_control(String mac) {
        doit_wifi = "air";
        common_control("air", mac);
    }

    /**
     * 共同控制
     */
    private void common_control(String doit, String mac) {
//        doit_wifi = doit;
//        if (mGizWifiDevice == null) {
////            getActivity().runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    if(dialogUtil != null) dialogUtil.loadDialog();
////                }
////            });
////            handler.sendEmptyMessage(0);
////            add_from_net("onclick");
////            init_giz_wifi_dev("onclick");
//            init_giz_wifi_dev("onclick", mac);
//        } else {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (dialogUtil != null) dialogUtil.removeDialog();
//                }
//            });
//        }
        String apple_name = "";
        for (int i = 0; i < list_hand_scene.size(); i++) {
            if (list_hand_scene.get(i).get("controllerId").equals(mac)) {
                apple_name = list_hand_scene.get(i).get("name").toString();

            }
        }
        if (wifiDevices.size() != 0) {
            get_to_wifi(mac, apple_name);//绑定订阅
            to_control(doit);
        } else {
            ToastUtil.showToast(getActivity(), "请与" + apple_name
                    +
                    "在同一网络后再控制");
        }
    }

    /**
     * 获取门磁等第三方设备
     */
    private void getOtherDevices() { // ----
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialogUtil != null) {
                    dialogUtil.loadDialog();
                }
            }
        });

        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(getActivity()));
//        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(SelectSensorActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_getWifiAppleInfos
                , mapdevice, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {//刷新togglen数据
                        getOtherDevices();
                    }
                }, getActivity(), dialogUtil) {
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
                    }
                });
    }


    /**
     * get_to_wifi
     *
     * @param mac
     * @param apple_name
     */
    private void get_to_wifi(String mac, String apple_name) {
        mGizWifiDevice = null;
        for (int i = 0; i < wifiDevices.size(); i++) {
            if (wifiDevices.get(i).getMacAddress().equals(mac)) {
                mGizWifiDevice = wifiDevices.get(i);
            }
        }
        if (!Utility.isEmpty(mGizWifiDevice)) { //
//            Object json = JSON.toJSON(mGizWifiDevice);
//            String str = new Gson().toJson(json);
            mDeviceManager.bindRemoteDevice(mGizWifiDevice);
            final GizWifiDevice finalMGizWifiDevice = mGizWifiDevice;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDeviceManager.setSubscribe(finalMGizWifiDevice, true);
                }
            }, 1000);
        } else {
            ToastUtil.showToast(getActivity(), "请与" + apple_name
                    +
                    "在同一网络后再控制");
            return;
        }
    }

    /**
     * 跳转到空调控制界面
     */
    private void to_control(String doit) {
        String mac = mapdevice.get("mac").toString();
        String rid = mapdevice.get("deviceId").toString();
        boolean issame = false;
        list_remotecontrol_air = SharedPreferencesUtil.getInfo_List(getActivity(), "remoteairlist");
        for (int i = 0; i < list_remotecontrol_air.size(); i++) {
            if (rid.equals(list_remotecontrol_air.get(i).get("rid"))) {
                issame = true;
                remoteControl_map_air = list_remotecontrol_air.get(i);
                break;
            } else {
                issame = false;
            }
        }

        if (issame) {
            switch (doit) {
                case "air":
                    toControl(mapdevice);
                    break;
                case "tv":
                    toControl_TV(mapdevice);
                    break;
            }
        } else {
            switch (doit) {
                case "air":
                    new DownloadThread("getDetailByRCID", mac, rid).start();//下载该遥控器编码
                    break;
                case "tv":
                    new DownloadThread("getDetailByRCID_TV", mac, rid).start();//下载该遥控器编码
                    break;
            }
        }
    }


    /**
     * 跳转到控制界面
     */
    private void toControl(Map mapdevice) {
        if (mGizWifiDevice == null) {
//            ToastUtil.showToast(getActivity(), "设备，null");
            return;
        }
        Intent intent = new Intent(getActivity(), AirControlActivity.class);
        intent.putExtra("GizWifiDevice", mGizWifiDevice);
        intent.putExtra("mapdevice", (Serializable) mapdevice);
        //remoteControl
//        intent.putExtra("remoteControl", remoteControl);
//        Map map_rcommand = remoteControl.getRcCommand();
        intent.putExtra("map_rcommand", (Serializable) remoteControl_map_air);
        startActivity(intent);
    }


    /**
     * 跳转到TV 控制界面
     */
    private void toControl_TV(Map mapdevice) {
        if (mGizWifiDevice == null) {
//            ToastUtil.showToast(getActivity(), "设备，null");
            return;
        }
        Intent intent = new Intent(getActivity(), TVShowActivity.class);
        intent.putExtra("GizWifiDevice", mGizWifiDevice);
        intent.putExtra("mapdevice", (Serializable) mapdevice);
        //remoteControl
//        intent.putExtra("remoteControl", remoteControl);
//        Map map_rcommand = remoteControl.getRcCommand();
        intent.putExtra("map_rcommand", (Serializable) remoteControl_map_air);
        startActivity(intent);
    }


    class DownloadThread extends Thread {
        private String viewId;
        String result = "";
        private String mac;
        private String rid;

        public DownloadThread(String viewId, String mac, String rid) {
            this.viewId = viewId;
            this.mac = mac;
            this.rid = rid;
        }

        @Override
        public void run() {

            final Message message = mHandler.obtainMessage();
            switch (viewId) {
                case "getDetailByRCID":
                    get_air_control_brand(message);
                    break;
                case "getDetailByRCID_TV":
                    get_air_control_brand_tv(message);
                    break;
                default:
                    break;
            }
        }

        /**
         * 获取空调编码
         *
         * @param message
         */
        private void get_air_control_brand(final Message message) {
            if (!mac.equals("")) {
                if (ykanInterface == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogUtil.loadDialog();
                    }
                });
                ykanInterface
                        .getRemoteDetails(mac, rid, new YKanHttpListener() {
                            @Override
                            public void onSuccess(BaseResult baseResult) {
                                if (baseResult != null) {
                                    add_remotes((RemoteControl) baseResult);
                                }
                            }

                            /**
                             * 添加遥控器
                             * @param baseResult
                             */
                            private void add_remotes(RemoteControl baseResult) {
                                index = 0;
                                remoteControl = baseResult;
                                result = remoteControl.toString();

                                //在这里保持遥控器红外码列表
                                HashMap<String, KeyCode> map = remoteControl.getRcCommand();
                                Map map_send = new HashMap();
                                map_send.put("mac", mac);
                                map_send.put("rid", rid);
                                Set<String> set = map.keySet();
                                for (String s : set) {
                                    if (map.get(s) == null) {
                                        continue;
                                    }

                                    if (map.get(s).getSrcCode() == null) {
                                        continue;
                                    }
                                    map_send.put(s, map.get(s).getSrcCode());
                                }
                                list_remotecontrol_air.add(map_send);
                                remoteControl_map_air = map_send;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferencesUtil.saveInfo_List(getActivity(), "remoteairlist", list_remotecontrol_air);
                                        dialogUtil.removeDialog();
                                        message.what = 1;
                                        message.obj = result;
                                        mHandler.sendMessage(message);
                                    }
                                });
                                //下载好遥控码后
                            }

                            @Override
                            public void onFail(final YKError ykError) {
                                Log.e(TAG, "ykError:" + ykError.toString());
                                handler.sendEmptyMessage(6);
                                result = "error";
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        dialogUtil.removeDialog();
//                                        ToastUtil.showToast(getActivity(),
//                                                ykError.getError());
                                    }
                                });
                            }
                        });
            } else {
                result = "请调用匹配数据接口";
                Log.e(TAG, " getDetailByRCID 没有遥控器设备对象列表");
            }
            Log.d(TAG, " getDetailByRCID result:" + result);
//            if (result.equals("")) {
//                handler.sendEmptyMessage(7);//
//            }

        }

        /**
         * 获取电视机遥控码
         *
         * @param message
         */
        private void get_air_control_brand_tv(final Message message) {
            if (!mac.equals("")) {
                if (ykanInterface == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogUtil.loadDialog();
                    }
                });
                ykanInterface
                        .getRemoteDetails(mac, rid, new YKanHttpListener() {
                            @Override
                            public void onSuccess(BaseResult baseResult) {
                                if (baseResult != null) {
                                    add_remotes((RemoteControl) baseResult);
                                }
                            }

                            /**
                             * 添加遥控器
                             * @param baseResult
                             */
                            private void add_remotes(RemoteControl baseResult) {
                                index = 0;
                                remoteControl = baseResult;
                                result = remoteControl.toString();
//                                            list_remotecontrol = new ArrayList<>();
                                //在这里保持遥控器红外码列表
                                HashMap<String, KeyCode> map = remoteControl.getRcCommand();
                                Map map_send = new HashMap();
                                map_send.put("mac", mac);
                                map_send.put("rid", rid);
                                Set<String> set = map.keySet();
                                for (String s : set) {
                                    if (map.get(s) == null) {
                                        continue;
                                    }

                                    if (map.get(s).getSrcCode() == null) {
                                        continue;
                                    }
                                    map_send.put(s, map.get(s).getSrcCode());
                                }
                                list_remotecontrol_air.add(map_send);
                                remoteControl_map_air = map_send;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferencesUtil.saveInfo_List(getActivity(), "remoteairlist", list_remotecontrol_air);
                                        dialogUtil.removeDialog();
                                        message.what = 2;
                                        message.obj = result;
                                        mHandler.sendMessage(message);
                                    }
                                });
                                //下载好遥控码后
                            }

                            @Override
                            public void onFail(final YKError ykError) {
                                Log.e(TAG, "ykError:" + ykError.toString());
                                handler.sendEmptyMessage(6);
                                result = "error";
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        dialogUtil.removeDialog();
//                                        ToastUtil.showToast(getActivity(),
//                                                ykError.getError());
                                    }
                                });
                            }
                        });
            } else {
                result = "请调用匹配数据接口";
                Log.e(TAG, " getDetailByRCID 没有遥控器设备对象列表");
            }
            Log.d(TAG, " getDetailByRCID result:" + result);
//            if (result.equals("")) {
//                handler.sendEmptyMessage(7);//
//            }
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    if (!Utility.isEmpty(remoteControl)) {
//                        airVerSion = remoteControl.getVersion();
//                        codeDatas = remoteControl.getRcCommand();
//                        airEvent = getAirEvent(codeDatas);
//                    }
                    toControl(mapdevice);
                    break;
                case 2:
                    toControl_TV(mapdevice);
                    break;
            }
        }
    };


    /**
     * 测试电视
     */
    private void test_tv(String mac) {
        doit_wifi = "tv";
        common_control("tv", mac);
    }

    /**
     * 测试pm2.5
     */
    private void test_pm25() {
        Intent intent = new Intent(getActivity(), Pm25SecondActivity.class);
        intent.putExtra("mapdevice", (Serializable) this.mapdevice);
        startActivity(intent);
    }

    /**
     * 门磁，水浸，人体感应，入墙PM2.5
     *
     * @param mapalldevice
     */
    private void special_type_device(Map<String, Object> mapalldevice) {
        Bundle bundle = new Bundle();
        bundle.putString("type", (String) mapalldevice.get("type"));
        bundle.putString("name", (String) mapalldevice.get("name"));
        bundle.putString("number", (String) mapalldevice.get("number"));
        bundle.putString("name1", (String) mapalldevice.get("name1"));
        bundle.putString("name2", (String) mapalldevice.get("name2"));
        bundle.putString("panelName", (String) mapalldevice.get("panelName"));
        bundle.putString("status", (String) mapalldevice.get("status"));
        bundle.putString("dimmer", (String) mapalldevice.get("dimmer"));
        bundle.putString("mode", (String) mapalldevice.get("mode"));
        IntentUtil.startActivity(getActivity(), MacdetailActivity.class, bundle);
    }

    /**
     * curtains and light,窗帘与灯
     *
     * @param position
     * @param mapalldevice
     */
    private void curtains_and_light(int position, Map<String, Object> mapalldevice) {
        if (list.get(position).get("type").toString().equals("1") || list.get(position).get("type").toString().equals("11")
                || list.get(position).get("type").toString().equals("16")
                || list.get(position).get("type").toString().equals("15")
                || list.get(position).get("type").toString().equals("17")) {
            String boxstatus = TokenUtil.getBoxstatus(getActivity());
            if (!boxstatus.equals("0")) {
                getBoxStatus(mapalldevice, position);
            }
        } else {
            /*窗帘所需要的属性值*/
            Log.e("zhu", "chuanglian:" + "窗帘所需要的属性值");
            Bundle bundle = new Bundle();
            bundle.putString("type", list.get(position).get("type").toString());
            bundle.putString("number", list.get(position).get("number").toString());
            bundle.putString("name1", list.get(position).get("name1").toString());
            bundle.putString("name2", list.get(position).get("name2").toString());
            bundle.putString("name", list.get(position).get("name").toString());
            LogUtil.eLength("名字", list.get(position).get("name1").toString() + list.get(position).get("name2").toString());
            IntentUtil.startActivity(getActivity(), LamplightActivity.class, bundle);
        }
    }

    private void getBoxStatus(final Map<String, Object> mapdevice, final int position) {
        Map<String, String> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        dialogUtil.loadDialog();
        get_mac_fragment(mapdevice, position, map);
    }

    private void get_mac_fragment(final Map<String, Object> mapdevice, final int position, final Map<String, String> map) {
        MyOkHttp.postMapString(ApiHelper.sraum_getBoxStatus, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//这个是获取togglen来刷新数据
                getBoxStatus(mapdevice, position);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                switch (user.status) {
                    case "1":
                    case "17":
                        sraum_device_control(mapdevice);
                        break;
                    case "0":
                        //网关离线
                        ToastUtil.showDelToast(getActivity(), "网关处于离线状态");
                        break;
                    default:
                        break;
                }
            }

            /**
             * sraum_device_control
             * @param
             */
            private void sraum_device_control(Map<String, Object> mapdevice) {
                MyOkHttp.postMapObject(ApiHelper.sraum_deviceControl, mapdevice, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        Map<String, Object> mapalldevice = new HashMap<String, Object>();
                        mapalldevice.put("token", TokenUtil.getToken(getActivity()));
                        mapalldevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));

                        mapalldevice.put("deviceInfo", listob);
                        sraum_device_control(mapalldevice);
                    }
                }, getActivity(), dialogUtil) {
                    @Override
                    public void fourCode() {
                        super.fourCode();
                        switch (listob.get(0).get("type").toString()) {
                            case "11":
                                ToastUtil.showToast(getActivity(), "恢复失败");
                                break;
                            default:
                                ToastUtil.showToast(getActivity(), "操作失败");
                                break;
                        }
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);

                        switch (listob.get(0).get("type").toString()) {
                            case "11":
                                ToastUtil.showToast(getActivity(), "恢复成功");
                                break;
                            default:
                                ToastUtil.showToast(getActivity(), "操作成功");
                                break;
                        }

                        if (vibflag) {
                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(200);
                        }

                        if (musicflag) {
                            LogUtil.i("铃声响起");
                            MusicUtil.startMusic(getActivity(), 1, "");
                        } else {
                            MusicUtil.stopMusic(getActivity(), "");
                        }
                        listtype.set(position, status);
                        String string = listtype.get(position);
//                        if (string.equals("1")) {
//                            itemrela_id.setBackgroundResource(R.drawable.markstarh);
//                        } else {
//                            itemrela_id.setBackgroundResource(R.drawable.markh);
//                        }
                        upload(true);
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private int tag = 0;

    @Override
    public void onDestroy() { // ----
        getActivity().unregisterReceiver(mMessageReceiver);
        getActivity().unregisterReceiver(receiver);
        NativeCaller.Free();
        Intent intent = new Intent();
        intent.setClass(getActivity(), BridgeService.class);
        getActivity().stopService(intent);
        tag = 0;
        EventBus.getDefault().unregister(this);
//        over_camera_list();
        super.onDestroy();
    }


    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.jr.treceiver";
    public static String ACTION_INTENT_RECEIVER_TO_SECOND_PAGE = "com.massky.secondpage.treceiver";

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        filter.addAction(MESSAGE_TONGZHI_DOOR_FIRST);
        filter.addAction(MESSAGE_TONGZHI_VIDEO_FROM_MYDEVICE);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {
                int messflag = intent.getIntExtra("notifactionId", 0);
                if (messflag == 1 || messflag == 3 || messflag == 4 || messflag == 5) {
                    upload(false);//控制部分，推送刷新；主动推送刷新。
                    Log.e("zhu", "upload(false):" + "upload(false)" + "messflag:" + messflag);
                    //控制部分的二级页面进去要同步更新推送的信息显示 （推送的是消息）。
                    sendBroad();
                    //推送过来的
//                    ToastUtil.showToast(getActivity(), "我控制的设备时推送过来的" + ",messflag:" + messflag);
                }
            } else if (intent.getAction().equals(MESSAGE_TONGZHI_DOOR_FIRST)) {//门铃视频预览
                type = (String) intent.getSerializableExtra("type");

                Map mapdevice = new HashMap();
                mapdevice.put("dimmer", "admin");
                mapdevice.put("temperature", "888888");
                mapdevice.put("mode", (String) intent.getSerializableExtra("uid"));//
                //VSTD128380HDAFD

//                mapdevice.put("mode", "VSTD128380HDAFD");//
//                done(mapdevice.get("dimmer").toString()
//                        , mapdevice.get("temperature").toString()
//                        , mapdevice.get("mode").toString());//String strUser,String strPwd,String strDID
                int index = (int) SharedPreferencesUtil.getData(context, "tongzhi_time", 1);
                index--;
                SharedPreferencesUtil.saveData(context, "tongzhi_time", index);
                if (index == 0) {
                    videofrom = "macfragment";
                    onitem_wifi_shexiangtou(mapdevice);
                }
            } else if (intent.getAction().equals(MESSAGE_TONGZHI_VIDEO_FROM_MYDEVICE)) {//来自设备页获取摄像头状态的通知
                videofrom = "devicefragment";
                video_item = (Map) intent.getSerializableExtra("video_item");
                common_video(video_item);
            }
        }
    }

    private void sendBroad() {
        Intent mIntent = new Intent(ACTION_INTENT_RECEIVER_TO_SECOND_PAGE);
        getActivity().sendBroadcast(mIntent);


//        //MACFRAGMENT_PM25
    }


    private void sendBroad_pm25(Map map) {
        Intent mIntent_pm25 = new Intent(MACFRAGMENT_PM25);
        mIntent_pm25.putExtra("mapdevice", (Serializable) mapdevice);
        getActivity().sendBroadcast(mIntent_pm25);
    }

    @Override
    public void onResume() {
        super.onResume();
//        init_giz_wifi_dev("onResume");
        blagg = true;
        mDeviceManager.setGizWifiCallBack(mGizWifiCallBack);
        update(mDeviceManager.getCanUseGizWifiDevice());
        getOtherDevices();
//            test();
        //

//        test();
//        add_from_net("onclick");
//        Log.e("robin debug","gizWifiDevice->mac:" + gizWifiDevice.getMacAddress());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (myWifiThread != null) {
            blagg = false;
        }
        NativeCaller.StopSearch();
    }

    StringBuffer stringbuffer = new StringBuffer();
    private int connection_wifi_camera_index;
    private Handler PPPPMsgHandler = new Handler() {
        public void handleMessage(Message msg) {

            Bundle bd = msg.getData();
            int msgParam = bd.getInt(STR_MSG_PARAM);
            //        bd.putString(STR_DID, did);
//            String  did = bd.getString(STR_DID);
            int msgType = msg.what;
            Log.i("aaa", "====" + msgType + "--msgParam:" + msgParam);
            String did = bd.getString(STR_DID);
            switch (msgType) {
                case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:
                    int resid;
                    switch (msgParam) {
                        case ContentCommon.PPPP_STATUS_CONNECTING://0
                            resid = R.string.pppp_status_connecting;
                            Log.e("fei->", "resid:" + "正在连接");
                            if (stringbuffer.toString().contains("未知状态,"))
                                stringbuffer.append("正在连接");
                            tag = 2;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_FAILED://3
                            resid = R.string.pppp_status_connect_failed;
                            Log.e("fei->", "resid:" + "连接失败");
                            tag = 0;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            connection_wifi_camera_index++;
                            if (connection_wifi_camera_index <= 10)
                                handler.sendEmptyMessage(11);
                            break;
                        case ContentCommon.PPPP_STATUS_DISCONNECT://4
                            resid = R.string.pppp_status_disconnect;
                            Log.e("fei->", "resid:" + "断线");
                            tag = 0;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_INITIALING://1
                            resid = R.string.pppp_status_initialing;
                            Log.e("fei->", "resid:" + "已连接吗，正在初始化");
                            tag = 2;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_INVALID_ID://5
                            resid = R.string.pppp_status_invalid_id;
                            Log.e("fei->", "resid:" + "ID号无效");
//                            progressBar.setVisibility(View.GONE);
                            tag = 0;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_ON_LINE://2 在线状态
                            resid = R.string.pppp_status_online;
                            Log.e("fei->", "resid:" + "在线");
                            connection_wifi_camera_index = 0;
                            if (stringbuffer.toString().contains("未知状态,正在连接"))
                                stringbuffer.append(",在线");
                            //摄像机在线之后读取摄像机类型
                            String cmd = "get_status.cgi?loginuse=admin&loginpas=" + SystemValue.devicePass
                                    + "&user=admin&pwd=" + SystemValue.devicePass;
                            NativeCaller.TransferMessage(did, cmd, 1);
                            tag = 1;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            handler.sendEmptyMessage(10);//设备已经在线了
                            break;
                        case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE://6
                            resid = R.string.device_not_on_line;
                            Log.e("fei->", "resid:" + "摄像机不在线");
                            tag = 0;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT://7
                            resid = R.string.pppp_status_connect_timeout;
                            Log.e("fei->", "resid:" + "连接超时");
                            tag = 0;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_ERRER://8
                            resid = R.string.pppp_status_pwd_error;
                            Log.e("fei->", "resid:" + "错误密码");
                            tag = 0;
                            if (dialogUtil != null) dialogUtil.removeDialog();
                            break;
                        default:
                            resid = R.string.pppp_status_unknown;
                            Log.e("fei->", "resid:" + "未知状态");
                            stringbuffer = new StringBuffer();
                            stringbuffer.append("未知状态,");
                            if (dialogUtil != null) dialogUtil.removeDialog();
                    }

                    init_Camera(did);

                    /*      textView_top_show.setText(getResources().getString(resid));*/
                    if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
                        NativeCaller.PPPPGetSystemParams(did, ContentCommon.MSG_TYPE_GET_PARAMS);
                    }
                    if (msgParam == ContentCommon.PPPP_STATUS_INVALID_ID
                            || msgParam == ContentCommon.PPPP_STATUS_CONNECT_FAILED
                            || msgParam == ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE
                            || msgParam == ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT
                            || msgParam == ContentCommon.PPPP_STATUS_CONNECT_ERRER) {
                        NativeCaller.StopPPPP(did);
                    }
                    break;
                case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
                    break;
            }
        }
    };

    /**
     * 初始化摄像头列表
     *
     * @param did
     */
    private void init_Camera(String did) {
        int index = 0;
        Map map = new HashMap();
        map.put("did", did);
        map.put("tag", tag);
        for (int i = 0; i < list_wifi_camera.size(); i++) {
            if (list_wifi_camera.get(i).get("did").equals(did)) {
                list_wifi_camera.get(i).put("tag", tag);
                index--;
            } else {
                index++;
            }
        }
        if (index == list_wifi_camera.size()) {
            list_wifi_camera.add(map);
        }
        SharedPreferencesUtil.saveInfo_List(getActivity(), "list_wifi_camera_first", list_wifi_camera);
    }

    @Override
    public void BSMsgNotifyData(String did, int type, int param) {
        Log.d("ip", "type:" + type + " param:" + param);
        Bundle bd = new Bundle();
        Message msg = PPPPMsgHandler.obtainMessage();
        msg.what = type;
        bd.putInt(STR_MSG_PARAM, param);
        bd.putString(STR_DID, did);
        msg.setData(bd);
        PPPPMsgHandler.sendMessage(msg);
        if (type == ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
            intentbrod.putExtra("ifdrop", param);
            getActivity().sendBroadcast(intentbrod);
        }
    }

    @Override
    public void BSSnapshotNotify(String did, byte[] bImage, int len) {
        // TODO Auto-generated method stub
        Log.i("ip", "BSSnapshotNotify---len" + len);
    }

    @Override
    public void callBackUserParams(String did, String user1, String pwd1,
                                   String user2, String pwd2, String user3, String pwd3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void CameraStatus(String did, int status) {

    }


    @Override
    public void CallBackGetStatus(String did, String resultPbuf, int cmd) {
        // TODO Auto-generated method stub
        if (cmd == ContentCommon.CGI_IEGET_STATUS) {
            String cameraType = spitValue(resultPbuf, "upnp_status=");
            int intType = Integer.parseInt(cameraType);
            int type14 = (int) (intType >> 16) & 1;// 14位 来判断是否报警联动摄像机
            if (intType == 2147483647) {// 特殊值
                type14 = 0;
            }

            if (type14 == 1) {
                updateListHandler.sendEmptyMessage(2);
            }
        }
    }

    private String spitValue(String name, String tag) {
        String[] strs = name.split(";");
        for (int i = 0; i < strs.length; i++) {
            String str1 = strs[i].trim();
            if (str1.startsWith("var")) {
                str1 = str1.substring(4, str1.length());
            }
            if (str1.startsWith(tag)) {
                String result = str1.substring(str1.indexOf("=") + 1);
                return result;
            }
        }
        return -1 + "";
    }


    private void test() {
        Map map1 = JSON.parseObject("{\"CREATOR\":{},\"MSG_RECV\":5,\"XPGWifiDeviceHardwareFirmwareIdKey\":\"firmwareId\",\"XPGWifiDeviceHardwareFirmwareVerKey\":\"firmwareVer\",\"XPGWifiDeviceHardwareMCUHardVerKey\":\"mcuHardVer\",\"XPGWifiDeviceHardwareMCUSoftVerKey\":\"mcuSoftVer\",\"XPGWifiDeviceHardwareProductKey\":\"productKey\",\"XPGWifiDeviceHardwareWifiHardVerKey\":\"wifiHardVer\",\"XPGWifiDeviceHardwareWifiSoftVerKey\":\"wifiSoftVer\",\"alias\":\"\",\"app_sn_queue\":[],\"dest\":{\"blobAshmemSize\":0},\"deviceMcuFirmwareVer\":\"\",\"deviceModuleFirmwareVer\":\"\",\"did\":\"KSgszGjyN9cNyVfUuAYRLo\",\"flags\":0,\"han\":{\"looper\":{\"currentThread\":false,\"queue\":{\"idle\":true,\"polling\":true},\"thread\":{\"alive\":true,\"daemon\":false,\"id\":1,\"interrupted\":false,\"name\":\"main\",\"priority\":5,\"stackTrace\":[{\"className\":\"android.os.MessageQueue\",\"fileName\":\"MessageQueue.java\",\"lineNumber\":-2,\"methodName\":\"nativePollOnce\",\"nativeMethod\":true},{\"className\":\"android.os.MessageQueue\",\"fileName\":\"MessageQueue.java\",\"lineNumber\":356,\"methodName\":\"next\",\"nativeMethod\":false},{\"className\":\"android.os.Looper\",\"fileName\":\"Looper.java\",\"lineNumber\":138,\"methodName\":\"loop\",\"nativeMethod\":false},{\"className\":\"android.app.ActivityThread\",\"fileName\":\"ActivityThread.java\",\"lineNumber\":6523,\"methodName\":\"main\",\"nativeMethod\":false},{\"className\":\"java.lang.reflect.Method\",\"fileName\":\"Method.java\",\"lineNumber\":-2,\"methodName\":\"invoke\",\"nativeMethod\":true},{\"className\":\"com.android.internal.os.ZygoteInit$MethodAndArgsCaller\",\"fileName\":\"ZygoteInit.java\",\"lineNumber\":942,\"methodName\":\"run\",\"nativeMethod\":false},{\"className\":\"com.android.internal.os.ZygoteInit\",\"fileName\":\"ZygoteInit.java\",\"lineNumber\":832,\"methodName\":\"main\",\"nativeMethod\":false}],\"state\":\"RUNNABLE\",\"threadGroup\":{\"daemon\":false,\"destroyed\":false,\"maxPriority\":10,\"name\":\"main\",\"parent\":{\"daemon\":false,\"destroyed\":false,\"maxPriority\":10,\"name\":\"system\"}},\"uncaughtExceptionHandler\":{\"$ref\":\"$.han.looper.thread.threadGroup\"}}}},\"hand\":{\"looper\":{\"$ref\":\"$.han.looper\"}},\"handler\":{\"looper\":{\"$ref\":\"$.han.looper\"}},\"hasProductDefine\":true,\"ipAddress\":\"192.168.169.219\",\"isBind\":false,\"isDisabled\":false,\"isLAN\":true,\"loginning\":false,\"logintimeout\":0,\"mListener\":{},\"macAddress\":\"5CCF7FB6C07C\",\"netStatus\":\"GizDeviceOnline\",\"notify_status_sn\":0,\"oldIsConnected\":false,\"oldIsOnline\":true,\"productKey\":\"0f998d408465430ea435b48f58a7ac3b\",\"productName\":\"小苹果\",\"productType\":\"GizDeviceNormal\",\"productUI\":\"{\\\"object\\\":{\\\"version\\\":4,\\\"showEditButton\\\":false},\\\"sections\\\":[{\\\"elements\\\":[{\\\"boolValue\\\":false,\\\"object\\\":{\\\"action\\\":\\\"entity0\\\",\\\"bind\\\":[\\\"entity0.switch\\\"],\\\"perm\\\":\\\"W\\\"},\\\"type\\\":\\\"QBooleanElement\\\",\\\"key\\\":\\\"entity0.switch\\\",\\\"title\\\":\\\"开关（未使用）\\\"},{\\\"items\\\":[\\\"低风\\\",\\\"中风\\\",\\\"高风\\\"],\\\"object\\\":{\\\"action\\\":\\\"entity0\\\",\\\"bind\\\":[\\\"entity0.fan_speed\\\"],\\\"perm\\\":\\\"W\\\"},\\\"type\\\":\\\"QRadioElement\\\",\\\"key\\\":\\\"entity0.fan_speed\\\",\\\"title\\\":\\\"风速（未使用）\\\"},{\\\"keyboardType\\\":\\\"NumbersAndPunctuation\\\",\\\"title\\\":\\\"遥控码\\\",\\\"object\\\":{\\\"action\\\":\\\"entity0\\\",\\\"bind\\\":[\\\"entity0.cmd_key\\\"],\\\"perm\\\":\\\"W\\\"},\\\"value\\\":0,\\\"key\\\":\\\"entity0.cmd_key\\\",\\\"maxLength\\\":1800,\\\"type\\\":\\\"QMultilineElement\\\"},{\\\"object\\\":{\\\"action\\\":\\\"entity0\\\",\\\"bind\\\":[\\\"entity0.room_temp\\\"],\\\"uint_spec\\\":{\\\"max\\\":35,\\\"step\\\":1,\\\"min\\\":-10},\\\"perm\\\":\\\"N\\\"},\\\"type\\\":\\\"QLabelElement\\\",\\\"key\\\":\\\"entity0.room_temp\\\",\\\"title\\\":\\\"室内温度（未使用）\\\"}]}]}\",\"remark\":\"\",\"sharingRole\":\"GizDeviceSharingNormal\",\"subscribed\":true}");
        /*        mGizWifiDevice = EntityUtils.mapToEntity(map1, GizWifiDevice.CREATOR);*/
//        parcel.setDataPosition(0);
//        String  macAddress = parcel.readString();

        //GizWifiDevice gizWifiDevice = GizWifiDevice.CREATOR.createFromParcel(parcel);

//        Log.e("robin debug", "gizWifiDevice:"+ gizWifiDevice.getMacAddress());
        deviceInfo = (String) SharedPreferencesUtil.getData(getActivity(), "mGizWifiDevice", "");
        Map map = JSON.parseObject(deviceInfo);
        GizWifiDevice gizWifiDevice = EntityUtils.mapToEntity(map, GizWifiDevice.CREATOR);
        if (gizWifiDevice != null) {
            Log.e("robin debug", "gizWifiDevice:" + gizWifiDevice.getMacAddress());//执行两边EntityUtils.mapToEntity(map,GizWifiDevice.CREATOR);
            //就可以获取该GizWifiDevice对象了
        }
//        Log.e("robin debug", "gizWifiDevice:"+ gizWifiDevice.getMacAddress());
    }

//    private void init_giz_wifi_dev(String onclick, String mac) {
////        if (dialogUtil != null) dialogUtil.removeDialog();
//        handler.sendEmptyMessage(0);
//        mDeviceManager.setGizWifiCallBack(mGizWifiCallBack);
//        Log.e("peng", "MacFragment->onResume:name:");
////        add_from_net(onclick);
//        getWifiApples(onclick, mac);
//    }

    /**
     * 从网上加载并保存到本地
     *
     * @param onclick
     */
    boolean isok;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void add_from_net(final String onclick) {
        final GizWifiDevice[] gizWifiDevice = {null};
        deviceInfo = (String) SharedPreferencesUtil.getData(getActivity(), "mGizWifiDevice", "");
//        if (!deviceInfo.equals("")) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //gizWifiDevice[0] = ParceUtil.unmarshall(deviceInfo, GizWifiDevice.CREATOR);
                GizWifiDevice gizWifiDevice1 = EntityUtils.mapToEntity(null, GizWifiDevice.CREATOR);
                if (gizWifiDevice1 != null) {
                    mGizWifiDevice = gizWifiDevice1;
                    isok = true;
                    Log.e("robin debug", "mGizWifiDevice:" + mGizWifiDevice.getMacAddress());
                    switch (onclick) {
                        case "onResume":
                            handler.sendEmptyMessage(1);
                            break;
                        case "onclick":
                            handler.sendEmptyMessage(2);
                            break;
                    }
                } else {
                    index_click++;
                    if (index_click >= 24) {
                        isok = false;
                        index_click = 0;
                        handler.sendEmptyMessage(3);
                        switch (onclick) {
                            case "onclick":
                                if (mGizWifiDevice == null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                                ToastUtil.showToast(getActivity(), "设备，null");
                                        }
                                    });
                                }
                                break;
                        }
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mDeviceManager.setGizWifiCallBack(mGizWifiCallBack);
                        add_from_net(onclick);
                    }
                }
            }
        }
        ).start();
//            } else {
//                getWifiApples(onclick);
//            }
    }
//    }
}


//    /**
//     * 添加小苹果遥控器编码列表
//     */
//    public interface AddRemoteController {
//        void add_remotecontrol();
//
//        void deleteRemoteControl();
//    }


