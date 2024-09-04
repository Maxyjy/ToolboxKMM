package org.example.project.constant

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/30
 */
val ADB_DEVICE_LIST = arrayOf("adb", "devices")

val ADB_REBOOT = arrayOf("adb", "reboot")

val ADB_KILL_APP = arrayOf("adb", "reboot")

val ADB_INSTALL = arrayOf("adb", "install")
val ADB_UNINSTALL = arrayOf("adb", "uninstall")

//adb shell dumpsys meminfo com.hihonor.redteamobile.roaming
val ADB_DUMP_MEM_INFO = arrayOf("adb", "shell", "dumpsys", "meminfo", "com.hihonor.redteamobile.roaming")
