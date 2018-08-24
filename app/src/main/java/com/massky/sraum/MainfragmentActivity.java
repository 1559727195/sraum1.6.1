package com.massky.sraum;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
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
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jpush.Constants;
import com.jpush.ExampleUtil;
import com.larksmart7618.sdk.communication.tools.commen.ToastTools;
import com.permissions.RxPermissions;
import com.service.SimpleIntentService;
import com.yaokan.sdk.api.YkanSDKManager;
import com.yaokan.sdk.ir.InitYkanListener;
import com.yaokan.sdk.utils.Logger;
import com.yaokan.sdk.utils.ProgressDialogUtils;
import com.yaokan.sdk.utils.Utility;
import com.yaokan.sdk.wifi.DeviceManager;
import com.yaokan.sdk.wifi.GizWifiCallBack;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

import static com.fragment.LeftFragment.MESSAGE_TONGZHI;
import static com.massky.sraum.R.id.addscene_id;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于主界面设置*/
public class MainfragmentActivity extends Basecfragmentactivity implements Mainviewpager.MyInterface, InitYkanListener {
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
            init_jizhiyun();
        }
    };

    @Override
    protected int viewId() {
        return R.layout.main;
    }

    @Override
    protected void onView() {
        dialogUtil = new DialogUtil(this);
        initPermission();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler_wifi.sendEmptyMessage(0);
            }
        }).start();

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
                setTabSelection(4);
            } else {
                setTabSelection(0);
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commitAllowingStateLoss();
//        sideslip_id.setOnClickListener(this);
        getDialog();
        boolean netflag = NetUtils.isNetworkConnected(MainfragmentActivity.this);
        if (netflag) {
            updateApk();
        }
        registerMessageReceiver();
        init_notifacation();//通知初始化
    }


    /**
     * 初始化机智云小苹果
     */
    private void init_jizhiyun() {
        initListener();
        // 初始化SDK

        dialogUtil.loadDialog();

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
                init_nofication(intent);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
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
                    sendBroad(type);
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

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }


    public static class MessageReceiver extends BroadcastReceiver {

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
                }
            } catch (Exception e) {

            }
        }
    }

    /*
    * 通知
    * */
    private void sendBroad(String content) {
        Intent mIntent = new Intent(MESSAGE_TONGZHI);
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
                            belowtext_id.setText("版本更新至" + Version);
                            viewDialog.loadViewdialog();
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
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

    public void setTabSelection(int index) {
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
                    mainviewpager = new Mainviewpager();
                    transaction.add(R.id.content_frame, mainviewpager);
                    LogUtil.i("数据", "setTabSelection: ");
                } else {
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
                UpdateManager manager = new UpdateManager(MainfragmentActivity.this, UpApkUrl);
                manager.showDownloadDialog();
                break;
            case R.id.qxbutton_id:
                viewDialog.removeviewDialog();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showDelToast(MainfragmentActivity.this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                MainfragmentActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public SlidingMenu getMenu() {
        return menu;
    }

    @Override
    protected void onResume() {
        isForegrounds = true;
        getNotify(getIntent());
        Log.e("zhu-", "MainfragmentActivity:onResume():isForegrounds:" + isForegrounds);
//      String  extras_login = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this,"extras_login","");
//        if (extras_login) {
//
//        }
//        后台唤醒的时候先掉下sc_islogin
//        返回100就继续能用返回别的就直接跳到登录
//        init_jlogin();
        init_jlogin();
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
        String szImei = TelephonyMgr.getDeviceId();
        init_islogin(mobilePhone, szImei);
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
        common_second();
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
