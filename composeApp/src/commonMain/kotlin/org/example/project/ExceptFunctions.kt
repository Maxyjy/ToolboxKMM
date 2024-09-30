package org.example.project

import kotlinx.coroutines.CoroutineScope
import org.example.project.adb.AdbExecuteCallback

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
expect fun getSystemName(): String

expect fun getSystemCurrentTimeMillis(): Long

expect fun formatTime(timeMillis: Long): String

expect fun unzipFile(filePath: String)

expect fun merge(logFiles: ArrayList<String>, mergeFilePath: String)

expect fun executeADB(adbCommand: String, callback: AdbExecuteCallback)

expect fun readFromFile(filePath: String, callback: (String) -> Unit)

expect fun filterOnlyRedTeaProcessId(
    processId: ArrayList<Int>,
    originalFilePath: String,
    filteredFilePath: String,
    getProcessIdFunction: (String) -> Int
)