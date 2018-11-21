package com.massky.sraum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AbstractThreadedSyncAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.App;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.NetUtils;
import com.Util.SharedPreferencesUtil;
import com.Util.SlidingUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.Util.UpdateManager;
import com.Util.VersionUtil;
import com.alibaba.fastjson.JSON;
import com.base.Basecfragmentactivity;
import com.data.User;
import com.dialog.CommonData;
import com.dialog.ToastUtils;
import com.fragment.AboutFragment;
import com.fragment.LeftFragment;
import com.fragment.MacdeviceFragment;
import com.fragment.Mainviewpager;
import com.fragment.MessageFragment;
import com.fragment.MyDeviceFragment;
import com.fragment.MyRoomFragment;
import com.fragment.MygatewayFragment;
import com.fragment.MysceneFragment;
import com.fragment.MysetFragment;
import com.fragment.PanelFragment;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.ipcamera.demo.BridgeService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jpush.Constants;
import com.jpush.ExampleUtil;
import com.larksmart7618.sdk.communication.tools.commen.ToastTools;
import com.permissions.RxPermissions;
import com.service.NotificationMonitorService;
import com.yaokan.sdk.api.YkanSDKManager;
import com.yaokan.sdk.ir.InitYkanListener;
import com.yaokan.sdk.utils.Logger;
import com.yaokan.sdk.utils.Utility;
import com.yaokan.sdk.wifi.DeviceManager;
import com.yaokan.sdk.wifi.GizWifiCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import vstc2.nativecaller.NativeCaller;

import static com.fragment.LeftFragment.MESSAGE_TONGZHI;
import static com.massky.sraum.R.id.addscene_id;
import static com.massky.sraum.R.id.swibtnone;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于主界面设置*/
public class MainfragmentActivity extends Basecfragmentactivity implements Mainviewpager.MyInterface, InitYkanListener {
    public static final String MESSAGE_RECEIVED_FROM_ABOUT_FRAGMENT = "com.massky.sraum.from_about_fragment";
    private static final int TONGZHI_APK_UPGRATE = 0x0012;
    //    @InjectView(R.id.sideslip_id)
//    RelativeLayout sideslip_id;
//    @InjectView(R.id.addrelative_id)
//    RelativeLayout addrelative_id;
//    @InjectView(R.id.addimage_id)
//    ImageView addimage_id;
//    @InjectView(R.id.cenimage_id)
//    ImageView cenimage_id;
//    @InjectView(R.id.centext_id)
//    TextView centext_id;
    public SlidingUtil slidingUtil;
    private Fragment mContent;
    public SlidingMenu menu;
    PopupWindow popupWindow;
    //    private RelativeLayout addmac_id, addscene_id, addroom_id;
    //主要保存当前显示的是第几个fragment的索引值
    private Mainviewpager mainviewpager;
    private MygatewayFragment mygatewayFragment;
    private MacdeviceFragment macdeviceFragment;
    private MysceneFragment mysceneFragment;
    private MyRoomFragment myRoomFragment;
    private MysetFragment mysetFragment;
    private AboutFragment aboutFragment;
    private PanelFragment panelFragment;
    private LeftFragment leftFragment;
    private long exitTime = 0;
    private Button checkbutton_id, qxbutton_id;
    private TextView dtext_id, belowtext_id;
    private DialogUtil viewDialog;
    private String usertype, Version;
    private int versionCode;
    private DialogUtil dialogUtil;
    private MessageFragment messagefragment;
    private MyDeviceFragment mydevicefragment;
    private int index;
    private Handler handler_wifi = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            init_jizhiyun();
        }
    };
    public static final String MESSAGE_RECEIVED_ACTION_APK_LOAD = "com.Util.MESSAGE_RECEIVED_ACTION_APK_LOAD";
    private Dialog dialog1;
    private MessageReceiver mMessageReceiver_aboutfragment;
    public static final String MESSAGE_TONGZHI_DOOR = "com.massky.sraum.message.tongzhi.door";

    public static final String SRAUM_IS_DOWN_LOAD = "sraum_is_download";
    private MessageReceiver mMessageReceiver_apk_down_load;
    private WeakReference<Context> weakReference;
    private boolean iswait_down_load;//等待NotificationListenerService这个服务唤醒
    private String isdo;
    private int init_jizhiyun;//机智云index

    @Override
    protected int viewId() {
        return R.layout.main;
    }

    /**
     * 唤醒NotifacationService
     */
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//        requestRebind(ComponentName componentName) 方法来支持重新绑定。
//        NotificationMonitorService.requestRebind(new ComponentName(this, NotificationMonitorService.class));

    }

    @Override
    protected void onView() {
//        iswait_down_load = false;
        init_jizhiyun = 1;//机智云index

        dialogUtil = new DialogUtil(this);
        initPermission();
//        toggleNotificationListenerService();
        over_camera_list();//结束wifi摄像头的tag
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler_wifi.sendEmptyMessage(0);
            }
        }).start();

        Intent intent = new Intent();
        intent.setClass(MainfragmentActivity.this, BridgeService.class);
        startService(intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
                    Thread.sleep(3000);
                    Message msg = new Message();
                    NativeCaller.SetAPPDataPath(getApplicationContext().getFilesDir().getAbsolutePath());
                } catch (Exception e) {

                }
            }
        }).start();


