package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.TokenUtil;
import com.adapter.SelectLinkageAdapter;
import com.adapter.SelectSensorAdapter;
import com.base.Basecactivity;
import com.data.User;
import com.xlistview.PullToRefreshLayout;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by zhu on 2018/6/13.
 */

public class SelectiveLinkageActivity extends Basecactivity implements
        AdapterView.OnItemClickListener,
        PullToRefreshLayout.OnRefreshListener {

    private static final int REQUEST_SENSOR = 101;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    @InjectView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @InjectView(R.id.maclistview_id)
    ListView maclistview_id;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.rel_scene_set)
    RelativeLayout rel_scene_set;
    private SelectLinkageAdapter selectexcutesceneresultadapter;
    private List<Map> list_hand_scene = new ArrayList<>();
    private Handler mHandler = new Handler();
    //    String[] again_elements = {"1", "3"
//            };
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private List<Boolean> list_bool = new ArrayList<>();
    private int position;
    private DialogUtil dialogUtil;
    //    private List<User.device> list = new ArrayList<>();
    private List<String> listtype = new ArrayList();
    private List<Map> panelList = new ArrayList<>();
    private Map map_link = new HashMap();
    private List<String> listpanelNumber = new ArrayList();
    private List<String> listpanelName = new ArrayList();

    @Override
    protected int viewId() {
        return R.layout.selective_linkage_lay;
    }

    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        dialogUtil = new DialogUtil(this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        rel_scene_set.setOnClickListener(this);
        onData();
        next_step_txt.setOnClickListener(this);
        maclistview_id.setOnItemClickListener(this);
        refresh_view.setOnRefreshListener(this);
//        refresh_view.autoRefresh();
//        dialogUtil = new DialogUtil(this);
        map_link = (Map) getIntent().getSerializableExtra("link_map");
        if (map_link == null) return;
    }


    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
//    R.drawable.magnetic_door_s,
//    R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
//    R.drawable.emergency_button_s
    private void onData() {
        list_hand_scene = new ArrayList<>();
//        for (String type : ) {
//            Boolean flag = false;
//            Map map = new HashMap();
//            map.put("name", type);
//            setPicture(type);
////            switch (type) {
////                case "7":
////                    map.put("image",R.drawable.magnetic_door_s);
////                    break;
////                case "8":
////                    map.put("image",R.drawable.human_induction_s);
////                    break;
////                case "9":
////                    map.put("image",R.drawable.water_s);
////                    break;
////                case "10":
////                    map.put("image",R.drawable.pm25_s);
////                    break;
////                case "11":
////                    map.put("image",R.drawable.emergency_button_s);
////                    break;
////                case "12":
////                    map.put("image",R.drawable.icon_rucebjq_40);
////                    break;
////                case "13":
////                    map.put("image",R.drawable.icon_yanwubjq_40);
////                    break;
////                case "14":
////                    map.put("image",R.drawable.icon_ranqibjq_40);
////                    break;
////                case "15":
////                    map.put("image",R.drawable.icon_zhinengmensuo_40);
////                    break;
////                case "16":
////                    map.put("image",R.drawable.ic_launcher);
////                    break;
////            }
//            list_hand_scene.add(map);
//            list_bool.add(flag);
//        }

        selectexcutesceneresultadapter = new SelectLinkageAdapter(SelectiveLinkageActivity.this,
                panelList, listint, listintwo);
        maclistview_id.setAdapter(selectexcutesceneresultadapter);
//        xListView_scan.setPullLoadEnable(false);
//        xListView_scan.setFootViewHide();
//        xListView_scan.setXListViewListener(this);

//        uploader_refresh();
        getData(true);
    }

    private void getData(boolean flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(SelectiveLinkageActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(SelectiveLinkageActivity.this));
        if (flag) {
            dialogUtil.loadDialog();
        }
        MyOkHttp.postMapObject(ApiHelper.sraum_getLinkController, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getData(false);
                    }
                }, SelectiveLinkageActivity.this, dialogUtil) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
