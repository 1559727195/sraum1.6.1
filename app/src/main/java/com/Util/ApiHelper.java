package com.Util;

/**
 * Created by masskywcy on 2016-12-29.
 */
//sraum API的调用
public class ApiHelper {

    public static String api = "https://test.sraum.com/SmartHome/";//
//    public static String api = "https://app.sraum.com/SmartHome/";//正式：
    public static String sraum_register = api + "sraum_register";
    public static String sraum_getToken = api + "sraum_getToken";
    public static String sraum_checkMobilePhone = api + "sraum_checkMobilePhone";
    public static String sraum_login = api + "sraum_login";
    public static String sraum_updatePwd = api + "sraum_updatePwd";
    public static String sraum_getAllBox = api + "sraum_getAllBox";
    public static String sraum_getAllDevice2 = api + "sraum_getAllDevice2";
    public static String sraum_getAllDevice = api + "sraum_getAllDevice";
    public static String sraum_deviceControl = api + "sraum_deviceControl";
    public static String sraum_getBoxStatus = api + "sraum_getBoxStatus";
    public static String sraum_updateAvatar = api + "sraum_updateAvatar";
    public static String sraum_updateUserId = api + "sraum_updateUserId";
    public static String sraum_updateAccountInfo = api + "sraum_updateAccountInfo";
    public static String sraum_getAccountInfo = api + "sraum_getAccountInfo";
    public static String sraum_getFamily = api + "sraum_getFamily";
    public static String sraum_addBox = api + "sraum_addBox";
    public static String sraum_deleteBox = api + "sraum_deleteBox";
    public static String sraum_getBoxInfo = api + "sraum_getBoxInfo";
    public static String sraum_deleteFamily = api + "sraum_deleteFamily";
    public static String sraum_setPwd = api + "sraum_setPwd";
    public static String sraum_setBoxPwd = api + "sraum_setBoxPwd";
    public static String sraum_getVersion = api + "sraum_getVersion";
    public static String sraum_updateDeviceInfo = api + "sraum_updateDeviceInfo";
    public static String sraum_UpdateBoxInfo = api + "sraum_UpdateBoxInfo";
    public static String sraum_addFamily = api + "sraum_addFamily";
    public static String sraum_findDevice = api + "sraum_findDevice";
    public static String sraum_checkMemberPhone = api + "sraum_checkMemberPhone";
    public static String sraum_getAllScene = api + "sraum_getAllScene";
    public static String sraum_addScene = api + "sraum_addScene";
    public static String sraum_updateSceneName = api + "sraum_updateSceneName";
    public static String sraum_deleteScene = api + "sraum_deleteScene";
    public static String sraum_updateScene = api + "sraum_updateScene";
    public static String sraum_getAllPanel = api + "sraum_getAllPanel";
    public static String sraum_findPanel = api + "sraum_findPanel";
    public static String sraum_sceneControl = api + "sraum_sceneControl";
    public static String sraum_panelRelation = api + "sraum_panelRelation";
    public static String sraum_changeBox = api + "sraum_changeBox";
    public static String sraum_updatePanelName = api + "sraum_updatePanelName";
    public static String sraum_deletePanel = api + "sraum_deletePanel";
    public static String UpdateApkUrl = "https://app.sraum.com/version/";
    public static String sraum_isLogin = api + "sraum_isLogin";
    public static String sraum_setBox = api + "sraum_setBox";
    public static String sraum_getPanelDevices = api + "sraum_getPanelDevices";
    public static String sraum_verifySceneName = api + "sraum_verifySceneName";
    public static String sraum_getOtherDevices = api + "sraum_getOtherDevices";
    public static String sraum_getAllPanelNew = api + "sraum_getAllPanelNew";
    public static String sraum_getBoxVersion = api + "sraum_getBoxVersion";
    public static String sraum_updateBox = api + "sraum_updateBox";
    public static String sraum_myDeviceLink = api + "sraum_myDeviceLink";
    public static String sraum_setDeviceLinkIsUse = api + "sraum_setDeviceLinkIsUse";
    public static String sraum_updateDeviceLinkName = api + "sraum_updateDeviceLinkName";
    public static String sraum_deleteDeviceLink = api + "sraum_deleteDeviceLink";
    public static String sraum_getLinkSensor = api + "sraum_getLinkSensor";
    public static String sraum_getLinkController = api + "sraum_getLinkController";
    public static String sraum_getLinkScene = api + "sraum_getLinkScene";
    public static String sraum_setDeviceLink = api + "sraum_setDeviceLink";
    public static String sraum_deviceLinkInfo = api + "sraum_deviceLinkInfo";
    public static String sraum_updateDeviceLink = api + "sraum_updateDeviceLink";
    public static String sraum_getMessage = api + "sraum_getMessage";
    public static String sraum_setReadStatus = api + "sraum_setReadStatus";
    public static String sraum_deleteMessage = api + "sraum_deleteMessage";
    public static String sraum_getMessageById = api + "sraum_getMessageById";
    public static String sraum_getWifiAppleInfos = api + "sraum_getWifiAppleInfos";
    public static String sraum_addWifiApple = api + "sraum_addWifiApple";
    public static String sraum_addWifiAppleDevice = api + "sraum_addWifiAppleDevice";
    public static String sraum_getWifiAppleDeviceStatus = api + "sraum_getWifiAppleDeviceStatus";
    public static String sraum_setWifiAppleDeviceStatus = api + "sraum_setWifiAppleDeviceStatus";
    public static String sraum_updateWifiAppleName = api + "sraum_updateWifiAppleName";
    public static String sraum_deleteWifiApple = api + "sraum_deleteWifiApple";
    public static String sraum_getWifiAppleDeviceInfos = api + "sraum_getWifiAppleDeviceInfos";
    public static String sraum_deleteWifiAppleDevice = api + "sraum_deleteWifiAppleDevice";
    public static String sraum_updateWifiAppleDeviceName = api + "sraum_updateWifiAppleDeviceName";
    public static String sraum_getAllScene2 = api + "sraum_getAllScene2";
    public static String sraum_manualControl = api + "sraum_manualControl";
    public static String sraum_addWifiApple_WIFI = api + "sraum_uploadWifiAppleInfo";
    public static String sraum_getWifiApple_WIFI = api + "sraum_downloadWifiAppleInfo";
    public static String sraum_addWifiCamera = api + "sraum_addWifiCamera";
    public static String sraum_deleteWifiCamera = api + "sraum_deleteWifiCamera";
    public static String sraum_updateWifiCameraName = api + "sraum_updateWifiCameraName";
    public static String sraum_logout = api + "sraum_logout";
}
