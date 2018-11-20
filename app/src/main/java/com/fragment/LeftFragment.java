package com.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.App;
import com.Util.AppManager;
import com.Util.BitmapUtil;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.SharedPreferencesUtil;
import com.base.Basecfragment;
import com.massky.sraum.LoginActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.R;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by masskywcy on 2016-09-09.
 */
public class LeftFragment extends Basecfragment {
    @InjectView(R.id.relativeone_id)
    RelativeLayout relativeone_id;
    @InjectView(R.id.relativetwo_id)
    RelativeLayout relativetwo_id;
    @InjectView(R.id.relativethree_id)
    RelativeLayout relativethree_id;
    @InjectView(R.id.relativefour_id)
    RelativeLayout relativefour_id;
    @InjectView(R.id.relativefive_id)
    RelativeLayout relativefive_id;
    @InjectView(R.id.relativesix_id)
    RelativeLayout relativesix_id;
    @InjectView(R.id.relativeseven_id)
    RelativeLayout relativeseven_id;
    @InjectView(R.id.relativeight_id)
    RelativeLayout relativeight_id;
    @InjectView(R.id.relativenine_id)
    RelativeLayout relativenine_id;
    @InjectView(R.id.relative_message_id)
    RelativeLayout relative_message_id;
    @InjectView(R.id.headportrait_id)
    CircleImageView headportrait_id;
    @InjectView(R.id.nickaname)
    TextView nickaname;
    @InjectView(R.id.mydevice_id)
    RelativeLayout mydevice_id;

    int BLACK = Color.parseColor("#d0d0d0");
    int GRAY = Color.parseColor("#e3e3e3");
    int newContent;
    private TextView dtext_id, belowtext_id;
    private Button qxbutton_id, checkbutton_id;
    private DialogUtil dialogBack;
    private boolean dialogFlag = true;
    private ScanReceiver scanReceiver;
    public static String MESSAGE_TONGZHI = "com.fragment.message_tongzhi";

    @Override
    protected int viewId() {
        return R.layout.layout_menu;
    }

    @Override
    protected void onView() {
        inti();
        deleteDialog();
        scanReceiver = new ScanReceiver();
        IntentFilter filter = new IntentFilter();//控制成功后刷新数据
        filter.addAction("UPSTATUS");//
        filter.addAction(MESSAGE_TONGZHI);
        getActivity().registerReceiver(scanReceiver, filter);
    }

