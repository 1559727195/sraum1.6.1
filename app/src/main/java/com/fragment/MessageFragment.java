package com.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.FragmentViewPagerAdapter;
import com.adapter.MygatewayAdapter;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.MysceneActivity;
import com.massky.sraum.R;
import com.massky.sraum.SelectZigbeeDeviceActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by masskywcy on 2016-09-09.
 */
//用于主界面首页设置
public class MessageFragment extends Basecfragment implements ViewPager.OnPageChangeListener {
    private static SlidingMenu mySlidingMenu;
    @InjectView(R.id.viewpager_id)
    ViewPager viewpager_id;
    @InjectView(R.id.macrelative_id)
    RelativeLayout macrelative_id;
    @InjectView(R.id.scenerelative_id)
    RelativeLayout scenerelative_id;
    @InjectView(R.id.viewone)
    View viewone;
    @InjectView(R.id.viewtwo)
    View viewtwo;
    //    @InjectView(R.id.viewthree)
//    View viewthree;
    @InjectView(R.id.mactext_id)
    TextView mactext_id;
    @InjectView(R.id.scene_id)
    TextView scene_id;
    @InjectView(R.id.sideslip_id)
    RelativeLayout sideslip_id;
    @InjectView(R.id.addrelative_id)
    RelativeLayout addrelative_id;
    @InjectView(R.id.addimage_id)
    ImageView addimage_id;
    @InjectView(R.id.cenimage_id)
    ImageView cenimage_id;
    @InjectView(R.id.centext_id)
    TextView centext_id;
    @InjectView(R.id.addtxt_text_id)
    TextView addtxt_text_id;
//    @InjectView(R.id.bottomimage_id)
//    ImageView bottomimage_id;

//    private View account_view;
//    private DialogUtil dialogUtil;
//    private LinearLayout all_read_linear;
//    private LinearLayout all_select_linear;
//    private LinearLayout delete_linear;

    private FragmentManager fm;
    private List<Fragment> list = new ArrayList<Fragment>();
    //    private RoomFragment roomFragment;
    private SceneFragment sceneFragment;
    private int golden = Color.parseColor("#e0c48e");
    private int black = Color.parseColor("#6f6f6f");
    private FragmentViewPagerAdapter adapter;
    private boolean flag = false;

    private DeviceMessageFragment devicemessagefragment;
    private SystemMessageFragment systemmessagefragment;

    public static String MESSAGE_FRAGMENT = "com.fragment.messagefragment";
    private ImageView common_setting_image;

    @Override
    protected int viewId() {
        return R.layout.message_frag_lay;
    }

