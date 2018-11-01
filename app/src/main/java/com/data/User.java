package com.data;

import com.Util.TokenUtil;
import com.massky.sraum.EditLinkDeviceResultActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by masskywcy on 2017-01-04.
 */
public class User {
    public String userName;
    public String result;
    public String token;
    public String expires_in;
    public String accountType;
    public String avatar;
    public String status;
    public String versionCode;
    public String version;
    public List<box> boxList;
    public userinfo userInfo;
    public List<device> deviceList;
    public List<familylist> familyList;
    public boxinfor boxInfo;
    public List<scenelist> sceneList;
    public List<panellist> panelList;
    public String type;
    public String panelType;
    public String panelStatus;
    public String panelName;
    public String panelMAC;
    public String deviceName;
    public String messageTitle;
    public String eventTime;
    public String number;
    public String dimmer;
    public String mode;
    public String temperature;
    public String speed;
    public String UDScavenging;
    public String LRScavenging;

    /**
     * WIFI->code 即WIFI红外模块编码
     */
    public String code;
    public String deviceInfo;

    public User() {

    }

    /*
    * id：面板编号
    mac：面板 MAC 地址
    name：面板名称
    type：面板类型
    status：面板状态 1-在线，0-离线*/
    public static class panellist {
        public String id;
        public String mac;
        public String name;
        public String type;
        public String status;
        public String buttonStatus;
        public String button5Name;
        public String button5Type;
        public String button6Name;
        public String button6Type;
        public String button7Name;
        public String button7Type;
        public String button8Name;
        public String button8Type;
        public String panelNumber;
        public String panelName;
        public String panelType;
        public String boxNumber;
        public String boxName;
        public String firmware;
        public String hardware;
        public String gatewayid;
    }

    //gatewayid面板id panelType面板类型
    public static class scenelist {
        public String type;
        public String name;
        public String sceneStatus;
        public String gatewayid;
        public String panelType;
        public String status;
        public String id;
        public String panelNumber;
        public String buttonNumber;
        public List<devicesce> deviceList;
        public String sceneId;
        public String sceneName;
        public String sceneType;
        public String boxNumber;
        public String boxName;

    }

    /*
    * 下载全部网关
    * type：网关类型，a001-单片机网关
    number：网关的编号（唯一标识）
    name：自定义名称
    status：状态，1-在线，0-离线
    sign：使用标记，0-未使用，1-使用中*/
    public static class box {
        public String type;
        public String number;
        public String name;
        public String status;
        public String sign;
        public String mac;
        public String firmware;
        public String hardware;
    }

    /*下载全部智能设备
    * type：设备类型，1-灯，2-调光灯，3-空调，4-窗帘，5-新风，6-地暖
    number：设备编号
    name：自定义设备名称
    status：状态，1-开，0-关（若为窗帘，则 1-全开，0-全关，2-暂停，3-组 1 开
    组 2 开，4-组 1 开组 2 关，5-组 1 关组 2 开，6-组 1 关组 2 关）
    dimmer：调光值，范围 0-100
    mode：空调模式，1-制冷，2-制热，3-除湿，4-自动，5-通风
    temperature：空调或新风或地暖的温度，范围 16-30
    speed：空调或新风或地暖的风速，1-低风，2-中风，3-高风，4-强力，5-送风，
    6-自动*/
    public static class device implements Serializable {
        //根据面板id-》去查找设备列表
        public String type;
        public String number;
        public String name;
        public String deviceId;
        public String status;
        public String mode;
        public String dimmer;
        public String temperature;
        public String speed;
        public String name1;
        public String name2;
        public boolean flag;
        public String panelName;
        public String boxNumber;
        public String boxName;

    }