    class ScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if ("UPSTATUS".equals(intent.getAction())) {
                String type = intent.getStringExtra("type");
                switch (type) {
//                    case "device":
//                        clear();
//                        dialogFlag = true;
//                        relativethree_id.setBackgroundColor(BLACK);
//                        newContent = 5;
//                        break;
//                    case "panel":
//                        clear();
//                        dialogFlag = true;
//                        relativenine_id.setBackgroundColor(BLACK);
//                        newContent = 4;
//                        break;
                    case "no_gateway"://没有网关
                        clear();
                        dialogFlag = true;
                        relativetwo_id.setBackgroundColor(BLACK);
                        newContent = 2;
                        break;
                }
                if (dialogFlag) {
                    switchFragment(newContent, type, "");
                }
            } else if (MESSAGE_TONGZHI.equals(intent.getAction())) {
                String type = intent.getStringExtra("type");
                String uid = intent.getStringExtra("uid");
                switch (type) {
                    case "1"://首页
                        clear();
                        dialogFlag = true;
                        relativeone_id.setBackgroundColor(BLACK);
                        newContent = 0;
                        break;
                    case "2"://场景
                        clear();
                        dialogFlag = true;
                        relativefive_id.setBackgroundColor(BLACK);
                        newContent = 5;
                        break;
                    case "51"://消息
                        clear();
                        dialogFlag = true;
                        relative_message_id.setBackgroundColor(BLACK);
                        newContent = 1;
                        break;
                    case "52"://可视门铃报警
                    case "53"://摄像头报警
                        clear();
                        dialogFlag = true;
                        relative_message_id.setBackgroundColor(BLACK);
                        newContent = 0;
                        break;
                }
                if (dialogFlag) {
                    switchFragment(newContent, type, uid);
                }
//                AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
            }
        }
    }


    private void inti() {
        relativeone_id.setBackgroundColor(BLACK);
        relativeone_id.setOnClickListener(this);
        relativetwo_id.setOnClickListener(this);
        relativethree_id.setOnClickListener(this);
        relativefour_id.setOnClickListener(this);
        relativefive_id.setOnClickListener(this);
        relativesix_id.setOnClickListener(this);
        relativeseven_id.setOnClickListener(this);
        relativeight_id.setOnClickListener(this);
        relativenine_id.setOnClickListener(this);
        relative_message_id.setOnClickListener(this);
        mydevice_id.setOnClickListener(this);
    }

    //设置用户头像
    @Override
    public void onResume() {
        super.onResume();
        String avatar = (String) SharedPreferencesUtil.
                getData(App.getInstance().getApplicationContext(), "avatar", "");
        headportrait_id.setImageBitmap(BitmapUtil.stringtoBitmap(avatar));
        String name = (String) SharedPreferencesUtil.getData(getActivity(), "userName", "");
        String loginPhone = (String) SharedPreferencesUtil.getData(getActivity(), "loginPhone", "");
        if (name.trim().equals("")) {
            name = loginPhone;
        }
        nickaname.setText(name);
        android.util.Log.e("peng", "LeftFragment->onResume:name:" + name);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.relativeone_id:
                clear();
                dialogFlag = true;
                relativeone_id.setBackgroundColor(BLACK);
                newContent = 0;
                break;
            case R.id.relative_message_id:
                clear();
                dialogFlag = true;
                relative_message_id.setBackgroundColor(BLACK);
                newContent = 1;
                break;
            case R.id.relativetwo_id:
                clear();
                dialogFlag = true;
                relativetwo_id.setBackgroundColor(BLACK);
                newContent = 2;
                break;
            case R.id.mydevice_id:
                clear();
                dialogFlag = true;
                mydevice_id.setBackgroundColor(BLACK);
                newContent = 3;
                break;
//            case R.id.relativethree_id:
//                clear();
//                dialogFlag = true;
//                relativethree_id.setBackgroundColor(BLACK);
//                newContent = 4;
//                break;
            case R.id.relativefour_id:
                clear();
                dialogFlag = true;
                relativefour_id.setBackgroundColor(BLACK);
                newContent = 4;
                break;
            case R.id.relativefive_id:
                clear();
                dialogFlag = true;
                relativefive_id.setBackgroundColor(BLACK);
                newContent = 5;
                break;
            case R.id.relativesix_id:
                clear();
                dialogFlag = true;
                relativesix_id.setBackgroundColor(BLACK);
                newContent = 6;
                break;
            case R.id.relativeseven_id:
                clear();
                dialogFlag = true;
                relativeseven_id.setBackgroundColor(BLACK);
                newContent = 7;
                break;
            case R.id.relativeight_id:
                dialogFlag = false;
                clear();
                dialogBack.loadViewdialog();
                relativeight_id.setBackgroundColor(BLACK);
                break;
//            case R.id.relativenine_id:
//                clear();
//                dialogFlag = true;
//                relativenine_id.setBackgroundColor(BLACK);
//                newContent = 7;
//                break;
            case R.id.qxbutton_id:
                dialogFlag = false;
                dialogBack.removeviewDialog();
                break;
            case R.id.checkbutton_id:
                dialogFlag = false;
                dialogBack.removeviewDialog();
                clear();
                SharedPreferencesUtil.saveData(getActivity(), "loginflag", false);
                IntentUtil.startActivityAndFinishFirst(getActivity(), LoginActivity.class);
                AppManager.getAppManager().finishAllActivity();
                break;
            default:
                break;
        }
        if (dialogFlag) {
            switchFragment(newContent, "", "");
        }
    }

    //删除dialog提示框
    private void deleteDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.check, null);
        dtext_id = (TextView) view.findViewById(R.id.dtext_id);
        belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
        qxbutton_id = (Button) view.findViewById(R.id.qxbutton_id);
        checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        dtext_id.setText("退出登录");
        belowtext_id.setText("确定退出登录吗？");
        qxbutton_id.setOnClickListener(this);
        checkbutton_id.setOnClickListener(this);
        dialogBack = new DialogUtil(getActivity(), view);
    }

    private void clear() {
        relativeone_id.setBackgroundColor(GRAY);
        relativetwo_id.setBackgroundColor(GRAY);
        relativethree_id.setBackgroundColor(GRAY);
        relativefour_id.setBackgroundColor(GRAY);
        relativefive_id.setBackgroundColor(GRAY);
        relativesix_id.setBackgroundColor(GRAY);
        relativeseven_id.setBackgroundColor(GRAY);
        relativeight_id.setBackgroundColor(GRAY);
        relativenine_id.setBackgroundColor(GRAY);
        relative_message_id.setBackgroundColor(GRAY);
        mydevice_id.setBackgroundColor(GRAY);
    }

    /**
     * 切换fragment
     *
     * @param index
     * @param type
     * @param uid
     */
    private void switchFragment(int index, String type, String uid) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof MainfragmentActivity) {
            MainfragmentActivity fca = (MainfragmentActivity) getActivity();
            fca.setTabSelection(index, type, uid);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        android.util.Log.e("fei", "LeftFragment->onStart():name:");
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(scanReceiver);
        super.onDestroy();
    }
}
