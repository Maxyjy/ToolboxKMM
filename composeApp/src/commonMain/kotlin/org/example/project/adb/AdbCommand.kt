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
const val ADB_PATH_HOLDER = "{ADB_PATH_HOLDER}"
const val PID_HOLDER = "{PID_HOLDER}"

const val DISPLAY_ID_HOLDER = "{DISPLAY_ID_HOLDER}"

const val PUSH_SOURCE_PATH = "{PUSH_SOURCE_PATH}"
const val PUSH_TARGET_PATH = "{PUSH_TARGET_PATH}"

const val RENAME_OLD_FILE_PATH = "{RENAME_OLD_FILE_PATH}"
const val RENAME_NEW_FILE_PATH = "{RENAME_NEW_FILE_PATH}"

const val MCC_HOLDER = "{MCC_HOLDER}"
const val SPACE_HOLDER = "{SPACE_HOLDER}"


// root
const val ADB_ROOT = "adb${SPACE_HOLDER}root"
const val ADB_REMOUNT = "adb${SPACE_HOLDER}remount"

// device control
// ADB commands
const val ADB_REBOOT = "adb${SPACE_HOLDER}reboot"

// Device info
const val ADB_DEVICE_LIST = "adb${SPACE_HOLDER}devices"
const val ADB_DEVICE_BRAND =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}getprop${SPACE_HOLDER}ro.product.brand"
const val ADB_DEVICE_NAME =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}getprop${SPACE_HOLDER}ro.product.model"

// App control
const val ADB_KILL_APP =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}am${SPACE_HOLDER}force-stop${SPACE_HOLDER}${PACKAGE_NAME_HOLDER}"
const val ADB_START_APP =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}monkey${SPACE_HOLDER}-p${SPACE_HOLDER}${PACKAGE_NAME_HOLDER}${SPACE_HOLDER}-c${SPACE_HOLDER}android.intent.category.LAUNCHER${SPACE_HOLDER}1"

// App install/uninstall
const val ADB_INSTALL = "adb${SPACE_HOLDER}install${SPACE_HOLDER}${FILE_PATH_HOLDER}" // + apk path
const val ADB_UNINSTALL =
    "adb${SPACE_HOLDER}uninstall${SPACE_HOLDER}${PACKAGE_NAME_HOLDER}" // + package name

// Package manager commands
const val ADB_PRINT_PATH =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}pm${SPACE_HOLDER}list${SPACE_HOLDER}packages${SPACE_HOLDER}-f${SPACE_HOLDER}|${SPACE_HOLDER}grep${SPACE_HOLDER}${PACKAGE_NAME_HOLDER}" // + package name
const val ADB_CLEAR_DATA =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}pm${SPACE_HOLDER}clear${SPACE_HOLDER}${PACKAGE_NAME_HOLDER}" // + package name
const val ADB_THIRD_PARTY_APP =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}pm${SPACE_HOLDER}list${SPACE_HOLDER}packages${SPACE_HOLDER}-3"
const val ADB_FIND_PID_BY_PACKAGE_NAME =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}pidof${SPACE_HOLDER}${PACKAGE_NAME_HOLDER}"

const val ADB_PUSH =
    "adb${SPACE_HOLDER}push${SPACE_HOLDER}${PUSH_SOURCE_PATH}${SPACE_HOLDER}${PUSH_TARGET_PATH}"
const val ADB_RENAME =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}mv${SPACE_HOLDER}${RENAME_OLD_FILE_PATH}${SPACE_HOLDER}${RENAME_NEW_FILE_PATH}"

// ADB shell dumpsys commands
const val ADB_DUMP_MEM_INFO =
    "adb${SPACE_HOLDER}dumpsys${SPACE_HOLDER}meminfo${SPACE_HOLDER}${PACKAGE_NAME_HOLDER}"
const val ADB_DUMP_SHOW_TOP_ACTIVITY =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}dumpsys${SPACE_HOLDER}activity${SPACE_HOLDER}top${SPACE_HOLDER}|${SPACE_HOLDER}grep${SPACE_HOLDER}ACTIVITY"

// Screen shot commands
const val ADB_FIND_ACTIVE_DISPLAY =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}dumpsys${SPACE_HOLDER}SurfaceFlinger${SPACE_HOLDER}|${SPACE_HOLDER}grep${SPACE_HOLDER}\\(active\\)"
const val ADB_SCREEN_SHOT =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}screencap${SPACE_HOLDER}/sdcard/Pictures/Screenshots/screen_shot.png${SPACE_HOLDER}-d${SPACE_HOLDER}${DISPLAY_ID_HOLDER}"
const val ADB_SCREEN_START_RECORD =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}screenrecord${SPACE_HOLDER}/sdcard/Pictures/Screenshots/screen_record.mp4${SPACE_HOLDER}--d${SPACE_HOLDER}${DISPLAY_ID_HOLDER}"
const val ADB_SCREEN_FIND_RECORD_PID =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}pidof${SPACE_HOLDER}screenrecord"
const val ADB_SCREEN_STOP_RECORD =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}kill${SPACE_HOLDER}-2${SPACE_HOLDER}${PID_HOLDER}"