    /*下载全部智能设备
   * type：设备类型，1-灯，2-调光灯，3-空调，4-窗帘，5-新风，6-地暖
   number：设备编号
   name：自定义设备名称
   status：状态，1-开，0-关（若为窗帘，则 1-全开，0-全关，2-暂停，3-组 1 开
   组 2 开，4-组 1 开组 2 关，5-组 1 关组 2 开，6-组 1 关组 2 关）
   dimmer：调光值，范围 0-100
   mode：空调模式，1-制冷，2-制热，3-除湿，4-自动，5-通风
   temperature：空调或新风或地暖的温度，范围 16-30
   speed：空调或新风或地暖的风速，1-低风，2-中风，3-高风，4-强力，5-送风，
   6-自动*/
    public static class devicesce implements Serializable {
        public String type;
        public String number;
        public String status;
        public String mode;
        public String dimmer;
        public String temperature;
        public String speed;
    }


    /*获得个人信息
    * customName：自定义用户名
        nickName：昵称
        avatar：头像
        gender：性别
        birthDay：生日
        mobilePhone：手机号
        family：家庭成员数量*/
    public static class userinfo {
        public String userId;
        public String nickname;
        public String avatar;
        public String gender;
        public String mode;
        public String birthDay;
        public String mobilePhone;
        public String family;
    }

    //家人列表
    public static class familylist {
        public String name;
        public String mobilePhone;
        public String userid1;
        public String userid2;
    }


    /*
    * 网关详情
    * mac：网关 mac
    name：网关名称
    model：型号
    firmware：固件版本
    hardware：硬件版本
    system：系统版本
    status：状态，1-在线，0-离线*/
    public static class boxinfor {
        public String mac;
        public String name;
        public String model;
        public String type;
        public String firmware;
        public String hardware;
        public String system;
        public String stauts;
    }

    public List<deviceList> deviceLists;

    /**
     * 获取门磁等第三方设备
     */
    public static class deviceList {
        public String name;
        public String number;
        public String type;
        public String status;
        public String mode;
        public String dimmer;
        public String temperature;
        public String speed;
        public String boxNumber;
        public String boxName;
    }

    /**
     * 获取网关版本号
     */
    public String currentVersion;
    public String newVersion;

    /**
     * 获取我的设备联动
     */

    public List<deviceLinkList> deviceLinkList;

    public static class deviceLinkList {
        public String id;
        public String name;
        public String isUse;
        public String type;
    }

    /**
     * 设备联动信息
     */
    public deviceLinkInfo deviceLinkInfo;

    public static class deviceLinkInfo {
        public String token;
        public String deviceId;
        public String deviceType;
        public String linkName;
        public String condition;
        public String minValue;
        public String maxValue;
        public String startTime;
        public String endTime;
        public List<deviceList> deviceList;
        public String deviceName;
        public String type;
        public String boxName;
    }

    /**
     * 分页获取消息列表
     */

    public List<messageList> messageList;

    public static class messageList implements Serializable {
        public String id;
        public String messageType;
        public String messageTitle;
        public String deviceName;
        public String readStatus;
        public String eventTime;
    }


    /**
     * 获取 wifi 红外转发设备列表
     */

    public List<controllerList> controllerList;

    public static class controllerList
            implements Serializable {
        public String type;
        public String name;
        public String number;
        public String controllerId;
    }


    public List<wifi_device> wifiList;//wifi设备列表

    /**
     * WIFI设备
     */
    public static class wifi_device implements Serializable {
        //根据面板id-》去查找设备列表
        public String type;
        public String number;
        public String name;
        public String status;
        public String mode;
        public String dimmer;
        public String temperature;
        public String speed;
        public String mac;
        public String deviceId;

        public String id;
        public String controllerId;
        public String wifi;
    }

    //manualList
    /**
     * 手动云场景
     */
    public List<manua> manualList;//wifi设备列表

    /**
     * WIFI设备
     */
    public static class manua implements Serializable {
        //根据面板id-》去查找设备列表
        public String type;
        public String sceneStatus;
        public String name;
    }
}