//                        refresh_view.stopRefresh(false);
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
//                        refresh_view.stopRefresh();

//                        adapter = new PanelAdapter(SelectiveLinkageActivity.this, panelList, mySlidingMenu, accountType
//                                , new PanelAdapter.PanelAdapterListener() {
//                            @Override
//                            public void panel_adapter_listener() {//删除item时刷新界面
//                                getData(false);
//                            }
//                        });
//                        panelrecycl.setAdapter(adapter);
                        panelList.clear();
//                        panelList = user.panelList;
                        listtype.clear();
                        listint.clear();
                        listintwo.clear();
                        listpanelNumber.clear();
                        for (User.panellist ud : user.panelList) {
//                            listtype.add(ud.panelType);
//                            listpanelNumber.add(ud.panelNumber);
//                            listpanelName.add(ud.panelName);
//                            setPicture(ud.panelType);
                            Map map = new HashMap();
                            map.put("panelName", ud.panelName);
                            map.put("panelNumber", ud.panelNumber);
                            map.put("boxNumber", ud.boxNumber);
                            map.put("boxName", ud.boxName);
                            map.put("panelType", ud.panelType);
                            map.put("controllerId", "");

                            panelList.add(map);
                        }

                        if (user.wifiList != null)
                            for (User.wifi_device ud : user.wifiList) {
//                            listtype.add(ud.panelType);
//                            listpanelNumber.add(ud.panelNumber);
//                            listpanelName.add(ud.panelName);
//                            setPicture(ud.panelType);
                                Map map = new HashMap();
                                map.put("panelName", ud.name);
                                map.put("panelNumber", ud.number);
                                map.put("boxNumber", "");
                                map.put("panelType", ud.type);
                                map.put("boxName", "");
                                map.put("controllerId", ud.controllerId);
                                panelList.add(map);
                            }


                        for (Map map : panelList) {
                            listtype.add(map.get("panelType").toString());
                            listpanelNumber.add(map.get("panelNumber").toString());
                            listpanelName.add(map.get("panelName").toString());
                            setPicture(map.get("panelType").toString());
                        }


//                        LogUtil.i("这是设备长度2", "" + list.size());

