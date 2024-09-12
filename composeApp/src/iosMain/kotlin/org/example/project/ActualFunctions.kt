package org.example.project

import org.example.project.adb.AdbExecuteCallback

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
actual fun getSystemCurrentTimeMillis(): Long {
    return 0L
}

actual fun formatTime(timeMillis: Long): String {
    return ""
}

actual fun unzipFile(filePath: String) {}

actual fun executeADB(adbCommand: String, callback: AdbExecuteCallback){

}

actual fun merge(logFiles: ArrayList<String>, mergeFilePath: String) {}

actual fun readFromFile(filePath: String, callback: (String) -> Unit) {}

actual fun filterOnlyRedTeaProcessId(
    processId: ArrayList<Int>,
    originalFilePath: String,
    filteredFilePath: String,
    getProcessIdFunction: (String) -> Int
) {
}