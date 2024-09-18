package org.example.project.adb

/**
 * adb cheat sheet
 *
 * @author YangJianyu
 * @date 2024/8/30
 */
const val PACKAGE_NAME_HOLDER = "{PACKAGE_NAME_HOLDER}"
const val FILE_PATH_HOLDER = "{FILE_NAME_HOLDER}"
const val DIR_PATH_HOLDER = "{DIR_NAME_HOLDER}"
const val PID_HOLDER = "{PID_HOLDER}"
const val DISPLAY_ID_HOLDER = "{DISPLAY_ID_HOLDER}"
const val MCC_HOLDER = "{MCC_HOLDER}"

// root
const val ADB_ROOT = "adb root"
const val ADB_REMOUNT = "adb remount"

// device control
const val ADB_REBOOT = "adb reboot"

// device info
const val ADB_DEVICE_LIST = "adb devices"
const val ADB_DEVICE_BRAND = "adb shell getprop ro.product.brand"
const val ADB_DEVICE_NAME = "adb shell getprop ro.product.model"

// app control
const val ADB_KILL_APP = "adb shell am force-stop $PACKAGE_NAME_HOLDER"
const val ADB_START_APP =
    "adb shell monkey -p $PACKAGE_NAME_HOLDER -c android.intent.category.LAUNCHER 1"

// app install uninstall
const val ADB_INSTALL = "adb install $FILE_PATH_HOLDER" // + apk path
const val ADB_UNINSTALL = "adb uninstall $PACKAGE_NAME_HOLDER" // + package name

// package manager
const val ADB_PRINT_PATH =
    "adb shell pm list packages -f | grep $PACKAGE_NAME_HOLDER" // + package name
const val ADB_CLEAR_DATA = "adb shell pm clear $PACKAGE_NAME_HOLDER" // + package name
const val ADB_THIRD_PARTY_APP = "adb shell pm list packages -3"
const val ADB_FIND_PID_BY_PACKAGE_NAME = "adb shell pidof $PACKAGE_NAME_HOLDER"

//adb shell dumpsys meminfo
const val ADB_DUMP_MEM_INFO = "adb dumpsys meminfo $PACKAGE_NAME_HOLDER"
const val ADB_DUMP_SHOW_TOP_ACTIVITY = "adb shell dumpsys activity top | grep ACTIVITY"

// screen shot
const val ADB_FIND_ACTIVE_DISPLAY = "adb shell dumpsys SurfaceFlinger | grep \\(active\\)"
const val ADB_SCREEN_SHOT =
    "adb shell screencap /sdcard/Pictures/Screenshots/screen_shot.png -d $DISPLAY_ID_HOLDER"
const val ADB_SCREEN_START_RECORD =
    "adb shell screenrecord /sdcard/Pictures/Screenshots/screen_shot.mp4 --d $DISPLAY_ID_HOLDER"
const val ADB_SCREEN_FIND_RECORD_PID = "adb shell pidof screenrecord"
const val ADB_SCREEN_STOP_RECORD = "adb shell kill -2 $PID_HOLDER"

const val ADB_SAVE_SCREEN_SHOT =
    "adb pull /sdcard/Pictures/Screenshots/screen_shot.png $DIR_PATH_HOLDER"
const val ADB_SAVE_SCREEN_RECORD =
    "adb pull /sdcard/Pictures/Screenshots/screen_shot.mp4 $DIR_PATH_HOLDER"

const val ADB_OPEN_LANGUAGE_CHANGE_SETTING =
    "adb shell am start -a android.settings.LOCALE_SETTINGS"

// honor preinstall apk, Hypercomm apk
// honor mcc
const val ADB_HONOR_GET_MCC_ENABLE_OVERSEA =
    "adb shell settings get global redtea_enable_succ_lev" // 开启境外套餐
const val ADB_HONOR_PUT_MCC_ENABLE_OVERSEA =
    "adb shell settings put global redtea_enable_succ_lev 1" // 开启境外套餐
const val ADB_HONOR_DELETE_MCC_ENABLE_OVERSEA =
    "adb shell settings delete system redtea_enable_succ_lev" // 关闭境外套餐？

const val ADB_HONOR_GET_MCC_LEVEL = "adb shell settings get system redtea_mcc_lev" // 启用 mcc 测试
const val ADB_HONOR_PUT_MCC_LEVEL = "adb shell settings put system redtea_mcc_lev 2" // 启用 mcc 测试
const val ADB_HONOR_DELETE_MCC_LEVEL =
    "adb shell settings delete system redtea_mcc_lev" // 清除 mcc 测试参数

const val ADB_HONOR_GET_MCC =
    "adb shell settings get global redtea_mcc" // + mcc 设置 mcc
const val ADB_HONOR_PUT_MCC =
    "adb shell settings put global redtea_mcc $MCC_HOLDER" // + mcc 设置 mcc

const val ADB_HONOR_MCC_BROAD_CAST_SEND =
    "adb shell am broadcast -a com.hihonor.general.vsim.action.VSIM_REG_PLMN_CHANGED --es mcc $MCC_HOLDER"  // + mcc 发送 mcc 广播

// oppo mcc
const val ADB_OPPO_GET_MCC_1 = "adb shell getprop android.telephony.mcc_change"
const val ADB_OPPO_PUT_MCC_1 = "adb shell setprop android.telephony.mcc_change $MCC_HOLDER"

const val ADB_OPPO_GET_MCC_2 = "adb shell getprop android.telephony.mcc_change2"
const val ADB_OPPO_PUT_MCC_2 = "adb shell setprop android.telephony.mcc_change2 $MCC_HOLDER"

const val ADB_OPPO_MCC_BROAD_CAST_SEND =
    "adb shell am broadcast -a android.telephony.action.mcc_change --es mcc $MCC_HOLDER -n com.redteamobile.roaming/.receiver.MccChangeReceiver"

const val SCREEN_COPY = "scrcpy"