//                adapter = new MacFragAdapter(getActivity(), list);
//                macfragritview_id.setAdapter(adapter);
                        // selectexcutesceneresultadapter.clear();
                        // adapter.addAll(list);//不要new adapter
                        selectexcutesceneresultadapter.setlist(panelList, listint, listintwo);
                        selectexcutesceneresultadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }



  /*  */

    /**
     * 获得智能设备列表
     *//*
    private void uploader_refresh() {
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(SelectiveLinkageActivity.this));
        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(SelectiveLinkageActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_getAllDevice, mapdevice,new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen数据
                uploader_refresh();
            }
        }, SelectiveLinkageActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);

            }

            @Override
            public void pullDataError() { //

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
                super.onSuccess(user);
                list.clear();
                listtype.clear();
                listint.clear();
                listintwo.clear();
                list.addAll(user.deviceList);
                for (User.device ud : list) {
                    listtype.add(ud.type);
                    setPicture(ud.type);
                }
                LogUtil.i("这是设备长度2", "" + list.size());

//                adapter = new MacFragAdapter(getActivity(), list);
//                macfragritview_id.setAdapter(adapter);
               // selectexcutesceneresultadapter.clear();
               // adapter.addAll(list);//不要new adapter
                selectexcutesceneresultadapter.setlist(list,listint,listintwo);
                selectexcutesceneresultadapter.notifyDataSetChanged();
            }
        });
    }*/
    private void setPicture(String type) {
        switch (type) {
//            case "1":
//                listint.add(R.drawable.icon_deng_40);
//                listintwo.add(R.drawable.icon_deng_40);
//                break;
//            case "2":
//                listint.add(R.drawable.icon_tiaoguang_40);
//                listintwo.add(R.drawable.icon_tiaoguang_40_active);
//                break;
//            case "3":
//                listint.add(R.drawable.icon_kongtiao_active_40);
//                listintwo.add(R.drawable.switch_kongtiao_70_active);
//                break;
//            case "4":
//                listint.add(R.drawable.icon_chuanglian_40);
//                listintwo.add(R.drawable.icon_chuanglian_40_active);
//                break;
//            case "5":
//                listint.add(R.drawable.defaultpic);
//                listintwo.add(R.drawable.defaultpic);
//                break;
//            case "6":
//                listint.add(R.drawable.defaultpic);
//                listintwo.add(R.drawable.defaultpic);
//                break;
//
//            case "7":
//                listint.add(R.drawable.icon_menci_40);
//                listintwo.add(R.drawable.icon_menci_40_active);
//                break;
//            case "8":
//                listint.add(R.drawable.icon_rentiganying_40);
//                listintwo.add(R.drawable.icon_rentiganying_40_active);
//                break;
//            case "9":
//                listint.add(R.drawable.icon_shuijin_40);
//                listintwo.add(R.drawable.icon_shuijin_40_active);
//                break;
//            case "10":
//                listint.add(R.drawable.icon_pm25_40);
//                listintwo.add(R.drawable.icon_pm25_40_active);
//                break;
//            case "11":
//                listint.add(R.drawable.icon_jinjianniu_40);
//                listintwo.add(R.drawable.icon_jinjianniu_40_active);
//                break;
//            case "12":
//                listint.add(R.drawable.icon_rucebjq_40);
//                listintwo.add(R.drawable.icon_rucebjq_40_active);
//                break;
//            case "13":
//                listint.add(R.drawable.icon_yanwubjq_40);
//                listintwo.add(R.drawable.icon_yanwubjq_40_active);
//                break;
//            case "14":
//                listint.add(R.drawable.icon_ranqibjq_40);
//                listintwo.add(R.drawable.icon_ranqibjq_40_active);
//                break;
//            case "15":
//                listint.add(R.drawable.icon_zhinengmensuo_40);
//                listintwo.add(R.drawable.icon_zhinengmensuo_40_active);
//                break;
//            case "16":
//                listint.add(R.drawable.ic_launcher);
//                listintwo.add(R.drawable.ic_launcher);
//                break;
            case "A201":
            case "A202":
            case "A203":
            case "A204":
                listint.add(R.drawable.icon_kaiguan_40);
                listintwo.add(R.drawable.icon_kaiguan_40_active);
                break;
            case "A301":
            case "A302":
            case "A303":
                listint.add(R.drawable.icon_tiaoguang_40);
                listintwo.add(R.drawable.icon_tiaoguang_40_active);
                break;
            case "A401":
                listint.add(R.drawable.icon_chuanglian_40);
                listintwo.add(R.drawable.icon_chuanglian_40_active);
                break;
            case "A511":
                listint.add(R.drawable.icon_kongtiao_40);
                listintwo.add(R.drawable.icon_kongtiao_40_active);
                break;
            case "A611":
                listint.add(R.drawable.defaultpic);
                listintwo.add(R.drawable.defaultpic);
                break;
            case "A711":
                listint.add(R.drawable.defaultpic);
                listintwo.add(R.drawable.defaultpic);
                break;
            case "A801":
                listint.add(R.drawable.icon_menci_40);
                listintwo.add(R.drawable.icon_menci_40_active);
                break;
            case "A901":
                listint.add(R.drawable.icon_rentiganying_40);
                listintwo.add(R.drawable.icon_rentiganying_40_active);
                break;
            case "A902":
                listint.add(R.drawable.icon_rucebjq_40);
                listintwo.add(R.drawable.icon_rucebjq_40_active);
                break;
            case "AB01":
                listint.add(R.drawable.icon_yanwubjq_40);
                listintwo.add(R.drawable.icon_yanwubjq_40_active);
                break;
            case "AB04":
                listint.add(R.drawable.icon_ranqibjq_40);
                listintwo.add(R.drawable.icon_ranqibjq_40_active);
                break;
            case "AC01":
                listint.add(R.drawable.icon_shuijin_40);
                listintwo.add(R.drawable.icon_shuijin_40_active);
                break;
            case "AD01":
                listint.add(R.drawable.icon_pm25_40);
                listintwo.add(R.drawable.icon_pm25_40_active);
                break;
            case "AD02":
                listint.add(R.drawable.defaultpic);
                listintwo.add(R.drawable.defaultpic);
                break;
            case "B001":
                listint.add(R.drawable.icon_jinjianniu_40);
                listintwo.add(R.drawable.icon_jinjianniu_40_active);
                break;
            case "B201":
                listint.add(R.drawable.icon_zhinengmensuo_40);
                listintwo.add(R.drawable.icon_zhinengmensuo_40_active);
                break;
            case "B301":
                listint.add(R.drawable.defaultpic);
                listintwo.add(R.drawable.defaultpic);
                break;
            case "AA02":
                listint.add(R.drawable.icon_yaokongqi_40);
                listintwo.add(R.drawable.icon_yaokongqi_40_active);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                SelectiveLinkageActivity.this.finish();
                break;
            case R.id.next_step_txt:
                intent = new Intent(SelectiveLinkageActivity.this, UnderWaterActivity.class);
//                intent.putExtra("type", (Serializable) again_elements[position]);
                startActivityForResult(intent, REQUEST_SENSOR);
                break;
            case R.id.rel_scene_set:
                intent = new Intent(SelectiveLinkageActivity.this,
                        ExcuteSomeOneSceneActivity.class);
                intent.putExtra("sensor_map", (Serializable) map_link);
                startActivity(intent);
                break;//执行某些手动场景
        }
    }


