package com.massky.sraum;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.DialogUtil;
import com.Util.LogUtil;
import com.adapter.FragmentViewPagerAdapter;
import com.base.Basecactivity;
import com.base.Basecfragment;
import com.base.Basecfragmentactivity;
import com.fragment.EachMatchFragment;
import com.fragment.IntelligentMatchFragment;
import com.fragment.OneKeyMatchFragment;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.lljjcoder.style.citylist.sortlistview.SideBar;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import com.yaokan.sdk.api.YkanSDKManager;
import com.yaokan.sdk.ir.YKanHttpListener;
import com.yaokan.sdk.ir.YkanIRInterface;
import com.yaokan.sdk.ir.YkanIRInterfaceImpl;
import com.yaokan.sdk.model.BaseResult;
import com.yaokan.sdk.model.Brand;
import com.yaokan.sdk.model.BrandResult;
import com.yaokan.sdk.model.DeviceDataStatus;
import com.yaokan.sdk.model.DeviceType;
import com.yaokan.sdk.model.DeviceTypeResult;
import com.yaokan.sdk.model.MatchRemoteControl;
import com.yaokan.sdk.model.MatchRemoteControlResult;
import com.yaokan.sdk.model.RemoteControl;
import com.yaokan.sdk.model.YKError;
import com.yaokan.sdk.wifi.DeviceController;
import com.yaokan.sdk.wifi.DeviceManager;
import com.yaokan.sdk.wifi.listener.LearnCodeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

public class RemoteControlMatchingActivity extends Basecfragmentactivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    private FragmentManager fm;
    private EachMatchFragment eachmatchfragment;
    private OneKeyMatchFragment onekeymatchfragment;
//    private IntelligentMatchFragment intelligenmatchfragment;
    private List<Fragment> list = new ArrayList<Fragment>();
    //    private RoomFragment roomFragment;

    private int golden = Color.parseColor("#e0c48e");
    private int black = Color.parseColor("#6f6f6f");
    private FragmentViewPagerAdapter adapter;

    @InjectView(R.id.viewpager_id)
    ViewPager viewpager_id;
    @InjectView(R.id.macrelative_id)
    RelativeLayout macrelative_id;
    @InjectView(R.id.scenerelative_id)
    RelativeLayout scenerelative_id;
    @InjectView(R.id.smartrelative_id)
    RelativeLayout smartrelative_id;
    @InjectView(R.id.viewthree)
    View viewthree;
    @InjectView(R.id.smart_id)
    TextView smart_id;
    @InjectView(R.id.viewone)
    View viewone;
    @InjectView(R.id.viewtwo)
    View viewtwo;
    @InjectView(R.id.mactext_id)
    TextView mactext_id;
    @InjectView(R.id.scene_id)
    TextView scene_id;
    private boolean flag;
    public static String ACTION_INTENT_RECEIVER_WIFI = "com.massky.sraum.RemoteControlMatchingActivity";


    @Override
    protected int viewId() {
        return R.layout.remoate_control_match_act;
    }


    private void initView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        onEvent();
    }

    private void onEvent() {
        back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onView() {
        initView();
//        initData();
////        setCityData(CityListLoader.getInstance().getCityListData());
//        get_device_and_brand();
        fm = getSupportFragmentManager();


        addFragment();
        onclick();
        startState();
    }

    //添加fragment
    private void addFragment() {
        eachmatchfragment = EachMatchFragment.newInstance();
//        roomFragment = new RoomFragment();
        onekeymatchfragment = OneKeyMatchFragment.newInstance(new OneKeyMatchFragment.OnDeviceMessageFragListener() {

            @Override
            public void ondevice_message_frag() {

            }
        });

//        intelligenmatchfragment = IntelligentMatchFragment.newInstance(new IntelligentMatchFragment.OnDeviceMessageFragListener() {
//
//            @Override
//            public void ondevice_message_frag() {
//
//            }
//        });
        list.add(eachmatchfragment);
        list.add(onekeymatchfragment);
//        list.add(intelligenmatchfragment);
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

    private void onclick() {
        MyOnclick on = new MyOnclick();
        macrelative_id.setOnClickListener(on);
//        roomrelative_id.setOnClickListener(on);
        scenerelative_id.setOnClickListener(on);
        smartrelative_id.setOnClickListener(on);
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
        smart_id.setTextColor(black);
        viewone.setVisibility(View.GONE);
        viewtwo.setVisibility(View.GONE);
        viewthree.setVisibility(View.GONE);
//        viewthree.setVisibility(View.GONE);
    }

    // 进行匹配设置一个方法，判断是否被点击
    private void viewchange(int num) {
        sendBroad(num);
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
            case R.id.smartrelative_id:
            case 2:
                smart_id.setTextColor(golden);
                viewthree.setVisibility(View.VISIBLE);
                viewpager_id.setCurrentItem(2);
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
    public void onClick(View v) {
        //new DownloadThread(v.getId()).start();
        switch (v.getId()) {
            case R.id.back:
                RemoteControlMatchingActivity.this.finish();
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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

    private void sendBroad(int index) {
        Intent mIntent = new Intent(ACTION_INTENT_RECEIVER_WIFI);
        mIntent.putExtra("index", index);
        sendBroadcast(mIntent);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        clear();
        viewchange(viewpager_id.getCurrentItem());

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
