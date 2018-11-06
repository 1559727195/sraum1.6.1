package com.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseAdapter;
import com.data.User;
import com.massky.sraum.R;

import java.util.List;
import java.util.Map;

import static cn.ciaapp.sdk.CIAService.context;

/**
 * Created by masskywcy on 2016-09-06.
 */
//用于第一个界面gritview  adapter
public class MacFragAdapter extends android.widget.BaseAdapter {
//    private List<User.device> list;

    Context context;
    List<Map> list;

    public MacFragAdapter(Context context, List<Map> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder mHolder = null;
        if (null == convertView) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.macgrititem, null);

            // 根据列数计算项目宽度，以使总宽度尽量填充屏幕
            int itemWidth = (int) (context.getResources().getDisplayMetrics().widthPixels - 4 * 10) / 4;
            // Calculate the height by your scale rate, I just use itemWidth here
            // 下面根据比例计算您的item的高度，此处只是使用itemWidth
            int itemHeight = itemWidth;
            AbsListView.LayoutParams params = (AbsListView.LayoutParams) convertView.getLayoutParams();
            if (params == null) {
                params = new AbsListView.LayoutParams(
                        itemWidth / 10 * 9,
                        itemHeight / 10 * 9 + itemHeight / 10 * 3);
                convertView.setLayoutParams(params);
            } else {
                params.height = itemHeight;
                params.width = itemWidth;
            }

            mHolder.imageitem_id = (ImageView) convertView.findViewById(R.id.imageitem_id);
            mHolder.textitem_id = (TextView) convertView.findViewById(R.id.textitem_id);
            mHolder.itemrela_id = (RelativeLayout) convertView.findViewById(R.id.itemrela_id);
            //status_txt
            mHolder.status_txt = (TextView) convertView.findViewById(R.id.status_txt);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.textitem_id.setText(list.get(position).get("name").toString());
        //type：设备类型，1-灯，2-调光，3-空调，4-窗帘，5-新风，6-地暖
//        mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
        switch (list.get(position).get("type").toString()) {
            case "1":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.status_txt.setText("开");//#E2C891
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_quankai_40_active);
                } else {
                    mHolder.status_txt.setText("关");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_deng_40);
                }
                break;
            case "2":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_tiaoguang_40_active);
                    mHolder.status_txt.setText("开");//#E2C891
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_tiaoguang_40);
                    mHolder.status_txt.setText("关");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "3":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_kongtiao_40_active);
                    mHolder.status_txt.setText("开");//#E2C891
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_kongtiao_40);
                    mHolder.status_txt.setText("关");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }

                break;
            case "4":
                String curstatus = list.get(position).get("status").toString();
                if (curstatus.equals("1") || curstatus.equals("3") || curstatus.equals("4")
                        || curstatus.equals("4") || curstatus.equals("5") || curstatus.equals("8")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("开");//#E2C891
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_chuanglian_40_active);
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.status_txt.setText("关");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_chuanglian_40);
                }
                break;
            case "5":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("开");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.status_txt.setText("关");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                mHolder.imageitem_id.setImageResource(R.drawable.freshair);
                break;
            case "6":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("开");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.status_txt.setText("关");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                mHolder.imageitem_id.setImageResource(R.drawable.floorheating);
                break;


            case "7":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_menci_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("打开");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_menci_40);
                    mHolder.status_txt.setText("关闭");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "8":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_rentiganying_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("有人");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_rentiganying_40);
                    mHolder.status_txt.setText("正常");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }

                break;
            case "9":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_shuijin_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("报警");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_shuijin_40);
                    mHolder.status_txt.setText("正常");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }

                break;
            case "10":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_pm25_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
//                    mHolder.status_txt.setText("报警");//#E2C891
                    mHolder.status_txt.setText(list.get(position).get("dimmer").toString());//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_pm25_40);
//                    mHolder.status_txt.setText("正常");
                    mHolder.status_txt.setText(list.get(position).get("dimmer").toString());//#E2C891
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "11":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_jinjianniu_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("报警");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_jinjianniu_40);
                    mHolder.status_txt.setText("正常");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "12":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_rucebjq_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("报警");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_rucebjq_40);
                    mHolder.status_txt.setText("正常");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "13":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_yanwubjq_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("报警");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_yanwubjq_40);
                    mHolder.status_txt.setText("正常");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "14":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_ranqibjq_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("报警");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_ranqibjq_40);
                    mHolder.status_txt.setText("正常");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "15":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_zhinengmensuo_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("打开");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_zhinengmensuo_40);
                    mHolder.status_txt.setText("关闭");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "16":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_jixieshou_40);
                    mHolder.status_txt.setText("打开");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                } else {
                    //                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_jixieshou_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("关闭");//#E2C891
                }
                break;

            case "17":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_kaiguan_socket_40_active);
                    mHolder.status_txt.setText("开");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
//                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                } else {
                    //                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_kaiguan_socket_40);
                    mHolder.status_txt.setText("关");//#E2C891
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;


            case "202":
            case "206":
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_yaokongqi_40_active);
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setText("开");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_yaokongqi_40);
                    mHolder.status_txt.setText("关");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;
            case "101"://wifi摄像头
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_shexiangtou_40);
//                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                    mHolder.status_txt.setText("正常");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_shexiangtou_40);
                    mHolder.status_txt.setText("断线");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;

            case "103"://wifi摄像头
                if (list.get(position).get("status").toString().equals("1")) {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markstarh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_keshimenling_40);
//                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.zongse_color));
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                    mHolder.status_txt.setText("正常");//#E2C891
                } else {
//                    mHolder.itemrela_id.setBackgroundResource(R.drawable.markh);
                    mHolder.imageitem_id.setImageResource(R.drawable.icon_keshimenling_40);
                    mHolder.status_txt.setText("断线");
                    mHolder.status_txt.setTextColor(context.getResources().getColor(R.color.e30));
                }
                break;

            default:
                mHolder.imageitem_id.setImageResource(R.drawable.marklamph);
                break;
        }
        return convertView;
    }


    public void setList(List<Map> list) {
        this.list = list;
    }

    class ViewHolder {
        ImageView imageitem_id;
        TextView textitem_id;
        RelativeLayout itemrela_id;
        TextView status_txt;
    }
}