//    private void onLoad() {
//        xListView_scan.stopRefresh();
//        xListView_scan.stopLoadMore();
//        xListView_scan.setRefreshTime("刚刚");
//    }
//
//    @Override
//    public void onRefresh() {
//        onLoad();
//    }

//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onLoad();
//            }
//        }, 1000);
//    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        View v = parent.getChildAt(position - maclistview_id.getFirstVisiblePosition());
//        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
//        ImageView img_guan_scene = (ImageView) v.findViewById(R.id.img_guan_scene);
//        TextView panel_scene_name_txt = (TextView) v.findViewById(R.id.panel_scene_name_txt);
//        cb.toggle();
//        //设置checkbox现在状态
//        SelectSensorAdapter.getIsSelected().put(position, cb.isChecked());
//        if (cb.isChecked()) {
//            Intent intent = new Intent(SelectiveLinkageActivity.this, SelectiveLinkageDeviceDetailActivity.class);
//            intent.putExtra("type", (Serializable) listtype.get(position));
//            startActivityForResult(intent, REQUEST_SENSOR);
//            img_guan_scene.setImageResource(listintwo.get(position));
//            panel_scene_name_txt.setTextColor(getResources().getColor(R.color.gold_color));
//
//        } else {
//            img_guan_scene.setImageResource(listint.get(position));
//            panel_scene_name_txt.setTextColor(getResources().getColor(R.color.black_color));
//        }
        Intent intent = null;

        switch (listtype.get(position)) {
            case "A501":
                getAir501Data(listpanelNumber.get(position));
                break;
            default:
                intent = new Intent(SelectiveLinkageActivity.this, SelectiveLinkageDeviceDetailSecondActivity.class);
//            intent.putExtra("type", (Serializable) listtype.get(position));
                intent.putExtra("panelNumber", (Serializable) listpanelNumber.get(position));
                intent.putExtra("panelType", (Serializable) listtype.get(position));
                intent.putExtra("panelName", (Serializable) listpanelName.get(position));
                //传感器参数
//                Map mapdevice = new HashMap();
//                mapdevice.put("sensorType", map_link.get("deviceType"));
//                mapdevice.put("sensorId", map_link.get("deviceId"));
//                mapdevice.put("sensorName",map_link.get("name"));
//                mapdevice.put("sensorCondition", map_link.get("condition"));
//                mapdevice.put("sensorMinValue", map_link.get("minValue"));
//                mapdevice.put("sensorMaxValue", map_link.get("maxValue"));
                intent.putExtra("sensor_map", (Serializable) map_link);

                //
                startActivityForResult(intent, REQUEST_SENSOR);
                break;
            case "AA02"://wifi红外模块
                intent = new Intent(SelectiveLinkageActivity.this, SelectLinkageYaoKongQiActivity.class);
//            intent.putExtra("type", (Serializable) listtype.get(position));
                intent.putExtra("panelNumber", (Serializable) listpanelNumber.get(position));
                intent.putExtra("panelType", (Serializable) listtype.get(position));
                intent.putExtra("panelName", (Serializable) listpanelName.get(position));
                //传感器参数
//                Map mapdevice = new HashMap();
//                mapdevice.put("sensorType", map_link.get("deviceType"));
//                mapdevice.put("sensorId", map_link.get("deviceId"));
//                mapdevice.put("sensorName",map_link.get("name"));
//                mapdevice.put("sensorCondition", map_link.get("condition"));
//                mapdevice.put("sensorMinValue", map_link.get("minValue"));
//                mapdevice.put("sensorMaxValue", map_link.get("maxValue"));
                intent.putExtra("sensor_map", (Serializable) map_link);
                startActivity(intent);

                break;

        }