    public static MessageFragment newInstance(SlidingMenu mySlidingMenu1) {
        MessageFragment newFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected void onView() {
        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("消息");
        addtxt_text_id.setVisibility(View.VISIBLE);
        addtxt_text_id.setText("编辑");
        LogUtil.i("这是oncreate方法", "onView: ");
        fm = getChildFragmentManager();
        addFragment();
        onclick();
        startState();
        addtxt_text_id.setOnClickListener(this);
//        addViewid();
    }

    private void sendBroad(String content) {
        Intent mIntent = new Intent(MESSAGE_FRAGMENT);
        mIntent.putExtra("action", content);
        getActivity().sendBroadcast(mIntent);
    }


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        myInterface = (MyInterface) activity;
//    }

    //添加fragment
    private void addFragment() {
        devicemessagefragment = DeviceMessageFragment.newInstance(new DeviceMessageFragment.OnDeviceMessageFragListener(){

            @Override
            public void ondevice_message_frag() {
                addtxt_text_id.setText("编辑");
                sendBroad("取消");
            }
        });
//        roomFragment = new RoomFragment();
        systemmessagefragment = new SystemMessageFragment();
        list.add(devicemessagefragment);
        list.add(systemmessagefragment);
//        list.add(roomFragment);
        adapter = new FragmentViewPagerAdapter(fm, viewpager_id, list);
        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageSelected(int i) {
                LogUtil.i("Extra...i:", i + "onExtraPageSelected: ");
            }
        });
        viewpager_id.addOnPageChangeListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            list.clear();
            devicemessagefragment = new DeviceMessageFragment();
//        roomFragment = new RoomFragment();
            systemmessagefragment = new SystemMessageFragment();
            list.add(devicemessagefragment);
            list.add(systemmessagefragment);
//            list.add(roomFragment);
            adapter = new FragmentViewPagerAdapter(fm, viewpager_id, list);
            startState();
            addtxt_text_id.setText("编辑");
        }
    }

    private void onclick() {
        MyOnclick on = new MyOnclick();
        macrelative_id.setOnClickListener(on);
//        roomrelative_id.setOnClickListener(on);
        scenerelative_id.setOnClickListener(on);
    }

    /**
     * 设置一个监听类
     */
    class MyOnclick implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            clear();
            viewchange(arg0.getId());
        }
    }

    private void clear() {
        mactext_id.setTextColor(black);
//        roomtext_id.setTextColor(black);
        scene_id.setTextColor(black);
        viewone.setVisibility(View.GONE);
        viewtwo.setVisibility(View.GONE);
//        viewthree.setVisibility(View.GONE);
    }

    // 进行匹配设置一个方法，判断是否被点击
    private void viewchange(int num) {
        switch (num) {
            case R.id.macrelative_id:
            case 0:
                mactext_id.setTextColor(golden);
                viewone.setVisibility(View.VISIBLE);
                viewpager_id.setCurrentItem(0);
                break;
            case R.id.scenerelative_id:
            case 1:
                scene_id.setTextColor(golden);
                viewtwo.setVisibility(View.VISIBLE);
                viewpager_id.setCurrentItem(1);
                break;

            default:
                break;
        }
    }

    private void startState() {
        clear();
        viewchange(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addtxt_text_id:
                switch (addtxt_text_id.getText().toString()) {
                    case "编辑":
                        addtxt_text_id.setText("取消");
                        sendBroad("编辑");
//                        dialogUtil.loadViewBottomdialog();
                        break;
                    case "取消":
                        addtxt_text_id.setText("编辑");
                        sendBroad("取消");
//                        dialogUtil.removeviewBottomDialog();
                        break;
                }
                break;

            case R.id.all_read_linear:

                break;

            case R.id.all_select_linear:

                break;

            case R.id.delete_linear:

                break;
        }
    }

//    /**
//     * 底部弹出拍照，相册弹出框
//     */
//    private void addViewid() {
//        account_view = LayoutInflater.from(getActivity()).inflate(R.layout.message_function_select, null);
//        all_read_linear = (LinearLayout) account_view.findViewById(R.id.all_read_linear);
//        all_select_linear = (LinearLayout) account_view.findViewById(R.id.all_select_linear);
//        delete_linear = (LinearLayout) account_view.findViewById(R.id.delete_linear);
//
//        all_read_linear.setOnClickListener(this);
//        all_select_linear.setOnClickListener(this);
//        delete_linear.setOnClickListener(this);
//
////        //common_setting_image
////        common_setting_image = (ImageView) account_view.findViewById(R.id.common_setting_image);
//        dialogUtil = new DialogUtil(getActivity(), account_view, 2);
//    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mySlidingMenu != null)
            if (position == 0) {
                mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                Log.e("robin debug", "ok");
            } else {
                mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                Log.e("robin debug", "no");
            }
    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.eLength("这是滚动", position + "");
        if (flag) {
            Basecfragment fagmentbase = (Basecfragment) list.get(position);
            fagmentbase.initData();
        }
        flag = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        clear();
        viewchange(viewpager_id.getCurrentItem());
    }


    @OnClick(R.id.sideslip_id)
    void change_slide_menu() {
        if (mySlidingMenu != null)
            mySlidingMenu.toggle();
    }


    @Override
    public void onStart() {//onStart方法是屏幕唤醒时弹执行该方法。
        super.onStart();
        addtxt_text_id.setText("编辑");
        sendBroad("取消");
    }

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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
}
