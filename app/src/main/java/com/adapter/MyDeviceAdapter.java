package com.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.data.User;
import com.example.swipemenuview.SwipeMenuLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.MyDeviceItemActivity;
import com.massky.sraum.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by masskywcy on 2017-05-16.
 */

public class MyDeviceAdapter extends android.widget.BaseAdapter {
    private List<Map> list = new ArrayList<>();
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private int temp = -1;
    private Activity activity;//上下文
    private String accountType;
    private SlidingMenu mySlidingMenu;
    private DialogUtil dialogUtil;


    public MyDeviceAdapter(Activity context, List<Map> list, List<Integer> listint, List<Integer> listintwo,
                           SelectSensorListener selectSensorListener, String accountType, SlidingMenu mySlidingMenu,
                           DialogUtil dialogUtil) {
        this.list = list;
        this.listint = listint;
        this.listintwo = listintwo;
        this.activity = context;
        this.selectSensorListener = selectSensorListener;
        this.activity = context;
        this.accountType = accountType;
        this.mySlidingMenu = mySlidingMenu;
        this.dialogUtil = dialogUtil;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderContentType viewHolderContentType = null;
        if (null == convertView) {
            viewHolderContentType = new ViewHolderContentType();
            convertView = LayoutInflater.from(activity).inflate(R.layout.my_device_item, null);
            viewHolderContentType.img_guan_scene = (ImageView) convertView.findViewById(R.id.img_guan_scene);
            viewHolderContentType.panel_scene_name_txt = (TextView) convertView.findViewById(R.id.panel_scene_name_txt);
            viewHolderContentType.execute_scene_txt = (TextView) convertView.findViewById(R.id.execute_scene_txt);
            viewHolderContentType.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolderContentType.swipe_context = (RelativeLayout) convertView.findViewById(R.id.swipe_context);
            //gateway_name_txt
            viewHolderContentType.gateway_name_txt = (TextView) convertView.findViewById(R.id.gateway_name_txt);

            convertView.setTag(viewHolderContentType);
        } else {
            viewHolderContentType = (ViewHolderContentType) convertView.getTag();
        }


        viewHolderContentType.panel_scene_name_txt.setText((String) list.get(position).get("name"));
//        viewHolderContentType.gateway_name_txt.setText((String) list.get(position).get("boxName"));
        if (position < listint.size())
            viewHolderContentType.img_guan_scene.setImageResource(listint.get(position));

        ((SwipeMenuLayout) convertView).setAccountType(accountType);
        ((SwipeMenuLayout) convertView).setPosition(position);
        final View finalConvertView = convertView;
        ((SwipeMenuLayout) convertView).setOnMenuClickListener(new SwipeMenuLayout.OnMenuClickListener() {
            @Override
            public void onMenuClick(View v, int position) {
                //弹出删除框
                showCenterDeleteDialog(list.get(position).get("id").toString(),
                        list.get(position).get("name").toString(),
                        list.get(position).get("type").toString(), ((SwipeMenuLayout) finalConvertView));
            }

            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(activity, MyDeviceItemActivity.class);
                intent.putExtra("panelItem", (Serializable) list.get(position));
                intent.putExtra("imgtype", (Serializable) listint.get(position));
                activity.startActivity(intent);
            }

            @Override
            public void onInterceptTouch() {//和slidemenu争夺事件权限
                mySlidingMenu.setTouchModeAbove(
                        SlidingMenu.TOUCHMODE_NONE);
            }

            @Override
            public void onInterceptTouch_end() {
                mySlidingMenu.setTouchModeAbove(
                        SlidingMenu.TOUCHMODE_FULLSCREEN);
            }
        });

        return convertView;
    }

    //自定义dialog,centerDialog删除对话框
    public void showCenterDeleteDialog(final String panelNumber, final String name, final String type,
                                       final SwipeMenuLayout finalConvertView) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // 布局填充器
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view = inflater.inflate(R.layout.user_name_dialog, null);
//        // 设置自定义的对话框界面
//        builder.setView(view);
//
//        cus_dialog = builder.create();
//        cus_dialog.show();

        View view = LayoutInflater.from(activity).inflate(R.layout.promat_dialog, null);
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
        final Dialog dialog = new Dialog(activity, R.style.BottomDialog);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finalConvertView.closeMenu();
                    }
                });
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sraum_deletepanel(panelNumber, type, dialog, finalConvertView);
            }
        });
    }


    /**
     * 删除设备
     *
     * @param
     * @param dialog
     * @param finalConvertView
     */
    private void sraum_deletepanel(final String panelNumber, final String type, final Dialog dialog, final SwipeMenuLayout finalConvertView) {
        if (dialogUtil != null)
            dialogUtil.loadDialog();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", TokenUtil.getToken(activity));
        String send_method = "";
        switch (type) {
            case "AA02":
                map.put("number", panelNumber);
                send_method = ApiHelper.sraum_deleteWifiApple;
                break;//wifi模块
            default:
                map.put("boxNumber", TokenUtil.getBoxnumber(activity));
                map.put("panelNumber", panelNumber);
                send_method = ApiHelper.sraum_deletePanel;
                break;
        }
        MyOkHttp.postMapObject(send_method, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_deletepanel(panelNumber, type, dialog, finalConvertView);
                    }
                }, activity, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        dialog.dismiss();
                        if (selectSensorListener != null)
                            selectSensorListener.selectsensor();
                        switch (type) {
                            case "AA02":
                                SharedPreferencesUtil.saveInfo_List(activity, "remoteairlist",
                                        new ArrayList<Map>());
                                SharedPreferencesUtil.saveData(activity, "mGizWifiDevice", "");
                                break;
                        }
                    }

                    @Override
                    public void fourCode() {
                        dialog.dismiss();
                        ToastUtil.showToast(activity, "删除失败");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finalConvertView.closeMenu();
                            }
                        });
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }


    public void setLists(List<Map> list_hand_scene, List<Integer> listint, List<Integer> listintwo) {
        this.list = list_hand_scene;
        this.listint = listint;
        this.listintwo = listintwo;
    }

//    public static HashMap<Integer, Boolean> getIsSelected() {
//        return isSelected;
//    }
//
//    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
//        SelectSensorSingleAdapter.isSelected = isSelected;
//    }


    public static class ViewHolderContentType {
        public ImageView img_guan_scene;
        public TextView panel_scene_name_txt;
        public TextView execute_scene_txt;
        public CheckBox checkbox;
        public TextView gateway_name_txt;
        RelativeLayout swipe_context;
    }

    public SelectSensorListener selectSensorListener;

    public interface SelectSensorListener {
        void selectsensor();
    }
}
