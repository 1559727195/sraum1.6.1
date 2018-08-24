package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.Util.view.SlideSwitchButton;
import com.base.Basecactivity;
import com.data.User;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by zhu on 2018/1/5.
 */

public class SelectiveLinkageDeviceDetailActivity extends Basecactivity implements SlideSwitchButton.SlideListener
        , SeekBar.OnSeekBarChangeListener {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.air_control_linear)
    LinearLayout air_control_linear;//空调
    @InjectView(R.id.window_linear)
    LinearLayout window_linear;//窗帘
    @InjectView(R.id.tiaoguangdeng_linear)
    LinearLayout tiaoguangdeng_linear;//调光灯
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    @InjectView(R.id.project_select)
    TextView project_select;
    @InjectView(R.id.status_view)
    StatusView statusView;
    private DialogUtil dialogUtil;
    private String panelNumber;
    private List<User.device> deviceList = new ArrayList<>();
    private List<Map> deviceActionList = new ArrayList<>();//执行动作集合
    private ArrayList<Map> list_type_index = new ArrayList<>();//收集某种设备类型的索引集合


    /***
     * 灯控
     */
    @InjectView(R.id.kaiguan_linear)
    LinearLayout kaiguan_linear;

    @InjectView(R.id.first_light_linear)
    LinearLayout first_light_linear;
    @InjectView(R.id.second_light_linear)
    LinearLayout second_light_linear;
    @InjectView(R.id.third_light_linear)
    LinearLayout third_light_linear;
    @InjectView(R.id.four_light_linear)
    LinearLayout four_light_linear;

    @InjectView(R.id.hand_device_content)
    TextView hand_device_content;
    @InjectView(R.id.hand_device_content_two)
    TextView hand_device_content_two;
    @InjectView(R.id.hand_device_content_three)
    TextView hand_device_content_three;
    @InjectView(R.id.hand_device_content_four)
    TextView hand_device_content_four;

    @InjectView(R.id.light_slide_one)
    SlideSwitchButton light_slide_one;
    @InjectView(R.id.light_slide_two)
    SlideSwitchButton light_slide_two;
    @InjectView(R.id.light_slide_three)
    SlideSwitchButton light_slide_three;
    @InjectView(R.id.light_slide_four)
    SlideSwitchButton light_slide_four;

    @InjectView(R.id.view_light_one)
    View view_light_one;
    @InjectView(R.id.view_light_two)
    View view_light_two;
    @InjectView(R.id.view_light_three)
    View view_light_three;
    @InjectView(R.id.view_light_four)
    View view_light_four;

    /***
     * 灯控
     */

    /**
     * 调光灯
     *
     * @return
     */
    @InjectView(R.id.tiao_linear_light_one)
    LinearLayout tiao_linear_light_one;
    @InjectView(R.id.tiao_linear_light_two)
    LinearLayout tiao_linear_light_two;
    @InjectView(R.id.tiao_linear_light_three)
    LinearLayout tiao_linear_light_three;
    @InjectView(R.id.tiao_linear_radio_one)
    RadioGroup tiao_linear_radio_one;
    @InjectView(R.id.tiao_linear_radio_two)
    RadioGroup tiao_linear_radio_two;
    @InjectView(R.id.tiao_linear_radio_three)
    RadioGroup tiao_linear_radio_three;
    @InjectView(R.id.tiao_linear_one)
    LinearLayout tiao_linear_one;
    @InjectView(R.id.tiao_linear_two)
    LinearLayout tiao_linear_two;
    @InjectView(R.id.tiao_linear_three)
    LinearLayout tiao_linear_three;
    @InjectView(R.id.seek_bar_txt_one)
    TextView seek_bar_txt_one;
    @InjectView(R.id.seek_bar_txt_two)
    TextView seek_bar_txt_two;
    @InjectView(R.id.seek_bar_txt_three)
    TextView seek_bar_txt_three;
    @InjectView(R.id.seek_bar_one)
    SeekBar seek_bar_one;
    @InjectView(R.id.seek_bar_two)
    SeekBar seek_bar_two;
    @InjectView(R.id.seek_bar_three)
    SeekBar seek_bar_three;

    /**
     * 调光灯
     *
     * @return
     */

    /**
     * 窗帘
     *
     * @return
     */
    @InjectView(R.id.window_radio_light)
    RadioGroup window_radio_light;
    @InjectView(R.id.window_radio_inner)
    RadioGroup window_radio_inner;
    @InjectView(R.id.window_radio_out)
    RadioGroup window_radio_out;
    /**
     * 窗帘
     * @return
     */

    /**
     * 空调
     *
     * @return
     */

    @InjectView(R.id.air_control_radio_model)
    RadioGroup air_control_radio_model;
    @InjectView(R.id.air_control_tmp_del)
    ImageView air_control_tmp_del;
    //air_control_tmp_add
    @InjectView(R.id.air_control_tmp_add)
    ImageView air_control_tmp_add;
    @InjectView(R.id.air_control_speed)
    RadioGroup air_control_speed;
    @InjectView(R.id.air_control_radio_open_close)
    RadioGroup air_control_radio_open_close;
    private String status = "0";

    /**
     * 空调
     *
     * @return
     */

    @Override
    protected int viewId() {
        return R.layout.selective_link_devdetail;
    }

    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        dialogUtil = new DialogUtil(this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
//        String type = (String) getIntent().getSerializableExtra("type");
        panelNumber = (String) getIntent().getSerializableExtra("panelNumber");
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);

        getData(true);
        onEvent();
    }

    private void onEvent() {
        /**
         * 灯控
         */
        light_slide_one.setSlideListener(this);
        light_slide_two.setSlideListener(this);
        light_slide_three.setSlideListener(this);
        light_slide_four.setSlideListener(this);
        /**
         * 灯控
         *
         */

        /**
         * 调光灯
         */
        tiaoguang_light_radio();
        /**
         * 调光灯
         */

        /**
         * 窗帘
         */
        window_curtain();
        /**
         * 窗帘
         */
    }

    /**
     * 窗帘控制
     */
    private void window_curtain() {
        /**
         * 内纱
         */
        for (int i = 0; i < window_radio_inner.getChildCount(); i++) {
            final View view = window_radio_inner.getChildAt(i);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String position_index = select_index_by_type(1, "4");

                    int position = (int) view.getTag();
                    switch (position) {
                        case 0:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "1");
                            break;
                        case 1:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "0");
                            break;
                    }
                }
            });
        }


        /**
         * 外纱
         */
        for (int i = 0; i < window_radio_out.getChildCount(); i++) {
            final View view = window_radio_out.getChildAt(i);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String position_index = select_index_by_type(1, "4");
                    int position = (int) view.getTag();
                    switch (position) {
                        case 0:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "1");
                            break;
                        case 1:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "0");
                            break;
                    }
                }
            });
        }

        /**
         * 阳台灯
         */
        for (int i = 0; i < window_radio_light.getChildCount(); i++) {
            final View view = window_radio_light.getChildAt(i);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String position_index = select_index_by_type(1, "1");
                    int position = (int) view.getTag();
                    switch (position) {
                        case 0:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "1");
                            break;
                        case 1:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "0");
                            break;
                        case 2:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "3");
                            break;
                    }
                }
            });
        }
    }

    /**
     * 调光灯-》radiogroup
     */
    private void tiaoguang_light_radio() {
        /**
         * 调光灯
         */
        tiaoguanglight();
        seek_bar_one.setOnSeekBarChangeListener(this);
        seek_bar_two.setOnSeekBarChangeListener(this);
        seek_bar_three.setOnSeekBarChangeListener(this);
        /**
         * 调光灯
         */
    }

    /**
     * 调光灯小灯
     */
    private void tiaoguanglight() {
        /**
         * 第一路调光小灯
         */
        for (int i = 0; i < tiao_linear_radio_one.getChildCount(); i++) {
            final View view = tiao_linear_radio_one.getChildAt(i);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String position_index = select_index_by_type(1, "1");

                    int position = (int) view.getTag();
                    switch (position) {
                        case 0:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "1");
                            break;
                        case 1:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "0");
                            break;
                        case 2:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "3");
                            break;
                    }
                }
            });
        }


        /**
         * 第二路调光小灯
         */
        for (int i = 0; i < tiao_linear_radio_two.getChildCount(); i++) {
            final View view = tiao_linear_radio_two.getChildAt(i);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String position_index = select_index_by_type(2, "1");
                    int position = (int) view.getTag();
                    switch (position) {
                        case 0:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "1");
                            break;
                        case 1:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "0");
                            break;
                        case 2:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "3");
                            break;
                    }
                }
            });
        }

        /**
         * 第三路调光小灯
         */
        for (int i = 0; i < tiao_linear_radio_three.getChildCount(); i++) {
            final View view = tiao_linear_radio_three.getChildAt(i);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String position_index = select_index_by_type(3, "1");
                    int position = (int) view.getTag();
                    switch (position) {
                        case 0:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "1");
                            break;
                        case 1:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "0");
                            break;
                        case 2:
                            init_device_upgrade_action(Integer.parseInt(position_index), "status", "3");
                            break;
                    }
                }
            });
        }
    }

    /**
     * 根据索引-》type->设备真正的位置
     *
     * @param index->为第几次出现
     */
    private String select_index_by_type(int index, String type) {
        String content_index = "";
        int add = 0;
        for (int i = 0; i < list_type_index.size(); i++) {
//            switch (list_type_index.get(i).get("type").toString()) {
//               if
//            }

            if (list_type_index.get(i).get("type").toString().equals(type)) {
                add++;
            }
            if (add == index) {//当出现的次数叠加等于第几次出现时，就去获取设备具体的索引位置
                content_index = list_type_index.get(i).get("index").toString();
                break;
            }
        }

        return content_index;
    }


    private void getData(boolean flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(SelectiveLinkageDeviceDetailActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(SelectiveLinkageDeviceDetailActivity.this));
        map.put("panelNumber", panelNumber);
        if (flag) {
            dialogUtil.loadDialog();
        }
        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getData(false);
                    }
                }, SelectiveLinkageDeviceDetailActivity.this, dialogUtil) {


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
                        deviceList.clear();
                        deviceActionList.clear();
                        list_type_index.clear();

                        for (int position = 0; position < user.deviceList.size(); position++) {
                            Map<String, Object> mapdevice = new HashMap<String, Object>();

                            Map<String, Object> type_index = new HashMap<>();
                            mapdevice.put("type", user.deviceList.get(position).type);
                            mapdevice.put("number", user.deviceList.get(position).number);
                            mapdevice.put("status", status);
                            mapdevice.put("dimmer", user.deviceList.get(position).dimmer);
                            mapdevice.put("mode", user.deviceList.get(position).mode);
                            mapdevice.put("temperature", user.deviceList.get(position).temperature);
                            mapdevice.put("speed", user.deviceList.get(position).speed);

                            mapdevice.put("name", user.deviceList.get(position).name);
                            mapdevice.put("panelName", user.deviceList.get(position).panelName);
                            type_index.put("type", user.deviceList.get(position).type);
                            type_index.put("index", position);
                            list_type_index.add(type_index);
                            deviceActionList.add(mapdevice);
                        }
                        deviceList.addAll(user.deviceList);
                        setPicture(user);
//                        panelrecycl.setAdapter(adapter);
//                        panelList.clear();
//                        panelList = user.panelList;
//                        listtype.clear();
//                        listint.clear();
//                        listintwo.clear();
//                        listpanelNumber.clear();
//                        for (User.panellist ud : panelList) {
//                            listtype.add(ud.panelType);
//                            listpanelNumber.add(ud.panelNumber);
//                            setPicture(ud.panelType);
//                        }
////                        LogUtil.i("这是设备长度2", "" + list.size());
//
////                adapter = new MacFragAdapter(getActivity(), list);
////                macfragritview_id.setAdapter(adapter);
//                        // selectexcutesceneresultadapter.clear();
//                        // adapter.addAll(list);//不要new adapter
//                        selectexcutesceneresultadapter.setlist(panelList, listint, listintwo);
//                        selectexcutesceneresultadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    /**
     * 根据面板类型显示
     *
     * @param user
     */
    private void setPicture(User user) {
        String panelType = user.panelType;
        String panelName = user.panelName;
        project_select.setText(panelName);
        switch (panelType) {
            case "A201":
                kaiguan_linear.setVisibility(View.VISIBLE);
                first_light_linear.setVisibility(View.VISIBLE);
                view_light_one.setVisibility(View.VISIBLE);
                hand_device_content.setText(deviceList.get(0).name);

                break;
            case "A202":
                kaiguan_linear.setVisibility(View.VISIBLE);
                first_light_linear.setVisibility(View.VISIBLE);
                view_light_one.setVisibility(View.VISIBLE);
                second_light_linear.setVisibility(View.VISIBLE);
                view_light_two.setVisibility(View.VISIBLE);
                hand_device_content.setText(deviceList.get(0).name);
                hand_device_content_two.setText(deviceList.get(1).name);
                break;
            case "A203":
                kaiguan_linear.setVisibility(View.VISIBLE);
                first_light_linear.setVisibility(View.VISIBLE);
                view_light_one.setVisibility(View.VISIBLE);
                second_light_linear.setVisibility(View.VISIBLE);
                view_light_two.setVisibility(View.VISIBLE);
                third_light_linear.setVisibility(View.VISIBLE);
                view_light_three.setVisibility(View.VISIBLE);
                hand_device_content.setText(deviceList.get(0).name);
                hand_device_content_two.setText(deviceList.get(1).name);
                hand_device_content_three.setText(deviceList.get(2).name);
                break;
            case "A204":
                kaiguan_linear.setVisibility(View.VISIBLE);
                first_light_linear.setVisibility(View.VISIBLE);
                view_light_one.setVisibility(View.VISIBLE);
                second_light_linear.setVisibility(View.VISIBLE);
                view_light_two.setVisibility(View.VISIBLE);
                third_light_linear.setVisibility(View.VISIBLE);
                view_light_three.setVisibility(View.VISIBLE);
                four_light_linear.setVisibility(View.VISIBLE);
                view_light_four.setVisibility(View.VISIBLE);
                hand_device_content.setText(deviceList.get(0).name);
                hand_device_content_two.setText(deviceList.get(1).name);
                hand_device_content_three.setText(deviceList.get(2).name);
                hand_device_content_four.setText(deviceList.get(3).name);
                break;
            case "A511":
                air_control_linear.setVisibility(View.VISIBLE);
                break;
            case "A401"://窗帘
                window_linear.setVisibility(View.VISIBLE);
                break;
            case "A301"://
                tiaoguangdeng_linear.setVisibility(View.VISIBLE);
                tiao_linear_light_one.setVisibility(View.VISIBLE);
                tiao_linear_light_two.setVisibility(View.VISIBLE);
                tiao_linear_light_three.setVisibility(View.VISIBLE);
                tiao_linear_one.setVisibility(View.VISIBLE);
                break;
            case "A302":
                tiaoguangdeng_linear.setVisibility(View.VISIBLE);
                tiao_linear_light_one.setVisibility(View.VISIBLE);
                tiao_linear_light_two.setVisibility(View.VISIBLE);
                tiao_linear_one.setVisibility(View.VISIBLE);
                tiao_linear_two.setVisibility(View.VISIBLE);
                break;

            case "A303":
                tiaoguangdeng_linear.setVisibility(View.VISIBLE);
                tiao_linear_light_one.setVisibility(View.VISIBLE);
                tiao_linear_one.setVisibility(View.VISIBLE);
                tiao_linear_two.setVisibility(View.VISIBLE);
                tiao_linear_three.setVisibility(View.VISIBLE);
                break;
//            case "A301"://一键调光，3键灯控  设备4个
//            case "A302"://两键调光，2键灯控
//            case "A303"://三键调光，一键灯控
        }
    }

//
//    @Override
//    protected void onEvent() {
//        back.setOnClickListener(this);
//        next_step_txt.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onData() {
//
//    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back:
//                HandAddSceneDeviceDetailActivity.this.finish();
//                break;
//            case R.id.next_step_txt:
//                HandAddSceneDeviceDetailActivity.this.finish();
//                break;
//        }
        switch (v.getId()) {
            case R.id.back:
                SelectiveLinkageDeviceDetailActivity.this.finish();
                break;
            case R.id.next_step_txt:
                startActivity(new Intent(SelectiveLinkageDeviceDetailActivity.this
                        , SetSelectLinkActivity.class));
                break;


        }
    }


    @Override
    public void openState(boolean isOpen, View view) {


        switch (view.getId()) {
            /**
             * 灯控
             */
            case R.id.light_slide_one:
                if (isOpen) {
                    init_device_upgrade_action(0, "status", "1");
                } else {
                    init_device_upgrade_action(0, "status", "0");
                }
                //init_device_action(int position, boolean doit, String content,String doit_type)
                break;
            case R.id.light_slide_two:
                if (isOpen) {
                    init_device_upgrade_action(1, "status", "1");
                } else {
                    init_device_upgrade_action(1, "status", "0");
                }
                break;
            case R.id.light_slide_three:
                if (isOpen) {
                    init_device_upgrade_action(2, "status", "1");
                } else {
                    init_device_upgrade_action(2, "status", "0");
                }
                break;
            case R.id.light_slide_four:
                if (isOpen) {
                    init_device_upgrade_action(3, "status", "1");
                } else {
                    init_device_upgrade_action(3, "status", "0");
                }
                break;
            /**
             * 灯控
             */

            /***
             * 调光灯
             */

            /**
             * 调光灯
             */
        }


    }


    /**
     * 更新执行设备的动作
     */
    private void init_device_upgrade_action(int position, String type, String content) {
//                mapdevice.put("type", doit == true ? content : deviceList.get(position).type);
        deviceActionList.get(position).put(type, content);
//        deviceActionList.add(mapdevice);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
        String position_index = "";
        switch (seekBar.getId()) {
            case R.id.seek_bar_one://第一路调光
//                                ToastUtil.showToast(SelectiveLinkageDeviceDetailActivity.this,"seek_bar_one");

                seek_bar_txt_one.setText(progress + "");
                position_index = select_index_by_type(1, "2");
                init_device_upgrade_action(Integer.parseInt(position_index), "dimmer", progress + "");


                break;
            case R.id.seek_bar_two://第二路调光
//                            ToastUtil.showToast(SelectiveLinkageDeviceDetailActivity.this,"seek_bar_two");
                seek_bar_txt_two.setText(progress + "");
                position_index = select_index_by_type(2, "2");
                init_device_upgrade_action(Integer.parseInt(position_index), "dimmer", progress + "");
                break;
            case R.id.seek_bar_three://第三路调光
//                            ToastUtil.showToast(SelectiveLinkageDeviceDetailActivity.this,"seek_bar_three");
                seek_bar_txt_three.setText(progress + "");
                position_index = select_index_by_type(3, "2");
                init_device_upgrade_action(Integer.parseInt(position_index), "dimmer", progress + "");
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