//        /**
//         * 关闭download监听service
//         */
//        Intent intent_apk_close = new Intent();
//        intent_apk_close.setClass(MainfragmentActivity.this, NotificationMonitorService.class);
//        stopService(intent_apk_close);
//
//        /**
//         * 开启download监听service
//         */
//        Intent intent_apk_down = new Intent();
//        intent_apk_down.setClass(MainfragmentActivity.this, NotificationMonitorService.class);
//        startService(intent_apk_down);

//        showCenterDeleteDialog("需要监听sraum下载通知",
//                "是否去开启sraum通知使用权");

//        /**
//         * 开启机智云小苹果服务(service)
//         */
//        Intent intentService = new Intent(this,SimpleIntentService.class);
//        startService(intentService);

        //在这里发送广播，expires_in是86400->24小时
        String expires_in = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this, "expires_in", "");
        Intent broadcast = new Intent("com.massky.sraum.broadcast");
        broadcast.putExtra("expires_in", expires_in);
        broadcast.putExtra("timestamp", TokenUtil.getLogintime(MainfragmentActivity.this));
        sendBroadcast(broadcast);

//        addPopwinwow();
        versionCode = Integer.parseInt(VersionUtil.getVersionCode(MainfragmentActivity.this));
        slidingUtil = new SlidingUtil(MainfragmentActivity.this);
        menu = slidingUtil.initSlidingMenu();
        if (mContent == null) {
            usertype = IntentUtil.getIntentString(MainfragmentActivity.this, "addflag");
            LogUtil.eLength("这是数据", usertype + "s数据问题");
            if (usertype.equals("1")) {
                setTabSelection(5, "", "");
            } else {
                setTabSelection(0, "", "");
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commitAllowingStateLoss();
//        sideslip_id.setOnClickListener(this);
        getDialog();
//        boolean netflag = NetUtils.isNetworkConnected(MainfragmentActivity.this);
//        if (netflag) {//获取版本号
////            if (!isEnabled()) {
////                if (!dialog1.isShowing()) {
////                    dialog1.show();
////                }
////            } else {
////                updateApk();
////            }
//
//            updateApk("oncreate");
//        }
        registerMessageReceiver();
        registerMessageReceiver_fromAbout();
//        registerMessageReceiver_fromApk_Down();
        init_notifacation();//通知初始化
        SharedPreferencesUtil.saveData(MainfragmentActivity.this, "loadapk", false);//apk版本正在更新中
    }

    // 判断是否打开了通知监听权限
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取文件长度
     */
    public long getFileSize(File file) {
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            System.out.println("文件" + fileName + "的大小是：" + file.length());
            return file.length();
        }
        return 0;
    }

    public static long SDCardSizeTest(File apkFile) {
//
//           // 取得SDCard当前的状态
//           String sDcString = android.os.Environment.getExternalStorageState();
//
//           if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
//
//                   // 取得sdcard文件路径
//                   File pathFile = android.os.Environment
//                            .getExternalStorageDirectory();

        android.os.StatFs statfs = new android.os.StatFs(apkFile.getPath());

        // 获取SDCard上BLOCK总数
        long nTotalBlocks = statfs.getBlockCount();

        // 获取SDCard上每个block的SIZE
        long nBlocSize = statfs.getBlockSize();

        // 获取可供程序使用的Block的数量
        long nAvailaBlock = statfs.getAvailableBlocks();

        // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
        long nFreeBlock = statfs.getFreeBlocks();

        // 计算SDCard 总容量大小MB
        long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;

        // 计算 SDCard 剩余大小MB
        long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;

        return (nTotalBlocks * nBlocSize - nAvailaBlock * nBlocSize) / 1024;

    }// end of if
    // end of func

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
//    public long getFileSize(File file) throws Exception {
//        long size = 0;
//        if (file.exists()) {
//            FileInputStream fis = null;
//            fis = new FileInputStream(file);
//            size = fis.available();
//            fis.close();
//        }
//        return size;
//    }

    /**
     * 初始化机智云小苹果
     */
    private void init_jizhiyun() {
        initListener();
        // 初始化SDK

//        dialogUtil.loadDialog();

        YkanSDKManager.init(MainfragmentActivity.this, MainfragmentActivity.this);
        //需要剥离机智云的用户调用此方法初始化
//        YkanSDKManager.custInit(this,false);
        // 设置Log信息是否打印
        YkanSDKManager.getInstance().setLogger(true);
    }

    private void initListener() {
        DeviceManager.instanceDeviceManager(getApplicationContext()).setGizWifiCallBack(new GizWifiCallBack() {

            @Override
            public void didBindDeviceCd(GizWifiErrorCode result, String did) {
                super.didBindDeviceCd(result, did);
            }

            @Override
            public void didTransAnonymousUser(GizWifiErrorCode result) {
                super.didTransAnonymousUser(result);
            }

            /** 用于用户登录的回调 */
            @Override
            public void userLoginCb(GizWifiErrorCode result, String uid, String token) {
                ToastUtil.showToast(MainfragmentActivity.this, "result:" + result);
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {// 登陆成功
                    Constants.UID = uid;
                    Constants.TOKEN = token;
//                    toDeviceList();
                    //登录成功
                    //去绑定订阅
//
//                    ToastUtil.showToast(MainfragmentActivity.this, "小苹果登录成功!");
//                    wiif_login_scuess();
                    SharedPreferencesUtil.saveData(MainfragmentActivity.this, "apple_login", true);
                } else if (result == GizWifiErrorCode.GIZ_OPENAPI_USER_NOT_EXIST) {// 用户不存在

                } else if (result == GizWifiErrorCode.GIZ_OPENAPI_USERNAME_PASSWORD_ERROR) {//// 用户名或者密码错误

                } else {
//                    toast("登陆失败，请重新登录");
                    index++;
                    if (index >= 2) {

                    } else {
                        DeviceManager.instanceDeviceManager(getApplicationContext()).userLoginAnonymous();//匿名登录
                    }
                    SharedPreferencesUtil.saveData(MainfragmentActivity.this, "apple_login", false);
                }
            }

            /** 用于用户注册的回调 */
            @Override
            public void registerUserCb(GizWifiErrorCode result, String uid, String token) {


            }

            /** 用于发送验证码的回调 */
            @Override
            public void didRequestSendPhoneSMSCodeCb(GizWifiErrorCode result) {

            }

            /** 用于重置密码的回调 */
            @Override
            public void didChangeUserPasswordCd(GizWifiErrorCode result) {

            }
        });
    }

