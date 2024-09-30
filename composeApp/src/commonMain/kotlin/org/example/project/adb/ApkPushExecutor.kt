package org.example.project.adb

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.page.ADB_RESULT_FAILED
import org.example.project.page.ADB_RESULT_OK

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/23
 */
object ApkPushExecutor {

    const val HYPER_COMM_APK_NAME = "Hypercomm.apk"
    const val ROAMING_APK_NAME = "HnRoamingRed.apk"

    const val HYPER_COMM_APK_PATH = "/system/priv-app/HnSystemServer/"
    const val ROAMING_APK_PATH = "/product_h/region_comm/china/app/HnRoamingRed/"
    const val HONOR_SUFFIX = ".honor"


    fun root(callback: (Int) -> Unit) {
        AdbExecutor.exec(ADB_ROOT, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                if (line.contains("adbd is already running as root")) {
                    callback.invoke(ADB_RESULT_OK)
                } else if (line.contains("unable to connect for root") || line.contains("adbd cannot run as root in production builds")) {
                    callback.invoke(ADB_RESULT_FAILED)
                }
            }
        })
    }

    fun remount(callback: (Int) -> Unit) {
        AdbExecutor.exec(ADB_REMOUNT, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                if (line.contains("Remount succeeded") || line.contains("overlay remount has been done,")) {
                    callback.invoke(ADB_RESULT_OK)
                } else if (line.contains("Not running as root.")) {
                    callback.invoke(ADB_RESULT_FAILED)
                }
            }
        })
    }

    fun rename(oldFilePath: String, newFilePath: String, callback: (Int) -> Unit) {
        var adbCommand = ADB_RENAME
        adbCommand = adbCommand.replace(RENAME_OLD_FILE_PATH, oldFilePath)
        adbCommand = adbCommand.replace(RENAME_NEW_FILE_PATH, newFilePath)
        AdbExecutor.exec(adbCommand, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                if (line.contains("No such file or directory")) {
                    callback.invoke(ADB_RESULT_OK)
                }
            }

            override fun onExit(exitCode: Int) {
                if (exitCode == 0) {
                    callback.invoke(ADB_RESULT_OK)
                }
            }
        })
    }

    fun push(sourceFilePath: String, targetFilePath: String, callback: (Int) -> Unit) {
        var adbCommand = ADB_PUSH
        adbCommand = adbCommand.replace(PUSH_SOURCE_PATH, sourceFilePath)
        adbCommand = adbCommand.replace(PUSH_TARGET_PATH, targetFilePath)
        AdbExecutor.exec(adbCommand, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                if (line.contains("file pushed")) {
                    callback.invoke(ADB_RESULT_OK)
                } else if (line.contains("error")) {
                    callback.invoke(ADB_RESULT_FAILED)
                }
            }

            override fun onExit(exitCode: Int) {
                println("Push" + exitCode)
            }
        })
    }

    fun remove(filePath: String, callback: (Int) -> Unit) {
        var adbCommand = ADB_REMOVE
        adbCommand = adbCommand.replace(REMOVE_TARGET_PATH, filePath)
        AdbExecutor.exec(adbCommand, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                if (line.contains("No such file or directory")) {
                    callback.invoke(ADB_RESULT_FAILED)
                }
            }

            override fun onExit(exitCode: Int) {
                println("remove" + exitCode)
            }
        })
    }


}