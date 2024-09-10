package org.example.project.constant

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/30
 */
val ADB = "adb"
val ADB_SHELL = "$ADB shell"
val ADB_DEVICE_LIST = "$ADB devices"
val ADB_DUMP_SYS = "$ADB_SHELL dumpsys"
val ADB_DEVICE_BRAND = "$ADB_SHELL getprop ro.product.brand"
val ADB_DEVICE_NAME = "$ADB_SHELL getprop ro.product.model"


val ADB_REBOOT = "$ADB reboot"

val ADB_KILL_APP = "$ADB "

val ADB_INSTALL = "$ADB "
val ADB_UNINSTALL = "$ADB "

//adb shell dumpsys meminfo com.hihonor.redteamobile.roaming
val ADB_DUMP_MEM_INFO = "$ADB_DUMP_SYS meminfo com.hihonor.redteamobile.roaming"
val ADB_SHOW_TOP_ACTIVITY = "$ADB_DUMP_SYS activity top | grep ACTIVITY"

//adb shell dumpsys activity top | grep ACTIVITY

//切换语言
//切换地区
//切换mcc