//        SelectSensorSingleAdapter.ViewHolderContentType viewHolder = (SelectSensorSingleAdapter.ViewHolderContentType) view.getTag();
//        viewHolder.checkbox.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选
    }

    /**
     * 空调面板501
     *
     * @param
     */
    private void getAir501Data(String panelNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(SelectiveLinkageActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(SelectiveLinkageActivity.this));
        map.put("panelNumber", panelNumber);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getData(false);
                    }
                }, SelectiveLinkageActivity.this, dialogUtil) {


                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
//                        refresh_view.stopRefresh(false);
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        List<Map> list = new ArrayList<>();
                        for (int position = 0; position < user.deviceList.size(); position++) {
                            Map<String, Object> mapdevice = new HashMap<String, Object>();
                            mapdevice.put("type", user.deviceList.get(position).type);
                            mapdevice.put("number", user.deviceList.get(position).number);
                            mapdevice.put("status", user.deviceList.get(position).status);
                            mapdevice.put("dimmer", user.deviceList.get(position).dimmer);
                            mapdevice.put("mode", user.deviceList.get(position).mode);
                            mapdevice.put("temperature", user.deviceList.get(position).temperature);
                            mapdevice.put("speed", user.deviceList.get(position).speed);
                            mapdevice.put("name", user.deviceList.get(position).name);
                            mapdevice.put("panelName", user.deviceList.get(position).panelName);

//                            //传感器参数
//                            mapdevice.put("sensorType", map_link.get("deviceType"));
//                            mapdevice.put("sensorId", map_link.get("deviceId"));
//                            mapdevice.put("sensorName", map_link.get("name"));
//                            mapdevice.put("sensorCondition", map_link.get("condition"));
//                            mapdevice.put("sensorMinValue", map_link.get("minValue"));
//                            mapdevice.put("sensorMaxValue", map_link.get("maxValue"));

                            //
                            list.add(mapdevice);
                        }

                        Intent intent = new Intent(
                                SelectiveLinkageActivity.this,
                                AirLinkageControlActivity.class
                        );
                        intent.putExtra("air_control_map", (Serializable) list.get(0));
                        intent.putExtra("sensor_map", (Serializable) map_link);
                        startActivity(intent);
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        getData(true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
