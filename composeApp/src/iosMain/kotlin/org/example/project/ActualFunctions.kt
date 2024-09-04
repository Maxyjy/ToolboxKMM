package org.example.project

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
actual fun unzipFile(filePath: String) {}

actual fun executeADB(adbCommand: Array<String>, callback: (String) -> Unit) {}

actual fun merge(logFiles: ArrayList<String>, mergeFilePath: String) {}

actual fun readFromFile(filePath: String, callback: (String) -> Unit) {}

actual fun filterOnlyRedTeaProcessId(
    processId: ArrayList<Int>,
    originalFilePath: String,
    filteredFilePath: String,
    getProcessIdFunction: (String) -> Int
) {
}