//    /**
//     * 小苹果WIFI登录成功，去绑定订阅
//     */
//    private void wiif_login_scuess() {
//        GizWifiDevice mGizWifiDevice = null;
//        List<GizWifiDevice> gizWifiDevices = DeviceManager.instanceDeviceManager(getApplicationContext()).getCanUseGizWifiDevice();
//        DeviceManager.instanceDeviceManager(getApplicationContext()).setGizWifiCallBack(mGizWifiCallBack);
//        if (gizWifiDevices != null) {
//            Log.e("MainActivity", gizWifiDevices.size() + "");
//            //红外转发器绑定设备
//            for (int i = 0; i < gizWifiDevices.size(); i++) {
//                mGizWifiDevice = gizWifiDevices.get(i);
//                // list_hand_scene
//                // 绑定选中项
//                if (!Utility.isEmpty(mGizWifiDevice) && !mGizWifiDevice.isBind()) {
//                    DeviceManager.instanceDeviceManager(getApplicationContext()).bindRemoteDevice(mGizWifiDevice);
//                    final GizWifiDevice finalMGizWifiDevice = mGizWifiDevice;
//                    handler_wifi.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            DeviceManager.instanceDeviceManager(getApplicationContext()).setSubscribe(finalMGizWifiDevice, true);
//                        }
//                    }, 1000);
//                }
//            }
//        }
//    }

    @Override
    public void onInitStart() {

    }

    @Override
    public void onInitFinish(int status, final String errorMsg) {
//         ToastUtil.showToast(MainfragmentActivity.this,"初始化成功");
//         DeviceManager.instanceDeviceManager(getApplicationContext()).userLoginAnonymous();//匿名登录
        if (dialogUtil != null)
            dialogUtil.removeDialog();
        if (status == INIT_SUCCESS) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                     ToastUtil.showToast(MainfragmentActivity.this,"SDK初始化成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DeviceManager.instanceDeviceManager(getApplicationContext()).userLoginAnonymous();
                        }
                    }, 2000);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastTools.short_Toast(MainfragmentActivity.this, errorMsg);
                    new AlertDialog.Builder(MainfragmentActivity.this).setTitle("error").setMessage(errorMsg).setPositiveButton("ok", null).create().show();
                }
            });
        }
    }

    private String TAG = "robin debug";
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
                ToastUtil.showToast(MainfragmentActivity.this, "绑定成功");
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
                ToastUtil.showToast(MainfragmentActivity.this, (isSubscribed ? "订阅" : "取消订阅") + "成功");
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
        if (gizWifiDevices != null) {
            Log.e("DeviceListActivity", gizWifiDevices.size() + "");
            Log.e("MainActivity", gizWifiDevices.size() + "");
            //红外转发器绑定设备
            for (int i = 0; i < gizWifiDevices.size(); i++) {
                mGizWifiDevice = gizWifiDevices.get(i);
                // list_hand_scene
                // 绑定选中项
                if (!Utility.isEmpty(mGizWifiDevice) && !mGizWifiDevice.isBind()) {
                    DeviceManager.instanceDeviceManager(getApplicationContext()).bindRemoteDevice(mGizWifiDevice);
                    final GizWifiDevice finalMGizWifiDevice = mGizWifiDevice;
                    handler_wifi.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DeviceManager.instanceDeviceManager(getApplicationContext()).setSubscribe(finalMGizWifiDevice, true);
                        }
                    }, 1000);
                }
            }
        }

