package com.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.base.BaseAdapter;
import com.massky.sraum.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by masskywcy on 2017-05-16.
 */

public class SelectSensorAdapter extends BaseAdapter {
    private List<Boolean> list_bool = new ArrayList<>();
    private List<Map> list = new ArrayList<>();
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected = new HashMap<>();

    public SelectSensorAdapter(Context context, List<Map> list, List<Integer> listint, List<Integer> listintwo, List<Boolean> list_bool) {
        super(context, list);
        this.list = list;
        this.listint = listint;
        this.listintwo = listintwo;
        this.list_bool = list_bool;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list_bool.size(); i++) {
            getIsSelected().put(i, false);
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderContentType viewHolderContentType = null;
        if (null == convertView) {
            viewHolderContentType = new ViewHolderContentType();
            convertView = LayoutInflater.from(context).inflate(R.layout.select_sensor_item, null);
            viewHolderContentType.img_guan_scene = (ImageView) convertView.findViewById(R.id.img_guan_scene);
            viewHolderContentType.panel_scene_name_txt = (TextView) convertView.findViewById(R.id.panel_scene_name_txt);
            viewHolderContentType.execute_scene_txt = (TextView) convertView.findViewById(R.id.execute_scene_txt);
            viewHolderContentType.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(viewHolderContentType);
        } else {
            viewHolderContentType = (ViewHolderContentType) convertView.getTag();
        }


//        int element = (Integer) list.get(position).get("image");
//        viewHolderContentType.img_guan_scene.setImageResource(element);
//        viewHolderContentType.panel_scene_name_txt.setText(list.get(position).get("name").toString());


//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(context, DeviceExcuteOpenActivity.class);
////                context.startActivity(intent);
//
//
//            }
//        });
        viewHolderContentType.checkbox.setChecked(getIsSelected().get(position));
        if (getIsSelected().get(position)) {
            viewHolderContentType.img_guan_scene.setImageResource(listintwo.get(position));
        } else {
            viewHolderContentType.img_guan_scene.setImageResource(listint.get(position));
        }
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SelectSensorAdapter.isSelected = isSelected;
    }

    class ViewHolderContentType {
        ImageView img_guan_scene;
        TextView panel_scene_name_txt;
        TextView execute_scene_txt;
        CheckBox checkbox;
    }
}
