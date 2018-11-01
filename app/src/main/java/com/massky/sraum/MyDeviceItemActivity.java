package com.massky.sraum;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.percent.PercentRelativeLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import com.base.Basecactivity;
import com.data.User;
import com.example.swipemenuview.SwipeMenuLayout;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by zhu on 2018/1/16.
 */

public class MyDeviceItemActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    private Map panelItem_map = new HashMap();
    @InjectView(R.id.device_name_txt)
    TextView device_name_txt;
    @InjectView(R.id.mac_txt)
    TextView mac_txt;
    @InjectView(R.id.status_txt)
    TextView status_txt;
    @InjectView(R.id.project_select)
    TextView project_select;
    @InjectView(R.id.wangguan_set_rel)
    RelativeLayout wangguan_set_rel;
    @InjectView(R.id.scene_list_rel)
    RelativeLayout scene_list_rel;
    @InjectView(R.id.list_gujian_rel)
    PercentRelativeLayout list_gujian_rel;
    @InjectView(R.id.banben_txt)
    TextView banben_txt;
    @InjectView(R.id.panid_txt)
    TextView panid_txt;
    @InjectView(R.id.xindao_txt)
    TextView xindao_txt;
    @InjectView(R.id.gateway_id_txt)
    TextView gateway_id_txt;
    @InjectView(R.id.other_jiantou)
    ImageView other_jiantou;
    @InjectView(R.id.delete_device_rel)
    RelativeLayout delete_device_rel;
    private DialogUtil dialogUtil;
    private String panelNumber = "";
    @InjectView(R.id.next_step_id)
    Button next_step_id;
    @InjectView(R.id.wangguan_set)
    ImageView wangguan_set;
    @InjectView(R.id.rel_yaokongqi)
    RelativeLayout rel_yaokongqi;
    @InjectView(R.id.view_yaokongqi)
    View view_yaokongqi;
    @InjectView(R.id.dev_txt)
    TextView dev_txt;

    private int[] iconName = {R.string.yijianlight, R.string.liangjianlight, R.string.sanjianlight, R.string.sijianlight,
            R.string.yilutiaoguang, R.string.lianglutiaoguang, R.string.sanlutiao, R.string.window_panel, R.string.air_panel,
            R.string.air_mode,R.string.xinfeng_mode,R.string.dinuan_mode
            , R.string.menci, R.string.rentiganying, R.string.jiuzuo, R.string.yanwu, R.string.tianranqi, R.string.jinjin_btn,
            R.string.zhineng, R.string.pm25, R.string.shuijin, R.string.jixieshou,R.string.cha_zuo_1,R.string.cha_zuo, R.string.wifi_hongwai,
            R.string.wifi_camera,R.string.one_light_control,R.string.two_light_control,R.string.three_light_control
            ,R.string.two_dimming_one_control,R.string.two_dimming_two_control,R.string.two_dimming_trhee_control,R.string.keshimenling
    };

    @Override
    protected int viewId() {
        return R.layout.my_device_item_act;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        dialogUtil = new DialogUtil(this);
        back.setOnClickListener(this);
        wangguan_set_rel.setOnClickListener(this);
        scene_list_rel.setOnClickListener(this);
        next_step_id.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        panelItem_map = (Map) getIntent().getSerializableExtra("panelItem");
        Integer imgtype = (Integer) getIntent().getSerializableExtra("imgtype");
        if (panelItem_map != null) {
            device_name_txt.setText(panelItem_map.get("name").toString());
            project_select.setText(panelItem_map.get("name").toString());
            mac_txt.setText(panelItem_map.get("mac").toString());
            banben_txt.setText(panelItem_map.get("firmware").toString());
            panid_txt.setText(panelItem_map.get("hardware").toString());
            panelNumber = panelItem_map.get("id").toString();
            switch (panelItem_map.get("status").toString()) {
                case "0":
                    status_txt.setText("离线");
                    break;
                default:
                    status_txt.setText("在线");
                    break;
            }
            wangguan_set.setImageResource(imgtype);
            set_type(panelItem_map.get("type").toString());
            //成员，业主accountType->addrelative_id
            String accountType = (String) SharedPreferencesUtil.getData(MyDeviceItemActivity.this, "accountType", "");
            switch (accountType) {
                case "1":
                    delete_device_rel.setVisibility(View.VISIBLE);
                    wangguan_set_rel.setEnabled(true);
                    break;//业主
                case "2":
                    delete_device_rel.setVisibility(View.GONE);
                    wangguan_set_rel.setEnabled(false);
                    break;//家庭成员
            }
        }

        onEvent();
    }

    private void onEvent() {
        rel_yaokongqi.setOnClickListener(this);
    }

    private void set_type(String type) {
        switch (type) {
            case "A201":
                gateway_id_txt.setText(iconName[0]);
                break;
            case "A202":
                gateway_id_txt.setText(iconName[1]);
                break;
            case "A203":
                gateway_id_txt.setText(iconName[2]);
                break;
            case "A204":
                gateway_id_txt.setText(iconName[3]);
                break;
            case "A301":
                gateway_id_txt.setText(iconName[4]);
                break;
            case "A302":
                gateway_id_txt.setText(iconName[5]);
                break;
            case "A303":
                gateway_id_txt.setText(iconName[6]);
                break;
            case "A401":
                gateway_id_txt.setText(iconName[7]);
                break;
            case "A501":
                gateway_id_txt.setText(iconName[8]);
                break;
            case "A511":
                gateway_id_txt.setText(iconName[9]);
                break;
            case "A611":
                gateway_id_txt.setText(iconName[10]);
                break;
            case "A711":
                gateway_id_txt.setText(iconName[11]);
                break;
            case "A801":
                gateway_id_txt.setText(iconName[12]);
                break;
            case "A901":
                gateway_id_txt.setText(iconName[13]);
                break;
            case "A902":
                gateway_id_txt.setText(iconName[14]);
                break;
            case "AB01":
                gateway_id_txt.setText(iconName[15]);
                break;
            case "AB04":
                gateway_id_txt.setText(iconName[16]);
                break;
            case "B001":
                gateway_id_txt.setText(iconName[17]);
                break;
            case "B201":
                gateway_id_txt.setText(iconName[18]);
                break;
            case "AD01":
                gateway_id_txt.setText(iconName[19]);
                break;
            case "AC01":
                gateway_id_txt.setText(iconName[20]);
                break;
            case "B301":
                gateway_id_txt.setText(iconName[21]);
                break;
            case "B101":
                gateway_id_txt.setText(iconName[22]);
                break;
            case "B102":
                gateway_id_txt.setText(iconName[23]);
                break;
            case "AA02"://WIFI转发模块
                gateway_id_txt.setText(iconName[24]);
                rel_yaokongqi.setVisibility(View.VISIBLE);
                view_yaokongqi.setVisibility(View.VISIBLE);
                dev_txt.setText("WIFI");
                banben_txt.setText(panelItem_map.get("wifi").toString());
                //controllerId
                mac_txt.setText(panelItem_map.get("controllerId").toString());
                break;

            case "AA03"://WIFI转发模块
                gateway_id_txt.setText(iconName[25]);
                dev_txt.setText("WIFI");
                banben_txt.setText(panelItem_map.get("wifi").toString());
                //controllerId
                mac_txt.setText(panelItem_map.get("controllerId").toString());
                break;
            case "A311":
                gateway_id_txt.setText(iconName[26]);
                break;
            case "A312":
                gateway_id_txt.setText(iconName[27]);
                break;
            case "A313":
                gateway_id_txt.setText(iconName[28]);
                break;
            case "A321":
                gateway_id_txt.setText(iconName[29]);
                break;
            case "A322":
                gateway_id_txt.setText(iconName[30]);
                break;
            case "A331":
                gateway_id_txt.setText(iconName[31]);
                break;
            case "AA04"://WIFI转发模块
                gateway_id_txt.setText(iconName[32]);
                dev_txt.setText("WIFI");
                banben_txt.setText(panelItem_map.get("wifi").toString());
                //controllerId
                mac_txt.setText(panelItem_map.get("controllerId").toString());
                break;

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                MyDeviceItemActivity.this.finish();
                break;
            case R.id.wangguan_set_rel:
                if (panelItem_map != null) {
                    intent = new Intent(MyDeviceItemActivity.this, EditMyDeviceActivity.class);
                    intent.putExtra("panelItem", (Serializable) panelItem_map);
                    startActivity(intent);
                }
                break;
            case R.id.scene_list_rel:
                if (list_gujian_rel.getVisibility() == View.VISIBLE) {
                    list_gujian_rel.setVisibility(View.GONE);
                    other_jiantou.setImageResource(R.drawable.bg_you);
                } else {
                    list_gujian_rel.setVisibility(View.VISIBLE);
                    other_jiantou.setImageResource(R.drawable.bg_xia);
                }
                break;
            case R.id.next_step_id://删除设备
                showCenterDeleteDialog(panelNumber, panelItem_map.get("name").toString(), panelItem_map.get("type").toString());
                break;
            case R.id.rel_yaokongqi://跳转到遥控器列表界面
                intent = new Intent(MyDeviceItemActivity.this, SelectYaoKongQiActivity.class);
                intent.putExtra("controllerNumber", panelItem_map.get("controllerId").toString());//controllerNumber
                startActivity(intent);
                break;
        }
    }


    //自定义dialog,centerDialog删除对话框
    public void showCenterDeleteDialog(final String panelNumber, final String name, final String type) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // 布局填充器
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view = inflater.inflate(R.layout.user_name_dialog, null);
//        // 设置自定义的对话框界面
//        builder.setView(view);
//
//        cus_dialog = builder.create();
//        cus_dialog.show();

        View view = LayoutInflater.from(MyDeviceItemActivity.this).inflate(R.layout.promat_dialog, null);
        TextView confirm; //确定按钮
        TextView cancel; //确定按钮
        TextView tv_title;
        TextView name_gloud;
