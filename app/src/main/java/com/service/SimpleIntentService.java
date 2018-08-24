package com.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.jpush.Constants;
import com.massky.sraum.MainfragmentActivity;
import com.yaokan.sdk.api.YkanSDKManager;
import com.yaokan.sdk.ir.InitYkanListener;
import com.yaokan.sdk.wifi.DeviceManager;
import com.yaokan.sdk.wifi.GizWifiCallBack;

import static com.fragment.LeftFragment.MESSAGE_TONGZHI;
import static com.massky.sraum.ConnWifiActivity.MESSAGE_RECEIVED_ACTION_ConnWifi;

/**
 * Created by zhu on 2018/7/27.
 */

public class SimpleIntentService extends IntentService implements InitYkanListener {
    private int  index = 0;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SimpleIntentService(String name) {
        super(SimpleIntentService.class.getName());
        setIntentRedelivery(true);
    }

    public SimpleIntentService() {
        super(SimpleIntentService.class.getName());
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //这个方法是在后台线程中调用的
        init_jizhiyun();
    }

    @Override
    public void onInitStart() {

    }

    @Override
    public void onInitFinish(int status, String errorMsg) {
//        ToastUtil.showToast(SimpleIntentService.this,"初始化成功");
        DeviceManager.instanceDeviceManager(getApplicationContext()).userLoginAnonymous();//匿名登录
    }


    /**
     * 初始化机智云小苹果
     */
    private void init_jizhiyun() {
                initListener();
                // 初始化SDK
                YkanSDKManager.init(SimpleIntentService.this, SimpleIntentService.this);
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

                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {// 登陆成功
                    Constants.UID = uid;
                    Constants.TOKEN = token;
//                    toDeviceList();
                    //登录成功
//                    ToastUtil.showToast(SimpleIntentService.this,"小苹果登录成功!");
//                    SharedPreferencesUtil.saveData(SimpleIntentService.this,"apple_login",true);
//                    sendBroad("apple_login_scuess");

                } else if (result == GizWifiErrorCode.GIZ_OPENAPI_USER_NOT_EXIST) {// 用户不存在

                } else if (result == GizWifiErrorCode.GIZ_OPENAPI_USERNAME_PASSWORD_ERROR) {//// 用户名或者密码错误

                } else {
//                    toast("登陆失败，请重新登录");
                    index++;
                    if(index >= 2) {

                    } else {
                        DeviceManager.instanceDeviceManager(getApplicationContext()).userLoginAnonymous();//匿名登录
                    }
                    SharedPreferencesUtil.saveData(SimpleIntentService.this,"apple_login",false);
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

    /*
* 通知
* */
    private void sendBroad(String content) {
        Intent mIntent = new Intent(MESSAGE_RECEIVED_ACTION_ConnWifi);
        mIntent.putExtra("issucess", content);
        sendBroadcast(mIntent);
    }

}