// Save screenshot and screen recording commands
const val ADB_SAVE_SCREEN_SHOT =
    "adb${SPACE_HOLDER}pull${SPACE_HOLDER}/sdcard/Pictures/Screenshots/screen_shot.png${SPACE_HOLDER}${DIR_PATH_HOLDER}"
const val ADB_SAVE_SCREEN_RECORD =
    "adb${SPACE_HOLDER}pull${SPACE_HOLDER}/sdcard/Pictures/Screenshots/screen_record.mp4${SPACE_HOLDER}${DIR_PATH_HOLDER}"

// Open language change setting command
const val ADB_OPEN_LANGUAGE_CHANGE_SETTING =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}am${SPACE_HOLDER}start${SPACE_HOLDER}-a${SPACE_HOLDER}android.settings.LOCALE_SETTINGS"

const val ADB_PATH_LIST_FILE = "adb${SPACE_HOLDER}shell${SPACE_HOLDER}ls${ADB_PATH_HOLDER}"

// Honor MCC level commands
const val ADB_HONOR_GET_MCC_ENABLE_OVERSEA =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}get${SPACE_HOLDER}global${SPACE_HOLDER}redtea_enable_succ_lev" // 开启境外套餐
const val ADB_HONOR_PUT_MCC_ENABLE_OVERSEA =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}put${SPACE_HOLDER}global${SPACE_HOLDER}redtea_enable_succ_lev${SPACE_HOLDER}1" // 开启境外套餐
const val ADB_HONOR_DELETE_MCC_ENABLE_OVERSEA =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}delete${SPACE_HOLDER}system${SPACE_HOLDER}redtea_enable_succ_lev" // 关闭境外套餐

const val ADB_HONOR_GET_MCC_LEVEL =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}get${SPACE_HOLDER}system${SPACE_HOLDER}redtea_mcc_lev" // 启用 mcc 测试
const val ADB_HONOR_PUT_MCC_LEVEL =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}put${SPACE_HOLDER}system${SPACE_HOLDER}redtea_mcc_lev${SPACE_HOLDER}2" // 启用 mcc 测试
const val ADB_HONOR_DELETE_MCC_LEVEL =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}delete${SPACE_HOLDER}system${SPACE_HOLDER}redtea_mcc_lev" // 清除 mcc 测试参数

// Honor MCC commands
const val ADB_HONOR_GET_MCC =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}get${SPACE_HOLDER}global${SPACE_HOLDER}redtea_mcc" // + mcc 设置 mcc
const val ADB_HONOR_PUT_MCC =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}settings${SPACE_HOLDER}put${SPACE_HOLDER}global${SPACE_HOLDER}redtea_mcc${SPACE_HOLDER}${MCC_HOLDER}" // + mcc 设置 mcc

const val ADB_HONOR_MCC_BROAD_CAST_SEND =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}am${SPACE_HOLDER}broadcast${SPACE_HOLDER}-a${SPACE_HOLDER}com.hihonor.general.vsim.action.VSIM_REG_PLMN_CHANGED${SPACE_HOLDER}--es${SPACE_HOLDER}mcc${SPACE_HOLDER}${MCC_HOLDER}" // + mcc 发送 mcc 广播


// Oppo MCC commands
const val ADB_OPPO_GET_MCC_1 =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}getprop${SPACE_HOLDER}android.telephony.mcc_change"
const val ADB_OPPO_PUT_MCC_1 =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}setprop${SPACE_HOLDER}android.telephony.mcc_change${SPACE_HOLDER}${MCC_HOLDER}"

const val ADB_OPPO_GET_MCC_2 =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}getprop${SPACE_HOLDER}android.telephony.mcc_change2"
const val ADB_OPPO_PUT_MCC_2 =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}setprop${SPACE_HOLDER}android.telephony.mcc_change2${SPACE_HOLDER}${MCC_HOLDER}"

const val ADB_OPPO_MCC_BROAD_CAST_SEND =
    "adb${SPACE_HOLDER}shell${SPACE_HOLDER}am${SPACE_HOLDER}broadcast${SPACE_HOLDER}-a${SPACE_HOLDER}android.telephony.action.mcc_change${SPACE_HOLDER}--es${SPACE_HOLDER}mcc${SPACE_HOLDER}${MCC_HOLDER}${SPACE_HOLDER}-n${SPACE_HOLDER}com.redteamobile.roaming/.receiver.MccChangeReceiver"

const val SCREEN_COPY = "scrcpy"