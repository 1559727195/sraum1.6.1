package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DbDevice;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MusicUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MacFragAdapter;
import com.adapter.SelectDeviceMessageAdapter;
import com.andview.refreshview.XRefreshView;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.massky.sraum.AirControlActivity;
import com.massky.sraum.LamplightActivity;
import com.massky.sraum.MacdetailActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.Pm25Activity;
import com.massky.sraum.R;
import com.massky.sraum.TVShowActivity;
import com.massky.sraum.WaterSensorActivity;
import com.xlistview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import static com.fragment.MessageFragment.MESSAGE_FRAGMENT;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于第一个fragment主界面*/
public class SystemMessageFragment extends Basecfragment implements
        AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {
    @InjectView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @InjectView(R.id.maclistview_id)
    ListView maclistview_id;
    private SelectDeviceMessageAdapter selectdevicemessageAdapter;
    private List<Map> list;
    private MessageReceiver mMessageReceiver;
    private String content = "";
    private View account_view;
    private DialogUtil dialogUtil;
    private LinearLayout all_read_linear;
    private LinearLayout all_select_linear;
    private LinearLayout delete_linear;


    @Override
    protected int viewId() {
        return R.layout.system_message_frag;
    }

    @Override
    public void onStart() {//onStart()-这个方法在屏幕唤醒时调用。
        super.onStart();

    }

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_FRAGMENT);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (content) {
            case "编辑":
                dialogUtil.loadViewBottomdialog();
                break;
            case "完成":

                break;
        }

        return true;
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(MESSAGE_FRAGMENT)) {
//                list.clear();
                content = (String) intent.getSerializableExtra("action");
//                switch (content) {
//                    case "编辑":
//                        list.clear();
//                        for (int i = 0; i < 5; i++) {
//                            Map map = new HashMap();
//                            map.put("name", "name11" + i);
//                            map.put("visable", true);
//                            list.add(map);
//                        }
//                        break;
//                    case "完成":
////                        list.clear();
////                        for (int i = 0; i < 5; i++) {
////                            Map map = new HashMap();
////                            map.put("name", "name11" + i);
////                            map.put("visable", false);
////                            list.add(map);
////                        }
////                        break;
////                }
////                selectdevicemessageAdapter.setList(list);
//                        break;
            }
        }
    }

    @Override

    protected void onView() {
        registerMessageReceiver();
        maclistview_id.setOnItemClickListener(this);
//        selectdevicemessageAdapter = new SelectDeviceMessageAdapter(getActivity(), list
//        );
//        maclistview_id.setAdapter(selectdevicemessageAdapter);
        refresh_view.setOnRefreshListener(this);
    }

    @Override
    public void initData() {//刷新数据，选择viewpager时刷新数据

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_read_linear:

                break;

            case R.id.all_select_linear:

                break;

            case R.id.delete_linear:

                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (content) {
            case "编辑":
                View v = parent.getChildAt(position - maclistview_id.getFirstVisiblePosition());
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
                cb.toggle();
//                selectdevicemessageAdapter.getIsSelected().put(position, cb.isChecked());
                break;
            case "完成":

                break;
        }
    }

    /**
     * 底部弹出拍照，相册弹出框
     */
    private void addViewid() {
        account_view = LayoutInflater.from(getActivity()).inflate(R.layout.message_function_select, null);
        all_read_linear = (LinearLayout) account_view.findViewById(R.id.all_read_linear);
        all_select_linear = (LinearLayout) account_view.findViewById(R.id.all_select_linear);
        delete_linear = (LinearLayout) account_view.findViewById(R.id.delete_linear);

        all_read_linear.setOnClickListener(this);
        all_select_linear.setOnClickListener(this);
        delete_linear.setOnClickListener(this);

//        //common_setting_image
//        common_setting_image = (ImageView) account_view.findViewById(R.id.common_setting_image);
        dialogUtil = new DialogUtil(getActivity(), account_view, 2);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("peng", "MacFragment->onResume:name:");
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mMessageReceiver);
    }
}