//        final TextView content; //内容
        cancel = (TextView) view.findViewById(R.id.call_cancel);
        confirm = (TextView) view.findViewById(R.id.call_confirm);
        tv_title = (TextView) view.findViewById(R.id.tv_title);//name_gloud
        name_gloud = (TextView) view.findViewById(R.id.name_gloud);
        name_gloud.setText(name);
//        tv_title.setText("是否拨打119");
//        content.setText(message);
        //显示数据
        final Dialog dialog = new Dialog(MyDeviceItemActivity.this, R.style.BottomDialog);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.8); //宽度设置为屏幕的0.5
//        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
//        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.getWindow().setAttributes(p);  //设置生效
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sraum_deletepanel(panelNumber, type, dialog);
            }
        });
    }


    /**
     * 删除设备
     *
     * @param
     * @param dialog
     */
    private void sraum_deletepanel(final String panelNumber, final String type, final Dialog dialog) {
        if (dialogUtil != null)
            dialogUtil.loadDialog();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", TokenUtil.getToken(MyDeviceItemActivity.this));
        String send_method = "";
        switch (type) {
            case "AA02":
                map.put("number", panelNumber);
                send_method = ApiHelper.sraum_deleteWifiApple;
                break;//wifi模块
            case "AA03":
                map.put("number", panelNumber);
                send_method = ApiHelper.sraum_deleteWifiCamera;
                break;//wifi模块
            default:
                map.put("boxNumber", TokenUtil.getBoxnumber(MyDeviceItemActivity.this));
                map.put("panelNumber", panelNumber);
                send_method = ApiHelper.sraum_deletePanel;
                break;
        }
        MyOkHttp.postMapObject(send_method, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_deletepanel(panelNumber, type, dialog);
                    }
                }, MyDeviceItemActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        dialog.dismiss();
                        MyDeviceItemActivity.this.finish();
                        switch (type) {
                            case "AA02":
                                SharedPreferencesUtil.saveInfo_List(MyDeviceItemActivity.this, "remoteairlist",
                                        new ArrayList<Map>());
                                SharedPreferencesUtil.saveData(MyDeviceItemActivity.this, "mGizWifiDevice", "");
                                break;
                        }
                    }

                    @Override
                    public void fourCode() {
                        dialog.dismiss();
                        ToastUtil.showToast(MyDeviceItemActivity.this, "删除失败");
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
