package com.Util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceType;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhu on 2018/8/21.
 */

public class EntityUtils {
    //（一定要绑定，订阅成功才能进行对象转换）。
    /**
     * 实体类转Map
     *
     * @param object
     * @return
     */
    public static Map<String, Object> entityToMap(Parcelable object) {
//        stu.writeToParcel(p, 0);
        Map<String, Object> map = new HashMap();
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    /**
     * 实体类转Map
     *
     * @param
     * @return
     */
    public static Map<String, Object> filterMap(Map<String, Object> map_filter) {
        Map map = new HashMap();
        map.put("macAddress", map_filter.get("macAddress"));
        map.put("did", map_filter.get("did"));
        map.put("ipAddress", map_filter.get("ipAddress"));
        map.put("productKey", map_filter.get("productKey"));
        map.put("productName", map_filter.get("productName"));
        map.put("remark", map_filter.get("remark"));
        map.put("alias", map_filter.get("alias"));
        map.put("productUI", map_filter.get("productUI"));
//        String isLAN = map_filter.get("productUI");
        map.put("isLAN", map_filter.get("isLAN"));//(byte)((boolean)map_filter.get("productUI")?1:0)
        map.put("subscribed", map_filter.get("subscribed"));
        map.put("isBind", map_filter.get("isBind"));
        map.put("hasProductDefine", map_filter.get("hasProductDefine"));
        map.put("isDisabled", map_filter.get("isDisabled"));
        map.put("netStatus", map_filter.get("netStatus"));
        map.put("productType", map_filter.get("productType"));
        return map;
    }


    /**
     * Map转实体类
     *
     * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @return
     */
    public static <T> T mapToEntity(Map<String, Object> map, Parcelable.Creator<T> creator) {

//        Parcel t = null;
        Parcel parcel = null;
        parcel = Parcel.obtain();
//            parcel.writeMap(map);
        parcel.setDataPosition(0); // this is extremely important!
//        parcel.writeMap(map);
        parcel.writeString((String) map.get("macAddress"));
        parcel.writeString((String) map.get("did"));
        parcel.writeString((String) map.get("ipAddress"));
        parcel.writeString((String) map.get("productKey"));
        parcel.writeString((String) map.get("productName"));
        parcel.writeString((String) map.get("remark"));
        parcel.writeString((String) map.get("alias"));
        parcel.writeString((String) map.get("productUI"));
        parcel.writeByte((byte) ((boolean) map.get("isLAN") ? 1 : 0));
        parcel.writeByte((byte) ((boolean) map.get("subscribed") ? 1 : 0));
        parcel.writeByte((byte) ((boolean) map.get("isBind") ? 1 : 0));
        parcel.writeByte((byte) ((boolean) map.get("hasProductDefine") ? 1 : 0));
        parcel.writeByte((byte) ((boolean) map.get("isDisabled") ? 1 : 0));

//        parcel.writeSerializable((GizWifiDeviceNetStatus) map.get("netStatus"));
//        parcel.writeSerializable((GizWifiDeviceType) map.get("productType"));
        switch ((String) map.get("netStatus")) {
            case "GizDeviceOffline":
                parcel.writeSerializable(GizWifiDeviceNetStatus.GizDeviceOffline);
                break;
            case "GizDeviceOnline":
                parcel.writeSerializable(GizWifiDeviceNetStatus.GizDeviceOnline);
                break;
            case "GizDeviceControlled":
                parcel.writeSerializable(GizWifiDeviceNetStatus.GizDeviceControlled);
                break;
            case "GizDeviceUnavailable":
                parcel.writeSerializable(GizWifiDeviceNetStatus.GizDeviceUnavailable);
                break;
        }

        switch ((String) map.get("productType")) {
            case "GizDeviceNormal":
                parcel.writeSerializable(GizWifiDeviceType.GizDeviceNormal);
                break;
            case "GizDeviceCenterControl":
                parcel.writeSerializable(GizWifiDeviceType.GizDeviceCenterControl);
                break;
            case "GizDeviceSub":
                parcel.writeSerializable(GizWifiDeviceType.GizDeviceSub);
                break;
        }

        parcel.setDataPosition(0);
//        String mac = parcel.readString();
//        String did = parcel.readString();
//        String productKey = parcel.readString();
//        String productName = parcel.readString();
//        String remark = parcel.readString();
        return creator.createFromParcel(parcel);
    }


//        T t = null;
//
//        Parcel parcel = Parcel.obtain();
////        parcel.writeMap(map);writeToParcel
//        writeToParcel
//        final Bundle bundle = new Bundle();
//        final Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
//        while (iter.hasNext()) {
//            final Map.Entry<String, Object> entry = iter.next();
////            bundle.putString(entry.getKey(), entry.getValue());
//            Object o = entry.getValue();
//            if (o == null) {
//                bundle.putString(entry.getKey(), "");
//            } else {
//                String type = o.getClass().getSimpleName();
//                if ("Integer".equals(type)) {
//                    bundle.putInt(entry.getKey(), (Integer) entry.getValue());
//                } else if ("Boolean".equals(type)) {
//                    bundle.putBoolean(entry.getKey(), (Boolean) entry.getValue());
//                } else if ("String".equals(type)) {
//                    bundle.putString(entry.getKey(), (String) entry.getValue());
//                } else if ("Float".equals(type)) {
//                    bundle.putFloat(entry.getKey(), (Float) entry.getValue());
//                } else if ("Long".equals(type)) {
//                    bundle.putLong(entry.getKey(), (Long) entry.getValue());
//                }
//            }
//        }
//        parcel.writeBundle(bundle);
//        parcel.recycle();
//        return GizWifiDevice.CREATOR.createFromParcel(parcel);


//            t = entity.newInstance();


//            for(Field field : entity.getDeclaredFields()) {
//                if (map.containsKey(field.getName())) {
//                    boolean flag = field.isAccessible();
//                    field.setAccessible(true);
//                    Object object = map.get(field.getName());
//                    if (object!= null && field.getType().isAssignableFrom(object.getClass())) {
//                        field.set(t, object);
//                    }
//                    field.setAccessible(flag);
//                }
//            }


}