//        adapter.notifyDataSetChanged();
    }


    private void init_notifacation() {
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getBundleExtra(Constants.EXTRA_BUNDLE);
            String title = null;//JingRuiApp
            String content = null;//2017-08-31 10:40:16,客厅,模块报警
            if (bundle != null) {
                SharedPreferencesUtil.saveData(MainfragmentActivity.this, "tongzhi_time", 1);
                //视频监控，极光push不太好用；
                init_nofication(intent);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        SharedPreferencesUtil.saveData(MainfragmentActivity.this, "tongzhi_time", 1);
        //视频监控，极光push不太好用；
        getNotify(intent);
        setIntent(intent);
    }

    private void getNotify(Intent intent) {
        init_nofication(intent); //暂时注销掉，fragment->childactivity-> fragment->mainactivity时,执行
        // 这里不是用的commit提交，用的commitAllowingStateLoss方式。commit不允许后台执行，不然会报Deferring update until onResume 错误
        super.onNewIntent(intent);
    }

    /**
     * 初始化通知
     *
     * @param intent
     */
    private void init_nofication(Intent intent) {
        if (null != intent) {
            Bundle bundle = getIntent().getBundleExtra(Constants.EXTRA_BUNDLE);
//            String title = null;//JingRuiApp
//            String content = null;//2017-08-31 10:40:16,客厅,模块报警
            if (bundle != null) {
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);////{"type":"2"}
                JSONObject json = null;
                try {
                    json = new JSONObject(extras);
                    String type = json.getString("type");
                    String uid = json.getString("uid");
                    sendBroad(type, uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static String ACTION_SRAUM_SETBOX = "ACTION_SRAUM_SETBOX";//notifactionId = 8 ->设置网关模式，sraum_setBox

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public void registerMessageReceiver_fromAbout() {
        mMessageReceiver_aboutfragment = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_FROM_ABOUT_FRAGMENT);
        registerReceiver(mMessageReceiver_aboutfragment, filter);
    }

    public void registerMessageReceiver_fromApk_Down() {
        mMessageReceiver_apk_down_load = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(SRAUM_IS_DOWN_LOAD);
        registerReceiver(mMessageReceiver_apk_down_load, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    boolean loginflag = (boolean) SharedPreferencesUtil.getData(CommonData.mNowContext, "loginflag", false);
//                    ToastUtil.showToast(CommonData.mNowContext,"MainfragmentActivity-loginflag:" + loginflag);
                    if (loginflag)
                        ToastUtils.getInstances().showDialog("账号在其他地方登录，请重新登录。");
                } else if (MESSAGE_RECEIVED_FROM_ABOUT_FRAGMENT.equals(intent.getAction())) {
                    String UpApkUrl = ApiHelper.UpdateApkUrl + "sraum" + Version + ".apk";
                    String apkName = "sraum" + Version + ".apk";
                    Log.e("fei", "UpApkUrl:" + UpApkUrl);
                    UpdateManager manager = new UpdateManager(MainfragmentActivity.this, UpApkUrl, apkName);
                    updateApkListener = (UpdateApkListener) manager;
                    manager.showDownloadDialog();
                } else if (SRAUM_IS_DOWN_LOAD.equals(intent.getAction())) {//apk正在下载
//                    ToastUtil.showToast(MainfragmentActivity.this, "apk正在下载中");
                    iswait_down_load = true;
                }
            } catch (Exception e) {//SRAUM_IS_DOWN_LOAD

            }
        }
    }

    /*
     * 通知
     * */
    private void sendBroad(String content, String bundle) {
        Intent mIntent = new Intent(MESSAGE_TONGZHI);
        mIntent.putExtra("uid", bundle == null ? "" : bundle);//    launchIntent.putExtra(Constants.EXTRA_BUNDLE, args);
        mIntent.putExtra("type", content);
        sendBroadcast(mIntent);
    }

    //拦截侧滑
    public void gotoStop() {
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    //开始侧滑
    public void gotoStart() {
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    private void updateApk() {
//        boolean tokenflag = TokenUtil.getTokenflag(MainfragmentActivity.this);
//        if (tokenflag) {
//            sraum_get_version();
//        }
        sraum_get_version();
    }

    private void initPermission() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void sraum_get_version() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(MainfragmentActivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getVersion, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_get_version();
                    }
                }, MainfragmentActivity.this, viewDialog) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        Version = user.version;
                        Log.e("fei", "Version:" + Version);
                        int sracode = Integer.parseInt(user.versionCode);
                        if (versionCode < sracode) {
                            //在这里判断有没有正在更新的apk,文件大小小于总长度即可
                            weakReference = new WeakReference<Context>(App.getInstance());
                            File apkFile = new File(weakReference.get().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app_name.apk");
                            if (apkFile != null && apkFile.exists()) {
//                                long istext = main_get_real_size(apkFile);
//                                long istext = 0;
//                                try {
//                                    istext = getFileSizes(apkFile);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
////                                long istext = SDCardSizeTest(apkFile);
//                                Log.e("robin debug", "istext:" + istext + "");
                                long apksize = 0;
                                try {
                                    apksize = getFileSize(apkFile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //获取已经下载字节数
//                               String totalSize =  getDataTotalSize(MainfragmentActivity.this,apkFile.getAbsolutePath());
//                              Integer.parseInt(totalSize);

                                int totalapksize = (int) SharedPreferencesUtil.getData(MainfragmentActivity.this, "apk_fileSize", 0);
                                if (totalapksize == 0) {//则说明，还没有下载过
                                    belowtext_id.setText("版本更新至" + Version);
                                    viewDialog.loadViewdialog();
                                    return;
                                }
                                if (apksize - totalapksize == 0) { //说明正在下载或者下载完毕，安装失败时，//->或者是下载完毕后没有去安装；
//                                    down_load_thread();
                                    ToastUtil.showToast(MainfragmentActivity.this, "检测到有新版本，正在下载中");
                                }
                            } else {//不存在，apk文件
                                belowtext_id.setText("版本更新至" + Version);
                                viewDialog.loadViewdialog();
                            }
                        } else {//没有可更新的apk时
                            SharedPreferencesUtil.saveData(MainfragmentActivity.this, "apk_fileSize", 0);
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    /**
     * 下载线程
     */
    private void down_load_thread() {
        final int[] add_wait_notifacation = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!iswait_down_load) {
                    add_wait_notifacation[0] += 1;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (add_wait_notifacation[0] == 15) {//倒计时用完时
                        iswait_down_load = true;//已经唤醒notifacationService
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (add_wait_notifacation[0] == 15) {
                            belowtext_id.setText("版本更新至" + Version);
                            viewDialog.loadViewdialog();
                        }
                    }
                });
            }
        }).start();
    }

    public String getDataTotalSize(Context context, String absolutePath) {
        StatFs sf = new StatFs(absolutePath);
        long blockSize = sf.getBlockSize();
        long totalBlocks = sf.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /*得到传入文件的大小*/
    public long getFileSizes(File f) throws Exception {

        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }
        return s;
    }

    public long main_get_real_size(File f) {
        FileChannel fc = null;
        long size = 0;
        try {
//            File f= new File("D:\\CentOS-6.5-x86_64-bin-DVD1.iso");
            if (f.exists() && f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                // 2.定义存储空间
                byte[] buffer = new byte[1024];
                // 3.开始读文件
                int len = -1;

                if (fis != null) {
                    while ((len = fis.read(buffer)) != -1) {
                        // 将Buffer中的数据写到outputStream对象中
//                            outputStream.write(buffer, 0, len);
                        size += len;
                        Log.e("robin debug", "size:" + size);
                    }
                }
//                fc = fis.getChannel();
//                logger.info(fc.size());
                return size;
            } else {
//                logger.info("file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {

                }
            }
        }
        return 0;
    }

    //用于设置dialog展示
    private void getDialog() {
        View view = getLayoutInflater().inflate(R.layout.check, null);
        checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        qxbutton_id = (Button) view.findViewById(R.id.qxbutton_id);
        dtext_id = (TextView) view.findViewById(R.id.dtext_id);
        belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
        dtext_id.setText("发现新版本");
        checkbutton_id.setText("立即更新");
        qxbutton_id.setText("以后再说");
        viewDialog = new DialogUtil(MainfragmentActivity.this, view);
        checkbutton_id.setOnClickListener(this);
        qxbutton_id.setOnClickListener(this);
    }

    /**
     * 切换Fragment
     *
     * @param
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setTabSelection(int index, String type, String uid) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 如果MessageFragment为空，则创建一个并添加到界面上
//                addrelative_id.setVisibility(View.VISIBLE);
//                addimage_id.setVisibility(View.VISIBLE);
//                cenimage_id.setVisibility(View.VISIBLE);
//                centext_id.setVisibility(View.GONE);
                if (mainviewpager == null) {
                    mainviewpager = Mainviewpager.newInstance(type);
                    transaction.add(R.id.content_frame, mainviewpager);
                    LogUtil.i("数据", "setTabSelection: ");
                } else {
                    tongzhi_door(type, uid);
                    LogUtil.i("展示数据", "setTabSelection: ");
                    transaction.show(mainviewpager);
                }
                break;
            case 1:
                // 如果MessageFragment为空，则创建一个并添加到界面上
//                addrelative_id.setVisibility(View.VISIBLE);
//                addimage_id.setVisibility(View.VISIBLE);
//                cenimage_id.setVisibility(View.VISIBLE);
//                centext_id.setVisibility(View.GONE);
                if (messagefragment == null) {
//                    messagefragment = new MessageFragment.newInstance(menu);
                    messagefragment = MessageFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, messagefragment);
                    LogUtil.i("数据", "setTabSelection: ");
                } else {
                    LogUtil.i("展示数据", "setTabSelection: ");
                    transaction.show(messagefragment);
                }
                break;
            case 2:
                // 如果MessageFragment为空，则创建一个并添加到界面上
//                addrelative_id.setVisibility(View.VISIBLE);
//                addimage_id.setVisibility(View.VISIBLE);
//                cenimage_id.setVisibility(View.VISIBLE);
//                centext_id.setVisibility(View.GONE);
                if (mygatewayFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mygatewayFragment = MygatewayFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, mygatewayFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mygatewayFragment);
                }
                break;
            case 3:
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setVisibility(View.GONE);

                if (mydevicefragment == null) {
//                    messagefragment = new MessageFragment.newInstance(menu);
                    mydevicefragment = MyDeviceFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, mydevicefragment);
                    LogUtil.i("数据", "setTabSelection: ");
                } else {
                    LogUtil.i("展示数据", "setTabSelection: ");
                    transaction.show(mydevicefragment);
                }
                break;
//            case 4:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.mac);
//                if (macdeviceFragment == null) {
//                    // 如果MessageFragment为空，则创建一个并添加到界面上
//                    macdeviceFragment = MacdeviceFragment.newInstance(menu);
//                    transaction.add(R.id.content_frame, macdeviceFragment);
//                } else {
//                    // 如果MessageFragment不为空，则直接将它显示出来
//                    transaction.show(macdeviceFragment);
//                }
//
//                macdeviceFragment.setBackToMainTitleListener(new MacdeviceFragment.BackToMainTitleListener() {
//                    @Override
//                    public void backToMainTitleLength(int length) {
////                        centext_id.setText("智能设备" + "(" + length + ")");
//                    }
//                });
//                break;
            case 4:
                if (myRoomFragment == null) {
//                    centext_id.setVisibility(View.VISIBLE);
//                    addrelative_id.setVisibility(View.GONE);
//                    addimage_id.setVisibility(View.GONE);
//                    cenimage_id.setVisibility(View.GONE);
//                    centext_id.setText(R.string.myroom);
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    myRoomFragment = MyRoomFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, myRoomFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(myRoomFragment);
                }
                break;
            case 5:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.scenecen);
                if (mysceneFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mysceneFragment = MysceneFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, mysceneFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mysceneFragment);
                }
                break;
            case 6:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.set);
                if (mysetFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mysetFragment = MysetFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, mysetFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mysetFragment);
                }
                break;
            case 7:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.aboutmain);
                if (aboutFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    aboutFragment = AboutFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, aboutFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(aboutFragment);
                }
                break;
//            case 9:
////                centext_id.setVisibility(View.VISIBLE);
////                addrelative_id.setVisibility(View.GONE);
////                addimage_id.setVisibility(View.GONE);
////                cenimage_id.setVisibility(View.GONE);
////                centext_id.setText("我的面板");
//                if (panelFragment == null) {
//                    // 如果MessageFragment为空，则创建一个并添加到界面上
//                    panelFragment = PanelFragment.newInstance(menu);
//                    transaction.add(R.id.content_frame, panelFragment);
//                } else {
//                    // 如果MessageFragment不为空，则直接将它显示出来
//                    transaction.show(panelFragment);
//                }
//
//                panelFragment.setBackToMainTitleListener(new PanelFragment.BackToMainTitleListener() {
//                    @Override
//                    public void backToMainTitleLength(int length) {
////                        centext_id.setText("我的面板"+"("+length+")");
//                    }
//                });
//                break;
        }
        transaction.commitAllowingStateLoss();
        menu.showContent();
    }

    private void tongzhi_door(String type, String uid) {
        Intent mIntent = new Intent(MESSAGE_TONGZHI_DOOR);
        mIntent.putExtra("type", type);
        mIntent.putExtra("uid", uid);
        sendBroadcast(mIntent);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mainviewpager != null) {
            transaction.hide(mainviewpager);
        }
        if (messagefragment != null) {
            transaction.hide(messagefragment);
        }

        if (mydevicefragment != null) {
            transaction.hide(mydevicefragment);
        }
        if (mygatewayFragment != null) {
            transaction.hide(mygatewayFragment);
        }
//        if (macdeviceFragment != null) {
//            transaction.hide(macdeviceFragment);
//        }
        if (mysceneFragment != null) {
            transaction.hide(mysceneFragment);
        }
        if (myRoomFragment != null) {
            transaction.hide(myRoomFragment);
        }
        if (mysetFragment != null) {
            transaction.hide(mysetFragment);
        }
        if (aboutFragment != null) {
            transaction.hide(aboutFragment);
        }
        if (leftFragment != null) {
            transaction.hide(leftFragment);
        }

//        if (panelFragment != null) {
//            transaction.hide(panelFragment);
//        }
    }

//    //加载主界面右上角popwindow
//    private void addPopwinwow() {
//        View v = LayoutInflater.from(MainfragmentActivity.this).inflate(R.layout.maindialog, null);
//        addmac_id = (RelativeLayout) v.findViewById(R.id.addmac_id);
//        addscene_id = (RelativeLayout) v.findViewById(addscene_id);
//        addroom_id = (RelativeLayout) v.findViewById(R.id.addroom_id);
//        popupWindow = new PopupWindow(v,
//                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable());
////        addrelative_id.setOnClickListener(this);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                darkenBackgroud(1f);
//            }
//        });
//        addmac_id.setOnClickListener(this);
//        addroom_id.setOnClickListener(this);
//        addscene_id.setOnClickListener(this);
//    }

    //设置popwindow灰暗程度
    private void darkenBackgroud(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.sideslip_id:
//                menu.toggle();
//                break;
//            case addrelative_id:
//                darkenBackgroud(0.7f);
//                popupWindow.showAtLocation(v, Gravity.TOP | Gravity.RIGHT, 3, addrelative_id.getHeight() + 50);
//                break;
            case R.id.addmac_id:
                Intent intent = new Intent(MainfragmentActivity.this, MacdeviceActivity.class);
                intent.putExtra("name", "2");
                startActivity(intent);
                break;
            case R.id.addroom_id:
                Intent intentr = new Intent(MainfragmentActivity.this, MacdeviceActivity.class);
                intentr.putExtra("name", "1");
                startActivity(intentr);
                popupWindow.dismiss();
                break;
            case addscene_id:
                Intent intent1 = new Intent(MainfragmentActivity.this, MysceneActivity.class);
                startActivity(intent1);
                break;
            case R.id.checkbutton_id:
                viewDialog.removeviewDialog();
                String UpApkUrl = ApiHelper.UpdateApkUrl + "sraum" + Version + ".apk";
                Log.e("fei", "UpApkUrl:" + UpApkUrl);
                String apkName = "sraum" + Version + ".apk";
                UpdateManager manager = new UpdateManager(MainfragmentActivity.this, UpApkUrl, apkName);
                updateApkListener = (UpdateApkListener) manager;
                manager.showDownloadDialog();

                break;
            case R.id.qxbutton_id:
                viewDialog.removeviewDialog();
                break;
        }
    }

    private UpdateApkListener updateApkListener;

    public interface UpdateApkListener {
        void sendTo_UPApk();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showDelToast(MainfragmentActivity.this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();

            } else {
                //-----
//                boolean loadapk = (boolean) SharedPreferencesUtil.getData(MainfragmentActivity.this, "loadapk", false);
//                if (loadapk) {
//                    if (!dialog1.isShowing()) {
//                        dialog1.show();
//                    }
//                } else {
//                    dialog1.dismiss();
//                    over_camera_list();//结束wifi摄像头的tag
//                    MainfragmentActivity.this.finish();
////                    System.exit(0);
//                }

//                dialog1.dismiss();
//                if (!dialog1.isShowing()) {
//                    dialog1.show();
//                }

                iswait_down_load = true;
                over_camera_list();//结束wifi摄像头的tag
                MainfragmentActivity.this.finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //自定义dialog,centerDialog删除对话框
    public void showCenterDeleteDialog(final String name1, final String name2) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // 布局填充器
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view = inflater.inflate(R.layout.user_name_dialog, null);
//        // 设置自定义的对话框界面
//        builder.setView(view);
//
//        cus_dialog = builder.create();
//        cus_dialog.show();

        View view = LayoutInflater.from(MainfragmentActivity.this).inflate(R.layout.promat_dialog, null);
        TextView confirm; //确定按钮
        TextView cancel; //确定按钮
        TextView tv_title;
        TextView name_gloud;
//        final TextView content; //内容
        cancel = (TextView) view.findViewById(R.id.call_cancel);
        confirm = (TextView) view.findViewById(R.id.call_confirm);
        tv_title = (TextView) view.findViewById(R.id.tv_title);//name_gloud
        name_gloud = (TextView) view.findViewById(R.id.name_gloud);
        name_gloud.setText(name1);
        tv_title.setText(name2);
//        tv_title.setText("是否拨打119");
//        content.setText(message);
        //显示数据
        dialog1 = new Dialog(MainfragmentActivity.this, R.style.BottomDialog);
        dialog1.setContentView(view);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.setCancelable(false);//设置它可以取消
        dialog1.setCanceledOnTouchOutside(false);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog1.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.8); //宽度设置为屏幕的0.5
//        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
//        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog1.getWindow().setAttributes(p);  //设置生效
//        dialog1.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                dialog1.dismiss();
                over_camera_list();//结束wifi摄像头的tag
                MainfragmentActivity.this.finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferencesUtil.saveData(MainfragmentActivity.this, "loadapk", false);
//                over_broad_apk_load();
                startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), TONGZHI_APK_UPGRATE);
            }
        });
    }

    private void over_broad_apk_load() {
//        Intent broadcast = new Intent(MESSAGE_RECEIVED_ACTION_APK_LOAD);
//        sendBroadcast(broadcast);
        if (updateApkListener != null) {
            updateApkListener.sendTo_UPApk();
        }
        MainfragmentActivity.this.finish();
        over_camera_list();//结束wifi摄像头的tag
    }

    /**
     * 清除wifi摄像头列表
     */
    private void over_camera_list() {
        List<Map> list = SharedPreferencesUtil.getInfo_List(MainfragmentActivity.this, "list_wifi_camera_first");
        List<Map> list_second = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map map = new HashMap();
            map.put("did", list.get(i).get("did"));
            map.put("tag", 0);
            list_second.add(map);
        }
        SharedPreferencesUtil.saveInfo_List(MainfragmentActivity.this, "list_wifi_camera_first", new ArrayList<Map>());
        SharedPreferencesUtil.saveInfo_List(MainfragmentActivity.this, "list_wifi_camera_first", list_second);
    }

    @Override
    public SlidingMenu getMenu() {
        return menu;
    }

    @Override
    protected void onResume() {
        //小苹果机智云初始化
//        init_jizhiyun = 1;

        if (init_jizhiyun == 1) {
            init_jizhiyun = 2;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
//                    init_visible();
                    init_jizhiyun();
                    Looper.loop();
                }
            }).start();
            Log.e("robin debug", "    Looper.prepare()");
        }

        isForegrounds = true;
        getNotify(getIntent());//防止重启亮屏后，重复送出极光数据
        Log.e("zhu-", "MainfragmentActivity:onResume():isForegrounds:" + isForegrounds);
