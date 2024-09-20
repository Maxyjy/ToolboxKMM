package org.example.project.adb

import org.example.project.executeADB


/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/12
 */
object AdbExecutor {

    fun exec(adbCommand: String, callback: AdbExecuteCallback) {
        executeADB(adbCommand, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                callback.onPrint(line)
            }

            override fun onExit(exitCode: Int) {
                callback.onExit(exitCode)
            }
        })
    }

    fun findConnectedDeviceName(callback: (String) -> Unit) {
        exec(ADB_DEVICE_NAME, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                callback.invoke(line)
            }
        })
    }

    fun findConnectedDeviceBrand(callback: (String) -> Unit) {
        exec(ADB_DEVICE_BRAND, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                callback.invoke(line)
            }
        })
    }

    fun findActiveDisplayId(callback: (String?) -> Unit) {
        println("--- find active display id ---")
        executeADB(ADB_FIND_ACTIVE_DISPLAY, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                if (line.contains("(active)")) {
                    val regex = "Display (\\d+) \\(active\\)".toRegex()
                    val matchResult = regex.find(line)
                    val activeDisplayId = matchResult?.groupValues?.get(1)
                    println("activeDisplayId$activeDisplayId")
                    if (activeDisplayId?.isNotEmpty() == true) {
                        callback.invoke(activeDisplayId)
                    }
                }
            }

            override fun onExit(exitCode: Int) {
                println("exit code:$exitCode")
                if (exitCode != 0) {
                    callback.invoke(null)
                }
            }
        })
    }

    fun screenshot(callback: AdbExecuteCallback) {
        findActiveDisplayId { displayId ->
            if (!displayId.isNullOrEmpty() && displayId != "null") {
                val adb = ADB_SCREEN_SHOT.replace(DISPLAY_ID_HOLDER, displayId)
                exec(adb, callback)
            } else {
                callback.onPrint("display id is null, or no devices")
                callback.onExit(1)
            }
        }
    }

    fun startRecord(callback: AdbExecuteCallback) {
        findActiveDisplayId { displayId ->
            if (!displayId.isNullOrEmpty() && displayId != "null") {
                val adb = ADB_SCREEN_START_RECORD.replace(DISPLAY_ID_HOLDER, displayId)
                exec(adb, callback)
            } else {
                callback.onPrint("display id is null, or no devices")
                callback.onExit(1)
            }
        }
    }

    fun stopRecord(callback: AdbExecuteCallback) {
        exec(ADB_SCREEN_FIND_RECORD_PID, object : AdbExecuteCallback {
            override fun onPrint(line: String) {
                println("screen record pid $line")
                val screenRecorderPid = line.split(" ")
                screenRecorderPid.forEach {
                    println("screen record pid $it")
                    executeADB(
                        ADB_SCREEN_STOP_RECORD.replace(PID_HOLDER, it),
                        callback
                    )
                }

            }

            override fun onExit(exitCode: Int) {
                callback.onExit(exitCode)
            }
        })
    }

}