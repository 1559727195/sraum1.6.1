package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.SelectDeviceMessageAdapter;
import com.base.Basecfragment;
import com.data.User;
import com.massky.sraum.MessageDetailActivity;
import com.massky.sraum.R;
import com.xlistview.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.fragment.MessageFragment.MESSAGE_FRAGMENT;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于第一个fragment主界面*/
public class IntelligentMatchFragment extends Basecfragment{


    @Override
    protected int viewId() {
        return R.layout.intelligent_match_fragment;
    }

    @Override
    public void onStart() {//onStart()-这个方法在屏幕唤醒时调用。
        super.onStart();

    }



    @Override

    protected void onView() {

    }

    @Override
    public void initData() {//刷新数据，选择viewpager时刷新数据
//        get_message(false, "doit");
//        if(onDeviceMessageFragListener1 != null)
//        onDeviceMessageFragListener1.ondevice_message_frag();
    }

    @Override
    public void onClick(View v) {

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.e("peng", "MacFragment->onResume:name:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public static IntelligentMatchFragment newInstance(OnDeviceMessageFragListener onDeviceMessageFragListener) {
        IntelligentMatchFragment newFragment = new IntelligentMatchFragment();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public interface OnDeviceMessageFragListener {
        void ondevice_message_frag();
    }

}