//      String  extras_login = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this,"extras_login","");
//        if (extras_login) {
//
//        }
//        后台唤醒的时候先掉下sc_islogin
//        返回100就继续能用返回别的就直接跳到登录
//        init_jlogin();
        init_jlogin();
        boolean netflag = NetUtils.isNetworkConnected(MainfragmentActivity.this);
        if (netflag) {//获取版本号
//            if (!isEnabled()) {
//                if (!dialog1.isShowing()) {
//                    dialog1.show();
//                }
//            } else {
//                updateApk();
//            }

            updateApk();
        }
        super.onResume();
    }

    private void init_jlogin() {
        String mobilePhone = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this, "loginPhone", "");
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        String szImei = TelephonyMgr.getDeviceId();
        String szImei = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this, "regId", "");
        init_islogin(mobilePhone, szImei);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case TONGZHI_APK_UPGRATE:
//                if (isEnabled()) {//监听通知权限开启
//                    if (dialog1 != null)
//                        dialog1.dismiss();
//                    updateApk();
//                } else {
//                    if (!dialog1.isShowing()) {
//                        dialog1.show();
//                    }
//                }
//                break;
//        }
    }

    /**
     * 初始化登录
     *
     * @param
     * @param mobilePhone
     * @param szImei
     */
    private void init_islogin(final String mobilePhone, final String szImei) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobilePhone", mobilePhone);
        map.put("phoneId", szImei);
        LogUtil.eLength("查看数据", JSON.toJSONString(map));
        MyOkHttp.postMapObject(ApiHelper.sraum_isLogin, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                init_islogin(mobilePhone, szImei);
            }
        }, MainfragmentActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(MainfragmentActivity.this, "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }

            @Override
            public void threeCode() {
                //103已经登录，需要退出app
//                dialog.show();
                ToastUtils.getInstances().showDialog("账号在其他地方登录，请重新登录。");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ToastUtil.showToast(MainfragmentActivity.this,"MainfragmentActivity:destroy");
        over_camera_list();//结束wifi摄像头的tag
        common_second();
//        over_broad_apk_load();
        if (dialog1 != null) {
            dialog1.dismiss();
        }
        unregisterReceiver(mMessageReceiver_aboutfragment);
//        unregisterReceiver(mMessageReceiver_apk_down_load);
//        iswait_down_load = false;

//        /**
//         * 开启download监听service
//         */
//        Intent intent_apk_down = new Intent();
//        intent_apk_down.setClass(MainfragmentActivity.this, NotificationMonitorService.class);
//        stopService(intent_apk_down);
    }


    /**
     * 清除联动的本地存储
     */
    private void common_second() {
        SharedPreferencesUtil.saveData(MainfragmentActivity.this, "linkId", "");
        SharedPreferencesUtil.saveInfo_List(MainfragmentActivity.this, "list_result", new ArrayList<Map>());
        SharedPreferencesUtil.saveInfo_List(MainfragmentActivity.this, "list_condition", new ArrayList<Map>());
        SharedPreferencesUtil.saveData(MainfragmentActivity.this, "editlink", false);
        SharedPreferencesUtil.saveInfo_List(MainfragmentActivity.this, "link_information_list", new ArrayList<Map>());
        SharedPreferencesUtil.saveData(MainfragmentActivity.this, "add_condition", false);
    }

}
