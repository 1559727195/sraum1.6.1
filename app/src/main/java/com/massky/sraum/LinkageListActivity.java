package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.adapter.LinkageListAdapter;
import com.adapter.SelectSensorSingleAdapter;
import com.base.Basecactivity;
import com.data.User;
import com.xlistview.PullToRefreshLayout;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by zhu on 2018/6/13.
 */

public class LinkageListActivity extends Basecactivity implements
        AdapterView.OnItemClickListener,
        PullToRefreshLayout.OnRefreshListener {
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
    private DialogUtil dialogUtil;
    private LinkageListAdapter linkageListAdapter;
    private List<User.deviceLinkList> list = new ArrayList<>();

    @Override
    protected int viewId() {
        return R.layout.linkage_list_lay;
    }


    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        dialogUtil = new DialogUtil(this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        maclistview_id.setOnItemClickListener(this);
        refresh_view.setOnRefreshListener(this);
        linkageListAdapter = new LinkageListAdapter(LinkageListActivity.this, list, dialogUtil, refresh_view, new LinkageListAdapter.RefreshListener() {
            @Override
            public void refresh() {
                get_myDeviceLink();
                common_second();
            }
        });
        maclistview_id.setAdapter(linkageListAdapter);
        //成员，业主accountType->addrelative_id
        String accountType = (String) SharedPreferencesUtil.getData(LinkageListActivity.this, "accountType", "");
        switch (accountType) {
            case "1":
                next_step_txt.setVisibility(View.VISIBLE);
                break;//业主
            case "2":
              next_step_txt.setVisibility(View.GONE);
                break;//家庭成员
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_myDeviceLink();
        common_second();
    }


    /**
     * 清除联动信息
     */
    private void common_second() {
        SharedPreferencesUtil.saveData(LinkageListActivity.this, "linkId", "");
        SharedPreferencesUtil.saveInfo_List(LinkageListActivity.this, "list_result", new ArrayList<Map>());
        SharedPreferencesUtil.saveInfo_List(LinkageListActivity.this, "list_condition", new ArrayList<Map>());
        SharedPreferencesUtil.saveData(LinkageListActivity.this, "editlink", false);
        SharedPreferencesUtil.saveInfo_List(LinkageListActivity.this, "link_information_list", new ArrayList<Map>());
        SharedPreferencesUtil.saveData(LinkageListActivity.this, "add_condition", false);
    }


    /**
     * 获取我的设备联动
     */
    private void get_myDeviceLink() {
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(LinkageListActivity.this));
//        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(LinkageListActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_myDeviceLink, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen数据
                get_myDeviceLink();
            }
        }, LinkageListActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
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

                list.clear();
//                listtype.clear();
                list.addAll(user.deviceLinkList);
                linkageListAdapter.clear();
                linkageListAdapter.addAll(list);//不要new adapter
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                LinkageListActivity.this.finish();
                break;
            case R.id.next_step_txt:
                startActivity(new Intent(LinkageListActivity.this, SelectSensorActivity.class));
                break;
            case R.id.rel_scene_set:
//                startActivity(new Intent(SelectExecuteSceneResultActivity.this,
//                        ExcuteSomeHandSceneActivity.class));
                break;//执行某些手动场景
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        getOtherDevices("refresh");
        get_myDeviceLink();
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
